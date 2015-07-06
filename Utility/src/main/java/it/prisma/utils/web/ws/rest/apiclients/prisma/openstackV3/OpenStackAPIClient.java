package it.prisma.utils.web.ws.rest.apiclients.prisma.openstackV3;

import it.prisma.domain.dsl.iaas.openstackV3.identity.Auth;
import it.prisma.domain.dsl.iaas.openstackV3.identity.Domain;
import it.prisma.domain.dsl.iaas.openstackV3.identity.Identity;
import it.prisma.domain.dsl.iaas.openstackV3.identity.Password;
import it.prisma.domain.dsl.iaas.openstackV3.identity.Scope;
import it.prisma.domain.dsl.iaas.openstackV3.identity.User;
import it.prisma.domain.dsl.iaas.openstackV3.identity.requests.AuthenticationOnDomainRequest;
import it.prisma.domain.dsl.iaas.openstackV3.identity.requests.CreateProjectRequest;
import it.prisma.domain.dsl.iaas.openstackV3.identity.requests.CreateUserProjectRequest;
import it.prisma.domain.dsl.iaas.openstackV3.identity.responses.CreateProjectResponse;
import it.prisma.domain.dsl.iaas.openstackV3.identity.responses.CreateUserProjectResponse;
import it.prisma.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class OpenStackAPIClient extends AbstractAPIClient {

	public OpenStackAPIClient(String baseWSUrl) {
		super(baseWSUrl);
	}
	
	public OpenStackAPIClient(String baseWSUrl,
			RestClientFactory restClientFactory) {
		super(baseWSUrl, restClientFactory);
	}
	
	// Identity *************************************** //

	public String authenticate(final String username, final String password, final String domain) throws MappingException,
	NoMappingModelFoundException, ServerErrorResponseException,
	APIErrorException, RestClientException, JsonParseException,
	JsonMappingException, IOException {
		String URL = baseWSUrl + "v3/auth/tokens";
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(new AuthenticationOnDomainRequest() {{
					this.setAuth(new Auth() {{
						this.setIdentity(new Identity() {{
							List<String> methods = new ArrayList<String>();
							methods.add("password");
							this.setMethods(methods);
							this.setPassword(new Password() {{
								this.setUser(new User() {{
									this.setName(username);
									this.setPassword(password);
									this.setDomain(new Domain() {{ 
										this.setName(domain); 
									}});
								}});
							}});
						}});
						this.setScope(new Scope() {{
							this.setDomain(new Domain() {{
								this.setName(domain); 
							}});
						}});
					}});
				}});
		RestMessage result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE);
		if (result.getHttpStatusCode() == Status.CREATED.getStatusCode()) {
			try {
				return (String) result.getHeaders().getFirst("X-Subject-Token");
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null, null);
			}
		} else {
			throw new APIErrorException("API_ERROR");
		}
	}
	
	public CreateProjectResponse createProject(final CreateProjectRequest request, String authToken)  throws MappingException,
	NoMappingModelFoundException, ServerErrorResponseException,
	APIErrorException, RestClientException, JsonParseException,
	JsonMappingException, IOException  {
		String URL = baseWSUrl + "v3/projects";
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		headers.add("X-Auth-Token", authToken);
		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new OpenstackRestResponseDecoder<CreateProjectResponse>(
						CreateProjectResponse.class), null);
		if (result.getStatus() == Status.OK) {
			try {
				return (CreateProjectResponse) result.getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null, result.getOriginalRestMessage(), null);
		}
	}
	
	public CreateUserProjectResponse createUserProject(final CreateUserProjectRequest request, String authToken)  throws MappingException,
	NoMappingModelFoundException, ServerErrorResponseException,
	APIErrorException, RestClientException, JsonParseException,
	JsonMappingException, IOException  {
		String URL = baseWSUrl + "v3/users";
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		headers.add("X-Auth-Token", authToken);
		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new OpenstackRestResponseDecoder<CreateUserProjectResponse>(
						CreateUserProjectResponse.class), null);
		if (result.getStatus() == Status.OK) {
			try {
				return (CreateUserProjectResponse) result.getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null, result.getOriginalRestMessage(), null);
		}
	}
	
}
