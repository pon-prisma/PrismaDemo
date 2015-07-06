package it.monitoringpillar.adapter.zabbix;

import it.monitoringpillar.adapter.zabbix.handler.ZabbixToken;
import it.monitoringpillar.config.URL4Pillar;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.ZabbixAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;

@Stateless
public class MonitoringServerRouter {

	static final long serialVersionUID = 1L;

	private String tokenZabbix;
	private String iaasURL;
	private ZabbixAPIClient zabClient;
	private String watcherURL;
	private String metricsURL;
	private String urlRuntime;

	private final static String SERVERMETRICS = "servermetrics";
	private final static String SERVERWATCHER = "serverwatcher";
	private final static String SERVERIAAS = "serveriaas";

	@Inject
	private ZabbixToken token;

	@Inject
	private URL4Pillar urlPillar;

//	public String initAdapter(String testbed, String serverPlatform) 
//			throws MappingException, 
//			NoMappingModelFoundException, ServerErrorResponseException, 
//			it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, 
//			RestClientException, Exception
//	
//		this.iaasURL = urlPillar.getURLfromPillar(testbed, serverType.SERVERIAAS.getServerType());
//		this.zabClient = new ZabbixAPIClient(
//				iaasURL );
	
	
	public String getToken(String testbed, String serverPlatform) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, RestClientException, Exception{

		switch(serverPlatform){

		case SERVERMETRICS:
			
			return token.getZabbixToken(testbed, getURL(testbed, serverPlatform));

		case SERVERIAAS:
			return token.getZabbixToken(testbed, getURL(testbed, serverPlatform));

		case SERVERWATCHER:
			return token.getZabbixToken(testbed, getURL(testbed, serverPlatform));

		default: 
			throw new NamingException("WRONG SERVER TYPE INSERTED for token retriever");
		}
	}

	public String getURL(String testbed, String serverPlatform) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException, RestClientException, Exception{

		switch(serverPlatform){

		case SERVERMETRICS:
			return this.iaasURL = urlPillar.getURLfromPillar(testbed, serverPlatform);

		case SERVERIAAS:
			return this.metricsURL = urlPillar.getURLfromPillar(testbed, serverPlatform);

		case SERVERWATCHER:
			return this.watcherURL = urlPillar.getURLfromPillar(testbed, serverPlatform);

		default: 
			throw new NamingException("WRONG SERVER TYPE INSERTED for THE URL Retriever");
		}
	}
}