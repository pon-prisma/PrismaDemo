package it.prisma.domain.dsl.paas.services.caaas;

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
@JsonPropertyOrder({
	"name",
	"serialNumber",
	"revoked"
})
public class CertificateInfoRepresentation {

	@JsonProperty("name")
	private String name;
	@JsonProperty("serialNumber")
	private String serialNumber;
	@JsonProperty("revoked")
	private Boolean revoked;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 * The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 * The serialNumber
	 */
	@JsonProperty("serialNumber")
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * 
	 * @param serialNumber
	 * The serialNumber
	 */
	@JsonProperty("serialNumber")
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * 
	 * @return
	 * The revoked
	 */
	@JsonProperty("revoked")
	public Boolean getRevoked() {
		return revoked;
	}

	/**
	 * 
	 * @param revoked
	 * The revoked
	 */
	@JsonProperty("revoked")
	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
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
		return new HashCodeBuilder().append(name).append(serialNumber).append(revoked).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof CertificateInfoRepresentation) == false) {
			return false;
		}
		CertificateInfoRepresentation rhs = ((CertificateInfoRepresentation) other);
		return new EqualsBuilder().append(name, rhs.name).append(serialNumber, rhs.serialNumber).append(revoked, rhs.revoked).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}