package it.monitoringpillar.adapter.zabbix.metrics;

import it.monitoringpillar.adapter.zabbix.handler.GroupIDByName;
import it.monitoringpillar.adapter.zabbix.handler.TemplateIDByName;
import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ServerType;
import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.monitoringpillar.config.PillarEnvironmentConfig;
import it.monitoringpillar.config.TimestampMonitoring;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.MonitoringVMCredentialsResponse;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MetricsHistoryTimeRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.HostGroupParamRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.HostIDMassUpdate;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.JSONRPCRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.JsonRpcDeleteHostRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.Macros;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.SearchInventory;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.Zabbix2_4ParamHost;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixFilterRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamCreateHostRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamGraphRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamGroupIntoHostCreateRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamHostGroupRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamInterfaceIntoHostCreateRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamItemRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTemplateRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTriggerRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamUpdate;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixSearchKeyRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.HostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.Inventory;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.JSONRPCResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV2_4;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponseV2_4;
import it.prisma.utils.json.JsonUtility;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.ZabbixAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.ws.rs.NotFoundException;

import org.apache.velocity.exception.ResourceNotFoundException;

import com.fasterxml.jackson.databind.JsonMappingException;

@Stateless
public class ZabbixAdapterSetter<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	private String jsonrpc;
	private String method;
	private int id;
	private String output;
	private String sortfield;
	private ZabbixParamItemRequest itemParameters;
	private ZabbixSearchKeyRequest key;
	private String watcherURL;
	private JSONRPCRequest<ZabbixParamItemRequest> requestItem;
	private JsonRpcDeleteHostRequest requestDelete;
	private static final String EXTEND = "extend";


	private JSONRPCRequest<ZabbixParamUpdate> requestUpdate;

	private ZabbixAPIClient zabClient;
	private ArrayList<ZabbixItemResponse> resultItem;

	private final String ZABBIXPORTAGENT = "10050";

	private MonitoringFeatureUtility feature;

	private final int TEXTHistoryType = 4;
	private final int INTHistoryType = 3;
	private final int FLOATHistoryType = 0;
	private final static String ENABLE_HOST_METRICS = "enable";
	private final static String DISABLE_HOST_METRIC = "disable";
	private ZabbixParamUpdate paramUpdate; 

	@EJB
	private GroupIDByName groupid;

	@EJB
	private TemplateIDByName idv2;

	@Inject
	private PillarEnvironmentConfig envConf;

	@Inject
	private TimestampMonitoring timeConv;

	public ZabbixAdapterSetter()//String testbed, String monitoringType) 
			throws MappingException, 
			NoMappingModelFoundException, 
			ServerErrorResponseException, 
			APIErrorException, 
			RestClientException, 
			ResourceNotFoundException,
			Exception {
		this.jsonrpc = "2.0";
		this.method = "";
		this.id = 1;
		this.output = "extend";
		this.sortfield = "name";
		this.itemParameters = new ZabbixParamItemRequest();
		this.paramUpdate = new ZabbixParamUpdate();
		this.key = new ZabbixSearchKeyRequest();
		this.requestDelete = new JsonRpcDeleteHostRequest();
		this.requestUpdate = new JSONRPCRequest<ZabbixParamUpdate>();
		this.requestItem = new JSONRPCRequest<ZabbixParamItemRequest>();
		this.feature = new MonitoringFeatureUtility();
		this.resultItem = new ArrayList<>();

	}	

	public enum updateHostMetric{
		ENABLE(0),DISABLE(1);

		private updateHostMetric(int updateHostMetric){
			this.updateHostMetric = updateHostMetric;
		}
		private int updateHostMetric;
		public int getupdateHostMetric(){
			return this.updateHostMetric;
		}
	}

	/************************
	 * CREATE HOST IN ZABBIX 
	 ************************/

	/**
	 * Gets host's created ID in PrismaMetrics
	 * @param testBedType
	 * @param metricsURL
	 * @param uuid
	 * @param vmIP
	 * @param serviceCategory
	 * @param serviceTag
	 * @param service
	 * @param zabbixMethod
	 * @return JsonRPCResponse<HostCreationParamRequest>
	 * @throws APIErrorException
	 * @throws Exception
	 */
	public JSONRPCResponse<MonitoringVMCredentialsResponse> getCreationHost(
			String testBedType,
			String url,
			String token,
			String servertype,
			String hostGroup, 
			String uuid, 
			String vmIP, 
			String serviceCategory,
			String serviceTag,
			List<String> service, 
			String zabbixMethod
			) throws NotFoundException, Exception 	{

		this.zabClient = new ZabbixAPIClient(url );

		jsonrpc = "2.0";
		id = 1;

		JSONRPCRequest<ZabbixParamCreateHostRequest> requestCreate = new JSONRPCRequest<ZabbixParamCreateHostRequest>(); 
		requestCreate.setJsonrpc(jsonrpc);
		requestCreate.setMethod(zabbixMethod);

		requestCreate.setAuth(token);

		Macros macros = new Macros();
		macros.setMacro("{$UUID}");
		macros.setValue(uuid);
		ZabbixParamCreateHostRequest hostCreationParameters = new ZabbixParamCreateHostRequest();
		hostCreationParameters.setMacros(macros);

		hostCreationParameters.setHost(uuid);

		ZabbixParamInterfaceIntoHostCreateRequest interfaceParameter = new ZabbixParamInterfaceIntoHostCreateRequest();
		interfaceParameter.setMain(1);
		interfaceParameter.setIp(vmIP);
		interfaceParameter.setDns("");
		interfaceParameter.setType(1);
		interfaceParameter.setPort(ZABBIXPORTAGENT);
		interfaceParameter.setUseip(1);

		ArrayList<ZabbixParamInterfaceIntoHostCreateRequest> interfaceArray = new ArrayList<ZabbixParamInterfaceIntoHostCreateRequest>();
		interfaceArray.add(interfaceParameter);
		String groupID = null;

		if (service==null){
			String ceilometer ="";
			service = new ArrayList<>();
			service.add(ceilometer);
		}

		for(int i=0; i<service.size();i++){
			String servicecheck = service.get(i);

			groupID = groupid.getGroupIDsintoZabbix(testBedType, url, token, hostGroup, null);
		}
		ZabbixParamGroupIntoHostCreateRequest grouprequest = new ZabbixParamGroupIntoHostCreateRequest();
		grouprequest.setGroupid(groupID);
		ArrayList<ZabbixParamGroupIntoHostCreateRequest> groupParamArray = new ArrayList<ZabbixParamGroupIntoHostCreateRequest>();
		groupParamArray.add(grouprequest);

		hostCreationParameters.setGroups(groupParamArray);
		hostCreationParameters.setInterfaces(interfaceArray);

		if(servertype.equals(ServerType.SERVERMETRICS.getServerType()))
			hostCreationParameters.setTemplates(
					idv2.getTemplateID(
							testBedType,
							url,
							token,
							ServerType.SERVERMETRICS.getServerType(),
							service
							));
		else if(servertype.equals(ServerType.SERVERWATCHER.getServerType()))
			hostCreationParameters.setTemplates(
					idv2.getTemplateID(
							testBedType,
							url,
							token,
							ServerType.SERVERWATCHER.getServerType(),
							service
							));
		else throw new ResourceNotFoundException("Wrong server inserted or it doesn't exist");


		Inventory inventory = new Inventory();
		inventory.setTag(serviceTag);
		inventory.setType(serviceCategory);
		hostCreationParameters.setInventory(inventory);

		requestCreate.setParams(hostCreationParameters);
		requestCreate.setId(id);

		return (zabClient).getHostCreationClient(requestCreate, url);
	}

	/***********************
	 * DELETE HOST IN ZABBIX 
	 ***********************/

	/**
	 * It removes the monitored host
	 * @param testBedType
	 * @param hostid
	 * @param zabbixMethod
	 * @return the host's ID
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public JSONRPCResponse<MonitoringVMCredentialsResponse> getDestructionHost  (
			String testBedType, 
			String url,
			String token,
			String hostid,
			String zabbixMethod
			) 
					throws MappingException, 
					NoMappingModelFoundException, 
					ServerErrorResponseException, 
					it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
					RestClientException, Exception
					{

		this.zabClient = new ZabbixAPIClient(url);
		requestDelete.setJsonrpc(jsonrpc);
		requestDelete.setMethod(zabbixMethod);
		ArrayList<String> host2delete = new ArrayList<>();
		host2delete.add(hostid);
		requestDelete.setParams(host2delete);

		requestDelete.setAuth(token);

		requestDelete.setId(id);

		return zabClient.getHostDeleteClient(requestDelete, url);
					}

	/*******************
	 * DELETE HOSTGROUP
	 ******************/
	public JSONRPCResponse<HostGroupResponse> getDeleteHostGroup  (
			String testBedType, 
			String url,
			String token,
			String hostGroupName,
			String zabbixMethod
			) 
					throws MappingException, 
					NoMappingModelFoundException, 
					ServerErrorResponseException, 
					it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
					RestClientException, Exception
					{

		this.zabClient = new ZabbixAPIClient(url);
		requestDelete.setJsonrpc(jsonrpc);
		requestDelete.setMethod(zabbixMethod);
		ArrayList<String> host2delete = new ArrayList<>();
		host2delete.add(hostGroupName);
		requestDelete.setParams(host2delete);

		requestDelete.setAuth(token);

		requestDelete.setId(id);

		return zabClient.getHostGroupDeleteClient(requestDelete, url);
					}

	/********************
	 * CREATE HOSTGROUP 
	 ********************/
	public JSONRPCResponse<HostGroupResponse> getHostGroupCreation  (
			String testBedType, 
			String url,
			String token,
			String hostGroupName,
			String zabbixMethod
			) 
					throws MappingException, 
					NoMappingModelFoundException, 
					ServerErrorResponseException, 
					it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
					RestClientException, Exception
					{

		this.zabClient = new ZabbixAPIClient(url);
		JSONRPCRequest<HostGroupParamRequest> request = new JSONRPCRequest<>();
		HostGroupParamRequest hostGroupParamRequest = new HostGroupParamRequest();
		request.setJsonrpc(jsonrpc);
		request.setMethod(zabbixMethod);	

		hostGroupParamRequest.setName(hostGroupName);
		request.setParams(hostGroupParamRequest);
		request.setAuth(token);
		request.setId(id);
		return zabClient.getHostGroupCreationClient(request, url);
					}


	/*******************************
	 * UPDATE HOST/METRIC IN ZABBIX
	 *******************************/

	/**
	 * Method useful for enabling/disabling the host
	 * @param testBedType
	 * @param hostid
	 * @param itemid
	 * @param zabbixMethod
	 * @param urlRuntime
	 * @return The array of disabled metrics or hosts 
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONRPCResponse<T> getUpdateHostItem  (
			String testBedType,
			String url,
			String token,
			String hostid,
			String itemid,
			String zabbixMethod,
			String update
			) 
					throws MappingException, 
					NoMappingModelFoundException, 
					ServerErrorResponseException, 
					it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
					RestClientException, NamingException, Exception
					{
		this.zabClient = new ZabbixAPIClient(url );

		HostIDMassUpdate host2update = new HostIDMassUpdate();
		requestUpdate.setJsonrpc(jsonrpc);
		requestUpdate.setMethod(zabbixMethod);

		if(zabbixMethod.equals(ZabbixMethods.MASSUPDATE.getzabbixMethod())){
			ArrayList<HostIDMassUpdate> hosts2update = new ArrayList<>();

			host2update.setHostid(hostid);
			hosts2update.add(host2update);
			paramUpdate.setHosts(hosts2update);
			setUpdate(update);
		}
		else {
			paramUpdate.setItemid(itemid);
			setUpdate(update);
		}

		requestUpdate.setAuth(token);
		requestUpdate.setId(id);
		requestUpdate.setParams(paramUpdate);

		return (JSONRPCResponse<T>) zabClient.getUpdateHostItemClient(requestUpdate, url);
					}

	/**
	 * It evaluates whether to disable or enable the host/metric 
	 * depending on which parameter has been passed on
	 * @param update
	 * @throws NamingException
	 */
	private void setUpdate(String update) throws NamingException{
		if(update.equals(ENABLE_HOST_METRICS)){ 
			paramUpdate.setStatus(updateHostMetric.ENABLE.getupdateHostMetric());
		}
		else if (update.equals(DISABLE_HOST_METRIC)){
			paramUpdate.setStatus(updateHostMetric.DISABLE.getupdateHostMetric());
		}
		else throw new NamingException("Wrong ENABLE or DISABLE inserted command");
	}


	/**
	 * Get Monitored HOSTS LIST in Zabbix Metrics useful for returning the host id to the 
	 * orchestrator when getting metrics for building graphs e for billing as well
	 * 
	 * @param testBedType
	 * @param zabbixMethod
	 * @param groupids
	 * @return host List from Zabbix API
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public ArrayList<ZabbixMonitoredHostsResponse> getMonitoredHosts(
			String testBedType, 
			String url,
			String token,
			String zabbixMethod, 
			String hostids,
			String groupids, 
			String filterByHostName
			) throws MappingException, 
			NoMappingModelFoundException, 
			ServerErrorResponseException, 
			it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
			RestClientException, Exception {
		this.zabClient = new ZabbixAPIClient(url );
		jsonrpc = "2.0";
		method = zabbixMethod;
		output = "extend";
		id = 1;

		JSONRPCRequest<ZabbixParamRequest> request = new JSONRPCRequest<ZabbixParamRequest>();
		request.setJsonrpc(jsonrpc);
		request.setMethod(method);

		request.setAuth(token);

		ZabbixParamRequest parametersHosts = new ZabbixParamRequest();
		parametersHosts.setOutput(output);

		if(!(groupids==null)){
			parametersHosts.setGroupids(groupids);
		}

		if(!(hostids==null)){
			parametersHosts.setHostids(hostids);
		}
		ZabbixFilterRequest filter = new ZabbixFilterRequest();
		if(filterByHostName!=null){
			filter.setHost(filterByHostName);
		}
		parametersHosts.setFilter(filter);
		parametersHosts.setSelectInventory("extend");
		request.setParams(parametersHosts);
		request.setId(id);

		return zabClient.getHosts4Adapter(request, url);
	}
	
	
	//TODO: to Delete once the new wrapper works
	/**
	 * 
	 * @param testBedType
	 * @param url
	 * @param token
	 * @param zabbixMethod
	 * @param hostids
	 * @param groupids
	 * @param filterByHostName
	 * @param tagId
	 * @return
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public ArrayList<ZabbixMonitoredHostsResponse> getMonitoredHosts2_4Temporay(
			String testBedType, 
			String url,
			String token,
			String zabbixMethod, 
			String hostids,
			String groupids, 
			String filterByHostName, 
			String tagId
			) throws MappingException, 
			NoMappingModelFoundException, 
			ServerErrorResponseException, 
			it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
			RestClientException, Exception {
		this.zabClient = new ZabbixAPIClient(url );
		jsonrpc = "2.0";
		method = zabbixMethod;
		output = EXTEND;
		id = 1;

		JSONRPCRequest<Zabbix2_4ParamHost> request = new JSONRPCRequest<Zabbix2_4ParamHost>();
		SearchInventory searchInventory = new SearchInventory();

		request.setJsonrpc(jsonrpc);
		request.setMethod(method);

		request.setAuth(token);

		Zabbix2_4ParamHost parametersHosts = new Zabbix2_4ParamHost();
		parametersHosts.setOutput(output);

		if(!(groupids==null)){
			parametersHosts.setGroupids(groupids);
		}

		if(!(hostids==null)){
			parametersHosts.setHostids(hostids);
		}
		ZabbixFilterRequest filter = new ZabbixFilterRequest();
		if(filterByHostName!=null){
			filter.setHost(filterByHostName);
		}
		searchInventory.setTag(tagId);
		parametersHosts.setSearchInventory(searchInventory);
		parametersHosts.setSearchWildcardsEnabled(false);
		parametersHosts.setFilter(filter);
		parametersHosts.setSelectInventory(EXTEND);
		parametersHosts.setSelectItems(null);
		parametersHosts.setSelectParentTemplates(null);
		request.setParams(parametersHosts);
		request.setId(id);

		return zabClient.getHosts2_4Temporary(request, url);
	}

	/***********************************************************
	 * Get Monitored HOSTS LIST in Zabbix 2.4 Metrics/Watcher 
	 * 
	 * @param testBedType
	 * @param zabbixMethod
	 * @param groupids
	 * @return host List from Zabbix API
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public ArrayList<ZabbixMonitoredHostResponseV2_4> getMonitoredHostsZABBIXV2_4(
			String testBedType, 
			String url,
			String token,
			String zabbixMethod, 
			String hostids,
			String groupids, 
			String filterByHostName, 
			String tagId
			) throws MappingException, 
			NoMappingModelFoundException, 
			ServerErrorResponseException, 
			it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
			RestClientException, Exception {
		this.zabClient = new ZabbixAPIClient(url );
		jsonrpc = "2.0";
		method = zabbixMethod;
		output = EXTEND;
		id = 1;

		JSONRPCRequest<Zabbix2_4ParamHost> request = new JSONRPCRequest<Zabbix2_4ParamHost>();
		SearchInventory searchInventory = new SearchInventory();

		request.setJsonrpc(jsonrpc);
		request.setMethod(method);

		request.setAuth(token);

		Zabbix2_4ParamHost parametersHosts = new Zabbix2_4ParamHost();
		parametersHosts.setOutput(output);

		if(!(groupids==null)){
			parametersHosts.setGroupids(groupids);
		}

		if(!(hostids==null)){
			parametersHosts.setHostids(hostids);
		}
		ZabbixFilterRequest filter = new ZabbixFilterRequest();
		if(filterByHostName!=null){
			filter.setHost(filterByHostName);
		}
		searchInventory.setTag(tagId);
		parametersHosts.setSearchInventory(searchInventory);
		parametersHosts.setSearchWildcardsEnabled(false);
		parametersHosts.setFilter(filter);
		parametersHosts.setSelectInventory(EXTEND);
		parametersHosts.setSelectItems(EXTEND);
		parametersHosts.setSelectParentTemplates(EXTEND);
		request.setParams(parametersHosts);
		request.setId(id);

		return zabClient.getHostsFromZabbix2_4(request, url);
	}


	/*********************
	 * Get GROUPS LIST
	 * @param testBedType
	 * @param zabbixMethod
	 * @return list of groups from Zabbix API
	 * @throws Exception
	 */
	public  ArrayList<ZabbixHostGroupResponse> getHostGroupListinMetrics(
			String testBedType, 
			String url,
			String token,
			String zabbixMethod, 
			String hostGroupIdtoAdapter,
			String tagService, 
			String hostid
			) throws Exception  {

		this.zabClient = new ZabbixAPIClient(url );
		jsonrpc = "2.0";
		method = zabbixMethod;
		output = "extend";
		id = 1;

		JSONRPCRequest<ZabbixParamHostGroupRequest> request = new JSONRPCRequest<ZabbixParamHostGroupRequest>();
		request.setJsonrpc(jsonrpc);
		request.setMethod(method);

		request.setAuth(token);

		ZabbixParamHostGroupRequest parametersHosts = new ZabbixParamHostGroupRequest();
		parametersHosts.setOutput(output);

		if(hostid!=null){
			parametersHosts.setHostids(hostid);
		}
		ZabbixFilterRequest filter = new ZabbixFilterRequest();

		parametersHosts.setFilter(filter);
		request.setParams(parametersHosts);
		request.setId(id);

		return zabClient.getHostGroup(request, url);
	}


	/********************************************
	 * Get TEMPLATE LIST into PrismaMetrics Server
	 * @param testBedType
	 * @param zabbixMethod
	 * @return template List from Zabbix API
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public ArrayList<ZabbixTemplateResponse> getTemplateIdListMetrics(
			String testBedType, 
			String url,
			String token,
			String zabbixMethod
			) throws MappingException, 
			NoMappingModelFoundException, 
			ServerErrorResponseException, 
			it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
			RestClientException, Exception  {

		this.zabClient = new ZabbixAPIClient( url );
		jsonrpc = "2.0";
		method = zabbixMethod;
		output = "extend";
		id = 1;

		JSONRPCRequest<ZabbixParamHostGroupRequest> request = new JSONRPCRequest<ZabbixParamHostGroupRequest>();
		request.setJsonrpc(jsonrpc);
		request.setMethod(method);

		request.setAuth(token);

		ZabbixParamHostGroupRequest parametersHosts = new ZabbixParamHostGroupRequest();
		parametersHosts.setOutput(output);

		ZabbixFilterRequest filter = new ZabbixFilterRequest();
		parametersHosts.setFilter(filter);
		request.setParams(parametersHosts);
		request.setId(id);

		return(zabClient).getTemplate(request);
	}

	/******************************************************************************
	 * Get Zabbix Features USEFUL FOR MULTIPLE PURPOSES depending on method passed
	 * @param testbedType
	 * @param hostId
	 * @param templateIdtoAdapter
	 * @param zabbixMethod
	 * @return Zabbix Items API
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public  ArrayList<ZabbixItemResponse> getZabbixFeature(
			String testbedType, 
			String url,
			String token,
			String hostId,
			String templateIdtoAdapter,
			String zabbixMethod
			) 
					throws MappingException, 
					NoMappingModelFoundException, 
					ServerErrorResponseException, 
					it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
					RestClientException, Exception  {

		this.zabClient = new ZabbixAPIClient( url );

		jsonrpc = "2.0";
		method = zabbixMethod;
		output = "extend";
		id = 1;

		requestItem.setJsonrpc(jsonrpc);
		requestItem.setMethod(method);

		requestItem.setAuth(token);

		ZabbixParamItemRequest params = new ZabbixParamItemRequest();
		params.setOutput(output);
		params.setHostids(hostId);

		if (!(templateIdtoAdapter==null)){
			params.setTemplateids(templateIdtoAdapter);
			params.setHostids(null);
		}
		key.setKey("");
		params.setSearch(key);
		requestItem.setParams(params);
		requestItem.setId(id);

		requestItem.setParams(params);

		return zabClient.getItemNOZabbixWrappingResponse(requestItem, url);		
	}

	/*************************************
	 * GET TEMPLATES AND ASSOCIATED ITEMS
	 ************************************/
	public ArrayList<ZabbixTemplateResponseV2_4> getTemplates(String testbedType, 
			String url,
			String token,
			String hostId,
			String zabbixMethod)

		throws MappingException, 
		NoMappingModelFoundException, 
		ServerErrorResponseException, 
		it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
		RestClientException, Exception  {

			this.zabClient = new ZabbixAPIClient( url );

			jsonrpc = "2.0";
			method = zabbixMethod;
			output = "extend";
			id = 1;

			JSONRPCRequest<ZabbixParamTemplateRequest> requestTemplate = new JSONRPCRequest<>();
			ZabbixParamTemplateRequest paramTemplate = new ZabbixParamTemplateRequest();
			
			requestTemplate.setJsonrpc(jsonrpc);
			requestTemplate.setMethod(method);

			requestTemplate.setAuth(token);

			paramTemplate.setOutput(output);
			paramTemplate.setHostids(hostId);

			paramTemplate.setSelectItems("extend");
			requestTemplate.setParams(paramTemplate);
			requestTemplate.setId(id);
			
			return zabClient.getTemplateByItems(requestTemplate, url);	
			
	}

		/*****************************************************
		 * Get LIST of METRICS for Specific host
		 * @param testbedType
		 * @param hostId
		 * @param templateIds
		 * @param metricIds
		 * @param zabbixMethod
		 * @return metrics List in Zabbix
		 * @throws MappingException
		 * @throws NoMappingModelFoundException
		 * @throws ServerErrorResponseException
		 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
		 * @throws RestClientException
		 * @throws Exception
		 */
		public ArrayList<ZabbixItemResponse> getMetricsList(
				String testbedType, 
				String url,
				String token,
				String hostId, 
				String templateIds,
				String metricIds,
				String triggerIds,
				String zabbixMethod,

				String requestTime

				) throws MappingException, 
				NoMappingModelFoundException, 
				ServerErrorResponseException, 
				it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
				RestClientException, Exception
				{
			this.zabClient = new ZabbixAPIClient( url );
			Long dateFrom = null;
			Long dateTill= null;
			ArrayList<ZabbixItemResponse> result = new ArrayList<>();

			try{
				MetricsHistoryTimeRequest timerequest = 
						(MetricsHistoryTimeRequest) JsonUtility.deserializeJson(requestTime, MetricsHistoryTimeRequest.class);

				dateFrom = timeConv.encodeDate2UnitTime(timerequest.getDateFrom().getYear() + "-" +
						timerequest.getDateFrom().getMonth() + "-" +
						timerequest.getDateFrom().getDay()+ " 13"+":"+"13"+":"+"13") ; 

				dateTill = timeConv.encodeDate2UnitTime(timerequest.getDateTo().getYear() + "-" +
						timerequest.getDateTo().getMonth() + "-" +
						timerequest.getDateTo().getDay() + " 16"+":"+"16"+":"+"16");

				boolean uptoNow = timerequest.getDateTo().getUpToNow();

				boolean every10mins = timerequest.getFilterTime().getEvery10mis();
				boolean every30mins = timerequest.getFilterTime().getEvery30mins();
				boolean every30s = timerequest.getFilterTime().getEvery30s();
				boolean averageValue = timerequest.getFilterTime().getOncePerDayAverage();

				result = getMetricHistory(
						testbedType, 
						url,
						token,
						hostId, 
						templateIds,
						metricIds,
						triggerIds,
						zabbixMethod,
						requestTime
						);
			}
			catch( NullPointerException ne){

				result = getMetricHistory(
						testbedType, 
						url,
						token,
						hostId, 
						templateIds,
						metricIds,
						triggerIds,
						zabbixMethod,
						requestTime 
						);
				return result;
			}

			catch( JsonMappingException me){

				result = getMetricHistory(
						testbedType, 
						url,
						token,
						hostId, 
						templateIds,
						metricIds,
						triggerIds,
						zabbixMethod,
						requestTime 
						);
				return result;

			}
			return result;
				}


		/*********************************************
		 * GET HISTORY 
		 * 
		 * @param testbedType
		 * @param url
		 * @param token
		 * @param hostId
		 * @param templateIds
		 * @param metricIds
		 * @param triggerIds
		 * @param zabbixMethod
		 * @param requestTime
		 * @return
		 * @throws MappingException
		 * @throws NoMappingModelFoundException
		 * @throws ServerErrorResponseException
		 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
		 * @throws RestClientException
		 * @throws Exception
		 */
		public ArrayList<ZabbixItemResponse> getMetricHistory(
				String testbedType, 
				String url,
				String token,
				String hostId, 
				String templateIds,
				String metricIds,
				String triggerIds,
				String zabbixMethod,
				String requestTime) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, RestClientException, Exception{

			this.zabClient = new ZabbixAPIClient( url );
			jsonrpc = "2.0";
			method = zabbixMethod;
			output = "extend";
			id = 1;

			requestItem.setJsonrpc(jsonrpc);
			requestItem.setMethod(method);

			requestItem.setAuth(token);

			itemParameters = new ZabbixParamItemRequest();
			itemParameters.setOutput(output);

			/***************************************
			 * SETTING HISTORY or EVENTS in ZABBIX
			 **************************************/
			if(zabbixMethod.equals(ZabbixMethods.HISTORY.getzabbixMethod())  || 
					zabbixMethod.equals(ZabbixMethods.EVENT.getzabbixMethod())      )
			{
				int limit = 10;
				String sortfield = "clock";
				if(requestTime==null && zabbixMethod.equals(ZabbixMethods.EVENT.getzabbixMethod())){
					limit = 5;
				}
				limit = 170;
				int history = TEXTHistoryType;

				ZabbixParamGraphRequest parameters4history = new ZabbixParamGraphRequest();
				JSONRPCRequest<ZabbixParamGraphRequest> request = new JSONRPCRequest<ZabbixParamGraphRequest>();

				String sortorder = "ASC"; //"DESC"
				int id = 1;

				request.setJsonrpc(jsonrpc);
				request.setMethod(method);
				request.setAuth(token);

				parameters4history.setOutput(output);
				parameters4history.setHistory(history);
				parameters4history.setLimit(limit);
				parameters4history.setSortfield(sortfield);
				parameters4history.setSortorder(sortorder);	

				if(!(metricIds==null))
				{
					parameters4history.setItemids(metricIds);
					itemParameters.setTemplateids(null);
					itemParameters.setHostids(null);
				}
				else{
					parameters4history.setHostids(hostId);
				}

				request.setParams(parameters4history);
				request.setId(id);
				ArrayList<ZabbixItemResponse> resultHistory = new ArrayList<>();
				resultHistory = zabClient.getGraph(request);

				if (resultHistory.size()==0  && (zabbixMethod.equals(ZabbixMethods.HISTORY.getzabbixMethod()))){
					history = INTHistoryType;
					parameters4history.setHistory(history);
					resultHistory = zabClient.getGraph(request);
					if (resultHistory.size()==0){
						history = FLOATHistoryType;
						parameters4history.setHistory(history);
						resultHistory = zabClient.getGraph(request);
					}
				}
				return resultHistory;
			}

			/***************************
			 * SETTING ITEMS in ZABBIX
			 ***************************/
			else  {

				itemParameters.setHostids(hostId);

				if(!(templateIds==null) && !(hostId==null)){
					itemParameters.setTemplateids(null);		
				}
				else if(!(templateIds==null)){
					itemParameters.setTemplateids(templateIds);
					itemParameters.setHostids(null);
				}
				//For getting specific metrics and history
				else if(!(metricIds==null))
				{
					itemParameters.setItemids(metricIds);
					itemParameters.setTemplateids(null);
					itemParameters.setHostids(null);
				}
				//	itemParameters.setTemplateids(templateIds);
				key = new ZabbixSearchKeyRequest();
				key.setKey("");

				itemParameters.setSearch(key);

				requestItem.setParams(itemParameters);
				requestItem.setId(id);

				resultItem = zabClient.getItemNOZabbixWrappingResponse(requestItem, url);

				return resultItem;
			}
		}


		/**
		 *TriggerList for wrapper  
		 * @param createdHostId
		 * @param itemId
		 * @param zabbixMethod
		 * @return trigger list from zabbix API 
		 * @throws MappingException
		 * @throws NoMappingModelFoundException
		 * @throws ServerErrorResponseException
		 * @throws it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException
		 * @throws RestClientException
		 * @throws Exception
		 */
		public  ArrayList<ZabbixItemResponse> getTriggerList(
				String testbed, 
				String url,
				String token,
				String createdHostId, 
				String itemId,
				String zabbixMethod) throws MappingException, 
				NoMappingModelFoundException, 
				ServerErrorResponseException, 
				it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
				RestClientException, Exception {

			this.zabClient = new ZabbixAPIClient( url );

			jsonrpc = "2.0";
			method = zabbixMethod;
			output = "extend";
			id = 1;

			JSONRPCRequest<ZabbixParamTriggerRequest> request = new JSONRPCRequest<ZabbixParamTriggerRequest>();
			request.setJsonrpc(jsonrpc);
			request.setMethod(method);

			request.setAuth(token);

			ZabbixParamTriggerRequest triggerParameters = new ZabbixParamTriggerRequest();
			triggerParameters.setOutput(output);
			if(!(itemId==null))
			{
				ArrayList<String> itemIdsarray = new ArrayList<>();
				itemIdsarray.add(itemId);
				triggerParameters.setItemids(itemIdsarray);
				createdHostId=null;
			}
			triggerParameters.setOnly_true(null);
			triggerParameters.setExpandDescription(true);
			triggerParameters.setHostids(createdHostId);
			request.setParams(triggerParameters);
			request.setId(id);

			resultItem = zabClient.getTrigger4Adapter(request);
			return resultItem;
		}
	}