package it.prisma.presentationlayer.webui.vos.paas.appaas;

import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@JsonPropertyOrder({ "total", "environments" })
public class Environments {
	@JsonProperty("total")
	private int total;
	@JsonProperty("rows")
	private List<APPaaSEnvironmentRepresentation> rows = new ArrayList<APPaaSEnvironmentRepresentation>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The total
	 */
	@JsonProperty("total")
	public int getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 *            The total
	 */
	@JsonProperty("total")
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * 
	 * @return The rows
	 */
	@JsonProperty("rows")
	public List<APPaaSEnvironmentRepresentation> getRows() {
		return rows;
	}

	/**
	 * 
	 * @param rows
	 *            The rows
	 */
	@JsonProperty("rows")
	public void setEnvironments(List<APPaaSEnvironmentRepresentation> rows) {
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
	
}