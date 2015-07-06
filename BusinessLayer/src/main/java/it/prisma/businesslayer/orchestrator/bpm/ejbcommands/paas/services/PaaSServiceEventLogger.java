package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.bizlib.misc.events.DeployingPrismaEvents;
import it.prisma.businesslayer.bizlib.misc.events.PrismaEvent;
import it.prisma.businesslayer.bizlib.misc.events.PrismaEventType;
import it.prisma.businesslayer.bizlib.orchestration.PaaSServiceManager;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.Error;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * Bean implementing a command to log PaaSServiceEvent. <br/>
 * <br/>
 * Workitem parameters:
 * 
 * @param LRT
 *            the LRT to log events to.
 * @param PaaSService
 * 
 * @param Type
 * 
 * @param Details
 * 
 * @return .
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
@Local(PaaSServiceEventLogger.class)
public class PaaSServiceEventLogger extends BaseCommand {

	@Inject
	private PaaSServiceManager paasServiceManager;

//	@Inject
//	private PaaSServiceEventDAO paasServiceEventDAO;

	public PaaSServiceEventLogger() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// *** Obtaining event parameters ***

		// Extract PaaSService (backward-compatibility for PaaSService &
		// AbstractPaaSService)
		PaaSService paasSvc;
		try {
			paasSvc = (PaaSService) workItem.getParameter("PaaSService");
		} catch (Exception e) {
			paasSvc = ((AbstractPaaSService) workItem
					.getParameter("PaaSService")).getPaaSService();
		}

		// <---
		// Might be a normal Event ?
		String evtType = (String) workItem.getParameter("Type");
		String evtDetails = (String) workItem.getParameter("Details");
		Error evtError = (Error) workItem.getParameter("Error");

		// If an error is available, append to details
		if (evtError != null)
			evtDetails = evtDetails + ". Errore: " + evtError;
		// --->

		// <---
		// Might be a Prisma Event ?
		PrismaEventType prismaEvtType = null;
		try {
			String pETN = (String) workItem.getParameter("PrismaEventType");
			if (pETN != null)
				prismaEvtType = DeployingPrismaEvents.lookupFromName(pETN);
		} catch (Exception e) {
		}
		// --->

		// Build the event upon Prisma Event
		if (prismaEvtType != null) {
			// Get specified Prisma Event arguments
			Object[] args = null;
			try {
				args = (Object[]) workItem.getParameter("EventArgs");
			} catch (Exception ex) {
			}

			// Get Prisma Event specific arguments
			if (args == null)
				args = getArgsForEvent(paasSvc, prismaEvtType, evtError);

			// Build the event
			PrismaEvent prismaEvt = new PrismaEvent(prismaEvtType, args);
			evtType = prismaEvt.getType().toString();
			evtDetails = prismaEvt.getDetails();
		}

		ExecutionResults exResults = new ExecutionResults();

		try {

			// Log newly created service resources details
			if (prismaEvtType == DeployingPrismaEvents.DEPLOY_RESOURCE_PROVISIONING_SUCCESSFUL) {
				paasServiceManager.logPaaSServiceComponentsEvents(paasSvc);
			}
			// Delete non-INFO events for undeployed service
			// else if (prismaEvtType ==
			// DeployingPrismaEvents.UNDEPLOY_ENDED_SUCCESSFULLY) {
			// Collection<PaaSServiceEvent> paaSServiceEvents =
			// paasServiceEventDAO
			// .findByPaaSServiceIdWithFilters(paasSvc.getId(), 0L,
			// Long.MAX_VALUE, Long.MAX_VALUE);
			// for (PaaSServiceEvent paaSServiceEvent : paaSServiceEvents) {
			// if (!paaSServiceEvent.getType().equals(
			// PaaSServiceEvent.EventType.INFO.toString())) {
			// paasServiceEventDAO.delete(paaSServiceEvent.getId());
			// }
			// }
			// }

			// Log event on DB
			PaaSServiceEvent paaSSvcEvt = new PaaSServiceEvent(paasSvc,
					evtType, evtDetails,
					(evtError != null ? evtError.toString() : ""));
			paasServiceManager.logPaaSServiceEvent(paasSvc, paaSSvcEvt);
			resultOccurred("OK", exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}

	private Object[] getArgsForEvent(PaaSService paasSvc, PrismaEventType pet,
			Error evtError) {
		List<Object> args = new ArrayList<Object>();

		if (pet instanceof DeployingPrismaEvents) {
			DeployingPrismaEvents evt = (DeployingPrismaEvents) pet;
			switch (evt) {
			case DEPLOY_POST_DEPLOY_DNS_ENTRY_SUCCESSFUL:
				args.add(paasSvc.getDomainName());
				break;
			case UNDEPLOY_IN_PROGRESS:
				args.add(paasSvc.getUserAccount().getNameIdOnIdentityProvider());
			default:
				if (evtError != null)
					args.add(evtError.getErrorMsg());
				break;
			}
		}

		return args.toArray();
	}

}
