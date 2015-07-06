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
	"environment", "groups"
})
public class MonitoringWrappedResponsePaas {

	@JsonProperty("environment")
	private String environment;
	@JsonProperty("groups")
	private List<Group> groups = new ArrayList<Group>();
	
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The environment
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
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	/**
	 * 
	 * @return
	 * The groups
	 */
	@JsonProperty("groups")
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * 
	 * @param groups
	 * The groups
	 */
	@JsonProperty("groups")
	public void setGroups(List<Group> groups) {
		this.groups = groups;
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
		return new HashCodeBuilder().append(environment).append(groups).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof MonitoringWrappedResponsePaas) == false) {
			return false;
		}
		MonitoringWrappedResponsePaas rhs = ((MonitoringWrappedResponsePaas) other);
		return new EqualsBuilder().append(environment, rhs.environment).append(groups, rhs.groups).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}
}
