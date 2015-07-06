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
	"hostGroup",
	"hostAffecteds"
})
public class WrappedIaasHealthByTrigger {

	@JsonProperty("hostGroup")
	private String hostGroup;
	@JsonProperty("hostAffecteds")
	private List<HostAffected> hostAffecteds = new ArrayList<HostAffected>();
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The hostGroup
	 */
	@JsonProperty("hostGroup")
	public String getHostGroup() {
		return hostGroup;
	}

	/**
	 * 
	 * @param hostGroup
	 * The hostGroup
	 */
	@JsonProperty("hostGroup")
	public void setHostGroup(String hostGroup) {
		this.hostGroup = hostGroup;
	}

	/**
	 * 
	 * @return
	 * The hostsAffected
	 */
	@JsonProperty("hostAffecteds")
	public List<HostAffected> getHostAffecteds() {
		return hostAffecteds;
	}

	/**
	 * 
	 * @param hostsAffected
	 * The hostsAffected
	 */
	@JsonProperty("hostAffecteds")
	public void setHostAffecteds(List<HostAffected> hostAffecteds) {
		this.hostAffecteds = hostAffecteds;
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
		return new HashCodeBuilder().append(hostGroup).append(hostAffecteds).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof WrappedIaasHealthByTrigger) == false) {
			return false;
		}
		WrappedIaasHealthByTrigger rhs = ((WrappedIaasHealthByTrigger) other);
		return new EqualsBuilder().append(hostGroup, rhs.hostGroup).append(hostAffecteds, rhs.hostAffecteds).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}

}