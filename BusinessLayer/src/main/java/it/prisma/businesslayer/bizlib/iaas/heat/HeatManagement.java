package it.prisma.businesslayer.bizlib.iaas.heat;

import it.prisma.businesslayer.bizlib.orchestration.deployment.DeployManagement;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

import java.util.Map;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.heat.Stack;

public interface HeatManagement extends DeployManagement {

	/**
	 * Returns informations about all the services (and their instances) of an
	 * Heat stack, converting to Prisma {@link PaaSDeployment} structure.
	 *
	 * @param client
	 *            an {@link OSClient}.
	 * @param stack
	 *            the Heat stack data.
	 * @param heatDeploymentData
	 *            the deployment data (ie. informations about Heat template to
	 *            use).
	 * @param paasService
	 * @return a {@link PaaSDeployment} containing all application service
	 *         instances informations (MUST be persisted manually).
	 * @throws Exception
	 */
	public PaaSDeployment getStackServicesInformations(OSClient client,
			Stack stack,  HeatDeploymentData heatDeploymentData,			
			AbstractPaaSService paasService) throws Exception;

	/**
	 * Generates the parameters needed for the deploy of the given PaaSService
	 * type & deployment data (ie. the Heat template).
	 * 
	 * @param heatDeploymentData
	 *            the deployment data (ie. informations about Heat template to
	 *            use).
	 * @param deployRequest
	 * @param paasService
	 * @return a map containing the populate parameters.
	 * @throws Exception
	 */
	public Map<String, String> getDeploymentTemplateParameters(
			HeatDeploymentData heatDeploymentData,
			GenericPaaSServiceDeployRequest<?> deployRequest,
			AbstractPaaSService paasService) throws Exception;

	public String buildStackName(AbstractPaaSService paasService);
}