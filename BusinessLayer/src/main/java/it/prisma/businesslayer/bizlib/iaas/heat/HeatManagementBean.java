package it.prisma.businesslayer.bizlib.iaas.heat;

import it.prisma.businesslayer.bizlib.orchestration.deployment.DeploymentUtils;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProvider;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProviderRegistry;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.heat.HeatPaaSServiceDeploymentProvider;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEndpoint;
import it.prisma.dal.entities.paas.services.dbaas.DBaaS;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.paas.deployments.PaaSDeploymentServiceRepresentation.PaaSDeploymentServiceRepresentationType;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation.DBProductType;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.utils.misc.EnumUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.heat.Stack;

public class HeatManagementBean implements HeatManagement {

	@Inject
	private EnvironmentConfig envConfig;

	@Inject
	private PaaSServiceDeploymentProviderRegistry deploymentProviders;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, String> getDeploymentTemplateParameters(
			HeatDeploymentData heatDeploymentData,
			GenericPaaSServiceDeployRequest<?> deployRequest,
			AbstractPaaSService paasService) throws Exception {

		// TODO: move customization params generation in something related
		// to each PaaSService ?
		Map<String, String> params = new HashMap<String, String>();

		// TODO: lookup images and so on based to the Prisma Zone
		if (deployRequest instanceof DBaaSDeployRequest) {
			PaaSService paasSvc = paasService.getPaaSService();
			DBaaSDeployRequest dbRequest = (DBaaSDeployRequest) deployRequest;

			params.put("vm_name", "vm_" + buildStackName(paasService));
			params.put("instance_type",
					paasSvc.getIaaSFlavor() + "." + paasSvc.getQoS());

			params.put("key_name", envConfig.getOpenStackPresets()
					.getPaaSKey());
			// La versione deve essere 12 se cambia avvertire il
			// monitoring,
			// ci sarà da cambiare il template che istalla zabbix-agent
			// params.put("image_id", envConfig.getTestbed().get("INFN")
			// .getImages().get("ubuntu12.04lts"));
			// params.put("shared_net_id", envConfig.getTestbed().get("INFN")
			// .getNetworks().get("shared_net_id"));
			params.put("net_id", envConfig.getOpenStackPresets()
					.getNetworks().get("net_id"));

			params.put("db_name", dbRequest.getDBaasDetails().getDatabaseName());
			params.put("db_root_password", dbRequest.getDBaasDetails()
					.getRootPassword());

			if (dbRequest.getDBaasDetails().getExtraUser() != null) {
				params.put("db_create_user", "create");
				params.put("db_username", dbRequest.getDBaasDetails()
						.getExtraUser().getUsername());
				params.put("db_password", dbRequest.getDBaasDetails()
						.getExtraUser().getPassword());
			} else {
				params.put("db_create_user", "do_not_create");
			}

			return params;
		} else if (deployRequest instanceof BIaaSDeployRequest
				|| deployRequest instanceof VMDeployRequest) {
			return ((HeatPaaSServiceDeploymentProvider) deploymentProviders
					.getProviderByPaaSType(paasService.getPaaSService()
							.getType())).getDeploymentTemplateParameters(
					heatDeploymentData, deployRequest, paasService);
		}

		throw new UnsupportedOperationException(
				"Cannot generate Heat template params for service "
						+ paasService);
	}

