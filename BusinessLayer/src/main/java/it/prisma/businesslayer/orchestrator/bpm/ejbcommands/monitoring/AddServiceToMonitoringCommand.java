package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.monitoring;

import it.prisma.businesslayer.bizlib.monitoring.MonitoringServiceBean;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.utils.RetryCommand;
import it.prisma.dal.dao.monitoring.AtomicServiceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.entities.monitoring.AtomicService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;
import it.prisma.domain.dsl.exceptions.monitoring.MonitoringException;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.CreatedHostInServer;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.Local;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * This class implements a jBPM Executor Command which adds a DNS record.
 * 
 * @author l.biava
 * 
 */
@Local(AddServiceToMonitoringCommand.class)
public class AddServiceToMonitoringCommand extends BaseCommand {

	private static final Logger LOG = LogManager
			.getLogger(AddServiceToMonitoringCommand.class);

	@Inject
	private MonitoringServiceBean monitoringBean;

	@Inject
	private EnvironmentConfig envConfig;
	
	@Inject
	private AtomicServiceDAO atomicServiceDAO;

	@Inject
	private PaaSServiceEventDAO paaSSvcEvtDAO;

	public AddServiceToMonitoringCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		DeploymentMessage depMsg = (DeploymentMessage) workItem
				.getParameter("deploymentMessage");
		Preconditions.checkNotNull(depMsg);

		ExecutionResults exResults = new ExecutionResults();
		try {
			PaaSService paasSvc = (PaaSService) depMsg.getPaaSService()
					.getPaaSService();
			String workgroupId = depMsg.getWorkgroup().getId().toString();

			Map<String, List<String>> hostsServices = new HashMap<String, List<String>>();
			Map<String, PaaSDeploymentServiceInstance> hosts = new HashMap<String, PaaSDeploymentServiceInstance>();

			String paaStype = paasSvc.getType();
			
			
			if (paasSvc.getType().equals(
					PaaSService.PaaSServiceType.APPaaSEnvironment.toString()))
				paaStype = "AppaaS";
			else
				paaStype = paasSvc.getType().toLowerCase();

			
			for (PaaSDeploymentService service : paasSvc.getPaaSDeployment()
					.getPaaSDeploymentServices()) {
				for (PaaSDeploymentServiceInstance instance : service
						.getPaaSDeploymentServiceInstances()) {
					if (hostsServices.containsKey(instance.getHostId())) {
					    
						AtomicService atomicService = atomicServiceDAO.findByPaaSDeploymentServiceType(service.getType());
						if(atomicService == null)
						    throw new MonitoringException("Cannot find AtomicService with name: " + service.getType());
						
						hostsServices.get(instance.getHostId()).add(atomicService.getName());
					} else {
						hosts.put(instance.getHostId(), instance);

						AtomicService atomicService =  atomicServiceDAO.findByPaaSDeploymentServiceType(service.getType());
						
					
						if(atomicService == null)
						    throw new MonitoringException("Cannot find AtomicService with name: " + service.getType());
						
						hostsServices.put(instance.getHostId(),
								new ArrayList<String>(Arrays.asList(atomicService.getName())));
					}
				}

			}
			
			
			// Test skip
			if (envConfig.getAppProperty(
					EnvironmentConfig.APP_TEST_SKIP_POST_DEPLOY_MONITORING_ADD)
					.equals("true")) {
				LOG.warn("Skipping Post-deploy monitoring add");
				try {
					PaaSServiceEvent paaSSvcEvt = new PaaSServiceEvent(paasSvc,
							EventType.WARNING.name(),
							"Skipping Post-deploy monitoring add !");
					paaSSvcEvtDAO.create(paaSSvcEvt);
				} catch (Exception e) {
				}
			} else {
				// variabile usata per debug. Serve per sapere quanti host sono
				// stati aggiunti al monitoring nel caso in cui il servizio sia
				// multihost
				int counter = 1;
				for (Entry<String, PaaSDeploymentServiceInstance> item : hosts
						.entrySet()) {
					PaaSDeploymentServiceInstance host = item.getValue();
					String hostPublicIP = host.getPublicIP();
					if (hostPublicIP.equals(""))
						hostPublicIP = host.getPrivateIP();
					// String hostPrivateIP = host.getPrivateIP();
					// String hostInstanceId = host.getInstanceId().toString();

					// TODO adapterType is hard-coded
					new AddServiceToMonitoringRetryCommand().init(
							monitoringBean, envConfig.getMonitoringAdapterType(), host.getHostId(), hostPublicIP,
							workgroupId, paaStype, paasSvc.getId().toString(),
							hostsServices.get(item.getKey())).run();

					// Ad ogni ciclo aggiungo un host al monitoring
					LOG.debug("Host added in Metrics and Watcher: " + host.getHostId()
							+ ". " + counter + "/" + hosts.size());
					counter++;
				}
			}
			exResults.setData("deploymentMessage", depMsg);
			resultOccurred(depMsg, exResults);
		} catch (Exception e) {
			handleSystemException(e,
					OrchestratorErrorCode.ORC_WF_DEPLOY_MONITORING_ERROR,
					exResults);
		}
		return exResults;
	}

	public static class AddServiceToMonitoringRetryCommand extends
			RetryCommand<CreatedHostInServer> {

		String adapterType, UUID, IP, group, serviceCategory, serviceID;
		List<String> atomicServices;
		MonitoringServiceBean instance;

		public CreatedHostInServer command(Object... args) throws Exception {
			return instance.addServiceToMonitoring(adapterType, UUID, IP,
					group, serviceCategory, serviceID, atomicServices);
		}

		public RetryCommand<CreatedHostInServer> me(
				MonitoringServiceBean instance) {
			this.instance = instance;
			return this;
		}

		public RetryCommand<CreatedHostInServer> init(
				MonitoringServiceBean instance, String adapterType,
				String UUID, String IP, String group, String serviceCategory,
				String serviceID, List<String> atomicServices) {
			me(instance);
			this.adapterType = adapterType;
			this.UUID = UUID;
			this.IP = IP;
			this.group = group;
			this.serviceCategory = serviceCategory;
			this.serviceID = serviceID;
			this.atomicServices = atomicServices;
			return this;
		}
	}
}
