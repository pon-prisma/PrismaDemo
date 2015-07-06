package it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.zabbix.dsl;
//package it.prisma.businesslayer.bizlib.rest.Zabbix.dsl;
//
//import it.prisma.businesslayer.bizlib.rest.Zabbix.dsl.response.AuthenticationResponse;
//import it.prisma.domain.dsl.cloudify.response.ApplicationDeploymentResponse;
//import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseDecoder;
//import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
//import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult.StatusType;
//import it.prisma.utils.web.ws.rest.restclient.RestClient;
//import it.prisma.utils.web.ws.rest.restclient.RestClientFactoryImpl;
//import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
//
//import javax.ws.rs.core.GenericEntity;
//import javax.ws.rs.core.MultivaluedMap;
//
//import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
//
//public class ZabbixRestClient {
//
//	public class ZabbixRestClientAPIException extends Exception {
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -8137549286756266453L;
//
//		private BaseRestResponseResult response;
//
//		public BaseRestResponseResult getResponse() {
//			return response;
//		}
//
//		public boolean hasResponse() {
//			return response != null;
//		}
//
//		public ZabbixRestClientAPIException(String msg, Throwable t) {
//			super(msg, t);
//		}
//
//		public ZabbixRestClientAPIException(String msg,
//				BaseRestResponseResult response) {
//			super(msg);
//			this.response = response;
//		}
//
//		public ZabbixRestClientAPIException(BaseRestResponseResult response) {
//			super();
//			this.response = response;
//		}
//	}
//
//	private String baseURL;
//	private RestClient<BaseRestResponseResult> restClient;
//
//	public ZabbixRestClient(String baseURL) {
//		super();
//		this.baseURL = baseURL;
//		this.restClient = new RestClientFactoryImpl().getRestClient();
//	}
//
//	public String getBaseURL() {
//		return baseURL;
//	}
//
//	public void setBaseURL(String baseURL) {
//		this.baseURL = baseURL;
//	}
//
//	/**
//	 * BETA ! <br/>
//	 * Deploys the application on Cloudify.
//	 * 
//	 * @param appName
//	 * @return the response of the API, {@link ApplicationDeploymentResponse}.
//	 * @throws CloudifyRestClientAPIException
//	 *             if there was an exception during the request.
//	 * @throws CloudifyRestClientAPIException
//	 *             if the content of the response is unexpected or an error
//	 *             message from the API. In this case the exception
//	 *             <b>includes the response</b> ({@link BaseRestResponseResult}) for further inspection.
//	 */
//	public AuthenticationResponse Authentication(Object request)
//			throws ZabbixRestClientAPIException {
//
//		try {
//
//			String URL = baseURL + "/auth...";
//			//ApplicationDeploymentRequest appDeploymentReq = new ApplicationDeploymentRequest();
//			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
//
//			// TODO: Fill in ApplicationDeploymentRequest data.
//			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
//					.build(request);
//
//			BaseRestResponseResult result = restClient.postRequest(URL,
//					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
//					new BaseRestResponseDecoder(),
//					new CloudifyAPIApplicationDeploymentRRDStrategy());
//
//			if (result.getStatus() == StatusType.OK) {
//				try {
//					return (AuthenticationResponse) result.getResult();
//				} catch (Exception e) {
//					throw new ZabbixRestClientAPIException(
//							"Unexpected response type.", result);
//				}
//			} else {
//				throw new ZabbixRestClientAPIException("Response is error.",
//						result);
//			}
//		} catch (ZabbixRestClientAPIException ce) {
//			throw ce;
//		} catch (Exception e) {
//			throw new ZabbixRestClientAPIException("Exception occurred.", e);
//		}
//	}
//}
