package it.prisma.businesslayer.orchestrator.dsl;

public class OpenStackEndpoint extends DeployerEndpoint {

	public static final String DEPLOYER_TYPE = "OpenStack";
	public static final String NAME = "Deployer-OpenStack-Heat";

	private String identityVersion;
	private String tenantName;
	private String domainName;

	public String getIdentityVersion() {
		return identityVersion;
	}

	public void setIdentityVersion(String identityVersion) {
		this.identityVersion = identityVersion;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getType() {
		return DEPLOYER_TYPE;
	}

	public String getName() {
		return NAME;
	}

}