	public String buildStackName(AbstractPaaSService paasService) {
		return DeploymentUtils.getStackName(paasService);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaaSDeployment getStackServicesInformations(OSClient client,
			Stack stack, HeatDeploymentData heatDeploymentData,
			AbstractPaaSService paasService) throws Exception {
		try {

			// TODO: Improve multiple VM data retrieval
			List<Map<String, Object>> op = stack.getOutputs();
			String vmPublicIP = (String) DeploymentUtils
					.lookupValueFromTemplateOutputParams(op, "instance_ip");
			String vmPrivateIP = vmPublicIP;
			String vmID = (String) DeploymentUtils
					.lookupValueFromTemplateOutputParams(op, "instance_id");
			String vmName = vmID;

			String stackReference = stack.getLinks().get(0).getHref();
			PaaSDeployment paasDeployment = new PaaSDeployment("HEAT");
			paasDeployment.setName(stackReference);

			String status = PaaSService.Status.START_IN_PROGRESS.toString();
			// if (appDescription.getApplicationState().equals("STARTED"))
			
			//After monitoring command we could set to RUNNING
			//status = PaaSService.Status.RUNNING.toString();
			paasDeployment.setStatus(status);

			// TODO: Implement PaaSDeployment creation for each PaaS service
			// type
			PaaSDeploymentService paasDS;
			PaaSDeploymentServiceInstance paasDSI;
			switch (PaaSService.PaaSServiceType.valueOf(paasService
					.getPaaSService().getType())) {
			case VMaaS:
			case BIaaS:
				return ((HeatPaaSServiceDeploymentProvider) deploymentProviders
						.getProviderByPaaSType(paasService.getPaaSService()
								.getType())).getStackServicesInformations(
						client, stack, heatDeploymentData, paasService);
				// BIaaS biaas = (BIaaS) paasService;
				//
				// //TODO: Remove after template updated
				// vmPublicIP = (String) lookupValueFromOutputParams(op,
				// "vmIp");
				// vmPrivateIP = vmPublicIP;
				// vmID = (String) lookupValueFromOutputParams(op,
				// "vmUuid");
				// vmName = vmID;
				//
				// switch (EnumUtils.fromString(BIProductType.class,
				// biaas.getProductType())) {
				// case Pentaho:
				// //TODO: PENTAHO - Add services lookup !
				// // MySQL service + instance
				// paasDS = new PaaSDeploymentService(paasDeployment,
				// AtomicServiceType.DB_POSTGRE.toString(),
				// AtomicServiceType.DB_POSTGRE.toString());
				// paasDS.setStatus("STARTED");
				// paasDeployment.getPaaSDeploymentServices().add(paasDS);
				//
				// paasDSI = new PaaSDeploymentServiceInstance(paasDS,
				// vmPublicIP, vmPrivateIP, "STARTED", vmName, vmID);
				// paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);
				//
				// // Apache HTTPD service + instance
				// paasDS = new PaaSDeploymentService(paasDeployment,
				// AtomicServiceType.EX_PENTAHO.toString(),
				// AtomicServiceType.EX_PENTAHO.toString());
				// paasDS.setStatus("STARTED");
				// paasDeployment.getPaaSDeploymentServices().add(paasDS);
				//
				// paasDSI = new PaaSDeploymentServiceInstance(paasDS,
				// vmPublicIP, vmPrivateIP, "STARTED", vmName, vmID);
				// paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);
				// return paasDeployment;
				// }
				// break;
			case DBaaS:
				DBaaS dbaas = (DBaaS) paasService;

				switch (EnumUtils.fromString(DBProductType.class,
						dbaas.getProductType())) {
				case MySQL:
					// MySQL service + instance
					paasDS = new PaaSDeploymentService(paasDeployment,
							PaaSDeploymentServiceRepresentationType.DB_MySQL.toString(),
							PaaSDeploymentServiceRepresentationType.DB_MySQL.toString());
					paasDS.setStatus(PaaSDeploymentServiceInstance.StatusType.STARTING.toString());
					paasDeployment.getPaaSDeploymentServices().add(paasDS);

					paasDSI = new PaaSDeploymentServiceInstance(paasDS,
							vmPublicIP, vmPrivateIP, PaaSDeploymentServiceInstance.StatusType.STARTING.toString(), vmName, vmID);
					paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);

					// Apache HTTPD service + instance
					paasDS = new PaaSDeploymentService(paasDeployment,
							PaaSDeploymentServiceRepresentationType.WS_APACHE_HTTPD.toString(),
							PaaSDeploymentServiceRepresentationType.WS_APACHE_HTTPD.toString());
					paasDS.setStatus(PaaSDeploymentServiceInstance.StatusType.STARTING.toString());
					paasDeployment.getPaaSDeploymentServices().add(paasDS);

					paasDSI = new PaaSDeploymentServiceInstance(paasDS,
							vmPublicIP, vmPrivateIP, PaaSDeploymentServiceInstance.StatusType.STARTING.toString(), vmName, vmID);
					paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);
					return paasDeployment;
				case PostgreSQL:
					// PostgreSQL service + instance
					paasDS = new PaaSDeploymentService(paasDeployment,
							PaaSDeploymentServiceRepresentationType.DB_POSTGRE.toString(),
							PaaSDeploymentServiceRepresentationType.DB_POSTGRE.toString());
					paasDS.setStatus(PaaSDeploymentServiceInstance.StatusType.STARTING.toString());
					paasDeployment.getPaaSDeploymentServices().add(paasDS);

					paasDSI = new PaaSDeploymentServiceInstance(paasDS,
							vmPublicIP, vmPrivateIP, PaaSDeploymentServiceInstance.StatusType.STARTING.toString(), vmName, vmID);
					paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);

					// Apache HTTPD service + instance
					paasDS = new PaaSDeploymentService(paasDeployment,
							PaaSDeploymentServiceRepresentationType.WS_APACHE_HTTPD.toString(),
							PaaSDeploymentServiceRepresentationType.WS_APACHE_HTTPD.toString());
					paasDS.setStatus(PaaSDeploymentServiceInstance.StatusType.STARTING.toString());
					paasDeployment.getPaaSDeploymentServices().add(paasDS);

					paasDSI = new PaaSDeploymentServiceInstance(paasDS,
							vmPublicIP, vmPrivateIP, PaaSDeploymentServiceInstance.StatusType.STARTING.toString(), vmName, vmID);
					paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);
					return paasDeployment;
				default:
				}
			default:
				throw new UnsupportedOperationException(
						"Cannot extract Heat params for service " + paasService);
			}

		} catch (Exception ex) {
			throw new Exception("Cannot to extract Heat params for stack "
					+ stack, ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<PaaSServiceEndpoint> getPaaSServiceEnpoints(
			AbstractPaaSService paasService) {
		switch (PaaSService.PaaSServiceType.valueOf(paasService
				.getPaaSService().getType())) {
		case VMaaS:
		case BIaaS:
			return ((PaaSServiceDeploymentProvider) deploymentProviders
					.getProviderByPaaSType(paasService.getPaaSService()
							.getType())).getPaaSServiceEndpoints(paasService);
		default:
			// TODO: Implement
			return new ArrayList<PaaSServiceEndpoint>();
		}
	}

}
