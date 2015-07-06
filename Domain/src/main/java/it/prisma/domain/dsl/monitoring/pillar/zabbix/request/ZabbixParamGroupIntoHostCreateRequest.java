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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"groupid"
})
public class ZabbixParamGroupIntoHostCreateRequest {

	@JsonProperty("groupid")
	private String groupid; 
//	private ArrayList<String> groupid;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("groupid")
	public String getGroupid() {
		return (String) groupid;
	}

	@JsonProperty("groupid")
	public void setGroupid (String groupid) {
		this.groupid = groupid;
	}
	
//	@JsonProperty("groupid")
//	public ArrayList<String> getGroupid() {
//		return (ArrayList<String>) groupid;
//	}
//
//	@JsonProperty("groupid")
//	public void setGroupid(ArrayList<String> groupid) {
//		this.groupid = groupid;
//	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}