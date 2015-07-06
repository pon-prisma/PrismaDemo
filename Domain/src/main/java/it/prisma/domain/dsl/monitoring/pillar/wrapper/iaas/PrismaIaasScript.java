package it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas;

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
@JsonPropertyOrder({
	"available_nodes",
	"storage",
	"network",
	"total_nodes"
})
public class PrismaIaasScript {

	@JsonProperty("available_nodes")
	private Integer availableNodes;
	@JsonProperty("storage")
	private String storage;
	@JsonProperty("network")
	private String network;
	@JsonProperty("total_nodes")
	private Integer totalNodes;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The availableNodes
	 */
	@JsonProperty("available_nodes")
	public Integer getAvailableNodes() {
		return availableNodes;
	}

	/**
	 * 
	 * @param availableNodes
	 * The available_nodes
	 */
	@JsonProperty("available_nodes")
	public void setAvailableNodes(Integer availableNodes) {
		this.availableNodes = availableNodes;
	}

	/**
	 * 
	 * @return
	 * The storage
	 */
	@JsonProperty("storage")
	public String getStorage() {
		return storage;
	}

	/**
	 * 
	 * @param storage
	 * The storage
	 */
	@JsonProperty("storage")
	public void setStorage(String storage) {
		this.storage = storage;
	}

	/**
	 * 
	 * @return
	 * The network
	 */
	@JsonProperty("network")
	public String getNetwork() {
		return network;
	}

	/**
	 * 
	 * @param network
	 * The network
	 */
	@JsonProperty("network")
	public void setNetwork(String network) {
		this.network = network;
	}

	/**
	 * 
	 * @return
	 * The totalNodes
	 */
	@JsonProperty("total_nodes")
	public Integer getTotalNodes() {
		return totalNodes;
	}

	/**
	 * 
	 * @param totalNodes
	 * The total_nodes
	 */
	@JsonProperty("total_nodes")
	public void setTotalNodes(Integer totalNodes) {
		this.totalNodes = totalNodes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

//	@JsonAnyGetter
//	public Map<String, Object> getAdditionalProperties() {
//		return this.additionalProperties;
//	}
//
//	@JsonAnySetter
//	public void setAdditionalProperty(String name, Object value) {
//		this.additionalProperties.put(name, value);
//	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(availableNodes).append(storage).append(network).append(totalNodes).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof PrismaIaasScript) == false) {
			return false;
		}
		PrismaIaasScript rhs = ((PrismaIaasScript) other);
		return new EqualsBuilder().append(availableNodes, rhs.availableNodes).append(storage, rhs.storage).append(network, rhs.network).append(totalNodes, rhs.totalNodes).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}

}
