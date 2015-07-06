package it.prisma.domain.dsl.monitoring.businesslayer.iaas.infrastructure;

import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.InfrastructureBareMetalStatus;

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
@JsonPropertyOrder({
	"environment",
	"storage",
	"network",
	"compute",
	"available_nodes",
	"total_nodes",
	"machineGroups"
})
public class InfrastructurePicture {

	@JsonProperty("environment")
	private String environment;
	@JsonProperty("storage")
	private InfrastructureBareMetalStatus storage;
	@JsonProperty("network")
	private InfrastructureBareMetalStatus network;
	@JsonProperty("compute")
	private InfrastructureBareMetalStatus compute;
	@JsonProperty("available_nodes")
	private int available_nodes;
	@JsonProperty("total_nodes")
	private int total_nodes;
	@JsonProperty("machineGroups")
	private List<GroupsMachine> machineGroups = new ArrayList<GroupsMachine>();
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The testbed
	 */
	@JsonProperty("environment")
	public String getEnvironment() {
		return environment;
	}

	/**
	 * 
	 * @param environment
	 * The environment
	 */
	@JsonProperty("environment")
	public void setTestbed(String environment) {
		this.environment = environment;
	}
	
	/**
	 * 
	 * @return
	 * The storage
	 */
	@JsonProperty("storage")
	public InfrastructureBareMetalStatus getStorage() {
		return storage;
	}

	/**
	 * 
	 * @param storage
	 * The storage
	 */
	@JsonProperty("storage")
	public void setStorage(InfrastructureBareMetalStatus storage) {
		this.storage = storage;
	}
	
	
	/**
	 * 
	 * @return
	 * The network info 
	 */
	@JsonProperty("network")
	public InfrastructureBareMetalStatus getNetwork() {
		return network;
	}

	/**
	 * 
	 * @param network
	 * The network
	 */
	@JsonProperty("network")
	public void setNetwork(InfrastructureBareMetalStatus network) {
		this.network = network;
	}
	
	
	/**
	 * 
	 * @return
	 * The compute info 
	 */
	@JsonProperty("compute")
	public InfrastructureBareMetalStatus getCompute() {
		return compute;
	}

	/**
	 * 
	 * @param Compute
//	 * The Compute
	 */
	@JsonProperty("compute")
	public void setCompute(InfrastructureBareMetalStatus compute) {
		this.compute= compute;
	}
	
	/**
	 * 
	 * @return
	 * The available_nodes for compute info
	 */
	@JsonProperty("available_nodes")
	public int getAvailable_nodes() {
		return available_nodes;
	}

	/**
	 * 
	 * @param available_nodes
	 * The available_nodes
	 */
	@JsonProperty("available_nodes")
	public void setAvailable_nodes(int available_nodes) {
		this.available_nodes = available_nodes;
	}
	
	/**
	 * 
	 * @return
	 * The total_nodes for compute info
	 */
	@JsonProperty("total_nodes")
	public int getTotal_nodes() {
		return total_nodes;
	}

	/**
	 * 
	 * @param total_nodes
	 * The total_nodes
	 */
	@JsonProperty("total_nodes")
	public void setTotal_nodes(int total_nodes) {
		this.total_nodes = total_nodes;
	}
	

	/**
	 * 
	 * @return
	 * The MachineGroupsList
	 */
	@JsonProperty("machineGroups")
	public List<GroupsMachine> getGroupsMachine() {
		return machineGroups;
	}

	/**
	 * 
	 * @param iaasMachineGroups
	 * The MachineGroupsList
	 */
	@JsonProperty("machineGroups")
	public void setMachineGroups(List<GroupsMachine> machineGroups) {
		this.machineGroups = machineGroups;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

//	@JsonAnyGetter
//	public Map<String, Object> getAdditionalProperties() {
//		return this.additionalProperties;
//	}
//
//	@JsonAnySetter
//	public void setAdditionalProperty(String name, Object value) {
//		this.additionalProperties.put(name, value);
//	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(environment).append(storage).append(network).append(compute).append(available_nodes).append(total_nodes).append(machineGroups).toHashCode();
//																																							append(additionalProperties).
																																							
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof InfrastructurePicture) == false) {
			return false;
		}
		InfrastructurePicture rhs = ((InfrastructurePicture) other);
		return new EqualsBuilder().append(environment, rhs.environment).append(storage, rhs.storage).append(network, rhs.network).append(compute, rhs.compute).append(available_nodes, rhs.available_nodes).append(total_nodes, rhs.total_nodes).append(machineGroups, rhs.machineGroups).
																																																														//append(additionalProperties, rhs.additionalProperties).
																																																														isEquals();
	}

}

