package it.prisma.domain.dsl.monitoring.businesslayer.paas.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@JsonPropertyOrder({ "name", "IP", "metrics" })
public class Host {

	@JsonProperty("name")
	private String name;
	@JsonProperty("IP")
	private String IP;
	@JsonProperty("metrics")
	private List<Metric> metrics = new ArrayList<Metric>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
//	 * 
	 * @return The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The IP
	 */
	@JsonProperty("IP")
	public String getIP() {
		return IP;
	}

	/**
	 * 
	 * @param IP
	 *            The IP
	 */
	@JsonProperty("IP")
	public void setIP(String IP) {
		this.IP = IP;
	}

	/**
	 * 
	 * @return The metrics
	 */
	@JsonProperty("metrics")
	public List<Metric> getMetrics() {
		return metrics;
	}

	/**
	 * 
	 * @param metrics
	 *            The metrics
	 */
	@JsonProperty("metrics")
	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
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
		return new HashCodeBuilder().append(name).append(IP).append(metrics)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Host) == false) {
			return false;
		}
		Host rhs = ((Host) other);
		return new EqualsBuilder().append(name, rhs.name).append(IP, rhs.IP)
				.append(metrics, rhs.metrics)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}