package it.prisma.domain.dsl.monitoring.pillar.zabbix.request;

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
	"hosts",
	"itemid",
	"status"
})
public class ZabbixParamUpdate {

	@JsonProperty("hosts")
	private List<HostIDMassUpdate> hosts = new ArrayList<HostIDMassUpdate>();
	@JsonProperty("itemid")
	private String itemid;
	@JsonProperty("status")
	private Integer status;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The hosts
	 */
	@JsonProperty("hosts")
	public List<HostIDMassUpdate> getHosts() {
		return hosts;
	}

	/**
	 * 
	 * @param hosts
	 * The hosts
	 */
	@JsonProperty("hosts")
	public void setHosts(List<HostIDMassUpdate> hosts) {
		this.hosts = hosts;
	}

	/**
	 * 
	 * @return
	 * The itemid
	 */
	@JsonProperty("itemid")
	public String getItemid() {
		return itemid;
	}

	/**
	 * 
	 * @param itemid
	 * The itemid
	 */
	@JsonProperty("itemid")
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	/**
	 * 
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public Integer getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(Integer status) {
		this.status = status;
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
		return new HashCodeBuilder().append(hosts).append(itemid).append(status).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof ZabbixParamUpdate) == false) {
			return false;
		}
		ZabbixParamUpdate rhs = ((ZabbixParamUpdate) other);
		return new EqualsBuilder().append(hosts, rhs.hosts).append(itemid, rhs.itemid).append(status, rhs.status).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}


