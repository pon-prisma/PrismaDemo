package it.prisma.domain.dsl.orchestrator;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "eventType", "createdAt", "data", "eventId", "lrtId" })
public class LRTEventRepresentation {

	@JsonProperty("eventType")
	private String eventType;
	@JsonProperty("createdAt")
	private Long createdAt;
	@JsonProperty("data")
	private Object data;
	@JsonProperty("eventId")
	private Long eventId;
	@JsonProperty("lrtId")
	private Long lrtId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("eventType")
	public String getEventType() {
		return eventType;
	}

	@JsonProperty("eventType")
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@JsonProperty("createdAt")
	public Long getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("createdAt")
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("data")
	public Object getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(Object data) {
		this.data = data;
	}

	@JsonProperty("eventId")
	public Long getEventId() {
		return eventId;
	}

	@JsonProperty("eventId")
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	@JsonProperty("lrtId")
	public Long getLRTId() {
		return lrtId;
	}

	@JsonProperty("lrtId")
	public void setLRTId(Long lrtId) {
		this.lrtId = lrtId;
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

}