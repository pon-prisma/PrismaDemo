package it.monitoringpillar.adapter.zabbix.handler;


import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.monitoringpillar.adapter.zabbix.metrics.ZabbixAdapterSetter;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.ZabbixAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;

/**
 *  
 * @author m.grandolfo
 * This class retrieves Host group IDs
 */
@Stateless
public class GroupIDByName implements Serializable{

	private static final long serialVersionUID = 1L;
	private ZabbixAPIClient zabClient;

	@Inject
	private ZabbixAdapterSetter zabAdapSetter;


	@PostConstruct
	public void initGroup(String testbedType, String url, String token, String monitoringServerType) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException, Exception{

		this.zabClient = new ZabbixAPIClient(
				url );
	}

	/**
	 * 
	 * @param testBedType to choose amongst 
	 * @param metricsURL
	 * @param serviceCategory
	 * @return
	 * @throws RestClientException 
	 * @throws APIErrorException 
	 * @throws ServerErrorResponseException 
	 * @throws NoMappingModelFoundException 
	 * @throws MappingException 
	 * @throws Exception
	 */
	public String getGroupIDsintoZabbix(
			String testBedType, 
			String url,
			String token,
			String hostGroupName,
			List<ZabbixHostGroupResponse> groups) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException, Exception {

		String hostGroupId = null;

		if(groups==null){
			ArrayList<ZabbixHostGroupResponse> hostGroups = 
					zabAdapSetter.getHostGroupListinMetrics(
							testBedType,
							url,
							token,
							ZabbixMethods.HOSTGROUP.getzabbixMethod(),
							null,
							null, 
							null
							);

			for(ZabbixHostGroupResponse zabbixHostGroupResponse : hostGroups) {
				if(zabbixHostGroupResponse.getName().equalsIgnoreCase(hostGroupName)){
					return 	hostGroupId = zabbixHostGroupResponse.getGroupid();
				}
			}
			throw new NamingException("Error this hostgroup is listed in zabbix server");
		}
		//In case the List Has been retrieved from calling object (in order to save number of calls) 
		else {
			for(ZabbixHostGroupResponse zabbixHostGroupResponse : groups) {
				if(zabbixHostGroupResponse.getName().equalsIgnoreCase(hostGroupName)){
					return	hostGroupId = zabbixHostGroupResponse.getGroupid();
				}
			}
			throw new NamingException("Error this hostgroup is listed in zabbix server");
		}
	}
}
