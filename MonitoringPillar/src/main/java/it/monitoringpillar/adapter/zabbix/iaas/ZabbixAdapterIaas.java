package it.monitoringpillar.adapter.zabbix.iaas;

import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.JSONRPCRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.TriggerParamRequestByGroup;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixFilterRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamHostGroupRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTemplateRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamItemRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTriggerRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixSearchKeyRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.ZabbixAPIClient;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Stateless
public class ZabbixAdapterIaas implements Serializable{

	private static final long serialVersionUID = 1L;
	private String jsonrpc;
	private String method;
	private String output;
	private String keyIaaS;
	private int id;
	private ZabbixAPIClient zabClient;
	private ZabbixParamItemRequest itemParameters;
	private ZabbixSearchKeyRequest key;
	private String iaasURL;

	public ZabbixAdapterIaas() {
		this.jsonrpc = "2.0";
		this.method = "";
		this.output = "extend";
		this.id =1;
	}
	
	/**
	 * Get List of Metrics for Specific host
	 * @param testbedType
	 * @param hostId
	 * @param templateIds
	 * @param methodFromManager
	 * @return list of metrics from zabbix API
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	public ArrayList<ZabbixItemResponse> getMetricsList( 
			String testbedType,
			String url,
			String token,
			String hostId,
			String templateIds,
			String methodFromManager) 
					throws JsonParseException, 
					JsonMappingException, 
					IOException, 
					Exception {

		this.zabClient = new ZabbixAPIClient( url );
		
		jsonrpc = "2.0";
		method = methodFromManager;
		output = "extend";
		id = 1;
	
		JSONRPCRequest<ZabbixParamTemplateRequest> request = new JSONRPCRequest<ZabbixParamTemplateRequest>();
		request.setJsonrpc(jsonrpc);
		request.setMethod(method);

		request.setAuth(token);
		ZabbixParamTemplateRequest itemFilteredParameters = new ZabbixParamTemplateRequest();
		itemFilteredParameters = new ZabbixParamTemplateRequest();
		itemFilteredParameters.setOutput(output);
		itemFilteredParameters.setHostids(hostId);

		request.setParams(itemFilteredParameters);
		request.setId(id);

		return zabClient.getFilterdItemNOZabbixWrappingResponse(request); 
	}


	/*
	 * Get Trigger List per Associated Host
	 */
	public  ArrayList<ZabbixItemResponse> getTriggerList( 
			String testBedType, 
			String url,
			String token, 
			String createdHostId, 
			String itemId, 
			String methodFromManager) throws Exception {
		
		boolean problems = false;
		String group = null;
		return setTriggerClient( testBedType, url, token, createdHostId, itemId, methodFromManager, problems, group);
	}
	
	public  ArrayList<ZabbixItemResponse> getTriggerWithProblems( 
			String testBedType, 
			String url,
			String token, 
			String createdHostId, 
			String itemId, 
			String methodFromManager,
			String group) throws Exception {
		
		boolean problems = true;
		return  setTriggerClient( testBedType, url, token, createdHostId, itemId, methodFromManager, problems, group);
	}
	
	private ArrayList<ZabbixItemResponse> setTriggerClient(String testBedType, 
			String url,
			String token, 
			String createdHostId, 
			String itemId, 
			String methodFromManager,
			boolean problems, 
			String group) throws Exception{
	
		this.zabClient = new ZabbixAPIClient(url);
	
	jsonrpc = "2.0";
	method = methodFromManager; 
	output = "extend";

	id = 1;

	JSONRPCRequest<ZabbixParamTriggerRequest> request = new JSONRPCRequest<ZabbixParamTriggerRequest>();
	JSONRPCRequest<TriggerParamRequestByGroup> requestByGroup = new JSONRPCRequest<>();
	
	request.setJsonrpc(jsonrpc);
	request.setMethod(method);

	request.setAuth(token);

	ZabbixParamTriggerRequest itemParameters = new ZabbixParamTriggerRequest();
	itemParameters.setOutput(output);
	itemParameters.setExpandDescription(true);
	
	itemParameters.setHostids(createdHostId);

	ArrayList<String> itemIdsarray = new ArrayList<String>();
	itemIdsarray.add(itemId);
	itemParameters.setItemids(itemIdsarray);
	itemParameters.setExpandExpression(true);

	if (!problems==false){
		TriggerParamRequestByGroup paramsByGroup = new TriggerParamRequestByGroup();
		paramsByGroup.setOnlyTrue(problems);
		paramsByGroup.setGroup(group);
		paramsByGroup.setOutput(output);
		paramsByGroup.setExpandExpression(true);
		//TODO: keep studying how to make it work with period of time
//		paramsByGroup.setLastChangeTill("");
		requestByGroup.setId(id);
		requestByGroup.setMethod(methodFromManager);
		requestByGroup.setAuth(token);
		requestByGroup.setJsonrpc(jsonrpc);
		requestByGroup.setParams(paramsByGroup);
		return zabClient.getTriggerByGroup(requestByGroup);
	}
	request.setParams(itemParameters);
	request.setId(id);
	
	return zabClient.getTrigger4Adapter(request);
	}

