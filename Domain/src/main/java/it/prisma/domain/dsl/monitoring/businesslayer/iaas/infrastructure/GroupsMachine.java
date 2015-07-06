package it.prisma.domain.dsl.monitoring.businesslayer.iaas.infrastructure;

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
		"infrastructureGroupName",
		"infrastructureMachine"
	})
	public class GroupsMachine {

		@JsonProperty("infrastructureGroupName")
		private String infrastructureGroupName;
		@JsonProperty("infrastructureMachine")
		private List<InfrastructureMachine> infrastructureMachine = new ArrayList<InfrastructureMachine>();
//		@JsonIgnore
//		private Map<String, Object> additionalProperties = new HashMap<String, Object>();

		/**
		 * 
		 * @return
		 * The infrastructureGroupName
		 */
		@JsonProperty("infrastructureGroupName")
		public String getInfrastructureGroupName() {
			return infrastructureGroupName;
		}

		/**
		 * 
		 * @param infrastructureGroupName
		 * The infrastructureGroupName
		 */
		@JsonProperty("infrastructureGroupName")
		public void setInfrastructureGroupName(String infrastructureGroupName) {
			this.infrastructureGroupName = infrastructureGroupName;
		}

		/**
		 * 
		 * @return
		 * The iaasMachinesList
		 */
		@JsonProperty("infrastructureMachine")
		public List<InfrastructureMachine> getInfrastructureMachine() {
			return infrastructureMachine;
		}

		/**
		 * 
		 * @param infrastructureMachinesList
		 * The infrastructureMachinesList
		 */
		@JsonProperty("infrastructureMachine")
		public void setInfrastructureMachine(List<InfrastructureMachine> infrastructureMachine) {
			this.infrastructureMachine = infrastructureMachine;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}

//		@JsonAnyGetter
//		public Map<String, Object> getAdditionalProperties() {
//			return this.additionalProperties;
//		}
//
//		@JsonAnySetter
//		public void setAdditionalProperty(String name, Object value) {
//			this.additionalProperties.put(name, value);
//		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(infrastructureGroupName).append(infrastructureMachine).//append(additionalProperties).
					toHashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if ((other instanceof GroupsMachine) == false) {
				return false;
			}
			GroupsMachine rhs = ((GroupsMachine) other);
			return new EqualsBuilder().append(infrastructureGroupName, rhs.infrastructureGroupName).append(infrastructureMachine, rhs.infrastructureMachine).//append(additionalProperties, rhs.additionalProperties).
					isEquals();
		}

	}
