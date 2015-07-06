package it.prisma.domain.dsl.monitoring.pillar.zabbix.request;

	import java.util.HashMap;
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
	"hostid"
	})
	public class HostIDMassUpdate {

	@JsonProperty("hostid")
	private String hostid;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	* 
	* @return
	* The hostid
	*/
	@JsonProperty("hostid")
	public String getHostid() {
	return hostid;
	}

	/**
	* 
	* @param hostid
	* The hostid
	*/
	@JsonProperty("hostid")
	public void setHostid(String hostid) {
	this.hostid = hostid;
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
	return new HashCodeBuilder().append(hostid).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
	if (other == this) {
	return true;
	}
	if ((other instanceof HostIDMassUpdate) == false) {
	return false;
	}
	HostIDMassUpdate rhs = ((HostIDMassUpdate) other);
	return new EqualsBuilder().append(hostid, rhs.hostid).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

	}
