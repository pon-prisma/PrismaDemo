package it.prisma.domain.dsl.orchestrator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
@JsonPropertyOrder({ "instanceId", "name", "userAccountId", "status",
		"createdAt", "completedAt", "events", "result", "lrtid" })
public class LRTRepresentation {

	@JsonProperty("instanceId")
	private long instanceId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("userAccountId")
	private long userAccountId;
	@JsonProperty("status")
	private String status;
	@JsonProperty("createdAt")
	private long createdAt;
	@JsonProperty("completedAt")
	private long completedAt;
	@JsonProperty("events")
	private Set<LRTEventRepresentation> events = new HashSet<LRTEventRepresentation>();
	@JsonProperty("result")
	private LRTEventRepresentation result;
	@JsonProperty("lrtid")
	private long lrtid;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("instanceId")
	public long getInstanceId() {
		return instanceId;
	}

	@JsonProperty("instanceId")
	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("userAccountId")
	public long getuserAccountId() {
		return userAccountId;
	}

	@JsonProperty("userAccountId")
	public void setuserAccountId(long userAccountId) {
		this.userAccountId = userAccountId;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("createdAt")
	public long getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("createdAt")
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("completedAt")
	public long getCompletedAt() {
		return completedAt;
	}

	@JsonProperty("completedAt")
	public void setCompletedAt(long completedAt) {
		this.completedAt = completedAt;
	}

	@JsonProperty("events")
	public Set<LRTEventRepresentation> getEvents() {
		return events;
	}

	@JsonProperty("events")
	public void setEvents(Set<LRTEventRepresentation> events) {
		this.events = events;
	}

	@JsonProperty("result")
	public LRTEventRepresentation getResult() {
		return result;
	}

	@JsonProperty("result")
	public void setResult(LRTEventRepresentation result) {
		this.result = result;
	}

	@JsonProperty("lrtid")
	public long getLrtid() {
		return lrtid;
	}

	@JsonProperty("lrtid")
	public void setLrtid(long lrtid) {
		this.lrtid = lrtid;
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