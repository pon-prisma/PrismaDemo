package it.prisma.domain.dsl.cloudify.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A POJO representing a generic response from the REST Gateway. This class is a
 * wrapper for an inner specific response.
 * 
 * @author elip
 * 
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "status", "message", "messageId", "verbose", "response" })
public class ResponseWrapper<T> implements Serializable {

	private static final long serialVersionUID = 5743416544105034494L;

	@JsonProperty("status")
	private String status;
	@JsonProperty("message")
	private String message;
	@JsonProperty("messageId")
	private String messageId;
	@JsonProperty("verbose")
	private Object verbose;
	@JsonProperty("response")
	private T response;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("messageId")
	public String getMessageId() {
		return messageId;
	}

	@JsonProperty("messageId")
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@JsonProperty("verbose")
	public Object getVerbose() {
		return verbose;
	}

	@JsonProperty("verbose")
	public void setVerbose(Object verbose) {
		this.verbose = verbose;
	}

	@JsonProperty("response")
	public T getResponse() {
		return response;
	}

	@JsonProperty("response")
	public void setResponse(final T response) {
		this.response = response;
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

}
