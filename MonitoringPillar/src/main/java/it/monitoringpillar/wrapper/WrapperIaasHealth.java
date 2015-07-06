package it.monitoringpillar.wrapper;

import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.monitoringpillar.adapter.zabbix.iaas.ZabbixAdapterIaas;
import it.monitoringpillar.config.TimestampMonitoring;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.HostAffected;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.TriggerShot;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class WrapperIaasHealth {

	@Inject
	private ZabbixAdapterIaas zabAdapIaas;

	@Inject
	private TimestampMonitoring timeConv;

	public WrappedIaasHealthByTrigger getTriggerforIaasHealth(String testbed,
			String url, String token, String group) throws Exception {

		WrappedIaasHealthByTrigger wrappedIaasHealthByTrigger = new WrappedIaasHealthByTrigger();
		
		//Put into the map all the host found by cycling on triggers
		Map<String, HostAffected> hostAffectedMap = new HashMap<String, HostAffected>();
		
		// It retrieves all shuttered triggers
		ArrayList<ZabbixItemResponse> triggers = zabAdapIaas
				.getTriggerWithProblems(testbed, url, token, null, null,
						ZabbixMethods.TRIGGER.getzabbixMethod(), group);

		
		for (ZabbixItemResponse zabbixItemResponse : triggers) {
			//get the host associated to trigger
			ArrayList<ZabbixMonitoredHostsResponse> hosts = zabAdapIaas
					.getMonitoredHostListinPrismaIaaS(testbed, url, token,
							ZabbixMethods.HOST.getzabbixMethod(), null, null,
							zabbixItemResponse.getTriggerid());
			
			//If the host is in the map just add it
			if(!hostAffectedMap.containsKey(hosts.get(0).getName())){
				HostAffected hostAffected = new HostAffected();
				hostAffected.setHostName(hosts.get(0).getName());
				hostAffected.setTriggerShots(new ArrayList<TriggerShot>());
				hostAffectedMap.put(hosts.get(0).getName(), hostAffected);
			}
			//set the trigger
			TriggerShot triggerShot = new TriggerShot();
			triggerShot.setTriggerId(zabbixItemResponse.getTriggerid());
			triggerShot.setDescription(zabbixItemResponse.getDescription());
			triggerShot.setExpression(zabbixItemResponse.getExpression());
			if (zabbixItemResponse.getLastchange() != null) {
				triggerShot.setTime(timeConv.decodUnixTime2Date(Long.parseLong(zabbixItemResponse.getLastchange())));
			} else
				triggerShot.setTime("0");
			//  associate it to the host
			hostAffectedMap.get(hosts.get(0).getName()).getTriggerShots().add(triggerShot);
			
		}
		wrappedIaasHealthByTrigger.setHostGroup(group);
		wrappedIaasHealthByTrigger.setHostAffecteds(new ArrayList<HostAffected>(hostAffectedMap.values()));
		return wrappedIaasHealthByTrigger;
	}
}
