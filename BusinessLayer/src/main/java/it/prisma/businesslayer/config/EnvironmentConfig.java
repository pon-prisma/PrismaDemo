package it.prisma.businesslayer.config;

import it.prisma.businesslayer.utils.PathHelper;
import it.prisma.businesslayer.utils.PathHelper.ResourceType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.ejbca.util.CryptoProviderTools;

@Startup
@Singleton
public class EnvironmentConfig {

	private static final org.apache.logging.log4j.Logger LOG = LogManager
			.getLogger(EnvironmentConfig.class);

	private Properties svcEndpoinsProperties;
	private Properties appProperties;
	private Properties mailProperties;
	private Properties securityProperties;
	private Properties openstackPresets;
	private Properties versionProperties;

	Properties props;

	private OpenStackPreset osPreset;

	public final String varResourceProfilesBasePath = PathHelper
			.getResourcesPath(ResourceType.CONFIG_ENV_VARIABLE_PROPERTIES_PROFILES);
	// this.getClass()
	// .getProtectionDomain().getCodeSource().getLocation().getPath()
	// + "/var-configs-profiles/";

	public final String varResourceBasePath = PathHelper
			.getResourcesPath(ResourceType.CONFIG_ENV_VARIABLE_PROPERTIES);
	// this.getClass()
	// .getProtectionDomain().getCodeSource().getLocation().getPath()
	// + "/var-configs/";
	public final String resourceBasePath = PathHelper
			.getResourcesPath(ResourceType.CONFIG_PROPERTIES);

	// this.getClass()
	// .getProtectionDomain().getCodeSource().getLocation().getPath()
	// + "/";

	public enum EnvironmentNames {
		DEFAULT, INFN, REPLY, REPLY_DEV, SIELTE
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

		LOG.warn("BizLayer Started Logging on DB !");
		try {
			// Explicit environment name in properties
			String envName = null;
			envName = System.getProperty("prisma.environment");

			// Try to get Environment name from server's hostname
			if (envName == null) {
				String hostName = null;
				try {
					hostName = InetAddress.getLocalHost()
							.getCanonicalHostName();
				} catch (UnknownHostException uhe) {
					// TODO Improve
					hostName = uhe.getMessage().split(":")[0];
				}

				for (EnvironmentNames name : EnvironmentNames.values()) {
					if (hostName.contains(name.toString().toLowerCase())) {
						envName = name.toString().toLowerCase();
						break;
					}
				}

				// TODO: Temporary hard-coded configuration for development
				// hosts
				if (hostName.contains("jbpm02") || hostName.contains("SAN"))
					envName = EnvironmentNames.REPLY_DEV.toString()
							.toLowerCase();
			}

			envProfile = EnvironmentNames.DEFAULT.toString().toLowerCase();

			if (envName != null) {
				envProfile = envName;
				LOG.info("ENVIRONMENT AUTO-DISCOVERY: "
						+ envName
						+ " environment detected ! Using "
						+ envProfile
						+ " profile (configuration files in 'var-configs-profiles/"
						+ envProfile + "' folder).");

				FileUtils.copyDirectory(new File(varResourceProfilesBasePath
						+ "/" + envProfile), new File(varResourceBasePath));
			} else {
				LOG.warn("ENVIRONMENT AUTO-DISCOVERY: environment detection failed ! Using default profile (configuration files in resources folder).");
			}

			// Load properties
			loadServicesEndpointsProperties();
			loadProperties();
			loadMailProperties();
			loadSecurityProperties();
			loadVersionProperties();

			loadTesbedConfiguration();
			// Check loaded
			if (!envProfile
					.equalsIgnoreCase(getSvcEndpointProperty(SVCEP_ENV_NAME)))
				throw new Exception(
						"Environment properties name mismatch (found '"
								+ getSvcEndpointProperty(SVCEP_ENV_NAME)
								+ "', expected '"
								+ envProfile
								+ "'). Check 'environment_name' properties in services_endpoint.properties file. It MUST match the profile folder name (not case sensitive).");

			EjbcaConnection(getSvcEndpointProperty(SVCEP_EJBCA_CERT_PWD),
					getSvcEndpointProperty(SVCEP_EJBCA_SUPERADMIN));
		} catch (Exception ex) {
			throw new Error("CANNOT LOAD ENVIRONMENT CONFIGURATION !", ex);
		}

	}

