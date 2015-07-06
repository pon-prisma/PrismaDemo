package it.prisma.domain.dsl.cloudify.response;

import java.io.Serializable;
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
@JsonPropertyOrder({ "serviceName", "instanceCount", "plannedInstances",
		"instancesDescription", "applicationName", "serviceState",
		"deploymentId" })
public class ServicesDescription implements Serializable {

	private static final long serialVersionUID = 7181138321499167632L;

	@JsonProperty("serviceName")
	private String serviceName;
	@JsonProperty("instanceCount")
	private Integer instanceCount;
	@JsonProperty("plannedInstances")
	private Integer plannedInstances;
	@JsonProperty("instancesDescription")
	private List<InstancesDescription> instancesDescription = new ArrayList<InstancesDescription>();
	@JsonProperty("applicationName")
	private String applicationName;
	@JsonProperty("serviceState")
	private String serviceState;
	@JsonProperty("deploymentId")
	private Object deploymentId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("serviceName")
	public String getServiceName() {
		return serviceName;
	}

	@JsonProperty("serviceName")
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@JsonProperty("instanceCount")
	public Integer getInstanceCount() {
		return instanceCount;
	}

	@JsonProperty("instanceCount")
	public void setInstanceCount(Integer instanceCount) {
		this.instanceCount = instanceCount;
	}

	@JsonProperty("plannedInstances")
	public Integer getPlannedInstances() {
		return plannedInstances;
	}

	@JsonProperty("plannedInstances")
	public void setPlannedInstances(Integer plannedInstances) {
		this.plannedInstances = plannedInstances;
	}

	@JsonProperty("instancesDescription")
	public List<InstancesDescription> getInstancesDescription() {
		return instancesDescription;
	}

	@JsonProperty("instancesDescription")
	public void setInstancesDescription(
			List<InstancesDescription> instancesDescription) {
		this.instancesDescription = instancesDescription;
	}

	@JsonProperty("applicationName")
	public String getApplicationName() {
		return applicationName;
	}

	@JsonProperty("applicationName")
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@JsonProperty("serviceState")
	public String getServiceState() {
		return serviceState;
	}

	@JsonProperty("serviceState")
	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}

	@JsonProperty("deploymentId")
	public Object getDeploymentId() {
		return deploymentId;
	}

	@JsonProperty("deploymentId")
	public void setDeploymentId(Object deploymentId) {
		this.deploymentId = deploymentId;
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