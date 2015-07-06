//package it.prisma.businesslayer.test;
//
//import it.prisma.businesslayer.bizlib.mail.MailBean;
//import it.prisma.businesslayer.bizlib.paas.services.caaas.CaHelper;
//import it.prisma.businesslayer.config.EnvironmentConfig;
//import it.prisma.businesslayer.test.integrationtests.dns.BindDNSTest;
//import it.prisma.businesslayer.test.integrationtests.iaas.openstack.OpenStackTest;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//import javax.inject.Inject;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//@Singleton
//@Startup
//public class ServicesTest {
//
//	private static final Logger LOG = LogManager.getLogger(ServicesTest.class);
//	private static final String TAG = "[Startup-Env-TEST] ";
//
//	@Inject
//	protected BindDNSTest dnsTest;
//
//	@Inject
//	protected OpenStackTest osTest;
//
//	@Inject
//	protected EnvironmentConfig envConfig;
//
//	@Inject
//	protected MailBean mailBean;
//
//	@Inject
//	protected CaHelper caHelper;
//
//	@PostConstruct
//	protected void init() {
//
//		if (envConfig.getAppProperty(
//				EnvironmentConfig.APP_STARTUP_ENV_TEST_SKIP).equals("true")) {
//			LOG.info(TAG + " Skipped !");
//			return;
//		}
//
//		LOG.info(TAG + "Starting Services Integration Tests.");
//		try {
//			// TEST OPENSTACK LOGIN
//			osTest.test();
//
//			// TEST DNS
//			dnsTest.test();
//
//			// TEST CLOUDIFY LOGIN
//
//			// MAIL TEST
//			mailBean.sendTestEmail("prisma-test@libero.it");
//
//			// CA TEST
//			caHelper.getConnection().getEjbcaVersion();
//
//			// ETC
//		} catch (Exception ex) {
//			LOG.error(TAG + "Failed !", ex);
//			throw new Error(TAG + "Failed !", ex);
//		}
//	}
//
//}
