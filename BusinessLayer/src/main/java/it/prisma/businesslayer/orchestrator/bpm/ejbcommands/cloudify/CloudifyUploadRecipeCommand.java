package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.domain.dsl.cloudify.response.UploadRecipeResponse;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to upload the PaaSService recipe to Cloudify.<br/>
 * <br/>
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} deploymentMessage
 * @return The updated deploymentMessage with reference to the uploaded recipe
 *         in {@link CloudifyDeploymentData}.
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class CloudifyUploadRecipeCommand extends BaseCloudifyCommand {

	@Inject
	private EnvironmentConfig envConfig;

	public CloudifyUploadRecipeCommand() throws Exception {
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

		String filePath = cfyDepData.getCustomizedRecipePath();
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

		try {

			// Invoke Cfy Upload Recipe API with CloudifyAPIClient
			File file = new File(filePath);
			if (!file.exists())
				throw new IOException("File not found");

			UploadRecipeResponse response;
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_CFY)
					.equals("true")) {
				response = new UploadRecipeResponse();
				response.setUploadKey("fake-upload-key");
			} else {
				response = cfyRC.UploadRecipe(fileName, file);
			}

			String recipeUploadKey = response.getUploadKey();

			cfyDepData.setRecipeUploadKey(recipeUploadKey);

			exResults.setData("deploymentMessage", depMsg);
			exResults.setData("recipeUploadKey", recipeUploadKey);
			resultOccurred(depMsg, exResults);

		} catch (IOException ioe) {
			errorOccurred(
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_RECIPE_NOT_FOUND,
					filePath, exResults);
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
		} finally {
			try {
				Files.deleteIfExists(FileSystems.getDefault().getPath(filePath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return exResults;
	}
}
