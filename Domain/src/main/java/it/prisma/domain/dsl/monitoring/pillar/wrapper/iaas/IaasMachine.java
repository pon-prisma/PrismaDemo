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
	"machineName",
	"connection",
	"metrics",
	"ip"
})
public class IaasMachine {

	@JsonProperty("machineName")
	private String machineName;
	@JsonProperty("connection")
	private String connection;
	@JsonProperty("metrics")
	private List<IaaSMetric> metrics = new ArrayList<IaaSMetric>();
	@JsonProperty("ip")
	private String ip;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The machineName
	 */
	@JsonProperty("machineName")
	public String getMachineName() {
		return machineName;
	}

	/**
	 * 
	 * @param machineName
	 * The machineName
	 */
	@JsonProperty("machineName")
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	/**
	 * 
	 * @return
	 * The connection
	 */
	@JsonProperty("connection")
	public String getConnection() {
		return connection;
	}

	/**
	 * 
	 * @param connection
	 * The connection
	 */
	@JsonProperty("connection")
	public void setConnection(String connection) {
		this.connection = connection;
	}

	/**
	 * 
	 * @return
	 * The metrics
	 */
	@JsonProperty("metrics")
	public List<IaaSMetric> getMetrics() {
		return metrics;
	}

	/**
	 * 
	 * @param metrics
	 * The metrics
	 */
	@JsonProperty("metrics")
	public void setMetrics(List<IaaSMetric> metrics) {
		this.metrics = metrics;
	}

	/**
	 * 
	 * @return
	 * The ip
	 */
	@JsonProperty("ip")
	public String getIp() {
		return ip;
	}

	/**
	 * 
	 * @param ip
	 * The ip
	 */
	@JsonProperty("ip")
	public void setIp(String ip) {
		this.ip = ip;
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
		return new HashCodeBuilder().append(machineName).append(connection).append(metrics).append(ip).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof IaasMachine) == false) {
			return false;
		}
		IaasMachine rhs = ((IaasMachine) other);
		return new EqualsBuilder().append(machineName, rhs.machineName).append(connection, rhs.connection).append(metrics, rhs.metrics).append(ip, rhs.ip).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}
}