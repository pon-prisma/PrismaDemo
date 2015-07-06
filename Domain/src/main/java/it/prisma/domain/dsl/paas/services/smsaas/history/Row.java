package it.prisma.domain.dsl.paas.services.smsaas.history;

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
@JsonPropertyOrder({ "date", "recipient", "sender", "text" })
public class Row {

	@JsonProperty("date")
	private String date;
	@JsonProperty("recipient")
	private String recipient;
	@JsonProperty("sender")
	private String sender;
	@JsonProperty("text")
	private String text;
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
	 * @return The recipient
	 */
	@JsonProperty("recipient")
	public String getRecipient() {
		return recipient;
	}

	/**
	 * 
	 * @param sender
	 *            The sender
	 */
	@JsonProperty("sender")
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	/**
	 * 
	 * @return The sender
	 */
	@JsonProperty("sender")
	public String getSender() {
		return sender;
	}

	/**
	 * 
	 * @param recipient
	 *            The recipient
	 */
	@JsonProperty("recipient")
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * 
	 * @return The text
	 */
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	/**
	 * 
	 * @param text
	 *            The text
	 */
	@JsonProperty("text")
	public void setText(String text) {
		this.text = text;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((recipient == null) ? 0 : recipient.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Row other = (Row) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (recipient == null) {
			if (other.recipient != null)
				return false;
		} else if (!recipient.equals(other.recipient))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	
}