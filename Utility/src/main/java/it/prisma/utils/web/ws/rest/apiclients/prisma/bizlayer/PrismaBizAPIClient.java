package it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.accounting.organizations.IdentityProviderRepresentation;
import it.prisma.domain.dsl.accounting.organizations.OrganizationRepresentation;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.users.UsersRepresentation;
import it.prisma.domain.dsl.accounting.users.requests.SignUpUserRequest;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupApprovationRequest;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupCreationRequest;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupMembershipRequest;
import it.prisma.domain.dsl.configuration.PlatformConfiguration;
import it.prisma.domain.dsl.configuration.PlatformConfigurations;
import it.prisma.domain.dsl.deployments.VirtualMachineRepresentation;
import it.prisma.domain.dsl.iaas.network.NetworkRepresentation;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRepresentation;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRequest;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.monitoring.businesslayer.iaas.hypervisor.HypervisorGroup;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.HypervisorInfo;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.IaaSHealth;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.domain.dsl.orchestrator.LRTRepresentation;
import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.response.ApplicationRepositoryEntryRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.caaas.CertificateInfoRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.emailaas.Credential;
import it.prisma.domain.dsl.paas.services.emailaas.EmailDomains;
import it.prisma.domain.dsl.paas.services.emailaas.EmailInfoRepresentation;
import it.prisma.domain.dsl.paas.services.emailaas.history.RowEmail;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.SMSNotificationsRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.history.History;
import it.prisma.domain.dsl.prisma.AccountingErrorCode;
import it.prisma.domain.dsl.prisma.DebugInformations;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.request.AddAppSrcFileRequest;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.response.AddAppSrcFileResponse;
import it.prisma.domain.dsl.prisma.prismaprotocol.Error;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.utils.json.JsonUtility;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.prisma.PrismaRestResponseDecoder;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClient;
import it.prisma.utils.web.ws.rest.restclient.RestClient.RestMethod;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactoryImpl;
import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * This class contains Prisma BizLib Rest API requests implementation.
 * 
 * @author l.biava
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PrismaBizAPIClient extends AbstractPrismaAPIClient {

	private Logger LOG = LogManager.getLogger(PrismaBizAPIClient.class);

	/**
	 * Creates a {@link PrismaBizAPIClient} using the default
	 * {@link RestClientFactoryImpl}.
	 * 
	 * @param baseWSUrl
	 *            The URL of Prisma BizWS.
	 */
	public PrismaBizAPIClient(String baseWSUrl) {
		this(baseWSUrl, new RestClientFactoryImpl());
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
	public PrismaBizAPIClient(String baseWSUrl,
			RestClientFactory restClientFactory) {
		super(baseWSUrl + "/biz/rest", restClientFactory);
	}

	// public AuthTokenRepresentation getAvailableAuthTokenForUser(String
	// userID,
	// String auth_data) throws RestClientException,
	// NoMappingModelFoundException, MappingException,
	// ServerErrorResponseException {
	// String URL = baseWSUrl + "/auth/tokens/" + userID;
	//
	// PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
	// auth_data);
	//
	// BaseRestResponseResult result = restClient.getRequest(URL, headers,
	// new PrismaRestResponseDecoder<AuthTokenRepresentation>(
	// ArrayList.class, AuthTokenRepresentation.class), null);
	// if (result.getStatus() == Status.OK){
	// ArrayList<AuthTokenRepresentation> authTokenList = new ArrayList(
	// ((PrismaResponseWrapper<List<AuthTokenRepresentation>>) result
	// .getResult()).getResult());
	// return authTokenList.size()>0?authTokenList.get(0):null;
	// }else {
	// throw new APIErrorException("API_ERROR", null,
	// result.getOriginalRestMessage(),
	// ((PrismaResponseWrapper) result.getResult()).getError());
	// }
	// }

	/**
	 * Return session token, Not viewable to user
	 * 
	 * @param userID
	 * @param auth_data
	 * @return
	 * @throws RestClientException
	 * @throws NoMappingModelFoundException
	 * @throws MappingException
	 * @throws ServerErrorResponseException
	 */
	public AuthTokenRepresentation getAvailableAuthTokenForUser(String userID,
			String auth_data) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException {
		String URL = baseWSUrl + "/auth/tokens/session/" + userID;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<AuthTokenRepresentation>(
						AuthTokenRepresentation.class), null);
		return handleResult(result, null);
	}

	// Restituzione di tutti i token
	public ArrayList<AuthTokenRepresentation> getAuthTokenForUser(Long userID,
			RestWSParamsContainer params, PrismaMetaData meta, String auth_data)
			throws Exception {

		String URL = baseWSUrl + "/auth/tokens/" + userID;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						URL,
						headers,
						addRestWSParamsToQueryParams(params),
						null,
						null,
						null,
						new PrismaRestResponseDecoder<ArrayList<AuthTokenRepresentation>>(
								ArrayList.class, AuthTokenRepresentation.class),
						null);
		return handleResult(result, meta);
	}

	public AuthTokenRepresentation generateNewSessionAuthTokenForUser(
			String userID, String auth_data) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException {
		String URL = baseWSUrl + "/auth/tokens/session/" + userID;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.putRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<AuthTokenRepresentation>(
						AuthTokenRepresentation.class), null);

		return handleResult(result, null);
	}

	public AuthTokenRepresentation generateNewAuthTokenForUser(String userID,
			String auth_data) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException {
		String URL = baseWSUrl + "/auth/tokens/" + userID;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.putRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<AuthTokenRepresentation>(
						AuthTokenRepresentation.class), null);

		return handleResult(result, null);
		//
		// if (result.getStatus() == Status.OK) {
		// ArrayList<AuthTokenRepresentation> authTokenList = new ArrayList(
		// ((PrismaResponseWrapper<List<AuthTokenRepresentation>>) result
		// .getResult()).getResult());
		// boolean tokenFound = false;
		// int i = 0;
		// AuthTokenRepresentation authToken = null;
		// while ((i < authTokenList.size()) && !tokenFound) {
		// authToken = authTokenList.get(i);
		// tokenFound = AuthTokenManagementUtil.isTokenValid(authToken
		// .getExpiresAt());
		// i++;
		// }
		//
		// if (tokenFound)
		// return authToken;
		// else
		// return null;
		// } else {
		// throw new APIErrorException("API_ERROR", null,
		// result.getOriginalRestMessage(),
		// ((PrismaResponseWrapper) result.getResult()).getError());
		// }
	}

	public boolean deleteAuthTokenForUser(String tokenID, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/auth/tokens/" + tokenID;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		RestMessage result = restClient.doRequest(RestMethod.DELETE, URL,
				headers, null, null, null, null);

		if (Status.Family.familyOf(result.getHttpStatusCode()) == Status.Family.SUCCESSFUL) {
			return true;
		} else {
			return false;
		}
	}

	// Start: WORKGROUPS ****************************************** //

	public WorkgroupRepresentation workgroupManagerCreate(
			WorkgroupCreationRequest request, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<WorkgroupRepresentation>(
						WorkgroupRepresentation.class), null);
		if (result.getStatus() == Status.CREATED) {
			try {
				return ((PrismaResponseWrapper<WorkgroupRepresentation>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public WorkgroupRepresentation workgroupManagerGetWorkgroupById(
			final Long workgroupId, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + "/accounting/workgroups/" + workgroupId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<WorkgroupRepresentation>(
						WorkgroupRepresentation.class), null);
		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<WorkgroupRepresentation>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public List<WorkgroupRepresentation> workgroupManagerGetAllWorkgroups(
			PrismaMetaData meta, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + "/accounting/workgroups";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupRepresentation>>(
								ArrayList.class, WorkgroupRepresentation.class),
						null);

		return handleResult(result, meta);

	}

	public WorkgroupMembershipRepresentation workgroupManagerGetMembershipByWorkgroupIdAndUserAccountId(
			final Long workgroupId, final Long userAccountId, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/" + workgroupId
				+ "/memberships/" + userAccountId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<WorkgroupMembershipRepresentation>(
								WorkgroupMembershipRepresentation.class), null);
		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<WorkgroupMembershipRepresentation>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public List<WorkgroupMembershipRepresentation> workgroupManagerGetAllMembershipsByWorkgroupId(
			final Long workgroupId, RestWSParamsContainer params,
			PrismaMetaData meta, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + "/accounting/workgroups/" + workgroupId
				+ "/memberships";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						URL,
						headers,
						addRestWSParamsToQueryParams(params),
						null,
						null,
						null,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupMembershipRepresentation>>(
								ArrayList.class,
								WorkgroupMembershipRepresentation.class), null);

		return handleResult(result, meta);

	}

	public List<WorkgroupMembershipRepresentation> workgroupManagerGetAllMembershipsApplicationsByWorkgroupId(
			final Long workgroupId, PrismaMetaData meta, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/accounting/workgroups/" + workgroupId
				+ "/memberships/applications";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupMembershipRepresentation>>(
								ArrayList.class,
								WorkgroupMembershipRepresentation.class), null);

		return handleResult(result, meta);
	}

	public List<WorkgroupMembershipRepresentation> workgroupManagerGetAllApprovedMembershipsByWorkgroupId(
			final Long workgroupId, PrismaMetaData meta, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/" + workgroupId
				+ "/memberships/approved";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupMembershipRepresentation>>(
								ArrayList.class,
								WorkgroupMembershipRepresentation.class), null);

		return handleResult(result, meta);

	}

	public List<WorkgroupMembershipRepresentation> workgroupManagerGetAllMemberships(
			PrismaMetaData meta, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + "/accounting/workgroups/memberships";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupMembershipRepresentation>>(
								ArrayList.class,
								WorkgroupMembershipRepresentation.class), null);

		return handleResult(result, meta);
	}

	public List<WorkgroupMembershipRepresentation> workgroupManagerGetAllMembershipsByUserAccountId(
			final Long userAccountId, PrismaMetaData meta, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/memberships/"
				+ userAccountId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupMembershipRepresentation>>(
								ArrayList.class,
								WorkgroupMembershipRepresentation.class), null);

		return handleResult(result, meta);

	}

	public List<WorkgroupMembershipRepresentation> workgroupManagerGetAllMembershipApplicationsByUserAccountId(
			final Long userAccountId, PrismaMetaData meta, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/memberships/"
				+ userAccountId + "/applications";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupMembershipRepresentation>>(
								ArrayList.class,
								WorkgroupMembershipRepresentation.class), null);

		return handleResult(result, meta);
	}

	public List<WorkgroupMembershipRepresentation> workgroupManagerGetAllApprovedMembershipsByUserAccountId(
			final Long userAccountId, PrismaMetaData meta, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/memberships/"
				+ userAccountId + "/approved";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupMembershipRepresentation>>(
								ArrayList.class,
								WorkgroupMembershipRepresentation.class), null);

		return handleResult(result, meta);

	}

	public List<WorkgroupMembershipRepresentation> workgroupManagerGetAllMembershipsApplications(
			PrismaMetaData meta, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl
				+ "/accounting/workgroups/memberships/applications";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupMembershipRepresentation>>(
								ArrayList.class,
								WorkgroupMembershipRepresentation.class), null);

		return handleResult(result, meta);

	}

	public List<WorkgroupMembershipRepresentation> workgroupManagerGetAllApprovedMemberships(
			PrismaMetaData meta, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + "/accounting/workgroups/memberships/approved";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupMembershipRepresentation>>(
								ArrayList.class,
								WorkgroupMembershipRepresentation.class), null);

		return handleResult(result, meta);

	}

	public void workgroupManagerApplicateForMembership(Long workgroupId,
			Long userAccountId, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + "/accounting/workgroups/" + workgroupId
				+ "/memberships/" + userAccountId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.putRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);
		if (!(result.getStatus() == Status.OK)) {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public void workgroupManagerAddWorkgroupAdminMembership(
			WorkgroupMembershipRequest request, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/memberships/wg-admin";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<WorkgroupRepresentation>(
						WorkgroupRepresentation.class), null);
		if (!(result.getStatus() == Status.OK)) {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public void workgroupManagerAddWorkgroupUserMembership(
			WorkgroupMembershipRequest request, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/memberships/wg-user";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<WorkgroupRepresentation>(
						WorkgroupRepresentation.class), null);
		if (!(result.getStatus() == Status.OK)) {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public void workgroupManagerAddWorkgroupGuestMembership(
			WorkgroupMembershipRequest request, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/memberships/wg-guest";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);
		if (!(result.getStatus() == Status.OK)) {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public void workgroupManagerApproveWorkgroup(
			WorkgroupApprovationRequest request, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/workgroups/approve";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);
		handleResult(result, null);

	}

	public List<WorkgroupRepresentation> workgroupManagerGetReferencedWorkgroupsByUserAccountId(
			final Long userAccountId, PrismaMetaData meta, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/accounting/workgroups/referents/"
				+ userAccountId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.getRequest(
						URL,
						headers,
						new PrismaRestResponseDecoder<ArrayList<WorkgroupRepresentation>>(
								ArrayList.class, WorkgroupRepresentation.class),
						null);
		return handleResult(result, meta);
	}

	public List<UserRepresentation> workgroupManagerGetWorkgroupReferentsByWorkgroupId(
			final Long workgroupId, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/accounting/workgroups/" + workgroupId
				+ "/referents";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<UsersRepresentation>(
						UsersRepresentation.class), null);
		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<UsersRepresentation>) result
						.getResult()).getResult().getUsers();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	// End: WORKGROUPS ******************************************** //

	// Start: ORGANIZATION ****************************************** //

	public OrganizationRepresentation getOrganizationById(
			String organizationId, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/organizations/" + organizationId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<OrganizationRepresentation>(
						OrganizationRepresentation.class), null);

		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<OrganizationRepresentation>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}

	}

	// Start: ACCOUNTING ****************************************** //

	public void signUpOnPrismaIdentityProvider(SignUpUserRequest request,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + "/accounting/users/signup";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);
		if (!(result.getStatus().getFamily() == Status.Family.SUCCESSFUL)) {
			Error error = ((PrismaResponseWrapper) result.getResult())
					.getError();
			if (error.getErrorCode() == OrchestratorErrorCode.ORC_ITEM_ALREADY_EXISTS
					.getCode()) {
				error.setErrorCode(AccountingErrorCode.ACC_USER_SIGNUP_CREDENTIAL_ALREADY_USED_ON_IDP
						.getCode());
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(), error);
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((PrismaResponseWrapper) result.getResult()).getError());
			}
		}

	}

	public void signUpFromThirdPartyIdentityProvider(SignUpUserRequest request,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/accounting/users/signup-third-party";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);
		if (!(result.getStatus().getFamily() == Status.Family.SUCCESSFUL)) {
			Error error = ((PrismaResponseWrapper) result.getResult())
					.getError();
			if (error.getErrorCode() == OrchestratorErrorCode.ORC_ITEM_ALREADY_EXISTS
					.getCode()) {
				error.setErrorCode(AccountingErrorCode.ACC_USER_SIGNUP_CREDENTIAL_ALREADY_USED_ON_IDP
						.getCode());
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(), error);
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((PrismaResponseWrapper) result.getResult()).getError());
			}
		}

		// if (!(result.getStatus() == Status.OK)) {
		// throw new APIErrorException("API_ERROR", null,
		// result.getOriginalRestMessage(),
		// ((PrismaResponseWrapper) result.getResult()).getError());
		// }
	}

	public UserRepresentation getUserById(Long userAccountId, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/users/" + userAccountId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<UserRepresentation>(
						UserRepresentation.class), null);
		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<UserRepresentation>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public ArrayList<UserRepresentation> getAllUsers(String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/users";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<ArrayList<UserRepresentation>>(
						ArrayList.class, UserRepresentation.class), null);
		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<ArrayList<UserRepresentation>>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public UserRepresentation getUserByCredentialsOnIdentityProvider(
			Long identityProviderId, String nameId, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/accounting/organizations/idps/"
				+ identityProviderId + "/users/" + nameId;

		LOG.debug("getUserByCredentialsOnIdentityProvider: ", URL);

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<UserRepresentation>(
						UserRepresentation.class), null);

		if (!(result.getStatus().getFamily() == Status.Family.SUCCESSFUL)) {
			Error error = ((PrismaResponseWrapper) result.getResult())
					.getError();
			if (error.getErrorCode() == OrchestratorErrorCode.ORC_USER_ACCOUNT_NOT_FOUND
					.getCode()) {
				error.setErrorCode(AccountingErrorCode.ACC_USER_NOT_FOUND
						.getCode());
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(), error);
			} else {
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((PrismaResponseWrapper) result.getResult()).getError());
			}
		}

		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<UserRepresentation>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public IdentityProviderRepresentation getIdpInfoWithIdPId(Long idPId,
			String auth_data) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException

	{
		String URL = baseWSUrl + "/idp/info/idpID/" + idPId;
		LOG.debug("getIdpInfoWithIdPId: ", URL);

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<IdentityProviderRepresentation>(
						IdentityProviderRepresentation.class), null);
		LOG.debug(result.getResult().toString());
		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<IdentityProviderRepresentation>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}

	}

	public IdentityProviderRepresentation getIdpInfoWithEntityID(
			String entityID, String auth_data) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException

	{
		String URL = baseWSUrl + "/idp/info/entityID/" + entityID;
		LOG.debug("getIdpInfoWithEntityID: ", URL);

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(String.class), null);
		LOG.debug(result.getResult().toString());

		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<IdentityProviderRepresentation>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}

	}

	public void enableUserAccount(Long userAccountId, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/accounting/users/" + userAccountId
				+ "/enable";

		LOG.debug("enableUserAccount: ", URL);

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.putRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);
		LOG.debug(result.getResult().toString());

		if (!(result.getStatus() == Status.OK)) {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	// End: ACCOUNTING ****************************************** //

	public Boolean checkNameAvailable(Long workgroupId, String name,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId
				+ "/paas/name/available?name=" + name;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, headers, null, null, null, null,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);

		return handleResult(result, null);
	}

	public Boolean checkDNSAvailable(Long workgroupId, String dn,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/misc/dns/available?dn=" + dn;

		MultivaluedMap<String, Object> queryParams = new MultivaluedHashMap<String, Object>();
		queryParams.add("dn", dn);

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, headers, queryParams, null, null, null,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);

		return handleResult(result, null);
	}

	public Boolean chechDNDAvailable(String dnsName, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/misc/dns/" + dnsName + "/available";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);

		return handleResult(result, null);
	}

	/**
	 * Consumes getLRTInfo API from Biz Layer.
	 * 
	 * @param lrtId
	 *            the id of the LRT to retrieve data about.
	 * @return the response of the API, an {@link LRTRepresentation}.
	 * @throws MappingException
	 *             if there
	 * @throws RestClientException
	 *             if there was an exception during the request.
	 * @throws APIErrorException
	 *             if the API reported an error.
	 * @throws ServerErrorResponseException
	 *             if the error responded with an error.
	 */

	public LRTRepresentation getLRTInfo(long lrtId, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {
		String URL = baseWSUrl + "/orc/info/lrtinfo/" + lrtId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<LRTRepresentation>(
						LRTRepresentation.class), null);
		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<LRTRepresentation>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	/**
	 * Consumes getPlatformConfiguration API from Biz Layer.
	 * 
	 * @param request
	 *            the request containing the list of key to retrieve.
	 * @return the response of the API, a list {@link PlatformConfiguration} if
	 *         no error occurred.
	 * @throws MappingException
	 *             if there
	 * @throws RestClientException
	 *             if there was an exception during the request.
	 * @throws APIErrorException
	 *             if the API reported an error.
	 * @throws ServerErrorResponseException
	 *             if the error responded with an error.
	 */

	public PlatformConfigurations getPlatformConfiguration(String[] request,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/config/platformconfig";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<PlatformConfigurations>(
						PlatformConfigurations.class), null);

		return handleResult(result);
	}

	public PlatformConfigurations getAllPlatformConfiguration(String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/config/allplatformconfig";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<PlatformConfigurations>(
						PlatformConfigurations.class), null);

		return handleResult(result);
	}

	/**
	 * Consumes getAllAPPaaS API from Biz Layer.
	 * 
	 * @param workgroupId
	 * @return the response of the API, a list {@link APPaaSRepresentation} if
	 *         no error occurred.
	 * @throws MappingException
	 *             if there is a problem mapping the response.
	 * @throws RestClientException
	 *             if there was an exception during the request.
	 * @throws APIErrorException
	 *             if the API reported an error.
	 * @throws ServerErrorResponseException
	 *             if the error responded with an error.
	 */
	public List<APPaaSRepresentation> getAllAPPaaS(Long workgroupId,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/1/paas/appaas/";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);
		headers.putSingle("X-Workgroup-Id", workgroupId);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<List<APPaaSRepresentation>>(
						ArrayList.class, APPaaSRepresentation.class), null);

		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<List<APPaaSRepresentation>>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public List<APPaaSRepresentation> getAllAPPaaS(Long workgroupId,
			RestWSParamsContainer params, PrismaMetaData meta, String auth_data)
			throws RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/paas/appaas/";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, headers, addRestWSParamsToQueryParams(params), null, null,
				null,
				new PrismaRestResponseDecoder<List<APPaaSRepresentation>>(
						List.class, APPaaSRepresentation.class), null);

		return handleResult(result, meta);
	}

	public APPaaSRepresentation getAPPaaS(Long workgroupId, Long appaasId,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/paas/appaas/"
				+ appaasId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<APPaaSRepresentation>(
						APPaaSRepresentation.class), null);

		return handleResult(result);
	}

	public APPaaSRepresentation updateAPPaaS(Long workgroupId, Long appaasId,
			APPaaSRepresentation app, String auth_data)
			throws RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/paas/appaas/"
				+ appaasId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(app);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.POST,
				URL, headers, null, ge, MediaType.APPLICATION_JSON_TYPE, null,
				new PrismaRestResponseDecoder<APPaaSRepresentation>(
						APPaaSRepresentation.class), null);

		return handleResult(result, null);
	}

	public List<APPaaSEnvironmentRepresentation> getAllAPPaaSEnv(
			Long workgroupId, Long appaasId, RestWSParamsContainer params,
			PrismaMetaData meta, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/paas/appaas/"
				+ appaasId + "/environments";

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						URL,
						headers,
						addRestWSParamsToQueryParams(params),
						null,
						null,
						null,
						new PrismaRestResponseDecoder<List<APPaaSEnvironmentRepresentation>>(
								ArrayList.class,
								APPaaSEnvironmentRepresentation.class), null);

		return handleResult(result, meta);

	}

	public APPaaSEnvironmentRepresentation getAPPaaSEnv(Long workgroupId,
			Long appId, Long envId, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/paas/appaas/"
				+ appId + "/environments/" + envId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<APPaaSEnvironmentRepresentation>(
						APPaaSEnvironmentRepresentation.class), null);

		return handleResult(result);
	}

	public List<VirtualMachineRepresentation> getVirtualMachines(
			final Long workgroupId, final Long appId, final Long envId,
			String auth_data) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/paas/appaas/"
				+ appId + "/environments/" + envId + "/vms";

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						URL,
						headers,
						null,
						null,
						null,
						null,
						new PrismaRestResponseDecoder<List<VirtualMachineRepresentation>>(
								List.class, VirtualMachineRepresentation.class),
						null);

		return handleResult(result, null);
	}

	public List<PaaSServiceEventRepresentation> getAPPaaSEnvEvents(
			Long workgroupId, Long appId, Long envId, String auth_data,
			RestWSParamsContainer params, PrismaMetaData meta)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		return getPaaSServiceEvents(workgroupId.toString(), "paas", "appaas/"
				+ appId + "/environments", envId, params, auth_data, meta);
	}

	/**
	 * 
	 * @param request
	 *            , mandatory params:
	 * 
	 *            <pre>
	 * {
	 *  "userId":1,
	 *  "workgroupId":1,
	 *  "tag":"tag2",
	 *  "appName":"appName",
	 *  "appDescription":"appDescription",
	 *  "public":false,
	 *  "fileName":"kunagi.war"
	 * }
	 * </pre>
	 * @param file
	 * @param auth_data
	 * @return
	 */
	public AddAppSrcFileResponse uploadAppSRCFile(AddAppSrcFileRequest request,
			File file, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/apprepo/appsrcfiles/workgroups/"
				+ request.getWorkgroupId();

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<MultipartFormDataOutput> ge = new RestClientHelper.FormDataEntityBuilder()
				.addFormData("file", file,
						RestClientHelper.FormDataEntityBuilder.MEDIA_TYPE,
						file.getName())
				.addFormData("requestJSON", JsonUtility.serializeJson(request),
						MediaType.APPLICATION_JSON_TYPE).build();

		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.FormDataEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<AddAppSrcFileResponse>(
						AddAppSrcFileResponse.class), null);

		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<AddAppSrcFileResponse>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	/**
	 * 
	 * @param request
	 *            , mandatory params:
	 * 
	 *            <pre>
	 * {
	 *  "userId":1,
	 *  "workgroupId":1,
	 *  "tag":"tag2",
	 *  "appName":"appName",
	 *  "appDescription":"appDescription",
	 *  "public":false,
	 *  "fileName":"kunagi.war"
	 *  "url":URL
	 * }
	 * </pre>
	 * @param auth_data
	 * @return
	 */
	public AddAppSrcFileResponse uploadAppSRCFileFromURL(
			AddAppSrcFileRequest request, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/apprepo/appsrcfiles/workgroups/"
				+ request.getWorkgroupId() + "/url";

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.PUT,
				URL, headers, null, ge, MediaType.APPLICATION_JSON_TYPE, null,
				new PrismaRestResponseDecoder<AddAppSrcFileResponse>(
						AddAppSrcFileResponse.class), null);

		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<AddAppSrcFileResponse>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else if (result.getStatus() == Status.CONFLICT) {
			throw new APIErrorException("CONFLICT", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());

		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	public List<ApplicationRepositoryEntryRepresentation> getPublicAppSrcFiles(
			RestWSParamsContainer params, PrismaMetaData meta, String auth_data)
			throws Exception {

		String URL = baseWSUrl + "/apprepo/appsrcfiles/public";

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						URL,
						headers,
						addRestWSParamsToQueryParams(params),
						null,
						null,
						null,
						new PrismaRestResponseDecoder<List<ApplicationRepositoryEntryRepresentation>>(
								ArrayList.class,
								ApplicationRepositoryEntryRepresentation.class),
						null);

		return handleResult(result, meta);
	}

	public List<ApplicationRepositoryEntryRepresentation> getPrivateAppSrcFiles(
			Long workgroupId, RestWSParamsContainer params,
			PrismaMetaData meta, String auth_data) throws Exception {

		String URL = baseWSUrl + "/apprepo/appsrcfiles/private/workgroups/"
				+ workgroupId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						URL,
						headers,
						addRestWSParamsToQueryParams(params),
						null,
						null,
						null,
						new PrismaRestResponseDecoder<List<ApplicationRepositoryEntryRepresentation>>(
								ArrayList.class,
								ApplicationRepositoryEntryRepresentation.class),
						null);

		return handleResult(result, meta);
	}

	/**
	 * Creates (and deploys to the IaaS if needed) the given PaaSService with
	 * related data.
	 * 
	 * @param deployRequest
	 * @param representationClass
	 *            the class of the {@link GenericPaaSServiceRepresentation} for
	 *            the response mapping
	 * @return
	 */
	public <SERVICE_REPRESENTATION_TYPE extends GenericPaaSServiceRepresentation> SERVICE_REPRESENTATION_TYPE deployPaaSService(
			GenericPaaSServiceDeployRequest<SERVICE_REPRESENTATION_TYPE> request,
			Class<SERVICE_REPRESENTATION_TYPE> representationClass,
			String auth_data) throws Exception {

		String servicePath = "";
		if (request instanceof APPaaSDeployRequest) {
			servicePath = "/paas/appaas/";
		} else if (request instanceof APPaaSEnvironmentDeployRequest) {
			servicePath = "/paas/appaas/"
					+ ((APPaaSEnvironmentDeployRequest) request)
							.getEnvironmentDetails().getAppId()
					+ "/environments/";
		} else if (request instanceof BIaaSDeployRequest) {
			servicePath = "/paas/biaas/";
		} else if (request instanceof DBaaSDeployRequest) {
			servicePath = "/paas/dbaas/";
		} else if (request instanceof MQaaSDeployRequest) {
			servicePath = "/paas/mqaas/";
		} else if (request instanceof VMDeployRequest) {
			servicePath = "/iaas/vmaas/";
		} else {
			throw new Exception("Class type <" + request.getClass()
					+ "> not found");
		}

		String URL = baseWSUrl + "/workgroups/"
				+ request.getAccount().getWorkgroupId() + servicePath;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<SERVICE_REPRESENTATION_TYPE>(
						representationClass), null);

		return handleResult(result);
	}

	public <SERVICE_REPRESENTATION_TYPE extends GenericPaaSServiceRepresentation> List<SERVICE_REPRESENTATION_TYPE> getAllServiceRepresentation(
			Long workgroupId, Long appId, RestWSParamsContainer params,
			PrismaMetaData meta,
			Class<SERVICE_REPRESENTATION_TYPE> representationClass,
			String auth_data) throws Exception {

		String servicePath = getServicePathByClass(representationClass, appId);

		String URL = baseWSUrl + "/workgroups/" + workgroupId + servicePath;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						URL,
						headers,
						addRestWSParamsToQueryParams(params),
						null,
						null,
						null,
						new PrismaRestResponseDecoder<List<SERVICE_REPRESENTATION_TYPE>>(
								List.class, representationClass), null);

		return handleResult(result, meta);
	}

	public <SERVICE_REPRESENTATION_TYPE extends GenericPaaSServiceRepresentation> SERVICE_REPRESENTATION_TYPE getServiceRepresentation(
			Long workgroupId, Long appId, Long serviceId,
			Class<SERVICE_REPRESENTATION_TYPE> representationClass,
			String auth_data) throws Exception {

		String servicePath = getServicePathByClass(representationClass, appId);
		String URL = baseWSUrl + "/workgroups/" + workgroupId + servicePath
				+ serviceId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, headers, null, null, null, null,
				new PrismaRestResponseDecoder<SERVICE_REPRESENTATION_TYPE>(
						representationClass), null);

		return handleResult(result);
	}

	/**
	 * Creates a new APPaaS.
	 * 
	 * @param request
	 *            mandatory params:
	 * 
	 *            <pre>
	 * "userId":"1",
	 * "workgroupId":1,
	 * "applicationParams": {
	 *  "appName":"MyApp3",
	 *  "appDescription":"My first app"
	 * }
	 * </pre>
	 * @param auth_data
	 * @return the newly created {@link APPaaSRepresentation}.
	 */
	@Deprecated
	public APPaaSRepresentation deployAPPaaS(APPaaSDeployRequest request,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/"
				+ request.getAccount().getWorkgroupId() + "/paas/appaas/";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		TypeFactory tf = TypeFactory.defaultInstance();
		JavaType targetClass = tf.constructMapLikeType(HashMap.class,
				String.class, Object.class);

		BaseRestResponseResult result = restClient
				.putRequest(URL, headers, ge,
						RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
						new PrismaRestResponseDecoder<Map<String, Object>>(
								targetClass), null);

		if (result.getStatus().getFamily() == Status.Family.SUCCESSFUL) {
			try {
				// TODO: Create DSL !!!!
				return (APPaaSRepresentation) JsonUtility
						.deserializeJson(
								JsonUtility
										.serializeJson(((PrismaResponseWrapper<Map<String, Object>>) result
												.getResult()).getResult().get(
												"appaaS")),
								APPaaSRepresentation.class);
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	/** DEPLOY ENV */

	/**
	 * Consumes getHypervisor Info API from Biz Layer.
	 * 
	 * @param workgroupId
	 * @return the response of the API, a list {@link APPaaSRepresentation} if
	 *         no error occurred.
	 * @throws MappingException
	 *             if there is a problem mapping the response.
	 * @throws RestClientException
	 *             if there was an exception during the request.
	 * @throws APIErrorException
	 *             if the API reported an error.
	 * @throws ServerErrorResponseException
	 *             if the error responded with an error.
	 */
	public HypervisorInfo getHypervisorAdminInfo(String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/orchestrator/monitoring/hypervisor-value";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, headers, null, null, null, null,
				new PrismaRestResponseDecoder<HypervisorInfo>(
						HypervisorInfo.class), null);

		return handleResult(result, null);
	}

	/**
	 * Returns the list of the events associated with the given PaaSService
	 * 
	 * @param workgroupId
	 * @param serviceType
	 *            the type of the PaaS Service.
	 * @param serviceId
	 *            the Id of the PaaS Service.
	 * @return
	 */
	public List<PaaSServiceEventRepresentation> getPaaSServiceEvents(
			String workgroupId, String service, String serviceType,
			Long serviceId, RestWSParamsContainer params, String auth_data,
			PrismaMetaData meta) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/" + service
				+ "/" + serviceType.toLowerCase() + "/" + serviceId + "/events";

		return getEvents(URL, addRestWSParamsToQueryParams(params), meta,
				auth_data);
	}

	// public List<PaaSServiceEventRepresentation> getPaaSSAppEnvEvents(
	// String workgroupId, String appId, String id, String auth_data,
	// PrismaMetaData meta)
	// throws MappingException, NoMappingModelFoundException,
	// ServerErrorResponseException, APIErrorException,
	// RestClientException, JsonParseException, JsonMappingException,
	// IOException {
	//
	// String URL = baseWSUrl + "/workgroups/" + workgroupId + "/paas/appaas/"
	// + appId + "/environments/" + id + "/events";
	//
	// MultivaluedMap<String, Object> queryParams=new MultivaluedHashMap<String,
	// Object>();
	//
	// return getEvents(URL,queryParams, meta);
	// }

	private List<PaaSServiceEventRepresentation> getEvents(String URL,
			MultivaluedMap<String, Object> queryParams, PrismaMetaData meta,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						URL,
						headers,
						queryParams,
						null,
						null,
						null,
						new PrismaRestResponseDecoder<List<PaaSServiceEventRepresentation>>(
								ArrayList.class,
								PaaSServiceEventRepresentation.class), null);

		return handleResult(result, meta);
		/*
		 * if (result.getStatus() == Status.OK) { try { if(meta != null){
		 * meta.setMeta(((PrismaResponseWrapper<?>)
		 * result.getResult()).getMeta());
		 * meta.setBaseRestResponseResult(result);
		 * meta.setPrismaResponseWrapper(((PrismaResponseWrapper<?>)
		 * result.getResult())); } return
		 * ((PrismaResponseWrapper<List<PaaSServiceEventRepresentation>>)
		 * result.getResult()).getResult(); } catch (Exception e) { throw new
		 * APIErrorException("Unexpected response type.", e); } } else { throw
		 * new APIErrorException("API_ERROR", null,
		 * result.getOriginalRestMessage(), ((PrismaResponseWrapper)
		 * result.getResult()).getError()); }
		 */
	}

	/**
	 * 
	 * Start or stop a VM
	 * 
	 * @param workgroup
	 *            the id of the workgroup
	 * @param serviceType
	 *            cloud be paas or iaas
	 * @param service
	 *            the service: dbaas, biaas....
	 * @param id
	 *            id of the service
	 * @param action
	 *            cloud be start or stop
	 * 
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws APIErrorException
	 * @throws RestClientException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public boolean startAndStopVM(String workgroup, String serviceType,
			String service, String id, String action, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroup + "/" + serviceType
				+ "/" + service + "/" + id + "/" + action;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				null, null, new PrismaRestResponseDecoder<VMRepresentation>(
						VMRepresentation.class), null);

		if (result.getStatus().getFamily() != Status.Family.SUCCESSFUL) {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
		return true;
	}

public void undeployPaaSService(
			GenericPaaSServiceRepresentation paaSServiceRepresentation,
			String auth_data) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException, IllegalArgumentException,
			APIErrorException {

		if (paaSServiceRepresentation.getId() == null
				|| paaSServiceRepresentation.getWorkgroupId() == null) {
			throw new IllegalArgumentException(
					"PaaS_ID and Workgroup_ID must not be null");
		}
		StringBuilder URL = new StringBuilder();
		URL.append(baseWSUrl).append("/workgroups/")
				.append(paaSServiceRepresentation.getWorkgroupId());

		getServicePathByClass(paaSServiceRepresentation.getClass(), null);

		if (paaSServiceRepresentation instanceof APPaaSEnvironmentRepresentation) {
			Long APPaaSid = ((APPaaSEnvironmentRepresentation) paaSServiceRepresentation)
					.getAppaasId();
			if (APPaaSid == null) {
				throw new IllegalArgumentException("APPaaS_ID must not be null");
			}
			URL.append("/paas/appaas/").append(APPaaSid)
					.append("/environments/");
		} else if (paaSServiceRepresentation instanceof MQaaSRepresentation) {
			URL.append("/paas/mqaas/");
		} else if (paaSServiceRepresentation instanceof DBaaSRepresentation) {
			URL.append("/paas/dbaas/");
		} else if (paaSServiceRepresentation instanceof BIaaSRepresentation) {
			URL.append("/paas/biaas/");
		} else if (paaSServiceRepresentation instanceof VMRepresentation) {
			URL.append("/iaas/vmaas/");
		} else {
			throw new IllegalArgumentException(
					"PaaSService type not supported: "
							+ paaSServiceRepresentation.getClass()
									.getCanonicalName() + ".");
		}
		URL.append(paaSServiceRepresentation.getId());

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		RestMessage result = restClient.doRequest(RestMethod.DELETE,
				URL.toString(), headers, null, null, null, null);
		if (Status.fromStatusCode(result.getHttpStatusCode()).getFamily() == Status.ACCEPTED
				.getFamily()) {
			return;
		} else {
			BaseRestResponseResult errorResult = null;
			try {
				errorResult = new PrismaRestResponseDecoder<Void>(Void.class)
						.decode(result);
			} catch (Exception e) {
				throw new RestClientException((String) result.getBody());
			}
			throw new APIErrorException("API_ERROR", null,
					errorResult.getOriginalRestMessage(),
					((PrismaResponseWrapper) errorResult.getResult())
							.getError());
		}
	}

	/**************************************************
	 * CA *
	 **************************************************/

	/**
	 * NON RESTITUISCE NULLA?
	 * 
	 * @param username
	 * @param subjectDN
	 * @param auth_data
	 * @return
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws APIErrorException
	 * @throws RestClientException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */

	public String caCreateCertificate(Long username, String subjectDN,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/CAaas/createCertificate/" + username + "/"
				+ subjectDN;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	public String caRevokeCertificate(Long workgroupId, String serialNumber,
			int reason, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/CAaas/revokeCertificate/" + workgroupId
				+ "/" + serialNumber + "/" + reason;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.putRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	public String caViewCertificate(Long workgroupId, String serialNumber,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/CAaas/viewCertificate/" + workgroupId + "/"
				+ serialNumber;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	public List<CertificateInfoRepresentation> caAllCertificate(
			Long workgroupId, RestWSParamsContainer params,
			PrismaMetaData meta, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/CAaas/allCertificate/" + workgroupId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient
				.doRequest(
						RestMethod.GET,
						URL,
						headers,
						addRestWSParamsToQueryParams(params),
						null,
						null,
						null,
						new PrismaRestResponseDecoder<List<CertificateInfoRepresentation>>(
								List.class, CertificateInfoRepresentation.class),
						null);

		return handleResult(result, meta);
	}

	public boolean caIsWorkgroupActive(Long workgroupId, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/CAaas/isActive/" + workgroupId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);

		return handleResult(result);
	}

	public String caSaveCertificate(Long workgroupId, String serialNumber,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/CAaas/saveCertificate/" + workgroupId + "/"
				+ serialNumber;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	/**************************************************
	 * SMS *
	 **************************************************/

	public String smsIsUserActive(String username, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/SMSaas/isUserActive/" + username;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	public SMSNotificationsRepresentation smsGetNotifications(String username,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/SMSaas/getNotify/" + username;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<SMSNotificationsRepresentation>(
						SMSNotificationsRepresentation.class), null);

		return handleResult(result);
	}

	public Boolean smsDisableUser(String username, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/SMSaas/disableUser/" + username;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.putRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);

		return handleResult(result);
	}

	public String smsGetToken(String username, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/SMSaas/requestToken/" + username;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	public String smsActiveService(String username, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/SMSaas/activeService/" + username;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	public BigDecimal smsGetCredit(String username, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/SMSaas/getCredit/" + username;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<BigDecimal>(BigDecimal.class),
				null);

		return handleResult(result);
	}

	public History getSMSHistory(String workgroupId, boolean filter,
			String filterData, int limit, int offset, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/SMSaas/smsHistory/" + workgroupId + "/"
				+ filter + "/" + filterData + "?limit=" + limit + "&offset="
				+ offset;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);
		headers.putSingle("X-Workgroup-Id", workgroupId);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<History>(History.class), null);

		return handleResult(result);
	}

	public String smsChangeToken(String username, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/SMSaas/changeToken/" + username;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.putRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	public SMSNotificationsRepresentation smsSetDailyNotifications(
			SMSNotificationsRepresentation request, String workgroupId,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/SMSaas/setDailyNotifications/" + workgroupId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	public SMSNotificationsRepresentation smsSetMonthlyNotifications(
			SMSNotificationsRepresentation request, String workgroupId,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/SMSaas/setMonthlyNotifications/"
				+ workgroupId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);

		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	/**************************************************
	 * EMAIL *
	 **************************************************/

	public boolean emailIsEmailAccountExists(String email, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/EMAILaaS/isEmailAccountExists/" + email;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);

		return handleResult(result);
	}

	public boolean isListAccountVoid(Long workgroupId, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/EMAILaaS/isListAccountVoid/" + workgroupId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);

		return handleResult(result);
	}

	public List<RowEmail> emailGetEmailAccountList(Long userPrismaId,
			RestWSParamsContainer params, PrismaMetaData meta, String auth_data)
			throws Exception {

		String URL = baseWSUrl + "/EMAILaaS/getEmailAccountList/"
				+ userPrismaId;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, headers, addRestWSParamsToQueryParams(params), null, null,
				null, new PrismaRestResponseDecoder<List<RowEmail>>(
						ArrayList.class, RowEmail.class), null);

		return handleResult(result, meta);

	}

	public List<EmailDomains> emailGetEmailDomainList(String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/EMAILaaS/getEmailDomainList";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<List<EmailDomains>>(List.class,
						EmailDomains.class), null);

		return handleResult(result);
	}

	public boolean emailUpdatePassword(Credential credential, String auth_data)
			throws MappingException,

			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/EMAILaaS/updatePassword";

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(credential);

		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);

		return handleResult(result);
	}

	public boolean emailDeleteEmailAccount(String account, String password,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/EMAILaaS/deleteEmailAccount/" + account
				+ "/" + password;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.deleteRequest(URL, headers,
				null, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);
		return handleResult(result);
	}

	public boolean emailCreateEmailAccount(Long userPrismaId,
			EmailInfoRepresentation request, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/EMAILaaS/createEmailAccount/" + userPrismaId;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);
		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(request);
		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result);
	}

	/**********************************
	 * NETWORK *
	 * *******************************/

	public List<NetworkRepresentation> getAllNetworks(Long workgroupId,
			RestWSParamsContainer params, PrismaMetaData meta, String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId
				+ "/iaas/networks/";

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, headers, addRestWSParamsToQueryParams(params), null, null,
				null,
				new PrismaRestResponseDecoder<List<NetworkRepresentation>>(
						List.class, NetworkRepresentation.class), null);

		return handleResult(result, meta);
	}

	/**********************************
	 * KEYPAIRS *
	 * *******************************/

	/**
	 * Utilizzato nella creazione delle vm
	 */
	@Deprecated
	public List<KeypairRepresentation> getAllkeypairs(Long workgroupId,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/iaas/keys";

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, headers, null, null, null, null,
				new PrismaRestResponseDecoder<List<KeypairRepresentation>>(
						List.class, KeypairRepresentation.class), null);

		return handleResult(result, null);
	}

	public List<KeypairRepresentation> getAllkeypairs(Long workgroupId,
			RestWSParamsContainer params, PrismaMetaData meta, String auth_data)
			throws Exception {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/iaas/keys";
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, headers, addRestWSParamsToQueryParams(params), null, null,
				null,
				new PrismaRestResponseDecoder<List<KeypairRepresentation>>(
						List.class, KeypairRepresentation.class), null);

		return handleResult(result, meta);
	}

	public String generateKeypairs(String workgroupId, String keyName,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/iaas/keys/"
				+ keyName;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.POST,
				URL, headers, null, null, null, null,
				new PrismaRestResponseDecoder<String>(String.class), null);

		return handleResult(result, null);
	}

	public boolean deleteKeypair(String keyName, Long workgroupId,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/iaas/keys/"
				+ keyName;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.DELETE,
				URL, headers, null, null, null, null,
				new PrismaRestResponseDecoder<Boolean>(Boolean.class), null);

		return handleResult(result, null);
	}

	public KeypairRepresentation importKeypair(KeypairRequest keypair,
			Long workgroupId, String auth_data) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/iaas/keys/";

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
				.build(keypair);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.POST,
				URL, headers, null, ge,
				RestClientHelper.JsonEntityBuilder.MEDIA_TYPE, null,
				new PrismaRestResponseDecoder<KeypairRepresentation>(
						KeypairRepresentation.class), null);

		return handleResult(result, null);
	}

	public KeypairRepresentation createKeypair(String name, Long workgroupId,
			String auth_data) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException {

		String URL = baseWSUrl + "/workgroups/" + workgroupId + "/iaas/keys/"
				+ name;

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.doRequest(RestMethod.POST,
				URL, headers, null, null, null, null,
				new PrismaRestResponseDecoder<KeypairRepresentation>(
						KeypairRepresentation.class), null);

		return handleResult(result, null);
	}

	/******************************
	 * MISC
	 *******************************/

	public DebugInformations getDebugInformations()
			throws RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException {

		String URL = baseWSUrl + "/misc/debug";

		BaseRestResponseResult result = restClient.doRequest(RestMethod.GET,
				URL, null, null, null, null, null,
				new PrismaRestResponseDecoder<DebugInformations>(
						DebugInformations.class), null);

		return handleResult(result, null);
	}

	/******************************
	 * MONITORING
	 *******************************/

	public IaaSHealth getIaaSStatus(String auth_data)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, APIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/monitoring/iaas-status";

		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<IaaSHealth>(
						IaaSHealth.class), null);

		return handleResult(result);
	}

	public MonitoringWrappedResponseIaas getPrismaIaaSMonitoringStatus(
			String endpoint, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + endpoint;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<MonitoringWrappedResponseIaas>(
						MonitoringWrappedResponseIaas.class), null);

		return handleResult(result);
	}

	public HypervisorGroup getPrismaHypervisorMonitoringStatus(String endpoint,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + endpoint;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<HypervisorGroup>(
						HypervisorGroup.class), null);

		return handleResult(result);
	}

	public MonitoringWrappedResponsePaas getPrismaGraph(String auth_data,
			String infoReq) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		String URL = baseWSUrl + infoReq;
		PrismaAuthenticatedHeader headers = new PrismaAuthenticatedHeader(
				auth_data);

		// GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
		// .build(request);

		// BaseRestResponseResult result = restClient.postRequest(URL, headers,
		// ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
		// new PrismaRestResponseDecoder<MonitoringWrappedResponsePaas>(
		// MonitoringWrappedResponsePaas.class), null);

		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<MonitoringWrappedResponsePaas>(
						MonitoringWrappedResponsePaas.class), null);

		return handleResult(result);
	}

	private String getServicePathByClass(Class c, Long appId)
			throws IllegalArgumentException {

		if (c.equals(APPaaSRepresentation.class)) {
			return "/paas/appaas/";

		} else if (c.equals(APPaaSEnvironmentRepresentation.class)) {
			return "/paas/appaas/" + appId + "/environments/";

		} else if (c.equals(BIaaSRepresentation.class)) {
			return "/paas/biaas/";

		} else if (c.equals(DBaaSRepresentation.class)) {
			return "/paas/dbaas/";

		} else if (c.equals(MQaaSRepresentation.class)) {
			return "/paas/mqaas/";

		} else if (c.equals(VMRepresentation.class)) {
			return "/iaas/vmaas/";
		}

		throw new IllegalArgumentException("Class type <"
				+ c.getCanonicalName() + "> not found");
	}
}
