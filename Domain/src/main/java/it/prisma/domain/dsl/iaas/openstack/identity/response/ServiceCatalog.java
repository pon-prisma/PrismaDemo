package it.prisma.domain.dsl.iaas.openstack.identity.response;

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
@JsonPropertyOrder({ "endpoints", "endpoints_links", "type", "name" })
public class ServiceCatalog {

	@JsonProperty("endpoints")
	private List<Endpoint> endpoints = new ArrayList<Endpoint>();
	@JsonProperty("endpoints_links")
	private List<Object> endpointsLinks = new ArrayList<Object>();
	@JsonProperty("type")
	private String type;
	@JsonProperty("name")
	private String name;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("endpoints")
	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	@JsonProperty("endpoints")
	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	@JsonProperty("endpoints_links")
	public List<Object> getEndpointsLinks() {
		return endpointsLinks;
	}

	@JsonProperty("endpoints_links")
	public void setEndpointsLinks(List<Object> endpointsLinks) {
		this.endpointsLinks = endpointsLinks;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
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