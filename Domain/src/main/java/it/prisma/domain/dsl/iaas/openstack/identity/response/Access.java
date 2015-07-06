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
@JsonPropertyOrder({ "token", "serviceCatalog", "user", "metadata" })
public class Access {

	@JsonProperty("token")
	private Token token;
	@JsonProperty("serviceCatalog")
	private List<ServiceCatalog> serviceCatalog = new ArrayList<ServiceCatalog>();
	@JsonProperty("user")
	private User user;
	@JsonProperty("metadata")
	private Metadata metadata;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("token")
	public Token getToken() {
		return token;
	}

	@JsonProperty("token")
	public void setToken(Token token) {
		this.token = token;
	}

	@JsonProperty("serviceCatalog")
	public List<ServiceCatalog> getServiceCatalog() {
		return serviceCatalog;
	}

	@JsonProperty("serviceCatalog")
	public void setServiceCatalog(List<ServiceCatalog> serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	@JsonProperty("user")
	public User getUser() {
		return user;
	}

	@JsonProperty("user")
	public void setUser(User user) {
		this.user = user;
	}

	@JsonProperty("metadata")
	public Metadata getMetadata() {
		return metadata;
	}

	@JsonProperty("metadata")
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
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