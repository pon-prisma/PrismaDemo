package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

import it.prisma.domain.dsl.iaas.openstack.ErrorWrapperNetwork;
import it.prisma.domain.dsl.iaas.openstack.network.response.OpenstackStackNetworkDetailsResponse;
import it.prisma.domain.dsl.iaas.openstack.network.response.OpenstackStackNetworkListResponse;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClient.RestMethod;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import javax.ws.rs.core.MultivaluedMap;

public class NetworkOpenstackAPIClient extends AbstractOpenstackAPIClient {

	/**
	 * 
	 * @param baseURL
	 * @param restClientFactory
	 * @param openstackServiceVersion
	 */
	protected NetworkOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory, String openstackServiceVersion) {
		super(baseURL, restClientFactory, openstackServiceVersion);
	}

	public NetworkOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory,
			String openstackServiceVersion, String tokenID) {
		super(baseURL, restClientFactory, openstackServiceVersion, tokenID);

	}

	public OpenstackStackNetworkListResponse listNetwork()
			throws RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			OpenstackAPIErrorException {

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(this.tokenID);

		String url = baseWSUrl + "/" + getServiceVersion() + "/networks";

		BaseRestResponseResult result = restClient
				.getRequest(
						url,
						headers,
						new OpenstackRestResponseDecoder<OpenstackStackNetworkListResponse>(
								new OpenstackNetworkRRDStrategy<OpenstackStackNetworkListResponse>(
										OpenstackStackNetworkListResponse.class)),
						null);

		return handleResult(result);
	}

	public OpenstackStackNetworkDetailsResponse getNetworkDetails(
			String network_id) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException, OpenstackAPIErrorException {

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(this.tokenID);

		String url = baseWSUrl + "/" + getServiceVersion() + "/networks/"
				+ network_id;

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						url,
						headers,
						null,
						null,
						null,
						null,
						new OpenstackRestResponseDecoder<OpenstackStackNetworkDetailsResponse>(
								new OpenstackNetworkRRDStrategy<OpenstackStackNetworkDetailsResponse>(
										OpenstackStackNetworkDetailsResponse.class)),
						null);

		return handleResult(result);
	}

	public Object deleteNetwork(String network_id) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException, OpenstackAPIErrorException {

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(this.tokenID);

		String url = baseWSUrl + "/" + getServiceVersion() + "/networks/"
				+ network_id;

		BaseRestResponseResult result = restClient.doRequest(RestMethod.DELETE,
				url, headers, null, null, null, null,
				new OpenstackRestResponseDecoder<Object>(
						new OpenstackNetworkRRDStrategy<Object>(Object.class)),
				null);

		return handleResult(result);
	}

	@Override
	protected <RespType> void handleErrorResult(BaseRestResponseResult result)
			throws OpenstackAPIErrorException {

		throw new OpenstackAPIErrorException("API_ERROR", null,
				result.getOriginalRestMessage(),
				((ErrorWrapperNetwork) result.getResult()).getError());
	}

	// @Override
	// protected <RespType> void handleErrorResult(BaseRestResponseResult
	// result)
	// throws OpenstackAPIErrorException {
	//
	// Error apiError = new Error();
	// apiError.setMessage(((String) result.getResult()));
	// apiError.setTitle(((String) result.getResult()));
	// apiError.setCode((long) result.getStatus().getStatusCode());
	//
	// throw new OpenstackAPIErrorException("API_ERROR", null,
	// result.getOriginalRestMessage(), apiError);
	// }
}
