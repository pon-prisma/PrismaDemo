package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestratorBean;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.domain.dsl.cloudify.response.ApplicationUndeploymentResponse;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

public class CloudifyUndeployPaaSServiceCommand extends BaseCloudifyCommand {

	@Inject
	private EnvironmentConfig envConfig;

	public CloudifyUndeployPaaSServiceCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		UndeploymentMessage undepMsg = (UndeploymentMessage) workItem
				.getParameter(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG);
		Preconditions.checkNotNull(undepMsg);

		CloudifyAPIClient cfyRC = super.getAPIClient((CloudifyEndpoint) undepMsg
				.getInfrastructureData().getDeployerEndpoint());

		ExecutionResults exResults = new ExecutionResults();
		
		try {

			// Invoke Cfy Deploy API with CloudifyAPIClient
			ApplicationUndeploymentResponse response;
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_CFY)
					.equals("true")) {
				response = new ApplicationUndeploymentResponse();
				response.setDeploymentID("fake-deployment-id");
			} else {
				response = cfyRC.applicationUndeployment(undepMsg.getPaaSService().getPaaSService().getPaaSDeployment().getName());
			}
//			String deploymentID = response.getDeploymentID();

			exResults.setData(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG, undepMsg);
			resultOccurred(undepMsg, exResults);

		} catch (CloudifyAPIErrorException apiEx) {
			// API Exception occurred
			if (apiEx.getAPIError().getMessageId().equals("failed_to_locate_app")) {
				exResults.setData(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG, undepMsg);
				resultOccurred(undepMsg, exResults);
			} else {
				errorOccurred(
						OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_UNDEPLOYING_ERROR,
						"API_ERROR:" + apiEx.getAPIError(), exResults);
			}
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_UNDEPLOYING_ERROR,
					exResults);
		}

		return exResults;
	}

}
