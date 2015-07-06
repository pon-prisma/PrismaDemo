package it.prisma.businesslayer.test.integrationtests.dns;

import it.prisma.businesslayer.bizlib.dns.DNSMgmtBean;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BindDNSTest {

	Logger logger = LogManager.getLogger(BindDNSTest.class);
	
	@Inject
	protected DNSMgmtBean dnsBean;

	public void test() throws Exception {
		try {
			// TODO Change domain
			String domainName = "test-"+UUID.randomUUID().toString().substring(0, 8)
					+ ".infn.ponsmartcities-prisma.it";
			String IP = "90.147.102.194";
//			dnsBean.registerDN(domainName, IP);
//			dnsBean.unregisterDN(domainName, IP);
			
			logger.info("[Startup-Env-TEST] DNS test succeded.");
		} catch (Exception ex) {
			throw new Exception("[Startup-Env-TEST] DNS test failed.", ex);
		}
	}
}
