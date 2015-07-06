package it.prisma.businesslayer.bizws.paas.services.dbaas;

import it.prisma.businesslayer.bizlib.accounting.PrismaUserDetails;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.infrastructure.AppRepoLookupInfo;
import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean;
import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean.AppRepoEndpoint;
import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestrator;
import it.prisma.businesslayer.bizlib.paas.services.PaaSServiceHelper;
import it.prisma.businesslayer.bizlib.paas.services.dbaas.DBaaSMgmtBean;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.ComputeOpenstackAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.MainOpenstackAPIClientImpl;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.OpenstackAPIErrorException;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.OpenstackEndpointType;
import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.bizws.orchestrator.OrchestratorMainWS;
import it.prisma.businesslayer.bizws.paas.services.PaaSServiceGenericWSImpl;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;
import it.prisma.dal.entities.paas.services.dbaas.DBaaS;
import it.prisma.domain.dsl.iaas.openstack.identity.response.Endpoint;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponse;
import it.prisma.utils.misc.polling.AbstractPollingBehaviour;
import it.prisma.utils.misc.polling.Poller;
import it.prisma.utils.misc.polling.PollingException;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

public class DBaaSWSImpl extends
		PaaSServiceGenericWSImpl<DBaaSRepresentation, DBaaSDeployRequest>
		implements DBaaSWS {

	private static Logger LOG = LogManager.getLogger(DBaaSWSImpl.class);

	@Inject
	protected DBaaSMgmtBean dbaasMgmt;

	@Inject
	protected MappingHelper<DBaaS, DBaaSRepresentation> dbaasMH;

	@Inject
	protected MappingHelper<PaaSServiceEvent, PaaSServiceEventRepresentation> paaSServiceEventMH;

	@Inject
	protected EnvironmentConfig envConfig;

	@Inject
	protected PaaSServiceHelper paasSvcHelper;

	@Inject
	protected PaaSServiceDAO paasSvcDAO;

	@Inject
	protected OrchestratorMainWS orcWS;
	
	@Inject
	private InfrastructureManagerBean infrastructureBean;
	

	@Override
	public Collection<DBaaSRepresentation> getResourcesByWorkgroup(
			Long workgroupId, String considerDeletedToo) {
		try {
			try {
				Collection<DBaaS> result = dbaasMgmt
						.getDBaaSByUserAndWorkgroup(workgroupId, null,
								Boolean.parseBoolean(considerDeletedToo));

				return dbaasMH.getDSL(result);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e.getMessage(), e);
		}
	}

	@Override
	public DBaaSRepresentation getResourceById(Long workgroupId, Long appId) {
		// TODO Authentication !
		try {
			try {
				DBaaS result = dbaasMgmt.getDBaaSById(appId);

				if (result == null)
					throw new ResourceNotFoundException(
							DBaaSRepresentation.class, "workgroupId="
									+ workgroupId, "id=" + appId);

				return dbaasMH.getDSL(result);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e.getMessage(), e);
		}

	}

	@Override
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<PaaSServiceEventRepresentation> getResourceEvents(
			HttpServletRequest request, Long workgroupId, Long envId) {
		try {

			Collection<PaaSServiceEvent> events = dbaasMgmt.getDBaaSEvents(
					envId, 0L, Long.MAX_VALUE, 1000L);

			// return handleResult(paaSServiceEventMH.getDSL(events));
			return paaSServiceEventMH.getDSL(events);
		} catch (Exception e) {
			// return handleGenericException(e);
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Response stopService(HttpServletRequest request, Long workgroupId,
			Long resId) {

		PaaSService paasSvc = null;
		try {
			try {
				DBaaS dbaas = dbaasMgmt.getDBaaSById(resId);

				if (dbaas == null)
					return handleNotFoundException(
							OrchestratorErrorCode.ORC_ITEM_NOT_FOUND, "");

				paasSvc = dbaas.getPaaSService();

				// TODO Improve
				String vmId = dbaas.getPaaSService().getPaaSDeployment()
						.getPaaSDeploymentServices().iterator().next()
						.getPaaSDeploymentServiceInstances().iterator().next()
						.getHostId();

				if (!PaaSService.Status.RUNNING.toString().equals(
						paasSvc.getStatus()))
					return handleError(Status.INTERNAL_SERVER_ERROR,
							OrchestratorErrorCode.ORC_PAAS_SVC_ILLEGAL_STATE,
							"Service is not running.");

				paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.INFO,
						"Stopping service...");

				AppRepoEndpoint endpoint = infrastructureBean.getPrivateAppRepoEndpoint(new AppRepoLookupInfo(workgroupId));
				
				
//				String identityURL = envConfig
//						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL);

				// TODO OS Tenant data lookup
//				String tenant = envConfig
//						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_NAME);
//				String username = envConfig
//						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_USERNAME);
//				String pwd = envConfig
//						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_PASSWORD);

				// Deploy on Heat
				MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
						endpoint.getOSIdentityURL(), null, "v2.0", endpoint.getTenantName(), endpoint.getUsername(), endpoint.getPassword());

				List<Endpoint> endpoints = mainOS
						.getServiceEndpoints(OpenstackEndpointType.COMPUTE);
				ComputeOpenstackAPIClient client = mainOS
						.getComputeClient(endpoints.get(0));

				try {
					client.stopServer(vmId, null);
				} catch (Exception e) {
					// If OS Conflicting request error -> Must update internal
					// PaaSService state -> Ignore error
					if (!((e instanceof OpenstackAPIErrorException) && (((OpenstackAPIErrorException) e)
							.getAPIError().getCode() == 409L)))
						throw e;
				}

				// TODO: Remove workaround for transaction problem
				paasSvc = dbaasMgmt.getDBaaSById(resId).getPaaSService();

				paasSvc.setStatus(PaaSService.Status.STOPPED.toString());

				paasSvcDAO.create(paasSvc);

				// TODO Check when stopped

				paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.INFO,
						"Service stopped.");

				return PrismaResponse.status(Status.ACCEPTED).build().build();
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			if (paasSvc != null)
				paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.ERROR,
						"Error occurred stopping the service.");

			return handleGenericException(e);
		}
	}

	@Override
	public Response startService(HttpServletRequest request, Long workgroupId,
			Long resId) {

		PaaSService paasSvc = null;
		try {
			try {
				DBaaS dbaas = dbaasMgmt.getDBaaSById(resId);
				if (dbaas != null)
					return handleNotFoundException(
							OrchestratorErrorCode.ORC_ITEM_NOT_FOUND, "");

				paasSvc = dbaas.getPaaSService();

				// TODO Improve
				String vmId = dbaas.getPaaSService().getPaaSDeployment()
						.getPaaSDeploymentServices().iterator().next()
						.getPaaSDeploymentServiceInstances().iterator().next()
						.getHostId();

				if (!PaaSService.Status.STOPPED.toString().equals(
						paasSvc.getStatus()))
					return handleError(Status.INTERNAL_SERVER_ERROR,
							OrchestratorErrorCode.ORC_PAAS_SVC_ILLEGAL_STATE,
							"Service is not stopped.");

				paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.INFO,
						"Starting service...");

				AppRepoEndpoint endpoint = infrastructureBean.getPrivateAppRepoEndpoint(new AppRepoLookupInfo(workgroupId));
				
				
//				String identityURL = envConfig
//						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL);

				// TODO OS Tenant data lookup
//				String tenant = envConfig
//						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_NAME);
//				String username = envConfig
//						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_USERNAME);
//				String pwd = envConfig
//						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_PASSWORD);

				// Deploy on Heat
				MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
						endpoint.getOSIdentityURL(), null, "v2.0", endpoint.getTenantName(), endpoint.getUsername(), endpoint.getPassword());

				List<Endpoint> endpoints = mainOS
						.getServiceEndpoints(OpenstackEndpointType.COMPUTE);
				ComputeOpenstackAPIClient client = mainOS
						.getComputeClient(endpoints.get(0));

				try {
					client.startServer(vmId, null);
				} catch (Exception e) {
					// If OS Conflicting request error -> Must update internal
					// PaaSService state -> Ignore error
					if (!((e instanceof OpenstackAPIErrorException) && (((OpenstackAPIErrorException) e)
							.getAPIError().getCode() == 409L)))
						throw e;
				}

				// TODO: Remove workaround for transaction problem
				paasSvc = dbaasMgmt.getDBaaSById(resId).getPaaSService();

				paasSvc.setStatus(PaaSService.Status.RUNNING.toString());

				paasSvcDAO.create(paasSvc);

				// TODO Check when stopped

				paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.INFO,
						"Service running.");

				return PrismaResponse.status(Status.ACCEPTED).build().build();
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			if (paasSvc != null)
				paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.ERROR,
						"Error occurred stopping the service.");

			return handleGenericException(e);
		}
	}

	@Override
	public Response rebootService(HttpServletRequest request, Long workgroupId,
			Long resId) {

		DBaaS dbaas = dbaasMgmt.getDBaaSById(resId);
		PaaSService paasSvc = dbaas.getPaaSService();

		/**
		 * If status is running then stop and start else if stopped then start
		 * else error
		 * */
		if (paasSvc.getStatus().equals(PaaSService.Status.RUNNING.toString())) {
			try {
				return restartService(request, workgroupId, resId);
			} catch (Exception e) {
				LOG.error("Exception restarting VM " + e);
				return handleError(Status.INTERNAL_SERVER_ERROR,
						OrchestratorErrorCode.ORC_GENERIC_ERROR, e);
			}
		} else if (paasSvc.getStatus().equals(
				PaaSService.Status.STOPPED.toString())) {
			return startService(request, workgroupId, resId);
		} else {
			LOG.error("Service status error: service is not neither running nor stopped");
			return handleError(Status.INTERNAL_SERVER_ERROR,
					OrchestratorErrorCode.ORC_PAAS_SVC_ILLEGAL_STATE,
					"service is not neither running nor stopped");
		}
	}

	private Response restartService(final HttpServletRequest request,
			final Long workgroupId, final Long resId) throws Exception {

		stopService(request, workgroupId, resId);

		AbstractPollingBehaviour<Object, String> pollBehaviour = new AbstractPollingBehaviour<Object, String>() {

			@Override
			public String doPolling(Object params) throws PollingException {
				DBaaS dbaas = dbaasMgmt.getDBaaSById(resId);
				PaaSService paasSvc = dbaas.getPaaSService();
				return paasSvc.getStatus();
			}

			@Override
			public boolean pollExit(String pollResult) {
				return pollResult.equals(PaaSService.Status.STOP_IN_PROGRESS
						.toString())
						|| pollResult.equals(PaaSService.Status.STOPPED
								.toString());
			}

			@Override
			public boolean pollSuccessful(Object params, String pollResult) {
				return pollResult.equals(PaaSService.Status.STOPPED.toString());
			}
		};
		pollBehaviour.setTimeoutThreshold(60000);

		Poller<Object, String> vmPoller = new Poller<Object, String>(
				pollBehaviour, 10000, 10000, 5);

		try {
			vmPoller.setLogEnabled(true);
			vmPoller.doPoll(null);
		} catch (PollingException e) {
			LOG.error("Stopping VM error.", e);
			throw new Exception("Stopping VM error.");
		}

		return startService(request, workgroupId, resId);
	}

	// @Override
	// public void undeployService(HttpServletRequest request, Long workgroupId,
	// Long resId) {
	// try {
	// // TODO Input from WS
	// // Long workgroupId=1L;
	//
	// String identityURL = envConfig
	// .getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL);
	//
	// // TODO OS Tenant data lookup
	// String tenant = envConfig
	// .getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_NAME);
	// String username = envConfig
	// .getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_USERNAME);
	// String pwd = envConfig
	// .getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_PASSWORD);
	//
	// dbaasMgmt.undeployDBaaS(resId, identityURL, tenant, username, pwd);
	//
	// getSessionServletResponse().setStatus(
	// Status.ACCEPTED.getStatusCode());
	// } catch (Exception e) {
	// throw new PrismaWrapperException(e.getMessage(), e);
	// }
	// }

	@Inject
	DeploymentOrchestrator deploymentOrchestrator;

	@Override
	public void deleteResource(final Long workgroupId, final Long resId) {
		try {
			Long userAccountId = ((PrismaUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal())
					.getUserData().getUserAccountId();
			deploymentOrchestrator.undeployDBaaS(userAccountId, workgroupId,
					resId);
			getSessionServletResponse().setStatus(
					Status.ACCEPTED.getStatusCode());

		} catch (PrismaWrapperException pwe) {
			throw pwe;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public DBaaSRepresentation createResource(Long workgroupId,
			DBaaSDeployRequest deployRequest) {
		return orcWS.provisioningDBaaS(deployRequest);
	}

}
