package it.prisma.businesslayer.bizlib.orchestration.deployment.providers.heat;

import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProvider;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

import java.util.Map;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.heat.Stack;

/**
 * Deployment logic for a PaaSService to be deployed using Heat.
 * @author l.biava
 *
 * @param <PAAS_SERVICE_TYPE>
 * @param <DEPLOY_REQUEST_TYPE>
 */
public interface HeatPaaSServiceDeploymentProvider<PAAS_SERVICE_TYPE extends AbstractPaaSService, DEPLOY_REQUEST_TYPE extends GenericPaaSServiceDeployRequest<?>>
		extends
		PaaSServiceDeploymentProvider<PAAS_SERVICE_TYPE, DEPLOY_REQUEST_TYPE> {

	public PaaSDeployment getStackServicesInformations(OSClient client,
			Stack stack, HeatDeploymentData heatDeploymentData,
			PAAS_SERVICE_TYPE paasService) throws Exception;

	
	public Map<String, String> getDeploymentTemplateParameters(
			HeatDeploymentData heatDeploymentData,
			DEPLOY_REQUEST_TYPE deployRequest, PAAS_SERVICE_TYPE paasService)
			throws Exception;

	/**
	 * Should return the template data (name, version, URL) for the current
	 * deploy request.<br/>
	 * For convenience, this data is stored in {@link HeatDeploymentData}
	 * structure.
	 * 
	 * @param deployRequest
	 * @return
	 */
	public HeatDeploymentData getTemplateData(DEPLOY_REQUEST_TYPE deployRequest);
}