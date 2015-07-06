package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.iaas.cloudify.CloudifyManagement;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.domain.dsl.cloudify.request.ApplicationDeploymentRequest;
import it.prisma.domain.dsl.cloudify.response.ApplicationDeploymentResponse;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to deploy a PaaSService to Cloudify.<br/>
 * The command is expecting a {@link DeploymentMessage} with
 * {@link CloudifyDeploymentData} ready for the deployment (ie. including an
 * already uploaded recipe's upload key). <br/>
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} deploymentMessage
 * @return The updated deploymentMessage with reference to the deployed
 *         application in {@link CloudifyDeploymentData}.
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class CloudifyDeployPaaSServiceCommand extends BaseCloudifyCommand {

	@Inject
	private EnvironmentConfig envConfig;
	
	@Inject
	private CloudifyManagement cfyBean;

	public CloudifyDeployPaaSServiceCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		DeploymentMessage depMsg = (DeploymentMessage) workItem
				.getParameter("deploymentMessage");
		Preconditions.checkNotNull(depMsg);

		CloudifyAPIClient cfyRC = super.getAPIClient((CloudifyEndpoint) depMsg
				.getInfrastructureData().getDeployerEndpoint());

		ExecutionResults exResults = new ExecutionResults();

		CloudifyDeploymentData cfyDepData = (CloudifyDeploymentData) depMsg
				.getDeploymentData();

		try {

			String applicationName = cfyBean.buildApplicationName(depMsg.getPaaSService());
			String authGroups = "myAuthGroup";
			Boolean isSelfHealing = true;
			String recipeUploadKey = cfyDepData.getRecipeUploadKey();

			ApplicationDeploymentRequest instAppRequest = new ApplicationDeploymentRequest();
			instAppRequest.setApplcationFileUploadKey(recipeUploadKey);
			instAppRequest.setApplicationName(applicationName);
			instAppRequest.setAuthGroups(authGroups);
			instAppRequest.setSelfHealing(isSelfHealing);

			// Invoke Cfy Deploy API with CloudifyAPIClient
			ApplicationDeploymentResponse response;
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_CFY)
					.equals("true")) {
				response = new ApplicationDeploymentResponse();
				response.setDeploymentID("fake-deployment-id");
			} else {
				response = cfyRC.ApplicationDeployment(applicationName,
						instAppRequest);
			}
			String deploymentID = response.getDeploymentID();

			cfyDepData.setApplicationName(applicationName);
			cfyDepData.setApplicationDeploymentID(deploymentID);
			exResults.setData("deploymentMessage", depMsg);
			exResults.setData("applicationName", applicationName);
			exResults.setData("deploymentID", deploymentID);
			resultOccurred(depMsg, exResults);

		} catch (CloudifyAPIErrorException apiEx) {
			// API Exception occurred
			// TODO ERROR_APPLICATION_NAME_IS_ALREADY_IN_USE
			// if("application_name_is_alreay_in_use".equals(errResp.getMessageId()))
			errorOccurred(
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					"API_ERROR:" + apiEx.getAPIError(), exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					exResults);
		}

		return exResults;
	}

}
