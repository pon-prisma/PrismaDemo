package it.prisma.domain.dsl.paas.services.appaas.response;

import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "appaaSEnvironmentId", "appaasId", "userId",
		"workgroupId", "name", "description", "domainName", "securityGroup",
		"iaaSFlavor", "loadBalancingInstances", "secureConnectionEnabled",
		"certificatePath", "qos", "publicVisibility", "availabilityZone",
		"notificationEmail", "serverType", "sourceFile", "createdAt", "status",
		"operation", "errorDescription", "events" })
public class APPaaSEnvironmentRepresentation extends PaaSServiceRepresentation {

	private static final long serialVersionUID = 4377419958022624682L;

	@JsonProperty("appaasId")
	private Long appaasId;

	@JsonProperty("serverType")
	private String serverType;
	// @JsonProperty("sourceFile")
	// TODO Create DSL
	@JsonProperty("applicationRepositoryEntry")
	private Object applicationRepositoryEntry;

	public Long getAppaasId() {
		return appaasId;
	}

	public void setAppaasId(Long appaasId) {
		this.appaasId = appaasId;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public Object getApplicationRepositoryEntry() {
		return applicationRepositoryEntry;
	}

	public void setApplicationRepositoryEntry(Object applicationRepositoryEntry) {
		this.applicationRepositoryEntry = applicationRepositoryEntry;
	}

}
