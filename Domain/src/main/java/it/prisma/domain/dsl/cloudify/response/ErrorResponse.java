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
 * This POJO is a mapping for the generic errore response from REST Gateway.
 * 
 * @author l.biava
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "message", "response", "status", "verbose", "messageId" })
public class ErrorResponse implements Serializable {

	private static final long serialVersionUID = 4548028802742034757L;

	@JsonProperty("message")
	private String message;
	@JsonProperty("response")
	private Object response;
	@JsonProperty("status")
	private String status;
	@JsonProperty("verbose")
	private String verbose;
	@JsonProperty("messageId")
	private String messageId;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("response")
	public Object getResponse() {
		return response;
	}

	@JsonProperty("response")
	public void setResponse(Object response) {
		this.response = response;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("verbose")
	public String getVerbose() {
		return verbose;
	}

	@JsonProperty("verbose")
	public void setVerbose(String verbose) {
		this.verbose = verbose;
	}

	@JsonProperty("messageId")
	public String getMessageId() {
		return messageId;
	}

	@JsonProperty("messageId")
	public void setMessageId(String messageId) {
		this.messageId = messageId;
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