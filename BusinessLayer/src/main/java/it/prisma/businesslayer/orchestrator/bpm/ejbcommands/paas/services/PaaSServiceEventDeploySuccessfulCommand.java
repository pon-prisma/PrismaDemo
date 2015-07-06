package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

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
@Local(PaaSServiceEventDeploySuccessfulCommand.class)
public class PaaSServiceEventDeploySuccessfulCommand extends BaseCommand {

	@Inject
	private PaaSServiceEventDAO paaSSvcEvtDAO;

	public PaaSServiceEventDeploySuccessfulCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		PaaSService paasSvc = (PaaSService) workItem
				.getParameter("PaaSService");
		String IP = paasSvc.getDomainName();

		ExecutionResults exResults = new ExecutionResults();

		try {
			String evtDetails="Deploy successful. Public IP: "+IP;
			PaaSServiceEvent paaSSvcEvt = new PaaSServiceEvent(paasSvc,
					"INFO", evtDetails);
			paaSSvcEvtDAO.create(paaSSvcEvt);
			resultOccurred("OK", exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}

}
