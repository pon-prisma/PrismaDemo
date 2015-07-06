package it.prisma.domain.dsl.monitoring.businesslayer.paas.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "uuid", "ip", "hostGroup", "serviceCategory", "serviceId", "atomicServices" })
public class HostMonitoringRequest {

	@JsonProperty("uuid")
	public String uuid;
	@JsonProperty("ip")
	public String ip;
	@JsonProperty("hostGroup")
	public String hostGroup;
	@JsonProperty("serviceCategory")
	public String serviceCategory;
	@JsonProperty("serviceId")
	public String serviceId;
	@JsonProperty("atomicServices")
	public List<String> atomicServices;
	
	@JsonProperty("uuid")
	public String getUuid() {
		return uuid;
	}	
	
	@JsonProperty("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@JsonProperty("ip")
	public String getIp() {
		return ip;
	}
	
	@JsonProperty("ip")
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@JsonProperty("hostGroup")
	public String getHostGroup() {
		return hostGroup;
	}
	
	@JsonProperty("hostGroup")
	public void setHostGroup(String hostGroup) {
		this.hostGroup = hostGroup;
	}
	
	@JsonProperty("serviceCategory")
	public String getServiceCategory() {
		return serviceCategory;
	}
	
	@JsonProperty("serviceCategory")
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	
	@JsonProperty("serviceId")
	public String getServiceId() {
		return serviceId;
	}
	
	@JsonProperty("serviceId")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@JsonProperty("atomicServices")
	public List<String> getAtomicServices() {
		return atomicServices;
	}
	
	@JsonProperty("atomicServices")
	public void setAtomicServices(List<String> atomicServices) {
		this.atomicServices = atomicServices;
	}
	
}
