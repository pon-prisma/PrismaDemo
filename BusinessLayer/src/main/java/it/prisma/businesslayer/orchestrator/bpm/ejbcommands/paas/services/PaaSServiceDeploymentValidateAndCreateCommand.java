package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.bizlib.common.exceptions.BadRequestException;
import it.prisma.businesslayer.bizlib.common.exceptions.DuplicatedResourceException;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.orchestration.deployment.PaaSServiceDeploymentValidateData;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.utils.misc.StackTrace;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to create a PaaSService given the deployment
 * request. <br/>
 * <br/>
 * Input parameters:
 * 
 * @param deploymentMessage
 * @return The edited deploymentMessage
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class PaaSServiceDeploymentValidateAndCreateCommand extends BaseCommand {

	@Inject
	private PaaSServiceDeploymentValidateData deploymentValidateBean;

	public PaaSServiceDeploymentValidateAndCreateCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		DeploymentMessage depMsg = (DeploymentMessage) workItem
				.getParameter("deploymentMessage");
		Preconditions.checkNotNull(depMsg);

		ExecutionResults exResults = new ExecutionResults();

		try {
			AbstractPaaSService paasService = deploymentValidateBean
					.generateAndValidatePaaSService(depMsg.getDeployRequest());
			depMsg.setPaaSService(paasService);

			// TODO: Set this status elsewhere or remove
			paasService.getPaaSService().setStatus(
					PaaSService.Status.DEPLOY_IN_PROGRESS.name());

			exResults.setData("deploymentMessage", depMsg);
			exResults.setData("paasService", paasService);
			resultOccurred(depMsg, exResults);
		} catch (DuplicatedResourceException dupEx) {
			errorOccurred(OrchestratorErrorCode.ORC_ITEM_ALREADY_EXISTS,
					StackTrace.getStackTraceToString(dupEx), exResults);
		} catch (ResourceNotFoundException | BadRequestException
				| IllegalArgumentException badEx) {
			errorOccurred(OrchestratorErrorCode.ORC_BAD_REQUEST,
					StackTrace.getStackTraceToString(badEx), exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}
}
