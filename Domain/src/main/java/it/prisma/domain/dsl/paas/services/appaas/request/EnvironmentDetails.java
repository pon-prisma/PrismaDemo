package it.prisma.domain.dsl.paas.services.appaas.request;

import it.prisma.domain.dsl.orchestrator.request.AppFile;

import java.io.Serializable;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "serverType", "appFileId" })
public class EnvironmentDetails implements Serializable {

	private static final long serialVersionUID = 4377419958022624682L;

	private AppFile appFile;
	@JsonProperty("appId")
	private String appId;
	@JsonProperty("appFileId")
	private Long appFileId;
	@JsonProperty("serverType")
	private String serverType;

	@JsonProperty("appId")
	public String getAppId() {
		return appId;
	}

	@JsonProperty("appId")
	public void setAppId(String appId) {
		this.appId = appId;
	}

	@JsonProperty("appFile")
	public AppFile getAppFile() {
		return appFile;
	}

	@JsonProperty("appFile")
	public void setAppFile(AppFile appFile) {
		this.appFile = appFile;
	}

	@JsonProperty("serverType")
	public String getServerType() {
		return serverType;
	}

	@JsonProperty("serverType")
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public Long getAppFileId() {
		return appFileId;
	}

	public void setAppFileId(Long appFileId) {
		this.appFileId = appFileId;
	}

}