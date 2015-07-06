package it.monitoringpillar.adapter.zabbix.handler;

import it.monitoringpillar.config.EnvironmentDeployConfig;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.JSONRPCRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixAuthenticationRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixAuthenticationResponse;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.ZabbixAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.inject.Singleton;

@Startup
@Singleton
public class ZabbixToken implements Serializable{

	private static final long serialVersionUID = 1L;

	private String usr;
	private String pswd;
	private String jsonrpc;
	private String method;
	private String token;
	private int id;
	private ZabbixAPIClient zabClient;
	private ZabbixAuthenticationRequest auth;
	private JSONRPCRequest<ZabbixAuthenticationRequest> authRequest;
	private ZabbixAuthenticationResponse authResp;

//	@EJB 
//	private PillarEnvironmentConfig envConfig;
	
	@EJB 
	private EnvironmentDeployConfig envDeplConfig;

	@PostConstruct
	public void init() throws IOException
	{
		this.jsonrpc = "2.0";
		this.method = "user.login";
		this.id = 1;
		this.token = null;
		this.zabClient = new ZabbixAPIClient("");
		this.auth = new ZabbixAuthenticationRequest();
		this.authRequest = new JSONRPCRequest<ZabbixAuthenticationRequest>();
		this.authResp = new ZabbixAuthenticationResponse();
	}

	public String getZabbixToken(String testbed, String url) 
			throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, 
			APIErrorException, RestClientException, Exception {

		zabClient = new ZabbixAPIClient(
				url );

		if(testbed.equals(envDeplConfig.getMonitoringEnvironment()))
		{
			usr = envDeplConfig.getUsernameZabbixServer();
			pswd = envDeplConfig.getPswdZabbixServer();
		}
		else throw new Exception("Testbed inserted does not exist");

		authRequest.setJsonrpc(jsonrpc);
		authRequest.setMethod(method);

		auth.setUser(usr); 		
		auth.setPassword(pswd);

		authRequest.setParams(auth);
		authRequest.setId(id);

		authResp.setResult(zabClient.authentication(authRequest));
		token = (String) authResp.getResult();
		return token;
	}

}