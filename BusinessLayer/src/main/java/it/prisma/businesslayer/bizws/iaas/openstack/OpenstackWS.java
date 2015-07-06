package it.prisma.businesslayer.bizws.iaas.openstack;

import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.ComputeOpenstackAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.IdentityOpenstackAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.MainOpenstackAPIClientImpl;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.NetworkOpenstackAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.ObjectStorageOpenstackAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.OpenstackAPIErrorException;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.OpenstackEndpointType;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.OrchestrationOpenstackAPIClient;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.domain.dsl.iaas.openstack.compute.request.Keypair;
import it.prisma.domain.dsl.iaas.openstack.compute.request.OpenstackCreateKeyPairRequest;
import it.prisma.domain.dsl.iaas.openstack.compute.response.OpenstackCreateKeyPairResponse;
import it.prisma.domain.dsl.iaas.openstack.compute.response.listKey.OpenstackStackListPairResponse;
import it.prisma.domain.dsl.iaas.openstack.identity.request.OpenstackGetTokenRequest;
import it.prisma.domain.dsl.iaas.openstack.identity.response.Endpoint;
import it.prisma.domain.dsl.iaas.openstack.identity.response.OpenstackGetTokenResponse;
import it.prisma.domain.dsl.iaas.openstack.identity.response.OpenstackTenantListResponse;
import it.prisma.domain.dsl.iaas.openstack.network.response.OpenstackStackNetworkDetailsResponse;
import it.prisma.domain.dsl.iaas.openstack.network.response.OpenstackStackNetworkListResponse;
import it.prisma.domain.dsl.iaas.openstack.orchestration.request.OpenstackCreateStackRequest;
import it.prisma.domain.dsl.iaas.openstack.orchestration.response.OpenstackCreateStackResponse;
import it.prisma.domain.dsl.iaas.openstack.orchestration.response.OpenstackStackStatusDetailsResponse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.ClientResponseException;
import org.openstack4j.model.heat.Stack;
import org.openstack4j.openstack.OSFactory;



@Path("/iaas/openstack")
public class OpenstackWS extends BaseWS {

	public static Logger prismaLog = LogManager.getLogger(OpenstackWS.class);

	private String identityURL;

	private String tenant;
	private String username;
	private String pwd;

	@Inject
	private EnvironmentConfig envCfg;

