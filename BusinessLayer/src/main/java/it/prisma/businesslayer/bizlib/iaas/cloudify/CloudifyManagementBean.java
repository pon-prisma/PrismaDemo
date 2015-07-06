package it.prisma.businesslayer.bizlib.iaas.cloudify;

import it.prisma.businesslayer.bizlib.iaas.cloudify.CustomRecipe.ReplaceStrategy;
import it.prisma.businesslayer.bizlib.orchestration.deployment.DeploymentUtils;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProviderRegistry;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.businesslayer.bizws.paas.services.appaas.APPaaSWS;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.PaaSServiceEndpoint;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.domain.dsl.cloudify.response.ApplicationDescriptionResponse;
import it.prisma.domain.dsl.cloudify.response.InstancesDescription;
import it.prisma.domain.dsl.cloudify.response.ServiceInstanceDescriptionResponse;
import it.prisma.domain.dsl.cloudify.response.ServicesDescription;
import it.prisma.domain.dsl.paas.deployments.PaaSDeploymentServiceRepresentation.PaaSDeploymentServiceRepresentationType;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class CloudifyManagementBean implements CloudifyManagement {

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

	@Inject
	private EnvironmentConfig envConfig;

	protected static Logger LOG = LogManager
			.getLogger(CloudifyManagementBean.class);

	private static final String TAG = "[CloudifyBean] ";

	@Inject
	private PaaSServiceDeploymentProviderRegistry deploymentProviders;

	/**
	 * Returns informations about all the services (and their instances) an
	 * application.
	 * 
	 * @param client
	 *            an {@link CloudifyAPIClient}.
	 * @param appName
	 *            the name of the application.
	 * @param paasService
	 * @return a {@link PaaSDeployment} containing all application service
	 *         instances informations (MUST be persisted manually).
	 * @throws Exception
	 */
	public PaaSDeployment getApplicationServicesInformations(
			CloudifyAPIClient client, String appName) throws Exception {

		try {

			LOG.debug(
					TAG
							+ " Getting application {} service instances informations.",
					appName);

			ApplicationDescriptionResponse appDescription = client
					.ApplicationDescription(appName);
			PaaSDeployment paasD = new PaaSDeployment("CLOUDIFY");
			paasD.setName(appName);
			String status = PaaSService.Status.START_IN_PROGRESS.toString();
			if (appDescription.getApplicationState().equals("STARTED"))
				status = PaaSService.Status.RUNNING.toString();
			paasD.setStatus(status);

			Set<PaaSDeploymentService> services = new HashSet<PaaSDeploymentService>();
			paasD.setPaaSDeploymentServices(services);

			for (ServicesDescription cfySvc : appDescription
					.getServicesDescription()) {

				LOG.debug(
						TAG
								+ " Getting application {} service {} instances informations.",
						appName, cfySvc.getServiceName());

				PaaSDeploymentService service = new PaaSDeploymentService(
						paasD, cfySvc.getServiceName(),
						lookupServiceType(cfySvc.getServiceName()));
				Set<PaaSDeploymentServiceInstance> instances = new HashSet<PaaSDeploymentServiceInstance>();
				service.setCurrentInstances(cfySvc.getInstanceCount());
				service.setPlannedInstances(cfySvc.getPlannedInstances());
				service.setPaaSDeploymentServiceInstances(instances);

				services.add(service);

				for (InstancesDescription cfyInst : cfySvc
						.getInstancesDescription()) {

					LOG.debug(
							TAG
									+ " Getting application {} service {} instance {} informations.",
							appName, cfySvc.getServiceName(),
							cfyInst.getInstanceName());

					ServiceInstanceDescriptionResponse cfyInstMetadata = client
							.ServiceInstanceDescription(appName, service
									.getName(), cfyInst.getInstanceId()
									.toString());

					PaaSDeploymentServiceInstance instance = new PaaSDeploymentServiceInstance(
							service, cfyInstMetadata.getPublicIp(),
							cfyInstMetadata.getPrivateIp(),
							cfyInst.getInstanceStatus(), cfyInst
									.getInstanceId().toString(),
							cfyInstMetadata.getMachineId());

					instances.add(instance);
				}
			}

			LOG.debug(
					TAG
							+ " Application (%) service instances informations retrieved successfully.",
					appName);

			return paasD;
		} catch (Exception ex) {
			throw new Exception("Failed to retrieve Cloudify application ("
					+ appName + ") service instance informations.", ex);
		}
	}

	public static String lookupServiceType(String serviceName) {
		// TODO Improve deeply and move out of here !
		switch (serviceName) {
		case "tomcat":
			return PaaSDeploymentServiceRepresentationType.AS_TOMCAT.toString();
		case "apacheLB":
			return PaaSDeploymentServiceRepresentationType.LB_APACHE.toString();
		case "rabbitmq":
			return PaaSDeploymentServiceRepresentationType.MQ_RABBITMQ.toString();
		case "haproxy":
			return PaaSDeploymentServiceRepresentationType.LB_HAPROXY.toString();
		default:
			if (serviceName.contains("LB"))
				return PaaSDeploymentServiceRepresentationType.LB_APACHE.toString();

			return serviceName;
		}

	}

	protected Map<String, String> getCustomizationParams(
			GenericPaaSServiceDeployRequest deployRequest,
			AbstractPaaSService paasService) {

		// TODO: move customization parameters generation in something related
		// to each PaaSService
		Map<String, String> params = new HashMap<String, String>();

		switch (PaaSServiceType.valueOf(paasService.getPaaSService().getType())) {
		case APPaaSEnvironment:
			AppaaSEnvironment appaasEnv = (AppaaSEnvironment) paasService;

			String sourcePath = envConfig
					.getSvcEndpointProperty(EnvironmentConfig.SVCEP_BIZLAYER_BASE_REST_URL)
					+ APPaaSWS.APPaaSEnvAppSrcFile_REST_URL;
			sourcePath = sourcePath.replace("{workgroupId}", appaasEnv
					.getPaaSService().getWorkgroup().getId().toString());
			sourcePath = sourcePath.replace("{envId}", appaasEnv.getId()
					.toString());
			sourcePath = sourcePath.replace("{appId}", appaasEnv.getAppaaS()
					.getId().toString());
			params.put("sourcePath", sourcePath);
			params.put("warName", appaasEnv.getApplicationRepositoryEntry()
					.getFileName());

			params.put("flavor", appaasEnv.getPaaSService().getIaaSFlavor()
					.toLowerCase());
			params.put("profile", appaasEnv.getPaaSService().getQoS()
					.toLowerCase());
			params.put("instanceNumber", appaasEnv.getPaaSService()
					.getLoadBalancingInstances().toString());
			params.put("useLoadBalancer", appaasEnv.getPaaSService()
					.getLoadBalancingInstances() > 1 ? "true" : "false");
			params.put("userTomcat", "test");
			params.put("passTomcat", "test");

			return params;
		case MQaaS:
			MQaaSDeployRequest mqRequest = (MQaaSDeployRequest) deployRequest;

			params.put("flavor", paasService.getPaaSService().getIaaSFlavor()
					.toLowerCase());
			params.put("qos", paasService.getPaaSService().getQoS()
					.toLowerCase());
			params.put("instanceNumber", paasService.getPaaSService()
					.getLoadBalancingInstances().toString());
			params.put("useLoadBalancer", paasService.getPaaSService()
					.getLoadBalancingInstances() > 1 ? "true" : "false");
			params.put("userAdmin", mqRequest.getMQaasDetails().getUsername());
			params.put("passAdmin", mqRequest.getMQaasDetails().getPassword());

			return params;

		default:
		}

		throw new UnsupportedOperationException(
				"Cannot customize Cloudify recipe params for service "
						+ paasService);
	}

	@Override
	public String customizeDeploymentRecipe(String recipeURL,
			String recipePropertiesFile,
			GenericPaaSServiceDeployRequest deployRequest,
			AbstractPaaSService paasService) throws Exception {

		// TODO: lookup properties file path
		String propertyFilePath = recipePropertiesFile;

		Map<String, String> params = getCustomizationParams(deployRequest,
				paasService);

		String customRecipePath = FilenameUtils.getFullPath(recipeURL)
				+ FilenameUtils.getBaseName(recipeURL) + "_custom_"
				+ UUID.randomUUID().toString() + "_"
				+ System.currentTimeMillis() + "."
				+ FilenameUtils.getExtension(recipeURL);

		CustomRecipe customRecipe = new CustomRecipe(recipeURL,
				customRecipePath, params, propertyFilePath);

		customRecipe.setReplaceStrategy(new PrismaCloudifyReplaceStrategy());

		return customRecipe.customize();
	}

	public String buildApplicationName(AbstractPaaSService paasService) {
		return DeploymentUtils.getAppName(paasService);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<PaaSServiceEndpoint> getPaaSServiceEnpoints(
			AbstractPaaSService paasService) {
		switch (PaaSService.PaaSServiceType.valueOf(paasService
				.getPaaSService().getType())) {
		// case APPaaSEnvironment:
		// case MQaaS:
		// return ((PaaSServiceDeploymentProvider) deploymentProviders
		// .getProviderByPaaSType(paasService.getPaaSService()
		// .getType())).getPaaSServiceEndpoints(paasService);
		default:
			// TODO: Implement
			return new ArrayList<PaaSServiceEndpoint>();
		}
	}
}