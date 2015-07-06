package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.DeployerDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.domain.dsl.cloudify.response.ApplicationDescriptionResponse;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.utils.misc.polling.AbstractPollingBehaviour;
import it.prisma.utils.misc.polling.ExternallyControlledPoller;
import it.prisma.utils.misc.polling.ExternallyControlledPoller.PollingStatus;
import it.prisma.utils.misc.polling.PollingBehaviour;
import it.prisma.utils.misc.polling.PollingException;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to wait for a PaaSService to be successfully
 * deployed to Cloudify.<br/>
 * The command is expecting a {@link DeploymentMessage} with
 * {@link CloudifyDeploymentData} containing the data of the ongoing deployment
 * (ie. the application name and the -optional- deployment ID). <br/>
 * <br/>
 * The command will start a polling mechanism to automatically retry in case of
 * transient communication failures and to fail after a predefined timeout. <br/>
 * 
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} deploymentMessage
 * @param {@link ExternallyControlledPoller} pollingStatus - a support variable
 *        to store polling status (might be empty and in that case will be
 *        initialized properly to start the polling procedure)
 * @return <ul>
 *         <li>bool <code>resultDeploymentCompleted</code> to indicate whether
 *         the deployment is ready.</li>
 *         <li>{@link ApplicationDescriptionResponse}
 *         <code>cloudifyApplicationDescription</code> with the whole response
 *         from Cloudify</li>
 *         <li>{@link ExternallyControlledPoller} <code>pollingStatus</code> a
 *         support variable to store polling status, to be passed to the next
 *         execution of the command</li>
 *         </ul>
 * 
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class CloudifyWaitPaaSServiceDeployCompleteCommand extends
		BaseCloudifyCommand {

	@Inject
	private EnvironmentConfig envConfig;

	public CloudifyWaitPaaSServiceDeployCompleteCommand() throws Exception {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		DeploymentMessage depMsg = (DeploymentMessage) workItem
				.getParameter("deploymentMessage");
		ExternallyControlledPoller<Map<String, Object>, ApplicationDescriptionResponse> pollingStatus = null;

		try {
			pollingStatus = (ExternallyControlledPoller<Map<String, Object>, ApplicationDescriptionResponse>) workItem
					.getParameter("pollingStatus");
		} catch (Exception e) {
		}
		Preconditions.checkNotNull(depMsg);

		CloudifyAPIClient cfyRC = super.getAPIClient((CloudifyEndpoint) depMsg
				.getInfrastructureData().getDeployerEndpoint());

		ExecutionResults exResults = new ExecutionResults();

		CloudifyDeploymentData cfyDepData = (CloudifyDeploymentData) depMsg
				.getDeploymentData();

		try {

			ApplicationDescriptionResponse response;

			// Create poller if not exists
			if (pollingStatus == null) {
				pollingStatus = getPoller(cfyDepData);
			}
			exResults.setData("pollingStatus", pollingStatus);

			// Do polling
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Client", cfyRC);
			params.put("AppName", cfyDepData.getApplicationName());
			try {
				// lrtEventLogger.log(LRTEventType.RESULT, lrt, response);
				logger.info("Checking deployment status");

				if (envConfig.getAppProperty(
						EnvironmentConfig.APP_TEST_FAKE_CFY).equals("true")) {
					response = new ApplicationDescriptionResponse();
					response.setApplicationState("STARTED");
					exResults.setData("resultDeploymentCompleted", "true");
					exResults.setData("cloudifyApplicationDescription",
							response);
					resultOccurred(response, exResults);
				} else {
					response = pollingStatus.doPollEvent(params);
					exResults.setData("cloudifyApplicationDescription",
							response);

					// Poll Ended successfully
					if (pollingStatus.getPollStatus() == PollingStatus.ENDED) {
						exResults.setData("resultDeploymentCompleted", "true");
						resultOccurred(response, exResults);
					} else {
						// Poll still in progress
						exResults.setData("resultDeploymentCompleted", "false");
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

	private static ExternallyControlledPoller<Map<String, Object>, ApplicationDescriptionResponse> getPoller(DeployerDeploymentData depData) {

		PollingBehaviour<Map<String, Object>, ApplicationDescriptionResponse> pollBehavior = new AbstractPollingBehaviour<Map<String, Object>, ApplicationDescriptionResponse>(depData.getDeploymentFailTimeout()) {

//			{
//				setTimeoutThreshold(30 * 60 * 1000);
//			}

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
