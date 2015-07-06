package it.monitoringpillar;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MonitoringAction {
	
	public enum MonitoringActionList  { 
		 create_monitored_host, 
	     delete_monitored_host,
	     disable_monitored_machine 
	}

	MonitoringActionList actionlist;

	public enum Adapters{
		
		ZABBIX, NAGIOS
	}

	public static Map<String, Object> serviceMap;


	public static Map<String, Object> MonitoringService() throws IOException  {

		Map<String, Object> services = new HashMap<String, Object>();
		return serviceMap = Collections.unmodifiableMap(services);	
	}
}