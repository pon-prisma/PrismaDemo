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

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Generated("org.jsonschema2pojo")
	@JsonPropertyOrder({
		"groupsIDInMetrics",
		"groupsIDinWatcher"
	})
	public class UpdatedPaasGroup {

		@JsonProperty("groupsIdInMetrics")
		private List<String> groupsIDInMetrics = new ArrayList<String>();
		@JsonProperty("groupsIDinWatcher")
		private List<String> groupsIDinWatcher = new ArrayList<String>();

		/**
		 * 
		 * @return
		 * The groupsIDInMetrics
		 */
		@JsonProperty("groupsIDInMetrics")
		public List<String> getGroupsIDInMetrics() {
			return groupsIDInMetrics;
		}

		/**
		 * 
		 * @param groupsIdInMetrics
		 * The groupsIdInMetrics
		 */
		@JsonProperty("groupsIDInMetrics")
		public void setGroupsIDInMetrics(List<String> groupsIDInMetrics) {
			this.groupsIDInMetrics = groupsIDInMetrics;
		}

		/**
		 * 
		 * @return
		 * The groupsIDinWatcher
		 */
		@JsonProperty("groupsIDinWatcher")
		public List<String> getGroupsIDinWatcher() {
			return groupsIDinWatcher;
		}

		/**
		 * 
		 * @param hostIDinWatcher
		 * The hostIDinWatcher
		 */
		@JsonProperty("groupsIDinWatcher")
		public void setGroupsIDinWatcher(List<String> groupsIDinWatcher) {
			this.groupsIDinWatcher = groupsIDinWatcher;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(groupsIDInMetrics).append(groupsIDinWatcher).toHashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if ((other instanceof UpdatedPaasGroup) == false) {
				return false;
			}
			UpdatedPaasGroup rhs = ((UpdatedPaasGroup) other);
			return new EqualsBuilder().append(groupsIDInMetrics, rhs.getGroupsIDInMetrics()).append(groupsIDinWatcher, rhs.groupsIDinWatcher).isEquals();
		}

	}

