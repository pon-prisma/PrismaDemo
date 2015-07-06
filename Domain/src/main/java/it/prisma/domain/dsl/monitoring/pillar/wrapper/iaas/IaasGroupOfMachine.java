package it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	"iaasGroupName",
	"iaasMachines"
})
public class IaasGroupOfMachine {

	@JsonProperty("iaasGroupName")
	private String iaasGroupName;
	@JsonProperty("iaasMachines")
	private List<IaasMachine> iaasMachines = new ArrayList<IaasMachine>();
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The iaasGroupName
	 */
	@JsonProperty("iaasGroupName")
	public String getIaasGroupName() {
		return iaasGroupName;
	}

	/**
	 * 
	 * @param iaasGroupName
	 * The iaasGroupName
	 */
	@JsonProperty("iaasGroupName")
	public void setIaasGroupName(String iaasGroupName) {
		this.iaasGroupName = iaasGroupName;
	}

	/**
	 * 
	 * @return
	 * The iaasMachinesList
	 */
	@JsonProperty("iaasMachines")
	public List<IaasMachine> getIaasMachines() {
		return iaasMachines;
	}

	/**
	 * 
	 * @param iaasMachinesList
	 * The iaasMachinesList
	 */
	@JsonProperty("iaasMachines")
	public void setIaasMachinesList(List<IaasMachine> iaasMachines) {
		this.iaasMachines = iaasMachines;
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
		return new HashCodeBuilder().append(iaasGroupName).append(iaasMachines).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof IaasGroupOfMachine) == false) {
			return false;
		}
		IaasGroupOfMachine rhs = ((IaasGroupOfMachine) other);
		return new EqualsBuilder().append(iaasGroupName, rhs.iaasGroupName).append(iaasMachines, rhs.iaasMachines).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}

}