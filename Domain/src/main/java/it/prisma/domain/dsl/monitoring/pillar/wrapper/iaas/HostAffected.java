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
	"hostName",
	"triggerShots"
})
public class HostAffected {

	@JsonProperty("hostName")
	private String hostName;
	@JsonProperty("triggerShots")
	private List<TriggerShot> triggerShots = new ArrayList<TriggerShot>();
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The hostName
	 */
	@JsonProperty("hostName")
	public String getHostName() {
		return hostName;
	}

	/**
	 * 
	 * @param hostName
	 * The hostName
	 */
	@JsonProperty("hostName")
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * 
	 * @return
	 * The triggerShot
	 */
	@JsonProperty("triggerShots")
	public List<TriggerShot> getTriggerShots() {
		return triggerShots;
	}

	/**
	 * 
	 * @param triggerShot
	 * The triggerShot
	 */
	@JsonProperty("triggerShots")
	public void setTriggerShots(List<TriggerShot> triggerShots) {
		this.triggerShots = triggerShots;
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
		return new HashCodeBuilder().append(hostName).append(triggerShots).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof HostAffected) == false) {
			return false;
		}
		HostAffected rhs = ((HostAffected) other);
		return new EqualsBuilder().append(hostName, rhs.hostName).append(triggerShots, rhs.triggerShots).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}

}