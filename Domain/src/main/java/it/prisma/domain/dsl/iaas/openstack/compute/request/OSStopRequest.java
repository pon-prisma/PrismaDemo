package it.prisma.domain.dsl.iaas.openstack.compute.request;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "os-stop" })
public class OSStopRequest {

	@JsonProperty("os-stop")
	private String osStop;

	/**
	 * 
	 * @return The osStart
	 */
	@JsonProperty("os-stop")
	public String getOsStop() {
		return osStop;
	}

	/**
	 * 
	 * @param osStart
	 *            The os-start
	 */
	@JsonProperty("os-stop")
	public void setOsStop(String osStop) {
		this.osStop = osStop;
	}

}