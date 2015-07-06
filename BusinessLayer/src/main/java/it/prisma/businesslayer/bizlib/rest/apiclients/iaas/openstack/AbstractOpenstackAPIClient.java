package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

import it.prisma.domain.dsl.iaas.openstack.ErrorWrapper;
import it.prisma.domain.dsl.iaas.openstack.identity.request.OpenstackGetTokenRequest;
import it.prisma.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;

import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response.Status.Family;

/**
 * 
 * This class contains Openstack Rest API requests implementation.
 * 
 * @author
 * 
 */
public abstract class AbstractOpenstackAPIClient extends AbstractAPIClient {

	protected String openstackServiceVersion;
	protected String tokenID;
	protected String tenant, user, pwd;

	public AbstractOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory, String openstackServiceVersion) {
		super(baseURL, restClientFactory);
		this.openstackServiceVersion = openstackServiceVersion;
	}

	public AbstractOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory,
			String openstackServiceVersion, String tokenID) {
		super(baseURL, restClientFactory);
		this.openstackServiceVersion = openstackServiceVersion;
		this.tokenID = tokenID;
	}

	/**
	 * This constructor generates token given tenant user and pwd
	 * 
	 * @param baseURL
	 * @param restClientFactory
	 * @param openstackServiceVersion
	 * @param tenant
	 * @param user
	 * @param pwd
	 * @param idOSClient
	 *            if null a default one will be used (same baseURL and v2.0).
	 */
	public AbstractOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory,
			String openstackServiceVersion, String tenant, String user,
			String pwd, IdentityOpenstackAPIClient idOSClient)
			throws OpenstackAuthenticationException {
		super(baseURL, restClientFactory);
		this.openstackServiceVersion = openstackServiceVersion;
		this.tenant = tenant;
		this.user = user;
		this.pwd = pwd;

		if (idOSClient == null)
			idOSClient = new IdentityOpenstackAPIClient(baseURL,
					restClientFactory, "v2.0");
		try {
			OpenstackGetTokenRequest request = new OpenstackGetTokenRequest(
					tenant, user, pwd);
			tokenID = idOSClient.generateToken(request).getAccess().getToken()
					.getId();
		} catch (Exception e) {
			throw new OpenstackAuthenticationException("", e);
		}
	}

	protected MultivaluedHashMap<String, Object> getAuthenticationHeaders(
			String tokenID) {
		MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		headers.putSingle("X-Auth-Token", tokenID);
		return headers;
	}

	protected MultivaluedHashMap<String, Object> getHeaders(
			Map<String, String> headerMap) {
		MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

		if (headerMap != null)
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				headers.putSingle(entry.getKey(), entry.getValue());
			}
		return headers;
	}

	public String getServiceVersion() {
		return openstackServiceVersion;
	}

	public void setServiceVersion(String openstackServiceVersion) {
		this.openstackServiceVersion = openstackServiceVersion;
	}

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * handleErrorResult
	 * 
	 * @param result
	 * @throws OpenstackAPIErrorException
	 */
	protected <RespType> void handleErrorResult(BaseRestResponseResult result)
			throws OpenstackAPIErrorException {

		throw new OpenstackAPIErrorException("API_ERROR", null,
				result.getOriginalRestMessage(),
				((ErrorWrapper) result.getResult()).getError());
	}

	@SuppressWarnings("unchecked")
	protected <RespType> RespType handleSuccessResult(
			BaseRestResponseResult result) throws MappingException {
		try {

			return ((RespType) result.getResult());
		} catch (Exception e) {
			throw new MappingException("Unexpected response type.", null,
					result.getOriginalRestMessage());
		}
	}

	protected <RespType> RespType handleResult(BaseRestResponseResult result)
			throws MappingException, OpenstackAPIErrorException {
		if (result.getStatus().getFamily() == Family.SUCCESSFUL)
			return handleSuccessResult(result);
		else
			handleErrorResult(result);

		return null;
	}

}
