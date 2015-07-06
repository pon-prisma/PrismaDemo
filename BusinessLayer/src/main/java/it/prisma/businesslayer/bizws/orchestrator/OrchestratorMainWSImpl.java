package it.prisma.businesslayer.bizws.orchestrator;

import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.iaas.services.vm.VMMgmtBean;
import it.prisma.businesslayer.bizlib.monitoring.MonitoringServiceBean;
import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestrator;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.dao.accounting.UserAccountDAO;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.entities.iaas.services.vm.VMaaS;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entities.paas.services.biaas.BIaaS;
import it.prisma.dal.entities.paas.services.dbaas.DBaaS;
import it.prisma.dal.entities.paas.services.mqaas.MQaaS;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.VMaaSMonitoringStatus;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/orc")
public class OrchestratorMainWSImpl extends BaseWS implements
		OrchestratorMainWS {

	public static Logger prismaLog = LogManager
			.getLogger(OrchestratorMainWSImpl.class);

	@Inject
	private EnvironmentConfig envConfig;
	@Inject
	UserAccountDAO userDAO;
	@Inject
	WorkgroupDAO workgroupDAO;
	@Inject
	protected PaaSServiceDAO paasSvcDAO;
	@Inject
	private VMMgmtBean vmMgmtBean;
	@Inject
	DeploymentOrchestrator deploymentOrchestrator;

	/* TEST */

	@Inject
	private MonitoringServiceBean monitoringBean;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/test/mon/{uuid}")
	public boolean testMon(@PathParam("uuid") String uuid,
			@QueryParam("services") List<String> services) throws Exception {
		return monitoringBean.isServiceRunning("zabbix", "group", uuid);
	}

	/* TEST */

	@Override
	public APPaaSRepresentation provisioningAPPaaS(
			@Valid APPaaSDeployRequest request) {

		try {
			AppaaS result = deploymentOrchestrator.deployAPPaaS(request);

			return getMH(result, APPaaSRepresentation.class).getDSL(result);
		} catch (PrismaWrapperException pwe) {
			throw pwe;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public APPaaSEnvironmentRepresentation provisioningAPPaaSEnvironment(
			@Valid APPaaSEnvironmentDeployRequest request) {

		try {
			AppaaSEnvironment result = deploymentOrchestrator
					.deployAPPaaSEnvironment(request);

			return getMH(result, APPaaSEnvironmentRepresentation.class).getDSL(
					result);
		} catch (PrismaWrapperException pwe) {
			throw pwe;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public MQaaSRepresentation provisioningMQaaS(
			@Valid MQaaSDeployRequest request) {

		try {
			MQaaS result = deploymentOrchestrator.deployMQaaS(request);

			return getMH(result, MQaaSRepresentation.class).getDSL(result);
		} catch (PrismaWrapperException pwe) {
			throw pwe;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public DBaaSRepresentation provisioningDBaaS(
			@Valid DBaaSDeployRequest request) {

		try {
			DBaaS result = deploymentOrchestrator.deployDBaaS(request);

			return getMH(result, DBaaSRepresentation.class).getDSL(result);
		} catch (PrismaWrapperException pwe) {
			throw pwe;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public BIaaSRepresentation provisioningBIaaS(
			@Valid BIaaSDeployRequest request) {

		try {
			BIaaS result = deploymentOrchestrator.deployBIaaS(request);

			return getMH(result, BIaaSRepresentation.class).getDSL(result);
		} catch (PrismaWrapperException pwe) {
			throw pwe;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public VMRepresentation provisioningVMaaS(@Valid VMDeployRequest request) {

		try {
			VMaaS result = deploymentOrchestrator.deployVMaaS(request);

			return getMH(result, VMRepresentation.class).getDSL(result);
		} catch (PrismaWrapperException pwe) {
			throw pwe;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/iaas/vmaas")
	@PrismaWrapper
	public void updateIaaSServiceStatus(
			VMaaSMonitoringStatus vMaaSMonitoringStatus) {

		try {
			vmMgmtBean.updateVirtualMachineStatus(vMaaSMonitoringStatus);
		} catch (ResourceNotFoundException enfe) {
			throw new PrismaWrapperException(enfe);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	// /**
	// * Test deploy VMaaS on heat, no WF
	// *
	// * @param request
	// * @return
	// */
	// @PUT
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Path("/deployments/vm/")
	// public Response provisioningVM(VMDeployRequest request) {
	//
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
	// VMaaS vm = vmMgmtBean.provisioningVM(request, userDAO
	// .findById(request.getAccount().getUserId()), workgroupDAO
	// .findById(request.getAccount().getWorkgroupId()),
	// identityURL, tenant, username, pwd);
	//
	// return PrismaResponse
	// .status(Status.CREATED,
	// getMH(vm, VMRepresentation.class).getDSL(vm))
	// .build().build();
	//
	// } catch (Exception e) {
	// return handleGenericException(e);
	// }
	// }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deployments/test/appaas")
	public List<APPaaSRepresentation> test() throws Exception {
		PrismaBizAPIClient client = new PrismaBizAPIClient(
				"http://localhost:8080/");
		return client.getAllAPPaaS(1L, null);
	}

}