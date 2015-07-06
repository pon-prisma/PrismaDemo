package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

import it.prisma.domain.dsl.iaas.openstack.identity.request.OpenstackGetTokenRequest;
import it.prisma.domain.dsl.iaas.openstack.identity.response.OpenstackGetTokenResponse;
import it.prisma.domain.dsl.iaas.openstack.identity.response.OpenstackTenantListResponse;
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

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class IdentityOpenstackAPIClient extends AbstractOpenstackAPIClient {

	/**
	 * 
	 * @param baseURL
	 * @param restClientFactory
	 * @param openstackServiceVersion
	 */
	protected IdentityOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory, String openstackServiceVersion) {
		super(baseURL, restClientFactory, openstackServiceVersion);
	}

	public IdentityOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory,
			String openstackServiceVersion, String tokenID) {
		super(baseURL, restClientFactory, openstackServiceVersion, tokenID);

	}

	public IdentityOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory,
			String openstackServiceVersion, String tenant, String user,
			String pwd, IdentityOpenstackAPIClient idOSClient)
			throws OpenstackAuthenticationException {
		super(baseURL, restClientFactory, openstackServiceVersion, tenant,
				user, pwd, idOSClient);
	}

	/**
	 * BETA
	 * 
	 * 
	 * 
	 * @param request
	 * @return
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws OpenstackAPIErrorException
	 * @throws RestClientException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public OpenstackGetTokenResponse generateToken(
			OpenstackGetTokenRequest request) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			OpenstackAPIErrorException, RestClientException,
			JsonParseException, JsonMappingException, IOException {

		String URL = baseWSUrl + "/" + getServiceVersion() + "/tokens";

		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new OpenstackRestResponseDecoder<OpenstackGetTokenResponse>(
						OpenstackGetTokenResponse.class), null);

		return handleResult(result);
	}

	public OpenstackTenantListResponse listTenants(
			OpenstackGetTokenRequest request) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			OpenstackAPIErrorException, RestClientException,
			JsonParseException, JsonMappingException, IOException {

		String URL = baseWSUrl + "/" + getServiceVersion() + "/tenants";

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(this.tokenID);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new OpenstackRestResponseDecoder<OpenstackTenantListResponse>(
						OpenstackTenantListResponse.class), null);

		return handleResult(result);
	}

}