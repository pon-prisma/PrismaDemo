package it.prisma.businesslayer.bizws.config.filters;

import it.prisma.businesslayer.config.EnvironmentConfig;

import java.io.IOException;
import java.net.InetAddress;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * This class is used to enrich REST responses with custom additional headers.
 * 
 * @author l.biava
 *
 */
@Provider
public class ResponseHeadersFilter implements ContainerResponseFilter {

	@Inject
	EnvironmentConfig envConfig;

	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {

		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			hostname = envConfig
					.getSvcEndpointProperty(EnvironmentConfig.SVCEP_BIZLAYER_BASE_REST_URL);
		}

		responseContext.getHeaders().add("X-Debug-Served-By", hostname);
	}
}