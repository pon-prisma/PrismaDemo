package it.prisma.businesslayer.bizws.misc;

import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.domain.dsl.prisma.DebugInformations;

import java.net.InetAddress;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class MiscWSImpl extends BaseWS implements MiscWS {

	@Inject
	protected PaaSServiceDAO paasSvcDAO;

	@Inject
	protected EnvironmentConfig envConfig;

	@Override
	public Response isDomainNameAvailable(String domainName) {

		try {
			if (paasSvcDAO.findByDomainName(domainName) == null)
				return handleResult(true);
			else
				return handleResult(false);
		} catch (Exception e) {
			return handleGenericException(e);
		}
	}

	@Override
	public DebugInformations debugInformations() {
		DebugInformations info = new DebugInformations();
		info.setProjectVersion(envConfig
				.getVersionProperty(EnvironmentConfig.PROJECT_BUILD_VERSION));

		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			hostname = envConfig
					.getSvcEndpointProperty(EnvironmentConfig.SVCEP_BIZLAYER_BASE_REST_URL);
		}
		info.setServerHostname(hostname);

		return info;
	}
}
