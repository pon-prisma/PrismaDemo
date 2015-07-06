package it.prisma.businesslayer.bizws.config.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This filter provides default logging for the request of every RESTful
 * WebService.
 * 
 * @author l.biava
 *
 */
@Provider
public class PrismaRestWSLoggingRequestFilter implements ContainerRequestFilter {

	private static final String TAG = "[RS-WS-Invoke]";
	private static final Logger LOG = LogManager.getLogger(TAG);

	@Context
	private HttpServletRequest request;

	//TODO: Autentication e Rate limit
	
	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		if (!LOG.isDebugEnabled())
			LOG.info(requestContext.getMethod() + " ("
					+ requestContext.getUriInfo().getPath() + ") - ");
		else
			LOG.debug(requestContext.getMethod() + " ("
					+ requestContext.getUriInfo().getPath() + ") - "
					+ request.getRemoteHost());

		requestContext.setProperty("X-WS-Received-Time",
				System.currentTimeMillis());
	}

}
