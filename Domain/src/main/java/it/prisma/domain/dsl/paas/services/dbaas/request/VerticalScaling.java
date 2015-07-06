package it.prisma.domain.dsl.paas.services.dbaas.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerticalScaling implements Serializable {

	private static final long serialVersionUID = 8218913368304836277L;

	@JsonProperty("maxFlavor")
	private String maxFlavor;
	@JsonProperty("maxDiskSize")
	private Integer maxDiskSize;

	public String getMaxFlavor() {
		return maxFlavor;
	}

	public void setMaxFlavor(String maxFlavor) {
		this.maxFlavor = maxFlavor;
	}

	public Integer getMaxDiskSize() {
		return maxDiskSize;
	}

	public void setMaxDiskSize(Integer maxDiskSize) {
		this.maxDiskSize = maxDiskSize;
	}
}
