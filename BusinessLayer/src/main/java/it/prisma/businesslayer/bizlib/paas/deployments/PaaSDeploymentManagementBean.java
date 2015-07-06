package it.prisma.businesslayer.bizlib.paas.deployments;

import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

@Stateless
public class PaaSDeploymentManagementBean implements PaaSDeploymentManagement {

	@Override
	public Map<String, List<PaaSDeploymentServiceInstance>> groupByHost(
			PaaSDeployment paasDeployment) {
		Map<String, List<PaaSDeploymentServiceInstance>> hosts = new HashMap<String, List<PaaSDeploymentServiceInstance>>();

		// Group by hostId
		for (PaaSDeploymentService depSvc : paasDeployment
				.getPaaSDeploymentServices()) {
			for (PaaSDeploymentServiceInstance depInst : depSvc
					.getPaaSDeploymentServiceInstances()) {
				List<PaaSDeploymentServiceInstance> instances = hosts
						.get(depInst.getHostId());

				if (instances == null) {
					instances = new ArrayList<PaaSDeploymentServiceInstance>();
					hosts.put(depInst.getHostId(), instances);
				}

				instances.add(depInst);
			}
		}
		return hosts;
	}

}
