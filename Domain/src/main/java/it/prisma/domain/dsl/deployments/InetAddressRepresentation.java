package it.prisma.domain.dsl.deployments;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "type", "ip" })
public class InetAddressRepresentation {

	public enum InetAddressType {
		PRIVATE, PUBLIC
	}

	@JsonProperty("type")
	private InetAddressType type;
	@JsonProperty("ip")
	private String ip;

	/**
	 * 
	 * @return The type
	 */
	@JsonProperty("type")
	public InetAddressType getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            The type
	 */
	@JsonProperty("type")
	public void setType(InetAddressType type) {
		this.type = type;
	}

	/**
	 * 
	 * @return The ip
	 */
	@JsonProperty("ip")
	public String getIp() {
		return ip;
	}

	/**
	 * 
	 * @param ip
	 *            The ip
	 */
	@JsonProperty("ip")
	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(type).append(ip).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof InetAddressRepresentation) == false) {
			return false;
		}
		InetAddressRepresentation rhs = ((InetAddressRepresentation) other);
		return new EqualsBuilder().append(type, rhs.type).append(ip, rhs.ip)
				.isEquals();
	}

}