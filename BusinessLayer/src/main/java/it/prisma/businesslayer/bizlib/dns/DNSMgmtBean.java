package it.prisma.businesslayer.bizlib.dns;

import it.prisma.businesslayer.bizlib.rest.apiclients.dns.bind.BindAPIClient;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.utils.RetryCommand;
import it.prisma.utils.misc.polling.AbstractPollingBehaviour;
import it.prisma.utils.misc.polling.Poller;
import it.prisma.utils.misc.polling.PollingException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DNSMgmtBean {

	@Inject
	protected EnvironmentConfig envConfig;

	public static class RegisterDNRetryCommand extends RetryCommand<Void> {

		String domainName;
		String ipAddress;
		boolean waitForSuccess;
		DNSMgmtBean instance;

		public Void command(Object... args) throws Exception {
			instance.registerDN(domainName, ipAddress, waitForSuccess);
			return null;
		}

		public RetryCommand<Void> me(DNSMgmtBean instance) {
			this.instance = instance;
			return this;
		}

		public RetryCommand<Void> init(DNSMgmtBean instance, String domainName,
				String ipAddress, boolean waitForSuccess) {
			me(instance);
			this.domainName = domainName;
			this.ipAddress = ipAddress;
			this.waitForSuccess = waitForSuccess;
			return this;
		}

		public RetryCommand<Void> init(DNSMgmtBean instance, String domainName,
				String ipAddress) {
			return init(instance, domainName, ipAddress, false);
		}
	}

	public static class UnregisterDNRetryCommand extends RetryCommand<Void> {

		String domainName;
		String ipAddress;
		DNSMgmtBean instance;

		public Void command(Object... args) throws Exception {
			instance.unregisterDN(domainName, ipAddress);
			return null;
		}

		public RetryCommand<Void> me(DNSMgmtBean instance) {
			this.instance = instance;
			return this;
		}

		public RetryCommand<Void> init(DNSMgmtBean instance, String domainName,
				String ipAddress) {
			me(instance);
			this.domainName = domainName;
			this.ipAddress = ipAddress;
			return this;
		}
	}
	
	/**
	 * Registers a new DN with the given IP, without waiting for the propagation
	 * of the update.
	 * 
	 * @param domainName
	 * @param ipAddress
	 * @throws Exception
	 * 
	 * @see {@link DNSMgmtBean#waitForDNRegistrationSuccessful(String, String)}
	 */
	public void registerDN(String domainName, String ipAddress)
			throws Exception {
		registerDN(domainName, ipAddress, false);
	}

	/**
	 * Registers a new DN with the given IP.
	 * 
	 * @param domainName
	 * @param ipAddress
	 * @param waitForSuccess
	 *            if <tt>true</tt> waits until the DN is propagated.
	 * @throws Exception
	 * 
	 * @see {@link DNSMgmtBean#waitForDNRegistrationSuccessful(String, String)}
	 */
	public void registerDN(String domainName, String ipAddress,
			boolean waitForSuccess) throws Exception {
		try {
			// Update DNS
			BindAPIClient dnsClient = new BindAPIClient(
					envConfig
							.getSvcEndpointProperty(EnvironmentConfig.SVCEP_DNS_URL));
			dnsClient.addDNSRecord(domainName, ipAddress, envConfig
					.getSvcEndpointProperty(EnvironmentConfig.SVCEP_DNS_AUTH));

		} catch (Exception ex) {
			throw new Exception("Cannot add DN to DNS (" + domainName + "/"
					+ ipAddress + ").", ex);
		}

		if (waitForSuccess)
			waitForDNRegistrationSuccessful(domainName, ipAddress);
	}

	public void unregisterDN(String domainName, String ipAddress)
			throws Exception {
		try {
			// Update DNS
			BindAPIClient dnsClient = new BindAPIClient(
					envConfig
							.getSvcEndpointProperty(EnvironmentConfig.SVCEP_DNS_URL));
			dnsClient.deleteDNSRecord(domainName, ipAddress, envConfig
					.getSvcEndpointProperty(EnvironmentConfig.SVCEP_DNS_AUTH));

		} catch (Exception ex) {
			throw new Exception("Cannot remove DN from DNS (" + domainName
					+ "/" + ipAddress + ").", ex);
		}
	}

	/**
	 * Waits (with active polling) for the successful DN entry propagation.
	 * 
	 * @param domainName
	 * @param ipAddress
	 * @throws Exception
	 */
	public void waitForDNRegistrationSuccessful(String domainName,
			String ipAddress) throws Exception {
		// Check DNS available
		AbstractPollingBehaviour<String, InetAddress> pollB = new AbstractPollingBehaviour<String, InetAddress>() {

			@Override
			public boolean pollExit(InetAddress pollResult) {
				return pollResult.getHostAddress() != null;
			}

			@Override
			public InetAddress doPolling(String param) throws PollingException {
				try {
					return InetAddress.getByName(param);
				} catch (UnknownHostException e) {
					throw new PollingException(e);
				}
			}

			@Override
			public boolean pollSuccessful(String params, InetAddress pollResult) {
				// TODO: Check with real IP
				// return
				// serviceIP.equals(dnsPoller.doPoll(serviceDN).getHostAddress();
				return true;
			}
		};

		pollB.setTimeoutThreshold(60000);
		Poller<String, InetAddress> dnsPoller = new Poller<String, InetAddress>(
				pollB, 2000, 2000, 5);
		try {
			dnsPoller.setLogEnabled(true);
			if (!ipAddress
					.equals(dnsPoller.doPoll(domainName).getHostAddress()))
				throw new Exception("DNS IP mismatch.");
		} catch (Exception ex) {
			throw new Exception("DNS Record error:", ex);
		}
	}

}
