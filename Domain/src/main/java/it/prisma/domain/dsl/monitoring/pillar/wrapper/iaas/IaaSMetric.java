package it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas;

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
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"metricName",
	"metricValue",
	"metricStatus",
	"metricTime",
	"iaasThresholds",
	"metricKey"
})
public class IaaSMetric {

	@JsonProperty("metricName")
	private String metricName;
	@JsonProperty("metricValue")
	private Object metricValue;
	@JsonProperty("metricStatus")
	private String metricStatus;
	@JsonProperty("metricTime")
	private String metricTime;
	@JsonProperty("iaasThresholds")
	private List<IaasThresholdsList> iaasThresholds = new ArrayList<>();
	@JsonProperty("metricKey")
	private String metricKey;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The metricName
	 */
	@JsonProperty("metricName")
	public String getMetricName() {
		return metricName;
	}

	/**
	 * 
	 * @param metricName
	 * The metricName
	 */
	@JsonProperty("metricName")
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	/**
	 * 
	 * @return
	 * The metricValue
	 */
	@JsonProperty("metricValue")
	public Object getMetricValue() {
		return metricValue;
	}

	/**
	 * 
	 * @param metricValue
	 * The metricValue
	 */
	@JsonProperty("metricValue")
	public void setMetricValue(Object metricValue) {
		this.metricValue = metricValue;
	}

	/**
	 * 
	 * @return
	 * The metricStatus
	 */
	@JsonProperty("metricStatus")
	public String getMetricStatus() {
		return metricStatus;
	}

	/**
	 * 
	 * @param metricStatus
	 * The metricStatus
	 */
	@JsonProperty("metricStatus")
	public void setMetricStatus(String metricStatus) {
		this.metricStatus = metricStatus;
	}

	/**
	 * 
	 * @return
	 * The metricTime
	 */
	@JsonProperty("metricTime")
	public String getMetricTime() {
		return metricTime;
	}

	/**
	 * 
	 * @param metricTime
	 * The metricTime
	 */
	@JsonProperty("metricTime")
	public void setMetricTime(String metricTime) {
		this.metricTime = metricTime;
	}

	/**
	 * 
	 * @return
	 * The iaasThresholdsList
	 */
	@JsonProperty("iaasThresholds")
	public List<IaasThresholdsList> getIaasThresholds() {
		return iaasThresholds;
	}

	/**
	 * 
	 * @param iaasThresholds
	 * The iaasThresholdsList
	 */
	@JsonProperty("iaasThresholds")
	public void setIaasThresholds(List<IaasThresholdsList> iaasThresholds) {
		this.iaasThresholds = iaasThresholds;
	}

	/**
	 * 
	 * @return
	 * The metricKey
	 */
	@JsonProperty("metricKey")
	public String getMetricKey() {
		return metricKey;
	}

	/**
	 * 
	 * @param metricKey
	 * The metricKey
	 */
	@JsonProperty("metricKey")
	public void setMetricKey(String metricKey) {
		this.metricKey = metricKey;
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
		return new HashCodeBuilder().append(metricName).append(metricValue).append(metricStatus).append(metricTime).append(iaasThresholds).append(metricKey).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof IaaSMetric) == false) {
			return false;
		}
		IaaSMetric rhs = ((IaaSMetric) other);
		return new EqualsBuilder().append(metricName, rhs.metricName).append(metricValue, rhs.metricValue).append(metricStatus, rhs.metricStatus).append(metricTime, rhs.metricTime).append(iaasThresholds, rhs.iaasThresholds).append(metricKey, rhs.metricKey).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}

}