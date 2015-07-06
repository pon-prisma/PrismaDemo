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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"jsonrpc",
	"method",
	"params",
	"auth",
	"id"
})
public class JsonRpcDeleteHostRequest {

	@JsonProperty("jsonrpc")
	private String jsonrpc;
	@JsonProperty("method")
	private String method;
	@JsonProperty("params")
	private List<String> params = new ArrayList<String>();
	@JsonProperty("auth")
	private String auth;
	@JsonProperty("id")
	private Integer id;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The jsonrpc
	 */
	@JsonProperty("jsonrpc")
	public String getJsonrpc() {
		return jsonrpc;
	}

	/**
	 * 
	 * @param jsonrpc
	 * The jsonrpc
	 */
	@JsonProperty("jsonrpc")
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	/**
	 * 
	 * @return
	 * The method
	 */
	@JsonProperty("method")
	public String getMethod() {
		return method;
	}

	/**
	 * 
	 * @param method
	 * The method
	 */
	@JsonProperty("method")
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 
	 * @return
	 * The params
	 */
	@JsonProperty("params")
	public List<String> getParams() {
		return params;
	}

	/**
	 * 
	 * @param params
	 * The params
	 */
	@JsonProperty("params")
	public void setParams(List<String> params) {
		this.params = params;
	}

	/**
	 * 
	 * @return
	 * The auth
	 */
	@JsonProperty("auth")
	public String getAuth() {
		return auth;
	}

	/**
	 * 
	 * @param auth
	 * The auth
	 */
	@JsonProperty("auth")
	public void setAuth(String auth) {
		this.auth = auth;
	}

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
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

}
