package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify;

import it.prisma.domain.dsl.cloudify.request.ApplicationDeploymentRequest;
import it.prisma.domain.dsl.cloudify.response.ApplicationDeploymentResponse;
import it.prisma.domain.dsl.cloudify.response.ApplicationDescriptionResponse;
import it.prisma.domain.dsl.cloudify.response.ApplicationUndeploymentResponse;
import it.prisma.domain.dsl.cloudify.response.ResponseWrapper;
import it.prisma.domain.dsl.cloudify.response.ServiceInstanceDescriptionResponse;
import it.prisma.domain.dsl.cloudify.response.UploadRecipeResponse;
import it.prisma.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class contains Cloudify Rest API requests implementation.
 * 
 * @author l.biava
 * 
 */
public class CloudifyAPIClient extends AbstractAPIClient {

	protected String cfyVersion;

	/**
	 * @see AbstractAPIClient#AbstractAPIClient(String)
	 * @param baseURL
	 * @param cfyVersion
	 */
	public CloudifyAPIClient(String baseURL, String cfyVersion) {
		super(baseURL);
		this.cfyVersion = cfyVersion;
	}

	/**
	 * @see AbstractAPIClient#AbstractAPIClient(String)
	 * @param baseURL
	 * @param restClientFactory
	 * @param cfyVersion
	 */
	public CloudifyAPIClient(String baseURL,
			RestClientFactory restClientFactory, String cfyVersion) {
		super(baseURL, restClientFactory);
		this.cfyVersion = cfyVersion;
	}

	public String getVersion() {
		return cfyVersion;
	}

	public void setVersion(String cfyVersion) {
		this.cfyVersion = cfyVersion;
	}

	// protected void
	// handleUnexpectedResponseTypeException(BaseRestResponseResult result)
	// throws MappingException {
	// throw new MappingException("Unexpected response type.", null,
	// result.getOriginalRestMessage());
	// }

	@SuppressWarnings("unchecked")
	protected <RespType> void handleErrorResult(BaseRestResponseResult result)
			throws CloudifyAPIErrorException {
		CloudifyError cfyError = new CloudifyError();

		try {
			cfyError.setMessageId(((ResponseWrapper<RespType>) result
					.getResult()).getMessageId());

		} catch (Exception e) {
			cfyError.setMessageId("unknown_error");
		}

		throw new CloudifyAPIErrorException("API_ERROR", null,
				result.getOriginalRestMessage(), cfyError);
	}

	@SuppressWarnings("unchecked")
	protected <RespType> RespType handleSuccessResult(
			BaseRestResponseResult result) throws MappingException {
		try {

			return ((ResponseWrapper<RespType>) result.getResult())
					.getResponse();
		} catch (Exception e) {
			throw new MappingException("Unexpected response type.", null,
					result.getOriginalRestMessage());
		}
	}

	protected <RespType> RespType handleResult(BaseRestResponseResult result)
			throws MappingException, CloudifyAPIErrorException {
		if (result.getStatus() == Status.OK)
			return handleSuccessResult(result);
		else
			handleErrorResult(result);

		return null;
	}

	/**
	 * BETA ! <br/>
	 * Deploys the application on Cloudify.
	 * 
	 * @param appName
	 * @return the response of the API, {@link ApplicationDeploymentResponse}.
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws CloudifyRestClientAPIException
	 *             if there was an exception during the request.
	 * @throws CloudifyRestClientAPIException
	 *             if the content of the response is unexpected or an error
	 *             message from the API. In this case the exception <b>includes
	 *             the response</b> ({@link BaseRestResponseResult}) for further
	 *             inspection.
	 */
	public ApplicationDeploymentResponse ApplicationDeployment(String appName,
			ApplicationDeploymentRequest request) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			CloudifyAPIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/" + cfyVersion + "/deployments/" + appName;
		// ApplicationDeploymentRequest appDeploymentReq = new
		// ApplicationDeploymentRequest();
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new CloudifyRestResponseDecoder<ApplicationDeploymentResponse>(
						ApplicationDeploymentResponse.class), null);

		return handleResult(result);
	}

	/**
	 * BETA ! <br/>
	 * Uploads a recipe on Cloudify.
	 * 
	 * @throws ServerErrorResponseException
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws RestClientException
	 * @throws CloudifyAPIErrorException
	 */
	public UploadRecipeResponse UploadRecipe(String fileName, File file)
			throws RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			CloudifyAPIErrorException {

		String URL = baseWSUrl + "/" + cfyVersion + "/upload/" + fileName;
		// ApplicationDeploymentRequest appDeploymentReq = new
		// ApplicationDeploymentRequest();
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

		// TODO: Fill in ApplicationDeploymentRequest data.
		GenericEntity<MultipartFormDataOutput> ge = new RestClientHelper.FormDataEntityBuilder()
				.addFormData("file", file,
						RestClientHelper.FormDataEntityBuilder.MEDIA_TYPE,
						fileName).build();

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.FormDataEntityBuilder.MEDIA_TYPE,
				new CloudifyRestResponseDecoder<UploadRecipeResponse>(
						UploadRecipeResponse.class), null);

		return handleResult(result);

	}

	/**
	 * BETA ! <br/>
	 * Application description.
	 * 
	 * @throws ServerErrorResponseException
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws RestClientException
	 * @throws CloudifyAPIErrorException
	 */
	public ApplicationDescriptionResponse ApplicationDescription(String appName)
			throws  RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			CloudifyAPIErrorException {

		String URL = baseWSUrl + "/" + cfyVersion
				+ "/deployments/applications/" + appName + "/description";
		// ApplicationDeploymentRequest appDeploymentReq = new
		// ApplicationDeploymentRequest();
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new CloudifyRestResponseDecoder<ApplicationDescriptionResponse>(
								ApplicationDescriptionResponse.class), null);

		return handleResult(result);
	}

	/**
	 * BETA ! <br/>
	 * Service instance description.
	 * 
	 * @throws ServerErrorResponseException
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws RestClientException
	 * @throws CloudifyAPIErrorException
	 */
	public ServiceInstanceDescriptionResponse ServiceInstanceDescription(
			String appName, String serviceName, String instanceId)
			throws RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException,
			CloudifyAPIErrorException {

		String URL = baseWSUrl + "/" + cfyVersion + "/deployments/" + appName
				+ "/service/" + serviceName + "/instances/" + instanceId
				+ "/metadata";

		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new CloudifyRestResponseDecoder<ServiceInstanceDescriptionResponse>(
								ServiceInstanceDescriptionResponse.class), null);

		return handleResult(result);
	}

	public ApplicationUndeploymentResponse applicationUndeployment(
			String applicationName) throws RestClientException, 
			NoMappingModelFoundException, MappingException, ServerErrorResponseException, 
			CloudifyAPIErrorException {
		String URL = baseWSUrl + "/" + cfyVersion + "/deployments/" + applicationName;
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

		BaseRestResponseResult result = restClient.deleteRequest(URL, headers,
				null, null,
				new CloudifyRestResponseDecoder<ApplicationUndeploymentResponse>(
						ApplicationUndeploymentResponse.class), null);

		return handleResult(result);
	}

}
