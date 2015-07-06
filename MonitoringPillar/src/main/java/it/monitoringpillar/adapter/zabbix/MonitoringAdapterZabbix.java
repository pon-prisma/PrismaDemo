package it.monitoringpillar.adapter.zabbix;

import it.monitoringpillar.MonitoringAdapter;
import it.monitoringpillar.adapter.zabbix.handler.GroupIDByName;
import it.monitoringpillar.adapter.zabbix.handler.HostIDByName;
import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ServerType;
import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.monitoringpillar.adapter.zabbix.iaas.ZabbixAdapterIaas;
import it.monitoringpillar.adapter.zabbix.metrics.ZabbixAdapterSetter;
import it.monitoringpillar.config.MonitoringConfig;
import it.monitoringpillar.config.PillarEnvironmentConfig;
import it.monitoringpillar.wrapper.WrapperIaaS;
import it.monitoringpillar.wrapper.WrapperIaasHealth;
import it.monitoringpillar.wrapper.WrapperMetricV2_4;
import it.monitoringpillar.wrapper.WrapperMetrics;
import it.monitoringpillar.wrapper.WrapperMonitoringEvents;
import it.monitoringpillar.wrapper.WrapperWatcher;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.MonitoringVMCredentialsResponse;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.MonitPillarEventResponse;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MetricsHistoryTimeRequest;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.HostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.JSONRPCResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV2_4;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponseV2_4;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.ws.rs.NotFoundException;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.javatuples.Triplet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
/**
 * 
 * @author m.grandolfo
 * This class implements the interface for giving back all the methods (for properly managing Zabbix API) 
 * to be used by MoniotringPillarService class which will expose them as Restful API
 */

@Stateless
@Local(MonitoringAdapter.class)
public class MonitoringAdapterZabbix implements MonitoringAdapter { 

	private String hostName;
	private String hostIdtoAdapter;
	private final String SERVER_METRICS = "metrics";
	private final String SERVER_WATCHER = "watcher";
	private final String SERVER_IAAS = "iaas";
	private String hostid2Zabbix;
	private String groupid2Zabbix;

	private JSONRPCResponse<ZabbixItemResponse> result;

	private String hostGroupIdtoAdapter;
	private String tagService;
	private String url;
	private String token;
	private final static String WG_PREFIX = "Workgroup-"; 

	//	private HostIntoMonitoringPlatform hostsPlatf;
	//	private GroupIntoMonitoringPlatform groupsPlatf;

	@Inject
	private WrapperIaaS wrapperIaaS;

	@Inject
	private WrapperMetrics vmInfoPaaS;

	@Inject
	private WrapperMetricV2_4 vmInfoPaaSV2_4;

	@Inject
	private WrapperWatcher vmInfoWatcher;

	@Inject
	private WrapperMonitoringEvents wrapperEvents;

	@Inject
	private WrapperIaasHealth wrapperTrigger4Iaas;

	@Inject
	private ZabbixAdapterIaas zabAdapIaas;

	@EJB
	private ZabbixAdapterSetter zabAdapMetrics;

	@EJB 
	private MonitoringServerRouter router;

	@EJB
	private HostIDByName zabHostId;

	@Inject
	private GroupIDByName zabGroupId; 

	@Inject
	private MonitoringConfig prop;

	@Inject
	private PillarEnvironmentConfig envConf;

	//	@Inject
	//	private HostIntoMonitoringPlatform hostsPlatf;
	//	
	//	@Inject 
	//	private GroupIntoMonitoringPlatform groupsPlatf;

	public MonitoringAdapterZabbix() throws IOException 
	{
		this.result = new JSONRPCResponse<ZabbixItemResponse>();
		this.url = null;
		this.token = null;
		this.hostid2Zabbix = null;
		this.groupid2Zabbix = null;
		//		this.hostsPlatf = new HostIntoMonitoringPlatform();
		//		this.groupsPlatf = new GroupIntoMonitoringPlatform();
	}


