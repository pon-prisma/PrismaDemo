//package it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer;
//
//import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.ComputeOpenstackAPIClient;
//import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.IdentityOpenstackAPIClient;
//import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.NetworkOpenstackAPIClient;
//import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.ObjectStorageOpenstackAPIClient;
//import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.OpenstackAuthenticationException;
//import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.OpenstackEndpointType;
//import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.OrchestrationOpenstackAPIClient;
//import it.prisma.domain.dsl.iaas.openstack.identity.request.OpenstackGetTokenRequest;
//import it.prisma.domain.dsl.iaas.openstack.identity.response.Endpoint;
//import it.prisma.domain.dsl.iaas.openstack.identity.response.OpenstackGetTokenResponse;
//import it.prisma.domain.dsl.iaas.openstack.identity.response.ServiceCatalog;
//import it.prisma.domain.dsl.iaas.openstack.identity.response.Token;
//import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * This class contains Prisma BizLib Rest API requests implementation.
// * 
// * @author l.biava
// * 
// */
//public class PrismaBizMasterAPIClientImpl implements PrismaBizMasterAPIClient{
//
//	protected String identityServiceVersion;
//	protected Token token;
//	protected String tenant, user, pwd;
//	protected Map<String, ServiceCatalog> serviceEndpoints;
//
//	/**
//	 * This constructor generates token given tenant user and pwd
//	 * 
//	 * @param identityURL
//	 *            The URL of Openstack identity endpoint for the authentication
//	 *            (es. ).
//	 * @param restClientFactory
//	 * @param identityServiceVersion
//	 *            The version of the identity service.
//	 * @param tenant
//	 * @param user
//	 * @param pwd
//	 */
//	public MainOpenstackAPIClientImpl(String identityURL,
//			RestClientFactory restClientFactory, String identityServiceVersion,
//			String tenant, String user, String pwd)
//			throws OpenstackAuthenticationException {
//
//		this.identityServiceVersion = identityServiceVersion;
//		this.tenant = tenant;
//		this.user = user;
//		this.pwd = pwd;
//
//		IdentityOpenstackAPIClient idOSClient = new IdentityOpenstackAPIClient(
//				identityURL, restClientFactory, identityServiceVersion);
//		try {
//			OpenstackGetTokenRequest request = new OpenstackGetTokenRequest(
//					tenant, user, pwd);
//			OpenstackGetTokenResponse response = idOSClient
//					.generateToken(request);
//
//			this.serviceEndpoints = new HashMap<String, ServiceCatalog>();
//
//			for (ServiceCatalog service : response.getAccess()
//					.getServiceCatalog()) {
//				serviceEndpoints.put(service.getType(), service);
//			}
//
//			token = response.getAccess().getToken();
//
//		} catch (Exception e) {
//			throw new OpenstackAuthenticationException("", e);
//		}
//	}
//
//	@Override
//	public ObjectStorageOpenstackAPIClient getObjectStorageClient(
//			Endpoint endpoint) {
//
//		// TODO Cache the client
//		return new ObjectStorageOpenstackAPIClient(endpoint.getPublicURL(),
//				null, "", token.getId());
//	}
//
//	public Token getToken() {
//		return token;
//	}
//
//	public String getTenant() {
//		
//		return tenant;
//	}
//	
//	public AppRepoAPIClient getAppRepoClient() {
//		return new AppRepoAPIClient();
//	}
//}