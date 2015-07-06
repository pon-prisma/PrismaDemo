package it.monitoringpillar.wrapper;

import it.monitoringpillar.adapter.zabbix.MonitoringServerRouter;
import it.monitoringpillar.adapter.zabbix.handler.GroupIDByName;
import it.monitoringpillar.adapter.zabbix.handler.MetricsParserHelper;
import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.monitoringpillar.adapter.zabbix.iaas.ZabbixAdapterIaas;
import it.monitoringpillar.config.TimestampMonitoring;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.MonitoringMetricsIAASNames;
import it.prisma.domain.dsl.monitoring.businesslayer.iaas.hypervisor.HypervisorGroup;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.IaaSMetric;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.IaasGroupOfMachine;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.IaasMachine;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.IaasThresholdsList;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.PrismaIaasScript;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.prisma.utils.json.JsonUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;

@Stateless
public class WrapperIaaS implements Serializable {

	private static final long serialVersionUID = 1L;
	// private ZabbixAdapterIaas zabAdapIaas;
	// private Metric4Orch metricInfo;
	private String url;
	private String token;
	private String hostName;
	private String groupName;
	private String hostGroupIdtoAdapter;
	private String iaasServer;
	private String metricsServer;
	// private Service service;
	// private ArrayList<Service> servicesList;
	// private ArrayList<Metric4Orch> hostsMonitored;
	private String vmip;
	private String vmdns;
	private String templateName;
	private String templateId;
	private String hostIdtoAdapter = null;
	// private HostID zabHostId;
	private String metricName;
	private String metricID;
	private String metricKey;
	private String metricValueType;
	private String triggerExpression;
	private String triggerValue;
	private String connectedHost;
	private String tagService;
	private String group;

//	private HostIntoMonitoringPlatform hostsPlatf;
//	private GroupIntoMonitoringPlatform groupsPlatf;

	@Inject
	private ZabbixAdapterIaas zabAdapIaas;

	@Inject
	private GroupIDByName zabGroupId;

	@Inject
	private MonitoringServerRouter router;

	@Inject
	private TimestampMonitoring timeConv;
	
//	@Inject
//	private HostIntoMonitoringPlatform hostsPlatf;
//	
//	@Inject 
//	private GroupIntoMonitoringPlatform groupsPlatf;
	

	@PostConstruct
	public void initWrapperIaas()
	{
		this.url = null;
		this.token = null;
//		this.hostsPlatf = new HostIntoMonitoringPlatform();
//		this.groupsPlatf = new GroupIntoMonitoringPlatform();
	}

	/**
	 * 
	 * @param adapterType
	 * @param testbedType
	 * @param iaasType
	 * @return MonitoringWrappedResponseIaasV2
	 * @throws Exception
	 */
	public MonitoringWrappedResponseIaas getWrappedIaas(
			String testbedType,
			String url, String token, List<String> iaasTypes,
			List<String> iaashosts, List<ZabbixHostGroupResponse> groups, 
			String groupId, List<ZabbixMonitoredHostsResponse> hosts, String hostId) throws Exception, NamingException {
		MonitoringWrappedResponseIaas wrappedIaas = new MonitoringWrappedResponseIaas();
		wrappedIaas.setTestbed(testbedType);
		wrappedIaas.setIaasMachineGroups(getGroupInfo(testbedType, url,
				token, iaasTypes, iaashosts, groups, 
				 groupId, hosts, hostId));
		return wrappedIaas;
	}

	/**
	 * It gives info about groups in the platform
	 * 
	 * @param adapterType
	 * @param testbedType
	 * @param iaasType
	 * @return ArrayList<IaasMachineGroupsList> groups info
	 * @throws Exception
	 */

