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
	"year",
	"month",
	"day"
})
public class DateFrom {

	@JsonProperty("year")
	private String year;
	@JsonProperty("month")
	private String month;
	@JsonProperty("day")
	private String day;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The year
	 */
	@JsonProperty("year")
	public String getYear() {
		return year;
	}

	/**
	 * 
	 * @param year
	 * The year
	 */
	@JsonProperty("year")
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * 
	 * @return
	 * The month
	 */
	@JsonProperty("month")
	public String getMonth() {
		return month;
	}

	/**
	 * 
	 * @param month
	 * The month
	 */
	@JsonProperty("month")
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * 
	 * @return
	 * The day
	 */
	@JsonProperty("day")
	public String getDay() {
		return day;
	}

	/**
	 * 
	 * @param day
	 * The day
	 */
	@JsonProperty("day")
	public void setDay(String day) {
		this.day = day;
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
		return new HashCodeBuilder().append(year).append(month).append(day).
//				append(additionalProperties).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof DateFrom) == false) {
			return false;
		}
		DateFrom rhs = ((DateFrom) other);
		return new EqualsBuilder().append(year, rhs.year).append(month, rhs.month).append(day, rhs.day).
//				append(additionalProperties, rhs.additionalProperties).
				isEquals();
	}
}