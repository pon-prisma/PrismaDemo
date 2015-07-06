package it.prisma.domain.dsl.monitoring.webui;

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
	"values",
	"dates"
})
public class GraphValueResponse {

	@JsonProperty("values")
	private List<String> values = new ArrayList<String>();
	@JsonProperty("dates")
	private List<String> dates = new ArrayList<String>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The values
	 */
	@JsonProperty("values")
	public List<String> getValues() {
		return values;
	}

	/**
	 * 
	 * @param values
	 * The values
	 */
	@JsonProperty("values")
	public void setValues(List<String> values) {
		this.values = values;
	}

	/**
	 * 
	 * @return
	 * The dates
	 */
	@JsonProperty("dates")
	public List<String> getDates() {
		return dates;
	}

	/**
	 * 
	 * @param dates
	 * The dates
	 */
	@JsonProperty("dates")
	public void setDates(List<String> dates) {
		this.dates = dates;
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
		return new HashCodeBuilder().append(values).append(dates).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof GraphValueResponse) == false) {
			return false;
		}
		GraphValueResponse rhs = ((GraphValueResponse) other);
		return new EqualsBuilder().append(values, rhs.values).append(dates, rhs.dates).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}
