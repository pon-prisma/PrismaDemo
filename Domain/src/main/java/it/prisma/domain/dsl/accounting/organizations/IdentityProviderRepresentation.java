package it.prisma.domain.dsl.accounting.organizations;

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
@JsonPropertyOrder({ "identityProviderId", "entityId", "organizationId",
		"createdAt", "modifiedAt" })
public class IdentityProviderRepresentation implements java.io.Serializable {

	@JsonProperty("identityProviderId")
	private Long identityProviderId;
	@JsonProperty("entityId")
	private String entityId;
	@JsonProperty("organizationId")
	private Long organizationId;
	@JsonProperty("createdAt")
	private String createdAt;
	@JsonProperty("modifiedAt")
	private String modifiedAt;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("identityProviderId")
	public Long getIdentityProviderId() {
		return identityProviderId;
	}

	@JsonProperty("identityProviderId")
	public void setIdentityProviderId(Long identityProviderId) {
		this.identityProviderId = identityProviderId;
	}

	@JsonProperty("entityId")
	public String getEntityId() {
		return entityId;
	}

	@JsonProperty("entityId")
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@JsonProperty("organizationId")
	public Long getOrganizationId() {
		return organizationId;
	}

	@JsonProperty("organizationId")
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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