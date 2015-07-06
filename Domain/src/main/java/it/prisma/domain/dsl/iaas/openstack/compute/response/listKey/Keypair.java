package it.prisma.domain.dsl.iaas.openstack.compute.response.listKey;

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
@JsonPropertyOrder({ "keypair" })
public class Keypair {

	@JsonProperty("keypair")
	private Keypair_ keypair;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The keypair
	 */
	@JsonProperty("keypair")
	public Keypair_ getKeypair() {
		return keypair;
	}

	/**
	 * 
	 * @param keypair
	 *            The keypair
	 */
	@JsonProperty("keypair")
	public void setKeypair(Keypair_ keypair) {
		this.keypair = keypair;
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
		return new HashCodeBuilder().append(keypair)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Keypair) == false) {
			return false;
		}
		Keypair rhs = ((Keypair) other);
		return new EqualsBuilder().append(keypair, rhs.keypair)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}