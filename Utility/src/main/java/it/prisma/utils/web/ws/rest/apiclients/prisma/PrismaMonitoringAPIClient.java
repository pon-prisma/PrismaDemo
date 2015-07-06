package it.prisma.utils.web.ws.rest.apiclients.prisma;

import it.prisma.domain.dsl.exceptions.monitoring.NotFoundMonitoringException;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.HostIaaS;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.IaaSHostGroups;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringDeleteRequest;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringRequest;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.CreatedHostInServer;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.ZabbixRestResponseDecoder;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.ZabbixRestResponseDecoderV2;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClient;
import it.prisma.utils.web.ws.rest.restclient.RestClient.RestMethod;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactoryImpl;
import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Monitoring Pillar Client. Use PrismaMonitoringAPIClient methods to invoke
 * pillar WS
 * 
 */
public class PrismaMonitoringAPIClient extends AbstractPrismaAPIClient {

    private Logger LOG = LogManager.getLogger(PrismaMonitoringAPIClient.class);

    /**
     * Creates a {@link PrismaBizAPIClient} using the default
     * {@link RestClientFactoryImpl}.
     * 
     * @param baseWSUrl
     *            The URL of Prisma BizWS.
     */
    public PrismaMonitoringAPIClient(String baseURL) {
	this(baseURL, new RestClientFactoryImpl());
    }

    /**
     * Creates a {@link PrismaBizAPIClient} with the given
     * {@link RestClientFactory}.
     * 
     * @param baseWSUrl
     *            The URL of Prisma BizWS.
     * @param restClientFactory
     *            The custom factory for the {@link RestClient}.
     */
    public PrismaMonitoringAPIClient(String baseWSUrl, RestClientFactory restClientFactory) {
	super(baseWSUrl, restClientFactory);
    }

    /**
     * Gets the trigger(only triggered) for the given hostgroup
     * 
     * @param adapterType
     * @param hostGroup
     * @return
     * @throws RestClientException
     * @throws NoMappingModelFoundException
     * @throws MappingException
     * @throws ServerErrorResponseException
     */
    public WrappedIaasHealthByTrigger getShotTriggerByHostGroup(String adapterType, String hostGroup)
	    throws RestClientException, NoMappingModelFoundException, MappingException, ServerErrorResponseException {

	String URL = baseWSUrl + adapterType + "/groups/" + hostGroup;

	MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

	BaseRestResponseResult result = restClient.doRequest(RestMethod.GET, URL, headers, null, null, null, null,
		new PrismaRestResponseDecoder<WrappedIaasHealthByTrigger>(WrappedIaasHealthByTrigger.class), null);

	return handleResult(result);
    }

    /**
     * Create the host to Zabbix metrics and watcher.
     * 
     * HostMonitoringRequest -> atomicServices should be null in case of VMaaS
     * 
     * @param adapterType
     * @param request
     * @returns
     * @throws MappingException
     * @throws APIErrorException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws RestClientException
     * @throws NoMappingModelFoundException
     * @throws ServerErrorResponseException
     */
    public CreatedHostInServer addHostToMonitoring(String adapterType, HostMonitoringRequest request)
	    throws MappingException, APIErrorException, JsonParseException, JsonMappingException, IOException,
	    RestClientException, NoMappingModelFoundException, ServerErrorResponseException {

	String URL = baseWSUrl + adapterType + "/hosts";

	MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

	GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

	BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
		RestClientHelper.JsonEntityBuilder.MEDIA_TYPE, new PrismaRestResponseDecoder<CreatedHostInServer>(
			CreatedHostInServer.class), null);

