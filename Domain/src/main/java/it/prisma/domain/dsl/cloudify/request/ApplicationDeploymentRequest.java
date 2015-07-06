package it.prisma.domain.dsl.cloudify.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This POJO is a mapping for API (ApplicatioNDeployment) Request.
 * 
 * @author l.biava
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "applcationFileUploadKey",
		"applicationOverridesUploadKey", "cloudOverridesUploadKey",
		"applicationName", "authGroups", "debugMode", "debugEvents",
		"cloudConfigurationUploadKey", "debugAll", "selfHealing" })
public class ApplicationDeploymentRequest implements Serializable {

	private static final long serialVersionUID = -5921222694763476522L;

	@JsonProperty("applcationFileUploadKey")
	private String applcationFileUploadKey;
	@JsonProperty("applicationOverridesUploadKey")
	private String applicationOverridesUploadKey;
	@JsonProperty("cloudOverridesUploadKey")
	private String cloudOverridesUploadKey;
	@JsonProperty("applicationName")
	private String applicationName;
	@JsonProperty("authGroups")
	private String authGroups;
	@JsonProperty("debugMode")
	private String debugMode;
	@JsonProperty("debugEvents")
	private String debugEvents;
	@JsonProperty("cloudConfigurationUploadKey")
	private String cloudConfigurationUploadKey;
	@JsonProperty("debugAll")
	private Boolean debugAll;
	@JsonProperty("selfHealing")
	private Boolean selfHealing;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("applcationFileUploadKey")
	public String getApplcationFileUploadKey() {
		return applcationFileUploadKey;
	}

	@JsonProperty("applcationFileUploadKey")
	public void setApplcationFileUploadKey(String applcationFileUploadKey) {
		this.applcationFileUploadKey = applcationFileUploadKey;
	}

	@JsonProperty("applicationOverridesUploadKey")
	public String getApplicationOverridesUploadKey() {
		return applicationOverridesUploadKey;
	}

	@JsonProperty("applicationOverridesUploadKey")
	public void setApplicationOverridesUploadKey(
			String applicationOverridesUploadKey) {
		this.applicationOverridesUploadKey = applicationOverridesUploadKey;
	}

	@JsonProperty("cloudOverridesUploadKey")
	public String getCloudOverridesUploadKey() {
		return cloudOverridesUploadKey;
	}

	@JsonProperty("cloudOverridesUploadKey")
	public void setCloudOverridesUploadKey(String cloudOverridesUploadKey) {
		this.cloudOverridesUploadKey = cloudOverridesUploadKey;
	}

	@JsonProperty("applicationName")
	public String getApplicationName() {
		return applicationName;
	}

	@JsonProperty("applicationName")
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@JsonProperty("authGroups")
	public String getAuthGroups() {
		return authGroups;
	}

	@JsonProperty("authGroups")
	public void setAuthGroups(String authGroups) {
		this.authGroups = authGroups;
	}

	@JsonProperty("debugMode")
	public String getDebugMode() {
		return debugMode;
	}

	@JsonProperty("debugMode")
	public void setDebugMode(String debugMode) {
		this.debugMode = debugMode;
	}

	@JsonProperty("debugEvents")
	public String getDebugEvents() {
		return debugEvents;
	}

	@JsonProperty("debugEvents")
	public void setDebugEvents(String debugEvents) {
		this.debugEvents = debugEvents;
	}

	@JsonProperty("cloudConfigurationUploadKey")
	public String getCloudConfigurationUploadKey() {
		return cloudConfigurationUploadKey;
	}

	@JsonProperty("cloudConfigurationUploadKey")
	public void setCloudConfigurationUploadKey(
			String cloudConfigurationUploadKey) {
		this.cloudConfigurationUploadKey = cloudConfigurationUploadKey;
	}

	@JsonProperty("debugAll")
	public Boolean getDebugAll() {
		return debugAll;
	}

	@JsonProperty("debugAll")
	public void setDebugAll(Boolean debugAll) {
		this.debugAll = debugAll;
	}

	@JsonProperty("selfHealing")
	public Boolean getSelfHealing() {
		return selfHealing;
	}

	@JsonProperty("selfHealing")
	public void setSelfHealing(Boolean selfHealing) {
		this.selfHealing = selfHealing;
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