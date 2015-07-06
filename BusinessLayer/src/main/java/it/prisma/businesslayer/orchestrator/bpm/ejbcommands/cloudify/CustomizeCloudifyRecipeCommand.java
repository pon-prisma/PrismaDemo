package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.iaas.cloudify.CustomRecipe;
import it.prisma.businesslayer.bizlib.iaas.cloudify.CustomRecipe.ReplaceStrategy;
import it.prisma.businesslayer.bizws.paas.services.appaas.APPaaSWS;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent.LRTEventType;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ejb.Local;

import org.apache.commons.io.FilenameUtils;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * Bean implementing a command to access APPaaS Entity. <br/>
 * <br/>
 * Workitem parameters:
 * 
 * @param LRT
 *            the LRT to log events to.
 * @param Operation
 *            the requested operation (only CRUD supported: {create, read,
 *            update, delete}).
 * @param APPaaS
 *            the APPaaS Entity to create or update
 * @param APPaaSID
 *            the APPaaS Id to read or delete
 * @param WorkgroupID
 *            the APPaaS WorkgroupId for read operation (to use with name)
 * @param APPaaSName
 *            the APPaaS Name for read operation (to use with WorkgroupId)
 * 
 * @return [op=create], the newly created 'APPaaSEnvironment' entity. <br/>
 *         [op=read], the 'APPaaS' entity or null if it doesn't exist. <br/>
 *         [op=update], the updated 'APPaaS' entity. <br/>
 *         [op=delete], nothing.
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
@Local(CustomizeCloudifyRecipeCommand.class)
public class CustomizeCloudifyRecipeCommand extends BaseCommand {

	private class PrismaCloudifyReplaceStrategy implements ReplaceStrategy {

		protected static final String format = "%key%";

		@Override
		public String getReplaceFormat() {
			return format;
		}

		@Override
		public String getReplacePropertyKeyFormatted(String key) {
			return format.replace("key", key);
		}

	}

	public CustomizeCloudifyRecipeCommand() throws Exception {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		Map<String, Object> cfyRecipeInfo = (Map<String, Object>) workItem
				.getParameter("CfyRecipeInfo");
		String propertyFilePath = (String) cfyRecipeInfo
				.get("propertyFilePath");
		String recipePath = (String) workItem.getParameter("RecipePath");
		String customRecipePath = (String) workItem
				.getParameter("CustomRecipePath");
		Map<String, String> param = (Map<String, String>) workItem
				.getParameter("ReplaceParams");
		Object deployRequest = (Object) workItem.getParameter("DeployRequest");

		Object paasSvc = workItem.getParameter("PaaSService");
		if (paasSvc instanceof AppaaSEnvironment) {
			if (deployRequest instanceof APPaaSDeployRequest) {
				AppaaSEnvironment appaasEnv = (AppaaSEnvironment) paasSvc;

				param = new HashMap<String, String>();

				String sourcePath = envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_BIZLAYER_BASE_REST_URL)
						+ APPaaSWS.APPaaSEnvAppSrcFile_REST_URL;
				sourcePath = sourcePath.replace("{workgroupId}", appaasEnv
						.getPaaSService().getWorkgroup().getId().toString());
				sourcePath = sourcePath.replace("{envId}", appaasEnv.getId()
						.toString());
				sourcePath = sourcePath.replace("{appId}", appaasEnv
						.getAppaaS().getId().toString());
				param.put("sourcePath", sourcePath);
				param.put("warName", appaasEnv.getApplicationRepositoryEntry()
						.getFileName());

				param.put("flavor", appaasEnv.getPaaSService().getIaaSFlavor()
						.toLowerCase());
				param.put("profile", appaasEnv.getPaaSService().getQoS()
						.toLowerCase());
				param.put("instanceNumber", appaasEnv.getPaaSService()
						.getLoadBalancingInstances().toString());
				param.put("useLoadBalancer", appaasEnv.getPaaSService()
						.getLoadBalancingInstances() > 1 ? "true" : "false");
				param.put("userTomcat", "test");
				param.put("passTomcat", "test");
			}
		}

		// TODO Eliminare in futuro
		/*
		 * recipePath = this.getClass().getProtectionDomain().getCodeSource()
		 * .getLocation().getPath() + "/cloudify-recipes/" + recipePath;
		 */

		if (customRecipePath == null) {
			customRecipePath = FilenameUtils.getFullPath(recipePath)
					+ FilenameUtils.getBaseName(recipePath) + "_custom_"
					+ UUID.randomUUID().toString() + "_"
					+ System.currentTimeMillis() + "."
					+ FilenameUtils.getExtension(recipePath);
		}

		ExecutionResults exResults = new ExecutionResults();

		try {
			CustomRecipe customRecipe = new CustomRecipe(recipePath,
					customRecipePath, param, propertyFilePath);

			customRecipe
					.setReplaceStrategy(new PrismaCloudifyReplaceStrategy());

			resultOccurred(customRecipe.customize(), exResults);
			// resultOccurred(recipePath, exResults);
			lrtEventLogger.log(LRTEventType.INFO, lrt,
					"Customized recipe successfull.");
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					exResults);
			// TODO:
			// OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_CUSTOM_RECIPE_ERROR
		}

		return exResults;
	}
}
