package it.prisma.domain.dsl.monitoring.pillar.zabbix.response;

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
	"jsonrpc",
	"result",
	"error",
	"id"
})
public class JSONRPCResponse<T> {

	@JsonProperty("jsonrpc")
	private String jsonrpc;
	@JsonProperty("result")
	private T result;
	
	
	@JsonProperty("error")
	private JSONRPCError error;
	@JsonProperty("id")
	private Integer id;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("jsonrpc")
	public String getJsonrpc() {
		return jsonrpc;
	}

	@JsonProperty("jsonrpc")
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	@JsonProperty("result")
	public T getResult() {
		return result;
	}

	@JsonProperty("result")
	public void setResult(T result) {
		this.result = result;
	}
	

	@JsonProperty("error")
	public JSONRPCError getError() {
		return error;
	}

	@JsonProperty("error")
	public void setError(JSONRPCError error) {
		this.error = error;
	}

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}
	

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
	
	/**
	 * 
	 * @return true, if the response contains an error
	 */
	@JsonIgnore	
	public boolean isAPIError() {
		return error!=null;
	}

}





