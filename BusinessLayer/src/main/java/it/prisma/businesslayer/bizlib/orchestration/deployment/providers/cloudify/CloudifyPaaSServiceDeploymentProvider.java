package it.prisma.businesslayer.bizlib.orchestration.deployment.providers.cloudify;

import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProvider;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyDeploymentData;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

import java.util.Map;

/**
 * Deployment logic for a PaaSService to be deployed using Cloudify.
 * @author l.biava
 *
 * @param <PAAS_SERVICE_TYPE>
 * @param <DEPLOY_REQUEST_TYPE>
 */
public interface CloudifyPaaSServiceDeploymentProvider<PAAS_SERVICE_TYPE extends AbstractPaaSService, DEPLOY_REQUEST_TYPE extends GenericPaaSServiceDeployRequest<?>>
		extends
		PaaSServiceDeploymentProvider<PAAS_SERVICE_TYPE, DEPLOY_REQUEST_TYPE> {

	/**
	 * Should return the actual parameters to be replaced in the deployment
	 * recipe for the given deployment data.
	 * 
	 * @param deployRequest
	 * @param paasService
	 * @return
	 */
	public Map<String, String> getCustomizationParams(
			DEPLOY_REQUEST_TYPE deployRequest, PAAS_SERVICE_TYPE paasService);

	/**
	 * Should return the recipe data (name, version, URL, propertyFile) for the
	 * current deploy request.<br/>
	 * For convenience, this data is stored in {@link CloudifyDeploymentData}
	 * structure.
	 * 
	 * @param deployRequest
	 * @return
	 */
	public CloudifyDeploymentData getRecipeData(
			DEPLOY_REQUEST_TYPE deployRequest);
}