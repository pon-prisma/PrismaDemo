package it.prisma.domain.dsl.paas.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
@JsonPropertyOrder({ "paasServiceId", "userId", "workgroupId", "name",
		"description", "domainName", "securityGroup", "iaaSFlavor",
		"loadBalancingInstances", "secureConnectionEnabled", "certificatePath",
		"qos", "publicVisibility", "availabilityZone", "notificationEmail",
		"createdAt", "status", "operation", "errorDescription", "events",
		"endpoints" })
public class PaaSServiceRepresentation implements
		GenericPaaSServiceRepresentation {

	public enum PaaSServiceType {

		DBaaS("DBaaS"), BIaaS("BIaaS"), VMaaS("VMaaS"), APPaaS("APPaaS"), APPaaSEnvironment(
				"APPaaSEnvironment"), MQaaS("QueueaaS");
		String name;

		PaaSServiceType(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}

		public PaaSServiceType getServiceType(String serviceType) {
			return PaaSServiceType.valueOf(serviceType);
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6861213674253769764L;

	@JsonProperty("id")
	protected Long id;
	@JsonProperty("userId")
	protected Long userId;
	@JsonProperty("workgroupId")
	protected Long workgroupId;

	// ? protected PaaSDeployment paaSDeployment;
	@JsonProperty("name")
	protected String name;
	@JsonProperty("description")
	protected String description;
	@JsonProperty("status")
	protected String status;
	@JsonProperty("operation")
	protected String operation;
	@JsonProperty("errorDescription")
	protected String errorDescription;
	@JsonProperty("createdAt")
	protected Date createdAt;
	@JsonProperty("domainName")
	protected String domainName;
	@JsonProperty("notificationEmail")
	protected String notificationEmail;
	@JsonProperty("secureConnectionEnabled")
	protected Boolean secureConnectionEnabled;
	@JsonProperty("certificatePath")
	protected String certificatePath;
	@JsonProperty("securityGroup")
	protected String securityGroup;
	@JsonProperty("iaaSFlavor")
	protected String iaaSFlavor;
	@JsonProperty("loadBalancingInstances")
	protected Integer loadBalancingInstances;
	@JsonProperty("qos")
	protected String qos;
	@JsonProperty("publicVisibility")
	protected Boolean publicVisibility = false;
	@JsonProperty("availabilityZone")
	protected String availabilityZone;
	@JsonProperty("events")
	protected Set<PaaSServiceEventRepresentation> paaSServiceEvents = new HashSet<PaaSServiceEventRepresentation>(
			0);
	@JsonProperty("endpoints")
	protected List<PaaSServiceEndpointRepresentation> paasServiceEndpoints = new ArrayList<PaaSServiceEndpointRepresentation>(
			0);

	@JsonProperty("productSpecificParameters")
	protected ProductSpecificParameters productSpecificParameters;

	@JsonIgnore
	protected Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public List<PaaSServiceEndpointRepresentation> getPaasServiceEndpoints() {
		return paasServiceEndpoints;
	}

	public void setPaasServiceEndpoints(
			List<PaaSServiceEndpointRepresentation> paasServiceEndpoints) {
		this.paasServiceEndpoints = paasServiceEndpoints;
	}

	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public Boolean getSecureConnectionEnabled() {
		return secureConnectionEnabled;
	}

	public void setSecureConnectionEnabled(Boolean secureConnectionEnabled) {
		this.secureConnectionEnabled = secureConnectionEnabled;
	}

	public String getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	public String getSecurityGroup() {
		return securityGroup;
	}

	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}

	public String getIaaSFlavor() {
		return iaaSFlavor;
	}

	public void setIaaSFlavor(String iaaSFlavor) {
		this.iaaSFlavor = iaaSFlavor;
	}

	public Integer getLoadBalancingInstances() {
		return loadBalancingInstances;
	}

	public void setLoadBalancingInstances(Integer loadBalancingInstances) {
		this.loadBalancingInstances = loadBalancingInstances;
	}

	public String getQoS() {
		return this.qos;
	}

	public void setQoS(String QoS) {
		this.qos = QoS;
	}

	public boolean isPublicVisibility() {
		return this.publicVisibility;
	}

	public void setPublicVisibility(boolean publicVisibility) {
		this.publicVisibility = publicVisibility;
	}

	public String getAvailabilityZone() {
		return this.availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public Set<PaaSServiceEventRepresentation> getPaaSServiceEvents() {
		return paaSServiceEvents;
	}

	public void setPaaSServiceEvents(
			Set<PaaSServiceEventRepresentation> paaSServiceEvents) {
		this.paaSServiceEvents = paaSServiceEvents;
	}

	public ProductSpecificParameters getProductSpecificParameters() {
		return productSpecificParameters;
	}

	public void setProductSpecificParameters(
			ProductSpecificParameters productSpecificParameters) {
		this.productSpecificParameters = productSpecificParameters;
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

	/**
	 * Populates a subclass of {@link GenericPaaSServiceRepresentation} with
	 * current PaaSService data.
	 * 
	 * @param obj
	 *            an instance of the class to populate.
	 * @return the populated class.
	 */
	public GenericPaaSServiceRepresentation populateSubclass(
			GenericPaaSServiceRepresentation obj) {
		obj.setCertificatePath(certificatePath);
		obj.setCreatedAt(createdAt);
		obj.setDescription(description);
		obj.setDomainName(domainName);
		obj.setErrorDescription(errorDescription);
		obj.setIaaSFlavor(iaaSFlavor);
		obj.setId(id);
		obj.setLoadBalancingInstances(loadBalancingInstances);
		obj.setName(name);
		obj.setNotificationEmail(notificationEmail);
		obj.setOperation(operation);
		obj.setPaaSServiceEvents(paaSServiceEvents);
		obj.setSecureConnectionEnabled(secureConnectionEnabled);
		obj.setSecurityGroup(securityGroup);
		obj.setStatus(status);
		obj.setUserId(userId);
		obj.setWorkgroupId(workgroupId);
		obj.setAvailabilityZone(availabilityZone);
		obj.setQoS(qos);
		obj.setPublicVisibility(publicVisibility);
		obj.setProductSpecificParameters(productSpecificParameters);
		obj.setPaasServiceEndpoints(paasServiceEndpoints);

		return obj;
	}

}
