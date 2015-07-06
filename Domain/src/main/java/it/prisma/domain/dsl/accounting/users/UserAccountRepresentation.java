package it.prisma.domain.dsl.accounting.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@JsonPropertyOrder({ "userAccountId", "identityProviderId",
		"nameIdOnIdentityProvider", "email", "suspended", "suspendedAt",
		"enabled", "enabledAt", "roles", "createdAt", "modifiedAt" })
public class UserAccountRepresentation implements java.io.Serializable {

	@JsonProperty("userAccountId")
	private Long userAccountId;
	@JsonProperty("identityProviderId")
	private Long identityProviderId;
	@JsonProperty("nameIdOnIdentityProvider")
	private String nameIdOnIdentityProvider;
	@JsonProperty("email")
	private String email;
	@JsonProperty("suspended")
	private Boolean suspended;
	@JsonProperty("suspendedAt")
	private String suspendedAt;
	@JsonProperty("enabled")
	private Boolean enabled;
	@JsonProperty("enabledAt")
	private String enabledAt;
	@JsonProperty("createdAt")
	private String createdAt;
	@JsonProperty("modifiedAt")
	private String modifiedAt;
	@JsonProperty("roles")
	private List<RoleRepresentation> roles = new ArrayList<RoleRepresentation>();

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("userAccountId")
	public Long getId() {
		return userAccountId;
	}

	@JsonProperty("userAccountId")
	public void setId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	@JsonProperty("identityProviderId")
	public Long getIdentityProviderId() {
		return identityProviderId;
	}

	@JsonProperty("identityProviderId")
	public void setIdentityProviderId(Long identityProviderId) {
		this.identityProviderId = identityProviderId;
	}

	@JsonProperty("nameIdOnIdentityProvider")
	public String getNameIdOnIdentityProvider() {
		return nameIdOnIdentityProvider;
	}

	@JsonProperty("nameIdOnIdentityProvider")
	public void setNameIdOnIdentityProvider(String nameIdOnIdentityProvider) {
		this.nameIdOnIdentityProvider = nameIdOnIdentityProvider;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("suspended")
	public Boolean getSuspended() {
		return suspended;
	}

	@JsonProperty("suspended")
	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	@JsonProperty("suspendedAt")
	public String getSuspendedAt() {
		return suspendedAt;
	}

	@JsonProperty("suspendedAt")
	public void setSuspendedAt(String suspendedAt) {
		this.suspendedAt = suspendedAt;
	}

	@JsonProperty("enabled")
	public Boolean getEnabled() {
		return enabled;
	}

	@JsonProperty("enabled")
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@JsonProperty("enabledAt")
	public String getEnabledAt() {
		return enabledAt;
	}

	@JsonProperty("enabledAt")
	public void setEnabledAt(String enabledAt) {
		this.enabledAt = enabledAt;
	}

	@JsonProperty("createdAt")
	public String getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("createdAt")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("modifiedAt")
	public String getModifiedAt() {
		return modifiedAt;
	}

	@JsonProperty("modifiedAt")
	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@JsonProperty("roles")
	public List<RoleRepresentation> getRoles() {
		return roles;
	}

	@JsonProperty("roles")
	public void setRoles(List<RoleRepresentation> roles) {
		this.roles = roles;
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