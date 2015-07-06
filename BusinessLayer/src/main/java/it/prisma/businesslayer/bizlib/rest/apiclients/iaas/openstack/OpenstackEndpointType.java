package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

public enum OpenstackEndpointType {
	COMPUTE("compute"),
	COMPUTE_v3("computev3"),
	NETWORK("network"),
	VOLUME("volume"),
	VOLUME_v2("volumev2"),
	IMAGE("image"),
	OBJECT_STORAGE("object-store"),
	ORCHESTRATION("orchestration"),
	IDENTITY("identity");

	private final String description;

	private OpenstackEndpointType(String description) {

		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name();
	}

	@Override
	public String toString() {
		return name() + ": " + description;
	}
}