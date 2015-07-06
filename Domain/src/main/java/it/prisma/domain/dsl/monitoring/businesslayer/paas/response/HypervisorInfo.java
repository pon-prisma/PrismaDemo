package it.prisma.domain.dsl.monitoring.businesslayer.paas.response;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "hypervisors" })
public class HypervisorInfo {

	@JsonProperty("hypervisors")
	private List<Hypervisor> hypervisors = new ArrayList<Hypervisor>();

	@JsonProperty("hypervisors")
	public List<Hypervisor> getHypervisors() {
		return hypervisors;
	}

	@JsonProperty("hypervisors")
	public void setHypervisors(List<Hypervisor> hypervisors) {
		this.hypervisors = hypervisors;
	}

	@Override
	public String toString() {
		return "HypervisorInfo [hypervisors=" + hypervisors + "]";
	}
	
}