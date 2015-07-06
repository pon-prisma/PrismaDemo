package it.prisma.domain.dsl.monitoring.pillar.wrapper.paas;

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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"hostIDInMetrics",
	"hostIDinWatcher"
})
public class CreatedHostInServer {

	@JsonProperty("hostIDInMetrics")
	private List<String> hostIDInMetrics = new ArrayList<String>();
	@JsonProperty("hostIDinWatcher")
	private List<String> hostIDinWatcher = new ArrayList<String>();

	/**
	 * 
	 * @return
	 * The hostIDInMetrics
	 */
	@JsonProperty("hostIDInMetrics")
	public List<String> getHostIDInMetrics() {
		return hostIDInMetrics;
	}

	/**
	 * 
	 * @param hostIDInMetrics
	 * The hostIDInMetrics
	 */
	@JsonProperty("hostIDInMetrics")
	public void setHostIDInMetrics(List<String> hostIDInMetrics) {
		this.hostIDInMetrics = hostIDInMetrics;
	}

	/**
	 * 
	 * @return
	 * The hostIDinWatcher
	 */
	@JsonProperty("hostIDinWatcher")
	public List<String> getHostIDinWatcher() {
		return hostIDinWatcher;
	}

	/**
	 * 
	 * @param hostIDinWatcher
	 * The hostIDinWatcher
	 */
	@JsonProperty("hostIDinWatcher")
	public void setHostIDinWatcher(List<String> hostIDinWatcher) {
		this.hostIDinWatcher = hostIDinWatcher;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(hostIDInMetrics).append(hostIDinWatcher).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof CreatedHostInServer) == false) {
			return false;
		}
		CreatedHostInServer rhs = ((CreatedHostInServer) other);
		return new EqualsBuilder().append(hostIDInMetrics, rhs.hostIDInMetrics).append(hostIDinWatcher, rhs.hostIDinWatcher).isEquals();
	}

}
