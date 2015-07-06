package it.prisma.domain.dsl.monitoring.businesslayer.paas.request;


import java.util.HashMap;
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
	"hostGroupName"
})
public class HostGroupMonitoringCreateRequest {

	@JsonProperty("hostGroupName")
	private String hostGroupName;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The hostGroupName
	 */
	@JsonProperty("hostGroupName")
	public String getHostGroupName() {
		return hostGroupName;
	}

	/**
	 * 
	 * @param hostGroupName
	 * The hostGroupName
	 */
	@JsonProperty("hostGroupName")
	public void setHostGroupName(String hostGroupName) {
		this.hostGroupName = hostGroupName;
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
		return new HashCodeBuilder().append(hostGroupName).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof HostGroupMonitoringCreateRequest) == false) {
			return false;
		}
		HostGroupMonitoringCreateRequest rhs = ((HostGroupMonitoringCreateRequest) other);
		return new EqualsBuilder().append(hostGroupName, rhs.hostGroupName).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}