	/***************
	 * IAAS
	 ***************/
	@Override
	public MonitoringWrappedResponseIaas getIaaSOverallMachines (
			String testbedType, 
			List<String> iaasTypes, 
			List<String> iaashosts
			) throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, Exception
	{
		//		vmInfoIaas.initWrapperIaas(testbedType);
		String groupID = null;
		//Get IaaS Server Credentials
		url = router.getURL(testbedType, ServerType.SERVERIAAS.getServerType());
		token = router.getToken(testbedType, ServerType.SERVERIAAS.getServerType());
		//		ArrayList<ZabbixHostGroupResponse> groupsInZabbixIaas = new ArrayList<>();
		//		ArrayList<ZabbixMonitoredHostsResponse> hostsinZabbixIaas = new ArrayList<>();

		ArrayList<ZabbixHostGroupResponse> groupsInZabbixIaas = new ArrayList<>();
		ArrayList<ZabbixMonitoredHostsResponse> hostsinZabbixIaas = new ArrayList<>();
		String hostId = null;

		for (String groupName: iaasTypes){
			groupsInZabbixIaas = 
					zabAdapIaas.getHostGroupList(testbedType, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod());
			//			groupsPlatf.setGroupInfo(groupsInZabbixIaas);
			groupID = zabGroupId.getGroupIDsintoZabbix(testbedType, url, token, groupName, groupsInZabbixIaas);
			//			groupsPlatf.setGroupId(groupID);

			hostsinZabbixIaas = 
					zabAdapIaas.getMonitoredHostListinPrismaIaaS(
							testbedType, url, token, ZabbixMethods.HOST.getzabbixMethod(), groupID, tagService, null);
			if (iaashosts!=null){
				for (String hostFromUser : iaashosts) {
					hostId = zabHostId.getHostID(testbedType, url, token, groupName, hostFromUser, null, hostsinZabbixIaas);
				}
			}
			else hostId = null;
		}

		//		hostsPlatf.setHostInfo(hostsinZabbixIaas);
		try{
			MonitoringWrappedResponseIaas result = 
					wrapperIaaS.getWrappedIaas(
							testbedType, url, token, iaasTypes, iaashosts,  groupsInZabbixIaas, groupID,  hostsinZabbixIaas, hostId);
			return result;
		}catch(Exception e){
			throw new Exception(e);
		}
	}


	@Override
	public WrappedIaasHealthByTrigger getIaasHealth(
			String testbed,
			String hostgroup) throws Exception {

		url = router.getURL(testbed, ServerType.SERVERIAAS.getServerType());
		token = router.getToken(testbed, ServerType.SERVERIAAS.getServerType());

		ArrayList<ZabbixHostGroupResponse> groupsIntoPlatf =
				zabAdapIaas.getHostGroupList(testbed, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod());

		for(ZabbixHostGroupResponse group : groupsIntoPlatf){
			if(group.getName().equalsIgnoreCase(hostgroup)){
				return wrapperTrigger4Iaas.getTriggerforIaasHealth(
						testbed, 
						url,
						token, 
						hostgroup 
						);
			}
			//		else if(hostgroup.equals(HostGroups.COMPUTE.getGroupName())){
			//		}
			//		else if(hostgroup.equals(HostGroups.CONTROLLER.getGroupName())){
			//		}
			//		else if(hostgroup.equals(HostGroups.NETWORK.getGroupName())){
			//		}
		}
		throw new NamingException("WRONG HOST GROUP INSERTED: NOT Present in the monitoring Platform");
	}


	/*******************
	 *        PAAS
	 *******************/
	/******************************
	 * CREATE GROUP
	 */
	@Override
	public ArrayList<JSONRPCResponse<HostGroupResponse>> creationMonitoredHostGroup(
			String testbed, String hostGroupName) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, Exception {

		//Create a Host into Metrics Server
		url =  defineServerTypePaaSURL(testbed, ServerType.SERVERMETRICS.getServerType());
		token = defineServerTypePaaSToken(testbed, ServerType.SERVERMETRICS.getServerType());
		ArrayList<JSONRPCResponse<HostGroupResponse>> createdPaasHostGroups = 
				new ArrayList<JSONRPCResponse<HostGroupResponse>>();

		JSONRPCResponse<HostGroupResponse> createdHostGroupintoMetrics = zabAdapMetrics.getHostGroupCreation(
				testbed, 
				url,
				token, 
				WG_PREFIX + hostGroupName,
				ZabbixMethods.HOSTGROUPCREATE.getzabbixMethod()
				);

		createdPaasHostGroups.add(createdHostGroupintoMetrics);

		//Create a Host into Watcher Server
		url =  defineServerTypePaaSURL(testbed, ServerType.SERVERWATCHER.getServerType());
		token = defineServerTypePaaSToken(testbed, ServerType.SERVERWATCHER.getServerType());
		JSONRPCResponse<HostGroupResponse> createdHostGroupintoWatcher = zabAdapMetrics.getHostGroupCreation(
				testbed, 
				url, 
				token, 
				WG_PREFIX+hostGroupName,
				ZabbixMethods.HOSTGROUPCREATE.getzabbixMethod()
				);

		createdPaasHostGroups.add(createdHostGroupintoWatcher);

		return createdPaasHostGroups; 
	}


