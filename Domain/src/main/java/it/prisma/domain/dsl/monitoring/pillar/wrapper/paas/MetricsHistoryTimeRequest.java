package it.prisma.domain.dsl.monitoring.pillar.wrapper.paas;

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
	"date_from",
	"date_To",
	"filter_time",
	"warn"
})
public class MetricsHistoryTimeRequest {

	@JsonProperty("date_from")
	private DateFrom dateFrom;
	@JsonProperty("date_To")
	private DateTo dateTo;
	@JsonProperty("filter_time")
	private FilterTime filterTime;
	@JsonProperty("warn")
	private String warn;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The dateFrom
	 */
	@JsonProperty("date_from")
	public DateFrom getDateFrom() {
		return dateFrom;
	}

	/**
	 * 
	 * @param dateFrom
	 * The date_from
	 */
	@JsonProperty("date_from")
	public void setDateFrom(DateFrom dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * 
	 * @return
	 * The dateTo
	 */
	@JsonProperty("date_To")
	public DateTo getDateTo() {
		return dateTo;
	}

	/**
	 * 
	 * @param dateTo
	 * The date_To
	 */
	@JsonProperty("date_To")
	public void setDateTo(DateTo dateTo) {
		this.dateTo = dateTo;
	}

	/**
	 * 
	 * @return
	 * The filterTime
	 */
	@JsonProperty("filter_time")
	public FilterTime getFilterTime() {
		return filterTime;
	}

	/**
	 * 
	 * @param filterTime
	 * The filter_time
	 */
	@JsonProperty("filter_time")
	public void setFilterTime(FilterTime filterTime) {
		this.filterTime = filterTime;
	}
	
	/**
	 * 
	 * @return
	 * The warn message
	 */
	@JsonProperty("warn")
	public String getWarn() {
		return warn;
	}

	/**
	 * 
	 * @param warn
	 * The warn message
	 */
	@JsonProperty("warn")
	public void setWarn(String warn) {
		this.warn = warn;
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
		return new HashCodeBuilder().append(dateFrom).append(dateTo).append(filterTime).append(warn).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof MetricsHistoryTimeRequest) == false) {
			return false;
		}
		MetricsHistoryTimeRequest rhs = ((MetricsHistoryTimeRequest) other);
		return new EqualsBuilder().append(dateFrom, rhs.dateFrom).append(dateTo, rhs.dateTo).append(filterTime, rhs.filterTime).append(warn, rhs.warn).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}

}
