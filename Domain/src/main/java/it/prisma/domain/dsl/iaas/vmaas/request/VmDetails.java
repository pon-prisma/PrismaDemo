package it.prisma.domain.dsl.iaas.vmaas.request;

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
@JsonPropertyOrder({ "imageName", "volume", "networks", "key",
		"securityGroups", "script", "diskPartition" })
public class VmDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("imageName")
	private String imageName;
	@JsonProperty("volume")
	private int volume;
	@JsonProperty("networks")
	private String networks;
	@JsonProperty("key")
	private String key;
	@JsonProperty("securityGroups")
	private String securityGroups;
	@JsonProperty("script")
	private String script;
	@JsonProperty("diskPartition")
	private String diskPartition;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The imageName
	 */
	@JsonProperty("imageName")
	public String getImageName() {
		return imageName;
	}

	/**
	 * 
	 * @param imageName
	 *            The imageName
	 */
	@JsonProperty("imageName")
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * 
	 * @return The volume
	 */
	@JsonProperty("volume")
	public int getVolume() {
		return volume;
	}

	/**
	 * 
	 * @param volume
	 *            The volume
	 */
	@JsonProperty("volume")
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 * 
	 * @return The networks
	 */
	@JsonProperty("networks")
	public String getNetworks() {
		return networks;
	}

	/**
	 * 
	 * @param networks
	 *            The networks
	 */
	@JsonProperty("networks")
	public void setNetworks(String networks) {
		this.networks = networks;
	}

	/**
	 * 
	 * @return The key
	 */
	@JsonProperty("key")
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @param key
	 *            The key
	 */
	@JsonProperty("key")
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 
	 * @return The securityGroups
	 */
	@JsonProperty("securityGroups")
	public String getSecurityGroups() {
		return securityGroups;
	}

	/**
	 * 
	 * @param securityGroups
	 *            The securityGroups
	 */
	@JsonProperty("securityGroups")
	public void setSecurityGroups(String securityGroups) {
		this.securityGroups = securityGroups;
	}

	/**
	 * 
	 * @return The script
	 */
	@JsonProperty("script")
	public String getScript() {
		return script;
	}

	/**
	 * 
	 * @param script
	 *            The script
	 */
	@JsonProperty("script")
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * 
	 * @return The diskPartition
	 */
	@JsonProperty("diskPartition")
	public String getDiskPartition() {
		return diskPartition;
	}

	/**
	 * 
	 * @param diskPartition
	 *            The diskPartition
	 */
	@JsonProperty("diskPartition")
	public void setDiskPartition(String diskPartition) {
		this.diskPartition = diskPartition;
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
		return new HashCodeBuilder().append(imageName).append(volume)
				.append(networks).append(key).append(securityGroups)
				.append(script).append(diskPartition)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof VmDetails) == false) {
			return false;
		}
		VmDetails rhs = ((VmDetails) other);
		return new EqualsBuilder().append(imageName, rhs.imageName)
				.append(volume, rhs.volume).append(networks, rhs.networks)
				.append(key, rhs.key)
				.append(securityGroups, rhs.securityGroups)
				.append(script, rhs.script)
				.append(diskPartition, rhs.diskPartition)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}