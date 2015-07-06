package it.prisma.domain.dsl.monitoring.pillar.wrapper.paas;

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
	"groupName",
	"paasMachines"
})
public class Group {

	@JsonProperty("groupName")
	private String groupName;
	@JsonProperty("paasMachines")
	private List<PaasMachine> paasMachines = new ArrayList<PaasMachine>();
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The categoryName
	 */
	@JsonProperty("groupName")
	public String getGroupName() {
		return groupName;
	}

	/**
	 * 
	 * @param groupName
	 * The groupName
	 */
	@JsonProperty("groupName")
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	/**
	 * 
	 * @return
	 * The paasMachines
	 */
	@JsonProperty("paasMachines")
	public List<PaasMachine> getPaasMachines() {
		return paasMachines;
	}

	/**
	 * 
	 * @param paasMachines
	 * The paasMachines
	 */
	@JsonProperty("paasMachines")
	public void setPaasMachines(List<PaasMachine> paasMachines) {
		this.paasMachines = paasMachines;
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
		return new HashCodeBuilder().append(groupName).append(paasMachines).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Group) == false) {
			return false;
		}
		Group rhs = ((Group) other);
		return new EqualsBuilder().append(groupName, rhs.groupName).append(paasMachines, rhs.paasMachines).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}

}