package it.prisma.domain.dsl.iaas.openstack.orchestration.response;

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
@JsonPropertyOrder({ "availability_zone", "shared_net_id", "db_password",
		"image_id", "AWS::StackName", "instance_type", "AWS::StackId",
		"db_name", "db_username", "db_root_password", "AWS::Region", "key_name" })
public class Parameters {

	@JsonProperty("availability_zone")
	private String availabilityZone;
	@JsonProperty("shared_net_id")
	private String sharedNetId;
	@JsonProperty("db_password")
	private String dbPassword;
	@JsonProperty("image_id")
	private String imageId;
	@JsonProperty("AWS::StackName")
	private String aWSStackName;
	@JsonProperty("instance_type")
	private String instanceType;
	@JsonProperty("AWS::StackId")
	private String aWSStackId;
	@JsonProperty("db_name")
	private String dbName;
	@JsonProperty("db_username")
	private String dbUsername;
	@JsonProperty("db_root_password")
	private String dbRootPassword;
	@JsonProperty("AWS::Region")
	private String aWSRegion;
	@JsonProperty("key_name")
	private String keyName;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("availability_zone")
	public String getAvailabilityZone() {
		return availabilityZone;
	}

	@JsonProperty("availability_zone")
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	@JsonProperty("shared_net_id")
	public String getSharedNetId() {
		return sharedNetId;
	}

	@JsonProperty("shared_net_id")
	public void setSharedNetId(String sharedNetId) {
		this.sharedNetId = sharedNetId;
	}

	@JsonProperty("db_password")
	public String getDbPassword() {
		return dbPassword;
	}

	@JsonProperty("db_password")
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	@JsonProperty("image_id")
	public String getImageId() {
		return imageId;
	}

	@JsonProperty("image_id")
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	@JsonProperty("AWS::StackName")
	public String getAWSStackName() {
		return aWSStackName;
	}

	@JsonProperty("AWS::StackName")
	public void setAWSStackName(String aWSStackName) {
		this.aWSStackName = aWSStackName;
	}

	@JsonProperty("instance_type")
	public String getInstanceType() {
		return instanceType;
	}

	@JsonProperty("instance_type")
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	@JsonProperty("AWS::StackId")
	public String getAWSStackId() {
		return aWSStackId;
	}

	@JsonProperty("AWS::StackId")
	public void setAWSStackId(String aWSStackId) {
		this.aWSStackId = aWSStackId;
	}

	@JsonProperty("db_name")
	public String getDbName() {
		return dbName;
	}

	@JsonProperty("db_name")
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@JsonProperty("db_username")
	public String getDbUsername() {
		return dbUsername;
	}

	@JsonProperty("db_username")
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	@JsonProperty("db_root_password")
	public String getDbRootPassword() {
		return dbRootPassword;
	}

	@JsonProperty("db_root_password")
	public void setDbRootPassword(String dbRootPassword) {
		this.dbRootPassword = dbRootPassword;
	}

	@JsonProperty("AWS::Region")
	public String getAWSRegion() {
		return aWSRegion;
	}

	@JsonProperty("AWS::Region")
	public void setAWSRegion(String aWSRegion) {
		this.aWSRegion = aWSRegion;
	}

	@JsonProperty("key_name")
	public String getKeyName() {
		return keyName;
	}

	@JsonProperty("key_name")
	public void setKeyName(String keyName) {
		this.keyName = keyName;
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