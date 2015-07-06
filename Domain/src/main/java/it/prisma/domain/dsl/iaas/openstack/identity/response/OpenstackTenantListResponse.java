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
@JsonPropertyOrder({ "tenants", "tenants_links" })
public class OpenstackTenantListResponse {

	@JsonProperty("tenants")
	private List<Tenant> tenants = new ArrayList<Tenant>();
	@JsonProperty("tenants_links")
	private List<Object> tenantsLinks = new ArrayList<Object>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("tenants")
	public List<Tenant> getTenants() {
		return tenants;
	}

	@JsonProperty("tenants")
	public void setTenants(List<Tenant> tenants) {
		this.tenants = tenants;
	}

	@JsonProperty("tenants_links")
	public List<Object> getTenantsLinks() {
		return tenantsLinks;
	}

	@JsonProperty("tenants_links")
	public void setTenantsLinks(List<Object> tenantsLinks) {
		this.tenantsLinks = tenantsLinks;
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