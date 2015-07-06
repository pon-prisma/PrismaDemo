package it.prisma.businesslayer.bizlib.paas.services.dbaas;

import it.prisma.businesslayer.bizlib.paas.services.PaaSServiceHelper;
import it.prisma.businesslayer.bizws.monitoring.MonitoringWSImpl;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.dao.paas.services.dbaas.DBaaSDAO;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.dbaas.DBaaS;

import java.util.Collection;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBaaSMgmtBean {

	protected static Logger LOG = LogManager.getLogger(DBaaSMgmtBean.class);

	private static final String TAG = "[DBaaSBean] ";

	@Inject
	private DBaaSAsyncMgmtBean dbaasAsyncMgmt;

	@Inject
	private PaaSServiceDAO paaSServiceDAO;

	@Inject
	private DBaaSDAO dbaaSDAO;

	@Inject
	protected PaaSServiceEventDAO paaSServiceEventDAO;

	public DBaaS getDBaaSById(Long Id) {
		return dbaaSDAO.findById(Id);
	}

	public Collection<DBaaS> getDBaaSByUserAndWorkgroup(Long workgroupId,
			Long userAccountId, boolean considerDeletedToo) {
		return dbaaSDAO.findByUserAndWorkgroup(userAccountId, workgroupId, considerDeletedToo);
	}

	public Collection<PaaSServiceEvent> getDBaaSEvents(Long id, Long from,
			Long to, Long size) {
		return paaSServiceEventDAO.findByPaaSServiceIdWithFilters(id, from, to,
				size);
	}

	@Resource
	UserTransaction ut;


	@Inject
	PaaSServiceHelper paasSvcHelper;

//	public DBaaS provisioningDBaaS(DBaaSDeployRequest request,
//			UserAccount userAccount, Workgroup workgroup, String identityURL,
//			String tenant, String username, String pwd) throws Exception {
//
//		try {
//			// TODO Input from WS
//			// Long workgroupId=1L;
//
//			LOG.debug(TAG + " DBaaS Deploy request.");
//
//			// UPDATE DB
//			// PaaSService paasSvc = new PaaSService(userAccount, workgroup,
//			// PaaSService.PaaSServiceType.DBaaS.toString(), stackName,
//			// stackName, null, request.getEnvironment().getFlavor(),
//			// request.getEnvironment().getQos(), true,
//			// request.getEnvironment().getZone());
//			DBaaS dbaas = DBaaSMappingHelper.generateFromDeployRequest(request,
//					userAccount, workgroup);
//			PaaSService paasSvc = dbaas.getPaaSService();
//			paasSvc.setStatus(PaaSService.Status.DEPLOY_IN_PROGRESS.toString());
//
//			// Check Platform status through monitoring
////			try {
////				// paasSvcHelper.logPaaSSvcEvent(paasSvc,
////				// DeployingPrismaEvents.DEPLOY_CHECKING_PLATFORM_STATUS);
////
////				OpenstackIaaSHealth result = monInfoBean.getIaaSScriptOS();
////				if (result.getCompute().contains("not ready")
////						|| result.getNetwork().contains("not ready")
////						| result.getStorage().contains("not ready"))
////					throw new PlatformStatusException(
////							"Platform is not ready for the requested operation");
////			} catch (Exception e) {
////				LOG.warn("Platform is not ready for the requested operation (Monitoring status not ready)");
////				// paasSvcHelper.logPaaSSvcEvent(paasSvc,
////				// DeployingPrismaEvents.DEPLOY_CHECKING_PLATFORM_STATUS_ERROR);
////				//
////				throw e;
////			}
//
//			try {
//
//				paaSServiceDAO.create(paasSvc);
//			} catch (Exception e) {
//				// TODO: Check duplicated exception
//				if(e.getMessage().contains("Duplicate"))
//					throw new DuplicatedResourceException(DBaaS.class, "name","workgroup","type");
//				
//				throw e;				
//			}
//			dbaaSDAO.create(dbaas);
//
//			// Force the transaction commit before going async.
//			// The PaaSService record MUST be persisted at this time.
//			ut.commit();
//
//			dbaasAsyncMgmt.provisioningDBaaSAsync(request, dbaas.getId(),
//					identityURL, tenant, username, pwd);
//
//			return dbaas;
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}

//	public void undeployDBaaS(long dbaasID, String identityURL, String tenant,
//			String username, String pwd) throws Exception {
//
//		try {
//			// TODO Input from WS
//			// Long workgroupId=1L;
//
//			LOG.debug(TAG + " DBaaS Undeploy request.");
//
//			// UPDATE DB
//			DBaaS dbaas = dbaaSDAO.findById(dbaasID);
//			if (dbaas == null)
//				throw new IllegalArgumentException("DBaaS does not exists.");
//			
//			PaaSService paasSvc = dbaas.getPaaSService();
//			if(PaaSService.Status.DEPLOY_IN_PROGRESS.toString().equals(paasSvc.getStatus()))
//				throw new IllegalServiceStatusException("Cannot undeploy a service still in deployment status.");
//			
//			if(PaaSService.Status.UNDEPLOY_IN_PROGRESS.toString().equals(paasSvc.getStatus()))
//				throw new IllegalServiceStatusException("Cannot undeploy a service already in undeployment status.");
//			
//			if(PaaSService.Status.DELETED.toString().equals(paasSvc.getStatus()))
//				throw new IllegalServiceStatusException("Service already undeployed.");
//			
////			if(PaaSService.Operation.UNDEPLOY.toString().equals(paasSvc.getOperation())&&PaaSService.Status.ERROR.toString().equals(paasSvc.getStatus()))
////				throw new IllegalStateException("Cannot undeploy a service still in deployment status.");
////			
//			paasSvc.setStatus(PaaSService.Status.UNDEPLOY_IN_PROGRESS
//					.toString());
//
//			paaSServiceDAO.update(paasSvc);
//			// dbaaSDAO.create(dbaas);
//
//			// Force the transaction commit before going async.
//			// The PaaSService record MUST be persisted at this time.
//			ut.commit();
//
//			dbaasAsyncMgmt.unprovisioningDBaaSAsync(dbaas.getId(), identityURL,
//					tenant, username, pwd);
//
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
}