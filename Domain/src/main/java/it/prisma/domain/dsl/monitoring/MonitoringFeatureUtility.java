package it.prisma.domain.dsl.monitoring;

public class MonitoringFeatureUtility {

    /**
     * Enum for Adapter
     * 
     */
    public enum Adapter {

	ZABBIX("zabbix"), NAGIOS("nagios");

	private Adapter(String adapter) {
	    this.adapter = adapter;
	}

	private String adapter;

	public String getAdapter() {
	    return this.adapter;
	}

	public String toString() {
	    return this.adapter;
	}
    }

    // public enum MonitoringService {
    // APACHE("apache"), GLASSFISH("glassfish"), HA_PROXY("haproxy"),
    // IAAS("iaas"), JBOSS("jboss"), MYSQL("mysql"), PENTAHO(
    // "pentaho"), POSTGRESQL("postgresql"), RABBIT_MQ("rabbitmq"),
    // TOMCAT("tomcat"), X2GO("x2go");
    //
    // private String service;
    //
    // private MonitoringService(String service) {
    // this.service = service;
    // }
    //
    // public String getService() {
    // return this.service;
    // }
    //
    // public String toString() {
    // return this.service;
    // }
    //
    // public static MonitoringService lookupFromName(String service) throws
    // IllegalArgumentException {
    // for (MonitoringService v : values()) {
    // if (service.equals(v.getService())) {
    // return v;
    // }
    // }
    // throw new IllegalArgumentException("Cannot find " + service +
    // " in MonitoringService");
    // }
    // }

    // public enum MonitoringMetric {
    //
    // // APACHE SERVICE HAS THESE METRICS:
    // APACHE_PROCESS("APACHEProcessRunning"),
    // APACHE_PORT("APACHEProcessPortListening"),
    //
    // // GLASSFISH SERVICE HAS THESE METRICS:
    // GLASSFISH_PROCESS("GLASSFISHProcessRunning"),
    //
    // // HAPROXY SERVICE HAS THESE METRICS:
    // HAPROXY_PROCESS("HAPROXYProcessRunning"),
    //
    // //IAAS SERVICE HAS THESE METRICS:
    // SSH_CONNECTION("IaasSSHConnection"), // Only this metric is used to check
    // is service is running
    // CPU_LOAD("IaaSCeilometerCPULOAD"),
    // RAM("IaaSCeilometerMEMORYRAM"),
    // UPTIME("IaaSCeilometerUPTIME"),
    // STORAGE("IaaSCeilometerSTORAGE"),
    // NETWORKIN("IaaSCeilometerNETWORKIN"),
    // NETWORKOUT("IaaSCeilometerNETWORKOUT"),
    //
    // //JBOSS SERVICE HAS THESE METRICS:
    // JBOSS_PROCESS("JBOSSProcessRunning"),
    // JBOSS_PORT("jbossASProcessPortListening"),
    //
    // //MYSQL SERVICE HAS THESE METRICS:
    // MYSQL_PROCESS("MYSQLProcessRunning"),
    // MYSQL_PORT("MYSQLProcessPortListening"),
    //
    // //PENTAHO SERVER SERVICE HAS THESE METRICS:
    // PENTAHO_PROCESS("PENTAHOProcessRunning"),
    //
    // //POSTGRESQL SERVICE HAS THESE METRICS:
    // POSTGRESSQL_PROCESS("POSTGRESQLProcessRunning"),
    // POSTGRESSQL_PORT("POSTGRESQLProcessPORTListening"),
    //
    // //RABBITMQL SERVICE HAS THESE METRICS:
    // RABBITMQ_PROCESS("RABBITMQProcessRunning"),
    // RABBITMQ_PORT("RABBITMQProcessPORTListening"),
    //
    // //TOMCAT SERVICE HAS THESE METRICS:
    // TOMCAT_PROCESS("TOMCATProcessRunning"),
    // TOMCAT_PORT("TOMCATProcessPortListening"),
    //
    // //X2GO (or "PENTAHO CLIENT") SERVICE HAS THESE METRICS:
    // X2GO_PROCESS("X2GOProcessRunning"),
    //
    // ;
    //
    // private String metric;
    //
    // private MonitoringMetric(String metric) {
    // this.metric = metric;
    // }
    //
    // public String getMetric() {
    // return this.metric;
    // }
    //
    // public String toString() {
    // return this.metric;
    // }
    //
    // public static MonitoringMetric lookupFromName(String metric) throws
    // IllegalArgumentException {
    // for (MonitoringMetric v : values()) {
    // if (metric.equals(v.getMetric())) {
    // return v;
    // }
    // }
    // throw new IllegalArgumentException("Cannot find " + metric +
    // " in MonitoringMetric");
    // }
    // }



