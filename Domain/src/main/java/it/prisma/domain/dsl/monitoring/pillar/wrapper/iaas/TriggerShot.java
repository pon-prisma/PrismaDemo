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
	"triggerId",
	"description",
	"time", 
	"expression"
})
public class TriggerShot {

	@JsonProperty("triggerId")
	private String triggerId;
	@JsonProperty("description")
	private String description;
	@JsonProperty("time")
	private String time;
	@JsonProperty("expression")
	private String expression;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The triggerId
	 */
	@JsonProperty("triggerId")
	public String getTriggerId() {
		return triggerId;
	}

	/**
	 * 
	 * @param triggerId
	 * The triggerId
	 */
	@JsonProperty("triggerId")
	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

	/**
	 * 
	 * @return
	 * The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 * The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 * The time
	 */
	@JsonProperty("time")
	public String getTime() {
		return time;
	}

	/**
	 * 
	 * @param time
	 * The time
	 */
	@JsonProperty("time")
	public void setTime(String time) {
		this.time = time;
	}
	
	/**
	 * 
	 * @return
	 * The expression
	 */
	@JsonProperty("expression")
	public String getExpression() {
		return expression;
	}

	/**
	 * 
	 * @param expression
	 * The expression
	 */
	@JsonProperty("expression")
	public void setExpression(String expression) {
		this.expression = expression;
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
		return new HashCodeBuilder().append(triggerId).append(description).append(time).append(expression).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof TriggerShot) == false) {
			return false;
		}
		TriggerShot rhs = ((TriggerShot) other);
		return new EqualsBuilder().append(triggerId, rhs.triggerId).append(description, rhs.description).append(time, rhs.time).append(expression, rhs.expression).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}
}