package it.monitoringpillar.config;

import it.monitoringpillar.MonitoringAdapter;
import it.monitoringpillar.adapter.nagios.IMonitAdaptNagios;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.Adapter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;


//@Singleton
//@Startup
@Stateless
public class DetermineAdapter {

	@Inject MonitoringAdapter monitAdaptZabbix;
	@Inject @IMonitAdaptNagios MonitoringAdapter monitAdaptNagios;

	public MonitoringAdapter getAdapter(String adapter) throws NamingException{
	
		if (adapter.equals(Adapter.ZABBIX.toString().toLowerCase())){
			return monitAdaptZabbix;
		}
		else if (adapter.equals(Adapter.NAGIOS.toString().toLowerCase())){
			return monitAdaptNagios;
		}
		else throw new NamingException("Not Implemented Adapter");
	}
}