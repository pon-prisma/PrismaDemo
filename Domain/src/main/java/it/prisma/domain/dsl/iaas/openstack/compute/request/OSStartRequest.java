package it.prisma.domain.dsl.iaas.openstack.compute.request;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "os-start" })
public class OSStartRequest {

	@JsonProperty("os-start")
	private String osStart;

	/**
	 * 
	 * @return The osStart
	 */
	@JsonProperty("os-start")
	public String getOsStart() {
		return osStart;
	}

	/**
	 * 
	 * @param osStart
	 *            The os-start
	 */
	@JsonProperty("os-start")
	public void setOsStart(String osStart) {
		this.osStart = osStart;
	}

}