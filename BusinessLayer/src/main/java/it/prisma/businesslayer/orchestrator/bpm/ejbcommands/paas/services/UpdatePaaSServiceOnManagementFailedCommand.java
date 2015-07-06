package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.GenericPaaSManagementMessage;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to retrieve a deployment information for a
 * PaaSService deployed to Cloudify. It will also populate the
 * {@link PaaSDeployment} structure related to the PaaSService accordingly.<br/>
 * The command is expecting a {@link DeploymentMessage} with
 * {@link CloudifyDeploymentData} containing the data of the ongoing deployment
 * (ie. the application name and the -optional- deployment ID). <br/>
 * <br/>
 * 
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} deploymentMessage
 * @return <ul>
 *         <li>{@link PaasDeployment} <code>paasDeployment</code> the service
 *         deployment data</li>
 *         <li>{@link DeploymentMessage} <code>deploymentMessage</code> with the
 *         newly populated {@link PaaSDeployment} and
 *         {@link AbstractPaaSService} fields (<b>NOT YET PERSISTED ON DB !</b>)
 *         </li>
 *         </ul>
 * 
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class UpdatePaaSServiceOnManagementFailedCommand extends BaseCommand {

	@Inject
	private PaaSServiceDAO paaSSvcDAO;

	public UpdatePaaSServiceOnManagementFailedCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		//TODO: Improve !!
		// Obtaining parameters
		GenericPaaSManagementMessage paaSManagementMsg = (GenericPaaSManagementMessage) workItem
				.getParameter("genericPaaSManagementMessage");
		Preconditions.checkNotNull(paaSManagementMsg);
		
		
		it.prisma.domain.dsl.prisma.prismaprotocol.Error error = (it.prisma.domain.dsl.prisma.prismaprotocol.Error) workItem
				.getParameter("Error");

		ExecutionResults exResults = new ExecutionResults();

		try {
			PaaSService paasSvc = paaSManagementMsg.getPaaSService().getPaaSService();
			paasSvc.setStatus(PaaSService.Status.ERROR.toString());
			if(error!=null)
				paasSvc.setErrorDescription(error.getErrorName());
			else
				paasSvc.setErrorDescription("UNKNOWN");
			
			paaSSvcDAO.update(paasSvc);
			
			exResults.setData("genericPaaSManagementMessage",paaSManagementMsg);
			resultOccurred("OK", exResults);
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
