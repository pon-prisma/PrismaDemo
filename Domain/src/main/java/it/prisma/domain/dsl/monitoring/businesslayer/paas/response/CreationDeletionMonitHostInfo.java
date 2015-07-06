package it.prisma.domain.dsl.monitoring.businesslayer.paas.response;

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
@JsonPropertyOrder({
	"status",
	"hostID",
	"hostname",
	"serviceID",
	"hostCategory",
	"hostIP",
	"atomicServices",
	"error"
})
public class CreationDeletionMonitHostInfo {

	@JsonProperty("status")
	private String status;
	@JsonProperty("hostID")
	private String hostID;
	@JsonProperty("hostname")
	private String hostname;
	@JsonProperty("serviceID")
	private String serviceID;
	@JsonProperty("hostCategory")
	private String hostCategory;
	@JsonProperty("hostIP")
	private String hostIP;
	@JsonProperty("atomicServices")
	private List<String> atomicServices = new ArrayList<String>();
	@JsonProperty("error")
	private Boolean error;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return
	 * The hostID
	 */
	@JsonProperty("hostID")
	public String getHostID() {
		return hostID;
	}

	/**
	 * 
	 * @param hostID
	 * The hostID
	 */
	@JsonProperty("hostID")
	public void setHostID(String hostID) {
		this.hostID = hostID;
	}

	/**
	 * 
	 * @return
	 * The hostname
	 */
	@JsonProperty("hostname")
	public String getHostname() {
		return hostname;
	}

	/**
	 * 
	 * @param hostname
	 * The hostname
	 */
	@JsonProperty("hostname")
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * 
	 * @return
	 * The serviceID
	 */
	@JsonProperty("serviceID")
	public String getServiceID() {
		return serviceID;
	}

	/**
	 * 
	 * @param serviceID
	 * The serviceID
	 */
	@JsonProperty("serviceID")
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	/**
	 * 
	 * @return
	 * The hostCategory
	 */
	@JsonProperty("hostCategory")
	public String getHostCategory() {
		return hostCategory;
	}

	/**
	 * 
	 * @param hostCategory
	 * The hostCategory
	 */
	@JsonProperty("hostCategory")
	public void setHostCategory(String hostCategory) {
		this.hostCategory = hostCategory;
	}

	/**
	 * 
	 * @return
	 * The hostIP
	 */
	@JsonProperty("hostIP")
	public String getHostIP() {
		return hostIP;
	}

	/**
	 * 
	 * @param hostIP
	 * The hostIP
	 */
	@JsonProperty("hostIP")
	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	/**
	 * 
	 * @return
	 * The atomicServices
	 */
	@JsonProperty("atomicServices")
	public List<String> getAtomicServices() {
		return atomicServices;
	}

	/**
	 * 
	 * @param atomicServices
	 * The atomicServices
	 */
	@JsonProperty("atomicServices")
	public void setAtomicServices(List<String> atomicServices) {
		this.atomicServices = atomicServices;
	}

	/**
	 * 
	 * @return
	 * The error
	 */
	@JsonProperty("error")
	public Boolean getError() {
		return error;
	}

	/**
	 * 
	 * @param error
	 * The error
	 */
	@JsonProperty("error")
	public void setError(Boolean error) {
		this.error = error;
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(status).append(hostID).append(hostname).append(serviceID).append(hostCategory).append(hostIP).append(atomicServices).append(error).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof CreationDeletionMonitHostInfo) == false) {
			return false;
		}
		CreationDeletionMonitHostInfo rhs = ((CreationDeletionMonitHostInfo) other);
		return new EqualsBuilder().append(status, rhs.status).append(hostID, rhs.hostID).append(hostname, rhs.hostname).append(serviceID, rhs.serviceID).append(hostCategory, rhs.hostCategory).append(hostIP, rhs.hostIP).append(atomicServices, rhs.atomicServices).append(error, rhs.error).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}
