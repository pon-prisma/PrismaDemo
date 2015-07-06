package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestrator;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.GenericPaaSManagementMessage;
import it.prisma.businesslayer.orchestrator.dsl.InfrastructureData;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

public class PaaSServiceRetriveEndpointsCommand extends BaseCommand {

	@Inject
	private DeploymentOrchestrator deploymentOrchestratorBean;

	public PaaSServiceRetriveEndpointsCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		GenericPaaSManagementMessage genericPaaSManagementMessage = (GenericPaaSManagementMessage) workItem
				.getParameter("genericPaaSManagementMessage");
		Preconditions.checkNotNull(genericPaaSManagementMessage);

		ExecutionResults exResults = new ExecutionResults();
		
		try {
			InfrastructureData infrastructureData = null;
			switch (it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType.valueOf(genericPaaSManagementMessage.getPaaSService().getPaaSService().getType())) {
			case DBaaS:
				infrastructureData = deploymentOrchestratorBean.buildInfrastructureData(PaaSServiceType.DBaaS, genericPaaSManagementMessage.getWorkgroup());
				break;
			case MQaaS:
				infrastructureData = deploymentOrchestratorBean.buildInfrastructureData(PaaSServiceType.MQaaS, genericPaaSManagementMessage.getWorkgroup());
				break;
			case APPaaSEnvironment:
				infrastructureData = deploymentOrchestratorBean.buildInfrastructureData(PaaSServiceType.APPaaSEnvironment, genericPaaSManagementMessage.getWorkgroup());
				break;
			case BIaaS:
				infrastructureData = deploymentOrchestratorBean.buildInfrastructureData(PaaSServiceType.BIaaS, genericPaaSManagementMessage.getWorkgroup());
				break;
			case VMaaS:
				infrastructureData = deploymentOrchestratorBean.buildInfrastructureData(PaaSServiceType.VMaaS, genericPaaSManagementMessage.getWorkgroup());
				break;
			default:
				throw new Exception("PaaSService type not supported: " + genericPaaSManagementMessage.getPaaSService().getPaaSService().getType() + ".");
			}
			genericPaaSManagementMessage.setInfrastructureData(infrastructureData);
			exResults.setData("genericPaaSManagementMessage", genericPaaSManagementMessage);
			resultOccurred(genericPaaSManagementMessage, exResults);
		} catch	(Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_UNDEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}
}
