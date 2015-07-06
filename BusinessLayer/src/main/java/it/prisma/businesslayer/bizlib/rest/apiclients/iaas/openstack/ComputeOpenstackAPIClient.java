package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

import it.prisma.domain.dsl.iaas.openstack.Error;
import it.prisma.domain.dsl.iaas.openstack.ErrorWrapperCompute;
import it.prisma.domain.dsl.iaas.openstack.compute.request.OSStartRequest;
import it.prisma.domain.dsl.iaas.openstack.compute.request.OSStopRequest;
import it.prisma.domain.dsl.iaas.openstack.compute.request.OpenstackCreateKeyPairRequest;
import it.prisma.domain.dsl.iaas.openstack.compute.response.OpenstackCreateKeyPairResponse;
import it.prisma.domain.dsl.iaas.openstack.compute.response.listKey.OpenstackStackListPairResponse;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * <b>NOTE</b>: This component doesn't match Openstack default error wrapper! It
 * uses {@link ErrorWrapperCompute}
 * 
 * 
 * @author Reply
 * 
 */
public class ComputeOpenstackAPIClient extends AbstractOpenstackAPIClient {

	/**
	 * 
	 * @param baseURL
	 * @param restClientFactory
	 * @param openstackServiceVersion
	 */
	protected ComputeOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory, String openstackServiceVersion) {
		super(baseURL, restClientFactory, openstackServiceVersion);
	}

	public ComputeOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory,
			String openstackServiceVersion, String tokenID) {
		super(baseURL, restClientFactory, openstackServiceVersion, tokenID);

	}

	@Override
	protected <RespType> void handleErrorResult(BaseRestResponseResult result)
			throws OpenstackAPIErrorException {

		// Gets the last error in the map (there should be only one error by default)
		Error error = null;
		for (Map.Entry<String, Error> e : ((ErrorWrapperCompute) result
				.getResult()).getAdditionalProperties().entrySet()) {
			error = e.getValue();
			error.setTitle(e.getKey());
		}

		throw new OpenstackAPIErrorException("API_ERROR", null,
				result.getOriginalRestMessage(), error);
	}
	
	public OpenstackCreateKeyPairResponse createKeyPair(
			OpenstackCreateKeyPairRequest request,
			MultivaluedMap<String, Object> headerMap)
			throws JsonParseException, JsonMappingException, IOException,
			RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			OpenstackAPIErrorException {

		String URL = baseWSUrl + "/" + getServiceVersion() + "os-keypairs";

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(tokenID);
		if (headerMap != null)
			headers.putAll(headerMap);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		OpenstackRestResponseDecoder<OpenstackCreateKeyPairResponse> decoder = new OpenstackRestResponseDecoder<OpenstackCreateKeyPairResponse>(
				new OpenstackComputeRRDStrategy<OpenstackCreateKeyPairResponse>(
						OpenstackCreateKeyPairResponse.class));

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE, decoder,
				null);

		return handleResult(result);
	}
	
	public OpenstackStackListPairResponse listKeypair()
			throws RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			OpenstackAPIErrorException {

		String url = baseWSUrl + "/" + getServiceVersion() + "os-keypairs";
		
		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(this.tokenID);

		BaseRestResponseResult result = restClient.getRequest(url,headers,
						new OpenstackRestResponseDecoder<OpenstackStackListPairResponse>(
								OpenstackStackListPairResponse.class),null);

		return handleResult(result);
	}

	
	public void stopServer(String vmId,	MultivaluedMap<String, Object> headerMap)
			throws JsonParseException, JsonMappingException, IOException,
			RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			OpenstackAPIErrorException {

		String URL = baseWSUrl + "/" + getServiceVersion() + "servers/" + vmId + "/action";
		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(tokenID);
		if (headerMap != null)
			headers.putAll(headerMap);

		OSStopRequest osStop = new OSStopRequest();
		osStop.setOsStop("1");
		
		
		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(osStop);

		OpenstackRestResponseDecoder<Object> decoder = new OpenstackRestResponseDecoder<Object>(
				new OpenstackComputeRRDStrategy<Object>(Object.class));
		
		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE, decoder,
				null);

		handleResult(result);
	}

	
	public void startServer(String vmId,	MultivaluedMap<String, Object> headerMap)
			throws JsonParseException, JsonMappingException, IOException,
			RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			OpenstackAPIErrorException {

		String URL = baseWSUrl + "/" + getServiceVersion() + "servers/" + vmId + "/action";
		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(tokenID);
		if (headerMap != null)
			headers.putAll(headerMap);

		OSStartRequest osStart = new OSStartRequest();
		osStart.setOsStart("1");
		
		
		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(osStart);

		OpenstackRestResponseDecoder<Object> decoder = new OpenstackRestResponseDecoder<Object>(
				new OpenstackComputeRRDStrategy<Object>(Object.class));
		
		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE, decoder,
				null);

		handleResult(result);
	}

	
}
