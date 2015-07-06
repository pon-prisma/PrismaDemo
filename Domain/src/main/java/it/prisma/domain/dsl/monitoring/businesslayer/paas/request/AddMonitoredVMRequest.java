package it.prisma.domain.dsl.monitoring.businesslayer.paas.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "vmUUID", "vmIP", "vmName", "services" })
public class AddMonitoredVMRequest {

	@JsonProperty("vmUUID")
	private String vmUUID;
	@JsonProperty("vmIP")
	private String vmIP;
	@JsonProperty("vmName")
	private String vmName;
	@JsonProperty("services")
	private List<Service> services = new ArrayList<Service>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("vmUUID")
	public String getVmUUID() {
		return vmUUID;
	}

	@JsonProperty("vmUUID")
	public void setVmUUID(String vmUUID) {
		this.vmUUID = vmUUID;
	}

	@JsonProperty("vmIP")
	public String getVmIP() {
		return vmIP;
	}

	@JsonProperty("vmIP")
	public void setVmIP(String vmIP) {
		this.vmIP = vmIP;
	}

	@JsonProperty("vmName")
	public String getVmName() {
		return vmName;
	}

	@JsonProperty("vmName")
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	@JsonProperty("services")
	public List<Service> getServices() {
		return services;
	}

	@JsonProperty("services")
	public void setServices(List<Service> services) {
		this.services = services;
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
