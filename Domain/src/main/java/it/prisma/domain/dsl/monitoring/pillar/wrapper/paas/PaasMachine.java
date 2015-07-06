package it.prisma.domain.dsl.monitoring.pillar.wrapper.paas;

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
@JsonPropertyOrder({
	"machineName",
	"ip",
	"serviceCategory",
	"serviceId",
	"services"
})
public class PaasMachine {

	@JsonProperty("machineName")
	private String machineName;
	@JsonProperty("ip")
	private String ip;
	@JsonProperty("serviceCategory")
	private String serviceCategory;
	@JsonProperty("serviceId")
	private String serviceId;
	@JsonProperty("services")
	private List<Service> services = new ArrayList<Service>();
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The machineName
	 */
	@JsonProperty("machineName")
	public String getMachineName() {
		return machineName;
	}

	/**
	 * 
	 * @param machineName
	 * The machineName
	 */
	@JsonProperty("machineName")
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	/**
	 * 
	 * @return
	 * The serviceCategory
	 */
	@JsonProperty("serviceCategory")
	public String getServiceCategory() {
		return serviceCategory;
	}

	/**
	 * 
	 * @param serviceCategories
	 * The serviceCategories
	 */
	@JsonProperty("serviceCategory")
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	
	/**
	 * 
	 * @return
	 * The serviceId
	 */
	@JsonProperty("serviceId")
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * @param serviceId
	 * The serviceId
	 */
	@JsonProperty("serviceId")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * 
	 * @return
	 * The ip
	 */
	@JsonProperty("ip")
	public String getIp() {
		return ip;
	}

	/**
	 * 
	 * @param ip
	 * The ip
	 */
	@JsonProperty("ip")
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 
	 * @return
	 * The services
	 */
	@JsonProperty("services")
	public List<Service> getServices() {
		return services;
	}

	/**
	 * 
	 * @param services
	 * The services
	 */
	@JsonProperty("services")
	public void setServices(List<Service> services) {
		this.services = services;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

//	@JsonAnyGetter
//	public Map<String, Object> getAdditionalProperties() {
//		return this.additionalProperties;
//	}
//
//	@JsonAnySetter
//	public void setAdditionalProperty(String name, Object value) {
//		this.additionalProperties.put(name, value);
//	}

}