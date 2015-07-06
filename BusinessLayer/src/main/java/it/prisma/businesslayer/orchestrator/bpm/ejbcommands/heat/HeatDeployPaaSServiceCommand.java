package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.heat;

import it.prisma.businesslayer.bizlib.iaas.heat.HeatManagement;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.OpenStackEndpoint;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.lang.reflect.Field;
import java.util.Map;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.OS4JException;
import org.openstack4j.model.heat.Stack;
import org.openstack4j.openstack.heat.domain.HeatStack;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to deploy a PaaSService to Heat.<br/>
 * The command is expecting a {@link DeploymentMessage} with
 * {@link HeatDeploymentData} ready for the deployment (ie. including a template
 * URL). <br/>
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} deploymentMessage
 * @return The updated deploymentMessage with reference to the deployed
 *         application in {@link HeatDeploymentData}.
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class HeatDeployPaaSServiceCommand extends BaseHeatCommand {

	@Inject
	private EnvironmentConfig envConfig;

	@Inject
	private HeatManagement heatBean;

	public HeatDeployPaaSServiceCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		DeploymentMessage depMsg = (DeploymentMessage) workItem
				.getParameter("deploymentMessage");
		Preconditions.checkNotNull(depMsg);

		ExecutionResults exResults = new ExecutionResults();
		
		try {
			OSClient osRC = super.getAPIClient((OpenStackEndpoint) depMsg
					.getInfrastructureData().getDeployerEndpoint());

			HeatDeploymentData heatDepData = (HeatDeploymentData) depMsg
					.getDeploymentData();

			String stackName = heatBean.buildStackName(depMsg.getPaaSService());
			// Invoke Heat Deploy API with OpenStack API Client
			Stack stack;
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_HEAT)
					.equals("true")) {
				stack = new HeatStack();
				Field f;
				f = HeatStack.class.getDeclaredField("name");
				f.setAccessible(true);
				f.set(stack, stackName);
				f = HeatStack.class.getDeclaredField("id");
				f.setAccessible(true);
				f.set(stack, "fake-id-" + stackName);
			} else {

				Map<String, String> parameters = heatBean
						.getDeploymentTemplateParameters(heatDepData,
								depMsg.getDeployRequest(),
								depMsg.getPaaSService());

				stack = osRC
						.heat()
						.stacks()
						.create(Builders.stack().name(stackName)
								.templateURL(heatDepData.getTemplateURL())
								.parameters(parameters).build());

				stack = osRC.heat().stacks()
						.getDetails(stackName, stack.getId());
			}

			heatDepData.setStack(stack);
			exResults.setData("deploymentMessage", depMsg);
			exResults.setData("stack", stack);
			resultOccurred(depMsg, exResults);

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

}
