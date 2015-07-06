package it.monitoringpillar.adapter.nagios;

import it.monitoringpillar.MonitoringAdapter;
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

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.naming.NamingException;

import org.apache.velocity.exception.ResourceNotFoundException;

//@Named
//@Stateless
//@Local(MonitoringAdapter.class)
@IMonitAdaptNagios
public class MonitoringAdapterNagios implements MonitoringAdapter{


	@Override
	public ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> deleteMonitoredHost(String testbedType, String uuid, String serviceId)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, Exception {
				return null;
	}

	@Override
	public MonitoringWrappedResponseIaas getIaaSOverallMachines(
			String testbedType, List<String> iaasType, List<String> iaashost)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MonitoringWrappedResponsePaas getOverallPaaSMetrics(
			String testbeds_id, String host_id, String group,  String service_category_id,
			String tag_service, List<String> atomic_service_id,
			List<String> metrics_id, List<String> triggers_id, String history, String server, String requestTime)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MonitPillarEventResponse getOverallServerEvents(String testbed,
			String host_id, String service_category_id, String tag_service,
			String server_type, String requestTime) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public  JSONRPCResponse<MonitoringVMCredentialsResponse> getDisablingHost(String testbed,
			String host_id, String metric_id, String tag_service, String server_type, String update)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WrappedIaasHealthByTrigger getIaasHealth(
			String monitoringEnvironment,
			String hostgroup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> creationMonitoredHost(
			String testbedType, String hostGroup, String uuid, String vmip,
			String serviceCategory, String serviceTag, List<String> service)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<JSONRPCResponse<HostGroupResponse>> creationMonitoredHostGroup(
			String testbedType, String hostGroupName) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<JSONRPCResponse<HostGroupResponse>> deleteMonitoredHostGroup(
			String testbedType, String hostGroupName) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, Exception, NamingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MonitoringWrappedResponsePaas getOverallPaaSMetricsV2_4(
			String testbed, String hostuuid, String group,
			String service_category, String tag_service,
			List<String> atomic_service_id, List<String> metrics_id,
			List<String> triggers_id, String history, String server_type,
			String requestTime) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, ResourceNotFoundException,
			Exception {
		// TODO Auto-generated method stub
		return null;
	}


}