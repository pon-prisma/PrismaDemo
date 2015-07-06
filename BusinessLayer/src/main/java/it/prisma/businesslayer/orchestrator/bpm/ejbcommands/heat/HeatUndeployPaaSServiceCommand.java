package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.heat;

import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestratorBean;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.OpenStackEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.OS4JException;
import org.openstack4j.model.heat.Stack;
import org.openstack4j.openstack.heat.domain.HeatStack;

import com.google.common.base.Preconditions;

public class HeatUndeployPaaSServiceCommand extends BaseHeatCommand {

	private static final Logger LOG = LogManager
			.getLogger(HeatUndeployPaaSServiceCommand.class);
	
	@Inject
	private EnvironmentConfig envConfig;

	public HeatUndeployPaaSServiceCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		UndeploymentMessage undepMsg = (UndeploymentMessage) workItem
				.getParameter(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG);
		Preconditions.checkNotNull(undepMsg);

		ExecutionResults exResults = new ExecutionResults();
		
		try {
			OSClient osRC = super.getAPIClient((OpenStackEndpoint) undepMsg
					.getInfrastructureData().getDeployerEndpoint());

			String paaSDeploymentName = undepMsg.getPaaSService().getPaaSService().getPaaSDeployment().getName();
			// Invoke Heat Undeploy API with OpenStack API Client
			Stack stack;
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_HEAT)
					.equals("true")) {
				stack = new HeatStack();
				Field f;
				f = HeatStack.class.getDeclaredField("name");
				f.setAccessible(true);
				f.set(stack, paaSDeploymentName);
				f = HeatStack.class.getDeclaredField("id");
				f.setAccessible(true);
				f.set(stack, "fake-id-" + paaSDeploymentName);
			} else {
				String refTrim = paaSDeploymentName;
				String separator = "/stacks/";
				refTrim = refTrim.substring(refTrim.indexOf("/stacks/")
						+ separator.length());
				String[] stack_params = refTrim.split("/");
				
				stack = osRC.heat().stacks().getDetails(stack_params[0], stack_params[1]);
				if (stack == null) {
					LOG.warn("Couldn't find the stack " + stack_params[1] + " associated with the PaaSService " + undepMsg.getPaaSService().getPaaSService().getId());
				} else {
					osRC.heat().stacks().delete(stack.getName(), stack.getId());
				}
			}
			((HeatDeploymentData)undepMsg.getDeploymentData()).setStack(stack);
			exResults.setData(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG, undepMsg);
			resultOccurred(undepMsg, exResults);

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
