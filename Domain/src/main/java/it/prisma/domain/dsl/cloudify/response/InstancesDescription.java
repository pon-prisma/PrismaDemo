package it.prisma.domain.dsl.cloudify.response;

import java.io.Serializable;
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
@JsonPropertyOrder({ "instanceName", "instanceStatus", "instanceId",
		"hostName", "hostAddress" })
public class InstancesDescription implements Serializable {

	private static final long serialVersionUID = 1936312225167577553L;

	@JsonProperty("instanceName")
	private String instanceName;
	@JsonProperty("instanceStatus")
	private String instanceStatus;
	@JsonProperty("instanceId")
	private Integer instanceId;
	@JsonProperty("hostName")
	private String hostName;
	@JsonProperty("hostAddress")
	private String hostAddress;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("instanceName")
	public String getInstanceName() {
		return instanceName;
	}

	@JsonProperty("instanceName")
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	@JsonProperty("instanceStatus")
	public String getInstanceStatus() {
		return instanceStatus;
	}

	@JsonProperty("instanceStatus")
	public void setInstanceStatus(String instanceStatus) {
		this.instanceStatus = instanceStatus;
	}

	@JsonProperty("instanceId")
	public Integer getInstanceId() {
		return instanceId;
	}

	@JsonProperty("instanceId")
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	@JsonProperty("hostName")
	public String getHostName() {
		return hostName;
	}

	@JsonProperty("hostName")
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@JsonProperty("hostAddress")
	public String getHostAddress() {
		return hostAddress;
	}

	@JsonProperty("hostAddress")
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
