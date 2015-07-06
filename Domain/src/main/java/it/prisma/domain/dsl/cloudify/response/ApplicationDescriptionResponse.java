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
@JsonPropertyOrder({ "servicesDescription", "applicationName", "authGroups",
		"applicationState" })
public class ApplicationDescriptionResponse implements Serializable {

	private static final long serialVersionUID = 4437298256082467485L;

	@JsonProperty("servicesDescription")
	private List<ServicesDescription> servicesDescription = new ArrayList<ServicesDescription>();
	@JsonProperty("applicationName")
	private String applicationName;
	@JsonProperty("authGroups")
	private String authGroups;
	@JsonProperty("applicationState")
	private String applicationState;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("servicesDescription")
	public List<ServicesDescription> getServicesDescription() {
		return servicesDescription;
	}

	@JsonProperty("servicesDescription")
	public void setServicesDescription(
			List<ServicesDescription> servicesDescription) {
		this.servicesDescription = servicesDescription;
	}

	@JsonProperty("applicationName")
	public String getApplicationName() {
		return applicationName;
	}

	@JsonProperty("applicationName")
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@JsonProperty("authGroups")
	public String getAuthGroups() {
		return authGroups;
	}

	@JsonProperty("authGroups")
	public void setAuthGroups(String authGroups) {
		this.authGroups = authGroups;
	}

	@JsonProperty("applicationState")
	public String getApplicationState() {
		return applicationState;
	}

	@JsonProperty("applicationState")
	public void setApplicationState(String applicationState) {
		this.applicationState = applicationState;
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
