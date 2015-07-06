package it.prisma.domain.dsl.monitoring.businesslayer.paas.response;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"hostids"
	,"zabbixhostids",
	"vmuuid",
	"vmname", 
	"error"
})
public class MonitoringVMCredentialsResponse {

	@JsonProperty("hostids")
	private ArrayList<String> hostids = new ArrayList<String>();
	@JsonProperty("vmuuid")
	private List<String> vmuuid = new ArrayList<String>();
	@JsonProperty("vmname")
	private String vmname = new String();
	@JsonProperty("error")
	private String error = new String();
	@JsonIgnoreProperties("zabbixhostids")
	private String zabbixhostids = new String();
	//@JsonIgnore
	//private Map<String, Object> additionalProperties = new HashMap<String, Object>();


	@JsonProperty("hostids")
	public ArrayList<String> getHostids() {
		return hostids;
	}

	@JsonProperty("hostids")
	public void setHostids(ArrayList<String> hostids) {
		this.hostids = hostids;
	}

	@JsonProperty("vmuuid")
	public List<String> getVmuuid() {
		return vmuuid;
	}

	@JsonProperty("vmuuid")
	public void setVmuuid(List<String> vmuuid) {
		this.vmuuid = vmuuid;
	}

	@JsonProperty("vmname")
	public String getVmname() {
		return vmname;
	}

	@JsonProperty("vmname")
	public void setVmname(String vmname) {
		this.vmname = vmname;
	}

	@JsonProperty("error")
	public String getError() {
		return error;
	}

	@JsonProperty("error")
	public void setError(String error) {
		this.error = error;
	}

	//@JsonAnyGetter
	//public Map<String, Object> getAdditionalProperties() {
	//return this.additionalProperties;
	//}
	//
	//@JsonAnySetter
	//public void setAdditionalProperty(String name, Object value) {
	//this.additionalProperties.put(name, value);
	//}

}
