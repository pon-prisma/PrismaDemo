package it.prisma.domain.dsl.monitoring.businesslayer.paas.response;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "host" })
public class NewMonitoredHostRequest {

	@JsonProperty("host")
	private Host host;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The host
	 */
	@JsonProperty("host")
	public Host getHost() {
		return host;
	}

	/**
	 * 
	 * @param host
	 *            The host
	 */
	@JsonProperty("host")
	public void setHost(Host host) {
		this.host = host;
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
		return new HashCodeBuilder().append(host).append(additionalProperties)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof NewMonitoredHostRequest) == false) {
			return false;
		}
		NewMonitoredHostRequest rhs = ((NewMonitoredHostRequest) other);
		return new EqualsBuilder().append(host, rhs.host)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}