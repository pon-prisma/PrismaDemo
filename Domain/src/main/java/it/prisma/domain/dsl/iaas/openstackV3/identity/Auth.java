package it.prisma.domain.dsl.iaas.openstackV3.identity;

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
@JsonPropertyOrder({ "identity", "scope" })
public class Auth {

	@JsonProperty("identity")
	private Identity identity;
	@JsonProperty("scope")
	private Scope scope;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The identity
	 */
	@JsonProperty("identity")
	public Identity getIdentity() {
		return identity;
	}

	/**
	 * 
	 * @param identity
	 *            The identity
	 */
	@JsonProperty("identity")
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	/**
	 * 
	 * @return The scope
	 */
	@JsonProperty("scope")
	public Scope getScope() {
		return scope;
	}

	/**
	 * 
	 * @param scope
	 *            The scope
	 */
	@JsonProperty("scope")
	public void setScope(Scope scope) {
		this.scope = scope;
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
		return new HashCodeBuilder().append(identity).append(scope)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Auth) == false) {
			return false;
		}
		Auth rhs = ((Auth) other);
		return new EqualsBuilder().append(identity, rhs.identity)
				.append(scope, rhs.scope)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}