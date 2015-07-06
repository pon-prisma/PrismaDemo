package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.iaas.cloudify.CloudifyManagement;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent.LRTEventType;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.paas.deployments.PaaSDeploymentServiceRepresentation.PaaSDeploymentServiceRepresentationType;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Local;
import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * 
 * @author l.biava
 * 
 */
@Local(CfyAPIServiceInstanceDescriptionCommand.class)
public class CfyAPIServiceInstanceDescriptionCommand extends BaseCfyAPICommand {

	@Inject
	private CloudifyManagement cfyMgmtBean;

	public CfyAPIServiceInstanceDescriptionCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// IMPORTANT !
		super.customExecute(ctx);

		// Obtaining parameters

		// String serviceName="mysql";
		// String instanceId = "1";

		// Retrieving data from context (also process variables)
		String appName = (String) workItem.getParameter("AppName");
		// String serviceName = (String) workItem.getParameter("ServiceName");
		// PaaSService paasService = (PaaSService)
		// workItem.getParameter("PaaSService");

		// Object prevAPIResult = workItem.getParameter("PreviousAPIResult");
		// ApplicationDeploymentResponse uplRecipeResp =
		// (ApplicationDeploymentResponse) prevAPIResult;
		// TODO: Mantenere parametri in ingresso precedenti

		ExecutionResults exResults = new ExecutionResults();

		try {
			// ServiceInstanceDescriptionResponse response;
			PaaSDeployment response;
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_CFY)
					.equals("true")) {

				PaaSDeployment paasD = new PaaSDeployment("CLOUDIFY");
				paasD.setName(appName);
				String status = PaaSService.Status.RUNNING.toString();
				paasD.setStatus(status);

				Set<PaaSDeploymentService> services = new HashSet<PaaSDeploymentService>();
				paasD.setPaaSDeploymentServices(services);

				// Tomcat Service + 1 Instance
				PaaSDeploymentService service = new PaaSDeploymentService(
						paasD,
						PaaSDeploymentServiceRepresentationType.AS_TOMCAT.toString(),
						PaaSDeploymentServiceRepresentationType.AS_TOMCAT.toString());
				Set<PaaSDeploymentServiceInstance> instances = new HashSet<PaaSDeploymentServiceInstance>();
				service.setPaaSDeploymentServiceInstances(instances);

				services.add(service);

				PaaSDeploymentServiceInstance instance = new PaaSDeploymentServiceInstance(
						service, "10.10.10.10", "10.10.10.10",
						PaaSService.Status.RUNNING.toString(), "1",
						"dummy-UUID-" + UUID.randomUUID());

				instances.add(instance);

				// Apache LB Service + 1 Instance
				service = new PaaSDeploymentService(
						paasD,
						PaaSDeploymentServiceRepresentationType.LB_APACHE.toString(),
						PaaSDeploymentServiceRepresentationType.LB_APACHE.toString());
				instances = new HashSet<PaaSDeploymentServiceInstance>();
				service.setPaaSDeploymentServiceInstances(instances);

				services.add(service);

				instance = new PaaSDeploymentServiceInstance(service,
						"10.10.10.20", "10.10.10.20",
						PaaSService.Status.RUNNING.toString(), "1",
						"dummy-UUID-" + UUID.randomUUID());

				instances.add(instance);

				response = paasD;

			} else {
				// response = cfyRC
				// .ServiceInstanceDescription(appName, serviceName,
				// instanceId);

				response = cfyMgmtBean.getApplicationServicesInformations(
						cfyRC, appName);
			}
			try {
				lrtEventLogger.log(LRTEventType.RESULT, lrt, response);
			} catch (Exception e) {
			}

			resultOccurred(response, exResults);

		} catch (CloudifyAPIErrorException apiEx) {
			// API Exception occurred
			errorOccurred(
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					"API_ERROR:" + apiEx.getAPIError(), exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					exResults);
		}

		try {
			logger.info("ENDED, Result(" + exResults.getData("ResultStatus")
					+ ")");
		} catch (Exception e) {
		}

		return exResults;
	}
}
