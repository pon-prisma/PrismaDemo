package it.prisma.domain.dsl.accounting.organizations;

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
@JsonPropertyOrder({ "organizationId", "name", "description", "logoUri",
		"websiteUri", "websiteLabel", "createdAt", "modifiedAt",
		"identityProvider", "referents" })
public class OrganizationRepresentation implements java.io.Serializable {

	@JsonProperty("organizationId")
	private Long organizationId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("logoUri")
	private String logoUri;
	@JsonProperty("websiteUri")
	private String websiteUri;
	@JsonProperty("websiteLabel")
	private String websiteLabel;
	@JsonProperty("createdAt")
	private String createdAt;
	@JsonProperty("modifiedAt")
	private String modifiedAt;
	@JsonProperty("identityProvider")
	private IdentityProviderRepresentation identityProvider;
	@JsonProperty("referents")
	private List<OrganizationReferentRepresentation> referents = new ArrayList<OrganizationReferentRepresentation>();

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("organizationId")
	public Long getOrganizationId() {
		return organizationId;
	}

	@JsonProperty("organizationId")
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("logoUri")
	public String getLogoUri() {
		return logoUri;
	}

	@JsonProperty("logoUri")
	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}

	@JsonProperty("websiteUri")
	public String getWebsiteUri() {
		return websiteUri;
	}

	@JsonProperty("websiteUri")
	public void setWebsiteUri(String websiteUri) {
		this.websiteUri = websiteUri;
	}

	@JsonProperty("websiteLabel")
	public String getWebsiteLabel() {
		return websiteLabel;
	}

	@JsonProperty("websiteLabel")
	public void setWebsiteLabel(String websiteLabel) {
		this.websiteLabel = websiteLabel;
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

	@JsonProperty("identityProvider")
	public IdentityProviderRepresentation getIdentityProvider() {
		return identityProvider;
	}

	@JsonProperty("identityProvider")
	public void setIdentityProvider(IdentityProviderRepresentation identityProvider) {
		this.identityProvider = identityProvider;
	}

	@JsonProperty("referents")
	public List<OrganizationReferentRepresentation> getReferents() {
		return referents;
	}

	@JsonProperty("referents")
	public void setReferents(List<OrganizationReferentRepresentation> referents) {
		this.referents = referents;
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