package it.prisma.businesslayer.config;

public class Constants {

	// Testbeds' Names
	public final static String RECTESTBED = "Rec";
	public final static String INFNTESTBED = "Infn";
	public final static String SIELTETESTBED = "Sielte";
	
	//Pillar Urls names
	
	public final static String RECURLPILLAR = "RECMonitoringPillarURL";
	public final static String INFNURLPILLAR = "INFNMonitoringPillarURL";
	public final static String SIELTEURLPILLAR = "SIELTEMonitoringPillarURL";

	// Machine's names
	public final static String MONITORINGMACHINEAS = "pillarmachine";

	public final static String BLMACHINE = "businesslayermachine";

	public final static String MAILMACHINE = "mailmachine";

	public final static String PUBLREPOMACHINE = "repomachine";

	public final static String DNSMACHINE = "dnsmachine";

	public final static String PUPPETMACHINE = "puppetmachine";

	public final static String CLOUDIFYSHMACHINE = "cloudifyshmachine";

	public final static String BL02MACHINE = "businesslayermachine02";

	public final static String WWWMACHINE = "webuimachine";

	public final static String DBMACHINE = "dbservermachine";

	public final static String SMSMACHINE = "smsmachine";

	public final static String JBCAMACHINE = "jbcamachine";

	public final static String PRISMA_IAAS_EXTERNALSCRIPT = "iaasExternalScriptOS";
	
	public final static String PRISMA_HYPERVISOR_EXTERNALSCRIPT = "hypervisorExternalScriptOS";

	// Atomic Services
	public final static String VMaaSAtomic = "VMaaS";
	
	public final static String MYSQLSERVICEAtomic = "MySQL";

	public final static String JBOSSSERVICEAtomic = "Jboss";

	public final static String APACHESERVICEAtomic = "Apache";

	public final static String TOMCATSERVICEAtomic = "Tomcat";

	public final static String GLASSFISHSERVICEAtomic = "Glassfish";

	public final static String RABBITMQSERVICEAtomic = "RabbitMQ";

	public final static String X2GOSERVERSERVICEAtomic = "X2goServer";

	public final static String POSTGRESERVICEAtomic = "PostgreSQL";
	
	public final static String HAPROXYSERVICEAtomic = "HAProxy";

	public final static String IAASSERVICEAtomic = "IaaS";
		
	public final static String PENTAHOAtomicService = "Pentaho";	

	// Zabbix Templates
	public final static String IAASTemplate = "templateIaaS";

	public final static String MYSQLTemplate = "templatemysql";

	public final static String APACHETemplate = "templateapache";

	public final static String POSTGRETemplate = "templatepostgre";

	public final static String RabbitTemplate = "templaterabbit";

	public final static String X2GOTemplate = "templatex2go";

	public final static String TOMCATTemplate = "templatetomcat";

	public final static String JBOSSTemplate = "templatejboss";

	public final static String GLASSFISHTemplate = "TemplateGlassFish";
	
	public final static String HAPROXYTemplate= "TemplateHAProxy";

	// Services Categories

//	public final static String APPAASSERVICECategory = "appaas";
//
//	public final static String DBAASSERVICECategory = "dbaas";
//
//	public final static String MQAASSERVICECategory = "mqaas";
//
//	public final static String VMAASSERVICECategory = "vmaas";
//	
//	public final static String BIAASSERVICECategory = "biaas";
	

	// Zabbix IaaS goups
	public final static String COMPUTEIaaSGRoupName = "Compute";

	public final static String CONTROLLERIaaSGroupName = "Controller";

	public final static String NETWORKIaaSGRoupName = "Network";

	public final static String IAASZabbixIaaSGRoupName = "IaaS";
	
	public final static String INFRASTRUCTUREHEALTHGRoupName = "InfrastructureHealth";

}
