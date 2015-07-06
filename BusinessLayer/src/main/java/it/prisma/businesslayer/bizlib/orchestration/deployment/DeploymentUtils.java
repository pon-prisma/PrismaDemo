package it.prisma.businesslayer.bizlib.orchestration.deployment;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;

import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

@Singleton
public class DeploymentUtils {

	@Inject
	private EnvironmentConfig envConfig;

	public static String getStackName(AbstractPaaSService paasService) {
		return "stack_" + paasService.getPaaSService().getType() + "_"
				+ paasService.getPaaSService().getWorkgroup().getId() + "_"
				+ paasService.getPaaSService().getId() + "_"
				+ paasService.getPaaSService().getName();
	}

	public static String getAppName(AbstractPaaSService paasService) {
		return StringUtils.abbreviate(/* "app_" + */paasService.getPaaSService()
				.getType()
				+ "_"
				+ paasService.getPaaSService().getId()
				+ "_"
				+ paasService.getPaaSService().getWorkgroup().getId() /*
																	 * + "_" +
																	 * paasService
																	 * .
																	 * getPaaSService
																	 * (
																	 * ).getName
																	 * ()
																	 */, 40);
	}

	public static Object lookupValueFromTemplateOutputParams(
			List<Map<String, Object>> params, String key) {
		for (Map<String, Object> param : params) {
			if (param.get("output_key").equals(key))
				return param.get("output_value");
		}
		return null;
	}

	public String getHeatTemplatesBasePath() {
		return envConfig
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_URL);
	}
	
	public String getHeatIaaSTemplatesBasePath() {
		return getHeatTemplatesBasePath()
				+ envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_BASE_IAAS);
	}
	
	public String getHeatPaaSTemplatesBasePath() {
		return getHeatTemplatesBasePath()
				+ envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_BASE_PAAS);
	}
}
