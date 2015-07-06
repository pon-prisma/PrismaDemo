package it.prisma.businesslayer.utils.mappinghelpers.iaas.vm;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.businesslayer.utils.mappinghelpers.PaaSServiceMappingHelper;
import it.prisma.dal.dao.iaas.services.IaaSNetworkDAO;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.iaas.services.IaaSNetwork;
import it.prisma.dal.entities.iaas.services.IaaSSecurityGroup;
import it.prisma.dal.entities.iaas.services.vm.VMaaS;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.iaas.vmaas.request.VmDetails;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class VMMappingHelper extends MappingHelperBase<VMaaS, VMRepresentation> {

	@Inject
	protected MappingHelper<PaaSService, PaaSServiceRepresentation> paasServiceMH;

	@Inject
	private IaaSNetworkDAO netDAO;
	
	
	public static VMaaS generateFromDeployRequest(VMDeployRequest request, IaaSNetwork networkEntity, UserAccount userAccount,
			Workgroup workgroup) {

		VMaaS vm = new VMaaS();
		PaaSService paassvc = PaaSServiceMappingHelper
				.generateFromPaaSServiceDeployRequest(workgroup, userAccount,
						request, PaaSServiceType.VMaaS);

		vm.setPaaSService(paassvc);
		
		VmDetails det = request.getVmDetails();

		if(request.getServiceDetails().getLoadBalancingInstances() > 0){
			vm.setHorizontalScalingEnabled(true);
			vm.setHorizontalScaling(request.getServiceDetails().getLoadBalancingInstances());
		} else {
			vm.setHorizontalScalingEnabled(false);
			vm.setHorizontalScaling(0);
		}	
		
		//TODO improve for multiple network
		Set<IaaSNetwork>  iaasNetworks = new HashSet<IaaSNetwork>();	
		iaasNetworks.add(networkEntity);
		vm.setIaaSNetworks(iaasNetworks);
		
		
		//TODO improve for multiple securitygroup
		Set<IaaSSecurityGroup>  iaasSecurityGroups = new HashSet<IaaSSecurityGroup>();
		IaaSSecurityGroup securityGroup = new IaaSSecurityGroup();
		securityGroup.setName(det.getSecurityGroups());
		iaasSecurityGroups.add(securityGroup);
		vm.setIaaSSecurityGroups(iaasSecurityGroups);
		
		
		vm.setImageName(det.getImageName());
		vm.setKeyPairName(det.getKey());
		vm.setPaaSService(paassvc);
		vm.setVolumeSize(det.getVolume());

		return vm;
	}
	

	@Override
	public VMaaS getEntity(VMRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public VMRepresentation getDSL(VMaaS obj) {
		VMRepresentation dsl = new VMRepresentation();
		
		PaaSServiceRepresentation paasSvc = paasServiceMH.getDSL(obj
				.getPaaSService());
		dsl = (VMRepresentation) paasSvc.populateSubclass(dsl);
		
		dsl.setId(obj.getId());
		dsl.setDiskPartition("TODO");
		dsl.setDiskSize(obj.getVolumeSize());

		dsl.setImageName(obj.getImageName());
		
		List<String> list = new ArrayList<String>();
	    for (IaaSNetwork net : obj.getIaaSNetworks()) {
	     list.add(net.getName());
	    }
		dsl.setNetworks(list);

		dsl.setImageName(dsl.getImageName());
		dsl.setKey(obj.getKeyPairName());
		
		List<String> listGroups = new ArrayList<String>();
	    for (IaaSSecurityGroup sg : obj.getIaaSSecurityGroups()) {
	    	list.add(sg.getName());
	    }
		dsl.setSecurityGroups(listGroups);
		

		return dsl;
	}
}
