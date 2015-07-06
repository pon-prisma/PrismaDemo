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
	"every_30s",
	"every_10mis",
	"every_30mins",
	"once_per_day_average"
})
public class FilterTime {

	@JsonProperty("every_30s")
	private Boolean every30s;
	@JsonProperty("every_10mis")
	private Boolean every10mis;
	@JsonProperty("every_30mins")
	private Boolean every30mins;
	@JsonProperty("once_per_day_average")
	private Boolean oncePerDayAverage;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The every30s
	 */
	@JsonProperty("every_30s")
	public Boolean getEvery30s() {
		return every30s;
	}

	/**
	 * 
	 * @param every30s
	 * The every_30s
	 */
	@JsonProperty("every_30s")
	public void setEvery30s(Boolean every30s) {
		this.every30s = every30s;
	}

	/**
	 * 
	 * @return
	 * The every10mis
	 */
	@JsonProperty("every_10mis")
	public Boolean getEvery10mis() {
		return every10mis;
	}

	/**
	 * 
	 * @param every10mis
	 * The every_10mis
	 */
	@JsonProperty("every_10mis")
	public void setEvery10mis(Boolean every10mis) {
		this.every10mis = every10mis;
	}

	/**
	 * 
	 * @return
	 * The every30mins
	 */
	@JsonProperty("every_30mins")
	public Boolean getEvery30mins() {
		return every30mins;
	}

	/**
	 * 
	 * @param every30mins
	 * The every_30mins
	 */
	@JsonProperty("every_30mins")
	public void setEvery30mins(Boolean every30mins) {
		this.every30mins = every30mins;
	}

	/**
	 * 
	 * @return
	 * The oncePerDayAverage
	 */
	@JsonProperty("once_per_day_average")
	public Boolean getOncePerDayAverage() {
		return oncePerDayAverage;
	}

	/**
	 * 
	 * @param oncePerDayAverage
	 * The once_per_day_average
	 */
	@JsonProperty("once_per_day_average")
	public void setOncePerDayAverage(Boolean oncePerDayAverage) {
		this.oncePerDayAverage = oncePerDayAverage;
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
		return new HashCodeBuilder().append(every30s).append(every10mis).append(every30mins).append(oncePerDayAverage).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof FilterTime) == false) {
			return false;
		}
		FilterTime rhs = ((FilterTime) other);
		return new EqualsBuilder().append(every30s, rhs.every30s).append(every10mis, rhs.every10mis).append(every30mins, rhs.every30mins).append(oncePerDayAverage, rhs.oncePerDayAverage).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}

}
