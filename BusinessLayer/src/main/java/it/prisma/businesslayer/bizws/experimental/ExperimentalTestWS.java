package it.prisma.businesslayer.bizws.experimental;

import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.annotations.PrismaRestWSParams;
import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Path("/experimental")
@PrismaWrapper
public class ExperimentalTestWS extends BaseWS {

	@Context
	HttpServletResponse servletResponse;

	@Context
	HttpServletRequest servletRequest;

	@Inject
	@PrismaRestWSParams
	RestWSParamsContainer restWSParams;

	@POST
	@Path("/ws-params")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String,Object> testWSParams() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orderingParams",restWSParams.getOrderingParams());
		map.put("paginationParams",restWSParams.getPaginationParams());
		return map;
	}

	@POST
	@Path("/no-wrapper/{exception}")
	@Produces(MediaType.APPLICATION_JSON)
	@PrismaWrapper(enabled = false)
	public String[] testNoWrapperWithParams(
			@PathParam("exception") boolean exception) {
		if (exception)
			throw new PrismaWrapperException("WS_ERROR_MSG", new Exception(
					"WS_INNER_EXCEPTION"));

		return new String[] { "a", "b" };
	}

	@POST
	@Path("/wrapper/{exception}")
	@Produces(MediaType.APPLICATION_JSON)
	public String[] testWrapperWithParams(
			@PathParam("exception") boolean exception) {
		if (exception)
			throw new PrismaWrapperException("WS_ERROR_MSG", new Exception(
					"WS_INNER_EXCEPTION"));

		return new String[] { "a", "b" };
	}

	@GET
	@Path("/wrapper")
	@Produces(MediaType.APPLICATION_JSON)
	public String[] testWrapper() {
		servletResponse.setHeader("X-WS-RS-Prisma-Wrap", "true");
		return new String[] { "a", "b" };
	}
}
