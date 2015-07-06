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
	"triggerExpression",
	"triggerKey",
	"triggerStatus",
	"triggerValue"
})
public class IaasThresholdsList {

	@JsonProperty("triggerExpression")
	private String triggerExpression;
	@JsonProperty("triggerKey")
	private String triggerKey;
	@JsonProperty("triggerStatus")
	private String triggerStatus;
	@JsonProperty("triggerValue")
	private String triggerValue;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The triggerExpression
	 */
	@JsonProperty("triggerExpression")
	public String getTriggerExpression() {
		return triggerExpression;
	}

	/**
	 * 
	 * @param triggerExpression
	 * The triggerExpression
	 */
	@JsonProperty("triggerExpression")
	public void setTriggerExpression(String triggerExpression) {
		this.triggerExpression = triggerExpression;
	}

	/**
	 * 
	 * @return
	 * The triggerKey
	 */
	@JsonProperty("triggerKey")
	public String getTriggerKey() {
		return triggerKey;
	}

	/**
	 * 
	 * @param triggerKey
	 * The triggerKey
	 */
	@JsonProperty("triggerKey")
	public void setTriggerKey(String triggerKey) {
		this.triggerKey = triggerKey;
	}

	/**
	 * 
	 * @return
	 * The triggerStatus
	 */
	@JsonProperty("triggerStatus")
	public String getTriggerStatus() {
		return triggerStatus;
	}

	/**
	 * 
	 * @param triggerStatus
	 * The triggerStatus
	 */
	@JsonProperty("triggerStatus")
	public void setTriggerStatus(String triggerStatus) {
		this.triggerStatus = triggerStatus;
	}

	/**
	 * 
	 * @return
	 * The triggerValue
	 */
	@JsonProperty("triggerValue")
	public String getTriggerValue() {
		return triggerValue;
	}

	/**
	 * 
	 * @param triggerValue
	 * The triggerValue
	 */
	@JsonProperty("triggerValue")
	public void setTriggerValue(String triggerValue) {
		this.triggerValue = triggerValue;
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
//
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(triggerExpression).append(triggerKey).append(triggerStatus).append(triggerValue).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof IaasThresholdsList) == false) {
			return false;
		}
		IaasThresholdsList rhs = ((IaasThresholdsList) other);
		return new EqualsBuilder().append(triggerExpression, rhs.triggerExpression).append(triggerKey, rhs.triggerKey).append(triggerStatus, rhs.triggerStatus).append(triggerValue, rhs.triggerValue).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}
}