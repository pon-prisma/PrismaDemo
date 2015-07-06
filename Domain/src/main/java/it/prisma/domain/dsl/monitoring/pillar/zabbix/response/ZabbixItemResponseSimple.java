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
	"itemid",
	"type",
	"snmp_community",
	"snmp_oid",
	"hostid",
	"name",
	"key_",
	"delay",
	"history",
	"trends",
	"status",
	"value_type",
	"trapper_hosts",
	"units",
	"multiplier",
	"delta",
	"snmpv3_securityname",
	"snmpv3_securitylevel",
	"snmpv3_authpassphrase",
	"snmpv3_privpassphrase",
	"formula",
	"error",
	"lastlogsize",
	"logtimefmt",
	"templateid",
	"valuemapid",
	"delay_flex",
	"params",
	"ipmi_sensor",
	"data_type",
	"authtype",
	"username",
	"password",
	"publickey",
	"privatekey",
	"mtime",
	"flags",
	"filter",
	"interfaceid",
	"port",
	"description",
	"inventory_link",
	"lifetime",
	"snmpv3_authprotocol",
	"snmpv3_privprotocol",
	"state",
	"snmpv3_contextname",
	"lastclock",
	"lastns",
	"lastvalue",
	"prevvalue"
	})
	public class ZabbixItemResponseSimple {

	@JsonProperty("itemid")
	private String itemid;
	@JsonProperty("type")
	private String type;
	@JsonProperty("snmp_community")
	private String snmpCommunity;
	@JsonProperty("snmp_oid")
	private String snmpOid;
	@JsonProperty("hostid")
	private String hostid;
	@JsonProperty("name")
	private String name;
	@JsonProperty("key_")
	private String key;
	@JsonProperty("delay")
	private String delay;
	@JsonProperty("history")
	private String history;
	@JsonProperty("trends")
	private String trends;
	@JsonProperty("status")
	private String status;
	@JsonProperty("value_type")
	private String valueType;
	@JsonProperty("trapper_hosts")
	private String trapperHosts;
	@JsonProperty("units")
	private String units;
	@JsonProperty("multiplier")
	private String multiplier;
	@JsonProperty("delta")
	private String delta;
	@JsonProperty("snmpv3_securityname")
	private String snmpv3Securityname;
	@JsonProperty("snmpv3_securitylevel")
	private String snmpv3Securitylevel;
	@JsonProperty("snmpv3_authpassphrase")
	private String snmpv3Authpassphrase;
	@JsonProperty("snmpv3_privpassphrase")
	private String snmpv3Privpassphrase;
	@JsonProperty("formula")
	private String formula;
	@JsonProperty("error")
	private String error;
	@JsonProperty("lastlogsize")
	private String lastlogsize;
	@JsonProperty("logtimefmt")
	private String logtimefmt;
	@JsonProperty("templateid")
	private String templateid;
	@JsonProperty("valuemapid")
	private String valuemapid;
	@JsonProperty("delay_flex")
	private String delayFlex;
	@JsonProperty("params")
	private String params;
	@JsonProperty("ipmi_sensor")
	private String ipmiSensor;
	@JsonProperty("data_type")
	private String dataType;
	@JsonProperty("authtype")
	private String authtype;
	@JsonProperty("username")
	private String username;
	@JsonProperty("password")
	private String password;
	@JsonProperty("publickey")
	private String publickey;
	@JsonProperty("privatekey")
	private String privatekey;
	@JsonProperty("mtime")
	private String mtime;
	@JsonProperty("flags")
	private String flags;
	@JsonProperty("filter")
	private String filter;
	@JsonProperty("interfaceid")
	private String interfaceid;
	@JsonProperty("port")
	private String port;
	@JsonProperty("description")
	private String description;
	@JsonProperty("inventory_link")
	private String inventoryLink;
	@JsonProperty("lifetime")
	private String lifetime;
	@JsonProperty("snmpv3_authprotocol")
	private String snmpv3Authprotocol;
	@JsonProperty("snmpv3_privprotocol")
	private String snmpv3Privprotocol;
	@JsonProperty("state")
	private String state;
	@JsonProperty("snmpv3_contextname")
	private String snmpv3Contextname;
	@JsonProperty("lastclock")
	private String lastclock;
	@JsonProperty("lastns")
	private String lastns;
	@JsonProperty("lastvalue")
	private String lastvalue;
	@JsonProperty("prevvalue")
	private String prevvalue;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	* 
	* @return
	* The itemid
	*/
	@JsonProperty("itemid")
	public String getItemid() {
	return itemid;
	}

	/**
	* 
	* @param itemid
	* The itemid
	*/
	@JsonProperty("itemid")
	public void setItemid(String itemid) {
	this.itemid = itemid;
	}

	/**
	* 
	* @return
	* The type
	*/
	@JsonProperty("type")
	public String getType() {
	return type;
	}

	/**
	* 
	* @param type
	* The type
	*/
	@JsonProperty("type")
	public void setType(String type) {
	this.type = type;
	}

	/**
	* 
	* @return
	* The snmpCommunity
	*/
	@JsonProperty("snmp_community")
	public String getSnmpCommunity() {
	return snmpCommunity;
	}

	/**
	* 
	* @param snmpCommunity
	* The snmp_community
	*/
	@JsonProperty("snmp_community")
	public void setSnmpCommunity(String snmpCommunity) {
	this.snmpCommunity = snmpCommunity;
	}

	/**
	* 
	* @return
	* The snmpOid
	*/
	@JsonProperty("snmp_oid")
	public String getSnmpOid() {
	return snmpOid;
	}

	/**
	* 
	* @param snmpOid
	* The snmp_oid
	*/
	@JsonProperty("snmp_oid")
	public void setSnmpOid(String snmpOid) {
	this.snmpOid = snmpOid;
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
	* The key
	*/
	@JsonProperty("key_")
	public String getKey() {
	return key;
	}

	/**
	* 
	* @param key
	* The key_
	*/
	@JsonProperty("key_")
	public void setKey(String key) {
	this.key = key;
	}

	/**
	* 
	* @return
	* The delay
	*/
	@JsonProperty("delay")
	public String getDelay() {
	return delay;
	}

	/**
	* 
	* @param delay
	* The delay
	*/
	@JsonProperty("delay")
	public void setDelay(String delay) {
	this.delay = delay;
	}

	/**
	* 
	* @return
	* The history
	*/
	@JsonProperty("history")
	public String getHistory() {
	return history;
	}

	/**
	* 
	* @param history
	* The history
	*/
	@JsonProperty("history")
	public void setHistory(String history) {
	this.history = history;
	}

	/**
	* 
	* @return
	* The trends
	*/
	@JsonProperty("trends")
	public String getTrends() {
	return trends;
	}

	/**
	* 
	* @param trends
	* The trends
	*/
	@JsonProperty("trends")
	public void setTrends(String trends) {
	this.trends = trends;
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
	* The valueType
	*/
	@JsonProperty("value_type")
	public String getValueType() {
	return valueType;
	}

	/**
	* 
	* @param valueType
	* The value_type
	*/
	@JsonProperty("value_type")
	public void setValueType(String valueType) {
	this.valueType = valueType;
	}

	/**
	* 
	* @return
	* The trapperHosts
	*/
	@JsonProperty("trapper_hosts")
	public String getTrapperHosts() {
	return trapperHosts;
	}

	/**
	* 
	* @param trapperHosts
	* The trapper_hosts
	*/
	@JsonProperty("trapper_hosts")
	public void setTrapperHosts(String trapperHosts) {
	this.trapperHosts = trapperHosts;
	}

	/**
	* 
	* @return
	* The units
	*/
	@JsonProperty("units")
	public String getUnits() {
	return units;
	}

	/**
	* 
	* @param units
	* The units
	*/
	@JsonProperty("units")
	public void setUnits(String units) {
	this.units = units;
	}

	/**
	* 
	* @return
	* The multiplier
	*/
	@JsonProperty("multiplier")
	public String getMultiplier() {
	return multiplier;
	}

	/**
	* 
	* @param multiplier
	* The multiplier
	*/
	@JsonProperty("multiplier")
	public void setMultiplier(String multiplier) {
	this.multiplier = multiplier;
	}

	/**
	* 
	* @return
	* The delta
	*/
	@JsonProperty("delta")
	public String getDelta() {
	return delta;
	}

	/**
	* 
	* @param delta
	* The delta
	*/
	@JsonProperty("delta")
	public void setDelta(String delta) {
	this.delta = delta;
	}

	/**
	* 
	* @return
	* The snmpv3Securityname
	*/
	@JsonProperty("snmpv3_securityname")
	public String getSnmpv3Securityname() {
	return snmpv3Securityname;
	}

	/**
	* 
	* @param snmpv3Securityname
	* The snmpv3_securityname
	*/
	@JsonProperty("snmpv3_securityname")
	public void setSnmpv3Securityname(String snmpv3Securityname) {
	this.snmpv3Securityname = snmpv3Securityname;
	}

	/**
	* 
	* @return
	* The snmpv3Securitylevel
	*/
	@JsonProperty("snmpv3_securitylevel")
	public String getSnmpv3Securitylevel() {
	return snmpv3Securitylevel;
	}

	/**
	* 
	* @param snmpv3Securitylevel
	* The snmpv3_securitylevel
	*/
	@JsonProperty("snmpv3_securitylevel")
	public void setSnmpv3Securitylevel(String snmpv3Securitylevel) {
	this.snmpv3Securitylevel = snmpv3Securitylevel;
	}

	/**
	* 
	* @return
	* The snmpv3Authpassphrase
	*/
	@JsonProperty("snmpv3_authpassphrase")
	public String getSnmpv3Authpassphrase() {
	return snmpv3Authpassphrase;
	}

	/**
	* 
	* @param snmpv3Authpassphrase
	* The snmpv3_authpassphrase
	*/
	@JsonProperty("snmpv3_authpassphrase")
	public void setSnmpv3Authpassphrase(String snmpv3Authpassphrase) {
	this.snmpv3Authpassphrase = snmpv3Authpassphrase;
	}

	/**
	* 
	* @return
	* The snmpv3Privpassphrase
	*/
	@JsonProperty("snmpv3_privpassphrase")
	public String getSnmpv3Privpassphrase() {
	return snmpv3Privpassphrase;
	}

	/**
	* 
	* @param snmpv3Privpassphrase
	* The snmpv3_privpassphrase
	*/
	@JsonProperty("snmpv3_privpassphrase")
	public void setSnmpv3Privpassphrase(String snmpv3Privpassphrase) {
	this.snmpv3Privpassphrase = snmpv3Privpassphrase;
	}

	/**
	* 
	* @return
	* The formula
	*/
	@JsonProperty("formula")
	public String getFormula() {
	return formula;
	}

	/**
	* 
	* @param formula
	* The formula
	*/
	@JsonProperty("formula")
	public void setFormula(String formula) {
	this.formula = formula;
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
	* The lastlogsize
	*/
	@JsonProperty("lastlogsize")
	public String getLastlogsize() {
	return lastlogsize;
	}

	/**
	* 
	* @param lastlogsize
	* The lastlogsize
	*/
	@JsonProperty("lastlogsize")
	public void setLastlogsize(String lastlogsize) {
	this.lastlogsize = lastlogsize;
	}

	/**
	* 
	* @return
	* The logtimefmt
	*/
	@JsonProperty("logtimefmt")
	public String getLogtimefmt() {
	return logtimefmt;
	}

	/**
	* 
	* @param logtimefmt
	* The logtimefmt
	*/
	@JsonProperty("logtimefmt")
	public void setLogtimefmt(String logtimefmt) {
	this.logtimefmt = logtimefmt;
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
	* The valuemapid
	*/
	@JsonProperty("valuemapid")
	public String getValuemapid() {
	return valuemapid;
	}

	/**
	* 
	* @param valuemapid
	* The valuemapid
	*/
	@JsonProperty("valuemapid")
	public void setValuemapid(String valuemapid) {
	this.valuemapid = valuemapid;
	}

	/**
	* 
	* @return
	* The delayFlex
	*/
	@JsonProperty("delay_flex")
	public String getDelayFlex() {
	return delayFlex;
	}

	/**
	* 
	* @param delayFlex
	* The delay_flex
	*/
	@JsonProperty("delay_flex")
	public void setDelayFlex(String delayFlex) {
	this.delayFlex = delayFlex;
	}

	/**
	* 
	* @return
	* The params
	*/
	@JsonProperty("params")
	public String getParams() {
	return params;
	}

	/**
	* 
	* @param params
	* The params
	*/
	@JsonProperty("params")
	public void setParams(String params) {
	this.params = params;
	}

	/**
	* 
	* @return
	* The ipmiSensor
	*/
	@JsonProperty("ipmi_sensor")
	public String getIpmiSensor() {
	return ipmiSensor;
	}

	/**
	* 
	* @param ipmiSensor
	* The ipmi_sensor
	*/
	@JsonProperty("ipmi_sensor")
	public void setIpmiSensor(String ipmiSensor) {
	this.ipmiSensor = ipmiSensor;
	}

	/**
	* 
	* @return
	* The dataType
	*/
	@JsonProperty("data_type")
	public String getDataType() {
	return dataType;
	}

	/**
	* 
	* @param dataType
	* The data_type
	*/
	@JsonProperty("data_type")
	public void setDataType(String dataType) {
	this.dataType = dataType;
	}

	/**
	* 
	* @return
	* The authtype
	*/
	@JsonProperty("authtype")
	public String getAuthtype() {
	return authtype;
	}

	/**
	* 
	* @param authtype
	* The authtype
	*/
	@JsonProperty("authtype")
	public void setAuthtype(String authtype) {
	this.authtype = authtype;
	}

	/**
	* 
	* @return
	* The username
	*/
	@JsonProperty("username")
	public String getUsername() {
	return username;
	}

	/**
	* 
	* @param username
	* The username
	*/
	@JsonProperty("username")
	public void setUsername(String username) {
	this.username = username;
	}

	/**
	* 
	* @return
	* The password
	*/
	@JsonProperty("password")
	public String getPassword() {
	return password;
	}

	/**
	* 
	* @param password
	* The password
	*/
	@JsonProperty("password")
	public void setPassword(String password) {
	this.password = password;
	}

	/**
	* 
	* @return
	* The publickey
	*/
	@JsonProperty("publickey")
	public String getPublickey() {
	return publickey;
	}

	/**
	* 
	* @param publickey
	* The publickey
	*/
	@JsonProperty("publickey")
	public void setPublickey(String publickey) {
	this.publickey = publickey;
	}

	/**
	* 
	* @return
	* The privatekey
	*/
	@JsonProperty("privatekey")
	public String getPrivatekey() {
	return privatekey;
	}

	/**
	* 
	* @param privatekey
	* The privatekey
	*/
	@JsonProperty("privatekey")
	public void setPrivatekey(String privatekey) {
	this.privatekey = privatekey;
	}

	/**
	* 
	* @return
	* The mtime
	*/
	@JsonProperty("mtime")
	public String getMtime() {
	return mtime;
	}

	/**
	* 
	* @param mtime
	* The mtime
	*/
	@JsonProperty("mtime")
	public void setMtime(String mtime) {
	this.mtime = mtime;
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
	* The filter
	*/
	@JsonProperty("filter")
	public String getFilter() {
	return filter;
	}

	/**
	* 
	* @param filter
	* The filter
	*/
	@JsonProperty("filter")
	public void setFilter(String filter) {
	this.filter = filter;
	}

	/**
	* 
	* @return
	* The interfaceid
	*/
	@JsonProperty("interfaceid")
	public String getInterfaceid() {
	return interfaceid;
	}

	/**
	* 
	* @param interfaceid
	* The interfaceid
	*/
	@JsonProperty("interfaceid")
	public void setInterfaceid(String interfaceid) {
	this.interfaceid = interfaceid;
	}

	/**
	* 
	* @return
	* The port
	*/
	@JsonProperty("port")
	public String getPort() {
	return port;
	}

	/**
	* 
	* @param port
	* The port
	*/
	@JsonProperty("port")
	public void setPort(String port) {
	this.port = port;
	}

	/**
	* 
	* @return
	* The description
	*/
	@JsonProperty("description")
	public String getDescription() {
	return description;
	}

	/**
	* 
	* @param description
	* The description
	*/
	@JsonProperty("description")
	public void setDescription(String description) {
	this.description = description;
	}

	/**
	* 
	* @return
	* The inventoryLink
	*/
	@JsonProperty("inventory_link")
	public String getInventoryLink() {
	return inventoryLink;
	}

	/**
	* 
	* @param inventoryLink
	* The inventory_link
	*/
	@JsonProperty("inventory_link")
	public void setInventoryLink(String inventoryLink) {
	this.inventoryLink = inventoryLink;
	}

	/**
	* 
	* @return
	* The lifetime
	*/
	@JsonProperty("lifetime")
	public String getLifetime() {
	return lifetime;
	}

	/**
	* 
	* @param lifetime
	* The lifetime
	*/
	@JsonProperty("lifetime")
	public void setLifetime(String lifetime) {
	this.lifetime = lifetime;
	}

	/**
	* 
	* @return
	* The snmpv3Authprotocol
	*/
	@JsonProperty("snmpv3_authprotocol")
	public String getSnmpv3Authprotocol() {
	return snmpv3Authprotocol;
	}

	/**
	* 
	* @param snmpv3Authprotocol
	* The snmpv3_authprotocol
	*/
	@JsonProperty("snmpv3_authprotocol")
	public void setSnmpv3Authprotocol(String snmpv3Authprotocol) {
	this.snmpv3Authprotocol = snmpv3Authprotocol;
	}

	/**
	* 
	* @return
	* The snmpv3Privprotocol
	*/
	@JsonProperty("snmpv3_privprotocol")
	public String getSnmpv3Privprotocol() {
	return snmpv3Privprotocol;
	}

	/**
	* 
	* @param snmpv3Privprotocol
	* The snmpv3_privprotocol
	*/
	@JsonProperty("snmpv3_privprotocol")
	public void setSnmpv3Privprotocol(String snmpv3Privprotocol) {
	this.snmpv3Privprotocol = snmpv3Privprotocol;
	}

	/**
	* 
	* @return
	* The state
	*/
	@JsonProperty("state")
	public String getState() {
	return state;
	}

	/**
	* 
	* @param state
	* The state
	*/
	@JsonProperty("state")
	public void setState(String state) {
	this.state = state;
	}

	/**
	* 
	* @return
	* The snmpv3Contextname
	*/
	@JsonProperty("snmpv3_contextname")
	public String getSnmpv3Contextname() {
	return snmpv3Contextname;
	}

	/**
	* 
	* @param snmpv3Contextname
	* The snmpv3_contextname
	*/
	@JsonProperty("snmpv3_contextname")
	public void setSnmpv3Contextname(String snmpv3Contextname) {
	this.snmpv3Contextname = snmpv3Contextname;
	}

	/**
	* 
	* @return
	* The lastclock
	*/
	@JsonProperty("lastclock")
	public String getLastclock() {
	return lastclock;
	}

	/**
	* 
	* @param lastclock
	* The lastclock
	*/
	@JsonProperty("lastclock")
	public void setLastclock(String lastclock) {
	this.lastclock = lastclock;
	}

	/**
	* 
	* @return
	* The lastns
	*/
	@JsonProperty("lastns")
	public String getLastns() {
	return lastns;
	}

	/**
	* 
	* @param lastns
	* The lastns
	*/
	@JsonProperty("lastns")
	public void setLastns(String lastns) {
	this.lastns = lastns;
	}

	/**
	* 
	* @return
	* The lastvalue
	*/
	@JsonProperty("lastvalue")
	public String getLastvalue() {
	return lastvalue;
	}

	/**
	* 
	* @param lastvalue
	* The lastvalue
	*/
	@JsonProperty("lastvalue")
	public void setLastvalue(String lastvalue) {
	this.lastvalue = lastvalue;
	}

	/**
	* 
	* @return
	* The prevvalue
	*/
	@JsonProperty("prevvalue")
	public String getPrevvalue() {
	return prevvalue;
	}

	/**
	* 
	* @param prevvalue
	* The prevvalue
	*/
	@JsonProperty("prevvalue")
	public void setPrevvalue(String prevvalue) {
	this.prevvalue = prevvalue;
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
	