	private void EjbcaConnection(String certPwd, String certName)
			throws IOException {

		// Path of keystore
		String basePath = this.getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath()
				+ "/cert/ejbca/";

		CryptoProviderTools.installBCProvider();

		// if you want use p12 format (original ejbca certificate superadmin)
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		// if you want to use jks format
		// System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		System.setProperty("javax.net.ssl.keyStore", basePath + certName);
		System.setProperty("javax.net.ssl.keyStorePassword", certPwd);

	}

	private void loadServicesEndpointsProperties() throws IOException {
		try {
			// create and load default properties
			Properties props = new Properties();

			// InputStream in = PathHelper.class.getClassLoader()
			// .getResourceAsStream(PathHelper.ResourceType.CONFIG_ENV_VARIABLE_PROPERTIES.getSubPath()+"/services-endpoints.properties");

			InputStream in = new FileInputStream(varResourceBasePath
					+ "services-endpoints.properties");
			props.load(in);
			svcEndpoinsProperties = new Properties(props);

			in.close();

			LOG.info("Services Endpoints loaded successfully !!");

			if (LOG.isDebugEnabled())
				for (Entry<Object, Object> item : props.entrySet()) {
					LOG.debug("[Services Endpoints] - K: " + item.getKey()
							+ ", V: " + item.getValue());
				}
		} catch (IOException e) {
			LOG.error("ERROR: Cannot load 'services-endpoints.properties'.");
			throw new Error("Cannot load 'services-endpoints.properties'");
		}
	}

	private void loadVersionProperties() throws IOException {
		try {
			Properties props = new Properties();

			InputStream in = new FileInputStream(varResourceBasePath
					+ "version.properties");
			props.load(in);
			versionProperties = new Properties(props);

			in.close();

			LOG.info("Version properties loaded successfully !!");

			if (LOG.isDebugEnabled())
				for (Entry<Object, Object> item : props.entrySet()) {
					LOG.debug("[Version] - K: " + item.getKey() + ", V: "
							+ item.getValue());
				}
		} catch (IOException e) {
			LOG.error("ERROR: Cannot load 'version.properties'.");
			throw new Error("Cannot load 'version.properties'");
		}
	}

	private void loadProperties() throws IOException {
		try {
			// create and load default properties
			Properties props = new Properties();

			FileInputStream in = new FileInputStream(resourceBasePath
					+ "app.properties");
			props.load(in);
			appProperties = new Properties(props);

			in.close();

			LOG.info("AppConfig loaded successfully !!");

			if (LOG.isDebugEnabled())
				for (Entry<Object, Object> item : props.entrySet()) {
					LOG.debug("[AppConfig Properties] - K: " + item.getKey()
							+ ", V: " + item.getValue());
				}
		} catch (IOException e) {
			LOG.error("ERROR: Cannot load 'app.properties'.");
			throw new Error("Cannot load 'app.properties'");
		}
	}

	private void loadMailProperties() throws IOException {
		try {
			// create and load default properties
			Properties props = new Properties();

			FileInputStream in = new FileInputStream(varResourceBasePath
					+ "orc.mailer.properties");
			props.load(in);
			mailProperties = new Properties(props);

			in.close();

			LOG.info("Mail Properties loaded successfully !!");

			if (LOG.isDebugEnabled())
				for (Entry<Object, Object> item : props.entrySet()) {
					LOG.debug("[Mail Properties] - K: " + item.getKey()
							+ ", V: " + item.getValue());
				}
		} catch (IOException e) {
			LOG.error("ERROR: Cannot load 'orc.mailer.properties'.");
			throw new Error("Cannot load 'orc.mailer.properties'");
		}
	}

	private void loadSecurityProperties() throws IOException {
		try {
			// create and load default properties
			Properties props = new Properties();

			FileInputStream in = new FileInputStream(resourceBasePath
					+ "security.properties");
			props.load(in);
			securityProperties = new Properties(props);

			in.close();

			LOG.info("Security properties loaded successfully !!");

			if (LOG.isDebugEnabled())
				for (Entry<Object, Object> item : props.entrySet()) {
					LOG.debug("[Security Properties] - K: " + item.getKey()
							+ ", V: " + item.getValue());
				}
		} catch (IOException e) {
			LOG.error("ERROR: Cannot load 'security.properties'.");
			throw new Error("Cannot load 'security.properties'");
		}
	}

