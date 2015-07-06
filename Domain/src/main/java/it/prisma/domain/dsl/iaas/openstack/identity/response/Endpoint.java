package it.prisma.domain.dsl.iaas.openstack.identity.response;

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
@JsonPropertyOrder({ "adminURL", "region", "internalURL", "id", "publicURL" })
public class Endpoint {

	@JsonProperty("adminURL")
	private String adminURL;
	@JsonProperty("region")
	private String region;
	@JsonProperty("internalURL")
	private String internalURL;
	@JsonProperty("id")
	private String id;
	@JsonProperty("publicURL")
	private String publicURL;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("adminURL")
	public String getAdminURL() {
		return adminURL;
	}

	@JsonProperty("adminURL")
	public void setAdminURL(String adminURL) {
		this.adminURL = adminURL;
	}

	@JsonProperty("region")
	public String getRegion() {
		return region;
	}

	@JsonProperty("region")
	public void setRegion(String region) {
		this.region = region;
	}

	@JsonProperty("internalURL")
	public String getInternalURL() {
		return internalURL;
	}

	@JsonProperty("internalURL")
	public void setInternalURL(String internalURL) {
		this.internalURL = internalURL;
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("publicURL")
	public String getPublicURL() {
		return publicURL;
	}

	@JsonProperty("publicURL")
	public void setPublicURL(String publicURL) {
		this.publicURL = publicURL;
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