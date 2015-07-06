package it.prisma.domain.dsl.deployments;

import it.prisma.domain.dsl.paas.deployments.PaaSDeploymentServiceInstanceRepresentation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "name", "inetAddresses", "services" })
public class VirtualMachineRepresentation {

	@JsonProperty("id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("inetAddresses")
	private List<InetAddressRepresentation> inetAddresses = new ArrayList<InetAddressRepresentation>();
	@JsonProperty("services")
	private List<PaaSDeploymentServiceInstanceRepresentation> services = new ArrayList<PaaSDeploymentServiceInstanceRepresentation>();

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
	 * @return The inetAddresses
	 */
	@JsonProperty("inetAddresses")
	public List<InetAddressRepresentation> getInetAddresses() {
		return inetAddresses;
	}

	/**
	 * 
	 * @param inetAddresses
	 *            The inetAddresses
	 */
	@JsonProperty("inetAddresses")
	public void setInetAddresses(List<InetAddressRepresentation> inetAddresses) {
		this.inetAddresses = inetAddresses;
	}

	/**
	 * 
	 * @return The services
	 */
	@JsonProperty("services")
	public List<PaaSDeploymentServiceInstanceRepresentation> getServices() {
		return services;
	}

	/**
	 * 
	 * @param services
	 *            The services
	 */
	@JsonProperty("services")
	public void setServices(
			List<PaaSDeploymentServiceInstanceRepresentation> services) {
		this.services = services;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(name)
				.append(inetAddresses).append(services).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof VirtualMachineRepresentation) == false) {
			return false;
		}
		VirtualMachineRepresentation rhs = ((VirtualMachineRepresentation) other);
		return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name)
				.append(inetAddresses, rhs.inetAddresses)
				.append(services, rhs.services).isEquals();
	}

}