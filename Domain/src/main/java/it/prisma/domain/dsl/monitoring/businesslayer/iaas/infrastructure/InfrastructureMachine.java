package it.prisma.domain.dsl.monitoring.businesslayer.iaas.infrastructure;

import java.util.ArrayList;

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
	"machineName",
	"machineIP",
	"machineStatus", 
	"machineMetrics", 
	"description"
})
public class InfrastructureMachine {

	@JsonProperty("machineName")
	private String machineName;
	@JsonProperty("machineIP")
	private String machineIP;
	@JsonProperty("machineStatus")
	private String machineStatus;
	@JsonProperty("machineMetrics")
	private ArrayList<String> machineMetrics;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The machineName
	 */
	@JsonProperty("machineName")
	public String getMachineName() {
		return machineName;
	}

	/**
	 * 
	 * @param machineName
	 * The machineName
	 */
	@JsonProperty("machineName")
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	
	
	/**
	 * 
	 * @return
	 * The machineIP
	 */
	@JsonProperty("machineIP")
	public String getMachineIP() {
		return machineIP;
	}

	/**
	 * 
	 * @param machineIP
	 * The machineIP
	 */
	@JsonProperty("machineIP")
	public void setMachineIP(String machineIP) {
		this.machineIP = machineIP;
	}

	/**
	 * 
	 * @return
	 * The machineStatus
	 */
	@JsonProperty("machineStatus")
	public String getMachineStatus() {
		return machineStatus;
	}

	/**
	 * 
	 * @param machineStatus
	 * The machineStatus
	 */
	@JsonProperty("machineStatus")
	public void setMachineStatus(String machineStatus) {
		this.machineStatus = machineStatus;
	}
	
	/**
	 * 
	 * @return
	 * The machineMetrics
	 */
	@JsonProperty("machineMetrics")
	public ArrayList<String> getMachineMetrics() {
		return machineMetrics;
	}

	/**
	 * 
	 * @param machineMetrics
	 * The machineMetrics
	 */
	@JsonProperty("machineMetrics")
	public void setMachineMetrics(ArrayList<String> machineMetrics) {
		this.machineMetrics = machineMetrics;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(machineName).append(machineIP).append(machineStatus).append(machineStatus).append(machineMetrics).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof InfrastructureMachine) == false) {
			return false;
		}
		InfrastructureMachine rhs = ((InfrastructureMachine) other);
		return new EqualsBuilder().append(machineName, rhs.machineName).append(machineIP, rhs.machineIP).append(machineStatus, rhs.machineStatus).append(machineMetrics, rhs.machineMetrics).isEquals();
	}
}
