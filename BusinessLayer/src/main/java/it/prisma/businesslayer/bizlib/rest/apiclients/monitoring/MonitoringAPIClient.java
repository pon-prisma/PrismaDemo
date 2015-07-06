package it.prisma.businesslayer.bizlib.rest.apiclients.monitoring;

import it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.api.MonitoringAPICheckHostRRDStrategy;
import it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.dsl.CheckHostResponse;
import it.prisma.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseDecoder;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

/**
 * This class contains Monitoring platform Rest API requests implementation.
 * 
 * @author l.biava
 * 
 */
public class MonitoringAPIClient extends AbstractAPIClient {

	/**
	 * @see AbstractAPIClient#AbstractAPIClient(String)
	 * @param bizWSURL
	 */
	public MonitoringAPIClient(String bizWSURL) {
		super(bizWSURL);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see AbstractAPIClient#AbstractAPIClient(String, RestClientFactory)cho
	 * @param bizWSURL
	 * @param restClientFactory
	 */
	public MonitoringAPIClient(String bizWSURL,
			RestClientFactory restClientFactory) {
		super(bizWSURL, restClientFactory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Consumes checkHost API from Monitoring pillar.
	 * 
	 * @param hostName
	 *            the hostName of the host to check the status of.
	 * @return the response of the API, {@link CheckHostResponse}.
	 * @throws MappingException
	 *             if there was a response mapping exception.
	 * @throws RestClientException
	 *             if there was an exception during the request.
	 * @throws APIErrorException
	 *             if the API reported an error.
	 * @throws ServerErrorResponseException
	 *             if the error responded with an error.
	 */
	public CheckHostResponse checkHost(String hostName)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException {

		// TODO: Convertire le risposte del monitoring con protocollo Prisma
		String URL = baseWSUrl + "/monitoring/MonitoringLayer/hosts-running";
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

		/*
		 * BaseRestResponseResult result = restClient.getRequest(URL, headers,
		 * new PrismaRestResponseDecoder<UserInfo>(UserInfo.class), null);
		 */

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new BaseRestResponseDecoder(),
				new MonitoringAPICheckHostRRDStrategy());

		if (result.getStatus() == Status.OK) {
			try {
				return ((CheckHostResponse) result.getResult());

			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}

		} else {
			/*
			 * throw new APIErrorException("API_ERROR", null,
			 * result.getOriginalRestMessage(), ((PrismaResponseWrapper)
			 * result.getResult()).getError());
			 */
			//TODO: passare l'errore.
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(), null);
		}

		/*
		 * if (result.getStatus() == StatusType.OK) { try { return
		 * ((PrismaResponseWrapper<UserInfo>) result .getResult()).getResult();
		 * } catch (Exception e) { throw new
		 * MappingException("Unexpected response type.", null,
		 * result.getOriginalRestMessage()); } } else { throw new
		 * APIErrorException("API_ERROR", null, result.getOriginalRestMessage(),
		 * ((PrismaResponseWrapper) result.getResult()).getError()); }
		 */

	}

	// /**
	// * BETA ! <br/>
	// * Deploys the application on Cloudify.
	// *
	// * @param appName
	// * @return the response of the API, {@link ApplicationDeploymentResponse}.
	// * @throws CloudifyRestClientAPIException
	// * if there was an exception during the request.
	// * @throws CloudifyRestClientAPIException
	// * if the content of the response is unexpected or an error
	// * message from the API. In this case the exception <b>includes
	// * the response</b> ({@link BaseRestResponseResult}) for further
	// * inspection.
	// */
	// public CheckHostResponse CheckHost(String hostName)
	// throws MonitoringRestClientAPIException {
	//
	// try {
	// String URL = baseURL + "/monitoring/MonitoringLayer/hosts-running";
	// MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String,
	// Object>();
	//
	// /*
	// * GenericEntity<String> ge = new
	// * RestClientHelper.JsonEntityBuilder() .build(request);
	// */
	//
	// BaseRestResponseResult result = restClient.getRequest(URL, headers,
	// new BaseRestResponseDecoder(),
	// new MonitoringAPICheckHostRRDStrategy());
	//
	// if (result.getStatus() == StatusType.OK) {
	// try {
	// return ((CheckHostResponse) result.getResult());
	//
	// } catch (Exception e) {
	// throw new MonitoringRestClientAPIException(
	// "Unexpected response type.", result);
	// }
	//
	// } else {
	// throw new MonitoringRestClientAPIException(
	// "Response is error.", result);
	// }
	// } catch (MonitoringRestClientAPIException ce) {
	// throw ce;
	// } catch (Exception e) {
	// throw new MonitoringRestClientAPIException("Exception occurred.", e);
	// }
	// }

}
