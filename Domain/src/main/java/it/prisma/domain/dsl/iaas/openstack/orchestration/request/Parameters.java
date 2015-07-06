package it.prisma.domain.dsl.iaas.openstack.orchestration.request;

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
@JsonPropertyOrder({ "vm_name", "shared_net_id", "key_name", "image_id",
		"instance_type" })
public class Parameters {

	@JsonProperty("vm_name")
	private String vmName;
	@JsonProperty("shared_net_id")
	private String sharedNetId;
	@JsonProperty("key_name")
	private String keyName;
	@JsonProperty("image_id")
	private String imageId;
	@JsonProperty("instance_type")
	private String instanceType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("vm_name")
	public String getVmName() {
		return vmName;
	}

	@JsonProperty("vm_name")
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	@JsonProperty("shared_net_id")
	public String getSharedNetId() {
		return sharedNetId;
	}

	@JsonProperty("shared_net_id")
	public void setSharedNetId(String sharedNetId) {
		this.sharedNetId = sharedNetId;
	}

	@JsonProperty("key_name")
	public String getKeyName() {
		return keyName;
	}

	@JsonProperty("key_name")
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	@JsonProperty("image_id")
	public String getImageId() {
		return imageId;
	}

	@JsonProperty("image_id")
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	@JsonProperty("instance_type")
	public String getInstanceType() {
		return instanceType;
	}

	@JsonProperty("instance_type")
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
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