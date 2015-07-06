package it.prisma.utils.web.ws.rest.apiclients.zabbix;

import it.prisma.domain.dsl.cloudify.response.ApplicationDeploymentResponse;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.MonitoringVMCredentialsResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.HostGroupParamRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.JSONRPCRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.JsonRpcDeleteHostRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.TriggerParamRequestByGroup;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.Zabbix2_4ParamHost;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixAuthenticationRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamCreateHostRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamGraphRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamHostGroupRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamItemRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTemplateRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTriggerRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamUpdate;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.HostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.JSONRPCResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.UpdatedItemsResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV2_4;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponseV2_4;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClient;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactoryImpl;
import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
//import it.prisma.domain.dsl.zabbix.response.JSONRPCResponse4ItemTrigger;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class contains Prisma BizLib Rest API requests implementation.
 * 
 */
public class ZabbixAPIClient {

	private String baseURL;
	private RestClientFactory restClientFactory;
	private RestClient<BaseRestResponseResult> restClient;

	/**
	 * Creates a {@link ZabbixAPIClient} using the default
	 * {@link RestClientFactoryImpl}.
	 * 
	 * @param bizWSURL
	 *            The URL of Prisma BizWS.
	 */
	public ZabbixAPIClient(String baseURL) {
		this(baseURL, new RestClientFactoryImpl());
	}

	/**
	 * Creates a {@link ZabbixAPIClient} with the given {@link RestClientFactory}.
	 * 
	 * @param bizWSURL
	 *            The URL of Prisma BizWS.
	 * @param restClientFactory
	 *            The custom factory for the {@link RestClient}.
	 */
	public ZabbixAPIClient(String bizWSURL, RestClientFactory restClientFactory) {
		super();
		this.baseURL = bizWSURL;
		this.restClientFactory = restClientFactory;
		this.restClient = restClientFactory.getRestClient();
	}

	public String getBaseURL() {
		return baseURL;
	}

	public RestClientFactory getRestClientFactory() {
		return restClientFactory;
	}

	public RestClient<BaseRestResponseResult> getRestClient() {
		return restClient;
	}