	/*****************
	 * HOST CREATION
	/**
	 * Implements the Interface for creating a host from adapter
	 * 
	 * @return the host Id that Zabbix API returns when creating an host
	 * @param testbed in order to choose which URL to set in the client
	 * @param uuid of the host
	 * @param vmip
	 * @param serviceCategory group to which the host belongs
	 * @param serviceTag ID identifying the group of hosts in the service
	 * @param one or more (depending on the type of serviceCategory) atomic service  
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> creationMonitoredHost (
			String testbed,
			String hostGroup,
			String uuid,
			String vmip,
			String serviceCategory,
			String serviceTag,
			List<String> service
			) throws NamingException, NotFoundException, Exception{

		//Create a Host into Metrics Server
		url =  defineServerTypePaaSURL(testbed, ServerType.SERVERMETRICS.getServerType());
		token = defineServerTypePaaSToken(testbed, ServerType.SERVERMETRICS.getServerType());
		ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> createdPaasHosts = 
				new ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>>();

		// Check if Group Exists, if not, it throws the exception
		if(isGroupPresent( testbed,  url,  token,  hostGroup)){
			JSONRPCResponse<MonitoringVMCredentialsResponse> createdHostintoMetrics = zabAdapMetrics.getCreationHost(
					testbed, 
					url,
					token, 
					ServerType.SERVERMETRICS.getServerType(),
					WG_PREFIX+hostGroup,
					uuid, 
					vmip, 
					serviceCategory,
					serviceTag,
					service,
					ZabbixMethods.HOSTCREATE.getzabbixMethod()
					);

			createdPaasHosts.add(createdHostintoMetrics);
		}
		//Create a Host into Watcher Server
		url =  defineServerTypePaaSURL(testbed, ServerType.SERVERWATCHER.getServerType());
		token = defineServerTypePaaSToken(testbed, ServerType.SERVERWATCHER.getServerType());
		// Check if Group Exists, if not, it throws the exception
		if(isGroupPresent( testbed,  url,  token,  hostGroup)){
			JSONRPCResponse<MonitoringVMCredentialsResponse> createdHostintoWatcher = zabAdapMetrics.getCreationHost(
					testbed, 
					url, 
					token, 
					ServerType.SERVERWATCHER.getServerType(),
					WG_PREFIX+hostGroup,
					uuid, 
					vmip, 
					serviceCategory,
					serviceTag,
					service,
					ZabbixMethods.HOSTCREATE.getzabbixMethod()
					);

			createdPaasHosts.add(createdHostintoWatcher);
		}
		return createdPaasHosts; 
	}


	private boolean isGroupPresent(String testbed, String url, String token, String hostGroup) 
			throws JsonParseException, JsonMappingException, IOException, NamingException, Exception{
		ArrayList<ZabbixHostGroupResponse> workgroups = 
				zabAdapIaas.getHostGroupList(testbed, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod());
		for (ZabbixHostGroupResponse group : workgroups){
			if (group.getName().equals(WG_PREFIX+hostGroup)){
				return true;
			}
		}
		throw new NamingException("The Group inserted does not exist into Monitoring platform at: " + url);
	}

	/*****************************************************************************
	 * This method removes the selected host by passing the vmuuid as information
	 * HOST DELETE
	 *****************************************************************************/
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> deleteMonitoredHost (
			String testbedType, 
			String hostvmuuids, 
			String serviceId) 
					throws MappingException, 
					NoMappingModelFoundException, 
					ServerErrorResponseException, 
					APIErrorException, 
					RestClientException, 
					Exception, NamingException {

		ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> removedPaasHosts =
				new ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>>();

		//		for (String vmuuid : hostvmuuids){
		/********************
		 * For ZABBIX METRICS
		 ********************/
		url =  defineServerTypePaaSURL(testbedType, ServerType.SERVERMETRICS.getServerType());
		token = defineServerTypePaaSToken(testbedType, ServerType.SERVERMETRICS.getServerType());

		hostid2Zabbix = zabHostId.getHostID(testbedType, url, token, null, hostvmuuids, tagService, null);

		JSONRPCResponse<MonitoringVMCredentialsResponse> hostRemovedFromMetrics = zabAdapMetrics.getDestructionHost(
				testbedType, 
				url,
				token, 
				hostid2Zabbix, 
				ZabbixMethods.HOSTDELETE.getzabbixMethod()
				);

		/*********************
		 * For ZABBIX WATCHER
		 ********************/
		url =  defineServerTypePaaSURL(testbedType, ServerType.SERVERWATCHER.getServerType());
		token = defineServerTypePaaSToken(testbedType, ServerType.SERVERWATCHER.getServerType());

		hostid2Zabbix = zabHostId.getHostID(testbedType, url, token, null, hostvmuuids, tagService, null);

		JSONRPCResponse<MonitoringVMCredentialsResponse> hostRemovedFromWatcher = zabAdapMetrics.getDestructionHost(
				testbedType,
				url, 
				token, 
				hostid2Zabbix, 
				ZabbixMethods.HOSTDELETE.getzabbixMethod()
				);

		removedPaasHosts.add(hostRemovedFromMetrics);
		removedPaasHosts.add(hostRemovedFromWatcher);
		//		}
		return removedPaasHosts;
	}

