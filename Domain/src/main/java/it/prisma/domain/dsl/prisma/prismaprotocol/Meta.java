package it.prisma.domain.dsl.prisma.prismaprotocol;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "status" })
public class Meta {

	@JsonProperty("status")
	private StatusType status;
	@JsonProperty("pagination")
	@JsonInclude(Include.NON_NULL)
	private Pagination pagination;
	@JsonIgnore
	@JsonInclude(Include.NON_EMPTY)
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("status")
	public long getStatus() {
		return status.getStatusCode();
	}

	@JsonProperty("status")
	public void setStatus(Integer statusCode) {
		this.status = Status.fromStatusCode(statusCode);
	}
	
	@JsonProperty("pagination")
	public Pagination getPagination() {
		return pagination;
	}
	
	@JsonProperty("pagination")
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
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
	 * 
	 * @return true, if the status is an error (not a Successful or
	 *         Informational {@link Family}).
	 */
	@JsonIgnore
	public boolean isError() {
		return !(status.getFamily() == Family.SUCCESSFUL || status.getFamily() == Family.INFORMATIONAL);
	}
}