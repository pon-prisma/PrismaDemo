package it.prisma.businesslayer.test.integrationtests.iaas.openstack;

import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.MainOpenstackAPIClientImpl;
import it.prisma.businesslayer.config.EnvironmentConfig;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OpenStackTest {

	Logger logger = LogManager.getLogger(OpenStackTest.class);

	String identityURL, tenant, username, pwd;
	
	@Inject
	protected EnvironmentConfig envConfig;
	
	@PostConstruct
	protected void setup() {
		identityURL = envConfig
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL);
		tenant = envConfig
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_NAME);
		username = envConfig
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_USERNAME);
		pwd = envConfig
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_PASSWORD);
	}
	
	public void test() throws Exception {
		try {
			// Login test (to orchestrator tenant)
			MainOpenstackAPIClientImpl client = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", tenant, username, pwd);

			// TODO More tests
			
			logger.info("[Startup-Env-TEST] OpenStack test succeded.");
		} catch (Exception ex) {
			throw new Exception("[Startup-Env-TEST] OpenStack test failed.", ex);
		}
	}
}
