package it.prisma.businesslayer.bizlib.infrastructure;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyEndpointBuilder;
import it.prisma.businesslayer.orchestrator.dsl.Credentials.CredentialsType;
import it.prisma.businesslayer.orchestrator.dsl.CredentialsBuilder;
import it.prisma.businesslayer.orchestrator.dsl.OpenStackEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.OpenStackEndpointBuilder;
import it.prisma.dal.dao.iaas.tenant.IaaSTenantDAO;
import it.prisma.dal.dao.iaas.tenant.IaasEnvironmentDAO;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.iaas.tenant.IaaSTenant;

import javax.ejb.Stateless;
import javax.inject.Inject;

//TODO Improve description
/**
 * This bean manages the infrastructure data for zone, OpenStack environments,
 * tenant-workgroup linkings, etc.
 * 
 * @author l.biava
 *
 */
@Stateless
public class InfrastructureManagerBean {

	@Inject
	IaasEnvironmentDAO iaasEnvironmentDAO;

	@Inject
	IaaSTenantDAO iaasTenantDAO;

	@Inject
	private EnvironmentConfig envCfg;

	public class AppRepoEndpoint {
		private String OSIdentityURL;
		private String OSRegion;
		private String tenantName;
		private String username;
		private String password;

		public String getOSRegion() {
			return OSRegion;
		}

		public void setOSRegion(String oSRegion) {
			OSRegion = oSRegion;
		}

		public String getOSIdentityURL() {
			return OSIdentityURL;
		}

		public void setOSIdentityURL(String oSIdentityURL) {
			OSIdentityURL = oSIdentityURL;
		}

		public String getTenantName() {
			return tenantName;
		}

		public void setTenantName(String tenantName) {
			this.tenantName = tenantName;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public AppRepoEndpoint(String oSIdentityURL, String OSRegion,
				String tenantName, String username, String password) {
			super();
			this.OSIdentityURL = oSIdentityURL;
			this.OSRegion = OSRegion;
			this.tenantName = tenantName;
			this.username = username;
			this.password = password;
		}

	}

	public OpenStackEndpoint getOpenStackEndpointForWorkgroup(Long workgroupId,
			boolean launchExceptionIfNotFound) {
		// TODO: MA POI HEAT E' SOLO DI OPENSTACK !!
		IaaSTenant iaasTenant = getIaaSTenantForWorkgroup(workgroupId,
				launchExceptionIfNotFound);

		// Lookup by zone & user
		return OpenStackEndpointBuilder
				.openStackEndpoint()
				.withUrl(iaasTenant.getIaasEnvironment().getIdentityURL())
				.withIdentityVersion(
						""+iaasTenant.getIaasEnvironment().getIdentityVersion())
				.withTenantName(iaasTenant.getName())
				.withDomainName(iaasTenant.getIaasEnvironment().getDomain())
				.withCredentials(
						CredentialsBuilder.credentials()
								.withType(CredentialsType.USERNAME_PASSWORD)
								.withUsername(iaasTenant.getUsername())
								.withPassword(iaasTenant.getPassword()).build())
				.build();
	}

	public CloudifyEndpoint getCloudifyEndpointForWorkgroup(Long workgroupId,
			boolean launchExceptionIfNotFound) {

		IaaSTenant iaasTenant = getIaaSTenantForWorkgroup(workgroupId,
				launchExceptionIfNotFound);

		it.prisma.dal.entities.iaas.tenant.CloudifyInstance cfyEP = iaasTenant
				.getCloudifyInstance();
		if (launchExceptionIfNotFound && cfyEP == null)
			throw new RuntimeException(
					"No CloudifyEnpoint found for Workgroup " + workgroupId);

		return CloudifyEndpointBuilder
				.cloudifyEndpoint()
				.withUrl(cfyEP.getUrl())
				.withVersion(cfyEP.getVersion())
				.withCredentials(
						CredentialsBuilder.credentials()
								.withType(CredentialsType.USERNAME_PASSWORD)
								.withUsername(cfyEP.getUsername())
								.withPassword(cfyEP.getPassword()).build())
				.build();
	}

	public IaaSTenant getIaaSTenantForWorkgroup(Workgroup workgroup,
			boolean launchExceptionIfNotFound) {
		return getIaaSTenantForWorkgroup(workgroup.getId(),
				launchExceptionIfNotFound);
	}

	public IaaSTenant getIaaSTenantForWorkgroup(Long workgroupId,
			boolean launchExceptionIfNotFound) {
		IaaSTenant iaasTenant = iaasTenantDAO.findByWorkgroupId(workgroupId);
		if (launchExceptionIfNotFound && iaasTenant == null)
			throw new IllegalArgumentException(
					"IaaSTenant not found for Workgroup " + workgroupId);

		return iaasTenant;
	}

	public AppRepoEndpoint getPrivateAppRepoEndpoint(
			AppRepoLookupInfo lookupInfo) {

		IaaSTenant tenant = getIaaSTenantForWorkgroup(lookupInfo.getWorkgroupId(),
				false);

		if (tenant == null)
			throw new IllegalArgumentException(
					"PrivateAppRepo not found for Workgroup "
							+ lookupInfo.getWorkgroupId());

		String completeIdentityURL = tenant.getIaasEnvironment()
				.getIdentityURL();

		/*
		 * switch (tenant.getIaasEnvironment().getIdentityVersion()) { case 2:
		 * completeIdentityURL += "/v2.0"; break; case 3: completeIdentityURL +=
		 * "/v3"; break; }
		 */

		return new AppRepoEndpoint(completeIdentityURL, null, tenant.getName(),
				tenant.getUsername(), tenant.getPassword());
	}

	public AppRepoEndpoint getPublicAppRepoEndpoint(AppRepoLookupInfo lookupInfo) {
		//TODO: Use configuration on DB !
		return new AppRepoEndpoint(
				//getPrivateAppRepoEndpoint(lookupInfo).getOSIdentityURL(),
				 envCfg.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL)
				 + "/v2.0",
				null,
				envCfg.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_NAME),
				envCfg.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_USERNAME),
				envCfg.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_PASSWORD));
	}
}
