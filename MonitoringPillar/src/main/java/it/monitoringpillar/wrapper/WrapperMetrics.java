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
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;

@Stateless
public class WrapperMetrics implements Serializable{

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

	public WrapperMetrics(){
		this.wrappedPaas = new MonitoringWrappedResponsePaas();
	}


	/**
	 * It gets groups with hosts with templates with metrics with triggers
	 * @param testbedType
	 * @param url
	 * @param token
	 * @param vmuuid
	 * @param service_category_id
	 * @param tag_service
	 * @param atomic_service_id
	 * @param metrics_id
	 * @param triggers_id
	 * @param history
	 * @param server
	 * @param requestTime
	 * @return MonitoringWrappedResponsePaas
	 * @throws Exception
	 * @throws NamingException
	 */
	public MonitoringWrappedResponsePaas getWrappedPaas(
			String  testbedType,
			String url,
			String token,
			String group, 
			String vmuuid, 
			String serviceCategory,
			String tag_service, 
			List<String> atomic_service_id, 
			List<String> metrics_id, 
			List<String> triggers_id,
			String history,
			String server, 
			String requestTime, 
			ArrayList<ZabbixHostGroupResponse> groupInfo,
			String groupId,
			ArrayList<ZabbixMonitoredHostsResponse> hostInfo, 
			String hostId

			) throws Exception, NamingException {	

		wrappedPaas = new MonitoringWrappedResponsePaas();

		wrappedPaas.setEnvironment(testbedType);

		wrappedPaas.setGroups(getGroupInfo( 
				testbedType,
				url, 
				token,
				group, 
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
				hostId
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
			String group,
			String vmuuid,
			String serviceCategory,
			String tag_service,
			List<String> atomic_service_id, 
			List<String> metrics_id, 
			List<String> triggers_id,
			String history, 
			String requestTime, 
			ArrayList<ZabbixHostGroupResponse> workgroupInfo,
			String groupId,
			ArrayList<ZabbixMonitoredHostsResponse> hostInfo, 
			String hostId
			) throws Exception, NamingException{

		//	if (IaaS==true)
		String hostGroupIdtoAdapter = null;
		ArrayList<ZabbixItemResponse> zabbixTemplateResponse = new ArrayList<>();
		ArrayList<ZabbixItemResponse> metrics4Host = new ArrayList<>();
		ArrayList<ZabbixItemResponse> trigger4Host = new ArrayList<>();


		List<Group> groupsList = new ArrayList<>();
		boolean workgroupFound = false;
		String workgroupName = null;
		String hostName = null;
		String metricName=null;
		String templateName=null;
		String serviceName=null;

		int i=0;
		for (i=0; i<workgroupInfo.size(); i++) {  
			Group workGroup = new Group();
			workGroup.setPaasMachines(new ArrayList<PaasMachine>());

			workgroupName = workgroupInfo.get(i).getName();
			workGroup.setGroupName(group);

			if(group.equalsIgnoreCase(workgroupName) || group.contains("null")) { 
				workgroupFound=true;
				hostGroupIdtoAdapter = workgroupInfo.get(i).getGroupid();

				/***********************************
				 * Specific Service specified by TAG
				 **********************************/
				if(!(tag_service==null) && vmuuid==null && atomic_service_id==null && metrics_id==null && triggers_id==null){

					String metricIdtoAdapter=null;
					String templateIdtoAdapter=null;
					String triggerIdtoAdapter=null;
					PaasMachine machines = new PaasMachine();

					int t=0;
					String tag="";
					boolean inventoryTag = false;
					for (t=0; t<hostInfo.size(); t++){
						tag = hostInfo.get(t).getInventory().getTag();

						serviceCategory = hostInfo.get(t).getInventory().getType();

						machines.setServiceCategory(serviceCategory);

						hostName = hostInfo.get(t).getName();

						if(tag.equalsIgnoreCase(tag_service)){   
							inventoryTag = true;
							machines.setServiceId(tag);
							String hostIdtoAdapter = hostInfo.get(t).getHostid();

							//machinesList = new ArrayList<>();

							hostGroupIdtoAdapter = workgroupInfo.get(i).getGroupid();
							workgroupName = workgroupInfo.get(i).getName();

							workGroup.setGroupName(group);

							workGroup.getPaasMachines().addAll(
									getMachinesList(
											testbedType,
											url,
											token,
											hostGroupIdtoAdapter,
											hostIdtoAdapter, 
											templateIdtoAdapter, 
											metricIdtoAdapter,
											triggerIdtoAdapter,

											history,
											workgroupName, 
											serviceName, 
											hostName,
											metricName, 

											true,//searchByServiceTAG
											false, //searchByHOST
											false,  //searchByGroup,
											requestTime, 
											hostInfo, 
											hostId
											));
							groupsList.add(workGroup);
						}
					}
					if (inventoryTag==false && t==hostInfo.size() && !(tag.equalsIgnoreCase(tag_service))){
						throw new NamingException("Wrong resourse Tag ID inserted");
					}
				}
				/*****************
				 * Host Specified
				 *****************/
				else if(!(vmuuid==null) && atomic_service_id==null && metrics_id==null && triggers_id==null)
				{
					String metricIdtoAdapter=null;
					String templateIdtoAdapter=null;
					String triggerIdtoAdapter=null;

					String hostIdtoAdapter = null;
					for(ZabbixMonitoredHostsResponse hostResult : hostInfo)
					{
						hostName = hostResult.getName();
						if(hostName.equalsIgnoreCase(vmuuid)){
							hostIdtoAdapter = hostResult.getHostid();
							break;
						}
					}
					if (!(hostName.equalsIgnoreCase(vmuuid))){
						throw new NamingException("Wrong resourse host name inserted");
					}
					hostGroupIdtoAdapter = workgroupInfo.get(i).getGroupid();

					workgroupName = workgroupInfo.get(i).getName();
					workGroup.setGroupName(workgroupName);

					workGroup.setPaasMachines(getMachinesList(
							testbedType,
							url,
							token,
							hostGroupIdtoAdapter,
							hostIdtoAdapter, 
							templateIdtoAdapter, 
							metricIdtoAdapter,
							triggerIdtoAdapter,
							history,
							workgroupName, 
							serviceName, 
							hostName,
							metricName,
							false, //searchByServiceTAG, 
							true, //searchByHOST
							false, //searchByGroup
							requestTime, hostInfo, 
							hostId
							));
					groupsList.add(workGroup);
				}

				/**************************
				 * Atomic Service Specified
				 **************************/
				else if  (!(atomic_service_id==null) && metrics_id==null && triggers_id==null) 
				{   
					String metricIdtoAdapter=null;
					String triggerIdtoAdapter=null;
					String hostIdtoAdapter=null;
					String templateIdtoAdapter = null;

					//Get the TEMPLATE FIRSTLY
					zabbixTemplateResponse = 
							(ArrayList<ZabbixItemResponse>) zabAdapMetrics.getZabbixFeature( 
									testbedType,
									url,
									token,
									hostIdtoAdapter, 
									templateIdtoAdapter, 
									ZabbixMethods.TEMPLATE.getzabbixMethod() 
									);

					Iterator<ZabbixItemResponse> templateIt = zabbixTemplateResponse.iterator();
					boolean templateFound = false;
					while (!templateFound && templateIt.hasNext()) {
						ZabbixItemResponse adapterResult = templateIt.next();
						templateName = adapterResult.getName();
						serviceName = templateName.substring(templateName.lastIndexOf("ate")+3);

						for(String atomicService : atomic_service_id){
							if(serviceName.equalsIgnoreCase(atomicService)){
								templateIdtoAdapter = adapterResult.getTemplateid();
								templateFound = true;
							}
						}
						if(templateFound==false && !(templateIt.hasNext()))
							throw new NamingException("Wrong resource service name inserted or not existing");
					}

					//Get the HOST SECONDLY
					//					hostInfo = getHosts( testbedType, url, token, ZabbixMethods.HOST.getzabbixMethod(), null, hostGroupIdtoAdapter);
					//TODO
					//					hostInfo = hostsPlatf.getHostInfo();

					for(ZabbixMonitoredHostsResponse hostResult : hostInfo){
						hostName = hostResult.getName();
						if(hostName.equalsIgnoreCase(vmuuid)){
							hostIdtoAdapter = hostResult.getHostid();
							break;
						}
					}
					if (!(hostName.equalsIgnoreCase(vmuuid)))
						throw new NamingException("Wrong resourse host name inserted");

					//					hostGroupIdtoAdapter = serviceCategoryInfo.get(i).get("groupid");
					//					serviceCategoryName = serviceCategoryInfo.get(i).get("name");

					hostGroupIdtoAdapter = workgroupInfo.get(i).getGroupid();
					workgroupName = workgroupInfo.get(i).getName();

					//					workGroup.setCategoryName(serviceCategory);

					List<PaasMachine> machines =  getMachinesList(
							testbedType,
							url,
							token,
							hostGroupIdtoAdapter,
							hostIdtoAdapter, 
							templateIdtoAdapter,
							metricIdtoAdapter, 
							triggerIdtoAdapter, 

							history,
							serviceCategory, 
							serviceName, 
							hostName,
							metricName, 

							false, //searchByServiceTAG
							false, //searchByHOST
							false, //searchByGroup
							requestTime, 
							hostInfo, 
							hostId
							);

					List<Service> servicesChosen = machines.get(0).getServices();

					List<PaaSMetric> metricsIntoSpecificService = servicesChosen.get(0).getPaasMetrics();
					List<PaaSMetric> metricsReduced = new ArrayList<>();
					for (PaaSMetric metricsTobeChosen : metricsIntoSpecificService) {

						if ((Pattern.compile(Pattern.quote(serviceName), Pattern.CASE_INSENSITIVE).matcher(metricsTobeChosen.getMetricName()).find()))
						{
							metricsReduced.add(metricsTobeChosen);
						}
					}
					metricsIntoSpecificService = metricsReduced;
					List<Service> serviceChosenNew = new ArrayList<Service>();
					for (Service serviceTobeChosen : servicesChosen){
						serviceTobeChosen.setServiceName(servicesChosen.get(0).getServiceName());
						serviceTobeChosen.setPaasMetrics(metricsIntoSpecificService);
						serviceChosenNew.add(serviceTobeChosen);
					}
					servicesChosen = serviceChosenNew;

					List<PaasMachine> machinesNew = new ArrayList<PaasMachine>();
					for (PaasMachine machineTobeChosen : machines){
						machineTobeChosen.setMachineName(machines.get(0).getMachineName());
						machineTobeChosen.setIp(machines.get(0).getIp());
						machineTobeChosen.setServices(servicesChosen);
						machinesNew.add(machineTobeChosen);
					}
					machines = machinesNew;

					workGroup.setPaasMachines(machines);

					groupsList.add(workGroup);
				}

				/*******************
				 * Metric Specified
				 ******************/

				else if(!(metrics_id==null) && triggers_id==null && history==null)
				{	
					String triggerIdtoAdapter=null;
					String hostIdtoAdapter = null;
					String metricIdtoAdapter = null;
					String templateIdtoAdapter = null;

					//					hostName = hostInfo.get(0).getName();
					//					hostIdtoAdapter = checkHostId(hostName, vmuuid);

					//Get the specified METRIC SECONDLY
					metrics4Host = 
							(ArrayList<ZabbixItemResponse>) zabAdapMetrics.
							getMetricsList(
									testbedType, 
									url,
									token,
									hostId,
									templateIdtoAdapter,
									metricIdtoAdapter,
									null,
									ZabbixMethods.METRIC.getzabbixMethod(), 
									null //TimeRequestBody
									);

					Iterator<ZabbixItemResponse> it = metrics4Host.iterator();
					boolean itemFound=false; 
					while (!itemFound && it.hasNext())
					{
						ZabbixItemResponse adapterResult = it.next();
						metricName = adapterResult.getName();

						for (String metricPassed : metrics_id)
						{
							if(metricName.equalsIgnoreCase(metricPassed)){
								metricIdtoAdapter = adapterResult.getItemid();
								itemFound = true;
							}
						}
						if(itemFound==false && !(it.hasNext()))
							throw new NamingException("Wrong resource metric name inserted or not existing");
					}

					//Get TEMPLATE THIRDLY

					zabbixTemplateResponse = 
							(ArrayList<ZabbixItemResponse>) zabAdapMetrics.getZabbixFeature( 
									testbedType, 
									url,
									token,
									hostIdtoAdapter, 
									templateIdtoAdapter, 
									ZabbixMethods.TEMPLATE.getzabbixMethod()
									);

					Iterator<ZabbixItemResponse> templateIt = zabbixTemplateResponse.iterator();
					boolean templateFound = false;
					while (!templateFound && templateIt.hasNext()) {
						ZabbixItemResponse adapterResult = templateIt.next();
						templateName = adapterResult.getName();
						serviceName = templateName.substring(templateName.lastIndexOf("ate")+3);

						for(String atomicService : atomic_service_id){
							if(serviceName.equalsIgnoreCase(atomicService)){
								templateIdtoAdapter = adapterResult.getTemplateid();
								templateFound = true;
							}
						}
						if(templateFound==false && !templateIt.hasNext())
							throw new NamingException("Wrong resource service name inserted or not existing");
					}

					hostGroupIdtoAdapter = workgroupInfo.get(i).getGroupid();
					workgroupName = workgroupInfo.get(i).getName();

					//					workGroup.setCategoryName(serviceCategory);
					workGroup.setPaasMachines(getMachinesList(
							testbedType,
							url,
							token,
							hostGroupIdtoAdapter,
							hostId, 
							templateIdtoAdapter,
							metricIdtoAdapter, 
							triggerIdtoAdapter, 

							history,
							serviceCategory, 
							serviceName, 
							hostName,
							metricName, 

							false, //searchByServiceTAG
							false, //searchByHOST
							false,

							requestTime, 
							hostInfo, 
							hostId
							));
					groupsList.add(workGroup);

				}

				/*******************
				 * HISTORY specified
				 *******************/

				else if(!(metrics_id==null) && triggers_id==null && history!=null){   
					String metricIdtoAdapter=null;
					String triggerIdtoAdapter=null;
					String hostIdtoAdapter = null;
					String templateIdtoAdapter = null;

					//Get the specified METRIC FIRSTLY
					metrics4Host = 
							(ArrayList<ZabbixItemResponse>) zabAdapMetrics.
							getMetricsList(
									testbedType, 
									url, 
									token,
									hostId,
									null,
									metricIdtoAdapter,
									null,
									ZabbixMethods.METRIC.getzabbixMethod(),  
									null //TimeRequest
									);

					Iterator<ZabbixItemResponse> it = metrics4Host.iterator();
					boolean itemFound=false;
					while (!itemFound && it.hasNext())
					{
						ZabbixItemResponse adapterResult = it.next();
						metricName = adapterResult.getName();

						for (String metricPassed : metrics_id){
							if(metricName.equalsIgnoreCase(metricPassed)){
								metricIdtoAdapter = adapterResult.getItemid();
								itemFound = true;
							}	
						}
						if (itemFound==false && !(it.hasNext()))
							throw new NamingException("Wrong resource metric name inserted or not existing");
					}

					workGroup.setPaasMachines(getMachinesList(
							testbedType,
							url,
							token,
							hostGroupIdtoAdapter,
							hostId, 
							templateIdtoAdapter,
							metricIdtoAdapter, 
							triggerIdtoAdapter,

							history,
							serviceCategory, 
							serviceName, 
							hostName,
							metricName, 

							false, //searchByServiceTAG
							false, //searchByHOST
							false, //searchByGroup
							requestTime, 
							hostInfo, 
							hostId
							));
					groupsList.add(workGroup);
				}


				/*******************
				 * Trigger specified
				 *******************/
				//					else if(decodedPaasKey.length==5)//>4 && decodedPaasKey.length<5)
				else if(!(triggers_id==null))	
				{   
					String hostIdtoAdapter = null;
					String metricIdtoAdapter = null;
					String templateIdtoAdapter = null;
					String triggerIdtoAdapter = null;

					//Get TRIGGER FIRSTLY
					trigger4Host = 
							(ArrayList<ZabbixItemResponse>) zabAdapMetrics.
							getTriggerList(
									testbedType, 
									url,
									token,
									hostIdtoAdapter, 
									metricIdtoAdapter,
									ZabbixMethods.TRIGGER.getzabbixMethod());
					String triggerExpression="";
					for (ZabbixItemResponse triggerList : trigger4Host){  	
						triggerExpression = triggerList.getExpression();
						if (triggerExpression.equals(triggers_id)){
							triggerIdtoAdapter = triggerList.getTriggerid();
							break;
						}
					}
					if(!triggerExpression.equals(triggers_id))
						throw new NamingException("Wrong resource trigger inserted or not existing");

					//Get the specified METRIC SECONDLY
					metrics4Host = 
							(ArrayList<ZabbixItemResponse>) zabAdapMetrics.
							getMetricsList( 
									testbedType, 
									url,
									token,
									hostIdtoAdapter,
									templateIdtoAdapter,
									metricIdtoAdapter,
									null,
									ZabbixMethods.METRIC.getzabbixMethod(),
									null //TimeRequestBody
									);

					Iterator<ZabbixItemResponse> it = metrics4Host.iterator();
					boolean itemFound=false;
					while (!itemFound && it.hasNext()){
						ZabbixItemResponse adapterResult = it.next();

						metricName = adapterResult.getName();
						for (String metricPassed : metrics_id){
							if(metricName.equalsIgnoreCase(metricPassed)){
								metricIdtoAdapter = adapterResult.getItemid();
								itemFound = true;
							}
						}
						if(itemFound==false && !it.hasNext())
							throw new NamingException("Wrong resource metric name inserted or not existing");
					}

					//Get TEMPLATE ID THIRDLY
					zabbixTemplateResponse = 
							(ArrayList<ZabbixItemResponse>) zabAdapMetrics.getZabbixFeature( 
									testbedType, 
									url,
									token,
									hostIdtoAdapter,
									templateIdtoAdapter,
									ZabbixMethods.TEMPLATE.getzabbixMethod()
									);

					Iterator<ZabbixItemResponse> templateIt = zabbixTemplateResponse.iterator();

					boolean templateFound = false;
					while (!templateFound && templateIt.hasNext()) {
						ZabbixItemResponse adapterResult = templateIt.next();
						templateName = adapterResult.getName();
						serviceName = templateName.substring(templateName.lastIndexOf("ate")+3);

						for(String atomicService : atomic_service_id){
							if(serviceName.equals(atomicService)){
								templateIdtoAdapter = adapterResult.getTemplateid();
								templateFound = true;
							}
						}
						if (templateFound==false && !templateIt.hasNext()) 
							throw new NamingException("Wrong resource service name inserted or not existing into platform");
					}


					//Get the HOST LASTLY
					for(ZabbixMonitoredHostsResponse hostResult : hostInfo){
						hostName = hostResult.getName();

						if(hostName.equals(vmuuid)){
							hostIdtoAdapter = hostResult.getHostid();
							break;
						}
					}
					if(!hostName.equals(vmuuid))
						throw new NamingException("Wrong resource host name inserted or not existing");

					hostGroupIdtoAdapter = workgroupInfo.get(i).getGroupid();
					workgroupName = workgroupInfo.get(i).getName();
					//							.substring(0,10);

					//					workGroup.setCategoryName(serviceCategory);
					workGroup.setPaasMachines(getMachinesList(
							testbedType,
							url,
							token,
							hostGroupIdtoAdapter,
							hostIdtoAdapter, 
							templateIdtoAdapter,
							metricIdtoAdapter, 
							triggerIdtoAdapter, 

							history,
							serviceCategory, 
							serviceName, 
							hostName,
							metricName, 

							false,//searchByServiceTAG
							false, //searchByHOST
							false,//searchByGroup 
							requestTime, 
							hostInfo, 
							hostId
							));
					groupsList.add(workGroup);
				}	

				/**********************
				 * Only group specified
				 **********************/

				else 
				{
					String hostIdtoAdapter = null;
					String metricIdtoAdapter = null;
					String templateIdtoAdapter = null;
					String triggerIdtoAdapter = null;

					hostGroupIdtoAdapter = workgroupInfo.get(i).getGroupid();
					workgroupName = workgroupInfo.get(i).getName();

					workGroup.setGroupName(group);
					workGroup.setPaasMachines(getMachinesList(
							testbedType,
							url,
							token,
							hostGroupIdtoAdapter,
							hostIdtoAdapter, 
							templateIdtoAdapter, 
							metricIdtoAdapter, 
							triggerIdtoAdapter, 
							history,
							serviceCategory, 
							serviceName, 
							hostName,
							metricName, 
							false, //searchByServiceTAG
							false, //searchByHOST
							true, //searchByGroup
							requestTime,
							hostInfo, 
							hostId
							));
					groupsList.add(workGroup);
				}
			}
		}
		if (workgroupFound==false && i==workgroupInfo.size() && !(serviceCategory.equalsIgnoreCase(workgroupName)) ){
			throw new NamingException("Wrong resource Group Name inserted or not existing into monitoring platform");	 
		}
		return (ArrayList<Group>) groupsList;	
	}

	/**
	 * Retrieves Machines depending on passed parameters
	 * @param adapterType
	 * @param testbedType
	 * @param hostGroupIdtoAdapter
	 * @param searchByGroup 
	 * @param b 
	 * @param b 
	 * @return ArrayList<PaasMachinesList>
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ArrayList<PaasMachine> getMachinesList(
			String testbedType,
			String url,
			String token,
			String hostGroupIdtoAdapter, 
			String hostIdtoAdapter,
			String templateIdtoAdapter, 
			String metricIdtoAdapter, 
			String triggerIdtoAdapter,

			String history,
			String serviceCategory, 
			String serviceName, 
			String hostName,
			String metricName, 

			boolean searchByServiceTAG, 
			boolean searchByHOST, 
			boolean searchByGroup, 

			String requestTime, 
			ArrayList<ZabbixMonitoredHostsResponse> hostInfo, 
			String hostId

			) throws Exception, NamingException {

		ArrayList<PaasMachine> machinesList = new ArrayList<>();
		String vmip = null;
		String vmdns = null;
		String tag_service = null;

		if (!(hostIdtoAdapter==null)){
			//TODO: better to be passed from upper methods or getting it here? To consider all the cases this method is called
			hostInfo = 
					zabAdapMetrics.getMonitoredHosts(
							testbedType, url, token, ZabbixMethods.HOST.getzabbixMethod(), hostIdtoAdapter, hostGroupIdtoAdapter, hostName);

			machinesList = setHostWrappedParams(testbedType, url, token, hostIdtoAdapter, templateIdtoAdapter, hostInfo,  metricIdtoAdapter, 
					triggerIdtoAdapter, history, serviceCategory, serviceName, metricName, searchByServiceTAG, searchByHOST, searchByGroup, requestTime);

		}
		if (machinesList.isEmpty()){
			throw new NamingException("Host ID is not present in zabbix server");
		}
		else if (hostIdtoAdapter==null){
			PaasMachine machines = new PaasMachine();

			machinesList = 
					setHostWrappedParams(
							testbedType, url, token, hostIdtoAdapter, templateIdtoAdapter, hostInfo, metricIdtoAdapter, triggerIdtoAdapter, 
							history, serviceCategory, serviceName, metricName, searchByServiceTAG, searchByHOST, searchByGroup, requestTime);
		}
		return machinesList;
	}

	//HELPER and Setter for getMachinesList method
	private ArrayList<PaasMachine> setHostWrappedParams(
			String testbedType, 
			String url, 
			String token, 
			String hostIdtoAdapter, 
			String templateIdtoAdapter, 
			ArrayList<ZabbixMonitoredHostsResponse> hostInfo,

			String metricIdtoAdapter, 
			String triggerIdtoAdapter,

			String history,
			String serviceCategory, 
			String serviceName, 
			String metricName, 

			boolean searchByServiceTAG,
			boolean searchByHOST,
			boolean searchByGroup,
			String requestTime
			) throws NamingException, Exception{


		ArrayList<PaasMachine> machinesList = new ArrayList<>();
		String vmip = null;
		String vmdns = null;
		String tag_service = null;
		//		String currentHostId=null;

		ArrayList<ZabbixItemResponse> zabbixinterfaceResponse = new ArrayList<>();
		for(ZabbixMonitoredHostsResponse hostResult : hostInfo){

			if(hostIdtoAdapter.equals(hostResult.getHostid())){
				PaasMachine machines = new PaasMachine();
				String hostName = hostResult.getName();
				hostIdtoAdapter = hostResult.getHostid();	
				String monitoredHost = hostResult.getAvailable();

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
								hostIdtoAdapter,
								templateIdtoAdapter,
								ZabbixMethods.INTERFACE.getzabbixMethod()
								);

				for(ZabbixItemResponse zabbixresult : zabbixinterfaceResponse){
					vmip = zabbixresult.getIp();
					vmdns = zabbixresult.getDns();
				}
				machines.setMachineName(hostName);
				machines.setIp(vmip);
				machines.setServiceCategory(hostResult.getInventory().getType());
				machines.setServiceId(hostResult.getInventory().getTag());
				machines.setServices(getAtomicService(
						testbedType,
						url,
						token,
						hostIdtoAdapter, 
						templateIdtoAdapter, 
						metricIdtoAdapter, 
						triggerIdtoAdapter,
						history,
						serviceCategory, 
						serviceName, 
						hostName,
						metricName, 
						searchByServiceTAG,
						searchByHOST,
						searchByGroup,
						requestTime
						));
				machinesList.add(machines);
			}
		}
		return machinesList;
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
			String metricName, 

			boolean searchByServiceTAG, 
			boolean searchByHOST,
			boolean searchByGroup, 

			String requestTime

			) throws Exception, NamingException {

		ArrayList<Service> servicesList = new ArrayList<>();
		ArrayList<ZabbixItemResponse> zabbixTemplateResponse = new ArrayList<>();

		String templateName=null;
		//		zabAdapMetrics.initMetrics(testbedType);

		if(!(templateIdtoAdapter==null)) {
			zabbixTemplateResponse = 
					(ArrayList<ZabbixItemResponse>) zabAdapMetrics.getZabbixFeature(
							testbedType,  
							url,
							token,
							hostIdtoAdapter, 
							templateIdtoAdapter,
							ZabbixMethods.TEMPLATE.getzabbixMethod()
							);

			for(ZabbixItemResponse adapterResult :  zabbixTemplateResponse)	{
				String CurrentTemplateID = adapterResult.getTemplateid();
				if(CurrentTemplateID.equals(templateIdtoAdapter))
				{
					Service services = new Service();
					templateName = adapterResult.getName();
					serviceName = templateName.substring(templateName.lastIndexOf("ate")+3);
					services.setServiceName(serviceName);
					services.setPaasMetrics(getPaasMetrics(
							testbedType,
							url,
							token,
							hostIdtoAdapter,
							templateIdtoAdapter, 
							metricIdtoAdapter, 
							triggerIdtoAdapter, 

							history, 
							serviceCategoryName, 
							serviceName, 
							hostName,
							metricName, 

							searchByServiceTAG, 
							requestTime
							));
					servicesList.add(services);
				}
			}
			if (servicesList.isEmpty()){
				throw new NamingException("Template ID not existing in Zabbix Server");
			}
		}
		else if (templateIdtoAdapter==null)
		{
			zabbixTemplateResponse = 
					(ArrayList<ZabbixItemResponse>) zabAdapMetrics.getZabbixFeature(
							testbedType, 
							url,
							token,
							hostIdtoAdapter, 
							templateIdtoAdapter,
							ZabbixMethods.TEMPLATE.getzabbixMethod() 
							);

			for(ZabbixItemResponse adapterResult :  zabbixTemplateResponse)
			{
				Service services = new Service();

				templateName = adapterResult.getName();
				templateIdtoAdapter = adapterResult.getTemplateid();

				String stringToFilter = templateName;
				serviceName = stringToFilter.substring(stringToFilter.lastIndexOf("ate")+3);
				services.setServiceName(serviceName);

				if(searchByHOST==true  || searchByGroup==true){
					hostIdtoAdapter=null;
				}
				else if (searchByServiceTAG==true){
					templateIdtoAdapter=null;
				}  

				services.setPaasMetrics(getPaasMetrics(
						testbedType, 
						url,
						token,
						hostIdtoAdapter,
						templateIdtoAdapter, 
						metricIdtoAdapter, 
						triggerIdtoAdapter, 

						history, 
						serviceCategoryName, 
						serviceName, 
						hostName,
						metricName, 

						searchByServiceTAG, 

						requestTime
						));
				servicesList.add(services);
			}
		}
		return servicesList;
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
			String templateIdtoAdapter, 
			String metricIdtoAdapter, 
			String triggerIdtoAdapter, 

			String history,
			String serviceCategoryName, 
			String serviceName, 
			String hostName,
			String metricName, 

			boolean searchByServiceTAG, 

			String requestTime

			) throws Exception, NamingException{

		ArrayList<PaaSMetric> metrics = new ArrayList<>();

		/********************
		 * METRIC LAST VALUE 
		 ********************/
		if (!(metricIdtoAdapter==null) && history==null){

			PaaSMetric paasMetrics = new PaaSMetric();
			ArrayList<ZabbixItemResponse>  metrics4Host = 
					(ArrayList<ZabbixItemResponse>) zabAdapMetrics.
					getMetricsList( 
							testbedType,
							url,
							token,
							hostidtoAdapter,
							templateIdtoAdapter,
							metricIdtoAdapter,
							null,
							ZabbixMethods.METRIC.getzabbixMethod(),
							null //TimeRequestBody
							);

			for(ZabbixItemResponse metrictype : metrics4Host){
				String id = metrictype.getItemid();
				if(metricIdtoAdapter.equals(id)){

					String metricMultiplier = metrictype.getMultiplier();
					String metricOffset = metrictype.getDelta();
					String metricKey = metrictype.getKey();
					String metricKeyDebug = serviceCategoryName + "." + hostName + "." + serviceName + "." + metricName;
					String metricUnit = metrictype.getUnits();
					String metricValueType = metrictype.getValueType();
					
					// If the value of metric is different from null and from a empty string and time returned is null as well then return metric as -1 value
					Float metricValue = null;
					String metricInstantTime = timeConv.decodUnixTime2Date(Long.parseLong(metrictype.getLastclock()));
					if (metrictype.getLastvalue()!=null && !metrictype.getLastvalue().equals("") && !(metricInstantTime.contains("1970")) ){//&& metrictype.getDelay() 
						metricValue = MetricsParserHelper.getMetricParsedValue(metricValueType, metrictype.getLastvalue());
					}
					else metricValue = (float) -1;

					metricName = metrictype.getName();

					paasMetrics.setMetricName(metricName);
					paasMetrics.setMetricValue(metricValue);
					metricUnit = metrictype.getUnits();

					paasMetrics.setMetricKey(metricKeyDebug);
					if (metricInstantTime.contains("1970"))
						paasMetrics.setMetricTime("Instant null because no metrics were returned in the last 24hs");
					else paasMetrics.setMetricTime(metricInstantTime);

					paasMetrics.setPaasThresholds(getThreshold(
							testbedType,
							url, 
							token,
							hostidtoAdapter,
							metricIdtoAdapter,
							triggerIdtoAdapter, 

							serviceCategoryName, 
							serviceName, 
							hostName, 
							metricName
							));
					metrics.add(paasMetrics);
				}
			}
			return metrics;
		}

		/*****************************************************
		 * METRIC LAST VALUE itemIDnull: search by Service Tag 
		 *****************************************************/
		else if(metricIdtoAdapter==null){
			PaaSMetric paasMetrics = new PaaSMetric();

			ArrayList<ZabbixItemResponse>  metrics4Host = 
					(ArrayList<ZabbixItemResponse>) zabAdapMetrics.
					getMetricsList(
							testbedType, 
							url,
							token,
							hostidtoAdapter, 
							templateIdtoAdapter,
							metricIdtoAdapter,
							null,
							ZabbixMethods.METRIC.getzabbixMethod(),
							null //TimeRequestBody
							);

			ArrayList<ZabbixItemResponse> metricsFiltered = new ArrayList<>();

			for(int r=0; r<metrics4Host.size(); r++){
				String itemName = metrics4Host.get(r).getName();
				//if the metricName contains part of ServiceName then get it!!
				if (Pattern.compile(serviceName, Pattern.CASE_INSENSITIVE + Pattern.LITERAL).matcher(itemName).find()){
					metricsFiltered.add(metrics4Host.get(r));
				}
			}

			for(ZabbixItemResponse metrictype : metricsFiltered){
				paasMetrics = new PaaSMetric();
				metricName = metrictype.getName();
				metricIdtoAdapter = metrictype.getItemid();
				String metricMultiplier = metrictype.getMultiplier();
				String metricOffset = metrictype.getDelta();
				String metricKey = metrictype.getKey();
				String metricKeyDebug = serviceCategoryName + "." + hostName + "." + serviceName + "." + metricName;
				String metricUnit = metrictype.getUnits();
				String metricValueType = metrictype.getValueType();
				String time = metrictype.getLastclock();
				
				// If the value of metric is different from null and from a empty string and time returned is null as well then return metric as -1 value
				Float metricValue = null;
				String metricInstantTime = timeConv.decodUnixTime2Date(Long.parseLong(metrictype.getLastclock()));
				if (metrictype.getLastvalue()!=null && !(metrictype.getLastvalue().equals("")) && !(metricInstantTime.contains("1970")) ){//&& metrictype.getDelay() 
					metricValue = MetricsParserHelper.getMetricParsedValue(metricValueType, metrictype.getLastvalue());
				}
				else metricValue = (float) -1;
				
				paasMetrics.setMetricName(metricName);
				paasMetrics.setMetricValue(metricValue);
				metricUnit = metrictype.getUnits();
				paasMetrics.setMetricKey(metricKeyDebug);

				if (metricInstantTime.contains("1970"))
					paasMetrics.setMetricTime("Instant null because no metrics were returned in the last 24hs");
				else paasMetrics.setMetricTime(metricInstantTime);

				paasMetrics.setPaasThresholds(getThreshold(
						testbedType,
						url,
						token,
						hostidtoAdapter,
						metricIdtoAdapter,
						triggerIdtoAdapter,
						serviceCategoryName, 
						serviceName, 
						hostName, 
						metricName
						));
				metrics.add(paasMetrics);
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
							templateIdtoAdapter,
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

				clock4Graph.add(timeConv.decodUnixTime2Date(Long.parseLong(metrictype.getClock())));

				paasMetrics.setHistoryClock(clock4Graph);
//				paasMetrics.setMetricTime(clock4Graph.get(0));
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

	/*
	 * Get Triggers for each metric
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PaasThreshold> getThreshold(
			String testbedType,
			String url,
			String token, 
			String hostIdtoAdapter,
			String metricIdtoAdapter, 
			String triggerIdtoAdapter,

			String serviceCategoryName, 
			String serviceName, 
			String hostName,
			String metricName

			) throws Exception, NamingException
			{
		ArrayList<PaasThreshold> thresholds = new ArrayList<>();
		String triggerValue = null;
		ArrayList<ZabbixItemResponse> trigger4Host = new ArrayList<>();

		trigger4Host = 
				(ArrayList<ZabbixItemResponse>) zabAdapMetrics.
				getTriggerList( 
						testbedType,
						url,
						token,
						hostIdtoAdapter, 
						metricIdtoAdapter, 
						ZabbixMethods.TRIGGER.getzabbixMethod());

		for (ZabbixItemResponse triggerList : trigger4Host)	{  
			PaasThreshold thresholdtype = new PaasThreshold();
			String triggerExpression = triggerList.getExpression();
			triggerValue = triggerList.getValue();
			String triggerKey = triggerList.getKey();
			String triggerKeyDebug = serviceCategoryName + "." + hostName + "." + serviceName + "." + metricName + "." + triggerExpression;
			String triggerPriority = triggerList.getPriority();

			if(!(triggerValue.equals("1"))){
				thresholdtype.setThresholdStatus(PaasThresholdStatus.OK);
			}
			else thresholdtype.setThresholdStatus(PaasThresholdStatus.PROBLEM);

			thresholdtype.setThresholdExpression(triggerExpression);
			thresholdtype.setThresholdName(triggerList.getDescription());
			thresholds.add(thresholdtype);
		}
		return thresholds;
			}


	private String checkHostId(String hostName, String vmuuid) throws NamingException{

		List<ZabbixMonitoredHostsResponse> hostInfo = new ArrayList<>();
		Iterator<ZabbixMonitoredHostsResponse> hostIt = hostInfo.iterator();
		boolean hostFound=false; 
		while(!hostFound && hostIt.hasNext()){
			ZabbixMonitoredHostsResponse adapterResult = hostIt.next();
			hostName = adapterResult.getName();
			if(hostName.equalsIgnoreCase(vmuuid)){
				String hostIdtoAdapter = null;
				hostIdtoAdapter = adapterResult.getHostid();
				hostFound = true;
				return hostIdtoAdapter;
			}
			else throw new NamingException("Wrong resourse host name inserted");
		}
		throw new NamingException("Not Existing host name into  Monitoring Platform");
	}
}