package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.domain.dsl.cloudify.response.ApplicationDescriptionResponse;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.utils.misc.polling.AbstractPollingBehaviour;
import it.prisma.utils.misc.polling.ExternallyControlledPoller;
import it.prisma.utils.misc.polling.ExternallyControlledPoller.PollingStatus;
import it.prisma.utils.misc.polling.PollingBehaviour;
import it.prisma.utils.misc.polling.PollingException;

import java.util.HashMap;
import java.util.Map;

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
@Local(CfyAPIApplicationDescriptionCommand.class)
public class CfyAPIApplicationDescriptionCommand extends BaseCfyAPICommand {

	@Inject
	private EnvironmentConfig envConfig;

	public CfyAPIApplicationDescriptionCommand() throws Exception {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// IMPORTANT !
		super.customExecute(ctx);

		// Obtaining parameters
		String appName = (String) workItem.getParameter("AppName");

		// Object prevAPIResult = workItem.getParameter("PreviousAPIResult");
		// ApplicationDeploymentResponse uplRecipeResp =
		// (ApplicationDeploymentResponse) prevAPIResult;
		// TODO: Mantenere parametri in ingresso precedenti

		ExternallyControlledPoller<Map<String, Object>, ApplicationDescriptionResponse> pollingStatus = null;

		try {
			pollingStatus = (ExternallyControlledPoller<Map<String, Object>, ApplicationDescriptionResponse>) workItem
					.getParameter("PollingStatus");
		} catch (Exception e) {
		}

		ExecutionResults exResults = new ExecutionResults();

		try {
			ApplicationDescriptionResponse response;

			// Create poller if not exists
			if (pollingStatus == null) {
				pollingStatus = getPoller();
			}
			exResults.setData("PollingStatus", pollingStatus);

			// Do polling
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Client", cfyRC);
			params.put("AppName", appName);
			try {
				// lrtEventLogger.log(LRTEventType.RESULT, lrt, response);
				logger.info("Checking deployment status");

				if (envConfig.getAppProperty(
						EnvironmentConfig.APP_TEST_FAKE_CFY).equals("true")) {
					response = new ApplicationDescriptionResponse();
					response.setApplicationState("STARTED");
					exResults.setData("ResultDeploymentCompleted", "true");
					resultOccurred(response, exResults);
				} else {
					response = pollingStatus.doPollEvent(params);

					// Poll Ended successfully
					if (pollingStatus.getPollStatus() == PollingStatus.ENDED) {
						exResults.setData("ResultDeploymentCompleted", "true");
						resultOccurred(response, exResults);
					} else {
						// Poll still in progress
						exResults.setData("ResultDeploymentCompleted", "false");
						resultOccurred(response, exResults);
					}
				}
			} catch (PollingException pe) {
				throw new Exception(
						"Cloudify application deploy polling, error: "
								+ pe.getMessage(), pe);
			} catch (Exception e) {
				throw new Exception(
						"Cloudify application deploy polling failed, error: "
								+ e.getMessage(), e);
			}

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

		return exResults;
	}

	private static ExternallyControlledPoller<Map<String, Object>, ApplicationDescriptionResponse> getPoller() {

		PollingBehaviour<Map<String, Object>, ApplicationDescriptionResponse> pollBehavior = new AbstractPollingBehaviour<Map<String, Object>, ApplicationDescriptionResponse>() {

			{
				setTimeoutThreshold(30 * 60 * 1000);
			}

			private static final long serialVersionUID = -5994059867039967783L;

			@Override
			public ApplicationDescriptionResponse doPolling(
					Map<String, Object> params) throws PollingException {
				try {
					CloudifyAPIClient cfyRC = (CloudifyAPIClient) params
							.get("Client");
					String appName = (String) params.get("AppName");

					return cfyRC.ApplicationDescription(appName);
				} catch (Exception e) {
					throw new PollingException(
							"Cloudify Polling for ServiceRunning - error occured: "
									+ e.getMessage(), e);
				}
			}

			@Override
			public boolean pollExit(ApplicationDescriptionResponse pollResult) {
				return (pollResult != null && pollResult.getApplicationState()
						.equals("STARTED"));
			}

		};

		return new ExternallyControlledPoller<Map<String, Object>, ApplicationDescriptionResponse>(
				pollBehavior, 3);
	}
}