	/**
	 * Get Monitored Hosts in PrismaIaaS
	 * @param testbedType
	 * @param methodFromAdapterZabbix
	 * @param hostGroupIdFromAdapter
	 * @param tagService
	 * @return the response from monitored host
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	public ArrayList<ZabbixMonitoredHostsResponse> getMonitoredHostListinPrismaIaaS(
			String testbedType, 
			String url,
			String token,
			String methodFromAdapterZabbix,
			String hostGroupIdFromAdapter,
			String tagService,
			String triggerid
			) 
					throws JsonParseException, 
					JsonMappingException, 
					IOException, Exception {
	
		this.zabClient = new ZabbixAPIClient( url );
		
		jsonrpc = "2.0";
		method = methodFromAdapterZabbix; 
		output = "extend";
		id = 1;

		JSONRPCRequest<ZabbixParamRequest> request = new JSONRPCRequest<ZabbixParamRequest>();
		request.setJsonrpc(jsonrpc);
		request.setMethod(method);

		request.setAuth(token);

		ZabbixParamRequest parametersHosts = new ZabbixParamRequest();
		parametersHosts.setOutput(output);

		parametersHosts.setGroupids(hostGroupIdFromAdapter);
		parametersHosts.setSelectInventory("extend");

		ZabbixFilterRequest filter = new ZabbixFilterRequest();

		parametersHosts.setFilter(filter);
		
		if (!(triggerid==null)){
			parametersHosts.setTriggerids(triggerid);
			parametersHosts.setHostids(null);
		}
		request.setParams(parametersHosts);
		request.setId(id);

		return zabClient.getHosts4Adapter(request, null);
	}

	/**
	 * Get HostGroup List
	 * @param testbedType
	 * @param methodFromAdapterZabbix
	 * @return groups from zabbix api
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	public  ArrayList<ZabbixHostGroupResponse> getHostGroupList(
			String testbedType, 
			String url,
			String token,
			String methodFromAdapterZabbix)
					throws JsonParseException, 
					JsonMappingException, 
					IOException, Exception {
		
		this.zabClient = new ZabbixAPIClient( url );
		
		jsonrpc = "2.0";
		method = methodFromAdapterZabbix;
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

		return zabClient.getHostGroup(request, null);
	}


	/**
	 * Get Zabbix Features
	 * @param testbedType
	 * @param iaasURL
	 * @param hostId
	 * @param zabbixMethodFromManager
	 * @return zabbix item API
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	public  ArrayList<ZabbixItemResponse> getZabbixFeature(
			String testbedType, 
			String url,
			String token,
			String hostId, 
			String zabbixMethodFromManager
			) 
					throws JsonParseException, JsonMappingException, IOException, Exception {

		this.zabClient = new ZabbixAPIClient( url );
		jsonrpc = "2.0";
		method = zabbixMethodFromManager;
		output = "extend";
		id = 1;

		JSONRPCRequest<ZabbixParamItemRequest> request = new JSONRPCRequest<ZabbixParamItemRequest>();
		request.setJsonrpc(jsonrpc);
		request.setMethod(method);

		request.setAuth(token);

		ZabbixParamItemRequest params = new ZabbixParamItemRequest();
		params.setOutput(output);
		params.setHostids(hostId);

		ZabbixSearchKeyRequest Key = new ZabbixSearchKeyRequest();
		Key.setKey("");
		params.setSearch(Key);
		request.setParams(params);
		request.setId(id);

		request.setParams(params);

		return zabClient.getItemNOZabbixWrappingResponse(request, null);
	}

}