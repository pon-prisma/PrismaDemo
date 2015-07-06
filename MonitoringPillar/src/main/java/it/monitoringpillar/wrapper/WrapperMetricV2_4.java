package it.monitoringpillar.wrapper;

import it.monitoringpillar.adapter.zabbix.handler.GroupIDByName;
import it.monitoringpillar.adapter.zabbix.handler.HostIDByName;
import it.monitoringpillar.adapter.zabbix.handler.MetricsParserHelper;
import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.monitoringpillar.adapter.zabbix.metrics.ZabbixAdapterSetter;
import it.monitoringpillar.config.TimestampMonitoring;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.Group;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MetricsHistoryTimeRequest;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.PaaSMetric;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.PaasMachine;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.PaasThreshold;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.PaasThreshold.PaasThresholdStatus;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.Service;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV2_4;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponseV2_4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;


@Stateless
public class WrapperMetricV2_4 implements Serializable{

	private static final long serialVersionUID = 1L;
	private MonitoringWrappedResponsePaas wrappedPaas;

	@Inject
	private HostIDByName zabHostId;

	@Inject
	private GroupIDByName zabGroupId;

	@Inject 
	private ZabbixAdapterSetter zabAdapMetrics;

	@Inject
	private TimestampMonitoring timeConv;

	public WrapperMetricV2_4(){
		this.wrappedPaas = new MonitoringWrappedResponsePaas();
	}


	/**
	 * 
	 * @param testbedType
	 * @param url
	 * @param token
	 * @param groupName
	 * @param vmuuid
	 * @param serviceCategory
	 * @param tag_service
	 * @param atomic_service_id
	 * @param metrics_id
	 * @param triggers_id
	 * @param history
	 * @param server
	 * @param requestTime
	 * @param groupInfo
	 * @param groupId
	 * @param hostInfo
	 * @param hostId
	 * @param items
	 * @param templates
	 * @return
	 * @throws Exception
	 * @throws NamingException
	 */
	public MonitoringWrappedResponsePaas getWrappedPaas(
			String testbedType,
			String url,
			String token,
			String groupName, 
			String vmuuid, 
			String serviceCategory,
			String tag_service, 
			List<String> atomic_service_id, 
			List<String> metrics_id, 
			List<String> triggers_id,
			String history,
			String server, 
			String requestTime, 

			List<ZabbixHostGroupResponse> groupInfo,
			String groupId,
			List<ZabbixMonitoredHostResponseV2_4> hostInfo, 
			String hostId, 
			List<ZabbixItemResponse> items, 
			List<ZabbixTemplateResponseV2_4> templates, 
			Map<ZabbixMonitoredHostResponseV2_4, List<ZabbixTemplateResponseV2_4>> hostsByTagMap,
			Map<ZabbixTemplateResponseV2_4, List<ZabbixItemResponse>> itemsByTemplateMap
			) throws Exception, NamingException {	

		wrappedPaas = new MonitoringWrappedResponsePaas();

		wrappedPaas.setEnvironment(testbedType);

		wrappedPaas.setGroups(getGroupInfo( 
				testbedType,
				url, 
				token,
				groupName, 
				vmuuid,
				serviceCategory,
				tag_service,
				atomic_service_id,
				metrics_id, 
				triggers_id,
				history, 
				requestTime, 
				groupInfo,
				groupId,
				hostInfo, 
				hostId,
				items, 
				templates, 
				hostsByTagMap,
				itemsByTemplateMap
				));
		return wrappedPaas;
	}

