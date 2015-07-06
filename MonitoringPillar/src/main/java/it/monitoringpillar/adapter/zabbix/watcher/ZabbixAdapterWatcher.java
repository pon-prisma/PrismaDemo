package it.monitoringpillar.adapter.zabbix.watcher;
//package it.prisma.monitoringpillar.adapter.zabbix.watcher;
//
//import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility;
//import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.zabbixMethods;
//import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.MonitoringVMCredentialsResponse;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.JSONRPCRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.JsonRpcDeleteHostRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.Macros;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixFilterRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamCreateHostRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamGraphRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamGroupIntoHostCreateRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamHostGroupRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamInterfaceIntoHostCreateRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamItemRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTriggerRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixSearchKeyRequest;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.Inventory;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.JSONRPCResponse;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
//import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponse;
//import it.prisma.monitoringpillar.adapter.zabbix.handler.HostGroupID;
//import it.prisma.monitoringpillar.adapter.zabbix.handler.TemplateIDV2;
//import it.prisma.monitoringpillar.adapter.zabbix.handler.ZabbixToken;
//import it.prisma.monitoringpillar.config.URL4Pillar;
//import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
//import it.prisma.utils.web.ws.rest.apiclients.zabbix.ZabbixAPIClient;
//import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
//import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
//import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
//import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.ArrayList;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//
//@Stateless
//public class ZabbixAdapterWatcher implements Serializable{
//
//	private static final long serialVersionUID = 1L;
//	private String jsonrpc;
//	private String method;
//	private int id;
//	private String output;
//	private String sortfield;
//	//	private ZabbixToken token;
//	private ZabbixParamItemRequest itemParameters;
//	private ZabbixSearchKeyRequest key;
//	private String groupID;
//	private String servicecheck;
//
////	private JSONRPCRequest<ZabbixParamCreateHostRequest> requestCreate; 
//	private JSONRPCRequest<ZabbixParamItemRequest> requestItem;
//	private JsonRpcDeleteHostRequest requestDelete;
//
//	private ArrayList<String> host2delete;
//	private ZabbixAPIClient zabClient;
////	private ZabbixParamGroupIntoHostCreateRequest grouprequest;
////	private ZabbixParamInterfaceIntoHostCreateRequest interfaceParameter;
////	private ZabbixParamCreateHostRequest hostCreationParameters;
//	private ArrayList<ZabbixItemResponse> resultItem;
//
//	private String tokenZabbix;
//	private final String ZABBIXPORTAGENT = "10050";
//
//	private MonitoringFeatureUtility feature;
//	private String watcherURL;
//
//	@EJB
//	private HostGroupID groupid;
//
//	@EJB
//	private TemplateIDV2 idv2;
//
//	@Inject
//	private ZabbixToken token;
//
//	@Inject
//	private URL4Pillar urlPillar;
//
//	@PostConstruct
//	public void initWatcher(String testbedType) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, RestClientException, Exception {
//		this.jsonrpc = "2.0";
//		this.method = "";
//		this.id = 1;
//		this.output = "extend";
//		this.sortfield = "name";
//		this.itemParameters = new ZabbixParamItemRequest();
//		this.key = new ZabbixSearchKeyRequest();
////		this.requestCreate = new JSONRPCRequest<ZabbixParamCreateHostRequest>();
//		this.requestDelete = new JsonRpcDeleteHostRequest();
//		this.host2delete = new ArrayList<String>();
//		this.requestItem = new JSONRPCRequest<ZabbixParamItemRequest>();
//		this.zabClient = new ZabbixAPIClient("");
//		this.feature = new MonitoringFeatureUtility();
//		this.resultItem = new ArrayList<>();
//
//		this.watcherURL = urlPillar.getURLfromPillar(testbedType, "prismawatcher");
//		this.tokenZabbix = token.getToken4PrismaMetrics(testbedType, watcherURL);
//		this.zabClient = new ZabbixAPIClient(
//				"http://"+ watcherURL +"/zabbix/api_jsonrpc.php");
//	}	
//
//	/**
//	 * Gets host's created ID in PrismaMetrics
//	 * @param testBedType
//	 * @param metricsURL
//	 * @param uuid
//	 * @param vmIP
//	 * @param serviceCategory
//	 * @param serviceTag
//	 * @param service
//	 * @param zabbixMethod
//	 * @return JsonRPCResponse<HostCreationParamRequest>
//	 * @throws APIErrorException
//	 * @throws Exception
//	 */
//	public 
//	JSONRPCResponse<MonitoringVMCredentialsResponse> 
////	MonitoringVMCredentialsResponse
//	getCreationHost(
//			String testBedType, 
//			String uuid, 
//			String vmIP, 
//			String serviceCategory,
//			String serviceTag,
//			ArrayList<String> service, 
//			String zabbixMethod
//			) throws Exception 	{
//
//		jsonrpc = "2.0";
//		id = 1;
//
//		JSONRPCRequest<ZabbixParamCreateHostRequest> requestCreate = new JSONRPCRequest<ZabbixParamCreateHostRequest>(); 
//		requestCreate.setJsonrpc(jsonrpc);
//		requestCreate.setMethod(zabbixMethod);
//
//		requestCreate.setAuth(tokenZabbix);
//		Macros macros = new Macros();
//		macros.setMacro("{$UUID}");
//		macros.setValue(uuid);
//		ZabbixParamCreateHostRequest hostCreationParameters = new ZabbixParamCreateHostRequest();
//		hostCreationParameters.setMacros(macros);
//
//		hostCreationParameters.setHost(uuid);
//
//		ZabbixParamInterfaceIntoHostCreateRequest interfaceParameter = new ZabbixParamInterfaceIntoHostCreateRequest();
//		interfaceParameter.setMain(1);
//		interfaceParameter.setIp(vmIP);
//		interfaceParameter.setDns("");
//		interfaceParameter.setType(1);
//		interfaceParameter.setPort(ZABBIXPORTAGENT);
//		interfaceParameter.setUseip(1);
//
//		ArrayList<ZabbixParamInterfaceIntoHostCreateRequest> interfaceArray = new ArrayList<ZabbixParamInterfaceIntoHostCreateRequest>();
//		interfaceArray.add(interfaceParameter);
//
//		for(int i=0; i<service.size();i++)
//		{
//			servicecheck = service.get(i);
//
//			groupID = groupid.getHostGroupIDsintoPrismaMetrics(
//					testBedType, 
//					serviceCategory);
//		}
//		ZabbixParamGroupIntoHostCreateRequest grouprequest = new ZabbixParamGroupIntoHostCreateRequest();
//		grouprequest.setGroupid(groupID);
//		ArrayList<ZabbixParamGroupIntoHostCreateRequest> groupParamArray = new ArrayList<ZabbixParamGroupIntoHostCreateRequest>();
//		groupParamArray.add(grouprequest);
//
//		hostCreationParameters.setGroups(groupParamArray);
//		hostCreationParameters.setInterfaces(interfaceArray);
//
//		hostCreationParameters.setTemplates(
//				idv2.getTemplateID(
//						testBedType, 
//						service
//						));
//
//		Inventory inventory = new Inventory();
//		inventory.setTag(serviceTag);
//		hostCreationParameters.setInventory(inventory);
//
//		requestCreate.setParams(hostCreationParameters);
//		requestCreate.setId(id);
//
//		return (zabClient).getHostCreation(requestCreate);
//	}
//
//	/**
//	 * 
//	 * @param testBedType
//	 * @param hostid
//	 * @param zabbixMethod
//	 * @return the host's ID
//	 * @throws MappingException
//	 * @throws NoMappingModelFoundException
//	 * @throws ServerErrorResponseException
//	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
//	 * @throws RestClientException
//	 * @throws Exception
//	 */
//	public JSONRPCResponse<MonitoringVMCredentialsResponse> getDestructionHost  (
//			String testBedType, 
//			String hostid,
//			String zabbixMethod) 
//					throws MappingException, 
//					NoMappingModelFoundException, 
//					ServerErrorResponseException, 
//					it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
//					RestClientException, Exception
//					{
//
//		requestDelete.setJsonrpc(jsonrpc);
//		requestDelete.setMethod(zabbixMethod);
//		host2delete.add(hostid);
//		requestDelete.setParams(host2delete);
//
//		requestDelete.setAuth(tokenZabbix);
//		requestDelete.setId(id);
//
//		JSONRPCResponse<MonitoringVMCredentialsResponse>  result = zabClient.getHostDestructionClient(requestDelete);
//		return result;
//					}
//
//	/**
//	 * Get Monitored Host in Prisma Metrics useful for returning the host id to the 
//	 * orchestrator when getting metrics for building graphs e for billing as well
//	 * @param testBedType
//	 * @param zabbixMethod
//	 * @param groupids
//	 * @return host List from Zabbix API
//	 * @throws MappingException
//	 * @throws NoMappingModelFoundException
//	 * @throws ServerErrorResponseException
//	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
//	 * @throws RestClientException
//	 * @throws Exception
//	 */
//	public ArrayList<ZabbixMonitoredHostsResponse> getMonitoredHostListinPrismaMetrics(
//			String testBedType, 
//			String zabbixMethod, 
//			String groupids
//			) throws MappingException, 
//			NoMappingModelFoundException, 
//			ServerErrorResponseException, 
//			it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
//			RestClientException, Exception {
//
//		jsonrpc = "2.0";
//		method = zabbixMethod;
//		output = "extend";
//		id = 1;
//
//		JSONRPCRequest<ZabbixParamRequest> request = new JSONRPCRequest<ZabbixParamRequest>();
//		request.setJsonrpc(jsonrpc);
//		request.setMethod(method);
//		request.setAuth(tokenZabbix);
//
//		ZabbixParamRequest parametersHosts = new ZabbixParamRequest();
//		parametersHosts.setOutput(output);
//
//		if(!(groupids==null)){
//			parametersHosts.setGroupids(groupids);
//		}
//
//		ZabbixFilterRequest filter = new ZabbixFilterRequest();
//		filter.setHost("");
//		parametersHosts.setFilter(filter);
//		parametersHosts.setSelectInventory("extend");
//		request.setParams(parametersHosts);
//		request.setId(id);
//
//		return zabClient.getHosts4Adapter(request);
//	}
//
//
//	/**
//	 * Get HostGroup List
//	 * @param testBedType
//	 * @param zabbixMethod
//	 * @return list of groups from Zabbix API
//	 * @throws MappingException
//	 * @throws NoMappingModelFoundException
//	 * @throws ServerErrorResponseException
//	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
//	 * @throws RestClientException
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 * @throws Exception
//	 */
//	public  ArrayList<ZabbixHostGroupResponse> getHostGroupListinMetrics(
//			String testBedType, 
//			//			String metricsURL, 
//			String zabbixMethod) 
//					throws MappingException, 
//					NoMappingModelFoundException, 
//					ServerErrorResponseException, 
//					it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
//					RestClientException, JsonParseException, JsonMappingException, IOException  {
//
//		jsonrpc = "2.0";
//		method = zabbixMethod;
//		output = "extend";
//		id = 1;
//
//		JSONRPCRequest<ZabbixParamHostGroupRequest> request = new JSONRPCRequest<ZabbixParamHostGroupRequest>();
//		request.setJsonrpc(jsonrpc);
//		request.setMethod(method);
//
//		request.setAuth(tokenZabbix);
//
//		ZabbixParamHostGroupRequest parametersHosts = new ZabbixParamHostGroupRequest();
//		parametersHosts.setOutput(output);
//
//		ZabbixFilterRequest filter = new ZabbixFilterRequest();
//
//		parametersHosts.setFilter(filter);
//		request.setParams(parametersHosts);
//		request.setId(id);
//
//		return zabClient.getHostGroup(request);
//	}
//
//
//	/**
//	 * Get Template List into PrismaMetrics Server
//	 * @param testBedType
//	 * @param zabbixMethod
//	 * @return template List from Zabbix API
//	 * @throws MappingException
//	 * @throws NoMappingModelFoundException
//	 * @throws ServerErrorResponseException
//	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
//	 * @throws RestClientException
//	 * @throws Exception
//	 */
//	public ArrayList<ZabbixTemplateResponse> getTemplateIdListMetrics(
//			String testBedType,  
//			String zabbixMethod) throws MappingException, 
//			NoMappingModelFoundException, 
//			ServerErrorResponseException, 
//			it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
//			RestClientException, Exception  {
//
//		jsonrpc = "2.0";
//		method = zabbixMethod;
//		output = "extend";
//		id = 1;
//
//		JSONRPCRequest<ZabbixParamHostGroupRequest> request = new JSONRPCRequest<ZabbixParamHostGroupRequest>();
//		request.setJsonrpc(jsonrpc);
//		request.setMethod(method);
//
//		request.setAuth(tokenZabbix);
//
//		ZabbixParamHostGroupRequest parametersHosts = new ZabbixParamHostGroupRequest();
//		parametersHosts.setOutput(output);
//
//		ZabbixFilterRequest filter = new ZabbixFilterRequest();
//		parametersHosts.setFilter(filter);
//		request.setParams(parametersHosts);
//		request.setId(id);
//
//		return(zabClient).getTemplate(request);
//	}
//
//	/**
//	 * Get Zabbix Features
//	 * @param testbedType
//	 * @param hostId
//	 * @param templateIdtoAdapter
//	 * @param zabbixMethod
//	 * @return Zabbix Items API
//	 * @throws MappingException
//	 * @throws NoMappingModelFoundException
//	 * @throws ServerErrorResponseException
//	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
//	 * @throws RestClientException
//	 * @throws Exception
//	 */
//	public  ArrayList<ZabbixItemResponse> getZabbixFeature(
//			String testbedType, 
//			String hostId,
//			String templateIdtoAdapter,
//			String zabbixMethod) 
//					throws MappingException, 
//					NoMappingModelFoundException, 
//					ServerErrorResponseException, 
//					it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
//					RestClientException, Exception  {
//
//		jsonrpc = "2.0";
//		method = zabbixMethod;
//		output = "extend";
//		id = 1;
//
//		requestItem.setJsonrpc(jsonrpc);
//		requestItem.setMethod(method);
//
//		requestItem.setAuth(tokenZabbix);
//		requestItem.setAuth(tokenZabbix);
//
//		ZabbixParamItemRequest params = new ZabbixParamItemRequest();
//		params.setOutput(output);
//		params.setHostids(hostId);
//
//		if (!(templateIdtoAdapter==null))
//		{
//			params.setTemplateids(templateIdtoAdapter);
//			params.setHostids(null);
//		}
//		key.setKey("");
//		params.setSearch(key);
//		requestItem.setParams(params);
//		requestItem.setId(id);
//
//		requestItem.setParams(params);
//
//		return zabClient.getItemNOZabbixWrappingResponse(requestItem);		
//	}
//
//	/**
//	 * Get List of Metrics for Specific host
//	 * @param testbedType
//	 * @param hostId
//	 * @param templateIds
//	 * @param metricIds
//	 * @param zabbixMethod
//	 * @return metrics List in Zabbix
//	 * @throws MappingException
//	 * @throws NoMappingModelFoundException
//	 * @throws ServerErrorResponseException
//	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
//	 * @throws RestClientException
//	 * @throws Exception
//	 */
//	public ArrayList<ZabbixItemResponse> getMetricsList(
//			String testbedType, 
//			String hostId, 
//			String templateIds,
//			String metricIds,
//			String zabbixMethod 
//				
//			) throws MappingException, 
//			NoMappingModelFoundException, 
//			ServerErrorResponseException, 
//			it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
//			RestClientException, Exception
//			{
//
//		jsonrpc = "2.0";
//		method = zabbixMethod;
//		output = "extend";
//		id = 1;
//
//		requestItem.setJsonrpc(jsonrpc);
//		requestItem.setMethod(method);
//
//		requestItem.setAuth(tokenZabbix);
//
//		itemParameters = new ZabbixParamItemRequest();
//		itemParameters.setOutput(output);
//
//		if(zabbixMethod.equals(zabbixMethods.HISTORY.getzabbixMethod()))
//		{
//			String sortfield = "clock";
//			int limit = 170;
//			int history = 0;
//			long epochCurrentTime = System.currentTimeMillis()/1000;
//			long epochOneWeekAgo = epochCurrentTime - 604800;
//			String time_from =  String.valueOf(epochOneWeekAgo);
//			String time_till = String.valueOf(epochCurrentTime);
//			String sortorder = "ASC"; //"DESC"
//			int id = 1;
//
//			JSONRPCRequest<ZabbixParamGraphRequest> request = new JSONRPCRequest<ZabbixParamGraphRequest>();
//			request.setJsonrpc(jsonrpc);
//			request.setMethod(method);
//			request.setAuth(tokenZabbix);
//
//			ZabbixParamGraphRequest parameters4history = new ZabbixParamGraphRequest();
//			parameters4history.setOutput(output);
//			parameters4history.setHistory(history);
//			parameters4history.setLimit(limit);
//			parameters4history.setSortfield(sortfield);
//			parameters4history.setSortorder(sortorder);
//			parameters4history.setItemids(metricIds);
//			parameters4history.setHostids(hostId);
//			request.setParams(parameters4history);
//			request.setId(id);
//		
//				return zabClient.getGraph(request);
//			
//		}
//		else {
//			
//			itemParameters.setHostids(hostId);
//
//			if(!(templateIds==null) && !(hostId==null)){
//				itemParameters.setTemplateids(null);		
//			}
//
//			else if(!(templateIds==null)){
//				itemParameters.setTemplateids(templateIds);
//				itemParameters.setHostids(null);
//			}
//			else if(!(metricIds==null))
//			{
//				itemParameters.setItemids(metricIds);
//				itemParameters.setTemplateids(null);
//				itemParameters.setHostids(null);
//			}
//
//			//	itemParameters.setTemplateids(templateIds);
//			key = new ZabbixSearchKeyRequest();
//			key.setKey("");
//
//			itemParameters.setSearch(key);
//
//			requestItem.setParams(itemParameters);
//			requestItem.setId(id);
//
//			resultItem = zabClient.getItemNOZabbixWrappingResponse(requestItem);
//			return resultItem;
//		}
//			}
//
//	/**
//	 *TriggerList for wrapper  
//	 * @param createdHostId
//	 * @param itemId
//	 * @param zabbixMethod
//	 * @return trigger list from zabbix API 
//	 * @throws MappingException
//	 * @throws NoMappingModelFoundException
//	 * @throws ServerErrorResponseException
//	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
//	 * @throws RestClientException
//	 * @throws Exception
//	 */
//	public  ArrayList<ZabbixItemResponse> getTriggerList(
//			String createdHostId, 
//			String itemId,
//			String zabbixMethod) throws MappingException, 
//			NoMappingModelFoundException, 
//			ServerErrorResponseException, 
//			it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
//			RestClientException, Exception {
//
//		jsonrpc = "2.0";
//		method = zabbixMethod;
//		output = "extend";
//		id = 1;
//
//		JSONRPCRequest<ZabbixParamTriggerRequest> request = new JSONRPCRequest<ZabbixParamTriggerRequest>();
//		request.setJsonrpc(jsonrpc);
//		request.setMethod(method);
//
//		request.setAuth(tokenZabbix);
//
//		ZabbixParamTriggerRequest itemParameters = new ZabbixParamTriggerRequest();
//		itemParameters.setOutput(output);
//		if(!(itemId==null))
//		{
//			ArrayList<String> itemIdsarray = new ArrayList<>();
//			itemIdsarray.add(itemId);
//			itemParameters.setItemids(itemIdsarray);
//			createdHostId=null;
//		}
//		itemParameters.setHostids(createdHostId);
//		request.setParams(itemParameters);
//		request.setId(id);
//
//		resultItem = zabClient.getTrigger4Adapter(request);
//		return resultItem;
//	}
//}
