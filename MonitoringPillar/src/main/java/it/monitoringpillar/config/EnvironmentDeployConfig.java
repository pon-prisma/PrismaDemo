package it.monitoringpillar.config;


import it.monitoringpillar.config.PathHelper.ResourceType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;

@Stateless
public class EnvironmentDeployConfig {

	@Inject
	private MonitoringConfig monitConf;

	private static final org.apache.logging.log4j.Logger LOG = LogManager
			.getLogger(EnvironmentDeployConfig.class);

	private Properties svcEndpoinsProperties;

	Set<Entry<Object, Object>> propes;
	Properties props;

	private Map<String, Testbed> testbeds;

	public final String varResourceProfilesBasePath = PathHelper
			.getResourcesPath(ResourceType.CONFIG_ENV_VARIABLE_PROPERTIES_PROFILES);


	public final String varResourceBasePath = PathHelper
			.getResourcesPath(ResourceType.CONFIG_ENV_VARIABLE_PROPERTIES);

	public final String resourceBasePath = PathHelper
			.getResourcesPath(ResourceType.CONFIG_PROPERTIES);
	//			("C:\\jboss-as-7.1.1.Final\\standalone\\deployments\\MIUR_PRISMA-2.1-MonitoringPillar.war\\WEB-INF\\classes\\var-configs\\");

	public enum EnvironmentNames {
		INFN, REPLY, REPLY_DEV, SIELTE, LOCALHOST
	}

	protected String envProfile;

	public String getEnvProfile() {
		return envProfile;
	}

	public void setEnvProfile(String env) {
		this.envProfile = env;
	}

	@PostConstruct
	private void init() throws NamingException, IOException {

		// Forward proxy system property
		if (System.getenv("java.net.useSystemProxies") != null)
			System.setProperty("java.net.useSystemProxies",
					System.getenv("java.net.useSystemProxies"));

		LOG.warn("Pillar Started Logging !");
		try {
			// Try to get Environment name from server's hostname
			String hostName = null;
			try {
				hostName = InetAddress.getLocalHost().getCanonicalHostName();
			} catch (UnknownHostException uhe) {
				// TODO Improve
				hostName = uhe.getMessage().split(":")[0];
			}

			String envName = null;
			for (EnvironmentNames name : EnvironmentNames.values()) {
				if (hostName.contains(name.toString().toLowerCase())) {
					envName = name.toString().toLowerCase();
					break;
				}
			}

			// TODO: Temporary hard-coded configuration for development hosts
			if (hostName == null || hostName.contains("grandolfo") || hostName.contains("SAN") || hostName.contains("Mike"))
				envName = EnvironmentNames.INFN.toString().toLowerCase();
			
			if( hostName.contains("SAN"))
				envName = EnvironmentNames.REPLY_DEV.toString().toLowerCase();
			
			
			if (envName != null) {
				envProfile = envName;
				LOG.info("ENVIRONMENT AUTO-DISCOVERY: " + envName
						+ " environment detected ! Using " + envProfile
						+ " profile.");
			} else
				LOG.warn("ENVIRONMENT AUTO-DISCOVERY: environment detection failed ! Using default ("
						+ envProfile + ") profile.");

			// Load properties
			loadServicesEndpointsProperties(true);

			// Check loaded
			if (!envProfile
					.equalsIgnoreCase(getSvcEndpointProperty(SVCEP_ENV_NAME)))
				throw new Exception(
						"Environment properties name mismatch (found '"
								+ getSvcEndpointProperty(SVCEP_ENV_NAME)
								+ "', expected '"
								+ envProfile
								+ "'). Check 'environment_name' properties in services_endpoint.properties file. It MUST match the profile folder name (not case sensitive).");

		} catch (Exception ex) {
			throw new Error("CANNOT LOAD ENVIRONMENT CONFIGURATION !", ex);
		}
	}


	private void loadServicesEndpointsProperties(boolean debug)
			throws IOException {
		try {
			// create and load default properties
			Properties props = new Properties();
			
			InputStream in = new FileInputStream(varResourceProfilesBasePath //varResourceBasePath
					+ envProfile + "/services-endpoints.properties");
			
			props.load(in);
			
			svcEndpoinsProperties = new Properties(props);

			in.close();

			LOG.info("EnvironmentConfig loaded successfully !!");

			if (debug)
				for (Entry<Object, Object> item : props.entrySet()) {
					LOG.info("[Services Endpoints] - K: " + item.getKey()
							+ ", V: " + item.getValue());
				}
		} catch (IOException e) {
			LOG.error("ERROR: Cannot load 'services-endpoints.properties'." + e.getMessage() );
			throw new Error("Cannot load 'services-endpoints.properties'");
		}
	}

	public static final String APP_TEST_SKIP_MONITORING = "APP_TEST_SKIP_MONITORING";

	public final static String APP_STARTUP_ENV_TEST_SKIP = "APP_STARTUP_ENV_TEST_SKIP";
	public final static String APP_TEST_SKIP_POST_DEPLOY_MONITORING_CHECK = "APP_TEST_SKIP_POST-DEPLOY_MONITORING_CHECK";
	public final static String APP_TEST_SKIP_POST_DEPLOY_MONITORING_ADD = "APP_TEST_SKIP_POST-DEPLOY_MONITORING_ADD";

	public final static String SVCEP_ENV_NAME = "environment_name";

	public final static String SVCEP_MONITORING_PILLAR_URL = "monitoring_URL";

	public final static String MONITORING_PILLAR_ZONE = "temporary_monitoring_pillar_zone";

	public final static String DEFAULT_AUTH_TOKEN = "DEFAULT_AUTH_TOKEN";
	public final static String DEFAULT_MONITORING_TOKEN = "DEFAULT_MONITORING_TOKEN";


	public String getSvcEndpointProperty(String name) {
		return svcEndpoinsProperties.getProperty(name);
	}

	public String getMonitoringIaaSURL() {
		return svcEndpoinsProperties.getProperty("server_zabbix_iaas_URL");
	}

	public String getMonitoringMetricsURL() {
		return svcEndpoinsProperties.getProperty("server_zabbix_metrics_URL");
	}

	public String getMonitoringWatcherURL() {
		return svcEndpoinsProperties.getProperty("server_zabbix_watcher_URL");
	}

	public String getMonitoringEnvironment() {
		return svcEndpoinsProperties.getProperty("environment_name");
	}

	public String getUsernameZabbixServer() {
		return svcEndpoinsProperties.getProperty("usernameZabbixServer");
	}

	public String getPswdZabbixServer() {
		return svcEndpoinsProperties.getProperty("pswdZabbixServer");
	}


	public MonitoringConfig monitoringConfig() {
		return monitConf;
	}
}
