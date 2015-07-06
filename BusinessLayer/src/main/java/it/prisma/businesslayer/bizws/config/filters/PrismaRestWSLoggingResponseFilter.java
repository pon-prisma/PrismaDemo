package it.prisma.businesslayer.bizws.config.filters;

import it.prisma.businesslayer.bizws.config.annotations.PrismaRestWSParams;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This filter provides default logging for the response of every RESTful
 * WebService.</br>It also provides additional information, such as the
 * <tt>Elaboration Time</tt> (using "X-Elaboration-Time" header), if used in
 * combination with {@link PrismaRestWSLoggingRequestFilter}.
 * 
 * @author l.biava
 *
 */
@Provider
public class PrismaRestWSLoggingResponseFilter implements
		ContainerResponseFilter {

	private static final String TAG = "[RS-WS-Invoke-Response]";
	private static final Logger LOG = LogManager.getLogger(TAG);

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;
	
	@RequestScoped
	@Inject
	@PrismaRestWSParams
	RestWSParamsContainer restWSParams;

	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {
		Long elaborationTime = null;

		try {					
			long startTime = (Long) requestContext
					.getProperty("X-WS-Received-Time");
			elaborationTime = System.currentTimeMillis() - startTime;

			response.setHeader("X-Elaboration-Time", elaborationTime.toString());
		} catch (Exception ex) {

		}		

		if (LOG.isDebugEnabled())
			LOG.debug(requestContext.getMethod() + " ("
					+ requestContext.getUriInfo().getPath() + ") - "
					+ request.getRemoteHost() + " - RESPONSE[Status:"
					+ responseContext.getStatus() + ", Time:"
					+ (elaborationTime != null ? elaborationTime + "ms" : "NA")
					+ "]");
		// response.get

	}

}