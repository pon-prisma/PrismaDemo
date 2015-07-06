package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent.LRTEventType;
import it.prisma.domain.dsl.cloudify.request.ApplicationDeploymentRequest;
import it.prisma.domain.dsl.cloudify.response.ApplicationDeploymentResponse;
import it.prisma.domain.dsl.cloudify.response.UploadRecipeResponse;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

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
@Local(CfyAPIDeployApplicationCommand.class)
public class CfyAPIDeployApplicationCommand extends BaseCfyAPICommand {

	@Inject
	private EnvironmentConfig envConfig;

	public CfyAPIDeployApplicationCommand() throws Exception {
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
		String appName = (String) workItem.getParameter("AppName");
		Object prevAPIResult = workItem.getParameter("PreviousAPIResult");
		UploadRecipeResponse uplRecipeResp = (UploadRecipeResponse) prevAPIResult;

		ExecutionResults exResults = new ExecutionResults();

		String applicationName = appName;
		String authGroups = "myAuthGroup";
		// String debugEvents = "init,install";
		// String debugMode = "onError";
		// Boolean isDebugAll = false;
		Boolean isSelfHealing = true;
		String uploadKey = uplRecipeResp.getUploadKey();// "ce91eadf-0f8e-43f4-8dd0-e9063ea60920";

		ApplicationDeploymentRequest instAppRequest = new ApplicationDeploymentRequest();
		instAppRequest.setApplcationFileUploadKey(uploadKey);
		instAppRequest.setApplicationName(applicationName);
		instAppRequest.setAuthGroups(authGroups);
		// instAppRequest.setDebugEvents(debugEvents);
		// instAppRequest.setDebugMode(debugMode);
		// instAppRequest.setDebugAll(isDebugAll);
		instAppRequest.setSelfHealing(isSelfHealing);

		// Invoke Cfy Deploy API with CloudifyAPIClient
		try {
			ApplicationDeploymentResponse response;
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_CFY)
					.equals("true")) {
				response = new ApplicationDeploymentResponse();
				response.setDeploymentID("fake-deployment-id");
			} else
				response = cfyRC.ApplicationDeployment(applicationName,
						instAppRequest);

			lrtEventLogger.log(LRTEventType.INFO, lrt, response);

			resultOccurred(response, exResults);

		} catch (CloudifyAPIErrorException apiEx) {
			// API Exception occurred
			// TODO ERROR_APPLICATION_NAME_IS_ALREADY_IN_USE
			// if("application_name_is_alreay_in_use".equals(errResp.getMessageId()))
			errorOccurred(
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					"API_ERROR:" + apiEx.getAPIError(), exResults);
		} catch (Exception
				sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					exResults);
		}

		return exResults;
	}
}
