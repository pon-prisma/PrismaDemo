package it.prisma.businesslayer.bizlib.paas.deployments;

import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;

import java.util.List;
import java.util.Map;

public interface PaaSDeploymentManagement {

	public Map<String, List<PaaSDeploymentServiceInstance>> groupByHost(
			PaaSDeployment paasDeployment);
}
