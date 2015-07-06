package it.prisma.domain.dsl.iaas.openstack.orchestration.request;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "stack_name", "template_url", "parameters", "timeout_mins" })
public class OpenstackCreateStackRequest {

	@JsonProperty("stack_name")
	private String stackName;
	@JsonProperty("template_url")
	private String templateUrl;
	@JsonProperty("parameters")
	private Map<String, Object> parameters = new HashMap<String, Object>();
	@JsonProperty("timeout_mins")
	private String timeoutMins;

	@JsonProperty("stack_name")
	public String getStackName() {
		return stackName;
	}

	@JsonProperty("stack_name")
	public void setStackName(String stackName) {
		this.stackName = stackName;
	}

	@JsonProperty("template_url")
	public String getTemplateUrl() {
		return templateUrl;
	}

	@JsonProperty("template_url")
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	@JsonProperty("parameters")
	public Map<String, Object> getProperties() {
		return this.parameters;
	}

	@JsonProperty("parameters")
	public void addProperty(String name, Object value) {
		this.parameters.put(name, value);
	}

	@JsonProperty("timeout_mins")
	public String getTimeoutMins() {
		return timeoutMins;
	}

	@JsonProperty("timeout_mins")
	public void setTimeoutMins(String timeoutMins) {
		this.timeoutMins = timeoutMins;
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

}