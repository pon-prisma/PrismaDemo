package it.prisma.businesslayer.bizws.config.filters;

import it.prisma.businesslayer.bizws.config.producers.PrismaRestWSParamsProducer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;

/**
 * This filter enables {@link PrismaRestWSParamsProducer} (for automatic Prisma
 * Rest WS Params use into DAL).
 * 
 * @author l.biava
 * 
 */
public class PrismaRestWSParamsEnablerRequestFilter implements
		ContainerRequestFilter {

	@Context
	private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		request.setAttribute(PrismaRestWSParamsProducer.ATTRIBUTE_ENABLER, true);
	}

}
