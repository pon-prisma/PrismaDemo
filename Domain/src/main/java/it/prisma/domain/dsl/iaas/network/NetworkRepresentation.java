package it.prisma.domain.dsl.iaas.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "name", "shared", "status", "adminstateup",
		"subnets" })
public class NetworkRepresentation {

	@JsonProperty("id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("shared")
	private Boolean shared;
	@JsonProperty("status")
	private State status;
	@JsonProperty("adminstateup")
	private String adminstateup;
	@JsonProperty("subnets")
	private List<SubnetRepresentation> subnets = new ArrayList<SubnetRepresentation>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	
	public NetworkRepresentation() {
	}

	public NetworkRepresentation(Long id, String name, Boolean shared,
			State status, String adminstateup,
			List<SubnetRepresentation> subnets) {
		this.id = id;
		this.name = name;
		this.shared = shared;
		this.status = status;
		this.adminstateup = adminstateup;
		this.subnets = subnets;
	}

	public NetworkRepresentation(Long id2, String name2, Boolean shared2,
			String string, String adminStateUp2, Object subnets2) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The shared
	 */
	@JsonProperty("shared")
	public Boolean getShared() {
		return shared;
	}

	/**
	 * 
	 * @param shared
	 *            The shared
	 */
	@JsonProperty("shared")
	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	/**
	 * 
	 * @return The status
	 */
	@JsonProperty("status")
	public State getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            The status
	 */
	@JsonProperty("status")
	public void setStatus(State status) {
		this.status = status;
	}

	/**
	 * 
	 * @return The adminstateup
	 */
	@JsonProperty("adminstateup")
	public String getAdminstateup() {
		return adminstateup;
	}

	/**
	 * 
	 * @param adminstateup
	 *            The adminstateup
	 */
	@JsonProperty("adminstateup")
	public void setAdminstateup(String adminstateup) {
		this.adminstateup = adminstateup;
	}

	/**
	 * 
	 * @return The subnets
	 */
	@JsonProperty("subnets")
	public List<SubnetRepresentation> getSubnets() {
		return subnets;
	}

	/**
	 * 
	 * @param subnets
	 *            The subnets
	 */
	@JsonProperty("subnets")
	public void setSubnets(List<SubnetRepresentation> subnets) {
		this.subnets = subnets;
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
		return new HashCodeBuilder().append(id).append(name).append(shared)
				.append(status).append(adminstateup).append(subnets)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof NetworkRepresentation) == false) {
			return false;
		}
		NetworkRepresentation rhs = ((NetworkRepresentation) other);
		return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name)
				.append(shared, rhs.shared).append(status, rhs.status)
				.append(adminstateup, rhs.adminstateup)
				.append(subnets, rhs.subnets)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}