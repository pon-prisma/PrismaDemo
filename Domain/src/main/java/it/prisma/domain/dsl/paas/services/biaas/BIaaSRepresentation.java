package it.prisma.domain.dsl.paas.services.biaas;

import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BIaaSRepresentation extends PaaSServiceRepresentation {

	private static final long serialVersionUID = 4377419958022624682L;

	public enum BIProductType {
		Pentaho
	}

	public enum BIPentahoConfigurationVariant {
		Server_only, Console_only, Server_w_Console
	}

	@JsonProperty("configurationVariant")
	private String configurationVariant;
	@JsonProperty("productType")
	private String productType;
	@JsonProperty("productVersion")
	private String productVersion;
	@JsonProperty("verticalScalingEnabled")
	private Boolean verticalScalingEnabled;
	@JsonProperty("verticalScalingMaxFlavor")
	private String verticalScalingMaxFlavor;
	@JsonProperty("verticalScalingMaxDiskSize")
	private Integer verticalScalingMaxDiskSize;
	@JsonProperty("backupEnabled")
	private Boolean backupEnabled;

	/**
	 * @see BIPentahoConfigurationVariant
	 * @return
	 */
	@JsonProperty("configurationVariant")
	public String getConfigurationVariant() {
		return configurationVariant;
	}

	/**
	 * @see BIPentahoConfigurationVariant
	 * @param configurationVariant
	 */
	@JsonProperty("configurationVariant")
	public void setConfigurationVariant(String configurationVariant) {
		this.configurationVariant = configurationVariant;
	}

	/**
	 * @see BIProductType
	 * @return
	 */
	public String getProductType() {
		return productType;
	}

	/**
	 * @see BIProductType
	 * @param productType
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public Boolean getVerticalScalingEnabled() {
		return verticalScalingEnabled;
	}

	public void setVerticalScalingEnabled(Boolean verticalScalingEnabled) {
		this.verticalScalingEnabled = verticalScalingEnabled;
	}

	public String getVerticalScalingMaxFlavor() {
		return verticalScalingMaxFlavor;
	}

	public void setVerticalScalingMaxFlavor(String verticalScalingMaxFlavor) {
		this.verticalScalingMaxFlavor = verticalScalingMaxFlavor;
	}

	public Integer getVerticalScalingMaxDiskSize() {
		return verticalScalingMaxDiskSize;
	}

	public void setVerticalScalingMaxDiskSize(Integer verticalScalingMaxDiskSize) {
		this.verticalScalingMaxDiskSize = verticalScalingMaxDiskSize;
	}

	public Boolean getBackupEnabled() {
		return backupEnabled;
	}

	public void setBackupEnabled(Boolean backupEnabled) {
		this.backupEnabled = backupEnabled;
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
