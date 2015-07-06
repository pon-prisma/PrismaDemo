package it.prisma.domain.dsl.paas.services.generic.request;

import java.io.Serializable;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "name", "description", "domainName", "notificationEmail",
		"loadBalancingInstances", "publicVisibility", "secureConnection",
		"environment" })
public class ServiceDetails implements Serializable {

	private static final long serialVersionUID = 6861213674253769764L;

	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("domainName")
	private String domainName;
	@JsonProperty("notificationEmail")
	private String notificationEmail;
	@JsonProperty("loadBalancingInstances")
	private Integer loadBalancingInstances;
	@JsonProperty("publicVisibility")
	private Boolean publicVisibility;
	@JsonProperty("secureConnection")
	private SecureConnection secureConnection;
	@JsonProperty("environment")
	private Environment environment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}

	public Integer getLoadBalancingInstances() {
		return loadBalancingInstances;
	}

	public void setLoadBalancingInstances(Integer loadBalancingInstances) {
		this.loadBalancingInstances = loadBalancingInstances;
	}

	public boolean isPublicVisibility() {
		return this.publicVisibility;
	}

	public void setPublicVisibility(boolean publicVisibility) {
		this.publicVisibility = publicVisibility;
	}
	

	public SecureConnection getSecureConnection() {
		return secureConnection;
	}

	public void setSecureConnection(SecureConnection secureConnection) {
		this.secureConnection = secureConnection;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
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