	private void loadTesbedConfiguration() {
		// TODO change with database configuration

		try {
			// create and load default properties
			Properties props = new Properties();

			FileInputStream in = new FileInputStream(resourceBasePath
					+ "openstack-presets.properties");
			props.load(in);
			openstackPresets = new Properties(props);

			in.close();

			String[] imageNames = openstackPresets.getProperty(
					"iaas.os.presets.compute.images.names").split(",");
			String[] imageIDs = openstackPresets.getProperty(
					"iaas.os.presets.compute.images.ids").split(",");

			HashMap<String, String> images = new HashMap<String, String>();
			for (int i = 0; i < imageNames.length; i++) {
				images.put(imageNames[i], imageIDs[i]);
			}

			String paasKey = openstackPresets
					.getProperty("iaas.os.presets.paas_key.name");

			String[] networkNames = openstackPresets.getProperty(
					"iaas.os.presets.network.networks.names").split(",");
			String[] networkIDs = openstackPresets.getProperty(
					"iaas.os.presets.network.networks.ids").split(",");

			HashMap<String, String> networks = new HashMap<String, String>();
			for (int i = 0; i < networkNames.length; i++) {
				networks.put(networkNames[i], networkIDs[i]);
			}

			String externalNetworkName = openstackPresets
					.getProperty("iaas.os.presets.network.external_network.name");

			this.osPreset = new OpenStackPreset();
			osPreset.setImages(images);
			osPreset.setNetworks(networks);
			osPreset.setPaaSKey(paasKey);
			osPreset.setExtNetwork(externalNetworkName);

			LOG.info("OpenStack presets properties loaded successfully !!");

			LOG.debug("[OpenStackPreset] - " + osPreset);

		} catch (IOException e) {
			LOG.error("ERROR: Cannot load 'openstack-presets.properties'.");
			throw new Error("Cannot load 'openstack-presets.properties'");
		}

		LOG.info("Testbed config loaded");
	}

	public static final String APP_TEST_SKIP_MONITORING = "APP_TEST_SKIP_MONITORING";
	public final static String APP_TEST_FAKE_CFY = "APP_TEST_FAKE_CFY";
	public final static String APP_TEST_FAKE_HEAT = "APP_TEST_FAKE_HEAT";
	public final static String APP_STARTUP_ENV_TEST_SKIP = "APP_STARTUP_ENV_TEST_SKIP";
	public final static String APP_TEST_SKIP_POST_DEPLOY_MONITORING_CHECK = "APP_TEST_SKIP_POST-DEPLOY_MONITORING_CHECK";
	public final static String APP_TEST_SKIP_POST_DEPLOY_MONITORING_ADD = "APP_TEST_SKIP_POST-DEPLOY_MONITORING_ADD";
	public final static String APP_TEST_SKIP_PRE_UNDEPLOY_MONITORING_REMOVE = "APP_TEST_SKIP_PRE-UNDEPLOY_MONITORING_REMOVE";

	public final static String SVCEP_ENV_NAME = "environment_name";

	public final static String SVCEP_CFY_VERSION = "cfy_manager_version";
	public final static String SVCEP_CFY_MANAGER_URL = "cfy_manager_URL";

	public final static String SVCEP_DNS_URL = "dns_URL";
	public final static String SVCEP_DN_SUFFIX = "dn_suffix";
	public final static String SVCEP_DNS_AUTH = "dns_auth";

	public final static String SVCEP_MONITORING_PILLAR_URL = "monitoring_URL";
	public final static String SVCEP_MONITORING_ADAPTER_TYPE = "adapter_type";

	public final static String SVCEP_OS_IDENTITY_URL = "openstack_identity_URL";

	public final static String SVCEP_OS_TENANT_ORC_NAME = "openstack_orchestrator_tenant_name";
	public final static String SVCEP_OS_TENANT_ORC_USERNAME = "openstack_orchestrator_tenant_username";
	public final static String SVCEP_OS_TENANT_ORC_PASSWORD = "openstack_orchestrator_tenant_password";

