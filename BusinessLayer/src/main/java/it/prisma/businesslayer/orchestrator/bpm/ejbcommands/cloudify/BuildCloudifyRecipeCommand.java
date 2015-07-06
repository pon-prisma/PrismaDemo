package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.iaas.cloudify.CloudifyManagement;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to customize a Cloudify recipe for the given
 * PaaSService deployment. <br/>
 * <br/>
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} deploymentMessage
 * @return The updated deploymentMessage with reference to updated
 *         {@link CloudifyDeploymentData} containing the customized recipe path.
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class BuildCloudifyRecipeCommand extends BaseCommand {

	@Inject
	private CloudifyManagement cloudifyBean;

	public BuildCloudifyRecipeCommand() throws Exception {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		DeploymentMessage depMsg = (DeploymentMessage) workItem
				.getParameter("deploymentMessage");
		Preconditions.checkNotNull(depMsg);

		ExecutionResults exResults = new ExecutionResults();

		try {
			CloudifyDeploymentData cfyDepData = (CloudifyDeploymentData) depMsg
					.getDeploymentData();

			String customizedRecipePath = cloudifyBean
					.customizeDeploymentRecipe(cfyDepData.getRecipeURL(),
							cfyDepData.getRecipePropertiesFile(),
							depMsg.getDeployRequest(), depMsg.getPaaSService());
			cfyDepData.setCustomizedRecipePath(customizedRecipePath);

			exResults.setData("deploymentMessage", depMsg);
			exResults.setData("customizedRecipePath", customizedRecipePath);
			resultOccurred(depMsg, exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}
}
