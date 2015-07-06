package it.prisma.businesslayer.bizlib.iaas.services.vm;

import it.prisma.businesslayer.bizlib.common.exceptions.BadRequestException;
import it.prisma.businesslayer.bizlib.orchestration.deployment.DeploymentUtils;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSDeploymentProvider;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.heat.BaseHeatDeploymentProvider;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.heat.HeatPaaSServiceDeploymentProvider;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentDataBuilder;
import it.prisma.businesslayer.utils.mappinghelpers.iaas.vm.VMMappingHelper;
import it.prisma.dal.dao.iaas.services.IaaSNetworkDAO;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.iaas.services.IaaSNetwork;
import it.prisma.dal.entities.iaas.services.vm.VMaaS;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.paas.deployments.PaaSDeploymentServiceRepresentation.PaaSDeploymentServiceRepresentationType;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.heat.Stack;

@Stateless
@PaaSDeploymentProvider
public class VMaaSDeploymentProvider extends
		BaseHeatDeploymentProvider<VMaaS, VMDeployRequest> implements
		HeatPaaSServiceDeploymentProvider<VMaaS, VMDeployRequest> {

	@Inject
	private EnvironmentConfig envConfig;

	@Inject
	private IaaSNetworkDAO netDAO;

	@Override
	public PaaSServiceType getPaaSServiceType() {
		return PaaSServiceType.VMaaS;
	}

	@Override
	public VMaaS generateAndValidatePaaSService(VMDeployRequest deployRequest,
			UserAccount user, Workgroup workgroup) {

		// Network exists ?
		Long netId = Long.valueOf(((VMDeployRequest) deployRequest)
				.getVmDetails().getNetworks());
		IaaSNetwork networkEntity = netDAO.findById(netId);
		if (networkEntity == null)
			throw new BadRequestException("Cannot find network with id="
					+ netId);

		return VMMappingHelper
				.generateFromDeployRequest((VMDeployRequest) deployRequest,
						networkEntity, user, workgroup);
	}

	@Override
	public PaaSDeployment getStackServicesInformations(OSClient client,
			Stack stack, HeatDeploymentData heatDeploymentData,
			VMaaS paasService) throws Exception {

		// TODO: Improve multiple VM data retrieval
		List<Map<String, Object>> op = stack.getOutputs();

		PaaSDeployment paasDeployment = createHeatPaaSDeployment(stack);

		// TODO: Implement PaaSDeployment creation for each PaaS service
		// type
		PaaSDeploymentService paasDS;
		PaaSDeploymentServiceInstance paasDSI;

		// TODO: Remove after template updated
		String vmPublicIP = (String) DeploymentUtils
				.lookupValueFromTemplateOutputParams(op, "instance_ip");
		String vmPrivateIP = vmPublicIP;
		String vmID = (String) DeploymentUtils
				.lookupValueFromTemplateOutputParams(op, "instance_id");
		String vmName = vmID;

		// VM Linux service + instance
		paasDS = new PaaSDeploymentService(paasDeployment,
				PaaSDeploymentServiceRepresentationType.VM_LINUX.toString(),
				PaaSDeploymentServiceRepresentationType.VM_LINUX.toString());
		paasDS.setStatus(PaaSDeploymentServiceInstance.StatusType.STARTING.toString());
		paasDeployment.getPaaSDeploymentServices().add(paasDS);

		paasDSI = new PaaSDeploymentServiceInstance(paasDS, vmPublicIP,
				vmPrivateIP, PaaSDeploymentServiceInstance.StatusType.STARTING.toString(), vmName, vmID);
		paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);

		return paasDeployment;
	}

	@Override
	public Map<String, String> getDeploymentTemplateParameters(
			HeatDeploymentData heatDeploymentData,
			VMDeployRequest deployRequest, VMaaS paasService) throws Exception {
		Map<String, String> params = super.getDeploymentTemplateParameters(
				heatDeploymentData, deployRequest, paasService);

		params.put("key_name", deployRequest.getVmDetails().getKey());
		params.put("net_id", paasService.getIaaSNetworks().iterator().next()
				.getOpenstackId());

		String image = deployRequest.getVmDetails().getImageName();

		params.put("image_id",
				envConfig.getOpenStackPresets().getImages().get(image));

		if (deployRequest.getVmDetails().getVolume() != 0) {
			params.put("volume_size", deployRequest.getVmDetails().getVolume()
					+ "");
		}

		return params;
	}

	@Override
	public HeatDeploymentData getTemplateData(VMDeployRequest deployRequest) {
		String template = "vm/";

		if (deployRequest.getVmDetails().getVolume() == 0) {
			template += "vm.template";
		} else {
			template += "vm_volume.template";
		}

		return HeatDeploymentDataBuilder
				.heatDeploymentData()
				.withTemplateURL(
						getDeploymentUtils().getHeatIaaSTemplatesBasePath()
								+ "/" + template).withTemplateName(template)
				.build();
	}

	@Override
	public VMDeployRequest castToProductSpecificRequest(
			VMDeployRequest deployRequest) throws Exception {
		return deployRequest;
	}

}
