package it.prisma.domain.dsl.iaas.openstack.network.response;

import java.util.HashMap;
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
@JsonPropertyOrder({ "network" })
public class OpenstackStackNetworkDetailsResponse {

	@JsonProperty("network")
	private Network network;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The network
	 */
	@JsonProperty("network")
	public Network getNetwork() {
		return network;
	}

	/**
	 * 
	 * @param network
	 *            The network
	 */
	@JsonProperty("network")
	public void setNetwork(Network network) {
		this.network = network;
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
		return new HashCodeBuilder().append(network)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof OpenstackStackNetworkDetailsResponse) == false) {
			return false;
		}
		OpenstackStackNetworkDetailsResponse rhs = ((OpenstackStackNetworkDetailsResponse) other);
		return new EqualsBuilder().append(network, rhs.network)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}