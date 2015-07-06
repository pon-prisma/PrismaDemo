package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.mail;

import it.prisma.businesslayer.bizlib.mail.MailBean;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.GenericPaaSManagementMessage;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.Error;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to send an email related to a PaaSService
 * deployment process. The email can be either a success email or an failure
 * email. <br/>
 * <br/>
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} <code>deploymentMessage</code>
 * @param {@link String} <code>type</code> the type of the email (see
 *        {@link MailType})
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class SendPaaSServiceManagementEmailCommand extends BaseCommand {

	public enum MailType {
		SUCCESS, FAILURE
	}
	
	public enum ActionType {
		DEPLOY, UNDEPLOY
	}

	@Inject
	protected MailBean mailBean;

	public SendPaaSServiceManagementEmailCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		GenericPaaSManagementMessage paaSManagementMsg = (GenericPaaSManagementMessage) workItem
				.getParameter("genericPaaSManagementMessage");
		Preconditions.checkNotNull(paaSManagementMsg);

		MailType mailType;
		try {
			mailType = MailType.valueOf((String) workItem.getParameter("mailType"));
		} catch (IllegalArgumentException | NullPointerException e) {
			
			throw new IllegalArgumentException("'mailType' should be either SUCCESS or FAILURE.");
		}
		
		ActionType actionType;
		try {
			actionType = ActionType.valueOf((String) workItem.getParameter("actionType"));
		} catch (IllegalArgumentException | NullPointerException e) {
			
			throw new IllegalArgumentException("'actionType' should be either DEPLOY or UNDEPLOY.");
		}

		ExecutionResults exResults = new ExecutionResults();

		try {
			//PaaSService paasSvc = depMsg.getPaaSService().getPaaSService();
			AbstractPaaSService paasService = paaSManagementMsg.getPaaSService();
			switch (actionType) {
			case DEPLOY:
				switch (mailType) {
				case FAILURE:
					// TODO: Retrieve error
					Error error = (Error) workItem.getParameter("Error");
					String errorDetails = "";
					if (error != null)
						errorDetails = error.getErrorMsg() + "(ERROR_CODE: "
								+ error.getErrorName() + ")";
					else
						errorDetails = "Unknown error occured (ERROR_CODE: ERR_UNK)";
					mailBean.sendPaaSDeployErrorEmail(paasService, errorDetails);
					break;

				case SUCCESS:
					mailBean.sendPaaSDeploySuccesfullEmail(paasService);
					break;
				}
				break;
			case UNDEPLOY:
				switch (mailType) {
				case FAILURE:
					// TODO: Retrieve error
					Error error = (Error) workItem.getParameter("Error");
					String errorDetails = "";
					if (error != null)
						errorDetails = error.getErrorMsg() + "(ERROR_CODE: "
								+ error.getErrorName() + ")";
					else
						errorDetails = "Unknown error occured (ERROR_CODE: ERR_UNK)";
					mailBean.sendPaaSUndeployErrorEmail(paasService, errorDetails);
					break;

				case SUCCESS:
					mailBean.sendPaaSUndeploySuccesfullEmail(paasService);
					break;
				}
				break;
			}

			exResults.setData("genericPaaSManagementMessage", paaSManagementMsg);
			exResults.setData("paasService", paasService);
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