	/*********************
	 * DELETE HOSTGROUP
	 *********************/
	@Override
	public ArrayList<JSONRPCResponse<HostGroupResponse>> deleteMonitoredHostGroup(
			String testbedType, String hostGroupName) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, Exception, NamingException {

		ArrayList<JSONRPCResponse<HostGroupResponse>> removedPaasHosts =
				new ArrayList<JSONRPCResponse<HostGroupResponse>>();

		//		for (String vmuuid : hostvmuuids){
		/********************
		 * For ZABBIX METRICS
		 ********************/
		url =  defineServerTypePaaSURL(testbedType, ServerType.SERVERMETRICS.getServerType());
		token = defineServerTypePaaSToken(testbedType, ServerType.SERVERMETRICS.getServerType());

		groupid2Zabbix = zabGroupId.getGroupIDsintoZabbix(testbedType, url, token, WG_PREFIX+hostGroupName, null);

		JSONRPCResponse<HostGroupResponse> hostRemovedFromMetrics = zabAdapMetrics.getDeleteHostGroup(
				testbedType, 
				url,
				token, 
				groupid2Zabbix, 
				ZabbixMethods.GROUPDELETE.getzabbixMethod()
				);

		/*********************
		 * For ZABBIX WATCHER
		 ********************/
		url =  defineServerTypePaaSURL(testbedType, ServerType.SERVERWATCHER.getServerType());
		token = defineServerTypePaaSToken(testbedType, ServerType.SERVERWATCHER.getServerType());
		groupid2Zabbix = zabGroupId.getGroupIDsintoZabbix(testbedType, url, token, WG_PREFIX+hostGroupName, null);

		JSONRPCResponse<HostGroupResponse> hostRemovedFromWatcher = zabAdapMetrics.getDeleteHostGroup(
				testbedType,
				url, 
				token, 
				groupid2Zabbix, 
				ZabbixMethods.GROUPDELETE.getzabbixMethod()
				);

		removedPaasHosts.add(hostRemovedFromMetrics);
		removedPaasHosts.add(hostRemovedFromWatcher);
		//		}
		return removedPaasHosts;
	}

	/****************
	 * DISABLE METRIC
	 * @param <T>
	 ****************/
	@SuppressWarnings("unchecked")
	@Override
	public <T> JSONRPCResponse<T> getDisablingHost(
			String testbed, 
			String host_uuid, 
			String metric_id,
			String tag_service,
			String server_type, 
			String update
			)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		String method = null;
		String metricName = null;
		String metricIdtoAdapter = null;
		String hostid2Zabbix = null;


