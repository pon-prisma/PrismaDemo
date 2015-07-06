package it.prisma.domain.dsl.monitoring.pillar.wrapper.paas;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "metricName", "metricKey", "metricValue", "metricTime",
		"metricStatus", "paasThresholds", "historyClocks", "historyValues" })
public class PaaSMetric {

	@JsonProperty("metricName")
	private String metricName;
	@JsonProperty("metricKey")
	private String metricKey;
	@JsonProperty("metricValue")
	private Float metricValue;
	@JsonProperty("metricTime")
	private Object metricTime;

	@JsonProperty("paasThresholds")
	private List<PaasThreshold> paasThresholds = new ArrayList<PaasThreshold>();
	@JsonProperty("historyClocks")
	private List<String> historyClocks = new ArrayList<String>();
	@JsonProperty("historyValues")
	private List<Float> historyValues = new ArrayList<>();

//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The metricName
	 */
	@JsonProperty("metricName")
	public String getMetricName() {
		return metricName;
	}

	/**
	 * 
	 * @param metricName
	 *            The metricName
	 */
	@JsonProperty("metricName")
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	/**
	 * 
	 * @return The metricKey
	 */
	@JsonProperty("metricKey")
	public String getMetricKey() {
		return metricKey;
	}

	/**
	 * 
	 * @param metricKey
	 *            The metricKey
	 */
	@JsonProperty("metricKey")
	public void setMetricKey(String metricKey) {
		this.metricKey = metricKey;
	}

	/**
	 * 
	 * @return The metricValue
	 */
	@JsonProperty("metricValue")
	public Float getMetricValue() {
		return metricValue;
	}

	/**
	 * 
	 * @param metricValue
	 *            The metricValue
	 */
	@JsonProperty("metricValue")
	public void setMetricValue(Float metricValue) {
		this.metricValue = metricValue;
	}

	/**
	 * 
	 * @return The metricTime
	 */
	@JsonProperty("metricTime")
	public Object getMetricTime() {
		return metricTime;
	}

	/**
	 * 
	 * @param metricInstant
	 *            The metricInstant
	 */
	@JsonProperty("metricTime")
	public void setMetricTime(Object metricTime) {
		this.metricTime = metricTime;
	}


	/**
	 * 
	 * @return The paasThresholds
	 */
	@JsonProperty("paasThresholds")
	public List<PaasThreshold> getPaasThresholds() {
		return paasThresholds;
	}

	/**
	 * 
	 * @param paasThresholdsList
	 *            The paasThresholdsList
	 */
	@JsonProperty("paasThresholds")
	public void setPaasThresholds(
			List<PaasThreshold> paasThresholds) {
		this.paasThresholds = paasThresholds;
	}

	// USEFUL FOR HISTORY and creating GRAPHS

	/**
	 * 
	 * @return The historyClocks
	 */
	@JsonProperty("historyClocks")
	public List<String> getHistoryClocks() {
		return historyClocks;
	}

	/**
	 * 
	 * @param historyClocks
	 *            The historyClocks
	 */
	@JsonProperty("historyClocks")
	public void setHistoryClock(List<String> historyClocks) {
		this.historyClocks = historyClocks;
	}

	/**
	 * 
	 * @return The historyValues
	 */
	@JsonProperty("historyValues")
	public List<Float> getHistoryValues() {
		return historyValues;
	}

	/**
	 * 
	 * @param historyValues
	 *            The historyValues
	 */
	@JsonProperty("historyValues")
	public void setHistoryValues(List<Float> historyValues) {
		this.historyValues = historyValues;
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
		return new HashCodeBuilder().append(metricName).append(metricKey)
				.append(paasThresholds).append(historyClocks)
				.append(historyValues).// append(historyTime_ns).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof PaaSMetric) == false) {
			return false;
		}
		PaaSMetric rhs = ((PaaSMetric) other);
		return new EqualsBuilder().append(metricName, rhs.metricName)
				.append(metricKey, rhs.metricKey)
				.append(metricValue, rhs.metricValue)
				.append(metricTime, rhs.metricTime)
				.append(paasThresholds, rhs.paasThresholds)
				.append(historyClocks, rhs.historyClocks)
				.append(historyValues, rhs.historyValues)
				.// append(historyTime_ns, rhs.historyTime_ns).
//				append(additionalProperties, rhs.additionalProperties)
				isEquals();
	}
}