package it.prisma.domain.dsl.orchestrator.request;

import it.prisma.domain.dsl.paas.services.generic.request.SecureConnection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "envName", "envDescription", "envPublicEndpoint",
		"appFile", "serverType", "iaaSFlavor", "qosProfile", "loadBalancingInstances",
		"secureConnection", "securityGroup", "notificationEmail" })
public class EnvironmentParams implements Serializable {

	private static final long serialVersionUID = 5440230260224441341L;

	@JsonProperty("envName")
	private String envName;
	@JsonProperty("envDescription")
	private String envDescription;
	@JsonProperty("envPublicEndpoint")
	private String envPublicEndpoint;
	@JsonProperty("appFile")
	private AppFile appFile;
	@JsonProperty("appFileId")
	private Long appFileId;
	@JsonProperty("serverType")
	private String serverType;
	@JsonProperty("iaaSFlavor")
	private String iaaSFlavor;
	@JsonProperty("qosProfile")
	private String qosProfile;
	@JsonProperty("loadBalancingInstances")
	private Integer loadBalancingInstances;
	@JsonProperty("secureConnection")
	private SecureConnection secureConnection;
	@JsonProperty("securityGroup")
	private String securityGroup;
	@JsonProperty("notificationEmail")
	private String notificationEmail;
	@JsonProperty("availabilityZone")
	private String availabilityZone;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonProperty("qosProfile")
	public String getQosProfile() {
		return qosProfile;
	}

	@JsonProperty("qosProfile")
	public void setQosProfile(String qosProfile) {
		this.qosProfile = qosProfile;
	}

	@JsonProperty("appFileId")
	public Long getAppFileId() {
		return appFileId;
	}
	
	@JsonProperty("appFileId")
	public void setAppFileId(Long appFileId) {
		this.appFileId = appFileId;
	}

	@JsonProperty("envName")
	public String getEnvName() {
		return envName;
	}

	@JsonProperty("envName")
	public void setEnvName(String envName) {
		this.envName = envName;
	}

	@JsonProperty("availabilityZone")
	public String getAvailabilityZone() {
		return availabilityZone;
	}
	
	@JsonProperty("availabilityZone")
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	@JsonProperty("envDescription")
	public String getEnvDescription() {
		return envDescription;
	}

	@JsonProperty("envDescription")
	public void setEnvDescription(String envDescription) {
		this.envDescription = envDescription;
	}

	@JsonProperty("envPublicEndpoint")
	public String getEnvPublicEndpoint() {
		return envPublicEndpoint;
	}

	@JsonProperty("envPublicEndpoint")
	public void setEnvPublicEndpoint(String envPublicEndpoint) {
		this.envPublicEndpoint = envPublicEndpoint;
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

	@JsonProperty("iaaSFlavor")
	public String getIaaSFlavor() {
		return iaaSFlavor;
	}

	@JsonProperty("iaaSFlavor")
	public void setIaaSFlavor(String iaaSFlavor) {
		this.iaaSFlavor = iaaSFlavor;
	}

	@JsonProperty("loadBalancingInstances")
	public Integer getLoadBalancingInstances() {
		return loadBalancingInstances;
	}

	@JsonProperty("loadBalancingInstances")
	public void setLoadBalancingInstances(Integer loadBalancingInstances) {
		this.loadBalancingInstances = loadBalancingInstances;
	}

	@JsonProperty("secureConnection")
	public SecureConnection getSecureConnection() {
		return secureConnection;
	}

	@JsonProperty("secureConnection")
	public void setSecureConnection(SecureConnection secureConnection) {
		this.secureConnection = secureConnection;
	}

	@JsonProperty("securityGroup")
	public String getSecurityGroup() {
		return securityGroup;
	}

	@JsonProperty("securityGroup")
	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}

	@JsonProperty("notificationEmail")
	public String getNotificationEmail() {
		return notificationEmail;
	}

	@JsonProperty("notificationEmail")
	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}