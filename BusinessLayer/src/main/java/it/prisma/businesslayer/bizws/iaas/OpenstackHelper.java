package it.prisma.businesslayer.bizws.iaas;

import it.prisma.businesslayer.bizlib.infrastructure.AppRepoLookupInfo;
import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean;
import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean.AppRepoEndpoint;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.domain.dsl.prisma.OpenstackErrorCode;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.ClientResponseException;
import org.openstack4j.api.exceptions.ConnectionException;
import org.openstack4j.api.exceptions.OS4JException;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;

public class OpenstackHelper {

	private static Logger LOG = LogManager.getLogger(OpenstackHelper.class);

	@Inject
	private InfrastructureManagerBean infrastructureBean;

	// TODO: Attenzione gli altri metodi sono statici, questo ha bisogno di CDI
	// ! Spostare da altre parti.
	public OSClient getOSClient(Long workgroupId) {

		AppRepoEndpoint endpoint = infrastructureBean
				.getPrivateAppRepoEndpoint(new AppRepoLookupInfo(workgroupId));

		OSFactory.enableHttpLoggingFilter(true);
		OSFactory.enableLegacyEndpointHandling(true);
		OSClient os = OSFactory.builder().endpoint(endpoint.getOSIdentityURL())
				// identityURL + "/v2.0")
				.credentials(endpoint.getUsername(), endpoint.getPassword())
				.tenantName(endpoint.getTenantName()).authenticate();

		LOG.debug(os.getAccess().toString());

		/*
		 * Vecchio fix per eliminare il problema dei nomi duplicati tra i
		 * servizi INFN
		 */
		// Service occi = null;
		// for (Service service : os.getAccess().getServiceCatalog()) {
		//
		// if (service.getType().toString().toLowerCase().equals("occi")) {
		// occi = service;
		// break;
		// }
		// }
		//
		// if (occi != null) {
		// os.getAccess().getServiceCatalog().remove(occi);
		// }

		return os;
	}

	public static OSClient getOSClient(String identityURL, String tenantName,
			String username, String password) {
		OSFactory.enableHttpLoggingFilter(true);
		// OSFactory.enableLegacyEndpointHandling(true);
		OSClient os = OSFactory
				.builder()
				.withConfig(
						Config.newConfig().withReadTimeout(120000)
								.withConnectionTimeout(120000))
				.endpoint(identityURL).credentials(username, password)
				.tenantName(tenantName).authenticate();

		LOG.debug(os.getAccess().toString());

		return os;
	}

	public static OSClient getOSClient(String identityURL, String version,
			String tenantName, String username, String password) {
		return getOSClient(identityURL, version, "default", tenantName,
				username, password);
	}

	public static OSClient getOSClient(String identityURL, String version,
			String domain, String tenantName, String username, String password) {
		OSFactory.enableHttpLoggingFilter(true);
		// OSFactory.enableLegacyEndpointHandling(true);
		if (version.equals("V2") || version.equals("2")
				|| version.equals("2.0"))
			return OSFactory
					.builder()
					.withConfig(
							Config.newConfig().withReadTimeout(120000)
									.withConnectionTimeout(120000))
					.endpoint(identityURL).credentials(username, password)
					.tenantName(tenantName).authenticate();
		else if (version.equals("V3") || version.equals("3"))
			return OSFactory
					.builderV3()
					.withConfig(
							Config.newConfig().withReadTimeout(120000)
									.withConnectionTimeout(120000))
					.endpoint(identityURL)
					.credentials(username, password,
							Identifier.byName(tenantName)).authenticate();

		throw new UnsupportedOperationException(
				"Only v2.0 & v3 versions are supported, given " + version);
	}

	public PrismaWrapperException throwException(OS4JException e) {

		PrismaWrapperException prismaException = new PrismaWrapperException(
				e.getMessage(), e);

		if (e instanceof ClientResponseException) {
			ClientResponseException clientException = (ClientResponseException) e;
			prismaException.setStatus(Status.fromStatusCode(clientException
					.getStatus()));
			switch (clientException.getStatus()) {
			case 400:
				prismaException
						.setErrorCode(OpenstackErrorCode.OPENSTACK_BAD_REQUEST);
				break;
			case 401:
				prismaException
						.setErrorCode(OpenstackErrorCode.OPENSTACK_UNAUTHORIZED);
				break;
			case 403:
				prismaException
						.setErrorCode(OpenstackErrorCode.OPENSTACK_FORBIDDEN);
				break;
			case 408:
				prismaException
						.setErrorCode(OpenstackErrorCode.OPENSTACK_FORBIDDEN);
				break;
			case 409:
				prismaException.setStatus(Status.CONFLICT);
				prismaException
						.setErrorCode(OpenstackErrorCode.OPENSTACK_CONFLICT);
				break;
			default:
				prismaException
						.setErrorCode(OpenstackErrorCode.OPENSTACK_GENERIC_ERROR);
				break;
			}

		} else if (e instanceof ConnectionException) {
			prismaException
					.setErrorCode(OpenstackErrorCode.OPENSTACK_CONNECTION_ERROR);
		}

		return prismaException;

	}

}
