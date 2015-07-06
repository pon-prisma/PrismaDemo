package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

import it.prisma.domain.dsl.iaas.openstack.orchestration.request.OpenstackCreateStackRequest;
import it.prisma.domain.dsl.iaas.openstack.orchestration.response.OpenstackCreateStackResponse;
import it.prisma.domain.dsl.iaas.openstack.orchestration.response.OpenstackStackStatusDetailsResponse;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class OrchestrationOpenstackAPIClient extends AbstractOpenstackAPIClient {

	/**
	 * 
	 * @param baseURL
	 * @param restClientFactory
	 * @param openstackServiceVersion
	 */
	protected OrchestrationOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory, String openstackServiceVersion) {
		super(baseURL, restClientFactory, openstackServiceVersion);
	}

	public OrchestrationOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory,
			String openstackServiceVersion, String tokenID) {
		super(baseURL, restClientFactory, openstackServiceVersion, tokenID);

	}

	public OpenstackCreateStackResponse createStack(
			OpenstackCreateStackRequest request,
			MultivaluedMap<String, Object> headerMap)
			throws JsonParseException, JsonMappingException, IOException,
			RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			OpenstackAPIErrorException {

		String URL = baseWSUrl + "/" + getServiceVersion() + "stacks";

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(tokenID);
		if (headerMap != null)
			headers.putAll(headerMap);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new OpenstackRestResponseDecoder<OpenstackCreateStackResponse>(
						OpenstackCreateStackResponse.class), null);

		return handleResult(result);
	}

	public OpenstackStackStatusDetailsResponse showStackDetails(String url)
			throws RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			OpenstackAPIErrorException {

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(this.tokenID);

		BaseRestResponseResult result = restClient
				.getRequest(
						url,
						headers,
						new OpenstackRestResponseDecoder<OpenstackStackStatusDetailsResponse>(
								OpenstackStackStatusDetailsResponse.class),
						null);

		return handleResult(result);
	}

	public void deleteStack(String stack_name,
			String stack_id) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException, OpenstackAPIErrorException {

		String URL = baseWSUrl + "/" + getServiceVersion() + "stacks" + "/"
				+ stack_name + "/" + stack_id;

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(this.tokenID);

		BaseRestResponseResult result = restClient.deleteRequest(URL, headers,
				null, null, new OpenstackRestResponseDecoder<String>(
						String.class), null);

		handleResult(result);
	}
}
