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
	"maintenances",
	"hostid",
	"proxy_hostid",
	"host",
	"status",
	"disable_until",
	"error",
	"available",
	"errors_from",
	"lastaccess",
	"ipmi_authtype",
	"ipmi_privilege",
	"ipmi_username",
	"ipmi_password",
	"ipmi_disable_until",
	"ipmi_available",
	"snmp_disable_until",
	"snmp_available",
	"maintenanceid",
	"maintenance_status",
	"maintenance_type",
	"maintenance_from",
	"ipmi_errors_from",
	"snmp_errors_from",
	"ipmi_error",
	"snmp_error",
	"jmx_disable_until",
	"jmx_available",
	"jmx_errors_from",
	"jmx_error",
	"name",
	"flags",
	"templateid",
	"inventory" 

})
public class ZabbixMonitoredHostsResponse {

	@JsonProperty("maintenances")
	private List<Object> maintenances = new ArrayList<Object>();
	@JsonProperty("hostid")
	private String hostid;
	@JsonProperty("proxy_hostid")
	private String proxyHostid;
	@JsonProperty("host")
	private String host;
	@JsonProperty("status")
	private String status;
	@JsonProperty("disable_until")
	private String disableUntil;
	@JsonProperty("error")
	private String error;
	@JsonProperty("available")
	private String available;
	@JsonProperty("errors_from")
	private String errorsFrom;
	@JsonProperty("lastaccess")
	private String lastaccess;
	@JsonProperty("ipmi_authtype")
	private String ipmiAuthtype;
	@JsonProperty("ipmi_privilege")
	private String ipmiPrivilege;
	@JsonProperty("ipmi_username")
	private String ipmiUsername;
	@JsonProperty("ipmi_password")
	private String ipmiPassword;
	@JsonProperty("ipmi_disable_until")
	private String ipmiDisableUntil;
	@JsonProperty("ipmi_available")
	private String ipmiAvailable;
	@JsonProperty("snmp_disable_until")
	private String snmpDisableUntil;
	@JsonProperty("snmp_available")
	private String snmpAvailable;
	@JsonProperty("maintenanceid")
	private String maintenanceid;
	@JsonProperty("maintenance_status")
	private String maintenanceStatus;
	@JsonProperty("maintenance_type")
	private String maintenanceType;
	@JsonProperty("maintenance_from")
	private String maintenanceFrom;
	@JsonProperty("ipmi_errors_from")
	private String ipmiErrorsFrom;
	@JsonProperty("snmp_errors_from")
	private String snmpErrorsFrom;
	@JsonProperty("ipmi_error")
	private String ipmiError;
	@JsonProperty("snmp_error")
	private String snmpError;
	@JsonProperty("jmx_disable_until")
	private String jmxDisableUntil;
	@JsonProperty("jmx_available")
	private String jmxAvailable;
	@JsonProperty("jmx_errors_from")
	private String jmxErrorsFrom;
	@JsonProperty("jmx_error")
	private String jmxError;
	@JsonProperty("name")
	private String name;
	@JsonProperty("flags")
	private String flags;
	@JsonProperty("templateid")
	private String templateid;
	@JsonProperty("inventory")
	private Inventory inventory;

	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	

	/**
	 * 
	 * @return
	 * The maintenances
	 */
	@JsonProperty("maintenances")
	public List<Object> getMaintenances() {
		return maintenances;
	}

	/**
	 * 
	 * @param maintenances
	 * The maintenances
	 */
	@JsonProperty("maintenances")
	public void setMaintenances(List<Object> maintenances) {
		this.maintenances = maintenances;
	}

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

	/**
	 * 
	 * @return
	 * The proxyHostid
	 */
	@JsonProperty("proxy_hostid")
	public String getProxyHostid() {
		return proxyHostid;
	}

	/**
	 * 
	 * @param proxyHostid
	 * The proxy_hostid
	 */
	@JsonProperty("proxy_hostid")
	public void setProxyHostid(String proxyHostid) {
		this.proxyHostid = proxyHostid;
	}

	/**
	 * 
	 * @return
	 * The host
	 */
	@JsonProperty("host")
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * @param host
	 * The host
	 */
	@JsonProperty("host")
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return
	 * The disableUntil
	 */
	@JsonProperty("disable_until")
	public String getDisableUntil() {
		return disableUntil;
	}

	/**
	 * 
	 * @param disableUntil
	 * The disable_until
	 */
	@JsonProperty("disable_until")
	public void setDisableUntil(String disableUntil) {
		this.disableUntil = disableUntil;
	}

	/**
	 * 
	 * @return
	 * The error
	 */
	@JsonProperty("error")
	public String getError() {
		return error;
	}

