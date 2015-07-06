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
"output",
"hostids",
"selectItems"
})
public class ZabbixParamTemplateRequest {

@JsonProperty("output")
private String output;
@JsonProperty("hostids")
private String hostids;
@JsonProperty("selectItems")
private String selectItems;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The output
*/
@JsonProperty("output")
public String getOutput() {
return output;
}

/**
* 
* @param output
* The output
*/
@JsonProperty("output")
public void setOutput(String output) {
this.output = output;
}

/**
* 
* @return
* The hostids
*/
@JsonProperty("hostids")
public String getHostids() {
return hostids;
}

/**
* 
* @param hostids
* The hostids
*/
@JsonProperty("hostids")
public void setHostids(String hostids) {
this.hostids = hostids;
}

/**
* 
* @return
* The selectItems
*/
@JsonProperty("selectItems")
public String getSelectItems() {
return selectItems;
}

/**
* 
* @param selectItems
* The selectItems
*/
@JsonProperty("selectItems")
public void setSelectItems(String selectItems) {
this.selectItems = selectItems;
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