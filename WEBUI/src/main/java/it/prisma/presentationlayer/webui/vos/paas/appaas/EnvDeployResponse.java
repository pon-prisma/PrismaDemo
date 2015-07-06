package it.prisma.presentationlayer.webui.vos.paas.appaas;

import java.util.HashMap;
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
@JsonPropertyOrder({ "name", "appId", "envId" })
public class EnvDeployResponse {

	@JsonProperty("name")
	private String name;
	@JsonProperty("appId")
	private Long appId;
	@JsonProperty("envId")
	private Long envId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
	 * @return The appId
	 */
	@JsonProperty("appId")
	public Long getAppId() {
		return appId;
	}

	/**
	 * 
	 * @param appId
	 *            The appId
	 */
	@JsonProperty("appId")
	public void setAppId(Long appId) {
		this.appId = appId;
	}

	/**
	 * 
	 * @return The envId
	 */
	@JsonProperty("envId")
	public Long getEnvId() {
		return envId;
	}

	/**
	 * 
	 * @param envId
	 *            The envId
	 */
	@JsonProperty("envId")
	public void setEnvId(Long envId) {
		this.envId = envId;
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
		return new HashCodeBuilder().append(name).append(appId).append(envId)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof EnvDeployResponse) == false) {
			return false;
		}
		EnvDeployResponse rhs = ((EnvDeployResponse) other);
		return new EqualsBuilder().append(name, rhs.name)
				.append(appId, rhs.appId).append(envId, rhs.envId)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}