	private ArrayList<IaasGroupOfMachine> getGroupInfo(String testbedType,
			String url, String token, List<String> iaasType,
			List<String> iaashost, List<ZabbixHostGroupResponse> groups, 
			String groupId, List<ZabbixMonitoredHostsResponse> hosts, String hostId) throws Exception, NamingException {

		List<ZabbixHostGroupResponse> hostgroupInfo = groups;
		
		ArrayList<IaasGroupOfMachine> groupsList = new ArrayList<>();
		boolean groupFound = false;
		int i = 0;
		for (i = 0; i < hostgroupInfo.size(); i++) {

			IaasGroupOfMachine hostgroupList = new IaasGroupOfMachine();
			
			hostgroupList.setIaasMachinesList(new ArrayList<IaasMachine>());

			groupName = hostgroupInfo.get(i).getName();

			/*******************************
			 * HOST GROUP SPECIFIED
			 ******************************/

			if (!(iaasType == null)
					&& (iaashost == null || iaashost.size() == 0)) {
				String group = "";
				String hostIdtoAdapter = null;
				int j = 0;
				for (j = 0; j < iaasType.size(); j++) {
					group = iaasType.get(j);
					if (group.equals(groupName)) {
						groupFound = true;
						hostGroupIdtoAdapter = hostgroupInfo.get(i)
								.getGroupid();
						hostgroupList.setIaasGroupName(groupName);
						hostgroupList.setIaasMachinesList(getMachinesList(
								testbedType, url, token, hostGroupIdtoAdapter,
								hostIdtoAdapter, hosts));
						groupsList.add(hostgroupList);
					}
				}
			}

			/*************************************************
			 * ASK FOR SPECIFIC HOST INTO A GROUP
			 ************************************************/

			else if (!(iaasType == null) && !(iaashost == null) && iaashost.size() > 0) {
				int j = 0;
				for (j = 0; j < iaasType.size(); j++) {
					group = iaasType.get(j);
					if (group.equals(groupName)) {
						groupFound = true;
						hostGroupIdtoAdapter = hostgroupInfo.get(i)
								.getGroupid();
						hostgroupList.setIaasGroupName(groupName);
						for (int p = 0; p < iaashost.size(); p++) {
							String hostVMName = iaashost.get(p);

							List<ZabbixMonitoredHostsResponse> hostInfo = hosts;
							
							for (ZabbixMonitoredHostsResponse hostResult : hostInfo) {
								hostName = hostResult.getName();
								if (hostName.equals(hostVMName)) {
									hostIdtoAdapter = hostResult.getHostid();
									break;
								}
							}
							if (!(hostName.equals(hostVMName))) {
								throw new NamingException(
										"Wrong resource Host name inserted or not existing into Monitoring Platform");
							}
							hostgroupList.getIaasMachines().addAll(
									(getMachinesList(testbedType, url, token,
											hostGroupIdtoAdapter,
											hostIdtoAdapter,  
											hosts)));
						}
						groupsList.add(hostgroupList);
					}
				}
			} else if (!(groupName.equals("Hypervisors")
					|| groupName.equals("Linux servers")
					|| groupName.equals("Virtual machines")
					|| groupName.equals("Discovered hosts")
					|| groupName.equals("Zabbix servers") || groupName
					.equals("Templates") 
					)
					&& iaasType == null) {
				hostGroupIdtoAdapter = hostgroupInfo.get(i).getGroupid();

				hostgroupList.setIaasGroupName(groupName);
				hostgroupList.setIaasMachinesList(getMachinesList(testbedType,
						url, token, hostGroupIdtoAdapter, hostIdtoAdapter, hosts));
				groupsList.add(hostgroupList);
			}
		}
		if (groupFound == false && i == iaasType.size()
				&& !(group.equals(groupName)))
			throw new NamingException(
					"Wrong resource Group name inserted or not existing");
		return groupsList;
	}

