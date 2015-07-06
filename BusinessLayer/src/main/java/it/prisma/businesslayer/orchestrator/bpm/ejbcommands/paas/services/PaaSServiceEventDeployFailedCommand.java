package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.ejb.Local;
import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

@Local(PaaSServiceEventDeployFailedCommand.class)
public class PaaSServiceEventDeployFailedCommand extends BaseCommand {

	@Inject
	private PaaSServiceDAO paaSSvcDAO;

	public PaaSServiceEventDeployFailedCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		PaaSService paasSvc = (PaaSService) workItem
				.getParameter("PaaSService");
		it.prisma.domain.dsl.prisma.prismaprotocol.Error error = (it.prisma.domain.dsl.prisma.prismaprotocol.Error) workItem
				.getParameter("Error");

		ExecutionResults exResults = new ExecutionResults();

		try {
			paasSvc.setStatus(PaaSService.Status.ERROR.toString());
			if(error!=null)
				paasSvc.setErrorDescription(error.getErrorName());
			else
				paasSvc.setErrorDescription("UNKNOWN");
			
			paaSSvcDAO.update(paasSvc);
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
