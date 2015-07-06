package it.prisma.domain.dsl.paas.services.mqaas;

import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MQaaSRepresentation extends PaaSServiceRepresentation {

	private static final long serialVersionUID = 4377419958022624682L;

	@JsonProperty("username")
	private String username;

	@JsonProperty("productType")
	private String productType;

	@JsonProperty("productVersion")
	private String productVersion;

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

	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	@JsonProperty("username")
	public void setUsername(String username) {
		this.username = username;
	}

}