	/**
	 * 
	 * @param adapterType
	 * @param testbedType
	 * @param hostGroupIdtoAdapter
	 * @return ArrayList<IaasMachinesList>
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	private ArrayList<IaasMachine> getMachinesList(String testbedType,
			String url, String token, String hostGroupIdtoAdapter,
			String hostIdtoAdapter, List<ZabbixMonitoredHostsResponse> hostInfo) throws Exception, NamingException {
		ArrayList<IaasMachine> machinesList = new ArrayList<>();

		if (!(hostIdtoAdapter == null)) {
			machinesList = new ArrayList<IaasMachine>();

			for (ZabbixMonitoredHostsResponse hostResult : hostInfo) {
				IaasMachine machines = new IaasMachine();
				String hostIdtoAdapterCurrent = hostResult.getHostid();
				if (hostIdtoAdapterCurrent.equals(hostIdtoAdapter)) {
					String monitoredHost = hostResult.getAvailable();
					if (monitoredHost.equals("1")) {
						connectedHost = "MONITORED";
					} else {
						connectedHost = "No communication between the agent and the Server";
					}

					ArrayList<ZabbixItemResponse> zabbixinterfaceResponse = (ArrayList<ZabbixItemResponse>) zabAdapIaas
							.getZabbixFeature(testbedType, url, token,
									hostIdtoAdapter,
									ZabbixMethods.INTERFACE.getzabbixMethod());

					for (ZabbixItemResponse zabbixresult : zabbixinterfaceResponse) {
						vmip = zabbixresult.getIp();
						vmdns = zabbixresult.getDns();
					}

					// Set Hosts' carachteristics

					machines.setMachineName(hostName);
					machines.setIp(vmip);
					machines.setConnection(connectedHost);

					machines.setMetrics(getIaasMetrics(testbedType, url,
							token, hostIdtoAdapter));

					machinesList.add(machines);
				}
			}
		}

		// no HostID
		else if (hostIdtoAdapter == null) {
			machinesList = new ArrayList<IaasMachine>();
			
			for (ZabbixMonitoredHostsResponse hostResult : hostInfo) {
				IaasMachine machines = new IaasMachine();
				hostName = hostResult.getName();
				if (hostName != "Zabbix server") {
					hostIdtoAdapter = hostResult.getHostid();
					String monitoredHost = hostResult.getAvailable();
					if (monitoredHost.equals("1")) {
						connectedHost = "MONITORED";
					} else {
						connectedHost = "No communication between the agent and the Server";
					}

					// if (IaaS==true)
					ArrayList<ZabbixItemResponse> zabbixinterfaceResponse = (ArrayList<ZabbixItemResponse>) zabAdapIaas
							.getZabbixFeature(testbedType, url, token,
									hostIdtoAdapter,
									ZabbixMethods.INTERFACE.getzabbixMethod());

					for (ZabbixItemResponse zabbixresult : zabbixinterfaceResponse) {
						vmip = zabbixresult.getIp();
						vmdns = zabbixresult.getDns();
					}
				}
				// Set Hosts' carachteristics
				machines.setMachineName(hostName);
				machines.setIp(vmip);
				machines.setConnection(connectedHost);
				machines.setMetrics(getIaasMetrics(testbedType, url, token,
						hostIdtoAdapter));
				machinesList.add(machines);
			}
		}
		return machinesList;
	}

	/**
	 * For a certain host and a certain service category gets metrics associated
	 * to it
	 * 
	 * @param adapterType
	 * @param testbedType
	 * @param hostidtoAdapter
	 * @return List<IaasMetricsList>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<IaaSMetric> getIaasMetrics(String testbedType,
			String url, String token, String hostidtoAdapter) throws Exception,
			NamingException {
		ArrayList<IaaSMetric> metrics = new ArrayList<>();

		ArrayList<ZabbixItemResponse> metrics4Host = (ArrayList<ZabbixItemResponse>) zabAdapIaas
				.getMetricsList(testbedType, url, token, hostidtoAdapter,
						templateId, ZabbixMethods.METRIC.getzabbixMethod());

		//Loop into all Metrics
		for (ZabbixItemResponse metrictype : metrics4Host) {
			IaaSMetric iaasMetrics = new IaaSMetric();
			metricName = metrictype.getName();
			metricID = metrictype.getItemid();
			String metricMultiplier = metrictype.getMultiplier();
			String metricOffset = metrictype.getDelta();
			metricKey = metrictype.getKey();
			String metricKeyDebug = groupName + "." + hostName + "."
					+ metricName;

			String metricTime = timeConv.decodUnixTime2Date(Long.parseLong(metrictype.getLastclock()));

			String metricValueType = metrictype.getValueType();
			
			Object metricValue = MetricsParserHelper.getMetricIaaSParsedValue(metricValueType, metrictype.getLastvalue());
			
			//if Metrics are values coming from External Scritps manage this
			if (metricName.equals(MonitoringMetricsIAASNames.PRISMA_IAAS_SCRIPT
					.getzabbixMetricsIAASNames())) {
				PrismaIaasScript scriptIaas = (PrismaIaasScript) JsonUtility
						.deserializeJson(metricValue.toString(),
								PrismaIaasScript.class);
				metricValue = scriptIaas;

				if (scriptIaas.getNetwork().equals("OK")
						&& scriptIaas.getStorage().equals("OK")
						&& scriptIaas.getAvailableNodes() > 0) {
					iaasMetrics.setMetricStatus("OK");
				} else
					iaasMetrics.setMetricStatus("PROBLEM");
			}
			
			else if (metricName
					.equals(MonitoringMetricsIAASNames.HYPERVISOR_IAAS_SCRIPT
							.getzabbixMetricsIAASNames())) {
				HypervisorGroup hypervisorScript = (HypervisorGroup) JsonUtility
						.deserializeJson(metricValue.toString(),
								HypervisorGroup.class);
				metricValue = hypervisorScript;
			}
			iaasMetrics.setMetricName(metricName);
			iaasMetrics.setMetricValue(metricValue);
			iaasMetrics.setMetricTime(metricTime);

			if (!(metricName == MonitoringMetricsIAASNames.PRISMA_IAAS_SCRIPT
					.getzabbixMetricsIAASNames())) {

				if (!(metricValue.equals("0") || metricValue==null)) {
					iaasMetrics.setMetricStatus("OK");
				} else
					iaasMetrics.setMetricStatus("PROBLEM at: " + metricKeyDebug);
			}
			iaasMetrics.setIaasThresholds(getThreshold(testbedType, url, token, metricID));
			iaasMetrics.setMetricKey(metricKeyDebug);
			metrics.add(iaasMetrics);
		}
		return metrics;
	}

	/**
	 * Get Triggers for each metric
	 * 
	 * @param adapterType
	 * @param testbedType
	 * @param metricID
	 * @return ArrayList<IaasThresholdsList>
	 * @throws Exception
	 */
	private List<IaasThresholdsList> getThreshold(String testbedType,
			String url, String token, String metricID) throws Exception {

		ArrayList<ZabbixItemResponse> trigger4Host = (ArrayList<ZabbixItemResponse>) zabAdapIaas
				.getTriggerList(testbedType, url, token, hostIdtoAdapter,
						metricID, ZabbixMethods.TRIGGER.getzabbixMethod());
		ArrayList<IaasThresholdsList> thresholds = new ArrayList<IaasThresholdsList>();
		for (ZabbixItemResponse triggerList : trigger4Host) {
			IaasThresholdsList iaasThresholds = new IaasThresholdsList();

			triggerExpression = triggerList.getExpression();
			triggerValue = triggerList.getValue();
			String triggerKey = triggerList.getKey();
			String triggerKeyDebug = groupName + "." + hostName + "."
					+ metricName + "." + triggerExpression;
			String triggerPriority = triggerList.getPriority();

			iaasThresholds.setTriggerKey(triggerKeyDebug);

			iaasThresholds.setTriggerExpression(triggerExpression);
			iaasThresholds.setTriggerValue(triggerValue);
			if (triggerValue.equals("0")) {
				iaasThresholds.setTriggerStatus("OK");
			} else {
				iaasThresholds.setTriggerStatus("PROBLEM at: "
						+ triggerKeyDebug);
			}
			thresholds.add(iaasThresholds);
		}
		return thresholds;
	}
}