		ArrayList<ZabbixItemResponse> item_id = new ArrayList<>();
		JSONRPCResponse<MonitoringVMCredentialsResponse> hostMetricUpdated = new JSONRPCResponse<>();
		url = defineServerTypePaaSURL(testbed, server_type);
		token = defineServerTypePaaSToken(testbed, server_type);

		//In case uuid has been passed as parameter
		if (tag_service==null && host_uuid!=null){
			ArrayList<ZabbixMonitoredHostsResponse> hostsinZabbixPaaS = 
					zabAdapMetrics.getMonitoredHosts(testbed, url, token, ZabbixMethods.HOST.getzabbixMethod(), null, null, host_uuid);
			for(ZabbixMonitoredHostsResponse host : hostsinZabbixPaaS){
				hostid2Zabbix = host.getHostid(); 
			}
		}


		//	else 
		//TODO: implementare la logica per prendere il gruppo di hosts a partire dal tag ID come fatto nel wrapper?

		/**********************************
		 * METRIC SPECIFIED TO BE DISABLED
		 **********************************/
		if(!(metric_id==null)){
			method = ZabbixMethods.ITEMUPDATE.getzabbixMethod();

			item_id = zabAdapMetrics.getMetricsList(
					testbed, 
					url, 
					token,
					hostid2Zabbix, 
					null, 
					null, 
					null, 
					ZabbixMethods.METRIC.getzabbixMethod(), 
					null);

			Iterator<ZabbixItemResponse> metricIter = item_id.iterator();
			boolean metricFound=false; 
			while(!metricFound && metricIter.hasNext()){
				ZabbixItemResponse adapterResult = metricIter.next();
				metricName = adapterResult.getName();
				if(metricName.equalsIgnoreCase(metric_id)){
					metricIdtoAdapter = adapterResult.getItemid();
					metricFound = true;
				}
			}
			if(metricFound==false && !(metricIter.hasNext()))
				throw new NamingException("Wrong resourse host name inserted");
		}

		else method = ZabbixMethods.MASSUPDATE.getzabbixMethod();

