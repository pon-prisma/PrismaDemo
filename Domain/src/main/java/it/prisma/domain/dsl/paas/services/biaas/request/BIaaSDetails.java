package it.prisma.domain.dsl.paas.services.biaas.request;

import it.prisma.domain.dsl.paas.services.generic.request.BaseCustomPaaSDetails;

import java.io.Serializable;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class BIaaSDetails implements Serializable {
	
	private static final long serialVersionUID = 4377419958022624682L;

	@JsonProperty("configurationVariant")
	private String configurationVariant;
	@JsonProperty("productType")
	private String productType;
	@JsonProperty("productVersion")
	private String productVersion;
	@JsonProperty("diskSize")
	private Integer diskSize;
	@JsonProperty("diskEncryptionEnabled")
	private Boolean diskEncryptionEnabled;
	@JsonProperty("productSpecificDetails")
	private BaseCustomPaaSDetails productSpecificDetails;

	public String getConfigurationVariant() {
		return configurationVariant;
	}

	public void setConfigurationVariant(String configurationVariant) {
		this.configurationVariant = configurationVariant;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public Integer getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(Integer diskSize) {
		this.diskSize = diskSize;
	}

	public Boolean getDiskEncryptionEnabled() {
		return diskEncryptionEnabled;
	}

	public void setDiskEncryptionEnabled(Boolean diskEncryptionEnabled) {
		this.diskEncryptionEnabled = diskEncryptionEnabled;
	}

	public BaseCustomPaaSDetails getProductSpecificDetails() {
		return productSpecificDetails;
	}

	public void setProductSpecificDetails(
			BaseCustomPaaSDetails productSpecificDetails) {
		this.productSpecificDetails = productSpecificDetails;
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

}