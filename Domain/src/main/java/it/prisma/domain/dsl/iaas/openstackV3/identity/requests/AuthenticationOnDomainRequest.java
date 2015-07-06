package it.prisma.domain.dsl.iaas.openstackV3.identity.requests;

import it.prisma.domain.dsl.iaas.openstackV3.identity.Auth;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "auth" })
public class AuthenticationOnDomainRequest {

	@JsonProperty("auth")
	private Auth auth;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The auth
	 */
	@JsonProperty("auth")
	public Auth getAuth() {
		return auth;
	}

	/**
	 * 
	 * @param auth
	 *            The auth
	 */
	@JsonProperty("auth")
	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(auth).append(additionalProperties)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof AuthenticationOnDomainRequest) == false) {
			return false;
		}
		AuthenticationOnDomainRequest rhs = ((AuthenticationOnDomainRequest) other);
		return new EqualsBuilder().append(auth, rhs.auth)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}
