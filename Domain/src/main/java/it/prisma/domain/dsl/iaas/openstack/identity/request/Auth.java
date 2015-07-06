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
@JsonPropertyOrder({ "tenantName", "passwordCredentials" })
public class Auth {

	@JsonProperty("tenantName")
	private String tenantName;
	@JsonProperty("passwordCredentials")
	private PasswordCredentials passwordCredentials;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Auth() {
		super();
	}

	public Auth(String tenantName, PasswordCredentials passwordCredentials) {
		super();
		this.tenantName = tenantName;
		this.passwordCredentials = passwordCredentials;
	}

	public Auth(String tenantName, String username, String password) {
		super();
		this.tenantName = tenantName;
		this.passwordCredentials = new PasswordCredentials(username, password);
	}

	@JsonProperty("tenantName")
	public String getTenantName() {
		return tenantName;
	}

	@JsonProperty("tenantName")
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	@JsonProperty("passwordCredentials")
	public PasswordCredentials getPasswordCredentials() {
		return passwordCredentials;
	}

	@JsonProperty("passwordCredentials")
	public void setPasswordCredentials(PasswordCredentials passwordCredentials) {
		this.passwordCredentials = passwordCredentials;
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