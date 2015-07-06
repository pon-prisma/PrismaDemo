package it.prisma.domain.dsl.monitoring.pillar.zabbix.request;


import java.util.ArrayList;
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
	"hostids",
	"output",
	"expandDescription",
	"expandExpression",
	"itemids", 
	"only_true", 
	"group"
})
public class ZabbixParamTriggerRequest {

	@JsonProperty("hostids")
	private String hostids;
	@JsonProperty("output")
	private String output;
	@JsonProperty("expandDescription")
	private boolean expandDescription;
	@JsonProperty("expandExpression")
	private boolean expandExpression;
	@JsonProperty("only_true")
	private Boolean only_true;
	@JsonProperty("itemids")
	private String group;
	@JsonProperty("group")
	private ArrayList<String> itemids = new ArrayList<String>();
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("hostids")
	public String getHostids() {
		return hostids;
	}

	@JsonProperty("hostids")
	public void setHostids(String hostids) {
		this.hostids = hostids;
	}
	
	@JsonProperty("output")
	public String getOutput() {
		return output;
	}

	@JsonProperty("output")
	public void setOutput(String output) {
		this.output = output;
	}
	
	@JsonProperty("expandExpression")
	public boolean getexpandExpression() {
		return expandExpression;
	}

	@JsonProperty("expandExpression")
	public void setExpandExpression(boolean expandExpression) {
		this.expandExpression = expandExpression;
	}
	
	@JsonProperty("only_true")
	public Boolean getOnly_true() {
		return only_true;
	}

	@JsonProperty("only_true")
	public void setOnly_true(Boolean only_true) {
		this.only_true = only_true;
	}
	
	@JsonProperty("expandDescription")
	public Boolean getExpandDescription() {
		return expandDescription;
	}

	@JsonProperty("expandDescription")
	public void setExpandDescription(Boolean expandDescription) {
		this.expandDescription = expandDescription;
	}

	@JsonProperty("itemids")
	public ArrayList<String> getItemids() {
		return itemids;
	}

	@JsonProperty("itemids")
	public void setItemids(ArrayList<String> itemids) {
		this.itemids = itemids;
	}
	
	@JsonProperty("group")
	public String getGroup() {
		return group;
	}

	@JsonProperty("group")
	public void setGroup(String group) {
		this.group = group;
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