	/**
	 * 
	 * @param adapterType
	 * @param testbedType
	 * @param vmuuid
	 * @param paasKey
	 * @return ArrayList<WorkGroup>
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ArrayList<Group> getGroupInfo(
			String testbedType,
			String url,
			String token,
			String groupName,
			String vmuuid,
			String serviceCategory,
			String tag_service,
			List<String> atomic_service_id, 
			List<String> metrics_id, 
			List<String> triggers_id,
			String history, 
			String requestTime, 
			List<ZabbixHostGroupResponse> workgroupInfo,
			String groupId,
			List<ZabbixMonitoredHostResponseV2_4> hostInfo, 
			String hostId, 
			List<ZabbixItemResponse> items, 
			List<ZabbixTemplateResponseV2_4> templates, 
			Map<ZabbixMonitoredHostResponseV2_4, List<ZabbixTemplateResponseV2_4>> hostsByTagMap, 
			Map<ZabbixTemplateResponseV2_4, List<ZabbixItemResponse>> itemsByTemplateMap
			) throws Exception, NamingException{

		//	if (IaaS==true)
		String hostGroupIdtoAdapter = null;
		ArrayList<ZabbixItemResponse> zabbixTemplateResponse = new ArrayList<>();
		ArrayList<ZabbixItemResponse> metrics4Host = new ArrayList<>();
		ArrayList<ZabbixItemResponse> trigger4Host = new ArrayList<>();

		//Prepare the Array for groups
		List<Group> groups = new ArrayList<>();
		boolean workgroupFound = false;
		String hostName = null;
		String workgroupName=null;
		String serviceName=null;

		//For each group into array set the name and ask collect the machines
		for (ZabbixHostGroupResponse workgroup : workgroupInfo){  
			Group workGroup = new Group();
			workGroup.setPaasMachines(new ArrayList<PaasMachine>());

			workgroupName = workgroup.getName();
			if (groupName.equals(workgroupName)){
				workgroupFound=true;
				workGroup.setGroupName(workgroupName);
			}		

			/********************************
			 * HOST UUID OR TAG ID AVAILABLE
			 ********************************/
			if(hostInfo!=null && hostId!=null){

				String metricIdtoAdapter=null;
				String templateIdtoAdapter=null;
				String triggerIdtoAdapter=null;

				String tag="";

				//Loop inside every host into array coming from Manager				

				for (ZabbixMonitoredHostResponseV2_4 host: hostInfo){
					PaasMachine paasMachine = new PaasMachine();
					paasMachine.setServices(new ArrayList<Service>());

					if(tag_service!=null){
						templates = hostsByTagMap.get(host);
						tag = tag_service;
					}

					serviceCategory = host.getInventory().getType();
					paasMachine.setServiceCategory(serviceCategory);

					hostName = host.getName();

					paasMachine.setServiceId(tag);

					hostId = host.getHostid();

					hostGroupIdtoAdapter = workgroup.getGroupid();

					workGroup.getPaasMachines().addAll(
							getMachinesList(
									testbedType,
									url,
									token,
									hostGroupIdtoAdapter,
									templateIdtoAdapter, 
									metricIdtoAdapter,
									triggerIdtoAdapter,
									history,
									workgroupName, 
									serviceName, 
									hostName,
									metrics_id, 
									requestTime, 
									hostInfo,
									host,
									hostId,
									paasMachine,
									items,
									templates, 
									itemsByTemplateMap
									));
				}
					groups.add(workGroup);
			}
			/**********************
			 * Only group specified
			 **********************/

