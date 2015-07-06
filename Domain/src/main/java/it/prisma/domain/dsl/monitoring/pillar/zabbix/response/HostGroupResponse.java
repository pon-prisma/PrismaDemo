package it.prisma.domain.dsl.monitoring.pillar.zabbix.response;


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
	"groupids"
	})
	public class HostGroupResponse {

	@JsonProperty("groupids")
	private List<String> groupids = new ArrayList<String>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	* 
	* @return
	* The groupids
	*/
	@JsonProperty("groupids")
	public List<String> getGroupids() {
	return groupids;
	}

	/**
	* 
	* @param groupids
	* The groupids
	*/
	@JsonProperty("groupids")
	public void setGroupids(List<String> groupids) {
	this.groupids = groupids;
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
	return new HashCodeBuilder().append(groupids).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
	if (other == this) {
	return true;
	}
	if ((other instanceof HostGroupResponse) == false) {
	return false;
	}
	HostGroupResponse rhs = ((HostGroupResponse) other);
	return new EqualsBuilder().append(groupids, rhs.groupids).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

	}
