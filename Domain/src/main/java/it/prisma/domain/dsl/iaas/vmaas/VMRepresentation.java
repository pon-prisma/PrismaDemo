package it.prisma.domain.dsl.iaas.vmaas;

import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VMRepresentation extends PaaSServiceRepresentation {

	private static final long serialVersionUID = 4377419958022624682L;

	@JsonProperty("imageName")
	private String imageName;

	@JsonProperty("networks")
	private List<String> networks;

	@JsonProperty("key")
	private String key;

	@JsonProperty("securityGroups")
	private List<String> securityGroups;

	@JsonProperty("diskPartition")
	private String diskPartition;

	@JsonProperty("diskSize")
	private Integer diskSize;

	@JsonProperty("scalingEnabled")
	private Boolean scalingEnabled;

	public Integer getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(Integer diskSize) {
		this.diskSize = diskSize;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getSecurityGroups() {
		return securityGroups;
	}

	public void setSecurityGroups(List<String> securityGroups) {
		this.securityGroups = securityGroups;
	}

	public String getDiskPartition() {
		return diskPartition;
	}

	public void setDiskPartition(String diskPartition) {
		this.diskPartition = diskPartition;
	}

	public Boolean getScalingEnabled() {
		return scalingEnabled;
	}

	public void setScalingEnabled(Boolean scalingEnabled) {
		this.scalingEnabled = scalingEnabled;
	}

	public List<String> getNetworks() {
		return networks;
	}

	public void setNetworks(List<String> networks) {
		this.networks = networks;
	}
}
