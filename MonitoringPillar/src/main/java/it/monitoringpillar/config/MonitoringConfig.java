package it.monitoringpillar.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.ejb.Startup;
import javax.inject.Singleton;

@Startup
@Singleton
public class MonitoringConfig implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//Machine's names
	public final static String MONITORINGMACHINEAS = "pillarmachine";

	public final static String BLMACHINE = "businesslayermachine";

	public final static String MAILMACHINE = "mailmachine";

	public final static String PUBLREPOMACHINE = "repomachine";

	public final static String DNSMACHINE = "dnsmachine";

	public final static String PUPPETMACHINE = "puppetmachine";

	public final static String CLOUDIFYSHMACHINE = "cloudifyshmachine";

	public final static String BL02MACHINE= "businesslayermachine02";

	public final static String WWWMACHINE = "webuimachine";

	public final static String DBMACHINE = "dbservermachine";

	public final static String SMSMACHINE = "smsmachine";

	public final static String JBCAMACHINE = "jbcamachine";

	public final static String PRISMA_IAAS_EXTERNALSCRIPT = "iaasExternalScript";

	//Atomic Services
	public final static String MYSQLSERVICEAtomic = "mysql";

	public final static String JBOSSSERVICEAtomic = "jboss";

	public final static String APACHESERVICEAtomic = "apache";

	public final static String TOMCATSERVICEAtomic = "tomcat";

	public final static String GLASSFISHSERVICEAtomic = "glassfish";

	public final static String RABBITMQSERVICEAtomic = "rabbitmq";

	public final static String X2GOSERVERSERVICEAtomic = "x2goserver";

	public final static String POSTGRESERVICEAtomic = "postgresql";
	
	public final static String HAPROXYSERVICEAtomic = "haproxy";
	
	public final static String PENTAHOSERVICEAtomic = "pentaho";

	public final static String IAASSERVICEAtomic = "iaas";
	
	public final static String VMAASSERVICEAtomic = "vmaas";


	//Zabbix Templates
	public final static String IAASTemplate = "templateiaas";

	public final static String MYSQLTemplate = "templatemysql";

	public final static String APACHETemplate = "templateapache";

	public final static String POSTGRETemplate = "templatepostgre";

	public final static String RabbitTemplate = "templaterabbit";

	public final static String X2GOTemplate = "templatex2go";

	public final static String TOMCATTemplate = "templatetomcat";

	public final static String JBOSSTemplate = "templatejboss";

	public final static String GLASSFISHTemplate = "templateglassfish";
	
	public final static String HAPROXYTemplate = "templatehaproxy";
	
	public final static String PENTAHOTemplate = "templatepentaho";
	

	//Services Categories

	public final static String APPAASSERVICECategory = "AppaaS";

	public final static String DBAASSERVICECategory = "DBaaS"; 

	public final static String MQAASSERVICECategory = "MQaaS";

	public final static String VMAASSERVICECategory = "VMIaaS";
	
	public final static String BUSINESSINTELLIGENCEServiceCategory = "BIaaS";
	
	public final static String BUSINESSPROCESSMGMServiceCategory = "BPMaaS";
	
	public final static String CMSServiceCategory = "CMSaaS";
	
	public final static String OPENDATAServiceCategory = "OpenDataaS";


	//Zabbix IaaS goups

	public final static String COMPUTEIaaSGRoupName = "compute";

	public final static String CONTROLLERIaaSGroupName = "controller";

	public final static String NETWORKIaaSGRoupName = "network";

	public final static String IAASZabbixIaaSGRoupName = "iaas";  


	private Properties svcEndpointsProperties;


	public  MonitoringConfig() throws IOException  {
		loadProperties();

	}

	private void loadProperties() throws IOException {
		try {
			// create and load default properties
			Properties props = new Properties();
			String filepath = getClass()
					.getProtectionDomain()
					.getCodeSource().getLocation().getPath()
					+ "/";

			//String filePathCorrected = filepath.substring(1)+"";
			FileInputStream in = new FileInputStream(filepath
											+ "monitoring-data.properties");
			props.load(in);
			svcEndpointsProperties = new Properties(props);

			in.close();
//			System.out.println("successully loaded");
			System.out.flush();
		} catch (IOException e) {
			System.out
			.println("ERROR: Cannot load 'monitoring-data.properties'.");
		}
	}


	/*
	 * Get Machine Names
	 */
	/**
	 * 
	 * @return pr01monit01
	 */
	public String getMonitoringMachineAS(){
		return svcEndpointsProperties.getProperty(MONITORINGMACHINEAS);
	}
	/**
	 * 
	 * @return pr01biz01
	 */
	public String getBLMachineAS(){
		return svcEndpointsProperties.getProperty(BLMACHINE);
	}
	/**
	 * 
	 * @return pr01mail01
	 */
	public String getMailMachine(){
		return svcEndpointsProperties.getProperty(MAILMACHINE);
	}
	/**
	 * 
	 * @return pr01publicrepo01
	 */
	public String getRepoMachine(){
		return svcEndpointsProperties.getProperty(PUBLREPOMACHINE);
	}
	/**
	 * 
	 * @return pr01dns01
	 */
	public String getDNSMachine(){
		return svcEndpointsProperties.getProperty(DNSMACHINE);
	}
	/**
	 * 
	 * @return puppet-orchestrator
	 */
	public String getPuppetMachine(){
		return svcEndpointsProperties.getProperty(PUPPETMACHINE);
	}
	/**
	 * 
	 * @return pr01cloudifysh01
	 */
	public String getCloudifyMachine(){
		return svcEndpointsProperties.getProperty(CLOUDIFYSHMACHINE);
	}
	/**
	 * 
	 * @return pr01biz02
	 */
	public String getBL02Machine(){
		return svcEndpointsProperties.getProperty(BL02MACHINE);
	}
	/**
	 * 
	 * @return pr01www01
	 */
	public String getWEBUIMachine(){
		return svcEndpointsProperties.getProperty(WWWMACHINE);
	}
	/**
	 * 
	 * @return db-server
	 */
	public String getDBServerMachine(){
		return svcEndpointsProperties.getProperty(DBMACHINE);
	}
	/**
	 * 
	 * @return pr01servplaysms01
	 */
	public String getSMSMachine(){
		return svcEndpointsProperties.getProperty(SMSMACHINE);
	}
	/**
	 * 
	 * @return pr01servejbca01
	 */
	public String getJBCAMachine(){
		return svcEndpointsProperties.getProperty(JBCAMACHINE);
	}
	/**
	 * 
	 * @return Prisma_IaaS
	 */
	public String getExternalScriptHostMachine(){
		return svcEndpointsProperties.getProperty(PRISMA_IAAS_EXTERNALSCRIPT);
	}

	/*
	 * Get Atomic Service Names
	 */
	/**
	 * 
	 * @return mysql
	 */
	public String getMysqlAtomicService(){
		return svcEndpointsProperties.getProperty(MYSQLSERVICEAtomic);
	}
	/**
	 * 
	 * @return jboss
	 */
	public String getJBossAtomicService(){
		return svcEndpointsProperties.getProperty(JBOSSSERVICEAtomic);
	}
	/**
	 * 
	 * @return apache
	 */
	public String getApacheAtomicService(){
		return svcEndpointsProperties.getProperty(APACHESERVICEAtomic);
	}
	/**
	 * 
	 * @return tomcat
	 */
	public String getTomcatAtomicService(){
		return svcEndpointsProperties.getProperty(TOMCATSERVICEAtomic);
	}
	/**
	 * 
	 * @return rabbitmq
	 */
	public String getRabbitMQAtomicService(){
		return svcEndpointsProperties.getProperty(RABBITMQSERVICEAtomic);
	}
	/**
	 * 
	 * @return x2goserver
	 */
	public String getX2GoServerAtomicService(){
		return svcEndpointsProperties.getProperty(X2GOSERVERSERVICEAtomic);
	}
	/**
	 * 
	 * @return postgresql
	 */
	public String getPostgreAtomicService(){
		return svcEndpointsProperties.getProperty(POSTGRESERVICEAtomic);
	}
	/**
	 * 
	 * @return glassfish
	 */
	public String getGlassfishAtomicService(){
		return svcEndpointsProperties.getProperty(GLASSFISHSERVICEAtomic);
	}
	/**
	 * 
	 * @return haproxy
	 */
	public String getHAPROXYAtomicService(){
		return svcEndpointsProperties.getProperty(HAPROXYSERVICEAtomic);
	}
	/**
	 * 
	 * @return pentaho
	 */
	public String getPENTAHOAtomicService(){
		return svcEndpointsProperties.getProperty(PENTAHOSERVICEAtomic);
	}
	/**
	 * 
	 * @return vmaas
	 */
	public String getIaaSAtomicService(){
		return svcEndpointsProperties.getProperty(IAASSERVICEAtomic);
	}
	
	public String getVMaaSAtomicService(){
		return svcEndpointsProperties.getProperty(VMAASSERVICEAtomic);
	}
	/*
	 * Get Zabbix Templates
	 */
	/**
	 * 
	 * @return TemplateIaaS
	 */
	public String getIaaSTemplate(){
		return svcEndpointsProperties.getProperty(IAASTemplate);
	}
	/**
	 * 
	 * @return TemplateMySQL
	 */
	public String getMysqlTemplate(){
		return svcEndpointsProperties.getProperty(MYSQLTemplate);
	}
	/**
	 * 
	 * @return TemplateJboss
	 */
	public String getJbossTemplate(){
		return svcEndpointsProperties.getProperty(JBOSSTemplate);
	}
	/**
	 * 
	 * @return TemplateApache
	 */
	public String getApacheTemplate(){
		return svcEndpointsProperties.getProperty(APACHETemplate);
	}
	/**
	 * 
	 * @return TemplatePostgreSQL
	 */
	public String getPostgreTemplate(){
		return svcEndpointsProperties.getProperty(POSTGRETemplate);
	}
	/**
	 * 
	 * @return TemplateRabbitMQ
	 */
	public String getRabbitMQTemplate(){
		return svcEndpointsProperties.getProperty(RABBITMQSERVICEAtomic);
	}
	/**
	 * 
	 * @return TemplateX2goServer
	 */
	public String getX2GoServerTemplate(){
		return svcEndpointsProperties.getProperty(X2GOTemplate);
	}
	/**
	 * 
	 * @return TemplateTomcat
	 */
	public String getTomcatemplate(){
		return svcEndpointsProperties.getProperty(TOMCATTemplate);
	}
	/**
	 * 
	 * @return TemplateGlaasFish
	 */
	public String getGlassFishTemplate(){
		return svcEndpointsProperties.getProperty(GLASSFISHTemplate);
	}
	/**
	 * 
	 * @return TemplateHAProxy
	 */
	public String getHAProxyTemplate(){
		return svcEndpointsProperties.getProperty(HAPROXYTemplate);
	}
	/**
	 * 
	 * @return TemplatePentaho
	 */
	public String getPENTAHOTemplate(){
		return svcEndpointsProperties.getProperty(PENTAHOTemplate);
	}

	/*
	 * Get Services' CAtegories
	 */
	/**
	 * 
	 * @return DBaaS
	 */
	public String getDBServiceCategory(){
		return svcEndpointsProperties.getProperty(DBAASSERVICECategory);
	}
	/**
	 * 
	 * @return AppaS
	 */
	public String getAppaaSServiceCategory(){
		return svcEndpointsProperties.getProperty(APPAASSERVICECategory);
	}
	/**
	 * 
	 * @return MQaaS
	 */
	public String getMQaaSServiceCategory(){
		return svcEndpointsProperties.getProperty(MQAASSERVICECategory);
	}
	/**
	 * 
	 * @return VMaaS
	 */
	public String getVMaaSServiceCategory(){
		return svcEndpointsProperties.getProperty(VMAASSERVICECategory);
	}	
	/**
	 * 
	 * @return BIaaS
	 */
	public String getBIaaSServiceCategory(){
		return svcEndpointsProperties.getProperty(BUSINESSINTELLIGENCEServiceCategory);
	}	
	/**
	 * 
	 * @return BPMaaS
	 */
	public String getBPMServiceCategory(){
		return svcEndpointsProperties.getProperty(BUSINESSPROCESSMGMServiceCategory);
	}
	/**
	 * 
	 * @return OpendataaaS
	 */
	public String getOpenDataServiceCategory(){
		return svcEndpointsProperties.getProperty(OPENDATAServiceCategory);
	}
	/**
	 * 
	 * @return CMSaaS
	 */
	public String getCMSServiceCategory(){
		return svcEndpointsProperties.getProperty(CMSServiceCategory);
	}

	/*
	 * Get ZAbbix IaaS groupNames
	 */
	/**
	 * 
	 * @return Compute
	 */
	public String getComputeIaaSGroupName(){
		return svcEndpointsProperties.getProperty(COMPUTEIaaSGRoupName);
	}
	/**
	 * 
	 * @return Controller
	 */
	public String getControllerIaaSGroupName(){
		return svcEndpointsProperties.getProperty(CONTROLLERIaaSGroupName);
	}
	/**
	 * 
	 * @return Network
	 */
	public String getNetworkIaaSGroupName(){
		return svcEndpointsProperties.getProperty(NETWORKIaaSGRoupName);
	}
	/**
	 * 
	 * @return IaaS
	 */
	public String getIAASZabbixIaaSGroupName(){
		return svcEndpointsProperties.getProperty(IAASZabbixIaaSGRoupName);
	} 



	/*
	 * Get IaaS URLs for each Testbed
	 */

	public String getURLMonitoringAdapterPRISMAIaaS_REC() {

		return svcEndpointsProperties.getProperty("ZabbixServerPrisma_REC");

	}

	public String getURLMonitoringAdapterPRISMAIaaS_INFN() {

		return svcEndpointsProperties.getProperty("ZabbixServerPrisma_INFN");

	}

	public String getURLMonitoringAdapterPRISMAIaaS_SIELTE() {

		return svcEndpointsProperties.getProperty("ZabbixServerPrismaIaaS_SIELTE");

	}

	/*
	 * Get Metrics URLs for each Testbed
	 */

	public String getURLMonitoringAdapterPRISMAMetrics_REC(){

		return svcEndpointsProperties.getProperty("ZabbixServerPrismaMetrics_REC");

	}


	public String getURLMonitoringAdapterPRISMAMetrics_INFN(){

		return svcEndpointsProperties.getProperty("ZabbixServerPrismaMetrics_INFN");

	}

	public String getURLMonitoringAdapterPRISMAMetrics_SIELTE(){

		return svcEndpointsProperties.getProperty("ZabbixServerPrismaMetrics_SIELTE");

	}

	/*
	 * Get Usr for each Testbed
	 */

	public String getUsernameZabbixServer_REC(){
		return svcEndpointsProperties.getProperty("usernameZabbixServer_REC");
	}

	public String getUsernameZabbixServer_SIELTE(){
		return svcEndpointsProperties.getProperty("usernameZabbixServer_SIELTE");
	}

	public String getUsernameZabbixServer_INFN(){
		return svcEndpointsProperties.getProperty("usernameZabbixServer_INFN");
	}

	/*
	 * Get Usr and Pswd for each Testbed
	 */

	public String getPasswordZabbixServer_REC(){
		return svcEndpointsProperties.getProperty("pswdZabbixServer_REC");
	}

	public String getPasswordZabbixServer_SIELTE(){
		return svcEndpointsProperties.getProperty("pswdZabbixServerIaaS_SIELTE"); //to change when sielte will confirm a unique pswd
	}

	public String getPasswordZabbixServer_INFN(){
		return svcEndpointsProperties.getProperty("pswdZabbixServer_INFN");
	}

	/*
	 * Get Flavors
	 */
	public String getFlavorSMALL(){
		return svcEndpointsProperties.getProperty("FLAVOR_SMALL");
	}
	public String getFlavorMEDIUM(){
		return svcEndpointsProperties.getProperty("FLAVOR_MEDIUM");
	}
	public String getFlavorLARGE(){
		return svcEndpointsProperties.getProperty("FLAVOR_LARGE");
	}


}



