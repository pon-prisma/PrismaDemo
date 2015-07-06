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
	"uuid",
	"serviceId"
})
public class HostMonitoringDeleteRequest {

	@JsonProperty("uuid")
	private String uuid;
	@JsonProperty("serviceId")
	private String serviceId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The uuid
	 */
	@JsonProperty("uuid")
	public String getUuid() {
		return uuid;
	}

	/**
	 * 
	 * @param uuid
	 * The uuid
	 */
	@JsonProperty("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * 
	 * @return
	 * The serviceId
	 */
	@JsonProperty("serviceId")
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * @param serviceId
	 * The serviceId
	 */
	@JsonProperty("serviceId")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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
		return new HashCodeBuilder().append(uuid).append(serviceId).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof HostMonitoringDeleteRequest) == false) {
			return false;
		}
		HostMonitoringDeleteRequest rhs = ((HostMonitoringDeleteRequest) other);
		return new EqualsBuilder().append(uuid, rhs.uuid).append(serviceId, rhs.serviceId).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}
