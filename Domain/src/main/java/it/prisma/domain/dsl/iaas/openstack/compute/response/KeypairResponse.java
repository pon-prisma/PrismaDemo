package it.prisma.domain.dsl.iaas.openstack.compute.response;

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
@JsonPropertyOrder({ "fingerprint", "name", "public_key", "user_id" })
public class KeypairResponse {

	@JsonProperty("fingerprint")
	private String fingerprint;
	@JsonProperty("name")
	private String name;
	@JsonProperty("public_key")
	private String publicKey;
	@JsonProperty("user_id")
	private String userId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("fingerprint")
	public String getFingerprint() {
		return fingerprint;
	}

	@JsonProperty("fingerprint")
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("public_key")
	public String getPublicKey() {
		return publicKey;
	}

	@JsonProperty("public_key")
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}

	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
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