	return handleResult(result);
    }

    /**
     * Delete service from zabbix metrics and watcher
     * 
     * @param adapterType
     * @param request
     * @return
     * @throws MappingException
     * @throws APIErrorException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws RestClientException
     * @throws NoMappingModelFoundException
     * @throws ServerErrorResponseException
     */
    public CreatedHostInServer deleteHostFromMonitoring(String adapterType, HostMonitoringDeleteRequest request)
	    throws MappingException, APIErrorException, JsonParseException, JsonMappingException, IOException,
	    RestClientException, NoMappingModelFoundException, ServerErrorResponseException,
	    NotFoundMonitoringException {

	String URL = baseWSUrl + adapterType + "/hosts";

	MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

	GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

	try {
	    BaseRestResponseResult result = restClient.deleteRequest(URL, headers, ge,
		    RestClientHelper.JsonEntityBuilder.MEDIA_TYPE, new PrismaRestResponseDecoder<CreatedHostInServer>(
			    CreatedHostInServer.class), null);
	    return handleResult(result);

	    // Il monitoring pillar se l'host da cancellare non lo trova
	    // risponde con APIErrorException ( erore 400)
	} catch (APIErrorException e) {
	    if (e.getAPIError().getErrorCode() == Status.BAD_REQUEST.getStatusCode()) {
		throw new NotFoundMonitoringException("Cannot find " + request.getServiceId()
			+ " on monitoring platform", e);
	    } else {
		throw e;
	    }
	}

    }

    /**
     * IaaS Overall INFO
     * 
     * @return
     * @throws MappingException
     * @throws NoMappingModelFoundException
     * @throws ServerErrorResponseException
     * @throws APIErrorException
     * @throws RestClientException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws TimeoutException
     */
    public MonitoringWrappedResponseIaas getMachineByHostGroup(String adapterType, IaaSHostGroups hostGroup)
	    throws RestClientException, NoMappingModelFoundException, MappingException, ServerErrorResponseException {

	String URL = baseWSUrl + adapterType + "/iaas?iaasType=" + hostGroup.getGroupName();
	MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

	BaseRestResponseResult result = restClient
		.doRequest(RestMethod.GET, URL, headers, null, null, null, null,
			new PrismaRestResponseDecoder<MonitoringWrappedResponseIaas>(
				MonitoringWrappedResponseIaas.class), null);

	return handleResult(result);

    }

    public MonitoringWrappedResponseIaas getItemsByHostGroupAndHost(String adapterType, IaaSHostGroups hostGroup,
	    String hostName) throws RestClientException, NoMappingModelFoundException, MappingException,
	    ServerErrorResponseException {

	String URL = baseWSUrl + adapterType + "/iaas?iaasType=" + hostGroup.getGroupName() + "/iaashost/" + hostName;
	MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

	BaseRestResponseResult result = restClient
		.doRequest(RestMethod.GET, URL, headers, null, null, null, null,
			new PrismaRestResponseDecoder<MonitoringWrappedResponseIaas>(
				MonitoringWrappedResponseIaas.class), null);

	return handleResult(result);
    }

   

    /**
     * Returns all the metrics and treasholds associated to all the monitored
     * hosts with the given ID.
     * 
     * @param adapterType
     *            for example "zabbix". This value must be know to
     *            MonitoringPillar
     * @param serviceGroup
     *            the group in which the service has been added (usually is the
     *            workgroupID)
     * @param serviceId
     *            the paasServiceID
     * @return
     * @throws RestClientException
     * @throws NoMappingModelFoundException
     * @throws MappingException
     * @throws ServerErrorResponseException
     */
    public MonitoringWrappedResponsePaas getMonitoringServiceByTag(String adapterType,
	    String serviceId) throws RestClientException, NoMappingModelFoundException, MappingException,
	    ServerErrorResponseException {

	String URL = baseWSUrl + adapterType.toLowerCase() 
		+ "/hosts?service-id=" + serviceId;

	MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

	BaseRestResponseResult result = restClient
		.getRequest(URL, headers, new PrismaRestResponseDecoder<MonitoringWrappedResponsePaas>(
			MonitoringWrappedResponsePaas.class), null);

	return handleResult(result);
    }

   
    public MonitoringWrappedResponseIaas getPrismaIaasHealth(String adapterType, String iaasType, String iaashost) throws MappingException,
	    NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException,
	    Exception {
	String URL = baseWSUrl + adapterType.toLowerCase() + "/machine?iaasType="+ iaasType + "&iaashost=" + iaashost;

	MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

	BaseRestResponseResult result = restClient
		.getRequest(URL, headers, new PrismaRestResponseDecoder<MonitoringWrappedResponseIaas>(
			MonitoringWrappedResponseIaas.class), null);

	return handleResult(result);
    }

   

}