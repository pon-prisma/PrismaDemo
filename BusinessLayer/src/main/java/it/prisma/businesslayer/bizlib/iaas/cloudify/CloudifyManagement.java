package it.prisma.businesslayer.bizlib.iaas.cloudify;

import it.prisma.businesslayer.bizlib.orchestration.deployment.DeployManagement;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

public interface CloudifyManagement extends DeployManagement {

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
			CloudifyAPIClient client, String appName) throws Exception;

	/**
	 * Customizes the given recipe based on given PaaSService type & deployment
	 * data.
	 * 
	 * @param recipeURL
	 *            the URL to the original recipe to customize. <b>CURRENTLY IT
	 *            MUST BE IN THE LOCAL FILESYSTEM</b>.
	 * @param deployRequest
	 * @param paasService
	 * @return the path to the customized recipe (currently it should be in the
	 *         local FileSystem).
	 * @throws Exception
	 */
	public String customizeDeploymentRecipe(String recipeURL,
			String recipePropertiesFile,
			GenericPaaSServiceDeployRequest<?> deployRequest,
			AbstractPaaSService paasService) throws Exception;

	public String buildApplicationName(AbstractPaaSService paasService);
}