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
@JsonPropertyOrder({ "fingerprint", "name", "public_key" })
public class Keypair_ {

	@JsonProperty("fingerprint")
	private String fingerprint;
	@JsonProperty("name")
	private String name;
	@JsonProperty("public_key")
	private String publicKey;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The fingerprint
	 */
	@JsonProperty("fingerprint")
	public String getFingerprint() {
		return fingerprint;
	}

	/**
	 * 
	 * @param fingerprint
	 *            The fingerprint
	 */
	@JsonProperty("fingerprint")
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	/**
	 * 
	 * @return The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The publicKey
	 */
	@JsonProperty("public_key")
	public String getPublicKey() {
		return publicKey;
	}

	/**
	 * 
	 * @param publicKey
	 *            The public_key
	 */
	@JsonProperty("public_key")
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
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
		return new HashCodeBuilder().append(fingerprint).append(name)
				.append(publicKey).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Keypair_) == false) {
			return false;
		}
		Keypair_ rhs = ((Keypair_) other);
		return new EqualsBuilder().append(fingerprint, rhs.fingerprint)
				.append(name, rhs.name).append(publicKey, rhs.publicKey)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}