			else{
				String hostIdtoAdapter = null;
				String metricIdtoAdapter = null;
				String templateIdtoAdapter = null;
				String triggerIdtoAdapter = null;

				hostGroupIdtoAdapter = workgroup.getGroupid();
				workgroupName = workgroup.getName();
				workGroup.setGroupName(workgroupName);

				hostInfo = 
						zabAdapMetrics.getMonitoredHosts(
								testbedType, url, token, ZabbixMethods.HOST.getzabbixMethod(), hostIdtoAdapter, hostGroupIdtoAdapter, hostName);

				for(ZabbixMonitoredHostResponseV2_4 host : hostInfo){
					PaasMachine paasMachine = new PaasMachine();
					workGroup.setPaasMachines(getMachinesList(
							testbedType,
							url,
							token,
							hostGroupIdtoAdapter, 
							templateIdtoAdapter, 
							metricIdtoAdapter, 
							triggerIdtoAdapter, 
							history,
							serviceCategory, 
							serviceName, 
							hostName,
							metrics_id, 
							requestTime,
							hostInfo, 
							host,
							hostId,
							paasMachine,
							items, 
							templates, 
							itemsByTemplateMap
							));
					groups.add(workGroup);
				}
			}
		}
		if (workgroupFound==false){
			throw new NamingException("Wrong resource Group Name inserted or not existing into monitoring platform");	 
		}
		return (ArrayList<Group>) groups;	
	}

	/**
	 * Retrieves Machines depending on passed parameters
	 * @param adapterType
	 * @param testbedType
	 * @param hostGroupIdtoAdapter
	 * @param searchByGroup 
	 * @return ArrayList<PaasMachinesList>
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ArrayList<PaasMachine> 
	//	PaasMachine 
	getMachinesList(
			String testbedType,
			String url,
			String token,
			String hostGroupIdtoAdapter, 
			String templateIdtoAdapter, 
			String metricIdtoAdapter, 
			String triggerIdtoAdapter,
			String history,
			String serviceCategory, 
			String serviceName, 
			String hostName,
			List<String> metricName,  
			String requestTime, 
			List<ZabbixMonitoredHostResponseV2_4> hostInfo, 
			ZabbixMonitoredHostResponseV2_4 host,
			String hostId,
			PaasMachine paasMachine,
			List<ZabbixItemResponse> items, 
			List<ZabbixTemplateResponseV2_4> templates, 
			Map<ZabbixTemplateResponseV2_4, List<ZabbixItemResponse>> itemsByTemplateMap

			) throws Exception, NamingException {

		List<PaasMachine> machines = new ArrayList<>();
		String vmip = null;
		ArrayList<ZabbixItemResponse> zabbixinterfaceResponse = new ArrayList<>();

		hostName = host.getName();
		hostId = host.getHostid();	
		String monitoredHost = host.getAvailable();

		if(monitoredHost.equals("1")){
			String connectedHost = "MONITORED";
		}
		else{
			String connectedHost = "External Script Communication, Failed Comunication between agent and server or Disabled";
		}
		//API useful for getting IP and Host's networkInfo
		zabbixinterfaceResponse = 
				(ArrayList<ZabbixItemResponse>) zabAdapMetrics.getZabbixFeature(
						testbedType, 
						url,
						token,
						hostId,
						templateIdtoAdapter,
						ZabbixMethods.INTERFACE.getzabbixMethod() );

		for(ZabbixItemResponse zabbixresult : zabbixinterfaceResponse){
			vmip = zabbixresult.getIp();
			String vmdns = zabbixresult.getDns();
		}
		paasMachine.setMachineName(hostName);
		paasMachine.setIp(vmip);
		paasMachine.setServiceCategory(host.getInventory().getType());
		paasMachine.setServiceId(host.getInventory().getTag());

		paasMachine.setServices(getAtomicService(
				testbedType,
				url,
				token,
				hostId, 
				templateIdtoAdapter,
				metricIdtoAdapter, 
				triggerIdtoAdapter,
				history,
				serviceCategory, 
				serviceName, 
				hostName,
				metricName, 
				items,
				templates,
				itemsByTemplateMap, 
				requestTime

				));
		machines.add(paasMachine);

		if (machines.isEmpty()){
			//		if(paasMachine==null){
			throw new NamingException("Host ID is not present in zabbix server");
		}
		//		return paasMachine;
		return (ArrayList<PaasMachine>) machines;
	}


	@SuppressWarnings("unchecked")
	public ArrayList<Service> getAtomicService(
			String testbedType,
			String url,
			String token,
			String hostIdtoAdapter, 
			String templateIdtoAdapter,
			String metricIdtoAdapter, 
			String triggerIdtoAdapter,
			String history,
			String serviceCategoryName, 
			String serviceName, 
			String hostName,
			List<String> metrics_id,
			List<ZabbixItemResponse> items,
			List<ZabbixTemplateResponseV2_4> templates,
			Map<ZabbixTemplateResponseV2_4, List<ZabbixItemResponse>> itemsByTemplateMap,
			String requestTime

			) throws Exception, NamingException {

		ArrayList<Service> services = new ArrayList<>();

		for(ZabbixTemplateResponseV2_4 template : templates){

			Service service = new Service();

			String templateName = template.getName();
			String stringToFilter = templateName;
			serviceName = stringToFilter.substring(stringToFilter.lastIndexOf("ate")+3);
			service.setServiceName(serviceName);

			if(!(itemsByTemplateMap.isEmpty())){
				items = itemsByTemplateMap.get(template);
			}
			else{
				//GET ONLY USEFUL ITEMS REALLY ASSOCIATED TO TEMPLATES
				List<ZabbixItemResponse> usefulItems = new ArrayList<>();
				for(ZabbixItemResponse itemFromExtHost: items){
					for (ZabbixItemResponse item : template.getItems()){
						if(item.getName().equals(itemFromExtHost.getName())){
							usefulItems.add(itemFromExtHost);
						}
					}
				}
			}
			service.setPaasMetrics(getPaasMetrics(
					testbedType, 
					url,
					token,
					null,
					templateName,
					metricIdtoAdapter, 
					items, 
					history, 
					serviceCategoryName, 
					serviceName, 
					hostName,
					metrics_id, 
					requestTime
					));

			services.add(service);
		}
		return services;

	}

	/*
	 * For a certain host and a certain service category gets metrics associated to it
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<PaaSMetric> getPaasMetrics( 
			String testbedType,
			String url,
			String token,
			String hostidtoAdapter,
			String templateName,
			String metricIdtoAdapter, 
			List<ZabbixItemResponse> items,
			String history,
			String serviceCategoryName, 
			String serviceName,
			String hostName,
			List<String> metrics_id, 
			String requestTime

			) throws Exception, NamingException{

		ArrayList<PaaSMetric> metrics = new ArrayList<>();

		/********************
		 * METRIC LAST VALUE 
		 ********************/
		if (history==null){

			PaaSMetric paasMetrics = new PaaSMetric();

			//Only for the the specified Metric into API
			if(metrics_id!=null){
				//				for(ZabbixTemplateResponseV2_4 template : templates){
				//					if (template.getItems().get(0).getName().equals(metrics_id)){
				//						setMetrics(testbedType, url, token, hostidtoAdapter, item, items, serviceCategoryName, hostName, serviceName, services,  metrics);
				//					} 
				//				}
			}
			else{
				for(ZabbixItemResponse item : items){
					PaaSMetric paasMetric = new PaaSMetric();
					setMetrics(testbedType, url, token, hostidtoAdapter, item, items, serviceCategoryName, hostName, serviceName, metrics, paasMetric);
				}
			}
			return metrics;
		}

		/********************
		 *     HISTORY
		 *******************/
		else if (!(metricIdtoAdapter==null) && !(history==null))
		{	
			PaaSMetric paasMetrics = new PaaSMetric();
			ArrayList<ZabbixItemResponse> metrics4Host = 
					(ArrayList<ZabbixItemResponse>) zabAdapMetrics.
					getMetricsList( 
							testbedType, 
							url,
							token,
							hostidtoAdapter,
							templateName,
							metricIdtoAdapter,
							null,
							ZabbixMethods.HISTORY.getzabbixMethod(),
							requestTime
							);

			MetricsHistoryTimeRequest timerequest = new MetricsHistoryTimeRequest(); 

			ArrayList<Float> values4Graph = new ArrayList<Float>();
			ArrayList<String> clock4Graph = new ArrayList<String>();
			ArrayList<String> time4Graph = new ArrayList<String>();

			// If the value of metric is different from null and from a empty string and time returned is null as well then return metric as -1 value
			Float metricValue = null;
			for(ZabbixItemResponse metrictype : metrics4Host){
				//				String metricInstantTime = timeConv.decodUnixTime2Date(Long.parseLong(metrictype.getLastclock()));
				if (metrictype.getLastvalue()!=null && !(metrictype.getLastvalue().equals("")) ){//&& metrictype.getDelay() 
					metricValue = MetricsParserHelper.getMetricParsedValue("0", metrictype.getLastvalue());
				}
				else metricValue = (float) -1;

				values4Graph.add(metricValue);

				paasMetrics.setHistoryValues(values4Graph);
				//Check this out better (the value of Type coming)
				paasMetrics.setMetricValue(metricValue);

				String metricName = metrictype.getName();

				clock4Graph.add(timeConv.decodUnixTime2Date(Long.parseLong(metrictype.getClock())));

				paasMetrics.setHistoryClock(clock4Graph);
				//paasMetrics.setMetricTime(clock4Graph.get(0));
				paasMetrics.setMetricName(metricName);
				String metricKeyDebug = serviceCategoryName + "." + hostName + "." + serviceName + "." + metricName;
				paasMetrics.setMetricKey(metricKeyDebug);
			}
			metrics.add(paasMetrics);

			/*
			 * ToDo: Implement the logic for processing the data to return in function of requestTime
			 */
			if(!(requestTime==null)){

				for(String dateValue : clock4Graph){
					String datefrom = timerequest.getDateFrom().getYear()+"-"+
							timerequest.getDateFrom().getMonth()+"-"+
							timerequest.getDateFrom().getDay();
					String dateto = timerequest.getDateTo().getYear()+"-"+
							timerequest.getDateTo().getMonth()+"-"+
							timerequest.getDateTo().getDay();
					if(dateValue.substring(0,9).contains(datefrom) || dateValue.contains(dateto)){

						paasMetrics.getHistoryClocks();

						//EVERY 30S METRICS
						if ( !(history==null) && !(requestTime==null) && timerequest.getFilterTime().getEvery30s() ){
						}
						//EVERY 30MINS
						else if ( !(history==null) && !(requestTime==null) && timerequest.getFilterTime().getEvery10mis() ){
						}
						//EVERY 30MINS
						else if ( !(history==null) && !(requestTime==null) && timerequest.getFilterTime().getEvery30mins() ){
						}
					}
					else timerequest.setWarn("NO HISTORY FOR THE TIME INDICATED");
				}
			}
			return metrics;
		}
		return metrics;
	}

	private void setMetrics(
			String testbedType, 
			String url, 
			String token,
			String hostidtoAdapter,
			ZabbixItemResponse item, 
			List<ZabbixItemResponse> items,
			String serviceCategoryName, 
			String hostName, 
			String serviceName, 
			ArrayList<PaaSMetric> metrics,
			PaaSMetric paasMetric)
					throws NamingException, Exception{

		String metricName = item.getName();
		String metricIdtoAdapter = item.getItemid();
		String metricMultiplier = item.getMultiplier();
		String metricOffset = item.getDelta();
		String metricKey = item.getKey();
		String metricKeyDebug = serviceCategoryName + "." + hostName + "." + serviceName + "." + metricName;
		String metricUnit = item.getUnits();
		String metricValueType = item.getValueType();
		String time = item.getLastclock();

		// If the value of metric is different from null and from a empty string and time returned is null as well then return metric as -1 value
		Float metricValue = null;
		String metricInstantTime = timeConv.decodUnixTime2Date(Long.parseLong(time));
		if (item.getLastvalue()!=null && !(item.getLastvalue().equals("")) 
				//				&& !(metricInstantTime.contains("1970")) 
				){
			metricValue = MetricsParserHelper.getMetricParsedValue(metricValueType, item.getLastvalue());
		}
		else metricValue = (float) -1;

		paasMetric.setMetricName(metricName);
		paasMetric.setMetricValue(metricValue);
		metricUnit = item.getUnits();
		paasMetric.setMetricKey(metricKeyDebug);

		if (metricInstantTime.contains("1970"))
			paasMetric.setMetricTime("Instant null because no metrics were returned in the last 24hs");
		else paasMetric.setMetricTime(metricInstantTime);

		paasMetric.setPaasThresholds(getThreshold(
				testbedType,
				url,
				token,
				hostidtoAdapter,
				metricIdtoAdapter,
				serviceCategoryName, 
				serviceName, 
				hostName, 
				metricName, 
				metricValue
				));
		metrics.add(paasMetric);
	}

	/*
	 * Get Triggers for each metric
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PaasThreshold> getThreshold(
			String testbedType,
			String url,
			String token, 
			String hostidtoAdapter,
			String metricIdtoAdapter, 
			String serviceCategoryName, 
			String serviceName, 
			String hostName,
			String metricName, 
			Float metricValue
			) 
					throws Exception, NamingException {

		ArrayList<PaasThreshold> thresholds = new ArrayList<>();
		String triggerValue = null;
		ArrayList<ZabbixItemResponse> trigger4Host = new ArrayList<>();

		trigger4Host = 
				(ArrayList<ZabbixItemResponse>) zabAdapMetrics.
				getTriggerList( 
						testbedType,
						url,
						token,
						hostidtoAdapter, 
						metricIdtoAdapter, 
						ZabbixMethods.TRIGGER.getzabbixMethod());

		for (ZabbixItemResponse triggerList : trigger4Host)	{  
			PaasThreshold thresholdtype = new PaasThreshold();
			String triggerExpression = triggerList.getExpression();
			triggerValue = triggerList.getValue();
			String triggerKey = triggerList.getKey();
			String triggerKeyDebug = serviceCategoryName + "." + hostName + "." + serviceName + "." + metricName + "." + triggerExpression;
			String triggerPriority = triggerList.getPriority();

			if((triggerValue.equals("0")) && metricValue!=-1){
				thresholdtype.setThresholdStatus(PaasThresholdStatus.OK);
			}
			else thresholdtype.setThresholdStatus(PaasThresholdStatus.PROBLEM);

			thresholdtype.setThresholdExpression(triggerExpression);
			thresholdtype.setThresholdName(triggerList.getDescription());
			thresholds.add(thresholdtype);
		}
		return thresholds;
	}
}
