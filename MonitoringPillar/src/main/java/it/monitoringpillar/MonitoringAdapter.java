package it.monitoringpillar;

import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.MonitoringVMCredentialsResponse;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.MonitPillarEventResponse;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.HostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.JSONRPCResponse;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.naming.NamingException;

import org.apache.velocity.exception.ResourceNotFoundException;


@Stateless
@Named
//@Local
public interface MonitoringAdapter {
	
	//TEST 
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
			) throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, ResourceNotFoundException, Exception;

	//	public Object getMonitoringIaaSStatus (String testbedType, String vmname)
	//			throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException, Exception;

	//	public Object getMonitoringHypervisorInfo (String testbedType, String hostId)
	//			throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException, Exception;
	//
	//	public Object getMonitoringInstancesInfo ( String testbedType, String hostId)
	//			throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException, Exception;


	/*
	 * Billing/Graph/IaaS/PaaS Metrics Interfaces
	 */

	//	public Object getMonitoringIaaSSMetrics (String testbedType, String vmuuid,
	//			Boolean uptime, Boolean storage, Boolean networkIN, Boolean networkOUT,
	//			Boolean memory, Boolean cpuload)
	//					throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException, Exception;
	//
	//	public Object getMonitoringPaaSMetrics(String testbedType, String vmuuid, Boolean jboss, Boolean tomcat, 
	//			Boolean apache, Boolean mysql, Boolean postgresql, Boolean rabbitmq, Boolean x2goserver, Boolean vmaas) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException, Exception;
	//
	//	public Object getMonitoringPaasTrigger(String testbedType, String vmuuid, Boolean jboss, Boolean tomcat, 
	//			Boolean apache, Boolean mysql, Boolean postgresql, Boolean rabbitmq, Boolean x2goserver, Boolean vmaas) throws MappingException,
	//			NoMappingModelFoundException, ServerErrorResponseException,
	//			APIErrorException, RestClientException, Exception;
	//
	//	public Object getMonitoringVMIaasMetricsGraph( String testbedType, String vmuuid, String itemid) 
	//			throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException, Exception;



	/***********
	 *  IAAS
	 ***********/
	/**
	 * Method for obtaining the Iaas machines status 
	 *
	 * @param adapter
	 * @param testbedType
	 * @param iaasType
	 * @return MonitoringWrappedResponseIaasV2 Info 
	 * about IaaS machines, metrics, triggers depending
	 * what parameters are passed as arguments
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public MonitoringWrappedResponseIaas getIaaSOverallMachines(
			String testbedType, 
			List<String> iaasType,
			List<String> iaashost
			)
					throws MappingException, 
					NoMappingModelFoundException,
					ServerErrorResponseException,
					APIErrorException,
					RestClientException,
					Exception;

	/***
	 * 
	 * @param monitoringEnvironment
	 * @param hostgroup
	 * @return
	 * @throws Exception 
	 */
	public WrappedIaasHealthByTrigger getIaasHealth(
			String monitoringEnvironment,
			String hostgroup) throws Exception;

	/****************
	 *    PAAS
	 ***************/
	/**
	 * Create a host with certain characteristics
	 * @param adapter
	 * @param testbedType
	 * @param uuid
	 * @param vmip
	 * @param serviceCategory
	 * @param serviceTag
	 * @param service
	 * @return
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> creationMonitoredHost (
			String testbedType, 
			String hostGroup,
			String uuid, 
			String vmip, 
			String serviceCategory, 
			String serviceTag, 
			List<String> service)
					throws MappingException, 
					NoMappingModelFoundException,
					ServerErrorResponseException, 
					APIErrorException,
					RestClientException,
					Exception;
	
	public ArrayList<JSONRPCResponse<HostGroupResponse>> creationMonitoredHostGroup (
			String testbedType, 
			String hostGroupName)
					throws MappingException, 
					NoMappingModelFoundException,
					ServerErrorResponseException, 
					APIErrorException,
					RestClientException,
					Exception;

	/**
	 * Interface for deleting the host
	 * @param testbedType
	 * @param hostvmuuids
	 * @param serviceId
	 * @return
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 * @throws NamingException
	 */
	public ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> deleteMonitoredHost(
			String testbedType, String hostvmuuids, String serviceId)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception, NamingException;
	
	public ArrayList<JSONRPCResponse<HostGroupResponse>> deleteMonitoredHostGroup(
			String testbedType, String hostGroupName)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception, NamingException;

	/**
	 * Method for getting PaaS machines, metrics, 
	 * service category and triggers status, depending 
	 * which parameters are passed as arguments
	 * 
	 * @param adapterType
	 * @param testbeds_id
	 * @param host_id
	 * @param service_category_id
	 * @param tag_service
	 * @param atomic_service_id
	 * @param metrics_id
	 * @param triggers_id
	 * @return MonitoringWrappedResponsePaaSV2 wrapped response for giving info about paas hosts 
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public MonitoringWrappedResponsePaas getOverallPaaSMetrics(				 
			String testbeds_id, 
			String host_id,
			String group,
			String service_category_id,
			String tag_service, 
			List<String> atomic_service_id, 
			List<String> metrics_id, 
			List<String> triggers_id
			,String graph
			,String server 
			,String requestTime
			)
					throws MappingException,
					NoMappingModelFoundException, ServerErrorResponseException,
					APIErrorException, RestClientException, Exception;


	/**
	 * Interface useful for getting all events for Monitoring platform 
	 * @param testbed
	 * @param host_id
	 * @param service_category_id
	 * @param tag_service
	 * @param server_type
	 * @param requestTime
	 * @return a wrapped response for giving the events situation
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */

	public MonitPillarEventResponse getOverallServerEvents(
			String testbed,
			String host_id,
			String service_category_id, 
			String tag_service,
			String server_type,
			String requestTime) 
					throws MappingException,
					NoMappingModelFoundException, ServerErrorResponseException,
					APIErrorException, RestClientException, Exception;

	/**
	 * Useful for disabling a specific host and a specific metric for specified host
	 * @param testbed
	 * @param host_id
	 * @param metric_id
	 * @param tag_service
	 * @param server_type
	 * @return the ID of the disabled host, metric (array of 'em)
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	public  <T> JSONRPCResponse<T> getDisablingHost(
			String testbed,
			String host_id,
			String metric_id, 
			String tag_service, 
			String server_type,
			String update) 
					throws MappingException,
					NoMappingModelFoundException, ServerErrorResponseException,
					APIErrorException, RestClientException, Exception;





}