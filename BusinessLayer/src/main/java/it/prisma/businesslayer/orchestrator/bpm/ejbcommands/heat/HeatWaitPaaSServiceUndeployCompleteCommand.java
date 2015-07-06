package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.heat;

import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestratorBean;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.OpenStackEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.OS4JException;
import org.openstack4j.model.heat.Stack;

import com.google.common.base.Preconditions;

public class HeatWaitPaaSServiceUndeployCompleteCommand extends BaseHeatCommand {

	@Inject
	private EnvironmentConfig envConfig;

	public HeatWaitPaaSServiceUndeployCompleteCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		UndeploymentMessage undepMsg = (UndeploymentMessage) workItem
				.getParameter(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG);
		Preconditions.checkNotNull(undepMsg);

		ExecutionResults exResults = new ExecutionResults();
		
		logger.info("Checking undeployment status");

		try {
			OSClient osRC = super.getAPIClient((OpenStackEndpoint) undepMsg
					.getInfrastructureData().getDeployerEndpoint());

			Stack stack = ((HeatDeploymentData)undepMsg.getDeploymentData()).getStack();

			if (envConfig.getAppProperty(
					EnvironmentConfig.APP_TEST_FAKE_HEAT).equals("true")) {

				// TODO: Fake stack deployment status
				// stack
				exResults.setData("ResultDeploymentCompleted", true);
				resultOccurred(true, exResults);
			} else {
				if (stack != null) {		
					stack = osRC.heat().stacks().getDetails(stack.getName(), stack.getId());
					
					if (stack.getStatus().equals("DELETE_IN_PROGRESS")) {
						// Poll still in progress
						exResults.setData("ResultDeploymentCompleted", false);
						resultOccurred(false, exResults);
					} else if (stack.getStatus().equals("DELETE_COMPLETE")) {
						// Poll Ended successfully
						exResults.setData("ResultDeploymentCompleted", true);
						resultOccurred(true, exResults);
					} else {
						errorOccurred(
								OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_UNDEPLOYING_ERROR,
								"API_ERROR:" + stack.getStackStatusReason(), exResults);
					}
				} else {
					exResults.setData("ResultDeploymentCompleted", true);
					resultOccurred(true, exResults);
				}
			}

		} catch (OS4JException apiEx) {
			// API Exception occurred
			errorOccurred(
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_UNDEPLOYING_ERROR,
					"API_ERROR:" + apiEx.getMessage(), exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_UNDEPLOYING_ERROR,
					exResults);
		}

		return exResults;
	}
}
