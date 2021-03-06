package it.prisma.domain.dsl.iaas.openstack.identity.request;

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
@JsonPropertyOrder({ "auth" })
public class OpenstackGetTokenRequest {

	@JsonProperty("auth")
	private Auth auth;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public OpenstackGetTokenRequest() {
		super();
	}

	public OpenstackGetTokenRequest(String tenantName, String username,
			String password) {
		super();
		this.auth = new Auth(tenantName, username, password);
	}

	@JsonProperty("auth")
	public Auth getAuth() {
		return auth;
	}

	@JsonProperty("auth")
	public void setAuth(Auth auth) {
		this.auth = auth;
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