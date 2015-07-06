package it.prisma.businesslayer.bizlib.monitoring;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.domain.dsl.exceptions.monitoring.MonitoringException;
import it.prisma.domain.dsl.exceptions.monitoring.NotFoundMonitoringException;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.HostIaaS;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.IaaSHostGroups;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.InfrastructureBareMetalMetric;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.InfrastructureBareMetalStatus;
import it.prisma.domain.dsl.monitoring.businesslayer.iaas.infrastructure.GroupsMachine;
import it.prisma.domain.dsl.monitoring.businesslayer.iaas.infrastructure.InfrastructureMachine;
import it.prisma.domain.dsl.monitoring.businesslayer.iaas.infrastructure.InfrastructurePicture;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.IaaSHealth;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.HostAffected;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.IaaSMetric;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.IaasMachine;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.PrismaMonitoringAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
@Local(MonitoringBean.class)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class MonitoringBean {

    private static final Logger LOG = LogManager.getLogger(MonitoringBean.class);

    private PrismaMonitoringAPIClient monitAPIClient;

    @Inject
    private EnvironmentConfig envConfig;

    @PostConstruct
    private void init() {
	LOG.debug("Init monitoring bean");
	this.monitAPIClient = new PrismaMonitoringAPIClient(envConfig.getMonitoringEndpoint());
    }

    /**
     * Retreive all the VMs in the group {@code groupName} Every VM containg the
     * the list of metrics {@code MetricsList} Every metric should contains the
     * iaasThresholds
     * 
     * @param adapterType
     * @param groupName
     * @return
     * @throws APIErrorException
     * @throws IOException
     * @throws TimeoutException
     * @throws Exception
     */
    public MonitoringWrappedResponseIaas getMachineByHostGroup(String adapterType, String groupName)
	    throws APIErrorException, IOException, TimeoutException, Exception {

	try{
	    return monitAPIClient.getMachineByHostGroup(adapterType, IaaSHostGroups.valueOf(groupName));
	} catch (RestClientException | NoMappingModelFoundException | MappingException | ServerErrorResponseException e){
	    throw new MonitoringException("Unable to retreive the list of host for HostGroup " + groupName, e);
	}
    }

    public MonitoringWrappedResponseIaas getItemsByHost(String adapterType, String groupName, String hostName)
	    throws ServerErrorResponseException, NotFoundMonitoringException {

	try {
	    return monitAPIClient.getItemsByHostGroupAndHost(adapterType, IaaSHostGroups.lookupFromName(groupName),
		    hostName);
	} catch (RestClientException | NoMappingModelFoundException | MappingException e) {
	    throw new NotFoundMonitoringException("Unable to retreive the list of host for hostName " + hostName, e);
	}
    }

    public MonitoringWrappedResponseIaas getIaaSStatusOverall(String adapterType, String groupName, String hostName)
	    throws APIErrorException, IOException, TimeoutException, Exception {

	if (hostName != null) {
	    return monitAPIClient.getItemsByHostGroupAndHost(adapterType, IaaSHostGroups.lookupFromName(groupName),
		    hostName);
	} else {
	    return monitAPIClient.getMachineByHostGroup(adapterType, IaaSHostGroups.lookupFromName(groupName));
	}

    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public InfrastructurePicture getInfrastructurePicture() throws Exception {

	InfrastructurePicture infrPict = new InfrastructurePicture();
	infrPict.setTestbed(envConfig.getEnvProfile());

	MonitoringWrappedResponseIaas monitoringWrappedResponseIaas = monitAPIClient.getMachineByHostGroup(
		envConfig.getMonitoringAdapterType(), IaaSHostGroups.INFRASTRUCTURE_HEALTH);

	/**
	 * In monitoringWrappedResponseIaas there is only one MachineGroup
	 * "HostGroups.INFRASTRUCTURE_HEALTH"
	 * 
	 * 
	 * 
	 */
	for (IaasMachine machine : monitoringWrappedResponseIaas.getIaasMachineGroups().get(0).getIaasMachines()) {
	    if (machine.getMachineName().equals(HostIaaS.PRISMA_IAAS.getHostName())) {
		Map<String, Object> metricValue = (Map<String, Object>) machine.getMetrics().get(0).getMetricValue();

		infrPict.setNetwork(InfrastructureBareMetalStatus.valueOf((String) metricValue
			.get(InfrastructureBareMetalMetric.NETWORK.getMetric())));
		infrPict.setStorage(InfrastructureBareMetalStatus.valueOf((String) metricValue
			.get(InfrastructureBareMetalMetric.STORAGE.getMetric())));
		float availableNodes = (Integer) metricValue.get(InfrastructureBareMetalMetric.AVAILABLE_NODES
			.getMetric());
		float totalNodes = (Integer) metricValue.get(InfrastructureBareMetalMetric.TOTAL_NODES.getMetric());
		infrPict.setAvailable_nodes((int) availableNodes);
		infrPict.setTotal_nodes((int) totalNodes);
		if ((availableNodes / totalNodes) > InfrastructureBareMetalStatus.OK.getLevel())
		    infrPict.setCompute(InfrastructureBareMetalStatus.OK);
		else if ((availableNodes / totalNodes) > InfrastructureBareMetalStatus.WARNING.getLevel())
		    infrPict.setCompute(InfrastructureBareMetalStatus.WARNING);
		else
		    infrPict.setCompute(InfrastructureBareMetalStatus.PROBLEM);

		break;
	    }
	}

	GroupsMachine groupMach = new GroupsMachine();
	groupMach.setInfrastructureGroupName(IaaSHostGroups.INFRASTRUCTURE_HEALTH.getGroupName());

	ArrayList<InfrastructureMachine> infrMachList = new ArrayList<InfrastructureMachine>();

	List<IaasMachine> iaasMachines = monitoringWrappedResponseIaas.getIaasMachineGroups().get(0).getIaasMachines();
	for (IaasMachine machinelist : iaasMachines) {
	    InfrastructureMachine infMach = new InfrastructureMachine();
	    infMach.setMachineName(machinelist.getMachineName());

	    ArrayList<IaaSMetric> metricsList = (ArrayList<IaaSMetric>) machinelist.getMetrics();

	    ArrayList<String> metricsWithProblem = new ArrayList<String>();
	    for (IaaSMetric metrics : metricsList) {
		if (!metrics.getMetricStatus().equals("OK")) {
		    metricsWithProblem.add(metrics.getMetricKey());
		}
	    }
	    if (metricsWithProblem.size() > 0)
		infMach.setMachineStatus(MachineStatus.PROBLEM.getStatus());
	    else
		infMach.setMachineStatus(MachineStatus.READY.getStatus());

	    infMach.setMachineIP(machinelist.getIp());
	    infrMachList.add(infMach);
	    infMach.setMachineMetrics(metricsWithProblem);
	}
	groupMach.setInfrastructureMachine(infrMachList);
	List<GroupsMachine> groupsMachines = new ArrayList<GroupsMachine>();
	groupsMachines.add(groupMach);
	infrPict.setMachineGroups(groupsMachines);

	return infrPict;
    }

    /**
     * Checks if the platform is available. This method calls the Monitoring Pillar in order to get all the exceeded thresholds.
     * 
     * @return true if not thresholds are exceeded.
     * @throws MonitoringException
     */
    public boolean isDeployAvailable() throws MonitoringException {

	try {
	    WrappedIaasHealthByTrigger wrappedIaasHealthByTrigger = monitAPIClient.getShotTriggerByHostGroup(
		    envConfig.getMonitoringAdapterType(), IaaSHostGroups.INFRASTRUCTURE_HEALTH.getGroupName());
	    if (wrappedIaasHealthByTrigger.getHostAffecteds().size() == 0)
		return true;

	    for (HostAffected host : wrappedIaasHealthByTrigger.getHostAffecteds()) {
		LOG.debug("Problem at: " + host.getHostName());
	    }
	    return false;
	} catch (RestClientException | NoMappingModelFoundException | MappingException | ServerErrorResponseException e) {
	    LOG.error("Monitoring Client Exception: " + e.getMessage());
	    throw new MonitoringException("Error in MonitoringAPIClient ", e);
	}
    }

    /**
     * 
     * Retrieve the status of BareMetal (Compute, Network and Storage)
     * 
     * @return @see{IaaSHealth} 
     * @throws MonitoringException
     */
    public IaaSHealth getIaaSHealth() throws MonitoringException {
	try {
	    MonitoringWrappedResponseIaas response = monitAPIClient.getPrismaIaasHealth(
		    envConfig.getMonitoringAdapterType(), IaaSHostGroups.IAAS.getGroupName(),
		    HostIaaS.PRISMA_IAAS.getHostName());
	   
	    IaaSHealth health = new IaaSHealth();
	  
	    Map<String, Object> o = (Map<String, Object>) response.getIaasMachineGroups().get(0).getIaasMachines()
		    .get(0).getMetrics().get(0).getMetricValue();


	    health.setNetwork((String) o.get(InfrastructureBareMetalMetric.NETWORK.getMetric()));
	    health.setStorage((String) o.get(InfrastructureBareMetalMetric.STORAGE.getMetric()));

	    float availableNodes = (Integer) o.get(InfrastructureBareMetalMetric.AVAILABLE_NODES.getMetric());
	    float totalNodes = (Integer) o.get(InfrastructureBareMetalMetric.TOTAL_NODES.getMetric());

	    health.setTotalNodes((int) totalNodes);
	    health.setAvailableNodes((int) availableNodes);

	    if ((availableNodes / totalNodes) > InfrastructureBareMetalStatus.OK.getLevel())
		health.setCompute(InfrastructureBareMetalStatus.OK.getStatus());
	    else if ((availableNodes / totalNodes) > InfrastructureBareMetalStatus.WARNING.getLevel())
		health.setCompute(InfrastructureBareMetalStatus.WARNING.getStatus());
	    else
		health.setCompute(InfrastructureBareMetalStatus.PROBLEM.getStatus());

	    return health;
	} catch (Exception e) {
	    throw new MonitoringException("Unable to retreive IaaS status Health");
	}
    }
}