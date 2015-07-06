package it.prisma.domain.dsl.monitoring.businesslayer.iaas.hypervisor;

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
	"cores",
	"threads",
	"sockets"
})
public class Topology {

	@JsonProperty("cores")
	private Integer cores;
	@JsonProperty("threads")
	private Integer threads;
	@JsonProperty("sockets")
	private Integer sockets;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The cores
	 */
	@JsonProperty("cores")
	public Integer getCores() {
		return cores;
	}

	/**
	 * 
	 * @param cores
	 * The cores
	 */
	@JsonProperty("cores")
	public void setCores(Integer cores) {
		this.cores = cores;
	}

	/**
	 * 
	 * @return
	 * The threads
	 */
	@JsonProperty("threads")
	public Integer getThreads() {
		return threads;
	}

	/**
	 * 
	 * @param threads
	 * The threads
	 */
	@JsonProperty("threads")
	public void setThreads(Integer threads) {
		this.threads = threads;
	}

	/**
	 * 
	 * @return
	 * The sockets
	 */
	@JsonProperty("sockets")
	public Integer getSockets() {
		return sockets;
	}

	/**
	 * 
	 * @param sockets
	 * The sockets
	 */
	@JsonProperty("sockets")
	public void setSockets(Integer sockets) {
		this.sockets = sockets;
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
		return new HashCodeBuilder().append(cores).append(threads).append(sockets).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Topology) == false) {
			return false;
		}
		Topology rhs = ((Topology) other);
		return new EqualsBuilder().append(cores, rhs.cores).append(threads, rhs.threads).append(sockets, rhs.sockets).append(additionalProperties, rhs.additionalProperties).isEquals();
	}
}