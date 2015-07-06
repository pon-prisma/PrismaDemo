package it.monitoringpillar.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless
public class PillarEnvironmentConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String REC = "rec";
	private final String INFN = "infn";
	private final String SIELTE = "sielte";
	
	private Properties svcEndpointsProperties;
	Set<Entry<Object,Object>> propes;
	Properties props;
	
	@PostConstruct
	public void init() throws IOException {
		loadServicesEndpointsProperties();
	}

	private void loadServicesEndpointsProperties() throws IOException {
		try {
			// create and load default properties
			Properties props = new Properties();
			String filepath = this.getClass()
					.getProtectionDomain()
					.getCodeSource().getLocation().getPath()
					+ "/";
			FileInputStream in = new FileInputStream(filepath
					+ "environments-data-monitoring.properties");
			props.load(in);
			svcEndpointsProperties = new Properties(props);

			in.close();

			System.out.flush();
		} catch (IOException e) {
			System.out
			.println("ERROR: Cannot load 'environment-data-monitoring.properties'.");
		}
	}

//	/********************
//	 * Environment Types 
//	 ********************/
//	public String getRECEnv() {
//		return svcEndpointsProperties.getProperty(REC);
//	}
//	public String getINFNEnv() {
//		return svcEndpointsProperties.getProperty(INFN);
//	}
//	public String getSIELTEEnv() {
//		return svcEndpointsProperties.getProperty(SIELTE);
//	}
//	
///*
// * Get URLs for each Testbed
// */
//	
//	/*
//	 * Get Metrics URLs for each Testbed
//	 */
//
//	/********
//	 * REC
//	 *******/
//	public String getURLMonitoringAdapterPRISMAIaaS_REC() {
//		return svcEndpointsProperties.getProperty("ZabbixServerPrismaIaaS_REC");
//	}
//	
//	public String getURLMonitoringAdapterPRISMAMetrics_REC(){
//		return svcEndpointsProperties.getProperty("ZabbixServerPrismaMetrics_REC");
//	}
//	
//	/********
//	 * INFN
//	 *******/
//	public String getURLMonitoringAdapterPRISMAIaaS_INFN() {
//		return svcEndpointsProperties.getProperty("ZabbixServerPrismaIaaS_INFN");
//	}
//	
//	public String getURLMonitoringAdapterPRISMAMetrics_INFN(){
//		return svcEndpointsProperties.getProperty("ZabbixServerPrismaMetrics_INFN");
//	}
//	
//	public String getURLMonitoringAdapterPRISMAWatcher_INFN(){
//		return svcEndpointsProperties.getProperty("ZabbixServerPrismaWatcher_INFN");
//	}
//	
//	/********
//	 * SIELTE
//	 ********/
//	public String getURLMonitoringAdapterPRISMAIaaS_SIELTE() {
//		return svcEndpointsProperties.getProperty("ZabbixServerPrismaIaaS_SIELTE");
//	}
//	
//	public String getURLMonitoringAdapterPRISMAMetrics_SIELTE(){
//		return svcEndpointsProperties.getProperty("ZabbixServerPrismaMetrics_SIELTE");
//	}
//	
//	/*
//	 * Get Usr for each Testbed
//	 */
//	
//	public String getUsernameZabbixServer_REC(){
//		 return svcEndpointsProperties.getProperty("usernameZabbixServer_REC");
//	}
//	
//	public String getUsernameZabbixServer_SIELTE(){
//		 return svcEndpointsProperties.getProperty("usernameZabbixServer_SIELTE");
//	}
//	
//	public String getUsernameZabbixServer_INFN(){
//		 return svcEndpointsProperties.getProperty("usernameZabbixServer_INFN");
//	}
//	
//	/*
//	 * Get Usr and Pswd for each Testbed
//	 */
//	
//	public String getPasswordZabbixServer_REC(){
//		 return svcEndpointsProperties.getProperty("pswdZabbixServer_REC");
//	}
//	
//	public String getPasswordZabbixServer_SIELTE(){
//		 return svcEndpointsProperties.getProperty("pswdZabbixServer_SIELTE"); 
//	}
//	
//	public String getPasswordZabbixServer_INFN(){
//		 return svcEndpointsProperties.getProperty("pswdZabbixServer_INFN");
//	}

	/***************
	 * Server Types
	 ***************/
	
	public String getZabbixServer_IaaS(){
		 return svcEndpointsProperties.getProperty("iaas");
	}
	
	public String getZabbixServer_Metrics(){
		 return svcEndpointsProperties.getProperty("metrics"); 
	}
	
	public String getZabbixServer_Watcher(){
		 return svcEndpointsProperties.getProperty("watcher");
	}
	

	/*
	 * Get Monitoring Metrics from Configuration File 
	 */

	public String getPath2ConfMetricsLocal(){
		return svcEndpointsProperties.getProperty("Path_ConfigurationFileMetricsLocal");
	}
	public String getPath2ConfMetricsOfficialServer(){
		return svcEndpointsProperties.getProperty("Path_ConfigurationFileMetricsOfficial");
	}
	
		
	
}