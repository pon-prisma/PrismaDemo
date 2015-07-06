package it.prisma.domain.dsl.paas.services.dbaas.request;

import java.io.Serializable;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "productType", "productVersion", "databaseName", "diskSize",
		"diskEncryption", "verticalScaling", "backup", "rootPassword",
		"extraUser" })
public class DBaaSDetails implements Serializable {

	private static final long serialVersionUID = 4377419958022624682L;

	@JsonProperty("productType")
	private String productType;
	@JsonProperty("productVersion")
	private String productVersion;
	@JsonProperty("databaseName")
	private String databaseName;
	@JsonProperty("diskSize")
	private Integer diskSize;
	@JsonProperty("diskEncryptionEnabled")
	private Boolean diskEncryptionEnabled;
	@JsonProperty("verticalScaling")
	private VerticalScaling verticalScaling;
	@JsonProperty("backup")
	private Backup backup;
	@JsonProperty("rootPassword")
	private String rootPassword;
	@JsonProperty("extraUser")
	private ExtraUser extraUser;

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

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
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

	public VerticalScaling getVerticalScaling() {
		return verticalScaling;
	}

	public void setVerticalScaling(VerticalScaling verticalScaling) {
		this.verticalScaling = verticalScaling;
	}

	public Backup getBackup() {
		return backup;
	}

	public void setBackup(Backup backup) {
		this.backup = backup;
	}

	public String getRootPassword() {
		return rootPassword;
	}

	public void setRootPassword(String rootPassword) {
		this.rootPassword = rootPassword;
	}

	public ExtraUser getExtraUser() {
		return extraUser;
	}

	public void setExtraUser(ExtraUser extraUser) {
		this.extraUser = extraUser;
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