package it.prisma.businesslayer.bizlib.rest.apiclients.monitoring;

import it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.api.MonitoringAPICheckHostRRDStrategy;
import it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.dsl.CheckHostResponse;
import it.prisma.domain.dsl.cloudify.response.ApplicationDeploymentResponse;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseDecoder;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClient;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactoryImpl;

import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

/**
 * This class contains Cloudify Rest API requests implementation.
 * 
 * @author l.biava
 * 
 */
public class MonitoringRestClient {

	public class MonitoringRestClientAPIException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8137549286756266453L;

		private BaseRestResponseResult response;

		public BaseRestResponseResult getResponse() {
			return response;
		}

		public boolean hasResponse() {
			return response != null;
		}

		public MonitoringRestClientAPIException(String msg, Throwable t) {
			super(msg, t);
		}

		public MonitoringRestClientAPIException(String msg,
				BaseRestResponseResult response) {
			super(msg);
			this.response = response;
		}

		public MonitoringRestClientAPIException(BaseRestResponseResult response) {
			super();
			this.response = response;
		}
	}

	private String baseURL;
	private RestClient<BaseRestResponseResult> restClient;

	public MonitoringRestClient(String baseURL) {
		super();
		this.baseURL = baseURL;
		this.restClient = new RestClientFactoryImpl().getRestClient();
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * BETA ! <br/>
	 * Deploys the application on Cloudify.
	 * 
	 * @param appName
	 * @return the response of the API, {@link ApplicationDeploymentResponse}.
	 * @throws CloudifyRestClientAPIException
	 *             if there was an exception during the request.
	 * @throws CloudifyRestClientAPIException
	 *             if the content of the response is unexpected or an error
	 *             message from the API. In this case the exception <b>includes
	 *             the response</b> ({@link BaseRestResponseResult}) for further
	 *             inspection.
	 */
	public CheckHostResponse CheckHost(String hostName)
			throws MonitoringRestClientAPIException {

		try {
			String URL = baseURL + "/monitoring/MonitoringLayer/hosts-running";
			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			/*
			 * GenericEntity<String> ge = new
			 * RestClientHelper.JsonEntityBuilder() .build(request);
			 */

			BaseRestResponseResult result = restClient.getRequest(URL, headers,
					new BaseRestResponseDecoder(),
					new MonitoringAPICheckHostRRDStrategy());

			if (result.getStatus() == javax.ws.rs.core.Response.Status.OK) {
				try {
					return ((CheckHostResponse) result.getResult());

				} catch (Exception e) {
					throw new MonitoringRestClientAPIException(
							"Unexpected response type.", result);
				}

			} else {
				throw new MonitoringRestClientAPIException(
						"Response is error.", result);
			}
		} catch (MonitoringRestClientAPIException ce) {
			throw ce;
		} catch (Exception e) {
			throw new MonitoringRestClientAPIException("Exception occurred.", e);
		}
	}

}
