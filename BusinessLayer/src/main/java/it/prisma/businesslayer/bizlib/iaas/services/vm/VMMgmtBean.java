package it.prisma.businesslayer.bizlib.iaas.services.vm;

import it.prisma.businesslayer.bizlib.common.exceptions.BadRequestException;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.infrastructure.AppRepoLookupInfo;
import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean;
import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean.AppRepoEndpoint;
import it.prisma.businesslayer.bizlib.misc.events.LifecyclePrismaEvents;
import it.prisma.businesslayer.bizlib.paas.services.PaaSServiceHelper;
import it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions.IllegalServiceStatusException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.exceptions.IllegalServiceStateException;
import it.prisma.businesslayer.exceptions.ServiceOperationException;
import it.prisma.dal.dao.iaas.services.IaaSNetworkDAO;
import it.prisma.dal.dao.iaas.services.vm.VMaaSDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.entities.iaas.services.vm.VMaaS;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.VMaaSMonitoringStatus;

import java.util.Collection;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.OS4JException;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.RebootType;
import org.openstack4j.openstack.OSFactory;

public class VMMgmtBean {

	protected static Logger LOG = LogManager.getLogger(VMMgmtBean.class);

	@Inject
	protected EnvironmentConfig envConfig;

	@Inject
	private VMAsyncMgmtBean vmAsyncMgmt;

	@Inject
	private PaaSServiceDAO paaSServiceDAO;
	
	@Inject
	private PaaSServiceEventDAO paasServiceEventDAO;

	@Inject
	protected PaaSServiceHelper paasSvcHelper;

	@Inject
	private VMaaSDAO vmDAO;

	@Inject
	private IaaSNetworkDAO netDAO;

	@Inject
	protected PaaSServiceEventDAO paaSServiceEventDAO;
	
	@Inject
	private InfrastructureManagerBean infrastructureBean;
	

	public VMaaS getVMaaSById(Long Id) {
		return vmDAO.findById(Id);
	}

	public Collection<VMaaS> getVMByUserAndWorkgroup(Long workgroupId, Long userAccountId, boolean considerDeletedToo) {
		return vmDAO.findByUserAndWorkgroup(userAccountId, workgroupId, considerDeletedToo);
	}

	public Collection<PaaSServiceEvent> getVMEvents(Long id, Long from, Long to, Long size) {
		return paaSServiceEventDAO.findByPaaSServiceIdWithFilters(id, from, to,	size);
	}

