package it.prisma.businesslayer.bizlib.rest.apiclients.dns.bind;

import it.prisma.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class contains Bind DNS Rest API requests implementation.
 * 
 * @author l.biava
 * 
 */
public class BindAPIClient extends AbstractAPIClient {

	/**
	 * @see AbstractAPIClient#AbstractAPIClient(String)
	 * @param baseURL
	 * @param cfyVersion
	 */
	public BindAPIClient(String baseURL) {
		super(baseURL);
	}

	/**
	 * @see AbstractAPIClient#AbstractAPIClient(String)
	 * @param baseURL
	 * @param restClientFactory
	 * @param cfyVersion
	 */
	public BindAPIClient(String baseURL, RestClientFactory restClientFactory) {
		super(baseURL, restClientFactory);
	}

	// @SuppressWarnings("unchecked")
	// protected <RespType> void handleErrorResult(BaseRestResponseResult
	// result)
	// throws BindAPIErrorException {
	// CloudifyError cfyError = new CloudifyError();
	//
	// try {
	// cfyError.setMessageId(((ResponseWrapper<RespType>) result
	// .getResult()).getMessageId());
	//
	// } catch (Exception e) {
	// cfyError.setMessageId("unknown_error");
	// }
	//
	// throw new BindAPIErrorException("API_ERROR", null,
	// result.getOriginalRestMessage(), cfyError);
	// }
	//
	// @SuppressWarnings("unchecked")
	// protected <RespType> RespType handleSuccessResult(
	// BaseRestResponseResult result) throws MappingException {
	// try {
	//
	// return ((ResponseWrapper<RespType>) result.getResult())
	// .getResponse();
	// } catch (Exception e) {
	// throw new MappingException("Unexpected response type.", null,
	// result.getOriginalRestMessage());
	// }
	// }
	//
	// protected <RespType> RespType handleResult(BaseRestResponseResult result)
	// throws MappingException, BindAPIErrorException {
	// if (result.getStatus() == Status.OK)
	// return handleSuccessResult(result);
	// else
	// handleErrorResult(result);
	//
	// return null;
	// }

	/**
	 * Adds a new DNS (A and PTR) record with the given DN and IP to the DNS
	 * server.
	 * 
	 * @param domainName
	 * @param IP
	 * @param authKey
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws BindAPIErrorException
	 * @throws RestClientException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void addDNSRecord(String domainName, String IP, String authKey)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, BindAPIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/dns";

		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		headers.putSingle("HTTP_X_API_KEY", authKey);

		// TODO: Improve DSL
		GenericEntity<String> ge = new GenericEntity<String>(
				"{ \"hostname\": \"" + domainName + "\", \"ip\": \"" + IP
						+ "\" }") {
		};

		// TODO: Decode response
		RestMessage result = restClient.postRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE);

		if (result.getHttpStatusCode() == Status.CREATED.getStatusCode())
			return;
		else
			throw new BindAPIErrorException("Cannot add DNS record.", null,
					result, null);

		// return handleResult(result);
	}

	/**
	 * Adds a new DNS (A and PTR) record with the given DN and IP to the DNS
	 * server.
	 * 
	 * @param domainName
	 * @param IP
	 * @param authKey
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws BindAPIErrorException
	 * @throws RestClientException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void deleteDNSRecord(String domainName, String IP, String authKey)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, BindAPIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/dns";

		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		headers.putSingle("HTTP_X_API_KEY", authKey);

		// TODO: Improve DSL
		GenericEntity<String> ge = new GenericEntity<String>(
				"{ \"hostname\": \"" + domainName + "\", \"ip\": \"" + IP
						+ "\" }") {
		};

		// TODO: Decode response
		RestMessage result = restClient.deleteRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE);

		if (result.getHttpStatusCode() == Status.OK.getStatusCode())
			return;
		else
			throw new BindAPIErrorException("Cannot delete DNS record.", null,
					result, null);

		// return handleResult(result);
	}

}
