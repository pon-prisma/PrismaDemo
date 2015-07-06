package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.heat;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.DeployerDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.OpenStackEndpoint;
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
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.OS4JException;
import org.openstack4j.model.heat.Stack;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to wait for a PaaSService to be successfully
 * deployed to Heat.<br/>
 * The command is expecting a {@link DeploymentMessage} with
 * {@link HeatDeploymentData} containing the data of the ongoing deployment (ie.
 * the stack data). <br/>
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
 *         <li>{@link Stack} <code>stack</code> with the whole response from
 *         Cloudify</li>
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
public class HeatWaitPaaSServiceDeployCompleteCommand extends BaseHeatCommand {

	@Inject
	private EnvironmentConfig envConfig;

	public HeatWaitPaaSServiceDeployCompleteCommand() throws Exception {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		DeploymentMessage depMsg = (DeploymentMessage) workItem
				.getParameter("deploymentMessage");
		ExternallyControlledPoller<Map<String, Object>, Stack> pollingStatus = null;
		ExecutionResults exResults = new ExecutionResults();
		try {
			pollingStatus = (ExternallyControlledPoller<Map<String, Object>, Stack>) workItem
					.getParameter("pollingStatus");
		} catch (Exception e) {
		}
		Preconditions.checkNotNull(depMsg);

		try {
			OSClient osRC = super.getAPIClient((OpenStackEndpoint) depMsg
					.getInfrastructureData().getDeployerEndpoint());

			HeatDeploymentData heatDepData = (HeatDeploymentData) depMsg
					.getDeploymentData();

			Stack stack = heatDepData.getStack();

			// Create poller if not exists
			if (pollingStatus == null) {
				pollingStatus = getPoller(heatDepData);
			}
			exResults.setData("pollingStatus", pollingStatus);

			// Do polling
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Client", osRC);
			params.put("Stack", heatDepData.getStack());
			try {
				logger.info("Checking Stack deployment status");

				if (envConfig.getAppProperty(
						EnvironmentConfig.APP_TEST_FAKE_HEAT).equals("true")) {

					// TODO: Fake stack deployment status
					// stack
					exResults.setData("resultDeploymentCompleted", "true");
					exResults.setData("stack", stack);
					resultOccurred(stack, exResults);
				} else {
					stack = pollingStatus.doPollEvent(params);
					exResults.setData("stack", stack);

					// Poll Ended successfully
					if (pollingStatus.getPollStatus() == PollingStatus.ENDED) {
						exResults.setData("resultDeploymentCompleted", "true");
						resultOccurred(stack, exResults);
					} else {
						// Poll still in progress
						exResults.setData("resultDeploymentCompleted", "false");
						resultOccurred(stack, exResults);
					}
				}
			} catch (PollingException pe) {
				throw new Exception("Heat stack deploy polling, error: "
						+ pe.getMessage(), pe);
			} catch (Exception e) {
				throw new Exception("Heat stack deploy polling failed, error: "
						+ e.getMessage(), e);
			}

		} catch (OS4JException apiEx) {
			// API Exception occurred
			errorOccurred(
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					"API_ERROR:" + apiEx.getMessage(), exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					exResults);
		}

		return exResults;
	}

	private static ExternallyControlledPoller<Map<String, Object>, Stack> getPoller(
			DeployerDeploymentData depData) {

		PollingBehaviour<Map<String, Object>, Stack> pollBehavior = new AbstractPollingBehaviour<Map<String, Object>, Stack>(
				depData.getDeploymentFailTimeout()) {

			// {
			// setTimeoutThreshold(30 * 60 * 1000);
			// }

			private static final long serialVersionUID = -5994059867039967783L;

			@Override
			public Stack doPolling(Map<String, Object> params)
					throws PollingException {
				try {
					OSClient osRC = (OSClient) params.get("Client");
					Stack stack = (Stack) params.get("Stack");

					return osRC.heat().stacks()
							.getDetails(stack.getName(), stack.getId());
				} catch (Exception e) {
					throw new PollingException(
							"Heat Polling for ServiceRunning - error occured: "
									+ e.getMessage(), e);
				}
			}

			@Override
			public boolean pollExit(Stack pollResult) {
				return !pollResult.getStatus().equals("CREATE_IN_PROGRESS");
			}

			@Override
			public boolean pollSuccessful(Map<String, Object> params,
					Stack pollResult) {

				return pollResult.getStatus().equals("CREATE_COMPLETE");
			}

		};

		return new ExternallyControlledPoller<Map<String, Object>, Stack>(
				pollBehavior, 3);
	}
}
