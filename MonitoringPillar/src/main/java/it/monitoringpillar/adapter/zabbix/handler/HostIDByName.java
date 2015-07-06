package it.monitoringpillar.adapter.zabbix.handler;

import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.monitoringpillar.adapter.zabbix.metrics.ZabbixAdapterSetter;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV2_4;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;

@Stateless
public class HostIDByName implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private ZabbixAdapterSetter zabAdapSetter;


	/*
	 * GET HOST ID
	 */

	/**
	 * 
	 * @param testbed
	 * @param url
	 * @param token
	 * @param hostgroupName
	 * @param vmuuid
	 * @param tag_service
	 * @param hosts
	 * @return
	 * @throws Exception
	 * @throws NamingException
	 */
	@SuppressWarnings("unchecked")
	public String getHostID(
			String testbed, 
			String url, 
			String token,  
			String hostgroupName,
			String vmuuid,
			String tag_service, 
			ArrayList<ZabbixMonitoredHostsResponse> hosts
			) throws Exception, NamingException { 

		if (hosts==null || vmuuid==null ){
			ArrayList<ZabbixMonitoredHostsResponse>  hostinfo = new ArrayList<ZabbixMonitoredHostsResponse>();

			hostinfo = (ArrayList<ZabbixMonitoredHostsResponse>) zabAdapSetter.getMonitoredHosts(
					testbed,
					url,
					token,
					ZabbixMethods.HOST.getzabbixMethod(), 
					null,
					null, 
					null
					); 

			for (int i = 0; i < hostinfo.size(); i++) {
				String hostIdfromPillar = hostinfo.get(i).getHostid();
				String hostname = hostinfo.get(i).getName();
				if (vmuuid.equalsIgnoreCase(hostname)) {
					return  hostIdfromPillar;
				}
			}
			throw new NamingException("Wrong host, group, tag inserted or not existing in monitoring platform");
		}
		else {
			for (int i = 0; i < hosts.size(); i++) {
				String hostIdfromPillar = hosts.get(i).getHostid();
				String hostname = hosts.get(i).getName();
				if (vmuuid.equalsIgnoreCase(hostname)) {
					return  hostIdfromPillar;
				}
			}
			throw new NamingException("Wrong host, group, tag inserted or not existing in monitoring platform");
		}
	}
	
	
	public String getHostIDV2_4(
			String testbed, 
			String url, 
			String token,  
			String hostgroupName,
			String vmuuid,
			String tag_service, 
			List<ZabbixMonitoredHostResponseV2_4> hosts
			) throws Exception, NamingException { 

		if (hosts==null || vmuuid==null ){
			ArrayList<ZabbixMonitoredHostsResponse>  hostinfo = new ArrayList<ZabbixMonitoredHostsResponse>();

			hostinfo = (ArrayList<ZabbixMonitoredHostsResponse>) zabAdapSetter.getMonitoredHosts(
					testbed,
					url,
					token,
					ZabbixMethods.HOST.getzabbixMethod(), 
					null,
					null, 
					null
					); 

			for (int i = 0; i < hostinfo.size(); i++) {
				String hostIdfromPillar = hostinfo.get(i).getHostid();
				String hostname = hostinfo.get(i).getName();
				if (vmuuid.equalsIgnoreCase(hostname)) {
					return  hostIdfromPillar;
				}
			}
			throw new NamingException("Wrong host, group, tag inserted or not existing in monitoring platform");
		}
		else {
			for (int i = 0; i < hosts.size(); i++) {
				String hostIdfromPillar = hosts.get(i).getHostid();
				String hostname = hosts.get(i).getName();
				if (vmuuid.equalsIgnoreCase(hostname)) {
					return  hostIdfromPillar;
				}
			}
			throw new NamingException("Wrong host, group, tag inserted or not existing in monitoring platform");
		}
	}
	
}