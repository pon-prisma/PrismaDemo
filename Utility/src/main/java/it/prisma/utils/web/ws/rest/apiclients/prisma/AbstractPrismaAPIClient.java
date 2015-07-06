package it.prisma.utils.web.ws.rest.apiclients.prisma;

import it.prisma.domain.dsl.prisma.prismaprotocol.Meta;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSPaginationParams;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClient;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactoryImpl;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

/**
 * This class contains an abstract Prisma Rest Protocol API Client, with utility
 * methods to create requests.
 * 
 * @author l.biava
 * 
 */
public class AbstractPrismaAPIClient extends AbstractAPIClient {


	public class PrismaMetaData extends MetaData {

		private PrismaResponseWrapper<?> prismaResponseWrapper;
		private Meta meta;

		public PrismaResponseWrapper<?> getPrismaResponseWrapper() {
			return prismaResponseWrapper;
		}

		public void setPrismaResponseWrapper(
				PrismaResponseWrapper<?> prismaResponseWrapper) {
			this.prismaResponseWrapper = prismaResponseWrapper;
		}

		public Meta getMeta() {
			return meta;
		}

		public void setMeta(Meta meta) {
			this.meta = meta;
		}
	}

	
	/** This class contains the headers of the PRISMA REST client. 
	 * At class instantiation time the "X-Auth-Token" header is added to the headers by default.
	 * This way any PRISMA REST call using this headers class will authenticate by default.   
	 * 
	 * @author g.demusso
	 *
	 */
	public class PrismaAuthenticatedHeader extends MultivaluedMapImpl<String, Object> {

		private static final long serialVersionUID = 1597826702715479951L;
		
		public PrismaAuthenticatedHeader(String authToken)
		{
			this.add("X-Auth-Token", authToken);
		}
		
		
	}
	
	
	/**
	 * Creates a {@link AbstractPrismaAPIClient} using the default
	 * {@link RestClientFactoryImpl}.
	 * 
	 * @param baseWSUrl
	 *            The URL of Prisma BizWS.
	 *            
	 */
	public AbstractPrismaAPIClient(String baseWSUrl) {
		this(baseWSUrl, new RestClientFactoryImpl());
	}

	/**
	 * Creates a {@link AbstractPrismaAPIClient} with the given
	 * {@link RestClientFactory}.
	 * 
	 * @param baseWSUrl
	 *            The URL of Prisma BizWS.
	 * @param restClientFactory
	 *            The custom factory for the {@link RestClient}.
	 */
	public AbstractPrismaAPIClient(String baseWSUrl, 
			RestClientFactory restClientFactory) {
		super(baseWSUrl, restClientFactory);
	}

	/**
	 * See
	 * {@link AbstractPrismaAPIClient#addRestWSParamsToQueryParams(RestWSParamsContainer, MultivaluedMap)}
	 * , default create query params.
	 * 
	 * @param params
	 * @return
	 */
	protected MultivaluedMap<String, Object> addRestWSParamsToQueryParams(
			RestWSParamsContainer params) {
		return addRestWSParamsToQueryParams(params, null);
	}

	/**
	 * Adds params contained in {@link RestWSParamsContainer} to current query
	 * parameters (if given).
	 * 
	 * @param params
	 *            the {@link RestWSParamsContainer} params.
	 * @param queryParams
	 *            the already existing query params map to add
	 *            {@link RestWSParamsContainer} params to, or <tt>null</tt> to
	 *            also create it.
	 * @return
	 */
	protected MultivaluedMap<String, Object> addRestWSParamsToQueryParams(
			RestWSParamsContainer params,
			MultivaluedMap<String, Object> queryParams) {
		if (queryParams == null)
			queryParams = new MultivaluedHashMap<String, Object>();

		queryParams.putSingle(RestWSPaginationParams.QUERY_PARAM_LIMIT, params
				.getPaginationParams().getLimit());
		queryParams.putSingle(RestWSPaginationParams.QUERY_PARAM_OFFSET, params
				.getPaginationParams().getOffset());

		return queryParams;
	}

	/**
	 * Generic comment
	 * 
	 * @param result
	 * @param meta
	 * @return
	 * @throws APIErrorException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <RETURN_TYPE> RETURN_TYPE handleResult(
			BaseRestResponseResult result, PrismaMetaData meta)
			throws APIErrorException {
		if (result.getStatus().getFamily() == Status.Family.SUCCESSFUL) {
			try {
				if (meta != null) {
					meta.setMeta(((PrismaResponseWrapper<?>) result.getResult())
							.getMeta());
					meta.setBaseRestResponseResult(result);
					meta.setPrismaResponseWrapper(((PrismaResponseWrapper<?>) result
							.getResult()));
				}
				return ((PrismaResponseWrapper<RETURN_TYPE>) result.getResult())
						.getResult();
			} catch (Exception e) {
				throw new APIErrorException("Unexpected response type.", e);
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <REPRESENTATION_CLASS> REPRESENTATION_CLASS handleResult(BaseRestResponseResult result) throws APIErrorException {
		
		if (result.getStatus().getFamily() == Status.Family.SUCCESSFUL) {
			try {
				return ((PrismaResponseWrapper<REPRESENTATION_CLASS>) result.getResult()).getResult();
			} catch (Exception e) {
				throw new APIErrorException("Unexpected response type.", e);
			}
		} else {
			throw new APIErrorException("API Error: " + result.getStatus(), null,
					result.getOriginalRestMessage(), ((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
}
