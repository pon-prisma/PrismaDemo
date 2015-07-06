package it.prisma.businesslayer.bizlib.dns;

import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;

import java.util.ArrayList;
import java.util.List;

public class DNSUtils {

	public static List<String> getSingleHostIPAddresses(
			AbstractPaaSService paasService) {
		List<String> addresses = new ArrayList<String>();
		for (PaaSDeploymentService svc : paasService.getPaaSService()
				.getPaaSDeployment().getPaaSDeploymentServices()) {
			for (PaaSDeploymentServiceInstance lbSvcInst : svc
					.getPaaSDeploymentServiceInstances()) {
				String serviceIP = lbSvcInst.getPublicIP();

				// TODO: Restore
				// if (serviceIP == null || serviceIP.equals(""))
				// throw new Exception("No public IP to register to DNS."
				// + lbSvcInst.toString());
				if (serviceIP == null || serviceIP.equals(""))
					serviceIP = lbSvcInst.getPrivateIP();

				addresses.add(serviceIP);
				return addresses;
			}
		}
		return addresses;
	}

	public static List<String> getLoadBalancerIPAddresses(
			AbstractPaaSService paasService) {
		List<String> addresses = new ArrayList<String>();
		PaaSDeploymentService lbSvc = getLBService(paasService.getPaaSService()
				.getPaaSDeployment());
		for (PaaSDeploymentServiceInstance lbSvcInst : lbSvc
				.getPaaSDeploymentServiceInstances()) {
			String serviceIP = lbSvcInst.getPublicIP();

			// TODO: Restore
			// if (serviceIP == null || serviceIP.equals(""))
			// throw new Exception("No public IP to register to DNS."
			// + lbSvcInst.toString());
			if (serviceIP == null || serviceIP.equals(""))
				serviceIP = lbSvcInst.getPrivateIP();

			addresses.add(serviceIP);
		}
		return addresses;
	}

	public static PaaSDeploymentService getLBService(
			PaaSDeployment paasDeployment) {
		for (PaaSDeploymentService item : paasDeployment
				.getPaaSDeploymentServices()) {
			// TODO: Fix naming
			if (item.getType().contains("LB")
					|| item.getType().contains("apache")
					|| item.getType().contains("haproxy"))
				return item;
		}
		return null;
	}

}