		return (JSONRPCResponse<T>) 
				(zabAdapMetrics.getUpdateHostItem(
						testbed, 
						url, 
						token, 
						hostid2Zabbix, 
						metricIdtoAdapter, 
						method, 
						update
						));
	}

	/******************************************
	 * IT WRAPS Monitoring platform INFOs
	 ******************************************/
	@Override
	public MonitoringWrappedResponsePaas getOverallPaaSMetrics ( 
			String  testbed, 
			String hostuuid, 
			String group, 
			String service_category,
			String tag_service,
			List<String> atomic_service_id, 
			List<String> metrics_id, 
			List<String> triggers_id,
			String history,
			String server_type, 
			String requestTime
			) 
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, ResourceNotFoundException, Exception {

		MonitoringWrappedResponsePaas wrappedResult = new MonitoringWrappedResponsePaas();
		MetricsHistoryTimeRequest timerequest = new MetricsHistoryTimeRequest();

		url = defineServerTypePaaSURL(testbed, server_type);
		token = defineServerTypePaaSToken(testbed, server_type);
		String hostId = null;
		String groupId = null;
		String groupName = null;
		ArrayList<ZabbixMonitoredHostsResponse> hostsinZabbixPaaS = new ArrayList<>();
		ArrayList<ZabbixHostGroupResponse> groupsInZabbixPaaS = new ArrayList<>();


		if(tag_service!=null && hostuuid==null && group==null){
			hostsinZabbixPaaS =
					zabAdapMetrics.getMonitoredHosts2_4Temporay(testbed, url, token, ZabbixMethods.HOST.getzabbixMethod(), null, null, null,tag_service);
			if(hostsinZabbixPaaS.isEmpty())throw new NamingException("Wrong Service ID Inserted: Not Existing Into Platform");
			for(ZabbixMonitoredHostsResponse host : hostsinZabbixPaaS){
				hostId = host.getHostid();
			}
			groupsInZabbixPaaS = zabAdapMetrics.getHostGroupListinMetrics(testbed, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod(), groupId, null, hostId);
			for(ZabbixHostGroupResponse groupInfo : groupsInZabbixPaaS){
				groupId = groupInfo.getGroupid();
				groupName = groupInfo.getName();
			}
		}
		/*
		 * HostUUID specified into API request
		 */
		else if(hostuuid!=null){
			hostsinZabbixPaaS = 
					zabAdapMetrics.getMonitoredHosts(testbed, url, token, ZabbixMethods.HOST.getzabbixMethod(), null, null, hostuuid);
			for(ZabbixMonitoredHostsResponse host : hostsinZabbixPaaS){
				hostId = host.getHostid();
			}
			groupsInZabbixPaaS = zabAdapMetrics.getHostGroupListinMetrics(testbed, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod(), groupId, null, hostId);
			for(ZabbixHostGroupResponse groupInfo : groupsInZabbixPaaS){
				groupId = groupInfo.getGroupid();
				groupName = WG_PREFIX+group;
			}
		}

		else{
			groupsInZabbixPaaS = 
					zabAdapMetrics.getHostGroupListinMetrics(testbed, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod(), null, null, null);

			groupId = zabGroupId.getGroupIDsintoZabbix(testbed, url, token, WG_PREFIX+group, groupsInZabbixPaaS);
			groupName = WG_PREFIX+group;

			hostsinZabbixPaaS = 
					zabAdapMetrics.getMonitoredHosts(
							testbed, url, token, ZabbixMethods.HOST.getzabbixMethod(), null, groupId, null); 
			if(hostuuid!=null){
				hostId = zabHostId.getHostID(testbed, url, token, group, hostuuid, null, hostsinZabbixPaaS);
			}
		}

		if(server_type.contains(SERVER_METRICS) || server_type==null) {
			//TODO: to change implementation under the hood WG_PREFIX to be assigned to proper host
			wrappedResult = vmInfoPaaS.getWrappedPaas(
					testbed, 
					url, 
					token, 
					groupName 
					,hostuuid
					,service_category, 
					tag_service,
					atomic_service_id, 
					metrics_id, 
					triggers_id
					,history
					,server_type
					,requestTime
					,groupsInZabbixPaaS
					,groupId
					,hostsinZabbixPaaS
					,hostId
					);
		}
		//		else wrappedResult = 
		//				vmInfoWatcher.getWrappedWatcher(
		//						testbed, url, token, WG_PREFIX+group, hostuuid, service_category, 
		//						tag_service, atomic_service_id, metrics_id, triggers_id, history, 
		//						server_type, requestTime, groupsInZabbixPaaS, groupId, hostsinZabbixPaaS, hostId);

		return wrappedResult;
	}

	/********************************
	 * IMPLEMENTATION FOR WRAPPER 2.4
	 ********************************/
	@Override
	public MonitoringWrappedResponsePaas getOverallPaaSMetricsV2_4 ( 
			String  testbed, 
			String hostuuid, 
			String group, 
			String service_category,
			String tag_service,
			List<String> atomic_service_id, 
			List<String> metrics_id, 
			List<String> triggers_id,
			String history,
			String server_type, 
			String requestTime
			) 
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, ResourceNotFoundException, NamingException, Exception {

		MonitoringWrappedResponsePaas wrappedResult = new MonitoringWrappedResponsePaas();
		MetricsHistoryTimeRequest timerequest = new MetricsHistoryTimeRequest();

		url = defineServerTypePaaSURL(testbed, server_type);
		token = defineServerTypePaaSToken(testbed, server_type);
		String hostId = null;
		String groupId = null;
		String groupName=null;
		List<ZabbixMonitoredHostResponseV2_4> hostsinZabbixPaaS = new ArrayList<>();
		List<ZabbixHostGroupResponse> groupsInZabbixPaaS = new ArrayList<>();
		List<ZabbixItemResponse> items = new ArrayList<>();
		List<ZabbixTemplateResponseV2_4> templatesExtended = new ArrayList<>();
		Map<ZabbixMonitoredHostResponseV2_4, List<ZabbixTemplateResponseV2_4>> hostsByTagMap = new HashMap<>();
		Map<ZabbixTemplateResponseV2_4, List<ZabbixItemResponse>> itemsByTemplate = new HashMap<>();



		/*****************************
		 * ONLY TAG ID PASSED FROM API
		 ****************************/
		if(tag_service!=null && hostuuid==null && group==null){
			//Get the list of EXTENDED hosts (templates and metrics associated to em) 
			hostsinZabbixPaaS =
					zabAdapMetrics.getMonitoredHostsZABBIXV2_4(testbed, url, token, ZabbixMethods.HOST.getzabbixMethod(), null, null, null, tag_service);
			if(hostsinZabbixPaaS.isEmpty())throw new NamingException("Wrong Service ID Inserted: Not Existing Into Platform");

			for(ZabbixMonitoredHostResponseV2_4 host : hostsinZabbixPaaS){
				hostId = host.getHostid();
				//GET the list of items
				items = host.getItems();
				//GET the list of templates
				templatesExtended =  
						zabAdapMetrics.getTemplates(testbed, url, token, hostId, ZabbixMethods.TEMPLATE.getzabbixMethod());	
				hostsByTagMap.put(host, templatesExtended);
				for(ZabbixTemplateResponseV2_4 template : templatesExtended){
					
					//GET ONLY USEFUL ITEMS REALLY ASSOCIATED TO TEMPLATES
					List<ZabbixItemResponse> usefulItems = new ArrayList<>();
					for(ZabbixItemResponse itemFromExtHost: items){
						for (ZabbixItemResponse item : template.getItems()){
							if(item.getName().equals(itemFromExtHost.getName())){
								usefulItems.add(itemFromExtHost);
							}
						}
					}
					itemsByTemplate.put(template, usefulItems);
				}
				
			}	
				groupsInZabbixPaaS = zabAdapMetrics.getHostGroupListinMetrics(testbed, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod(), groupId, null, hostId);
				for(ZabbixHostGroupResponse groupInfo : groupsInZabbixPaaS){
					groupId = groupInfo.getGroupid();
					groupName = groupInfo.getName();
				}
		}


		/**************************
		 * ONLY HOST UUID SPECIFIED
		 **************************/
		else if(hostuuid!=null && group==null){
			hostsinZabbixPaaS = 
					zabAdapMetrics.getMonitoredHostsZABBIXV2_4(testbed, url, token, ZabbixMethods.HOST.getzabbixMethod(), null, null, hostuuid, null);
			if(hostsinZabbixPaaS.isEmpty())throw new NamingException("Wrong Host Name Inserted: Not Existing Into Platform");
			for(ZabbixMonitoredHostResponseV2_4 host : hostsinZabbixPaaS){
				hostId = host.getHostid();
				//GET the list of items
				items = host.getItems();
				//GET the list of templates EXTENDED (useful for cycling on metrics associated to em.. From extended host answer couldn't know it)	
				templatesExtended =  
						zabAdapMetrics.getTemplates(testbed, url, token, hostId, ZabbixMethods.TEMPLATE.getzabbixMethod());
				
				hostsByTagMap.put(host, templatesExtended);
				for(ZabbixTemplateResponseV2_4 template : templatesExtended){
					
					//GET ONLY USEFUL ITEMS REALLY ASSOCIATED TO TEMPLATES
					List<ZabbixItemResponse> usefulItems = new ArrayList<>();
					for(ZabbixItemResponse itemFromExtHost: items){
						for (ZabbixItemResponse item : template.getItems()){
							if(item.getName().equals(itemFromExtHost.getName())){
								usefulItems.add(itemFromExtHost);
							}
						}
					}
					itemsByTemplate.put(template, usefulItems);
				}
			}
			groupsInZabbixPaaS = zabAdapMetrics.getHostGroupListinMetrics(testbed, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod(), groupId, null, hostId);
			for(ZabbixHostGroupResponse groupInfo : groupsInZabbixPaaS){
				groupId = groupInfo.getGroupid();
				groupName = groupInfo.getName();
			}
		}

		/**************************
		 * ONLY GROUP SPECIFIED
		 **************************/
		else{
			groupsInZabbixPaaS = 
					zabAdapMetrics.getHostGroupListinMetrics(testbed, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod(), null, null, null);
			if(groupsInZabbixPaaS.isEmpty())throw new NamingException("Wrong Group Name Inserted: Not Existing Into Platform");
			groupId = zabGroupId.getGroupIDsintoZabbix(testbed, url, token, WG_PREFIX+group, groupsInZabbixPaaS);
			groupName = WG_PREFIX+group;

			hostsinZabbixPaaS = 
					zabAdapMetrics.getMonitoredHostsZABBIXV2_4(
							testbed, url, token, ZabbixMethods.HOST.getzabbixMethod(), null, groupId, null, null); 
			if(hostuuid!=null){
				hostId = zabHostId.getHostIDV2_4(testbed, url, token, group, hostuuid, null, hostsinZabbixPaaS);
			}
		}

		if(server_type.contains(SERVER_METRICS) || server_type==null) {
			//TODO: to change implementation under the hood WG_PREFIX to be assigned to proper host
			wrappedResult = vmInfoPaaSV2_4.getWrappedPaas(
					testbed, 
					url, 
					token, 
					groupName
					,hostuuid
					,service_category, 
					tag_service,
					atomic_service_id, 
					metrics_id, 
					triggers_id
					,history
					,server_type
					,requestTime
					,groupsInZabbixPaaS
					,groupId
					,hostsinZabbixPaaS
					,hostId
					,items
					,templatesExtended
					,hostsByTagMap
					, itemsByTemplate
					);
		}
		else wrappedResult = 
				vmInfoWatcher.getWrappedWatcher(
						testbed, url, token, groupName, hostuuid, service_category, 
						tag_service, atomic_service_id, metrics_id, triggers_id, history, 
						server_type, requestTime, groupsInZabbixPaaS, groupId, hostsinZabbixPaaS, hostId, items,templatesExtended);

		return wrappedResult;
	}

