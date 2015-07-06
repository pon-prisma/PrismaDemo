package it.prisma.domain.dsl.accounting.idp;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "error", "status" })
public class Meta {

	@JsonProperty("error")
	private Boolean error;
	@JsonProperty("status")
	private Long status;
//	@JsonIgnore
	@JsonProperty("additionalProperties")
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The error
	 */
	@JsonProperty("error")
	public Boolean getError() {
		return error;
	}

	/**
	 * 
	 * @param error
	 *            The error
	 */
	@JsonProperty("error")
	public void setError(Boolean error) {
		this.error = error;
	}

	public Meta withError(Boolean error) {
		this.error = error;
		return this;
	}

	/**
	 * 
	 * @return The status
	 */
	@JsonProperty("status")
	public Long getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            The status
	 */
	@JsonProperty("status")
	public void setStatus(Long status) {
		this.status = status;
	}

	public Meta withStatus(Long status) {
		this.status = status;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

//	@JsonAnyGetter
	@JsonProperty("additionalProperties")
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

//	@JsonAnySetter
	@JsonProperty("additionalProperties")
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public Meta withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

}