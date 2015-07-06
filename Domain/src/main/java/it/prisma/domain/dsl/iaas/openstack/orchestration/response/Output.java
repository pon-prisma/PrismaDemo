package it.prisma.domain.dsl.iaas.openstack.orchestration.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

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
@JsonPropertyOrder({ "output_value", "description", "output_key" })
public class Output {

	@JsonProperty("output_value")
	private String outputValue;
	@JsonProperty("description")
	private String description;
	@JsonProperty("output_key")
	private String outputKey;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Output() {
	}

	public Output(String key) {
		this.outputKey = key;
	}

	@JsonProperty("output_value")
	public String getOutputValue() {
		return outputValue;
	}

	@JsonProperty("output_value")
	public void setOutputValue(String outputValue) {
		this.outputValue = outputValue;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("output_key")
	public String getOutputKey() {
		return outputKey;
	}

	@JsonProperty("output_key")
	public void setOutputKey(String outputKey) {
		this.outputKey = outputKey;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * Comparison using Output Key !
	 */
	@Override
	public boolean equals(Object other) {
		return (other instanceof Output ? ((Output) other).getOutputKey()
				.equals(outputKey) : false);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	/**
	 * Converts the given List into a Map containing the same elements, indexed by OutputKey.
	 * @param list the list of Output entries.
	 * @return the map.
	 */
	@JsonIgnore
	public static Map<String, Output> listToMap(List<Output> list) {
		Map<String, Output> map = new HashMap<String, Output>();
		for (Output item : list) {
			map.put(item.getOutputKey(), item);
		}
		return map;
	}
}