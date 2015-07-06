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
@JsonPropertyOrder({ "issued_at", "expires", "id", "tenant" })
public class Token {

	@JsonProperty("issued_at")
	private String issuedAt;
	@JsonProperty("expires")
	private String expires;
	@JsonProperty("id")
	private String id;
	@JsonProperty("tenant")
	private Tenant tenant;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("issued_at")
	public String getIssuedAt() {
		return issuedAt;
	}

	@JsonProperty("issued_at")
	public void setIssuedAt(String issuedAt) {
		this.issuedAt = issuedAt;
	}

	@JsonProperty("expires")
	public String getExpires() {
		return expires;
	}

	@JsonProperty("expires")
	public void setExpires(String expires) {
		this.expires = expires;
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("tenant")
	public Tenant getTenant() {
		return tenant;
	}

	@JsonProperty("tenant")
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
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