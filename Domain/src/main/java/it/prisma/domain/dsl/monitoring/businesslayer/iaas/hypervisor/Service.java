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
	"host",
	"id"
})
public class Service {

	@JsonProperty("host")
	private String host;
	@JsonProperty("id")
	private Integer id;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The host
	 */
	@JsonProperty("host")
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * @param host
	 * The host
	 */
	@JsonProperty("host")
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
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
		return new HashCodeBuilder().append(host).append(id).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Service) == false) {
			return false;
		}
		Service rhs = ((Service) other);
		return new EqualsBuilder().append(host, rhs.host).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}