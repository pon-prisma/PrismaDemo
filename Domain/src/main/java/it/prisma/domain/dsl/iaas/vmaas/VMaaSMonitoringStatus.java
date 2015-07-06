package it.prisma.domain.dsl.iaas.vmaas;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "status" })
public class VMaaSMonitoringStatus {

	@JsonProperty("id")
	private String id;
	@JsonProperty("status")
	private VMaaSStatus status;

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public VMaaSMonitoringStatus withId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @return The status
	 */
	@JsonProperty("status")
	public VMaaSStatus getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            The status
	 */
	@JsonProperty("status")
	public void setStatus(VMaaSStatus status) {
		this.status = status;
	}

	public VMaaSMonitoringStatus withStatus(VMaaSStatus status) {
		this.status = status;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(status).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof VMaaSMonitoringStatus) == false) {
			return false;
		}
		VMaaSMonitoringStatus rhs = ((VMaaSMonitoringStatus) other);
		return new EqualsBuilder().append(id, rhs.id)
				.append(status, rhs.status)
				.isEquals();
	}
	
}