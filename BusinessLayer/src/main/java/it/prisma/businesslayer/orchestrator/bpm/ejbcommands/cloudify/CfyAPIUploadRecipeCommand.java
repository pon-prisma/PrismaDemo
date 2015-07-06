package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent.LRTEventType;
import it.prisma.domain.dsl.cloudify.response.UploadRecipeResponse;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import javax.ejb.Local;
import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * This class implements a jBPM Executor Command which consumes a REST web
 * service.
 * 
 * @author l.biava
 * 
 */
@Local(CfyAPIUploadRecipeCommand.class)
public class CfyAPIUploadRecipeCommand extends BaseCfyAPICommand {

	@Inject
	private EnvironmentConfig envConfig;

	public CfyAPIUploadRecipeCommand() throws Exception {
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
		// String appName = (String) workItem.getParameter("AppName");
		String recipePath = (String) workItem.getParameter("RecipePath");

		ExecutionResults exResults = new ExecutionResults();

		String fileName = recipePath.substring(recipePath.lastIndexOf("/") + 1);// "mysql-app.zip";
		String filePath = /*
						 * this.getClass().getProtectionDomain().getCodeSource()
						 * .getLocation().getPath() + "/cloudify-recipes/" +
						 */recipePath;

		// Invoke Cfy Upload Recipe API with CloudifyAPIClient
		try {
			File file = new File(filePath);
			if (!file.exists())
				throw new IOException("File not found");

			UploadRecipeResponse response;
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_CFY)
					.equals("true")) {
				response = new UploadRecipeResponse();
				response.setUploadKey("fake-upload-key");
			} else
				response = cfyRC.UploadRecipe(fileName, file);

			lrtEventLogger.log(LRTEventType.INFO, lrt, response);

			resultOccurred(response, exResults);

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
		}

		try {
			Files.deleteIfExists(FileSystems.getDefault().getPath(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return exResults;
	}
}
