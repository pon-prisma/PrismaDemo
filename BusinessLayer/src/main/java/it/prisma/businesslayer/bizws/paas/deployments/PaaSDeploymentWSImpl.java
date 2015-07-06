package it.prisma.businesslayer.bizws.paas.deployments;

import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.paas.deployments.PaaSDeploymentManagement;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.deployments.InetAddressRepresentation;
import it.prisma.domain.dsl.deployments.InetAddressRepresentation.InetAddressType;
import it.prisma.domain.dsl.deployments.VirtualMachineRepresentation;
import it.prisma.domain.dsl.paas.deployments.PaaSDeploymentServiceInstanceRepresentation;
import it.prisma.domain.dsl.paas.deployments.PaaSDeploymentServiceRepresentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class PaaSDeploymentWSImpl extends BaseWS implements PaaSDeploymentWS {

	// private MappingHelper<VirtualMachine, VirtualMachineRepresentation> vmMH;

	@Inject
	private PaaSServiceDAO paasServiceDAO;

	@Inject
	private PaaSDeploymentManagement paasDeploymentBean;
	
	@Override
	public Collection<VirtualMachineRepresentation> getVirtualMachinesByPaaSService(
			Long id) {
		
		//TODO Spostare in un Bean & spostare nei mapping helper
		try {
			PaaSService svc = paasServiceDAO.findById(id);
			if (svc == null)
				throw new ResourceNotFoundException(PaaSService.class, "id="+id);

			Collection<VirtualMachineRepresentation> vms = new ArrayList<VirtualMachineRepresentation>();
			Map<String, List<PaaSDeploymentServiceInstance>> hosts = new HashMap<String, List<PaaSDeploymentServiceInstance>>();

			PaaSDeployment paasDeployment = svc.getPaaSDeployment();
			if (paasDeployment == null)
				return vms;

			hosts=paasDeploymentBean.groupByHost(paasDeployment);
			
//			// Group by hostId
//			for (PaaSDeploymentService depSvc : svc.getPaaSDeployment()
//					.getPaaSDeploymentServices()) {
//				for (PaaSDeploymentServiceInstance depInst : depSvc
//						.getPaaSDeploymentServiceInstances()) {
//					List<PaaSDeploymentServiceInstance> instances = hosts
//							.get(depInst.getHostId());
//
//					if (instances == null) {
//						instances = new ArrayList<PaaSDeploymentServiceInstance>();
//						hosts.put(depInst.getHostId(), instances);
//					}
//
//					instances.add(depInst);
//				}
//			}

			// Populate VMs
			long vmId = 0;
			for (Map.Entry<String, List<PaaSDeploymentServiceInstance>> host : hosts
					.entrySet()) {
				VirtualMachineRepresentation vm = new VirtualMachineRepresentation();

				List<PaaSDeploymentServiceInstance> instances = host.getValue();
				PaaSDeploymentServiceInstance inst = instances.get(0);

				// InetAddresses
				List<InetAddressRepresentation> addresses = new ArrayList<InetAddressRepresentation>();

				InetAddressRepresentation ip = new InetAddressRepresentation();
				ip.setIp(inst.getPrivateIP());
				ip.setType(InetAddressType.PRIVATE);
				addresses.add(ip);

				ip = new InetAddressRepresentation();
				ip.setIp(inst.getPublicIP());
				ip.setType(InetAddressType.PUBLIC);
				addresses.add(ip);

				vm.setId(vmId++);
				vm.setName(inst.getHostId());
				vm.setInetAddresses(addresses);

				// Add ServiceInstances to VM
				List<PaaSDeploymentServiceInstanceRepresentation> svcInstances = new ArrayList<PaaSDeploymentServiceInstanceRepresentation>();
				for (PaaSDeploymentServiceInstance svcInst : host.getValue()) {
					PaaSDeploymentServiceInstanceRepresentation svcInstance = new PaaSDeploymentServiceInstanceRepresentation();
					svcInstance.setId(svcInst.getId());
					svcInstance.setStatus(svcInst.getStatus());

					// Add Service type to instance
					PaaSDeploymentServiceRepresentation serviceDSL = new PaaSDeploymentServiceRepresentation();
					PaaSDeploymentService service = svcInst
							.getPaaSDeploymentService();
					serviceDSL.setId(service.getId());
					serviceDSL.setCurrentInstances(service
							.getCurrentInstances());
					serviceDSL.setPlannedInstances(service
							.getPlannedInstances());
					serviceDSL.setName(service.getName());
					serviceDSL.setType(service.getType());

					svcInstance.setService(serviceDSL);
					svcInstances.add(svcInstance);
				}

				vm.setServices(svcInstances);
				vms.add(vm);
			}

			return vms;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
}
