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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"hostid",
"inventory_mode",
"type",
"type_full",
"name",
"alias",
"os",
"os_full",
"os_short",
"serialno_a",
"serialno_b",
"tag",
"asset_tag",
"macaddress_a",
"macaddress_b",
"hardware",
"hardware_full",
"software",
"software_full",
"software_app_a",
"software_app_b",
"software_app_c",
"software_app_d",
"software_app_e",
"contact",
"location",
"location_lat",
"location_lon",
"notes",
"chassis",
"model",
"hw_arch",
"vendor",
"contract_number",
"installer_name",
"deployment_status",
"url_a",
"url_b",
"url_c",
"host_networks",
"host_netmask",
"host_router",
"oob_ip",
"oob_netmask",
"oob_router",
"date_hw_purchase",
"date_hw_install",
"date_hw_expiry",
"date_hw_decomm",
"site_address_a",
"site_address_b",
"site_address_c",
"site_city",
"site_state",
"site_country",
"site_zip",
"site_rack",
"site_notes",
"poc_1_name",
"poc_1_email",
"poc_1_phone_a",
"poc_1_phone_b",
"poc_1_cell",
"poc_1_screen",
"poc_1_notes",
"poc_2_name",
"poc_2_email",
"poc_2_phone_a",
"poc_2_phone_b",
"poc_2_cell",
"poc_2_screen",
"poc_2_notes"
})
public class Inventory {

@JsonProperty("hostid")
private String hostid;
@JsonProperty("inventory_mode")
private String inventoryMode;
@JsonProperty("type")
private String type;
@JsonProperty("type_full")
private String typeFull;
@JsonProperty("name")
private String name;
@JsonProperty("alias")
private String alias;
@JsonProperty("os")
private String os;
@JsonProperty("os_full")
private String osFull;
@JsonProperty("os_short")
private String osShort;
@JsonProperty("serialno_a")
private String serialnoA;
@JsonProperty("serialno_b")
private String serialnoB;
@JsonProperty("tag")
private String tag;
@JsonProperty("asset_tag")
private String assetTag;
@JsonProperty("macaddress_a")
private String macaddressA;
@JsonProperty("macaddress_b")
private String macaddressB;
@JsonProperty("hardware")
private String hardware;
@JsonProperty("hardware_full")
private String hardwareFull;
@JsonProperty("software")
private String software;
@JsonProperty("software_full")
private String softwareFull;
@JsonProperty("software_app_a")
private String softwareAppA;
@JsonProperty("software_app_b")
private String softwareAppB;
@JsonProperty("software_app_c")
private String softwareAppC;
@JsonProperty("software_app_d")
private String softwareAppD;
@JsonProperty("software_app_e")
private String softwareAppE;
@JsonProperty("contact")
private String contact;
@JsonProperty("location")
private String location;
@JsonProperty("location_lat")
private String locationLat;
@JsonProperty("location_lon")
private String locationLon;
@JsonProperty("notes")
private String notes;
@JsonProperty("chassis")
private String chassis;
@JsonProperty("model")
private String model;
@JsonProperty("hw_arch")
private String hwArch;
@JsonProperty("vendor")
private String vendor;
@JsonProperty("contract_number")
private String contractNumber;
@JsonProperty("installer_name")
private String installerName;
@JsonProperty("deployment_status")
private String deploymentStatus;
@JsonProperty("url_a")
private String urlA;
@JsonProperty("url_b")
private String urlB;
@JsonProperty("url_c")
private String urlC;
@JsonProperty("host_networks")
private String hostNetworks;
@JsonProperty("host_netmask")
private String hostNetmask;
@JsonProperty("host_router")
private String hostRouter;
@JsonProperty("oob_ip")
private String oobIp;
@JsonProperty("oob_netmask")
private String oobNetmask;
@JsonProperty("oob_router")
private String oobRouter;
@JsonProperty("date_hw_purchase")
private String dateHwPurchase;
@JsonProperty("date_hw_install")
private String dateHwInstall;
@JsonProperty("date_hw_expiry")
private String dateHwExpiry;
@JsonProperty("date_hw_decomm")
private String dateHwDecomm;
@JsonProperty("site_address_a")
private String siteAddressA;
@JsonProperty("site_address_b")
private String siteAddressB;
@JsonProperty("site_address_c")
private String siteAddressC;
@JsonProperty("site_city")
private String siteCity;
@JsonProperty("site_state")
private String siteState;
@JsonProperty("site_country")
private String siteCountry;
@JsonProperty("site_zip")
private String siteZip;
@JsonProperty("site_rack")
private String siteRack;
@JsonProperty("site_notes")
private String siteNotes;
@JsonProperty("poc_1_name")
private String poc1Name;
@JsonProperty("poc_1_email")
private String poc1Email;
@JsonProperty("poc_1_phone_a")
private String poc1PhoneA;
@JsonProperty("poc_1_phone_b")
private String poc1PhoneB;
@JsonProperty("poc_1_cell")
private String poc1Cell;
@JsonProperty("poc_1_screen")
private String poc1Screen;
@JsonProperty("poc_1_notes")
private String poc1Notes;
@JsonProperty("poc_2_name")
private String poc2Name;
@JsonProperty("poc_2_email")
private String poc2Email;
@JsonProperty("poc_2_phone_a")
private String poc2PhoneA;
@JsonProperty("poc_2_phone_b")
private String poc2PhoneB;
@JsonProperty("poc_2_cell")
private String poc2Cell;
@JsonProperty("poc_2_screen")
private String poc2Screen;
@JsonProperty("poc_2_notes")
private String poc2Notes;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
* The inventoryMode
*/
@JsonProperty("inventory_mode")
public String getInventoryMode() {
return inventoryMode;
}

/**
* 
* @param inventoryMode
* The inventory_mode
*/
@JsonProperty("inventory_mode")
public void setInventoryMode(String inventoryMode) {
this.inventoryMode = inventoryMode;
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
* The typeFull
*/
@JsonProperty("type_full")
public String getTypeFull() {
return typeFull;
}

/**
* 
* @param typeFull
* The type_full
*/
@JsonProperty("type_full")
public void setTypeFull(String typeFull) {
this.typeFull = typeFull;
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
* The alias
*/
@JsonProperty("alias")
public String getAlias() {
return alias;
}

/**
* 
* @param alias
* The alias
*/
@JsonProperty("alias")
public void setAlias(String alias) {
this.alias = alias;
}

/**
* 
* @return
* The os
*/
@JsonProperty("os")
public String getOs() {
return os;
}

/**
* 
* @param os
* The os
*/
@JsonProperty("os")
public void setOs(String os) {
this.os = os;
}

/**
* 
* @return
* The osFull
*/
@JsonProperty("os_full")
public String getOsFull() {
return osFull;
}

/**
* 
* @param osFull
* The os_full
*/
@JsonProperty("os_full")
public void setOsFull(String osFull) {
this.osFull = osFull;
}

/**
* 
* @return
* The osShort
*/
@JsonProperty("os_short")
public String getOsShort() {
return osShort;
}

/**
* 
* @param osShort
* The os_short
*/
@JsonProperty("os_short")
public void setOsShort(String osShort) {
this.osShort = osShort;
}

/**
* 
* @return
* The serialnoA
*/
@JsonProperty("serialno_a")
public String getSerialnoA() {
return serialnoA;
}

/**
* 
* @param serialnoA
* The serialno_a
*/
@JsonProperty("serialno_a")
public void setSerialnoA(String serialnoA) {
this.serialnoA = serialnoA;
}

/**
* 
* @return
* The serialnoB
*/
@JsonProperty("serialno_b")
public String getSerialnoB() {
return serialnoB;
}

/**
* 
* @param serialnoB
* The serialno_b
*/
@JsonProperty("serialno_b")
public void setSerialnoB(String serialnoB) {
this.serialnoB = serialnoB;
}

/**
* 
* @return
* The tag
*/
@JsonProperty("tag")
public String getTag() {
return tag;
}

/**
* 
* @param tag
* The tag
*/
@JsonProperty("tag")
public void setTag(String tag) {
this.tag = tag;
}

/**
* 
* @return
* The assetTag
*/
@JsonProperty("asset_tag")
public String getAssetTag() {
return assetTag;
}

/**
* 
* @param assetTag
* The asset_tag
*/
@JsonProperty("asset_tag")
public void setAssetTag(String assetTag) {
this.assetTag = assetTag;
}

/**
* 
* @return
* The macaddressA
*/
@JsonProperty("macaddress_a")
public String getMacaddressA() {
return macaddressA;
}

/**
* 
* @param macaddressA
* The macaddress_a
*/
@JsonProperty("macaddress_a")
public void setMacaddressA(String macaddressA) {
this.macaddressA = macaddressA;
}

/**
* 
* @return
* The macaddressB
*/
@JsonProperty("macaddress_b")
public String getMacaddressB() {
return macaddressB;
}

/**
* 
* @param macaddressB
* The macaddress_b
*/
@JsonProperty("macaddress_b")
public void setMacaddressB(String macaddressB) {
this.macaddressB = macaddressB;
}

/**
* 
* @return
* The hardware
*/
@JsonProperty("hardware")
public String getHardware() {
return hardware;
}

/**
* 
* @param hardware
* The hardware
*/
@JsonProperty("hardware")
public void setHardware(String hardware) {
this.hardware = hardware;
}

/**
* 
* @return
* The hardwareFull
*/
@JsonProperty("hardware_full")
public String getHardwareFull() {
return hardwareFull;
}

/**
* 
* @param hardwareFull
* The hardware_full
*/
@JsonProperty("hardware_full")
public void setHardwareFull(String hardwareFull) {
this.hardwareFull = hardwareFull;
}

/**
* 
* @return
* The software
*/
@JsonProperty("software")
public String getSoftware() {
return software;
}

/**
* 
* @param software
* The software
*/
@JsonProperty("software")
public void setSoftware(String software) {
this.software = software;
}

/**
* 
* @return
* The softwareFull
*/
@JsonProperty("software_full")
public String getSoftwareFull() {
return softwareFull;
}

/**
* 
* @param softwareFull
* The software_full
*/
@JsonProperty("software_full")
public void setSoftwareFull(String softwareFull) {
this.softwareFull = softwareFull;
}

/**
* 
* @return
* The softwareAppA
*/
@JsonProperty("software_app_a")
public String getSoftwareAppA() {
return softwareAppA;
}

/**
* 
* @param softwareAppA
* The software_app_a
*/
@JsonProperty("software_app_a")
public void setSoftwareAppA(String softwareAppA) {
this.softwareAppA = softwareAppA;
}

/**
* 
* @return
* The softwareAppB
*/
@JsonProperty("software_app_b")
public String getSoftwareAppB() {
return softwareAppB;
}

/**
* 
* @param softwareAppB
* The software_app_b
*/
@JsonProperty("software_app_b")
public void setSoftwareAppB(String softwareAppB) {
this.softwareAppB = softwareAppB;
}

/**
* 
* @return
* The softwareAppC
*/
@JsonProperty("software_app_c")
public String getSoftwareAppC() {
return softwareAppC;
}

/**
* 
* @param softwareAppC
* The software_app_c
*/
@JsonProperty("software_app_c")
public void setSoftwareAppC(String softwareAppC) {
this.softwareAppC = softwareAppC;
}

/**
* 
* @return
* The softwareAppD
*/
@JsonProperty("software_app_d")
public String getSoftwareAppD() {
return softwareAppD;
}

/**
* 
* @param softwareAppD
* The software_app_d
*/
@JsonProperty("software_app_d")
public void setSoftwareAppD(String softwareAppD) {
this.softwareAppD = softwareAppD;
}

/**
* 
* @return
* The softwareAppE
*/
@JsonProperty("software_app_e")
public String getSoftwareAppE() {
return softwareAppE;
}

/**
* 
* @param softwareAppE
* The software_app_e
*/
@JsonProperty("software_app_e")
public void setSoftwareAppE(String softwareAppE) {
this.softwareAppE = softwareAppE;
}

/**
* 
* @return
* The contact
*/
@JsonProperty("contact")
public String getContact() {
return contact;
}

/**
* 
* @param contact
* The contact
*/
@JsonProperty("contact")
public void setContact(String contact) {
this.contact = contact;
}

/**
* 
* @return
* The location
*/
@JsonProperty("location")
public String getLocation() {
return location;
}

/**
* 
* @param location
* The location
*/
@JsonProperty("location")
public void setLocation(String location) {
this.location = location;
}

/**
* 
* @return
* The locationLat
*/
@JsonProperty("location_lat")
public String getLocationLat() {
return locationLat;
}

/**
* 
* @param locationLat
* The location_lat
*/
@JsonProperty("location_lat")
public void setLocationLat(String locationLat) {
this.locationLat = locationLat;
}

/**
* 
* @return
* The locationLon
*/
@JsonProperty("location_lon")
public String getLocationLon() {
return locationLon;
}

/**
* 
* @param locationLon
* The location_lon
*/
@JsonProperty("location_lon")
public void setLocationLon(String locationLon) {
this.locationLon = locationLon;
}

/**
* 
* @return
* The notes
*/
@JsonProperty("notes")
public String getNotes() {
return notes;
}

/**
* 
* @param notes
* The notes
*/
@JsonProperty("notes")
public void setNotes(String notes) {
this.notes = notes;
}

/**
* 
* @return
* The chassis
*/
@JsonProperty("chassis")
public String getChassis() {
return chassis;
}

/**
* 
* @param chassis
* The chassis
*/
@JsonProperty("chassis")
public void setChassis(String chassis) {
this.chassis = chassis;
}

/**
* 
* @return
* The model
*/
@JsonProperty("model")
public String getModel() {
return model;
}

/**
* 
* @param model
* The model
*/
@JsonProperty("model")
public void setModel(String model) {
this.model = model;
}

/**
* 
* @return
* The hwArch
*/
@JsonProperty("hw_arch")
public String getHwArch() {
return hwArch;
}

/**
* 
* @param hwArch
* The hw_arch
*/
@JsonProperty("hw_arch")
public void setHwArch(String hwArch) {
this.hwArch = hwArch;
}

/**
* 
* @return
* The vendor
*/
@JsonProperty("vendor")
public String getVendor() {
return vendor;
}

/**
* 
* @param vendor
* The vendor
*/
@JsonProperty("vendor")
public void setVendor(String vendor) {
this.vendor = vendor;
}

/**
* 
* @return
* The contractNumber
*/
@JsonProperty("contract_number")
public String getContractNumber() {
return contractNumber;
}

/**
* 
* @param contractNumber
* The contract_number
*/
@JsonProperty("contract_number")
public void setContractNumber(String contractNumber) {
this.contractNumber = contractNumber;
}

/**
* 
* @return
* The installerName
*/
@JsonProperty("installer_name")
public String getInstallerName() {
return installerName;
}

/**
* 
* @param installerName
* The installer_name
*/
@JsonProperty("installer_name")
public void setInstallerName(String installerName) {
this.installerName = installerName;
}

/**
* 
* @return
* The deploymentStatus
*/
@JsonProperty("deployment_status")
public String getDeploymentStatus() {
return deploymentStatus;
}

/**
* 
* @param deploymentStatus
* The deployment_status
*/
@JsonProperty("deployment_status")
public void setDeploymentStatus(String deploymentStatus) {
this.deploymentStatus = deploymentStatus;
}

/**
* 
* @return
* The urlA
*/
@JsonProperty("url_a")
public String getUrlA() {
return urlA;
}

/**
* 
* @param urlA
* The url_a
*/
@JsonProperty("url_a")
public void setUrlA(String urlA) {
this.urlA = urlA;
}

/**
* 
* @return
* The urlB
*/
@JsonProperty("url_b")
public String getUrlB() {
return urlB;
}

/**
* 
* @param urlB
* The url_b
*/
@JsonProperty("url_b")
public void setUrlB(String urlB) {
this.urlB = urlB;
}

/**
* 
* @return
* The urlC
*/
@JsonProperty("url_c")
public String getUrlC() {
return urlC;
}

/**
* 
* @param urlC
* The url_c
*/
@JsonProperty("url_c")
public void setUrlC(String urlC) {
this.urlC = urlC;
}

/**
* 
* @return
* The hostNetworks
*/
@JsonProperty("host_networks")
public String getHostNetworks() {
return hostNetworks;
}

/**
* 
* @param hostNetworks
* The host_networks
*/
@JsonProperty("host_networks")
public void setHostNetworks(String hostNetworks) {
this.hostNetworks = hostNetworks;
}

/**
* 
* @return
* The hostNetmask
*/
@JsonProperty("host_netmask")
public String getHostNetmask() {
return hostNetmask;
}

/**
* 
* @param hostNetmask
* The host_netmask
*/
@JsonProperty("host_netmask")
public void setHostNetmask(String hostNetmask) {
this.hostNetmask = hostNetmask;
}

/**
* 
* @return
* The hostRouter
*/
@JsonProperty("host_router")
public String getHostRouter() {
return hostRouter;
}

/**
* 
* @param hostRouter
* The host_router
*/
@JsonProperty("host_router")
public void setHostRouter(String hostRouter) {
this.hostRouter = hostRouter;
}

/**
* 
* @return
* The oobIp
*/
@JsonProperty("oob_ip")
public String getOobIp() {
return oobIp;
}

/**
* 
* @param oobIp
* The oob_ip
*/
@JsonProperty("oob_ip")
public void setOobIp(String oobIp) {
this.oobIp = oobIp;
}

/**
* 
* @return
* The oobNetmask
*/
@JsonProperty("oob_netmask")
public String getOobNetmask() {
return oobNetmask;
}

/**
* 
* @param oobNetmask
* The oob_netmask
*/
@JsonProperty("oob_netmask")
public void setOobNetmask(String oobNetmask) {
this.oobNetmask = oobNetmask;
}

/**
* 
* @return
* The oobRouter
*/
@JsonProperty("oob_router")
public String getOobRouter() {
return oobRouter;
}

/**
* 
* @param oobRouter
* The oob_router
*/
@JsonProperty("oob_router")
public void setOobRouter(String oobRouter) {
this.oobRouter = oobRouter;
}

/**
* 
* @return
* The dateHwPurchase
*/
@JsonProperty("date_hw_purchase")
public String getDateHwPurchase() {
return dateHwPurchase;
}

/**
* 
* @param dateHwPurchase
* The date_hw_purchase
*/
@JsonProperty("date_hw_purchase")
public void setDateHwPurchase(String dateHwPurchase) {
this.dateHwPurchase = dateHwPurchase;
}

/**
* 
* @return
* The dateHwInstall
*/
@JsonProperty("date_hw_install")
public String getDateHwInstall() {
return dateHwInstall;
}

/**
* 
* @param dateHwInstall
* The date_hw_install
*/
@JsonProperty("date_hw_install")
public void setDateHwInstall(String dateHwInstall) {
this.dateHwInstall = dateHwInstall;
}

/**
* 
* @return
* The dateHwExpiry
*/
@JsonProperty("date_hw_expiry")
public String getDateHwExpiry() {
return dateHwExpiry;
}

/**
* 
* @param dateHwExpiry
* The date_hw_expiry
*/
@JsonProperty("date_hw_expiry")
public void setDateHwExpiry(String dateHwExpiry) {
this.dateHwExpiry = dateHwExpiry;
}

/**
* 
* @return
* The dateHwDecomm
*/
@JsonProperty("date_hw_decomm")
public String getDateHwDecomm() {
return dateHwDecomm;
}

/**
* 
* @param dateHwDecomm
* The date_hw_decomm
*/
@JsonProperty("date_hw_decomm")
public void setDateHwDecomm(String dateHwDecomm) {
this.dateHwDecomm = dateHwDecomm;
}

/**
* 
* @return
* The siteAddressA
*/
@JsonProperty("site_address_a")
public String getSiteAddressA() {
return siteAddressA;
}

/**
* 
* @param siteAddressA
* The site_address_a
*/
@JsonProperty("site_address_a")
public void setSiteAddressA(String siteAddressA) {
this.siteAddressA = siteAddressA;
}

/**
* 
* @return
* The siteAddressB
*/
@JsonProperty("site_address_b")
public String getSiteAddressB() {
return siteAddressB;
}

/**
* 
* @param siteAddressB
* The site_address_b
*/
@JsonProperty("site_address_b")
public void setSiteAddressB(String siteAddressB) {
this.siteAddressB = siteAddressB;
}

/**
* 
* @return
* The siteAddressC
*/
@JsonProperty("site_address_c")
public String getSiteAddressC() {
return siteAddressC;
}

/**
* 
* @param siteAddressC
* The site_address_c
*/
@JsonProperty("site_address_c")
public void setSiteAddressC(String siteAddressC) {
this.siteAddressC = siteAddressC;
}

/**
* 
* @return
* The siteCity
*/
@JsonProperty("site_city")
public String getSiteCity() {
return siteCity;
}

/**
* 
* @param siteCity
* The site_city
*/
@JsonProperty("site_city")
public void setSiteCity(String siteCity) {
this.siteCity = siteCity;
}

/**
* 
* @return
* The siteState
*/
@JsonProperty("site_state")
public String getSiteState() {
return siteState;
}

/**
* 
* @param siteState
* The site_state
*/
@JsonProperty("site_state")
public void setSiteState(String siteState) {
this.siteState = siteState;
}

/**
* 
* @return
* The siteCountry
*/
@JsonProperty("site_country")
public String getSiteCountry() {
return siteCountry;
}

/**
* 
* @param siteCountry
* The site_country
*/
@JsonProperty("site_country")
public void setSiteCountry(String siteCountry) {
this.siteCountry = siteCountry;
}

/**
* 
* @return
* The siteZip
*/
@JsonProperty("site_zip")
public String getSiteZip() {
return siteZip;
}

/**
* 
* @param siteZip
* The site_zip
*/
@JsonProperty("site_zip")
public void setSiteZip(String siteZip) {
this.siteZip = siteZip;
}

/**
* 
* @return
* The siteRack
*/
@JsonProperty("site_rack")
public String getSiteRack() {
return siteRack;
}

/**
* 
* @param siteRack
* The site_rack
*/
@JsonProperty("site_rack")
public void setSiteRack(String siteRack) {
this.siteRack = siteRack;
}

/**
* 
* @return
* The siteNotes
*/
@JsonProperty("site_notes")
public String getSiteNotes() {
return siteNotes;
}

/**
* 
* @param siteNotes
* The site_notes
*/
@JsonProperty("site_notes")
public void setSiteNotes(String siteNotes) {
this.siteNotes = siteNotes;
}

/**
* 
* @return
* The poc1Name
*/
@JsonProperty("poc_1_name")
public String getPoc1Name() {
return poc1Name;
}

/**
* 
* @param poc1Name
* The poc_1_name
*/
@JsonProperty("poc_1_name")
public void setPoc1Name(String poc1Name) {
this.poc1Name = poc1Name;
}

/**
* 
* @return
* The poc1Email
*/
@JsonProperty("poc_1_email")
public String getPoc1Email() {
return poc1Email;
}

/**
* 
* @param poc1Email
* The poc_1_email
*/
@JsonProperty("poc_1_email")
public void setPoc1Email(String poc1Email) {
this.poc1Email = poc1Email;
}

/**
* 
* @return
* The poc1PhoneA
*/
@JsonProperty("poc_1_phone_a")
public String getPoc1PhoneA() {
return poc1PhoneA;
}

/**
* 
* @param poc1PhoneA
* The poc_1_phone_a
*/
@JsonProperty("poc_1_phone_a")
public void setPoc1PhoneA(String poc1PhoneA) {
this.poc1PhoneA = poc1PhoneA;
}

/**
* 
* @return
* The poc1PhoneB
*/
@JsonProperty("poc_1_phone_b")
public String getPoc1PhoneB() {
return poc1PhoneB;
}

/**
* 
* @param poc1PhoneB
* The poc_1_phone_b
*/
@JsonProperty("poc_1_phone_b")
public void setPoc1PhoneB(String poc1PhoneB) {
this.poc1PhoneB = poc1PhoneB;
}

/**
* 
* @return
* The poc1Cell
*/
@JsonProperty("poc_1_cell")
public String getPoc1Cell() {
return poc1Cell;
}

/**
* 
* @param poc1Cell
* The poc_1_cell
*/
@JsonProperty("poc_1_cell")
public void setPoc1Cell(String poc1Cell) {
this.poc1Cell = poc1Cell;
}

/**
* 
* @return
* The poc1Screen
*/
@JsonProperty("poc_1_screen")
public String getPoc1Screen() {
return poc1Screen;
}

/**
* 
* @param poc1Screen
* The poc_1_screen
*/
@JsonProperty("poc_1_screen")
public void setPoc1Screen(String poc1Screen) {
this.poc1Screen = poc1Screen;
}

/**
* 
* @return
* The poc1Notes
*/
@JsonProperty("poc_1_notes")
public String getPoc1Notes() {
return poc1Notes;
}

/**
* 
* @param poc1Notes
* The poc_1_notes
*/
@JsonProperty("poc_1_notes")
public void setPoc1Notes(String poc1Notes) {
this.poc1Notes = poc1Notes;
}

/**
* 
* @return
* The poc2Name
*/
@JsonProperty("poc_2_name")
public String getPoc2Name() {
return poc2Name;
}

/**
* 
* @param poc2Name
* The poc_2_name
*/
@JsonProperty("poc_2_name")
public void setPoc2Name(String poc2Name) {
this.poc2Name = poc2Name;
}

/**
* 
* @return
* The poc2Email
*/
@JsonProperty("poc_2_email")
public String getPoc2Email() {
return poc2Email;
}

/**
* 
* @param poc2Email
* The poc_2_email
*/
@JsonProperty("poc_2_email")
public void setPoc2Email(String poc2Email) {
this.poc2Email = poc2Email;
}

/**
* 
* @return
* The poc2PhoneA
*/
@JsonProperty("poc_2_phone_a")
public String getPoc2PhoneA() {
return poc2PhoneA;
}

/**
* 
* @param poc2PhoneA
* The poc_2_phone_a
*/
@JsonProperty("poc_2_phone_a")
public void setPoc2PhoneA(String poc2PhoneA) {
this.poc2PhoneA = poc2PhoneA;
}

/**
* 
* @return
* The poc2PhoneB
*/
@JsonProperty("poc_2_phone_b")
public String getPoc2PhoneB() {
return poc2PhoneB;
}

/**
* 
* @param poc2PhoneB
* The poc_2_phone_b
*/
@JsonProperty("poc_2_phone_b")
public void setPoc2PhoneB(String poc2PhoneB) {
this.poc2PhoneB = poc2PhoneB;
}

/**
* 
* @return
* The poc2Cell
*/
@JsonProperty("poc_2_cell")
public String getPoc2Cell() {
return poc2Cell;
}

/**
* 
* @param poc2Cell
* The poc_2_cell
*/
@JsonProperty("poc_2_cell")
public void setPoc2Cell(String poc2Cell) {
this.poc2Cell = poc2Cell;
}

/**
* 
* @return
* The poc2Screen
*/
@JsonProperty("poc_2_screen")
public String getPoc2Screen() {
return poc2Screen;
}

/**
* 
* @param poc2Screen
* The poc_2_screen
*/
@JsonProperty("poc_2_screen")
public void setPoc2Screen(String poc2Screen) {
this.poc2Screen = poc2Screen;
}

/**
* 
* @return
* The poc2Notes
*/
@JsonProperty("poc_2_notes")
public String getPoc2Notes() {
return poc2Notes;
}

/**
* 
* @param poc2Notes
* The poc_2_notes
*/
@JsonProperty("poc_2_notes")
public void setPoc2Notes(String poc2Notes) {
this.poc2Notes = poc2Notes;
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
return new HashCodeBuilder().append(hostid).append(inventoryMode).append(type).append(typeFull).append(name).append(alias).append(os).append(osFull).append(osShort).append(serialnoA).append(serialnoB).append(tag).append(assetTag).append(macaddressA).append(macaddressB).append(hardware).append(hardwareFull).append(software).append(softwareFull).append(softwareAppA).append(softwareAppB).append(softwareAppC).append(softwareAppD).append(softwareAppE).append(contact).append(location).append(locationLat).append(locationLon).append(notes).append(chassis).append(model).append(hwArch).append(vendor).append(contractNumber).append(installerName).append(deploymentStatus).append(urlA).append(urlB).append(urlC).append(hostNetworks).append(hostNetmask).append(hostRouter).append(oobIp).append(oobNetmask).append(oobRouter).append(dateHwPurchase).append(dateHwInstall).append(dateHwExpiry).append(dateHwDecomm).append(siteAddressA).append(siteAddressB).append(siteAddressC).append(siteCity).append(siteState).append(siteCountry).append(siteZip).append(siteRack).append(siteNotes).append(poc1Name).append(poc1Email).append(poc1PhoneA).append(poc1PhoneB).append(poc1Cell).append(poc1Screen).append(poc1Notes).append(poc2Name).append(poc2Email).append(poc2PhoneA).append(poc2PhoneB).append(poc2Cell).append(poc2Screen).append(poc2Notes).append(additionalProperties).toHashCode();
}

@Override
public boolean equals(Object other) {
if (other == this) {
return true;
}
if ((other instanceof Inventory) == false) {
return false;
}
Inventory rhs = ((Inventory) other);
return new EqualsBuilder().append(hostid, rhs.hostid).append(inventoryMode, rhs.inventoryMode).append(type, rhs.type).append(typeFull, rhs.typeFull).append(name, rhs.name).append(alias, rhs.alias).append(os, rhs.os).append(osFull, rhs.osFull).append(osShort, rhs.osShort).append(serialnoA, rhs.serialnoA).append(serialnoB, rhs.serialnoB).append(tag, rhs.tag).append(assetTag, rhs.assetTag).append(macaddressA, rhs.macaddressA).append(macaddressB, rhs.macaddressB).append(hardware, rhs.hardware).append(hardwareFull, rhs.hardwareFull).append(software, rhs.software).append(softwareFull, rhs.softwareFull).append(softwareAppA, rhs.softwareAppA).append(softwareAppB, rhs.softwareAppB).append(softwareAppC, rhs.softwareAppC).append(softwareAppD, rhs.softwareAppD).append(softwareAppE, rhs.softwareAppE).append(contact, rhs.contact).append(location, rhs.location).append(locationLat, rhs.locationLat).append(locationLon, rhs.locationLon).append(notes, rhs.notes).append(chassis, rhs.chassis).append(model, rhs.model).append(hwArch, rhs.hwArch).append(vendor, rhs.vendor).append(contractNumber, rhs.contractNumber).append(installerName, rhs.installerName).append(deploymentStatus, rhs.deploymentStatus).append(urlA, rhs.urlA).append(urlB, rhs.urlB).append(urlC, rhs.urlC).append(hostNetworks, rhs.hostNetworks).append(hostNetmask, rhs.hostNetmask).append(hostRouter, rhs.hostRouter).append(oobIp, rhs.oobIp).append(oobNetmask, rhs.oobNetmask).append(oobRouter, rhs.oobRouter).append(dateHwPurchase, rhs.dateHwPurchase).append(dateHwInstall, rhs.dateHwInstall).append(dateHwExpiry, rhs.dateHwExpiry).append(dateHwDecomm, rhs.dateHwDecomm).append(siteAddressA, rhs.siteAddressA).append(siteAddressB, rhs.siteAddressB).append(siteAddressC, rhs.siteAddressC).append(siteCity, rhs.siteCity).append(siteState, rhs.siteState).append(siteCountry, rhs.siteCountry).append(siteZip, rhs.siteZip).append(siteRack, rhs.siteRack).append(siteNotes, rhs.siteNotes).append(poc1Name, rhs.poc1Name).append(poc1Email, rhs.poc1Email).append(poc1PhoneA, rhs.poc1PhoneA).append(poc1PhoneB, rhs.poc1PhoneB).append(poc1Cell, rhs.poc1Cell).append(poc1Screen, rhs.poc1Screen).append(poc1Notes, rhs.poc1Notes).append(poc2Name, rhs.poc2Name).append(poc2Email, rhs.poc2Email).append(poc2PhoneA, rhs.poc2PhoneA).append(poc2PhoneB, rhs.poc2PhoneB).append(poc2Cell, rhs.poc2Cell).append(poc2Screen, rhs.poc2Screen).append(poc2Notes, rhs.poc2Notes).append(additionalProperties, rhs.additionalProperties).isEquals();
}

}