	@Resource
	UserTransaction ut;

//	public VMaaS provisioningVM(VMDeployRequest request, UserAccount userAccount, Workgroup workgroup, String identityURL,
//			String tenant, String username, String pwd) throws Exception {
//
//		try {
//
//			// TODO cambiare in request.getVmDetails().getNetworks()
//			IaaSNetwork networkEntity = netDAO.findById(Long.valueOf(request.getVmDetails().getNetworks()));
//
//			VMaaS vm = VMMappingHelper.generateFromDeployRequest(request, networkEntity, userAccount, workgroup);
//			PaaSService paasSvc = vm.getPaaSService();
//			paasSvc.setStatus(PaaSService.Status.DEPLOY_IN_PROGRESS.toString());
//
//			paaSServiceDAO.create(paasSvc);
//			vmDAO.create(vm);
//
//			// Force the transaction commit before going async.
//			// The PaaSService record MUST be persisted at this time.
//			ut.commit();
//
//			vmAsyncMgmt.provisioningVMaaSAsync(request, vm.getId(),
//					identityURL, tenant, username, pwd);
//
//			return vm;
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}

	public VMaaS stopVirtualMachine(Long workgroupId, Long resId) throws IllegalServiceStateException, ServiceOperationException, OS4JException {

		PaaSService paasSvc = getPaasService(resId.toString());
		
		// Verifico lo stato del servizio
		if (!PaaSService.Status.RUNNING.toString().equals(paasSvc.getStatus()))
			throw new IllegalServiceStatusException("Service state error. Action required is STOP on a " + paasSvc.getStatus() + " service");

		
		AppRepoEndpoint endpoint = infrastructureBean
			.getPrivateAppRepoEndpoint(new AppRepoLookupInfo(
					workgroupId));
		 
		 
//		String identityURL = envConfig.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL);

		// TODO OS Tenant data lookup
//		String tenant = envConfig.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_NAME);
//		String username = envConfig.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_USERNAME);
//		String pwd = envConfig.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_PASSWORD);
		
		
		OSClient os = OSFactory.builder().endpoint(endpoint.getOSIdentityURL())
				.credentials(endpoint.getUsername(), endpoint.getPassword())
				.tenantName(endpoint.getTenantName()).authenticate();
		
		
//		OSClient os = OSFactory.builder().endpoint(identityURL + "/v2.0/").credentials(username, pwd).tenantName(tenant).authenticate();

		// TODO Improve
		String openstackId = paasSvc.getPaaSDeployment()
				.getPaaSDeploymentServices().iterator().next()
				.getPaaSDeploymentServiceInstances().iterator().next()
				.getHostId();

		// Check if IaaS status is equal to last know
		org.openstack4j.model.compute.Server.Status osStatus = os.compute()
				.servers().get(openstackId).getStatus();
		if (osStatus.equals(org.openstack4j.model.compute.Server.Status.ACTIVE)) {
			ActionResponse actionResponse = os.compute().servers()
					.action(openstackId, Action.STOP);
			if (actionResponse.isSuccess()) {

				paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.INFO,
						LifecyclePrismaEvents.STOP_IN_PROGRESS.toString());

				// TODO: Remove workaround for transaction problem
				paasSvc = this.getVMaaSById(resId).getPaaSService();

				paasSvc.setStatus(PaaSService.Status.STOP_IN_PROGRESS
						.toString());
				paaSServiceDAO.create(paasSvc);

				return this.getVMaaSById(resId);
			} else {
				throw new ServiceOperationException(actionResponse.getFault());
			}
		} else {
			throw new IllegalServiceStateException(
					"Service state error. Action required is STOP but IaaS status is "
							+ osStatus);
		}
	}

	public VMaaS startVirtualMachine(Long workgroupId, Long resId)
			throws IllegalServiceStateException, ServiceOperationException,
			OS4JException {

		PaaSService paasSvc = getPaasService(resId.toString());

		// Verifico lo stato del servizio
		if ((PaaSService.Status.START_IN_PROGRESS.toString().equals(
				paasSvc.getStatus()) || PaaSService.Status.RUNNING.toString()
				.equals(paasSvc.getStatus())))
			throw new IllegalServiceStatusException(
					"Service state error. Action required is START on a "
							+ paasSvc.getStatus() + " service");

		

		AppRepoEndpoint endpoint = infrastructureBean
				.getPrivateAppRepoEndpoint(new AppRepoLookupInfo(
						workgroupId));
			 
		
		
		OSClient os = OSFactory.builder().endpoint(endpoint.getOSIdentityURL())
				.credentials(endpoint.getUsername(), endpoint.getPassword())
				.tenantName(endpoint.getTenantName()).authenticate();
		
		
//		String identityURL = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL);

		// TODO OS Tenant data lookup
//		String tenant = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_NAME);
//		String username = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_USERNAME);
//		String pwd = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_PASSWORD);

//		OSClient os = OSFactory.builder().endpoint(identityURL + "/v2.0/")
//				.credentials(username, pwd).tenantName(tenant).authenticate();

		// TODO Improve
		String openstackId = paasSvc.getPaaSDeployment()
				.getPaaSDeploymentServices().iterator().next()
				.getPaaSDeploymentServiceInstances().iterator().next()
				.getHostId();

		// Check if IaaS status is equal to last know
		org.openstack4j.model.compute.Server.Status osStatus = os.compute()
				.servers().get(openstackId).getStatus();
		if (osStatus .equals(org.openstack4j.model.compute.Server.Status.SHUTOFF)) {
			ActionResponse actionResponse = os.compute().servers()
					.action(openstackId, Action.START);
			if (actionResponse.isSuccess()) {

				paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.INFO,
						LifecyclePrismaEvents.START_IN_PROGRESS.toString());

				// TODO: Remove workaround for transaction problem
				paasSvc = this.getVMaaSById(resId).getPaaSService();

				paasSvc.setStatus(PaaSService.Status.START_IN_PROGRESS
						.toString());
				paaSServiceDAO.create(paasSvc);

				return this.getVMaaSById(resId);
			} else {
				throw new ServiceOperationException(actionResponse.getFault());
			}
		} else {
			throw new IllegalServiceStateException(
					"Service state error. Action required is START but IaaS status is "
							+ osStatus);
		}
	}

	public VMaaS restartVirtualMachine(Long workgroupId, Long resId) {

		PaaSService paasSvc = getPaasService(resId.toString());

		// Verifico lo stato del servizio, solo se il servizio è RUNNING si può effettuare il restart
		if (!PaaSService.Status.RUNNING.toString().equals(paasSvc.getStatus()))
			throw new IllegalServiceStatusException( "Service state error. Action required is RESTART on a " + paasSvc.getStatus() + " service");

		

		AppRepoEndpoint endpoint = infrastructureBean
				.getPrivateAppRepoEndpoint(new AppRepoLookupInfo(
						workgroupId));
			 
		OSClient os = OSFactory.builder().endpoint(endpoint.getOSIdentityURL())
				.credentials(endpoint.getUsername(), endpoint.getPassword())
				.tenantName(endpoint.getTenantName()).authenticate();
		
//		String identityURL = envConfig.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL);

		// TODO OS Tenant data lookup
//		String tenant = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_NAME);
//		String username = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_USERNAME);
//		String pwd = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_PASSWORD);
//
//		OSClient os = OSFactory.builder().endpoint(identityURL + "/v2.0/")
//				.credentials(username, pwd).tenantName(tenant).authenticate();

		// TODO Improve
		String openstackId = paasSvc.getPaaSDeployment()
				.getPaaSDeploymentServices().iterator().next()
				.getPaaSDeploymentServiceInstances().iterator().next()
				.getHostId();

		// Check if IaaS status
		org.openstack4j.model.compute.Server.Status osStatus = os.compute().servers().get(openstackId).getStatus();
		if (osStatus.equals(org.openstack4j.model.compute.Server.Status.ACTIVE)) {
			
			ActionResponse actionResponse = os.compute().servers().reboot(openstackId, RebootType.SOFT);
			if (actionResponse.isSuccess()) {

				
				os.compute().servers().get(openstackId).getStatus();
				paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.INFO,
						LifecyclePrismaEvents.RESTART_IN_PROGRESS.toString());

				// TODO: Remove workaround for transaction problem
				paasSvc = this.getVMaaSById(resId).getPaaSService();

				paasSvc.setStatus(PaaSService.Status.RESTART_IN_PROGRESS
						.toString());
				paaSServiceDAO.create(paasSvc);
			
				return this.getVMaaSById(resId);
			} else {
				throw new ServiceOperationException(actionResponse.getFault());
			}
		} else {
			throw new IllegalServiceStateException("Service state error. Action required is RESTART but IaaS status is " + osStatus);
		}
	}

	/**
	 * 
	 * TODO: verificare i possibili stati che il monitoring può rilevare
	 * 
	 * @param vmaaSMonitoringStatus
	 * @return
	 */
	public boolean updateVirtualMachineStatus(VMaaSMonitoringStatus vmaaSMonitoringStatus) {
		
		PaaSService paaSServiceEntity = getPaasService(vmaaSMonitoringStatus.getId());
		
		PaaSService.Status status = PaaSService.Status.valueOf(paaSServiceEntity.getStatus());
		PaaSServiceEvent paasEvt;
		switch (vmaaSMonitoringStatus.getStatus()) {
		case RUNNING:
			if(!status.equals(PaaSService.Status.START_IN_PROGRESS) && !status.equals(PaaSService.Status.RESTART_IN_PROGRESS))
				throw new IllegalServiceStateException("Service state error. New status is RUNNING, last know status is " + status.toString());
			paasEvt = new PaaSServiceEvent(paaSServiceEntity, EventType.INFO.toString(), LifecyclePrismaEvents.STARTED.toString());
			break;
		case STOPPED:
			if(!status.equals(PaaSService.Status.STOP_IN_PROGRESS))
				throw new IllegalServiceStateException("Service state error. New status is STOPPED, last know status is " + status.toString());
			paasEvt = new PaaSServiceEvent(paaSServiceEntity, EventType.INFO.toString(), LifecyclePrismaEvents.STOPPED.toString());
			break;
		default:
			throw new IllegalServiceStateException("Illegal update state: " + vmaaSMonitoringStatus.getStatus().toString());
		}

		
		paaSServiceEntity.setStatus(vmaaSMonitoringStatus.getStatus().toString());
		paaSServiceDAO.update(paaSServiceEntity);
		paasServiceEventDAO.create(paasEvt);

		return true;
		
	}


	/**
	 * Get a PaaSService for the given paasServiceID
	 * Check if paasServiceID belogs to a VMaaS
	 * 
	 * @param paasServiceID the database unique id
	 * @return
	 */
	private PaaSService getPaasService(String paasServiceID) throws ResourceNotFoundException{
		Long longPaasServiceID = null;
		try{
			longPaasServiceID = Long.valueOf(paasServiceID);
		} catch(NumberFormatException numericException){
			throw new BadRequestException("Invalid service ID: " + paasServiceID);
		}
		// Verifico se l'id corrisponde ad una VMaaS presente a DB
		VMaaS vm = this.getVMaaSById(longPaasServiceID);
		if (vm == null)
			throw new ResourceNotFoundException(VMRepresentation.class, "id="
					+ paasServiceID);
		// Prendo il PaasService corrispondente alla VMaaS. Nel PaasService c'è
		// lo stato del servizio
		PaaSService paasSvc = vm.getPaaSService();
		if (paasSvc == null)
			throw new ResourceNotFoundException(
					"Cannot find PaaSService associated with VMaaS " + paasServiceID);
		
		return paasSvc;
	}
}