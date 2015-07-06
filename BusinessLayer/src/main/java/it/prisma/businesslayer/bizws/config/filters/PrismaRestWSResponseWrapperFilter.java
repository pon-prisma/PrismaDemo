package it.prisma.businesslayer.bizws.config.filters;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.dal.dao.queryparams.annotations.DAOQueryParams;
import it.prisma.domain.dsl.prisma.prismaprotocol.Error;
import it.prisma.domain.dsl.prisma.prismaprotocol.Links;
import it.prisma.domain.dsl.prisma.prismaprotocol.Pagination;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper.PrismaResponseWrapperBuilder;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSPaginationParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This filter can be used to wrap the result of a WS with a
 * {@link PrismaResponseWrapper}. It must be enabled using the
 * {@link PrismaWrapper} annotation.
 * 
 * @author l.biava
 *
 */
public class PrismaRestWSResponseWrapperFilter implements WriterInterceptor {

	private static final Logger LOG = LogManager
			.getLogger(PrismaRestWSResponseWrapperFilter.class);

	@Context
	protected HttpServletResponse response;

	@Context
	protected HttpServletRequest request;

	@Inject
	@RequestScoped
	@DAOQueryParams
	it.prisma.dal.dao.queryparams.DAOQueryParams daoQueryParams;

	@Override
	public void aroundWriteTo(WriterInterceptorContext context)
			throws IOException, WebApplicationException {

		try {
			Status status = Status.fromStatusCode(response.getStatus());

			if (status == null) {
				LOG.error("No status found for code " + response.getStatus()
						+ ". Defaulting to 500.");
				status = Status.INTERNAL_SERVER_ERROR;
			}

			Object result = context.getEntity();
			PrismaResponseWrapper<?> prismaResponse;
			if (result instanceof Error)
				prismaResponse = new PrismaResponseWrapperBuilder(status,
						(Error) result).build();
			else
				prismaResponse = new PrismaResponseWrapperBuilder(status,
						result).build();

			// Update context entity data and type
			context.setType(PrismaResponseWrapper.class);
			context.setGenericType(PrismaResponseWrapper.class);
			context.setEntity(prismaResponse);

			// Add pagination informations if enabled
			if (!daoQueryParams.isDisabled()) {
				if (daoQueryParams.getPaginationParams().getResultTotalCount() != null) {
					long paginationTotalCount = daoQueryParams
							.getPaginationParams().getResultTotalCount();
					Pagination pagination = new Pagination();
					prismaResponse.getMeta().setPagination(pagination);
					pagination.setTotalCount(paginationTotalCount);
					pagination.setLimit(daoQueryParams.getPaginationParams()
							.getLimit());
					pagination.setOffset(daoQueryParams.getPaginationParams()
							.getOffset());
					// TODO: Links
					Links links = new Links();
					pagination.setLinks(links);

					links.setNext(getNextLink(request.getRequestURL()
							.toString(), request.getQueryString(), pagination));

					response.setHeader("X-Pagination-Limit", ""
							+ daoQueryParams.getPaginationParams().getLimit());
					response.setHeader("X-Pagination-Offset", ""
							+ daoQueryParams.getPaginationParams().getOffset());
					response.setHeader("X-Total-Count", ""
							+ paginationTotalCount);
					response.setHeader(
							"Link",
							getLinkHeader(request.getRequestURL().toString(),
									request.getQueryString(), pagination));
				}
			}
		} catch (Exception ex) {
			LOG.error(
					"Error Wrapping Prisma REST WS Response: "
							+ ex.getMessage(), ex);
		}
		context.proceed();
	}

	private String getNextLink(String baseURL, String queryString,
			Pagination pagination) {
		if (queryString == null)
			queryString = "";

		String offsetPattern = "(" + RestWSPaginationParams.QUERY_PARAM_OFFSET
				+ "=)(\\d+)";

		String newOffset = RestWSPaginationParams.QUERY_PARAM_OFFSET + "="
				+ (pagination.getOffset() + pagination.getLimit());

		if (queryString.contains(RestWSPaginationParams.QUERY_PARAM_OFFSET
				+ "="))
			queryString = queryString.replaceAll(offsetPattern, newOffset);
		else
			queryString += (queryString.equals("") ? "" : "&") + newOffset;

		return baseURL + "?" + queryString;
	}

	private String getLinkHeader(String baseURL, String queryString,
			Pagination pagination) {

		// Map of Rel - URL for links
		Map<String, String> links = new HashMap<String, String>();

		links.put("next", getNextLink(baseURL, queryString, pagination));
		links.put("last", getNextLink(baseURL, queryString, pagination));

		String linkHeader = "";
		for (Map.Entry<String, String> link : links.entrySet()) {
			linkHeader += "<" + link.getValue() + ">; rel=\"" + link.getKey()
					+ "\",";
		}

		if (links.size() > 0)
			linkHeader = linkHeader.substring(0, linkHeader.length() - 1);

		return linkHeader;
	}
}