	/**
	 * 
	 * @param error
	 * The error
	 */
	@JsonProperty("error")
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * 
	 * @return
	 * The available
	 */
	@JsonProperty("available")
	public String getAvailable() {
		return available;
	}

	/**
	 * 
	 * @param available
	 * The available
	 */
	@JsonProperty("available")
	public void setAvailable(String available) {
		this.available = available;
	}

	/**
	 * 
	 * @return
	 * The errorsFrom
	 */
	@JsonProperty("errors_from")
	public String getErrorsFrom() {
		return errorsFrom;
	}

	/**
	 * 
	 * @param errorsFrom
	 * The errors_from
	 */
	@JsonProperty("errors_from")
	public void setErrorsFrom(String errorsFrom) {
		this.errorsFrom = errorsFrom;
	}

	/**
	 * 
	 * @return
	 * The lastaccess
	 */
	@JsonProperty("lastaccess")
	public String getLastaccess() {
		return lastaccess;
	}

	/**
	 * 
	 * @param lastaccess
	 * The lastaccess
	 */
	@JsonProperty("lastaccess")
	public void setLastaccess(String lastaccess) {
		this.lastaccess = lastaccess;
	}

	/**
	 * 
	 * @return
	 * The ipmiAuthtype
	 */
	@JsonProperty("ipmi_authtype")
	public String getIpmiAuthtype() {
		return ipmiAuthtype;
	}

	/**
	 * 
	 * @param ipmiAuthtype
	 * The ipmi_authtype
	 */
	@JsonProperty("ipmi_authtype")
	public void setIpmiAuthtype(String ipmiAuthtype) {
		this.ipmiAuthtype = ipmiAuthtype;
	}

	/**
	 * 
	 * @return
	 * The ipmiPrivilege
	 */
	@JsonProperty("ipmi_privilege")
	public String getIpmiPrivilege() {
		return ipmiPrivilege;
	}

	/**
	 * 
	 * @param ipmiPrivilege
	 * The ipmi_privilege
	 */
	@JsonProperty("ipmi_privilege")
	public void setIpmiPrivilege(String ipmiPrivilege) {
		this.ipmiPrivilege = ipmiPrivilege;
	}

	/**
	 * 
	 * @return
	 * The ipmiUsername
	 */
	@JsonProperty("ipmi_username")
	public String getIpmiUsername() {
		return ipmiUsername;
	}

	/**
	 * 
	 * @param ipmiUsername
	 * The ipmi_username
	 */
	@JsonProperty("ipmi_username")
	public void setIpmiUsername(String ipmiUsername) {
		this.ipmiUsername = ipmiUsername;
	}

	/**
	 * 
	 * @return
	 * The ipmiPassword
	 */
	@JsonProperty("ipmi_password")
	public String getIpmiPassword() {
		return ipmiPassword;
	}

	/**
	 * 
	 * @param ipmiPassword
	 * The ipmi_password
	 */
	@JsonProperty("ipmi_password")
	public void setIpmiPassword(String ipmiPassword) {
		this.ipmiPassword = ipmiPassword;
	}

	/**
	 * 
	 * @return
	 * The ipmiDisableUntil
	 */
	@JsonProperty("ipmi_disable_until")
	public String getIpmiDisableUntil() {
		return ipmiDisableUntil;
	}

	/**
	 * 
	 * @param ipmiDisableUntil
	 * The ipmi_disable_until
	 */
	@JsonProperty("ipmi_disable_until")
	public void setIpmiDisableUntil(String ipmiDisableUntil) {
		this.ipmiDisableUntil = ipmiDisableUntil;
	}

	/**
	 * 
	 * @return
	 * The ipmiAvailable
	 */
	@JsonProperty("ipmi_available")
	public String getIpmiAvailable() {
		return ipmiAvailable;
	}

	/**
	 * 
	 * @param ipmiAvailable
	 * The ipmi_available
	 */
	@JsonProperty("ipmi_available")
	public void setIpmiAvailable(String ipmiAvailable) {
		this.ipmiAvailable = ipmiAvailable;
	}

	/**
	 * 
	 * @return
	 * The snmpDisableUntil
	 */
	@JsonProperty("snmp_disable_until")
	public String getSnmpDisableUntil() {
		return snmpDisableUntil;
	}

	/**
	 * 
	 * @param snmpDisableUntil
	 * The snmp_disable_until
	 */
	@JsonProperty("snmp_disable_until")
	public void setSnmpDisableUntil(String snmpDisableUntil) {
		this.snmpDisableUntil = snmpDisableUntil;
	}

