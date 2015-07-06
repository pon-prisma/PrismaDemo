package it.prisma.domain.dsl.iaas.network;

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
@JsonPropertyOrder({ "id", "networkid", "name", "cidr", "ipversion",
		"enabledhcp", "gateway" })
public class SubnetRepresentation {

	@JsonProperty("id")
	private String id;
	@JsonProperty("networkid")
	private String networkid;
	@JsonProperty("name")
	private String name;
	@JsonProperty("cidr")
	private String cidr;
	@JsonProperty("ipversion")
	private String ipversion;
	@JsonProperty("enabledhcp")
	private Boolean enabledhcp;
	@JsonProperty("gateway")
	private String gateway;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The networkid
	 */
	@JsonProperty("networkid")
	public String getNetworkid() {
		return networkid;
	}

	/**
	 * 
	 * @param networkid
	 *            The networkid
	 */
	@JsonProperty("networkid")
	public void setNetworkid(String networkid) {
		this.networkid = networkid;
	}

	/**
	 * 
	 * @return The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The cidr
	 */
	@JsonProperty("cidr")
	public String getCidr() {
		return cidr;
	}

	/**
	 * 
	 * @param cidr
	 *            The cidr
	 */
	@JsonProperty("cidr")
	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	/**
	 * 
	 * @return The ipversion
	 */
	@JsonProperty("ipversion")
	public String getIpversion() {
		return ipversion;
	}

	/**
	 * 
	 * @param ipversion
	 *            The ipversion
	 */
	@JsonProperty("ipversion")
	public void setIpversion(String ipversion) {
		this.ipversion = ipversion;
	}

	/**
	 * 
	 * @return The enabledhcp
	 */
	@JsonProperty("enabledhcp")
	public Boolean getEnabledhcp() {
		return enabledhcp;
	}

	/**
	 * 
	 * @param enabledhcp
	 *            The enabledhcp
	 */
	@JsonProperty("enabledhcp")
	public void setEnabledhcp(Boolean enabledhcp) {
		this.enabledhcp = enabledhcp;
	}

	/**
	 * 
	 * @return The gateway
	 */
	@JsonProperty("gateway")
	public String getGateway() {
		return gateway;
	}

	/**
	 * 
	 * @param gateway
	 *            The gateway
	 */
	@JsonProperty("gateway")
	public void setGateway(String gateway) {
		this.gateway = gateway;
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
		return new HashCodeBuilder().append(id).append(networkid).append(name)
				.append(cidr).append(ipversion).append(enabledhcp)
				.append(gateway).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof SubnetRepresentation) == false) {
			return false;
		}
		SubnetRepresentation rhs = ((SubnetRepresentation) other);
		return new EqualsBuilder().append(id, rhs.id)
				.append(networkid, rhs.networkid).append(name, rhs.name)
				.append(cidr, rhs.cidr).append(ipversion, rhs.ipversion)
				.append(enabledhcp, rhs.enabledhcp)
				.append(gateway, rhs.gateway)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}