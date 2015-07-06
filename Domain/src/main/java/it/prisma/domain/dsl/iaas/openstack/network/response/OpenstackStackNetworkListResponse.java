package it.prisma.domain.dsl.iaas.openstack.network.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "networks" })
public class OpenstackStackNetworkListResponse {

	@JsonProperty("networks")
	private List<Network> networks = new ArrayList<Network>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The networks
	 */
	@JsonProperty("networks")
	public List<Network> getNetworks() {
		return networks;
	}

	/**
	 * 
	 * @param networks
	 *            The networks
	 */
	@JsonProperty("networks")
	public void setNetworks(List<Network> networks) {
		this.networks = networks;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(networks)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof OpenstackStackNetworkListResponse) == false) {
			return false;
		}
		OpenstackStackNetworkListResponse rhs = ((OpenstackStackNetworkListResponse) other);
		return new EqualsBuilder().append(networks, rhs.networks)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}