	/**
	 * 
	 * @return
	 * The snmpAvailable
	 */
	@JsonProperty("snmp_available")
	public String getSnmpAvailable() {
		return snmpAvailable;
	}

	/**
	 * 
	 * @param snmpAvailable
	 * The snmp_available
	 */
	@JsonProperty("snmp_available")
	public void setSnmpAvailable(String snmpAvailable) {
		this.snmpAvailable = snmpAvailable;
	}

	/**
	 * 
	 * @return
	 * The maintenanceid
	 */
	@JsonProperty("maintenanceid")
	public String getMaintenanceid() {
		return maintenanceid;
	}

	/**
	 * 
	 * @param maintenanceid
	 * The maintenanceid
	 */
	@JsonProperty("maintenanceid")
	public void setMaintenanceid(String maintenanceid) {
		this.maintenanceid = maintenanceid;
	}

	/**
	 * 
	 * @return
	 * The maintenanceStatus
	 */
	@JsonProperty("maintenance_status")
	public String getMaintenanceStatus() {
		return maintenanceStatus;
	}

	/**
	 * 
	 * @param maintenanceStatus
	 * The maintenance_status
	 */
	@JsonProperty("maintenance_status")
	public void setMaintenanceStatus(String maintenanceStatus) {
		this.maintenanceStatus = maintenanceStatus;
	}

	/**
	 * 
	 * @return
	 * The maintenanceType
	 */
	@JsonProperty("maintenance_type")
	public String getMaintenanceType() {
		return maintenanceType;
	}

	/**
	 * 
	 * @param maintenanceType
	 * The maintenance_type
	 */
	@JsonProperty("maintenance_type")
	public void setMaintenanceType(String maintenanceType) {
		this.maintenanceType = maintenanceType;
	}

	/**
	 * 
	 * @return
	 * The maintenanceFrom
	 */
	@JsonProperty("maintenance_from")
	public String getMaintenanceFrom() {
		return maintenanceFrom;
	}

	/**
	 * 
	 * @param maintenanceFrom
	 * The maintenance_from
	 */
	@JsonProperty("maintenance_from")
	public void setMaintenanceFrom(String maintenanceFrom) {
		this.maintenanceFrom = maintenanceFrom;
	}

	/**
	 * 
	 * @return
	 * The ipmiErrorsFrom
	 */
	@JsonProperty("ipmi_errors_from")
	public String getIpmiErrorsFrom() {
		return ipmiErrorsFrom;
	}

	/**
	 * 
	 * @param ipmiErrorsFrom
	 * The ipmi_errors_from
	 */
	@JsonProperty("ipmi_errors_from")
	public void setIpmiErrorsFrom(String ipmiErrorsFrom) {
		this.ipmiErrorsFrom = ipmiErrorsFrom;
	}

	/**
	 * 
	 * @return
	 * The snmpErrorsFrom
	 */
	@JsonProperty("snmp_errors_from")
	public String getSnmpErrorsFrom() {
		return snmpErrorsFrom;
	}

	/**
	 * 
	 * @param snmpErrorsFrom
	 * The snmp_errors_from
	 */
	@JsonProperty("snmp_errors_from")
	public void setSnmpErrorsFrom(String snmpErrorsFrom) {
		this.snmpErrorsFrom = snmpErrorsFrom;
	}

	/**
	 * 
	 * @return
	 * The ipmiError
	 */
	@JsonProperty("ipmi_error")
	public String getIpmiError() {
		return ipmiError;
	}

	/**
	 * 
	 * @param ipmiError
	 * The ipmi_error
	 */
	@JsonProperty("ipmi_error")
	public void setIpmiError(String ipmiError) {
		this.ipmiError = ipmiError;
	}

	/**
	 * 
	 * @return
	 * The snmpError
	 */
	@JsonProperty("snmp_error")
	public String getSnmpError() {
		return snmpError;
	}

	/**
	 * 
	 * @param snmpError
	 * The snmp_error
	 */
	@JsonProperty("snmp_error")
	public void setSnmpError(String snmpError) {
		this.snmpError = snmpError;
	}

	/**
	 * 
	 * @return
	 * The jmxDisableUntil
	 */
	@JsonProperty("jmx_disable_until")
	public String getJmxDisableUntil() {
		return jmxDisableUntil;
	}

	/**
	 * 
	 * @param jmxDisableUntil
	 * The jmx_disable_until
	 */
	@JsonProperty("jmx_disable_until")
	public void setJmxDisableUntil(String jmxDisableUntil) {
		this.jmxDisableUntil = jmxDisableUntil;
	}

	/**
	 * 
	 * @return
	 * The jmxAvailable
	 */
	@JsonProperty("jmx_available")
	public String getJmxAvailable() {
		return jmxAvailable;
	}

