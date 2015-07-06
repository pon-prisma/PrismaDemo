package it.prisma.businesslayer.bizws.paas.services.mqaas;

import it.prisma.businesslayer.bizlib.accounting.PrismaUserDetails;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestrator;
import it.prisma.businesslayer.bizlib.paas.services.PaaSServiceHelper;
import it.prisma.businesslayer.bizlib.paas.services.mqaas.MQaaSMgmtBean;
import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.bizws.orchestrator.OrchestratorMainWS;
import it.prisma.businesslayer.bizws.paas.services.PaaSServiceGenericWSImpl;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.mqaas.MQaaS;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

public class MQaaSWSImpl extends
		PaaSServiceGenericWSImpl<MQaaSRepresentation, MQaaSDeployRequest>
		implements MQaaSWS {

	private static Logger LOG = LogManager.getLogger(MQaaSWSImpl.class);

	@Inject
	protected MQaaSMgmtBean mqaasMgmt;

	@Inject
	protected MappingHelper<MQaaS, MQaaSRepresentation> mqaasMH;

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

	@PrismaWrapper(restParamsEnabled = true)
	@Override
	public Collection<MQaaSRepresentation> getResourcesByWorkgroup(
			Long workgroupId, String considerDeletedToo) {
		try {
			try {
				Collection<MQaaS> result = mqaasMgmt.getByUserAndWorkgroup(
						workgroupId, null,
						Boolean.parseBoolean(considerDeletedToo));

				return mqaasMH.getDSL(result);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e.getMessage(), e);
		}
	}

	@Override
	public MQaaSRepresentation getResourceById(Long workgroupId, Long appId) {
		// TODO Authentication !
		try {
			try {
				MQaaS result = mqaasMgmt.getById(appId);

				if (result == null)
					throw new ResourceNotFoundException(
							DBaaSRepresentation.class, "workgroupId="
									+ workgroupId, "id=" + appId);

				return mqaasMH.getDSL(result);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e.getMessage(), e);
		}

	}

	@Override
	// @PreAuthorize("hasRole('ROLE_FAKE')") //NOT WORKING
	@Secured({ "ROLE_FAKE" })
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<PaaSServiceEventRepresentation> getResourceEvents(
			HttpServletRequest request, Long workgroupId, Long envId) {
		try {

			Collection<PaaSServiceEvent> events = mqaasMgmt.getEvents(envId,
					0L, Long.MAX_VALUE, 1000L);

			// return handleResult(paaSServiceEventMH.getDSL(events));
			return paaSServiceEventMH.getDSL(events);
		} catch (Exception e) {
			// return handleGenericException(e);
			throw new PrismaWrapperException(e);
		}
	}

	@Inject
	DeploymentOrchestrator deploymentOrchestrator;

	@Override
	public void deleteResource(final Long workgroupId, final Long resId) {
		try {
			Long userAccountId = ((PrismaUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal())
					.getUserData().getUserAccountId();
			deploymentOrchestrator.undeployMQaaS(userAccountId, workgroupId,
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
	public MQaaSRepresentation createResource(Long workgroupId,
			MQaaSDeployRequest deployRequest) {
		return orcWS.provisioningMQaaS(deployRequest);
	}

}
