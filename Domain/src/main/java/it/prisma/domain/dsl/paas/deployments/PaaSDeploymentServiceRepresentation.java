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
@JsonPropertyOrder({ "id", "name", "plannedInstances", "currentInstances" })
public class PaaSDeploymentServiceRepresentation {

	public enum PaaSDeploymentServiceRepresentationType {
		LB_APACHE, LB_HAPROXY, DB_MySQL, DB_POSTGRE, WS_APACHE_HTTPD, AS_JBOSS, AS_TOMCAT, AS_GLASSFISH, MQ_RABBITMQ, VM_LINUX, EX_PENTAHO, RD_X2GO;
	}

	@JsonProperty("id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("type")
	private String type;
	@JsonProperty("plannedInstances")
	private Integer plannedInstances;
	@JsonProperty("currentInstances")
	private Integer currentInstances;

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
	 * @return The type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            The type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return The plannedInstances
	 */
	@JsonProperty("plannedInstances")
	public Integer getPlannedInstances() {
		return plannedInstances;
	}

	/**
	 * 
	 * @param plannedInstances
	 *            The plannedInstances
	 */
	@JsonProperty("plannedInstances")
	public void setPlannedInstances(Integer plannedInstances) {
		this.plannedInstances = plannedInstances;
	}

	/**
	 * 
	 * @return The currentInstances
	 */
	@JsonProperty("currentInstances")
	public Integer getCurrentInstances() {
		return currentInstances;
	}

	/**
	 * 
	 * @param currentInstances
	 *            The currentInstances
	 */
	@JsonProperty("currentInstances")
	public void setCurrentInstances(Integer currentInstances) {
		this.currentInstances = currentInstances;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(name).append(type)
				.append(plannedInstances).append(currentInstances).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof PaaSDeploymentServiceRepresentation) == false) {
			return false;
		}
		PaaSDeploymentServiceRepresentation rhs = ((PaaSDeploymentServiceRepresentation) other);
		return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name)
				.append(type, rhs.type)
				.append(plannedInstances, rhs.plannedInstances)
				.append(currentInstances, rhs.currentInstances).isEquals();
	}

}