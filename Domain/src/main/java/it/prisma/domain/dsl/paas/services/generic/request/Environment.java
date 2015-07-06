package it.prisma.domain.dsl.paas.services.generic.request;

import java.io.Serializable;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "availabilityZone", "iaaSFlavor", "qos" })
public class Environment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("availabilityZone")
	private String availabilityZone;
	@JsonProperty("iaaSFlavor")
	private String iaaSFlavor;
	@JsonProperty("qos")
	private String qos;

	@JsonProperty("availabilityZone")
	public String getAvailabilityZone() {
		return availabilityZone;
	}

	@JsonProperty("availabilityZone")
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	@JsonProperty("iaaSFlavor")
	public String getIaaSFlavor() {
		return iaaSFlavor;
	}

	@JsonProperty("iaaSFlavor")
	public void setIaaSFlavor(String iaaSFlavor) {
		this.iaaSFlavor = iaaSFlavor;
	}

	@JsonProperty("qos")
	public String getQos() {
		return qos;
	}

	@JsonProperty("qos")
	public void setQos(String qos) {
		this.qos = qos;
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

}