	@PostConstruct
	private void init() {
		identityURL = envCfg
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL);
		tenant = envCfg
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_NAME);
		username = envCfg
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_USERNAME);
		pwd = envCfg
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_PASSWORD);
	}

	/************************************************************
	 * * IDENTITY * *
	 ************************************************************/

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/generateToken")
	public Response generateToken(OpenstackGetTokenRequest request)
			throws Exception {

		try {
			IdentityOpenstackAPIClient identityClient = new IdentityOpenstackAPIClient(
					identityURL, null, "v2.0", request.getAuth()
							.getTenantName(), request.getAuth()
							.getPasswordCredentials().getUsername(), request
							.getAuth().getPasswordCredentials().getPassword(),
					null);

			OpenstackGetTokenResponse response = identityClient
					.generateToken(request);
			return handleResult(response);

		} catch (Exception e) {
			e.printStackTrace();
			return handleGenericException(e);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/tenantList")
	public Response getTenantList(OpenstackGetTokenRequest request)
			throws Exception {
		try {

			IdentityOpenstackAPIClient identityClient = new IdentityOpenstackAPIClient(
					identityURL, null, "v2.0", request.getAuth()
							.getTenantName(), request.getAuth()
							.getPasswordCredentials().getUsername(), request
							.getAuth().getPasswordCredentials().getPassword(),
					null);

			OpenstackTenantListResponse response = identityClient
					.listTenants(request);

			return handleResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			return handleGenericException(e);
		}
	}

	/************************************************************
	 * * OBJECTSTORAGE * *
	 ************************************************************/

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/insertObj")
	public Response putObj() throws Exception {
		try {
			String tokenID = "MIIMmQYJKoZIhvcNAQcCoIIMijCCDIYCAQExCTAHBgUrDgMCGjCCCu8GCSqGSIb3DQEHAaCCCuAEggrceyJhY2Nlc3MiOiB7InRva2VuIjogeyJpc3N1ZWRfYXQiOiAiMjAxNC0xMC0wMVQxMzowNjoyMC44MTUwODgiLCAiZXhwaXJlcyI6ICIyMDE0LTEwLTAyVDEzOjA2OjIwWiIsICJpZCI6ICJwbGFjZWhvbGRlciIsICJ0ZW5hbnQiOiB7ImRlc2NyaXB0aW9uIjogIkFkbWluIFRlbmFudCIsICJlbmFibGVkIjogdHJ1ZSwgImlkIjogIjljZmQxMjhjOWNiZDQxMzZhODJiYTM3YzUwYjkxODYzIiwgIm5hbWUiOiAiYWRtaW4ifX0sICJzZXJ2aWNlQ2F0YWxvZyI6IFt7ImVuZHBvaW50cyI6IFt7ImFkbWluVVJMIjogImh0dHA6Ly8xMC4zOS4xOS4yMjE6ODc3NC92Mi85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyIsICJyZWdpb24iOiAicmVnaW9uT25lIiwgImludGVybmFsVVJMIjogImh0dHA6Ly8xMC4zOS4xOS4yMjE6ODc3NC92Mi85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyIsICJpZCI6ICI2M2Q1M2ZhMzBkOWQ0OTBlOWZjOGRhMmFkMWI3OGEzZSIsICJwdWJsaWNVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMTo4Nzc0L3YyLzljZmQxMjhjOWNiZDQxMzZhODJiYTM3YzUwYjkxODYzIn1dLCAiZW5kcG9pbnRzX2xpbmtzIjogW10sICJ0eXBlIjogImNvbXB1dGUiLCAibmFtZSI6ICJub3ZhIn0sIHsiZW5kcG9pbnRzIjogW3siYWRtaW5VUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMjo5Njk2IiwgInJlZ2lvbiI6ICJyZWdpb25PbmUiLCAiaW50ZXJuYWxVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMjo5Njk2IiwgImlkIjogIjg3NWMxZTAxNmUyMTQ4MzE5OGJmZjFkOWNlMDQyZjczIiwgInB1YmxpY1VSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIyOjk2OTYifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAibmV0d29yayIsICJuYW1lIjogIm5ldXRyb24ifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjg3NzYvdjIvOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMiLCAicmVnaW9uIjogInJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjg3NzYvdjIvOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMiLCAiaWQiOiAiMTc4YWYwYmVmZTVlNDZiN2I5OTdlOTE4ZWNjMzg4MWEiLCAicHVibGljVVJMIjogImh0dHA6Ly8xMC4zOS4xOS4yMjE6ODc3Ni92Mi85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyJ9XSwgImVuZHBvaW50c19saW5rcyI6IFtdLCAidHlwZSI6ICJ2b2x1bWV2MiIsICJuYW1lIjogImNpbmRlcnYyIn0sIHsiZW5kcG9pbnRzIjogW3siYWRtaW5VUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMTo5MjkyIiwgInJlZ2lvbiI6ICJyZWdpb25PbmUiLCAiaW50ZXJuYWxVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMTo5MjkyIiwgImlkIjogIjM2ZGU4NjczYjNhZDRlYmNhNGQ1NDQ3YzBjMjRkMGY1IiwgInB1YmxpY1VSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjkyOTIifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAiaW1hZ2UiLCAibmFtZSI6ICJnbGFuY2UifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjg3NzYvdjEvOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMiLCAicmVnaW9uIjogInJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjg3NzYvdjEvOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMiLCAiaWQiOiAiMTU2MTE0MDQ2YzcyNDkwY2FjZDEwZTIzODU3NDE5YmUiLCAicHVibGljVVJMIjogImh0dHA6Ly8xMC4zOS4xOS4yMjE6ODc3Ni92MS85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyJ9XSwgImVuZHBvaW50c19saW5rcyI6IFtdLCAidHlwZSI6ICJ2b2x1bWUiLCAibmFtZSI6ICJjaW5kZXIifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIyOjgwODAiLCAicmVnaW9uIjogInJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIyOjgwODAvdjEvQVVUSF85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyIsICJpZCI6ICIyYzlhN2JhMDVlNmQ0NDc1YTE2YTk4YTE0YWEzMDdkNyIsICJwdWJsaWNVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMjo4MDgwL3YxL0FVVEhfOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAib2JqZWN0LXN0b3JlIiwgIm5hbWUiOiAic3dpZnQifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjM1MzU3L3YyLjAiLCAicmVnaW9uIjogInJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjUwMDAvdjIuMCIsICJpZCI6ICIxYWU4OTljNmQ5OGY0MzJjODRjZjU4OTNjM2ZkYzM2ZCIsICJwdWJsaWNVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMTo1MDAwL3YyLjAifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAiaWRlbnRpdHkiLCAibmFtZSI6ICJrZXlzdG9uZSJ9XSwgInVzZXIiOiB7InVzZXJuYW1lIjogImFkbWluIiwgInJvbGVzX2xpbmtzIjogW10sICJpZCI6ICI4OGVhZTFhZTc1ZGY0N2NlOTlkNjJiZjFmODgwNDA5NiIsICJyb2xlcyI6IFt7Im5hbWUiOiAiYWRtaW4ifV0sICJuYW1lIjogImFkbWluIn0sICJtZXRhZGF0YSI6IHsiaXNfYWRtaW4iOiAwLCAicm9sZXMiOiBbImE4Mzk0ZDQ1YzNhOTQzNjA4Mjk4YWFkNTc3NWVjNjgwIl19fX0xggGBMIIBfQIBATBcMFcxCzAJBgNVBAYTAlVTMQ4wDAYDVQQIDAVVbnNldDEOMAwGA1UEBwwFVW5zZXQxDjAMBgNVBAoMBVVuc2V0MRgwFgYDVQQDDA93d3cuZXhhbXBsZS5jb20CAQEwBwYFKw4DAhowDQYJKoZIhvcNAQEBBQAEggEAOIBa1iLuLRpmwNXfn4EefhgAPy68fvCDfx6mwdxiG0i8-gN6swegVfJwF+ZbJ-KhJgZgbrKSJ+bfKvxNzmMVEHPj+3u9tqNka8zKJWSb2+-m5BBnw6IPuG6mO4n-TJVANSaNbSS5Yxcgc62UjTSOo-OInk4UTQle6UsQ4WU2EvoH1bzqBX5f5xYCKdYpD+pUR1-B0nYLV8Rtc+twE03uxvBiOOQORFOAXMtua-rK4A+flYlW-pCxg1zMYTooIYSXtI2DgAoxy+xxRbEGW+siwvvAtiE2UqP76uv21MXNN29K5p0sn6DPbynLBugx0eyxeyjn+WDoDf7qqZ7JH9n-hA==";
			ObjectStorageOpenstackAPIClient objClient = new ObjectStorageOpenstackAPIClient(
					identityURL, null, "v1", tokenID);

			MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
			headers.putSingle("X-Auth-Token", tokenID);

			File file = new File("prova.txt");
			String response = objClient.createObject(
					"AUTH_9cfd128c9cbd4136a82ba37c50b91863", "repository",
					"prova", headers, file);

			return handleResult(response);

		} catch (Exception e) {
			e.printStackTrace();

			return handleGenericException(e);

		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteObj")
	public Response deleteObj() throws Exception {
		try {
			String tokenID = "MIIMmQYJKoZIhvcNAQcCoIIMijCCDIYCAQExCTAHBgUrDgMCGjCCCu8GCSqGSIb3DQEHAaCCCuAEggrceyJhY2Nlc3MiOiB7InRva2VuIjogeyJpc3N1ZWRfYXQiOiAiMjAxNC0xMC0wMVQxNDo1NTo1My40MDU4NzQiLCAiZXhwaXJlcyI6ICIyMDE0LTEwLTAyVDE0OjU1OjUzWiIsICJpZCI6ICJwbGFjZWhvbGRlciIsICJ0ZW5hbnQiOiB7ImRlc2NyaXB0aW9uIjogIkFkbWluIFRlbmFudCIsICJlbmFibGVkIjogdHJ1ZSwgImlkIjogIjljZmQxMjhjOWNiZDQxMzZhODJiYTM3YzUwYjkxODYzIiwgIm5hbWUiOiAiYWRtaW4ifX0sICJzZXJ2aWNlQ2F0YWxvZyI6IFt7ImVuZHBvaW50cyI6IFt7ImFkbWluVVJMIjogImh0dHA6Ly8xMC4zOS4xOS4yMjE6ODc3NC92Mi85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyIsICJyZWdpb24iOiAicmVnaW9uT25lIiwgImludGVybmFsVVJMIjogImh0dHA6Ly8xMC4zOS4xOS4yMjE6ODc3NC92Mi85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyIsICJpZCI6ICI2M2Q1M2ZhMzBkOWQ0OTBlOWZjOGRhMmFkMWI3OGEzZSIsICJwdWJsaWNVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMTo4Nzc0L3YyLzljZmQxMjhjOWNiZDQxMzZhODJiYTM3YzUwYjkxODYzIn1dLCAiZW5kcG9pbnRzX2xpbmtzIjogW10sICJ0eXBlIjogImNvbXB1dGUiLCAibmFtZSI6ICJub3ZhIn0sIHsiZW5kcG9pbnRzIjogW3siYWRtaW5VUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMjo5Njk2IiwgInJlZ2lvbiI6ICJyZWdpb25PbmUiLCAiaW50ZXJuYWxVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMjo5Njk2IiwgImlkIjogIjg3NWMxZTAxNmUyMTQ4MzE5OGJmZjFkOWNlMDQyZjczIiwgInB1YmxpY1VSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIyOjk2OTYifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAibmV0d29yayIsICJuYW1lIjogIm5ldXRyb24ifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjg3NzYvdjIvOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMiLCAicmVnaW9uIjogInJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjg3NzYvdjIvOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMiLCAiaWQiOiAiMTc4YWYwYmVmZTVlNDZiN2I5OTdlOTE4ZWNjMzg4MWEiLCAicHVibGljVVJMIjogImh0dHA6Ly8xMC4zOS4xOS4yMjE6ODc3Ni92Mi85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyJ9XSwgImVuZHBvaW50c19saW5rcyI6IFtdLCAidHlwZSI6ICJ2b2x1bWV2MiIsICJuYW1lIjogImNpbmRlcnYyIn0sIHsiZW5kcG9pbnRzIjogW3siYWRtaW5VUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMTo5MjkyIiwgInJlZ2lvbiI6ICJyZWdpb25PbmUiLCAiaW50ZXJuYWxVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMTo5MjkyIiwgImlkIjogIjM2ZGU4NjczYjNhZDRlYmNhNGQ1NDQ3YzBjMjRkMGY1IiwgInB1YmxpY1VSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjkyOTIifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAiaW1hZ2UiLCAibmFtZSI6ICJnbGFuY2UifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjg3NzYvdjEvOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMiLCAicmVnaW9uIjogInJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjg3NzYvdjEvOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMiLCAiaWQiOiAiMTU2MTE0MDQ2YzcyNDkwY2FjZDEwZTIzODU3NDE5YmUiLCAicHVibGljVVJMIjogImh0dHA6Ly8xMC4zOS4xOS4yMjE6ODc3Ni92MS85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyJ9XSwgImVuZHBvaW50c19saW5rcyI6IFtdLCAidHlwZSI6ICJ2b2x1bWUiLCAibmFtZSI6ICJjaW5kZXIifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIyOjgwODAiLCAicmVnaW9uIjogInJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIyOjgwODAvdjEvQVVUSF85Y2ZkMTI4YzljYmQ0MTM2YTgyYmEzN2M1MGI5MTg2MyIsICJpZCI6ICIyYzlhN2JhMDVlNmQ0NDc1YTE2YTk4YTE0YWEzMDdkNyIsICJwdWJsaWNVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMjo4MDgwL3YxL0FVVEhfOWNmZDEyOGM5Y2JkNDEzNmE4MmJhMzdjNTBiOTE4NjMifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAib2JqZWN0LXN0b3JlIiwgIm5hbWUiOiAic3dpZnQifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjM1MzU3L3YyLjAiLCAicmVnaW9uIjogInJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTAuMzkuMTkuMjIxOjUwMDAvdjIuMCIsICJpZCI6ICIxYWU4OTljNmQ5OGY0MzJjODRjZjU4OTNjM2ZkYzM2ZCIsICJwdWJsaWNVUkwiOiAiaHR0cDovLzEwLjM5LjE5LjIyMTo1MDAwL3YyLjAifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAiaWRlbnRpdHkiLCAibmFtZSI6ICJrZXlzdG9uZSJ9XSwgInVzZXIiOiB7InVzZXJuYW1lIjogImFkbWluIiwgInJvbGVzX2xpbmtzIjogW10sICJpZCI6ICI4OGVhZTFhZTc1ZGY0N2NlOTlkNjJiZjFmODgwNDA5NiIsICJyb2xlcyI6IFt7Im5hbWUiOiAiYWRtaW4ifV0sICJuYW1lIjogImFkbWluIn0sICJtZXRhZGF0YSI6IHsiaXNfYWRtaW4iOiAwLCAicm9sZXMiOiBbImE4Mzk0ZDQ1YzNhOTQzNjA4Mjk4YWFkNTc3NWVjNjgwIl19fX0xggGBMIIBfQIBATBcMFcxCzAJBgNVBAYTAlVTMQ4wDAYDVQQIDAVVbnNldDEOMAwGA1UEBwwFVW5zZXQxDjAMBgNVBAoMBVVuc2V0MRgwFgYDVQQDDA93d3cuZXhhbXBsZS5jb20CAQEwBwYFKw4DAhowDQYJKoZIhvcNAQEBBQAEggEAKKyCPc7CdXgn68iDXPb13ur5vS6Y2pkjHX48VsQ8LBF6By8ur0W7MhFKLpnakyFk+s54COymadKoargT-NWv0UkKoHYuuHm-+S6hIvyTdPK16kwVETyaG6iL1vXc121pAFrLBs+1S5kk19GyXQm02s9Hvx8pRzGTiFs9bOrcBX5ftDFUHULZuq9uAh5ZuYOkNecQWHwnbFhaEvISNkN6wzBlRRd1tsTBNarwwY4ilpwwQVLi9nJvYh6e52x6lD1ivRnTSGwfaQwa-iwns+drIQnGvpPGzpxApVqWBJr5WlxbsiVrk0-LtoLc0peht4B5yqsBaEFlSx6niTf9hqyc0g==";
			ObjectStorageOpenstackAPIClient objClient = new ObjectStorageOpenstackAPIClient(
					identityURL, null, "v1", tokenID);

			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("X-Auth-Token", tokenID);

			String response = objClient.deleteObject(
					"AUTH_9cfd128c9cbd4136a82ba37c50b91863", "repository",
					"prova", headers);

			return handleResult(response);

		} catch (Exception e) {
			e.printStackTrace();

			return handleGenericException(e);

		}
	}

	/***************************
	 * ORCHESTRATION (SWIFT) *
	 ***************************/

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createStack")
	public Response createStack() throws Exception {
		try {

			MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", this.tenant, this.username,
					this.pwd);

			List<Endpoint> endpoints = mainOS
					.getServiceEndpoints(OpenstackEndpointType.ORCHESTRATION);
			OrchestrationOpenstackAPIClient orchestratorClient = mainOS
					.getOrchestrationClient(endpoints.get(0));

			OpenstackCreateStackRequest r = new OpenstackCreateStackRequest();

			r.setStackName("testMySQLPHPMYADMIN");
			r.setTemplateUrl("http://90.147.102.243/mysqlphpmyadmin.template");
			r.setTimeoutMins("10");
			r.addProperty("key_name", "orchestrator");
			r.addProperty("vm_name", "CreatedWithHEAT");
			r.addProperty("image_id", "4f3e431e-10ac-4e86-99bf-868f81647229");
			r.addProperty("instance_type", "medium");
			r.addProperty("shared_net_id",
					"a57813c5-c531-4233-a65e-35d99739cf56");
			r.addProperty("net_id", "a57813c5-c531-4233-a65e-35d99739cf56");
			r.addProperty("db_name", "matteo");
			r.addProperty("db_username", "matteo");
			r.addProperty("db_password", "matteo");
			r.addProperty("db_root_password", "matteo");

			OpenstackCreateStackResponse response = orchestratorClient
					.createStack(r, null);
			return handleResult(response);
		} catch (Exception e) {
			e.printStackTrace();

			return handleGenericException(e);

		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/stackDetails")
	public Response showStackDetails() throws Exception {
		try {

			MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", this.tenant, this.username,
					this.pwd);

			List<Endpoint> endpoints = mainOS
					.getServiceEndpoints(OpenstackEndpointType.ORCHESTRATION);
			OrchestrationOpenstackAPIClient orchestratorClient = mainOS
					.getOrchestrationClient(endpoints.get(0));

			String url = "http://orchestration-havana.sielte.it:8004/v1/302a340fad2b41e08eaccf2632bd79b5/stacks/stackName/1ca31a63-5c80-4f0e-a09f-795d1fa12ec5";
			OpenstackStackStatusDetailsResponse response = orchestratorClient
					.showStackDetails(url);

			return handleResult(response);

		} catch (Exception e) {
			e.printStackTrace();
			return handleGenericException(e);
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteStack")
	public Response deleteStack() throws Exception {
		try {
			MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", this.tenant, this.username,
					this.pwd);

			List<Endpoint> endpoints = mainOS
					.getServiceEndpoints(OpenstackEndpointType.ORCHESTRATION);
			OrchestrationOpenstackAPIClient orchestratorClient = mainOS
					.getOrchestrationClient(endpoints.get(0));

			orchestratorClient.deleteStack("mysql5",
					"817c3b9b-5fc2-491d-8bc3-ddce83bf10c5");

			return handleResult("");

		} catch (Exception e) {
			e.printStackTrace();
			return handleGenericException(e);
		}
	}

	/***************************
	 * COMPUTE (NOVA) *
	 ***************************/

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createKey")
	public Response createKeyPair() throws Exception {
		try {

			MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", "orchestrator", "orchestrator ",
					"!2014ORCH.Prisma");

			List<Endpoint> endpoints = mainOS
					.getServiceEndpoints(OpenstackEndpointType.COMPUTE);
			ComputeOpenstackAPIClient computeClient = mainOS
					.getComputeClient(endpoints.get(0));
			OpenstackCreateKeyPairRequest r = new OpenstackCreateKeyPairRequest();

			Keypair k = new Keypair();
			k.setName("chiaveMatteo");
			k.setPublicKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQDx8nkQv/zgGgB4rMYmIf+6A4l6Rr+o/6lHBQdW5aYd44bd8JttDCE/F/pNRr0lRE+PiqSPO8nDPHw0010JeMH9gYgnnFlyY3/OcJ02RhIPyyxYpv9FhY+2YiUkpwFOcLImyrxEsYXpD/0d3ac30bNH6Sw9JD9UZHYcpSxsIbECHw==");
			r.setKeypair(k);

			OpenstackCreateKeyPairResponse response = computeClient
					.createKeyPair(r, null);

			return handleResult(response);

		} catch (OpenstackAPIErrorException apie) {
			// return Response
			// .status(Status.OK)
			// .entity(PrismaResponseWrapper.error(
			// OrchestratorErrorCode.ORC_WF_IAAS_GENERIC_ERROR,
			// apie.getAPIError().toString()).build()).build();
			return handleGenericException(apie);
		} catch (Exception e) {
			e.printStackTrace();
			return handleGenericException(e);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/listKey")
	public Response listKey() throws Exception {
		try {

			MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", this.tenant, this.username,
					this.pwd);

			List<Endpoint> endpoints = mainOS
					.getServiceEndpoints(OpenstackEndpointType.COMPUTE);

			ComputeOpenstackAPIClient computeClient = mainOS
					.getComputeClient(endpoints.get(0));

			OpenstackStackListPairResponse response = computeClient
					.listKeypair();

			return handleResult(response);

		} catch (Exception e) {
			e.printStackTrace();
			return handleGenericException(e);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/stopServer/{id}")
	public Response stopServer(@PathParam("id") String id) throws Exception {
		try {

			MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", "PrismaDemo", "mrossi",
					"!Mrossi99");

			List<Endpoint> endpoints = mainOS
					.getServiceEndpoints(OpenstackEndpointType.COMPUTE);
			ComputeOpenstackAPIClient computeClient = mainOS
					.getComputeClient(endpoints.get(0));

			computeClient.stopServer(id, null);

			return handleResult("");

		} catch (OpenstackAPIErrorException apie) {
			return handleGenericException(apie);
		} catch (Exception e) {
			e.printStackTrace();
			return handleGenericException(e);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/listNetwork")
	public Response listNetwork() throws Exception {
		try {

			MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", this.tenant, this.username,
					this.pwd);

			List<Endpoint> endpoints = mainOS
					.getServiceEndpoints(OpenstackEndpointType.NETWORK);

			NetworkOpenstackAPIClient networkClient = mainOS
					.getNetworkClient(endpoints.get(0));

			OpenstackStackNetworkListResponse response = networkClient
					.listNetwork();

			return handleResult(response);

		} catch (Exception e) {
			e.printStackTrace();
			return handleGenericException(e);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/showNetworkDetails/{network_id}")
	@PrismaWrapper
	public OpenstackStackNetworkDetailsResponse showNetworkDetails(
			@PathParam("network_id") String network_id) throws Exception {
		try {

			MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", this.tenant, this.username,
					this.pwd);

			List<Endpoint> endpoints = mainOS
					.getServiceEndpoints(OpenstackEndpointType.NETWORK);

			NetworkOpenstackAPIClient networkClient = mainOS
					.getNetworkClient(endpoints.get(0));

			OpenstackStackNetworkDetailsResponse response = networkClient
					.getNetworkDetails(network_id);

			return response;
		} catch (OpenstackAPIErrorException ose) {
			if (ose.getAPIError().getType().contains("NotFound"))
				throw new PrismaWrapperException("",
						new ResourceNotFoundException("Network not found."));
			throw new PrismaWrapperException(ose);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteNetwork/{network_id}")
	@PrismaWrapper
	public void deleteNetwork(@PathParam("network_id") String network_id)
			throws Exception {
		try {
			MainOpenstackAPIClientImpl mainOS = new MainOpenstackAPIClientImpl(
					identityURL, null, "v2.0", this.tenant, this.username,
					this.pwd);

			List<Endpoint> endpoints = mainOS
					.getServiceEndpoints(OpenstackEndpointType.NETWORK);

			NetworkOpenstackAPIClient networkClient = mainOS
					.getNetworkClient(endpoints.get(0));

			networkClient.deleteNetwork(network_id);

			getSessionServletResponse().setStatus(
					Status.NO_CONTENT.getStatusCode());
		} catch (OpenstackAPIErrorException ose) {
			if (ose.getAPIError().getType().contains("NotFound"))
				throw new PrismaWrapperException("",
						new ResourceNotFoundException("Network not found."));
			throw new PrismaWrapperException(ose);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/osclient")
	public String getOSClient() {
		
		try {
			
			OSClient os = OSFactory.builder().endpoint("https://prisma-cloud.ba.infn.it:5000/v2.0/")
				.credentials("mrossi", "!Mrossi99").tenantName("PrismaDemo").authenticate();
		
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("key_name", "keyINFNPrismaDemo");
			parameters.put("vm_name", "vm_test");
			parameters.put("image_id", "c4597117-91d9-4bff-9e3d-e434642a399e");
			parameters.put("instance_type", "medium.silver");
			parameters.put("shared_net_id", "a57813c5-c531-4233-a65e-35d99739cf56");
			parameters.put("net_id", "a57813c5-c531-4233-a65e-35d99739cf56");
			parameters.put("db_name", "mydb");
			parameters.put("db_root_password", "password");
			
			Stack stack = os.heat().stacks().create(Builders.stack().name("test").templateURL("http://90.147.102.53/templates/paas/dbaas/mysqlphpmyadmin.template").parameters(parameters).build());
			//os.heat().stacks().create("testOpenstack", "http://90.147.102.53/templates/paas/dbaas/mysqlphpmyadmin.template", parameters, true, 10L);
			stack.getLinks().get(0);
			System.out.println(stack.getId());
					
			Stack s = os.heat().stacks().getDetails("test", stack.getId());
			System.out.println(s.getStatus());
			
			return os.getToken().getId();
		}catch(ClientResponseException e){
			return e.getMessage();
		}
	}
}
