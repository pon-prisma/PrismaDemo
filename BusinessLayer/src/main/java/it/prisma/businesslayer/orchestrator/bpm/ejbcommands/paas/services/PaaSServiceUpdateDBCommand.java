package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.bizlib.orchestration.deployment.PaaSServiceUpdateDB;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.GenericPaaSManagementMessage;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to update or create a PaaSService in the DB. <br/>
 * <br/>
 * Input parameters:
 * 
 * @param {@link AbstractPaaSService} paasService
 * @param {@link String} action [CREATE, UPDATE]
 * @return The persisted/update {@link AbstractPaaSService} paasService
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class PaaSServiceUpdateDBCommand extends BaseCommand {

	public enum ActionType {
		CREATE, UPDATE
	}
	
	@Inject
	private PaaSServiceUpdateDB paasServiceUpdateDBBean;

	public PaaSServiceUpdateDBCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		GenericPaaSManagementMessage paaSManagementMsg = (GenericPaaSManagementMessage) workItem
				.getParameter("genericPaaSManagementMessage");
		Preconditions.checkNotNull(paaSManagementMsg);
		ActionType action;
		try {
			action = ActionType.valueOf((String) workItem.getParameter("action"));
		} catch (IllegalArgumentException | NullPointerException e) {
			
			throw new IllegalArgumentException("'action' should be either CREATE or UPDATE.");
		}

		ExecutionResults exResults = new ExecutionResults();

		try {
			AbstractPaaSService paasService=paaSManagementMsg.getPaaSService();
			if(action == ActionType.CREATE)
				paasService=paasServiceUpdateDBBean.createPaaSService(paasService);
			else
				paasService=paasServiceUpdateDBBean.updatePaaSService(paasService);

			paaSManagementMsg.setPaaSService(paasService);
			exResults.setData("genericPaaSManagementMessage",paaSManagementMsg);
			exResults.setData("paasService",paasService);
			resultOccurred(paaSManagementMsg, exResults);
		} catch (Exception sysEx) {
			OrchestratorErrorCode errorCode;
			if (paaSManagementMsg instanceof DeploymentMessage) {
				errorCode = OrchestratorErrorCode.ORC_WF_DEPLOY_GENERIC_ERROR;
			} else if (paaSManagementMsg instanceof UndeploymentMessage) {
				errorCode = OrchestratorErrorCode.ORC_WF_UNDEPLOY_GENERIC_ERROR;
			} else {
				errorCode = OrchestratorErrorCode.ORC_GENERIC_ERROR;
			}
			// System Exception occurred
			handleSystemException(sysEx, errorCode, exResults);
		}

		return exResults;
	}
}
