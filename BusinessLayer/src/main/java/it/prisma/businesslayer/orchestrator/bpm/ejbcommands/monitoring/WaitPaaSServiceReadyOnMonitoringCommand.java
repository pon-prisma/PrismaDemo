package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.monitoring;

import it.prisma.businesslayer.bizlib.monitoring.MonitoringServiceBean;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.DeployerDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.utils.misc.polling.AbstractPollingBehaviour;
import it.prisma.utils.misc.polling.ExternallyControlledPoller;
import it.prisma.utils.misc.polling.ExternallyControlledPoller.PollingStatus;
import it.prisma.utils.misc.polling.PollingBehaviour;
import it.prisma.utils.misc.polling.PollingException;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to wait for a PaaSService to be ready for the
 * monitoring platform.<br/>
 * The command is expecting a {@link DeploymentMessage} containing the data of
 * the ongoing deployment.<br/>
 * The command will start a polling mechanism to automatically retry in case of
 * transient communication failures and to fail after a predefined timeout. <br/>
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} deploymentMessage
 * @param {@link ExternallyControlledPoller} pollingStatus - a support variable
 *        to store polling status (might be empty and in that case will be
 *        initialized properly to start the polling procedure)
 * @return <ul>
 *         <li>bool <code>isReady</code> to indicate whether the PaaSService is
 *         ready to use</li>
 *         <li>{@link ExternallyControlledPoller} <code>pollingStatus</code> a
 *         support variable to store polling status, to be passed to the next
 *         execution of the command</li>
 *         </ul>
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class WaitPaaSServiceReadyOnMonitoringCommand extends BaseCommand {

    private static final Logger LOG = LogManager.getLogger(WaitPaaSServiceReadyOnMonitoringCommand.class);

    @Inject
    private MonitoringServiceBean monitoringBean;
    
    @Inject
    private EnvironmentConfig envConfig;

    @Inject
    private PaaSServiceDAO paasServiceDAO;

    @Inject
    PaaSServiceEventDAO paaSSvcEvtDAO;

    public WaitPaaSServiceReadyOnMonitoringCommand() throws Exception {
	super();
    }

    @Override
    public ExecutionResults customExecute(CommandContext ctx) throws Exception {

	// Obtaining parameters
	DeploymentMessage depMsg = (DeploymentMessage) workItem.getParameter("deploymentMessage");
	Preconditions.checkNotNull(depMsg);

	ExternallyControlledPoller<Map<String, Object>, Boolean> pollingStatus = null;

	try {
	    pollingStatus = (ExternallyControlledPoller<Map<String, Object>, Boolean>) workItem
		    .getParameter("pollingStatus");
	} catch (Exception e) {
	}

	ExecutionResults exResults = new ExecutionResults();

	try {

	    PaaSService paasSvc = (PaaSService) depMsg.getPaaSService().getPaaSService();

	    Long workgroupId = depMsg.getWorkgroup().getId();
	    // Create poller if not exists
	    if (pollingStatus == null) {
		pollingStatus = getPoller(depMsg.getDeploymentData());
	    }
	    exResults.setData("pollingStatus", pollingStatus);

	    //paasServiceDAO.update(paasSvc);
	    // Do polling
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("PaaSService", paasSvc);
	    params.put("MonitoringBean", monitoringBean);
	    params.put("AdapterType", envConfig.getMonitoringAdapterType());
	    params.put("WorkgroupId", workgroupId);

	    params.put("MonitoringEndpoint", depMsg.getInfrastructureData().getMonitoringEndpoint());
	    try {
		pollingStatus.doPollEvent(params);

		// Poll Ended successfully
		if (pollingStatus.getPollStatus() == PollingStatus.ENDED
			|| envConfig.getAppProperty(EnvironmentConfig.APP_TEST_SKIP_POST_DEPLOY_MONITORING_CHECK)
				.equals("true")) {

		    if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_SKIP_POST_DEPLOY_MONITORING_CHECK).equals(
			    "true")) {
			LOG.warn("Skipping Post-deploy monitoring check");
			try {
			    PaaSServiceEvent paaSSvcEvt = new PaaSServiceEvent(paasSvc, EventType.WARNING.name(),
				    "Skipping Post-deploy monitoring check !");
			    paaSSvcEvtDAO.create(paaSSvcEvt);
			} catch (Exception e) {
			}
		    }

		    // TODO remove after merge with StartAndStop
		    //paasSvc = paasServiceDAO.findById(paasSvc.getId());
			    
		    for(PaaSDeploymentService paasDeploymentService :  paasSvc.getPaaSDeployment().getPaaSDeploymentServices()){
			paasDeploymentService.setStatus(PaaSService.Status.RUNNING.toString());
			for(PaaSDeploymentServiceInstance paaSDeploymentServiceInstance : paasDeploymentService.getPaaSDeploymentServiceInstances()){
			    paaSDeploymentServiceInstance.setStatus(PaaSService.Status.RUNNING.toString());
			}
		    }
		    
		    paasSvc.getPaaSDeployment().setStatus(PaaSService.Status.RUNNING.toString());
		    
		    paasSvc.setStatus(PaaSService.Status.RUNNING.toString());


		    exResults.setData("isReady", true);
		    
		    // TODO remove after merge with StartAndStop
		    depMsg.getPaaSService().setPaaSService(paasSvc);
		    
		    
		    resultOccurred(depMsg, exResults);
		} else {
		    // Poll still in progress
		    exResults.setData("isReady", false);
		    resultOccurred(depMsg, exResults);
		}
	    } catch (PollingException pe) {
		throw new Exception("Monitoring polling failed, error: " + pe.getMessage(), pe);
	    } catch (Exception e) {
		throw new Exception("Monitoring Generic Exception, error: " + e.getMessage(), e);
	    }
	} catch (Exception e) {
	    handleSystemException(e, OrchestratorErrorCode.ORC_WF_DEPLOY_MONITORING_ERROR, exResults);
	}

	exResults.setData("deploymentMessage", depMsg);

	return exResults;
    }

    private static ExternallyControlledPoller<Map<String, Object>, Boolean> getPoller(DeployerDeploymentData depData) {

	PollingBehaviour<Map<String, Object>, Boolean> pollBehavior = new AbstractPollingBehaviour<Map<String, Object>, Boolean>(
		depData.getDeploymentFailTimeout()) {

	    private static final long serialVersionUID = -5994059867039967783L;

	    @Override
	    public Boolean doPolling(Map<String, Object> params) throws PollingException {
		try {
		    PaaSService paasSvc = (PaaSService) params.get("PaaSService");
		    MonitoringServiceBean monitoringBean = (MonitoringServiceBean) params.get("MonitoringBean");		    
		    String workgroupId = params.get("WorkgroupId").toString();
		    String adapterType = params.get("AdapterType").toString();
		    // TODO hard-coded zabbix
		    return monitoringBean.isServiceRunning(adapterType, workgroupId, paasSvc.getId().toString());

		} catch (Exception e) {
		    throw new PollingException("Monitoring Polling for ServiceRunning - error occured: " + e.getMessage(), e);
		}
	    }

	    @Override
	    public boolean pollExit(Boolean pollResult) {
		return pollResult;
	    }

	};

	return new ExternallyControlledPoller<Map<String, Object>, Boolean>(pollBehavior, 10);
    }
}
