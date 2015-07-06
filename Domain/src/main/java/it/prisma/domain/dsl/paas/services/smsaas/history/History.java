package it.prisma.domain.dsl.paas.services.smsaas.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@JsonPropertyOrder({ "total", "rows" })
public class History {

	@JsonProperty("total")
	private Long total;
	@JsonProperty("rows")
	private List<Row> rows = new ArrayList<Row>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The total
	 */
	@JsonProperty("total")
	public Long getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 *            The total
	 */
	@JsonProperty("total")
	public void setTotal(Long total) {
		this.total = total;
	}

	/**
	 * 
	 * @return The rows
	 */
	@JsonProperty("rows")
	public List<Row> getRows() {
		return rows;
	}

	/**
	 * 
	 * @param rows
	 *            The rows
	 */
	@JsonProperty("rows")
	public void setRows(List<Row> rows) {
		this.rows = rows;
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
		return new HashCodeBuilder().append(total).append(rows)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof History) == false) {
			return false;
		}
		History rhs = ((History) other);
		return new EqualsBuilder().append(total, rhs.total)
				.append(rows, rhs.rows)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}