package it.prisma.domain.dsl.paas.services.smsaas;
/**
 * This class is used to conserve the status of a user(exist, active and disable)
 * @author l.calicchio
 */


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
	@JsonPropertyOrder({
	"status"
	})
	public enum IsActiveStatus {
		SERVICE_ON ("Sms service on"),
		SERVICE_OFF("Sms service off"),
		NOT_FOUND_USER("User not found");
	
		
		
	@JsonProperty("status")
	private final String status;
	
	private IsActiveStatus(String status){
		this.status=status;
	}
		
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("status")
	public String getStatus() {
	return status;
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