	public final static String SVCEP_TEMPLATE_REPO_URL = "template_repo_URL";
	public final static String SVCEP_TEMPLATE_REPO_BASE_PAAS = "template_repo_paas_base";
	public final static String SVCEP_TEMPLATE_REPO_BASE_IAAS = "template_repo_iaas_base";

	public final static String SVCEP_TEMPLATE_REPO_PAAS_BIAAS_PENTAHO_SERVER_ONLY = "template_repo_paas_pentaho_server_only";
	public final static String SVCEP_TEMPLATE_REPO_PAAS_BIAAS_PENTAHO_CONSOLE_ONLY = "template_repo_paas_pentaho_console_only";
	public final static String SVCEP_TEMPLATE_REPO_PAAS_BIAAS_PENTAHO_SERVER_W_CONSOLE = "template_repo_paas_pentaho_server_w_console";

	public final static String SVCEP_TEMPLATE_REPO_PAAS_DBAAS_MYSQL = "template_repo_paas_mysql";
	public final static String SVCEP_TEMPLATE_REPO_PAAS_DBAAS_POSTGRE = "template_repo_paas_postgre";

	public final static String SVCEP_PLAYSMS_IP = "SMS_IPPLAYSMS";
	public final static String SVCEP_PLAYSMS_DB_URL = "SMS_IPDATABASE";
	public final static String SVCEP_PLAYSMS_DB_USER = "SMS_USERDB";
	public final static String SVCEP_PLAYSMS_DB_PASSWORD = "SMS_PASSWORDDB";
	public final static String SVCEP_PLAYSMS_ADMIN_TOKEN = "ADMINTOKEN";

	public final static String SVCEP_EJBCA_IP = "SVCEP_EJBCA_IP";
	public final static String SVCEP_EJBCA_CERT_PWD = "SVCEP_EJBCA_CERT_PWD";
	public final static String SVCEP_EJBCA_CA_NAME = "SVCEP_EJBCA_CA_NAME";
	public final static String SVCEP_EJBCA_SUPERADMIN = "SVCEP_EJBCA_SUPERADMIN";

	public final static String SVCEP_BIZLAYER_BASE_REST_URL = "SVCEP_BIZLAYER_BASE_REST_URL";
	public final static String SVCEP_WEBUI_BASE_REST_URL = "SVCEP_WEBUI_BASE_REST_URL";

	public final static String MAIL_BASE_ADDRESS = "prisma.mailer.base.address";
	public final static String MAIL_INFO_ADDRESS = "prisma.mailer.info.address";
	
	public final static String SVCEP_PRISMA_IDP_URL = "SVCEP_PRISMA_IDP_URL";

	public final static String DEFAULT_AUTH_TOKEN = "DEFAULT_AUTH_TOKEN";
	public final static String DEFAULT_MONITORING_TOKEN = "DEFAULT_MONITORING_TOKEN";

	public String getSecurityProperty(String name) {
		return securityProperties.getProperty(name);
	}

	public String getMailProperty(String name) {
		return mailProperties.getProperty(name);
	}

	public String getAppProperty(String name) {
		return appProperties.getProperty(name);
	}

	public String getSvcEndpointProperty(String name) {
		return svcEndpoinsProperties.getProperty(name);
	}

	public final static String PROJECT_BUILD_VERSION = "build.version";

	public String getVersionProperty(String name) {
		return versionProperties.getProperty(name);
	}

	public String getMonitoringURL() {
		return svcEndpoinsProperties.getProperty("monitoring_URL");
	}

	public String getCloudifyManagerURL() {
		return svcEndpoinsProperties.getProperty("cfy_manager_URL");
	}

	public String getMailServerURL() {
		return svcEndpoinsProperties.getProperty("mail_server_URL");
	}

	public OpenStackPreset getOpenStackPresets() {
		return osPreset;
	}

	 /**
	     * @return the pillar adapter type read in services-endpoint.properties
	     */
	    public String getMonitoringAdapterType() {
		return svcEndpoinsProperties.getProperty(SVCEP_MONITORING_ADAPTER_TYPE);
	    }

	    /**
	     * @return the pillar URL read in services-endpoint.properties
	     */
	    public String getMonitoringEndpoint() {
		return svcEndpoinsProperties.getProperty(SVCEP_MONITORING_PILLAR_URL);
	    }
}