    public enum MonitoringMetricsIAASNames {

	PRISMA_IAAS_SCRIPT("Prisma IaaS"), HYPERVISOR_IAAS_SCRIPT("Hypervisor show");

	private MonitoringMetricsIAASNames(String zabbixMetricsIAASNames) {
	    this.zabbixMetricsIAASNames = zabbixMetricsIAASNames;
	}

	private String zabbixMetricsIAASNames;

	public String getzabbixMetricsIAASNames() {
	    return this.zabbixMetricsIAASNames;
	}

	public String toString() {
	    return this.zabbixMetricsIAASNames;
	}
    }

    public enum IaaSHostGroups {

	COMPUTE("Compute"), CONTROLLER("Controller"), NETWORK("Network"), IAAS("IaaS"), INFRASTRUCTURE_HEALTH(
		"InfrastructureHealth");

	private String groupName;

	private IaaSHostGroups(String groupName) {
	    this.groupName = groupName;
	}

	public String getGroupName() {
	    return groupName;
	}

	public static IaaSHostGroups lookupFromName(String groupName) {
	    for (IaaSHostGroups v : values()) {
		if (groupName.equals(v.getGroupName())) {
		    return v;
		}
	    }
	    throw new IllegalArgumentException("Cannot find " + groupName + " in HostGroups");
	}

    }

    public enum HostIaaS {

	PRISMA_IAAS("Prisma_IaaS");

	private String hostName;

	private HostIaaS(String hostName) {
	    this.hostName = hostName;
	}

	public String getHostName() {
	    return hostName;
	}

	public HostIaaS lookupFromName(String hostName) {
	    for (HostIaaS v : values()) {
		if (hostName.equals(v.name())) {
		    return v;
		}
	    }
	    throw new IllegalArgumentException("Cannot find " + hostName + " in HostName Enum");
	}
    }

    public enum InfrastructureBareMetalMetric {

	NETWORK("network"), STORAGE("storage"), AVAILABLE_NODES("availableNodes"), TOTAL_NODES("totalNodes");

	private String metric;

	private InfrastructureBareMetalMetric(String metric) {
	    this.metric = metric;
	}

	public String getMetric() {
	    return metric;
	}

	public InfrastructureBareMetalMetric lookupFromName(String metric) {
	    for (InfrastructureBareMetalMetric v : values()) {
		if (metric.equals(v.name())) {
		    return v;
		}
	    }
	    throw new IllegalArgumentException("Cannot find " + metric + " in InfrastructureBareMetalMetric Enum");
	}
    }

    public enum InfrastructureBareMetalStatus {

	OK("ok", 0.66f), WARNING("warning", 0.33f), PROBLEM("problem", 0.0f);

	private String status;
	private float level;

	private InfrastructureBareMetalStatus(String status, float level) {
	    this.status = status;
	    this.level = level;
	}

	public String getStatus() {
	    return status;
	}

	public float getLevel() {
	    return level;
	}

	public InfrastructureBareMetalStatus lookupFromName(String status) {
	    for (InfrastructureBareMetalStatus v : values()) {
		if (status.equals(v.name())) {
		    return v;
		}
	    }
	    throw new IllegalArgumentException("Cannot find " + status + " in InfrastructureBareMetalStatus Enum");
	}
    }

}