	/**
	 * 
	 * @param jmxAvailable
	 * The jmx_available
	 */
	@JsonProperty("jmx_available")
	public void setJmxAvailable(String jmxAvailable) {
		this.jmxAvailable = jmxAvailable;
	}

	/**
	 * 
	 * @return
	 * The jmxErrorsFrom
	 */
	@JsonProperty("jmx_errors_from")
	public String getJmxErrorsFrom() {
		return jmxErrorsFrom;
	}

	/**
	 * 
	 * @param jmxErrorsFrom
	 * The jmx_errors_from
	 */
	@JsonProperty("jmx_errors_from")
	public void setJmxErrorsFrom(String jmxErrorsFrom) {
		this.jmxErrorsFrom = jmxErrorsFrom;
	}

	/**
	 * 
	 * @return
	 * The jmxError
	 */
	@JsonProperty("jmx_error")
	public String getJmxError() {
		return jmxError;
	}

	/**
	 * 
	 * @param jmxError
	 * The jmx_error
	 */
	@JsonProperty("jmx_error")
	public void setJmxError(String jmxError) {
		this.jmxError = jmxError;
	}

	/**
	 * 
	 * @return
	 * The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 * The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 * The flags
	 */
	@JsonProperty("flags")
	public String getFlags() {
		return flags;
	}

	/**
	 * 
	 * @param flags
	 * The flags
	 */
	@JsonProperty("flags")
	public void setFlags(String flags) {
		this.flags = flags;
	}

	/**
	 * 
	 * @return
	 * The templateid
	 */
	@JsonProperty("templateid")
	public String getTemplateid() {
		return templateid;
	}

	/**
	 * 
	 * @param templateid
	 * The templateid
	 */
	@JsonProperty("templateid")
	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	/**
	 * 
	 * @return
	 * The inventory
	 */
	@JsonProperty("inventory")
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * 
	 * @param inventory
	 * The inventory
	 */
	@JsonProperty("inventory")
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
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
		return new HashCodeBuilder().append(maintenances).append(hostid).append(proxyHostid).append(host).append(status).append(disableUntil).append(error).append(available).append(errorsFrom).append(lastaccess).append(ipmiAuthtype).append(ipmiPrivilege).append(ipmiUsername).append(ipmiPassword).append(ipmiDisableUntil).append(ipmiAvailable).append(snmpDisableUntil).append(snmpAvailable).append(maintenanceid).append(maintenanceStatus).append(maintenanceType).append(maintenanceFrom).append(ipmiErrorsFrom).append(snmpErrorsFrom).append(ipmiError).append(snmpError).append(jmxDisableUntil).append(jmxAvailable).append(jmxErrorsFrom).append(jmxError).append(name).append(flags).append(templateid).append(inventory).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof ZabbixMonitoredHostsResponse) == false) {
			return false;
		}
		ZabbixMonitoredHostsResponse rhs = ((ZabbixMonitoredHostsResponse) other);
		return new EqualsBuilder().append(maintenances, rhs.maintenances).append(hostid, rhs.hostid).append(proxyHostid, rhs.proxyHostid).append(host, rhs.host).append(status, rhs.status).append(disableUntil, rhs.disableUntil).append(error, rhs.error).append(available, rhs.available).append(errorsFrom, rhs.errorsFrom).append(lastaccess, rhs.lastaccess).append(ipmiAuthtype, rhs.ipmiAuthtype).append(ipmiPrivilege, rhs.ipmiPrivilege).append(ipmiUsername, rhs.ipmiUsername).append(ipmiPassword, rhs.ipmiPassword).append(ipmiDisableUntil, rhs.ipmiDisableUntil).append(ipmiAvailable, rhs.ipmiAvailable).append(snmpDisableUntil, rhs.snmpDisableUntil).append(snmpAvailable, rhs.snmpAvailable).append(maintenanceid, rhs.maintenanceid).append(maintenanceStatus, rhs.maintenanceStatus).append(maintenanceType, rhs.maintenanceType).append(maintenanceFrom, rhs.maintenanceFrom).append(ipmiErrorsFrom, rhs.ipmiErrorsFrom).append(snmpErrorsFrom, rhs.snmpErrorsFrom).append(ipmiError, rhs.ipmiError).append(snmpError, rhs.snmpError).append(jmxDisableUntil, rhs.jmxDisableUntil).append(jmxAvailable, rhs.jmxAvailable).append(jmxErrorsFrom, rhs.jmxErrorsFrom).append(jmxError, rhs.jmxError).append(name, rhs.name).append(flags, rhs.flags).append(templateid, rhs.templateid).append(inventory, rhs.inventory).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}