//private void determineItems(String testbed, String url, string token, String hostId, String zabbixMethod, ArrayList<ZabbixItemResponse> items){
//	
//	templatesExtended =  
//			zabAdapMetrics.getTemplates(testbed, url, token, hostId, ZabbixMethods.TEMPLATE.getzabbixMethod());	
//	hostsByTagMap.put(host, templatesExtended);
//	for(ZabbixTemplateResponseV2_4 template : templatesExtended){
//		
//		//GET ONLY USEFUL ITEMS REALLY ASSOCIATED TO TEMPLATES
//		List<ZabbixItemResponse> usefulItems = new ArrayList<>();
//		for(ZabbixItemResponse itemFromExtHost: items){
//			for (ZabbixItemResponse item : template.getItems()){
//				if(item.getName().equals(itemFromExtHost.getName())){
//					usefulItems.add(itemFromExtHost);
//				}
//			}
//		}
//		itemsByTemplate.put(template, usefulItems);
//	}
//}
	
	/*****************************************
	 * IT WRAPS EVENTS RETURNED FROM PLATFORM
	 *****************************************/
	@Override
	public MonitPillarEventResponse getOverallServerEvents(
			String testbed, 
			String host_id,
			String service_category_id,
			String tag_service,
			String server_type, 
			String requestTime
			)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		MonitPillarEventResponse wrappedEvents = new MonitPillarEventResponse();

		url = defineServerTypePaaSURL(testbed, server_type);
		token =defineServerTypePaaSToken(testbed, server_type);

		wrappedEvents = wrapperEvents.getEvent(
				testbed, 
				url, 
				token,
				host_id,
				service_category_id, 
				tag_service, 
				server_type,
				requestTime
				);

		return wrappedEvents;
	}

	private String defineServerTypePaaSURL(String testbed, String server_type) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, RestClientException, Exception{

		if(server_type.contains(SERVER_METRICS)){
			return url = router.getURL(testbed, ServerType.SERVERMETRICS.getServerType());	
		}
		else if (server_type.contains(SERVER_WATCHER)){
			return url = router.getURL(testbed, ServerType.SERVERWATCHER.getServerType());
		}
		else if (server_type.contains(SERVER_IAAS)){
			return url = router.getURL(testbed, ServerType.SERVERIAAS.getServerType());
		}
		throw new NamingException("Wrong server type inserted");
	}

	private String defineServerTypePaaSToken(String testbed, String server_type) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, RestClientException, Exception{

		if(server_type.contains(SERVER_METRICS)){
			return token = router.getToken(testbed, ServerType.SERVERMETRICS.getServerType());		
		}
		else if(server_type.contains(SERVER_WATCHER)){
			return token = router.getToken(testbed, ServerType.SERVERWATCHER.getServerType());	
		}
		else if(server_type.contains(SERVER_IAAS)){
			return token = router.getToken(testbed, ServerType.SERVERIAAS.getServerType());	
		}
		throw new NamingException("Wrong server type inserted");
	}
}