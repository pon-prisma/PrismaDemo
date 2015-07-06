package it.prisma.businesslayer.bizlib.orchestration.deployment.providers.heat;

import it.prisma.businesslayer.bizlib.orchestration.deployment.DeploymentUtils;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.BasePaaSDeploymentProvider;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.openstack4j.model.heat.Stack;

public abstract class BaseHeatDeploymentProvider<PAAS_SERVICE_TYPE extends AbstractPaaSService, DEPLOY_REQUEST_TYPE extends GenericPaaSServiceDeployRequest<?>>
		extends
		BasePaaSDeploymentProvider<PAAS_SERVICE_TYPE, DEPLOY_REQUEST_TYPE>
		implements
		HeatPaaSServiceDeploymentProvider<PAAS_SERVICE_TYPE, DEPLOY_REQUEST_TYPE> {

	@Inject
	private EnvironmentConfig envConfig;

	
	
//	@Override
//	public PaaSDeployment getStackServicesInformations(OSClient client,
//			Stack stack, HeatDeploymentData heatDeploymentData,
//			PAAS_SERVICE_TYPE paasService) throws Exception {

		//TODO: Superiorize !
		
//		// TODO: Improve multiple VM data retrieval
//		List<Map<String, Object>> op = stack.getOutputs();
//
//		String stackReference = stack.getLinks().get(0).getHref();
//		PaaSDeployment paasDeployment = new PaaSDeployment("HEAT");
//		paasDeployment.setName(stackReference);
//
//		String status = PaaSService.Status.START_IN_PROGRESS.toString();
//		// if (appDescription.getApplicationState().equals("STARTED"))
//		status = PaaSService.Status.RUNNING.toString();
//		paasDeployment.setStatus(status);
//
//		// TODO: Implement PaaSDeployment creation for each PaaS service
//		// type
//		PaaSDeploymentService paasDS;
//		PaaSDeploymentServiceInstance paasDSI;
//
//		BIaaS biaas = (BIaaS) paasService;
//
//		// TODO: Remove after template updated
//		String vmPublicIP = (String) DeploymentUtils
//				.lookupValueFromTemplateOutputParams(op, "instance_ip");
//		String vmPrivateIP = vmPublicIP;
//		String vmID = (String) DeploymentUtils
//				.lookupValueFromTemplateOutputParams(op, "instance_id");
//		String vmName = vmID;
//
//		switch (EnumUtils.fromString(BIProductType.class,
//				biaas.getProductType())) {
//		case Pentaho:
//			// TODO: PENTAHO - Add services lookup !
//			// MySQL service + instance
//			paasDS = new PaaSDeploymentService(paasDeployment,
//					AtomicServiceType.DB_POSTGRE.toString(),
//					AtomicServiceType.DB_POSTGRE.toString());
//			paasDS.setStatus("STARTED");
//			paasDeployment.getPaaSDeploymentServices().add(paasDS);
//
//			paasDSI = new PaaSDeploymentServiceInstance(paasDS, vmPublicIP,
//					vmPrivateIP, "STARTED", vmName, vmID);
//			paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);
//
//			// Apache HTTPD service + instance
//			paasDS = new PaaSDeploymentService(paasDeployment,
//					AtomicServiceType.EX_PENTAHO.toString(),
//					AtomicServiceType.EX_PENTAHO.toString());
//			paasDS.setStatus("STARTED");
//			paasDeployment.getPaaSDeploymentServices().add(paasDS);
//
//			paasDSI = new PaaSDeploymentServiceInstance(paasDS, vmPublicIP,
//					vmPrivateIP, "STARTED", vmName, vmID);
//			paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);
//			return paasDeployment;
//		}
//
//		throw new UnsupportedOperationException(
//				"Cannot extract Heat params for service " + paasService);
//	}

	/**
	 * Default implementation, populating the following parameters:
	 * <ul>
	 * <li>vm_name</li>
	 * <li>key_name</li>
	 * <li>instance_type</li>
	 * <li>net_id</li>
	 * </ul>
	 */
	@Override
	public Map<String, String> getDeploymentTemplateParameters(
			HeatDeploymentData heatDeploymentData,
			DEPLOY_REQUEST_TYPE deployRequest, PAAS_SERVICE_TYPE paasService)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();

		PaaSService paasSvc = paasService.getPaaSService();

		params.put("vm_name", "vm_" + DeploymentUtils.getStackName(paasService));
		params.put("key_name", envConfig.getOpenStackPresets().getPaaSKey());

		params.put("instance_type",
				paasSvc.getIaaSFlavor() + "." + paasSvc.getQoS());

		// La versione deve essere 12 se cambia avvertire il
		// monitoring,
		// ci sarà da cambiare il template che istalla zabbix-agent
		// params.put("image_id", envConfig.getTestbed().get("INFN")
		// .getImages().get("ubuntu12.04lts"));
		// params.put("shared_net_id", envConfig.getTestbed().get("INFN")
		// .getNetworks().get("shared_net_id"));
		params.put("net_id", envConfig.getOpenStackPresets().getNetworks()
				.get("net_id"));

		return params;
	}

	protected PaaSDeployment createHeatPaaSDeployment(Stack stack) {
		String stackReference = stack.getLinks().get(0).getHref();
		return createPaaSDeployment("HEAT", stackReference);
	}	
}
