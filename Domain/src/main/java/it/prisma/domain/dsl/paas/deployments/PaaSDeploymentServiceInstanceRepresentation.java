package it.prisma.domain.dsl.paas.deployments;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "status", "service" })
public class PaaSDeploymentServiceInstanceRepresentation {

	@JsonProperty("id")
	private Long id;
	@JsonProperty("status")
	private String status;
	@JsonProperty("service")
	private PaaSDeploymentServiceRepresentation service;

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return The service
	 */
	@JsonProperty("service")
	public PaaSDeploymentServiceRepresentation getService() {
		return service;
	}

	/**
	 * 
	 * @param service
	 *            The service
	 */
	@JsonProperty("service")
	public void setService(PaaSDeploymentServiceRepresentation service) {
		this.service = service;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(status).append(service)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof PaaSDeploymentServiceInstanceRepresentation) == false) {
			return false;
		}
		PaaSDeploymentServiceInstanceRepresentation rhs = ((PaaSDeploymentServiceInstanceRepresentation) other);
		return new EqualsBuilder().append(id, rhs.id)
				.append(status, rhs.status).append(service, rhs.service)
				.isEquals();
	}

}