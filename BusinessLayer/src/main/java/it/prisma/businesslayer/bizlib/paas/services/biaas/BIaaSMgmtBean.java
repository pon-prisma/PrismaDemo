package it.prisma.businesslayer.bizlib.paas.services.biaas;

import it.prisma.businesslayer.bizlib.paas.services.PaaSServiceHelper;
import it.prisma.businesslayer.bizws.monitoring.MonitoringWSImpl;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.dao.paas.services.biaas.BIaaSDAO;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.biaas.BIaaS;

import java.util.Collection;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BIaaSMgmtBean {

	protected static Logger LOG = LogManager.getLogger(BIaaSMgmtBean.class);

	private static final String TAG = "[BIaaSBean] ";

	@Inject
	private BIaaSDAO biaaSDAO;

	@Inject
	protected PaaSServiceEventDAO paaSServiceEventDAO;

	public BIaaS getBIaaSById(Long Id) {
		return biaaSDAO.findById(Id);
	}

	public Collection<BIaaS> getBIaaSByUserAndWorkgroup(Long workgroupId,
			Long userAccountId, boolean considerDeletedToo) {
		return biaaSDAO.findByUserAndWorkgroup(userAccountId, workgroupId, considerDeletedToo);
	}

	public Collection<PaaSServiceEvent> getBIaaSEvents(Long id, Long from,
			Long to, Long size) {
		return paaSServiceEventDAO.findByPaaSServiceIdWithFilters(id, from, to,
				size);
	}


	@Inject
	PaaSServiceHelper paasSvcHelper;

	// public void undeployBIaaS(long biaasID, String identityURL, String
	// tenant,
	// String username, String pwd) throws Exception {
	//
	// try {
	// // TODO Input from WS
	// // Long workgroupId=1L;
	//
	// LOG.debug(TAG + " BIaaS Undeploy request.");
	//
	// // UPDATE DB
	// BIaaS biaas = biaaSDAO.findById(biaasID);
	// if (biaas == null)
	// throw new IllegalArgumentException("BIaaS does not exists.");
	//
	// PaaSService paasSvc = biaas.getPaaSService();
	// if(PaaSService.Status.DEPLOY_IN_PROGRESS.toString().equals(paasSvc.getStatus()))
	// throw new
	// IllegalServiceStatusException("Cannot undeploy a service still in deployment status.");
	//
	// if(PaaSService.Status.UNDEPLOY_IN_PROGRESS.toString().equals(paasSvc.getStatus()))
	// throw new
	// IllegalServiceStatusException("Cannot undeploy a service already in undeployment status.");
	//
	// if(PaaSService.Status.DELETED.toString().equals(paasSvc.getStatus()))
	// throw new IllegalServiceStatusException("Service already undeployed.");
	//
	// //
	// if(PaaSService.Operation.UNDEPLOY.toString().equals(paasSvc.getOperation())&&PaaSService.Status.ERROR.toString().equals(paasSvc.getStatus()))
	// // throw new
	// IllegalStateException("Cannot undeploy a service still in deployment status.");
	// //
	// paasSvc.setStatus(PaaSService.Status.UNDEPLOY_IN_PROGRESS
	// .toString());
	//
	// paaSServiceDAO.update(paasSvc);
	// // biaaSDAO.create(biaas);
	//
	// // Force the transaction commit before going async.
	// // The PaaSService record MUST be persisted at this time.
	// ut.commit();
	//
	// biaasAsyncMgmt.unprovisioningBIaaSAsync(biaas.getId(), identityURL,
	// tenant, username, pwd);
	//
	// } catch (Exception ex) {
	// throw ex;
	// }
	// }
}