	/**
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String authentication(
			JSONRPCRequest<ZabbixAuthenticationRequest> request)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		try {

			String URL = baseURL;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoder<String>(String.class),
					null);

			if (result.getStatus() == Status.OK) {
				try {
					return  ((JSONRPCResponse<String>) result
							.getResult()).getResult();
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse<String>) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	/*
	 * Client for getting the list of host groups
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixHostGroupResponse> getHostGroup(
			JSONRPCRequest<ZabbixParamHostGroupRequest> request, String urlRuntime)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, JsonParseException, JsonMappingException, IOException {

		try {
			String URL = baseURL;
			if (urlRuntime==null){
			}
			else URL = urlRuntime;
			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(
					URL, 
					headers, 
					ge, 
					RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixHostGroupResponse>>(
							ArrayList.class, ZabbixHostGroupResponse.class), null);

			if (result.getStatus() == Status.OK) {
				try {

					ArrayList<ZabbixHostGroupResponse> finalresult = 
							(ArrayList<ZabbixHostGroupResponse>) ((JSONRPCResponse) result.
									getResult()).getResult();

					return finalresult;

				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		}
	}

	/*
	 * Client for getting the list of Template
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixTemplateResponse> getTemplate(
			JSONRPCRequest<ZabbixParamHostGroupRequest> request)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		try {

			String URL = baseURL;
			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(
					URL, 
					headers, 
					ge, 
					RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixTemplateResponse>>(
							ArrayList.class, ZabbixTemplateResponse.class), null);

			if (result.getStatus() == Status.OK) {
				try {

					ArrayList<ZabbixTemplateResponse> finalresult = 
							(ArrayList<ZabbixTemplateResponse>) ((JSONRPCResponse) result.
									getResult()).getResult();

					return finalresult;

				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * Retrieves the list of Monitored hosts (Zabbix Agents installed into Openstack Nodes)
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getHosts4Orchestrator(
			JSONRPCRequest<ZabbixParamRequest> request)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		try {

			String URL = baseURL;
			// userId;
			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoder<ZabbixMonitoredHostsResponse>(ZabbixMonitoredHostsResponse.class),
					null);

			if (result.getStatus() == Status.OK) {
				try {
					return ((JSONRPCResponse<ZabbixMonitoredHostsResponse>) result
							//							.getResult()).getResult();
							.getResult());
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixMonitoredHostsResponse> getHosts4Adapter(
			JSONRPCRequest<ZabbixParamRequest> request, String urlRuntime)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		try {
			String URL = baseURL;
			if(urlRuntime==null){
			}
			else URL = urlRuntime;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixMonitoredHostsResponse>>(
							ArrayList.class, ZabbixMonitoredHostsResponse.class), null);

			if (result.getStatus() == Status.OK) {
				try {
					ArrayList<ZabbixMonitoredHostsResponse> finalresult = 
							(ArrayList<ZabbixMonitoredHostsResponse>) ((JSONRPCResponse) result.
									getResult()).getResult();
					return finalresult;

				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/************
	 * TEMPORARY
	 * @param request
	 * @param urlRuntime
	 * @return
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws APIErrorException
	 * @throws RestClientException
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixMonitoredHostsResponse> getHosts2_4Temporary(
			JSONRPCRequest<Zabbix2_4ParamHost> request, String urlRuntime)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		try {
			String URL = baseURL;
			if(urlRuntime==null){
			}
			else URL = urlRuntime;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixMonitoredHostsResponse>>(
							ArrayList.class, ZabbixMonitoredHostsResponse.class), null);

			if (result.getStatus() == Status.OK) {
				try {
					ArrayList<ZabbixMonitoredHostsResponse> finalresult = 
							(ArrayList<ZabbixMonitoredHostsResponse>) ((JSONRPCResponse) result.
									getResult()).getResult();
					return finalresult;

				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	/**************************************************************************
	 * GET HOSTS FILTERED BY NEW ZABBIX 2.4 (extended with items and templates)
	 **************************************************************************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixMonitoredHostResponseV2_4> getHostsFromZabbix2_4(
			JSONRPCRequest<Zabbix2_4ParamHost> request, String urlRuntime)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		try {
			String URL = baseURL;
			if(urlRuntime==null){
			}
			else URL = urlRuntime;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixMonitoredHostResponseV2_4>>(
							ArrayList.class, ZabbixMonitoredHostResponseV2_4.class), null);

			if (result.getStatus() == Status.OK) {
				try {
					ArrayList<ZabbixMonitoredHostResponseV2_4> finalresult = 
							(ArrayList<ZabbixMonitoredHostResponseV2_4>) ((JSONRPCResponse) result.
									getResult()).getResult();
					return finalresult;

				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}



	/*
	 * Client for item
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONRPCResponse<ZabbixItemResponse> getItem(
			JSONRPCRequest<ZabbixParamItemRequest> request)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		try {

			String URL = baseURL;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixItemResponse>>(
							ArrayList.class, ZabbixItemResponse.class), null);

			if (result.getStatus() == Status.OK) {
				try {
					return ((JSONRPCResponse<ZabbixItemResponse>) (JSONRPCResponse)result
							//							.getResult()).getResult();
							.getResult());
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	/*
	 * Client for Item Prisma Protocol
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getItemPrismaProtocol(
			JSONRPCRequest<ZabbixParamItemRequest> request)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		try {

			String URL = baseURL;// + "/biz/rest/accounting/user/find/" +
			// userId;
			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoder<ZabbixItemResponse>(ZabbixItemResponse.class),
					null);

			if (result.getStatus() == Status.OK) {
				try {
					return ((JSONRPCResponse<ZabbixItemResponse>) result
							//							.getResult()).getResult();
							.getResult());
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * Client for item to be wrapped without zabbix notation 
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixItemResponse> getItemNOZabbixWrappingResponse(
			JSONRPCRequest<ZabbixParamItemRequest> request, String urlRuntime)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {

		try {
			String URL = baseURL;
			if(urlRuntime==null){
			}
			else URL = urlRuntime;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixItemResponse>>(
							ArrayList.class, ZabbixItemResponse.class), null);			

			if (result.getStatus() == Status.OK) {
				try {

					ArrayList<ZabbixItemResponse> finalresult = 
							(ArrayList<ZabbixItemResponse>) ((JSONRPCResponse) result.
									getResult()).getResult();
					return finalresult;

				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	/*
	 * Client for item to be wrapped without zabbix notation 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixItemResponse> getFilterdItemNOZabbixWrappingResponse(
			JSONRPCRequest<ZabbixParamTemplateRequest> request)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {
		try {
			String URL = baseURL;// + "/biz/rest/accounting/user/find/" +
			// userId;
			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixItemResponse>>(
							ArrayList.class, ZabbixItemResponse.class), null);

			if (result.getStatus() == Status.OK) {
				try {

					ArrayList<ZabbixItemResponse> finalresult = 
							(ArrayList<ZabbixItemResponse>) ((JSONRPCResponse) result.
									getResult()).getResult();
					return finalresult;

				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	/*
	 * Client for Trigger returned to be used by Adapter Logic
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixItemResponse> getTrigger4Adapter(
			JSONRPCRequest<ZabbixParamTriggerRequest> request)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {
		try {
			String URL = baseURL;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixItemResponse>>(
							ArrayList.class, ZabbixItemResponse.class), null);			

			if (result.getStatus() == Status.OK) {
				try {

					ArrayList<ZabbixItemResponse> finalresult = 
							(ArrayList<ZabbixItemResponse>) ((JSONRPCResponse) result.
									getResult()).getResult();

					return finalresult;						

				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}



	/*
	 * Client for Trigger returned to be used by adapter By passing group (useful for IaasHealth)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixItemResponse> getTriggerByGroup(
			JSONRPCRequest<TriggerParamRequestByGroup> request)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {
		try {
			String URL = baseURL;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixItemResponse>>(
							ArrayList.class, ZabbixItemResponse.class), null);			

			if (result.getStatus() == Status.OK) {
				try {

					ArrayList<ZabbixItemResponse> finalresult = 
							(ArrayList<ZabbixItemResponse>) ((JSONRPCResponse) result.
									getResult()).getResult();

					return finalresult;						

				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * Client for asking Graph for each of the preset items
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ZabbixItemResponse> getGraph(
			JSONRPCRequest<ZabbixParamGraphRequest> request)
					throws MappingException, NoMappingModelFoundException,
					ServerErrorResponseException, APIErrorException,
					RestClientException, Exception {
		try {
			String URL = baseURL;// + "/biz/rest/accounting/user/find/" +
			// userId;
			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixItemResponse>>(
							ArrayList.class, ZabbixItemResponse.class), null);			

			if (result.getStatus() == Status.OK) {
				try {
					ArrayList<ZabbixItemResponse> finalresult = 
							(ArrayList<ZabbixItemResponse>) ((JSONRPCResponse) result.
									getResult()).getResult();

					return finalresult;
				} catch (Exception e) 
				{
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	@SuppressWarnings("unchecked")
	public <T> JSONRPCResponse<T> getHostGroupCreationClient(
			JSONRPCRequest<HostGroupParamRequest> request, String urlRuntime)
					throws Exception {

		try {
			String URL = baseURL;
			if(urlRuntime==null){
			}
			else URL = urlRuntime;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult baseResult = new BaseRestResponseResult(null, null, null, null);
			if(request.getMethod().equals("host.massupdate")){
				baseResult = restClient.postRequest(URL,
						headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
						new ZabbixRestResponseDecoder<MonitoringVMCredentialsResponse>(MonitoringVMCredentialsResponse.class),
						null);
			}
			else baseResult = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoder<HostGroupResponse>(HostGroupResponse.class),
					null);

			if (baseResult.getStatus() == Status.OK) {
				try {
					return  ((JSONRPCResponse<T>) baseResult
							.getResult());//.getResult();
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, baseResult.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						baseResult.getOriginalRestMessage(),
						((JSONRPCResponse) baseResult.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	/*
	 * Client for creatimg the host from Adapter
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONRPCResponse<MonitoringVMCredentialsResponse> getHostCreationClient(
			JSONRPCRequest<ZabbixParamCreateHostRequest> request, String URLRuntime) throws Exception
			{
		try {
			//String URL = baseURL;
			String URL = URLRuntime;
			// userId;
			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoder<MonitoringVMCredentialsResponse>(MonitoringVMCredentialsResponse.class),
					null);

			if (result.getStatus() == Status.OK) {
				try {
					return ((JSONRPCResponse<MonitoringVMCredentialsResponse>) result
							.getResult());
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
			}


	@SuppressWarnings("unchecked")
	public JSONRPCResponse<MonitoringVMCredentialsResponse> getHostDeleteClient(
			JsonRpcDeleteHostRequest request, String urlRuntime)
					throws Exception {

		try {
			String URL = baseURL;
			if(urlRuntime==null){
			}
			else URL = urlRuntime;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoder<MonitoringVMCredentialsResponse>(MonitoringVMCredentialsResponse.class),
					null);

			if (result.getStatus() == Status.OK) {
				try {
					return ((JSONRPCResponse<MonitoringVMCredentialsResponse>) result
							.getResult());//.getResult();
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public JSONRPCResponse<HostGroupResponse> getHostGroupDeleteClient(
			JsonRpcDeleteHostRequest request, String urlRuntime)
					throws Exception {

		try {
			String URL = baseURL;
			if(urlRuntime==null){
			}
			else URL = urlRuntime;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult result = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoder<HostGroupResponse>(HostGroupResponse.class),
					null);

			if (result.getStatus() == Status.OK) {
				try {
					return ((JSONRPCResponse<HostGroupResponse>) result
							.getResult());//.getResult();
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> JSONRPCResponse<T> getUpdateHostItemClient(
			JSONRPCRequest<ZabbixParamUpdate> request, String urlRuntime)
					throws Exception {

		try {
			String URL = baseURL;
			if(urlRuntime==null){
			}
			else URL = urlRuntime;

			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(request);

			BaseRestResponseResult baseResult = new BaseRestResponseResult(null, null, null, null);
			if(request.getMethod().equals("host.massupdate")){
				baseResult = restClient.postRequest(URL,
						headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
						new ZabbixRestResponseDecoder<MonitoringVMCredentialsResponse>(MonitoringVMCredentialsResponse.class),
						null);
			}
			else baseResult = restClient.postRequest(URL,
					headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoder<UpdatedItemsResponse>(UpdatedItemsResponse.class),
					null);

			if (baseResult.getStatus() == Status.OK) {
				try {
					return  ((JSONRPCResponse<T>) baseResult
							.getResult());//.getResult();
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, baseResult.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						baseResult.getOriginalRestMessage(),
						((JSONRPCResponse) baseResult.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	
	/****************************************************
	 * CLIENT FOR HANDLING TEMPLATES WITH ITEMS EXTENDED
	 * @param requestTemplate
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ZabbixTemplateResponseV2_4> getTemplateByItems(
			JSONRPCRequest<ZabbixParamTemplateRequest> requestTemplate, String url) throws Exception{

		try {

			String URL = url;
			MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

			GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
			.build(requestTemplate);

			BaseRestResponseResult result = restClient.postRequest(
					URL, 
					headers, 
					ge, 
					RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
					new ZabbixRestResponseDecoderV2<ArrayList<ZabbixTemplateResponseV2_4>>(
							ArrayList.class, ZabbixTemplateResponseV2_4.class), null);

			if (result.getStatus() == Status.OK) {
				try {

					ArrayList<ZabbixTemplateResponseV2_4> finalresult = 
							(ArrayList<ZabbixTemplateResponseV2_4>) ((JSONRPCResponse) result.
									getResult()).getResult();

					return finalresult;
					
				} catch (Exception e) {
					throw new MappingException("Unexpected response type.",
							null, result.getOriginalRestMessage());
				}
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((JSONRPCResponse) result.getResult()).getError());
			}
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | RestClientException me) {
			me.printStackTrace();
			throw me;
		} catch (APIErrorException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}