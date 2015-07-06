package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.GenericPaaSManagementMessage;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

public class PaaSServiceSetUndeployedStatusCommand extends BaseCommand {

	public PaaSServiceSetUndeployedStatusCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		GenericPaaSManagementMessage paaSManagementMsg = (GenericPaaSManagementMessage) workItem
				.getParameter("genericPaaSManagementMessage");
		Preconditions.checkNotNull(paaSManagementMsg);

		ExecutionResults exResults = new ExecutionResults();

		try {
			AbstractPaaSService abstractPaasService = paaSManagementMsg.getPaaSService();
			abstractPaasService.getPaaSService().setStatus(PaaSService.Status.DELETED.toString());
			abstractPaasService.getPaaSService().setErrorDescription(null);
			PaaSDeployment paaSDeployment = abstractPaasService.getPaaSService().getPaaSDeployment();
			if (paaSDeployment != null) {
				paaSDeployment.setStatus(PaaSService.Status.DELETED.toString());
				for (PaaSDeploymentService paaSDeploymentService : paaSDeployment.getPaaSDeploymentServices()) {
					paaSDeploymentService.setStatus(PaaSService.Status.DELETED.toString());
					for (PaaSDeploymentServiceInstance paaSDeploymentServiceInstance : paaSDeploymentService.getPaaSDeploymentServiceInstances()) {
						paaSDeploymentServiceInstance.setStatus(PaaSDeploymentServiceInstance.StatusType.DELETED.toString());
					}
				}
			}
			exResults.setData("genericPaaSManagementMessage", paaSManagementMsg);
			resultOccurred(true, exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_UNDEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}
}
