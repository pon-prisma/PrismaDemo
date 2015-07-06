package it.prisma.domain.dsl.monitoring.pillar.zabbix.response;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"code",
"message",
"data"
})
public class JSONRPCError {

@JsonProperty("code")
private Integer code;
@JsonProperty("message")
private String message;
@JsonProperty("data")
private String data;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("code")
public Integer getCode() {
return code;
}

@JsonProperty("code")
public void setCode(Integer code) {
this.code = code;
}

@JsonProperty("message")
public String getMessage() {
return message;
}

@JsonProperty("message")
public void setMessage(String message) {
this.message = message;
}

@JsonProperty("data")
public String getData() {
return data;
}

@JsonProperty("data")
public void setData(String data) {
this.data = data;
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