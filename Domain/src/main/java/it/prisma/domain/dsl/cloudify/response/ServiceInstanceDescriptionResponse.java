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
@JsonPropertyOrder({ "instanceId", "applicationName", "serviceName",
		"privateIp", "publicIp", "processDetails", "imageId", "hardwareId",
		"serviceInstanceName", "templateName", "machineId" })
public class ServiceInstanceDescriptionResponse implements Serializable {

	private static final long serialVersionUID = -7107224762108881528L;

	@JsonProperty("instanceId")
	private Integer instanceId;
	@JsonProperty("applicationName")
	private String applicationName;
	@JsonProperty("serviceName")
	private String serviceName;
	@JsonProperty("privateIp")
	private String privateIp;
	@JsonProperty("publicIp")
	private String publicIp;
	@JsonProperty("processDetails")
	private ProcessDetails processDetails;
	@JsonProperty("imageId")
	private String imageId;
	@JsonProperty("hardwareId")
	private String hardwareId;
	@JsonProperty("serviceInstanceName")
	private String serviceInstanceName;
	@JsonProperty("templateName")
	private String templateName;
	@JsonProperty("machineId")
	private String machineId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("instanceId")
	public Integer getInstanceId() {
		return instanceId;
	}

	@JsonProperty("instanceId")
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	@JsonProperty("applicationName")
	public String getApplicationName() {
		return applicationName;
	}

	@JsonProperty("applicationName")
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@JsonProperty("serviceName")
	public String getServiceName() {
		return serviceName;
	}

	@JsonProperty("serviceName")
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@JsonProperty("privateIp")
	public String getPrivateIp() {
		return privateIp;
	}

	@JsonProperty("privateIp")
	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	@JsonProperty("publicIp")
	public String getPublicIp() {
		return publicIp;
	}

	@JsonProperty("publicIp")
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	@JsonProperty("processDetails")
	public ProcessDetails getProcessDetails() {
		return processDetails;
	}

	@JsonProperty("processDetails")
	public void setProcessDetails(ProcessDetails processDetails) {
		this.processDetails = processDetails;
	}

	@JsonProperty("imageId")
	public String getImageId() {
		return imageId;
	}

	@JsonProperty("imageId")
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	@JsonProperty("hardwareId")
	public String getHardwareId() {
		return hardwareId;
	}

	@JsonProperty("hardwareId")
	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}

	@JsonProperty("serviceInstanceName")
	public String getServiceInstanceName() {
		return serviceInstanceName;
	}

	@JsonProperty("serviceInstanceName")
	public void setServiceInstanceName(String serviceInstanceName) {
		this.serviceInstanceName = serviceInstanceName;
	}

	@JsonProperty("templateName")
	public String getTemplateName() {
		return templateName;
	}

	@JsonProperty("templateName")
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@JsonProperty("machineId")
	public String getMachineId() {
		return machineId;
	}

	@JsonProperty("machineId")
	public void setMachineId(String machineId) {
		this.machineId = machineId;
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
