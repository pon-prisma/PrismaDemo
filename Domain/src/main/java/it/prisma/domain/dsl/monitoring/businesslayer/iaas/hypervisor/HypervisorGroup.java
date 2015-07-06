package it.prisma.domain.dsl.monitoring.businesslayer.iaas.hypervisor;

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
	"hypervisors"
})
public class HypervisorGroup {

	@JsonProperty("hypervisors")
	private List<Hypervisor> hypervisors = new ArrayList<Hypervisor>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The hypervisors
	 */
	@JsonProperty("hypervisors")
	public List<Hypervisor> getHypervisors() {
		return hypervisors;
	}

	/**
	 * 
	 * @param hypervisors
	 * The hypervisors
	 */
	@JsonProperty("hypervisors")
	public void setHypervisors(List<Hypervisor> hypervisors) {
		this.hypervisors = hypervisors;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(hypervisors).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof HypervisorGroup) == false) {
			return false;
		}
		HypervisorGroup rhs = ((HypervisorGroup) other);
		return new EqualsBuilder().append(hypervisors, rhs.hypervisors).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}