package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.monitoring;

import it.prisma.businesslayer.bizlib.monitoring.MonitoringServiceBean;
import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestratorBean;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.businesslayer.utils.RetryCommand;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

public class RemoveServiceFromMonitoringCommand extends BaseCommand {

	private static final Logger LOG = LogManager
			.getLogger(RemoveServiceFromMonitoringCommand.class);

	@Inject
	private MonitoringServiceBean monitoringBean;

	@Inject
	private EnvironmentConfig envConfig;

	@Inject
	private PaaSServiceEventDAO paaSSvcEvtDAO;

	public RemoveServiceFromMonitoringCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		UndeploymentMessage undepMsg = (UndeploymentMessage) workItem
				.getParameter(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG);
		Preconditions.checkNotNull(undepMsg);

		ExecutionResults exResults = new ExecutionResults();

		try {
			PaaSService paasSvc = undepMsg.getPaaSService().getPaaSService();

			// Test skip
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_SKIP_PRE_UNDEPLOY_MONITORING_REMOVE).equals("true")) {
				LOG.warn("Skipping undeploy monitoring remove");
				try {
					PaaSServiceEvent paaSSvcEvt = new PaaSServiceEvent(paasSvc, EventType.WARNING.name(),
							"Skipping undeploy monitoring remove!");
					paaSSvcEvtDAO.create(paaSSvcEvt);
				} catch (Exception e) {
				}
			} else {
				for (PaaSDeploymentService service : paasSvc.getPaaSDeployment().getPaaSDeploymentServices()) {
					for (PaaSDeploymentServiceInstance instance : service.getPaaSDeploymentServiceInstances()) {
						new RemoveServiceToMonitoringRetryCommand().init(
								monitoringBean, envConfig.getMonitoringAdapterType(), instance.getHostId())
								.run();
					}

				}
			}
			exResults.setData(
					DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG,
					undepMsg);
			resultOccurred(undepMsg, exResults);

		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_UNDEPLOY_MONITORING_ERROR,
					exResults);
		}

		return exResults;
	}

	public static class RemoveServiceToMonitoringRetryCommand extends
			RetryCommand<Boolean> {

		String adapterType, hostUUID;
		MonitoringServiceBean instance;

		public Boolean command(Object... args) throws Exception {
		    
		    try{
			instance.removeServiceToMonitoring(adapterType, hostUUID);
			return true;
		    } catch(Exception e){
			return false;
		    }
		}

		public RetryCommand<Boolean> me(
				MonitoringServiceBean instance) {
			this.instance = instance;
			return this;
		}

		public RetryCommand<Boolean> init(
				MonitoringServiceBean instance, String adapterType,
				String hostUUID) {
			me(instance);
			this.adapterType = adapterType;
			this.hostUUID = hostUUID;
			return this;
		}
	}
}
