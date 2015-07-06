package it.prisma.businesslayer.orchestrator.dsl;

public class CloudifyEndpoint extends DeployerEndpoint {

	public static final String DEPLOYER_TYPE = "Cloudify";
	public static final String NAME = "Deployer-Cloudify";

	private String version;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return DEPLOYER_TYPE;
	}

	public String getName() {
		return NAME;
	}

}
