package it.prisma.domain.dsl.iaas.openstack.orchestration.response;

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
@JsonPropertyOrder({ "disable_rollback", "description", "parameters",
		"stack_status_reason", "stack_name", "outputs", "creation_time",
		"links", "capabilities", "notification_topics", "timeout_mins",
		"stack_status", "updated_time", "id", "template_description" })
public class Stack {

	public enum Status {
		CREATE_IN_PROGRESS,
		CREATE_COMPLETE,
		CREATE_FAILED
	}
	
	@JsonProperty("disable_rollback")
	private Boolean disableRollback;
	@JsonProperty("description")
	private String description;
	@JsonProperty("parameters")
	private Parameters parameters;
	@JsonProperty("stack_status_reason")
	private String stackStatusReason;
	@JsonProperty("stack_name")
	private String stackName;
	@JsonProperty("outputs")
	private List<Output> outputs = new ArrayList<Output>();
	@JsonProperty("creation_time")
	private String creationTime;
	@JsonProperty("links")
	private List<Link> links = new ArrayList<Link>();
	@JsonProperty("capabilities")
	private List<Object> capabilities = new ArrayList<Object>();
	@JsonProperty("notification_topics")
	private List<Object> notificationTopics = new ArrayList<Object>();
	@JsonProperty("timeout_mins")
	private Long timeoutMins;
	@JsonProperty("stack_status")
	private String stackStatus;
	@JsonProperty("updated_time")
	private String updatedTime;
	@JsonProperty("id")
	private String id;
	@JsonProperty("template_description")
	private String templateDescription;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("disable_rollback")
	public Boolean getDisableRollback() {
		return disableRollback;
	}

	@JsonProperty("disable_rollback")
	public void setDisableRollback(Boolean disableRollback) {
		this.disableRollback = disableRollback;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("parameters")
	public Parameters getParameters() {
		return parameters;
	}

	@JsonProperty("parameters")
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	@JsonProperty("stack_status_reason")
	public String getStackStatusReason() {
		return stackStatusReason;
	}

	@JsonProperty("stack_status_reason")
	public void setStackStatusReason(String stackStatusReason) {
		this.stackStatusReason = stackStatusReason;
	}

	@JsonProperty("stack_name")
	public String getStackName() {
		return stackName;
	}

	@JsonProperty("stack_name")
	public void setStackName(String stackName) {
		this.stackName = stackName;
	}

	@JsonProperty("outputs")
	public List<Output> getOutputs() {
		return outputs;
	}

	@JsonProperty("outputs")
	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}

	@JsonProperty("creation_time")
	public String getCreationTime() {
		return creationTime;
	}

	@JsonProperty("creation_time")
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	@JsonProperty("links")
	public List<Link> getLinks() {
		return links;
	}

	@JsonProperty("links")
	public void setLinks(List<Link> links) {
		this.links = links;
	}

	@JsonProperty("capabilities")
	public List<Object> getCapabilities() {
		return capabilities;
	}

	@JsonProperty("capabilities")
	public void setCapabilities(List<Object> capabilities) {
		this.capabilities = capabilities;
	}

	@JsonProperty("notification_topics")
	public List<Object> getNotificationTopics() {
		return notificationTopics;
	}

	@JsonProperty("notification_topics")
	public void setNotificationTopics(List<Object> notificationTopics) {
		this.notificationTopics = notificationTopics;
	}

	@JsonProperty("timeout_mins")
	public Long getTimeoutMins() {
		return timeoutMins;
	}

	@JsonProperty("timeout_mins")
	public void setTimeoutMins(Long timeoutMins) {
		this.timeoutMins = timeoutMins;
	}

	@JsonProperty("stack_status")
	public String getStackStatus() {
		return stackStatus;
	}

	@JsonProperty("stack_status")
	public void setStackStatus(String stackStatus) {
		this.stackStatus = stackStatus;
	}

	@JsonProperty("updated_time")
	public String getUpdatedTime() {
		return updatedTime;
	}

	@JsonProperty("updated_time")
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("template_description")
	public String getTemplateDescription() {
		return templateDescription;
	}

	@JsonProperty("template_description")
	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
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