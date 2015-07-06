package it.prisma.businesslayer.bizws.testing.monitoring.dsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "adapterType", "vmuuid", "vmip", "group",
		"serviceCategory", "services" })
public class TestMonitoringAddServiceRequest {

	@JsonProperty("adapterType")
	private String adapterType;
	@JsonProperty("vmuuid")
	private String vmuuid;
	@JsonProperty("vmip")
	private String vmip;
	@JsonProperty("group")
	private String group;
	@JsonProperty("serviceId")
	private String serviceId;
	@JsonProperty("serviceCategory")
	private String serviceCategory;
	@JsonProperty("services")
	private List<String> services = new ArrayList<String>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The adapterType
	 */
	@JsonProperty("adapterType")
	public String getAdapterType() {
		return adapterType;
	}

	/**
	 * 
	 * @param adapterType
	 *            The adapterType
	 */
	@JsonProperty("adapterType")
	public void setAdapterType(String adapterType) {
		this.adapterType = adapterType;
	}

	/**
	 * 
	 * @return The vmuuid
	 */
	@JsonProperty("vmuuid")
	public String getVmuuid() {
		return vmuuid;
	}

	/**
	 * 
	 * @param vmuuid
	 *            The vmuuid
	 */
	@JsonProperty("vmuuid")
	public void setVmuuid(String vmuuid) {
		this.vmuuid = vmuuid;
	}

	/**
	 * 
	 * @return The vmip
	 */
	@JsonProperty("vmip")
	public String getVmip() {
		return vmip;
	}

	/**
	 * 
	 * @param vmip
	 *            The vmip
	 */
	@JsonProperty("vmip")
	public void setVmip(String vmip) {
		this.vmip = vmip;
	}

	/**
	 * 
	 * @return The group
	 */
	@JsonProperty("group")
	public String getGroup() {
		return group;
	}

	/**
	 * 
	 * @param group
	 *            The group
	 */
	@JsonProperty("group")
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * 
	 * @return The serviceId
	 */
	@JsonProperty("serviceId")
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * @param serviceId
	 *            The serviceId
	 */
	@JsonProperty("serviceId")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	/**
	 * 
	 * @return The serviceCategory
	 */
	@JsonProperty("serviceCategory")
	public String getServiceCategory() {
		return serviceCategory;
	}

	/**
	 * 
	 * @param serviceCategory
	 *            The serviceCategory
	 */
	@JsonProperty("serviceCategory")
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	/**
	 * 
	 * @return The services
	 */
	@JsonProperty("services")
	public List<String> getServices() {
		return services;
	}

	/**
	 * 
	 * @param services
	 *            The services
	 */
	@JsonProperty("services")
	public void setServices(List<String> services) {
		this.services = services;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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