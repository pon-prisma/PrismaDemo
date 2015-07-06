package it.prisma.domain.dsl.cloudify.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "icon", "url", "Cloud Public IP", "Cloud Image ID",
		"Cloud Private IP", "GSC PID", "Cloud Hardware ID", "Instance ID",
		"Machine ID", "Working Directory", "USM.UI", "MySQL IP", "MySQL Port" })
public class ProcessDetails implements Serializable {

	private static final long serialVersionUID = 2706906826546079078L;

	@JsonProperty("icon")
	private String icon;
	@JsonProperty("url")
	private Object url;
	@JsonProperty("Cloud Public IP")
	private String cloudPublicIP;
	@JsonProperty("Cloud Image ID")
	private String cloudImageID;
	@JsonProperty("Cloud Private IP")
	private String cloudPrivateIP;
	@JsonProperty("GSC PID")
	private Integer gSCPID;
	@JsonProperty("Cloud Hardware ID")
	private String cloudHardwareID;
	@JsonProperty("Instance ID")
	private Integer instanceID;
	@JsonProperty("Machine ID")
	private String machineID;
	@JsonProperty("Working Directory")
	private String workingDirectory;
	@JsonProperty("USM.UI")
	private USMUI uSMUI;
	@JsonProperty("MySQL IP")
	private String mySQLIP;
	@JsonProperty("MySQL Port")
	private Integer mySQLPort;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("icon")
	public String getIcon() {
		return icon;
	}

	@JsonProperty("icon")
	public void setIcon(String icon) {
		this.icon = icon;
	}

	@JsonProperty("url")
	public Object getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(Object url) {
		this.url = url;
	}

	@JsonProperty("Cloud Public IP")
	public String getCloudPublicIP() {
		return cloudPublicIP;
	}

	@JsonProperty("Cloud Public IP")
	public void setCloudPublicIP(String cloudPublicIP) {
		this.cloudPublicIP = cloudPublicIP;
	}

	@JsonProperty("Cloud Image ID")
	public String getCloudImageID() {
		return cloudImageID;
	}

	@JsonProperty("Cloud Image ID")
	public void setCloudImageID(String cloudImageID) {
		this.cloudImageID = cloudImageID;
	}

	@JsonProperty("Cloud Private IP")
	public String getCloudPrivateIP() {
		return cloudPrivateIP;
	}

	@JsonProperty("Cloud Private IP")
	public void setCloudPrivateIP(String cloudPrivateIP) {
		this.cloudPrivateIP = cloudPrivateIP;
	}

	@JsonProperty("GSC PID")
	public Integer getGSCPID() {
		return gSCPID;
	}

	@JsonProperty("GSC PID")
	public void setGSCPID(Integer gSCPID) {
		this.gSCPID = gSCPID;
	}

	@JsonProperty("Cloud Hardware ID")
	public String getCloudHardwareID() {
		return cloudHardwareID;
	}

	@JsonProperty("Cloud Hardware ID")
	public void setCloudHardwareID(String cloudHardwareID) {
		this.cloudHardwareID = cloudHardwareID;
	}

	@JsonProperty("Instance ID")
	public Integer getInstanceID() {
		return instanceID;
	}

	@JsonProperty("Instance ID")
	public void setInstanceID(Integer instanceID) {
		this.instanceID = instanceID;
	}

	@JsonProperty("Machine ID")
	public String getMachineID() {
		return machineID;
	}

	@JsonProperty("Machine ID")
	public void setMachineID(String machineID) {
		this.machineID = machineID;
	}

	@JsonProperty("Working Directory")
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	@JsonProperty("Working Directory")
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	@JsonProperty("USM.UI")
	public USMUI getUSMUI() {
		return uSMUI;
	}

	@JsonProperty("USM.UI")
	public void setUSMUI(USMUI uSMUI) {
		this.uSMUI = uSMUI;
	}

	@JsonProperty("MySQL IP")
	public String getMySQLIP() {
		return mySQLIP;
	}

	@JsonProperty("MySQL IP")
	public void setMySQLIP(String mySQLIP) {
		this.mySQLIP = mySQLIP;
	}

	@JsonProperty("MySQL Port")
	public Integer getMySQLPort() {
		return mySQLPort;
	}

	@JsonProperty("MySQL Port")
	public void setMySQLPort(Integer mySQLPort) {
		this.mySQLPort = mySQLPort;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
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
