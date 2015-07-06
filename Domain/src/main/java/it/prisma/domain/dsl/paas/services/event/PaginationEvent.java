package it.prisma.domain.dsl.paas.services.event;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "date", "level", "description" })
public class PaginationEvent {

	@JsonProperty("date")
	private String date;
	@JsonProperty("level")
	private String level;
	@JsonProperty("description")
	private String description;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The date
	 */
	@JsonProperty("date")
	public String getDate() {
		return date;
	}

	/**
	 * 
	 * @param date
	 *            The date
	 */
	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 
	 * @return The level
	 */
	@JsonProperty("level")
	public String getLevel() {
		return level;
	}

	/**
	 * 
	 * @param level
	 *            The level
	 */
	@JsonProperty("level")
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * 
	 * @return The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
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

}