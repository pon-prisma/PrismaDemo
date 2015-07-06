package it.prisma.businesslayer.bizlib.paas.services;

import it.prisma.businesslayer.bizlib.mail.MailBean;
import it.prisma.businesslayer.bizlib.misc.events.PrismaEvent;
import it.prisma.businesslayer.bizlib.misc.events.PrismaEventType;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;
import it.prisma.utils.misc.StackTrace;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaaSServiceHelper {

	protected static Logger LOG = LogManager.getLogger(PaaSServiceHelper.class);

	@Inject
	protected MailBean mailBean;

	@Inject
	protected PaaSServiceDAO paasServiceDAO;

	@Inject
	protected PaaSServiceEventDAO paasServiceEventDAO;

	@Resource
	protected UserTransaction userTransaction;

	/**
	 * Logs a PaaSService event.
	 * 
	 * <br/>
	 * <b>NOTE: This method should use a different transaction, but it appears
	 * not every transaction manager must implement nested transaction.
	 * Therefore, a workaround is applied: committing current transaction,
	 * beginning and committing the required transaction and beginning another
	 * one for the call (BE AWARE IT WILL BE ANOTHER TRANSACTION !).</b>
	 * 
	 * @param paasSvcId
	 * @param type
	 * @param details
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void logPaaSSvcEvent(PaaSService paasSvc, EventType type,
			String details, Exception t) {
		// PaaSService paasSvc = paasServiceDAO.findById(paasSvcId);
		try {
			userTransaction.commit();
		} catch (Exception e) {
		}
		try {
			userTransaction.begin();
		} catch (Exception e) {
			LOG.error("Cannote re-begin session before PaaSServiceEvent.");
		}
		PaaSServiceEvent paasEvt = new PaaSServiceEvent(paasSvc,
				type.toString(), details, (t != null ? StackTrace.getStackTraceToString(t) : ""));

		paasServiceEventDAO.create(paasEvt);
		try {
			userTransaction.commit();
		} catch (Exception e) {
		}
		try {
			userTransaction.begin();
		} catch (Exception e) {
			LOG.error("Cannote re-begin session after PaaSServiceEvent.");
		}
		// paasServiceEventDAO.forceFlush();
	}

	public void logPaaSSvcEvent(PaaSService paasSvc, EventType type,
			String details) {
		logPaaSSvcEvent(paasSvc, type, details, null);
	}

	/* with PrismaEvents */

	public void logPaaSSvcEvent(PaaSService paasSvc, PrismaEvent evt) {
		logPaaSSvcEvent(paasSvc, evt.getType(), evt.getDetails(), null);
	}

	public void logPaaSSvcEvent(PaaSService paasSvc, PrismaEvent evt,
			Exception t) {
		logPaaSSvcEvent(paasSvc, evt.getType(), evt.getDetails(), t);
	}

	public void resultOccurred(PaaSService paasSvc,
			PrismaEventType prismaEvtType, PaaSService.Status serviceStatus,
			String details) {
		// Log status + event in DB
		paasSvc.setStatus(serviceStatus.toString());
		paasServiceDAO.update(paasSvc);

		if (details == null)
			logPaaSSvcEvent(paasSvc, new PrismaEvent(prismaEvtType), null);
		else
			logPaaSSvcEvent(paasSvc, EventType.INFO, details, null);
	}

	// public void resultOccurred(PaaSService paasSvc, String details, String
	// serviceStatus) {
	// // Log status + event in DB
	// paasSvc.setStatus(serviceStatus);
	// paasServiceDAO.update(paasSvc);
	//
	// if (details == null)
	// logPaaSSvcEvent(paasSvc,
	// DeployingPrismaEvents.DEPLOY_ENDED_SUCCESSFULLY);
	// else
	// logPaaSSvcEvent(paasSvc, EventType.INFO, details);
	// }

	public void errorOccurred(PaaSService paasSvc, String errDescr,
			String details, Exception t) {
		// Log status + event in DB
		paasSvc.setErrorDescription(StringUtils.abbreviate(errDescr, 44));
		paasSvc.setStatus(PaaSService.Status.ERROR.toString());

		paasServiceDAO.update(paasSvc);

		// Send Email
		try {
			//TODO: Use WF instead !
			//mailBean.sendPaaSDeployErrorEmail(paasSvc, details);
		} catch (Exception ex) {
			LOG.error("Unable to send error notification e-mail for "
					+ paasSvc.toString());
		}

		logPaaSSvcEvent(paasSvc, EventType.ERROR, details, t);

	}

	public void errorOccurred(PaaSService paasSvc, PrismaEventType evtType,
			String errDescr, String details, Exception t) {
		// Log status + event in DB
		paasSvc.setErrorDescription(StringUtils.abbreviate(errDescr, 44));
		paasSvc.setStatus(PaaSService.Status.ERROR.toString());

		paasServiceDAO.update(paasSvc);

		// Send Email
		try {
			//TODO: Use WF instead !
			//mailBean.sendPaaSDeployErrorEmail(paasSvc, details);
		} catch (Exception ex) {
			LOG.error("Unable to send error notification e-mail for "
					+ paasSvc.toString());
		}

		logPaaSSvcEvent(paasSvc, new PrismaEvent(evtType), t);
	}
}
