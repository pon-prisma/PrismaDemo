package it.prisma.businesslayer.bizlib.orchestration.deployment.providers.cloudify;

import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.BasePaaSDeploymentProvider;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

import org.openstack4j.model.heat.Stack;

public abstract class BaseCloudifyDeploymentProvider<PAAS_SERVICE_TYPE extends AbstractPaaSService, DEPLOY_REQUEST_TYPE extends GenericPaaSServiceDeployRequest<?>>
		extends
		BasePaaSDeploymentProvider<PAAS_SERVICE_TYPE, DEPLOY_REQUEST_TYPE>
		implements
		CloudifyPaaSServiceDeploymentProvider<PAAS_SERVICE_TYPE, DEPLOY_REQUEST_TYPE> {

	protected PaaSDeployment createCloudifyPaaSDeployment(Stack stack) {
		String stackReference = stack.getLinks().get(0).getHref();
		return createPaaSDeployment("HEAT", stackReference);
	}
}
