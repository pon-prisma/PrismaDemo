package it.prisma.domain.dsl.paas.services.emailaas;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"sender",
	"password",
	"recipient",
	"subject",
	"body"
})
public class EmailMessageRepresentation {

	@JsonProperty("sender")
	private String sender;
	@JsonProperty("password")
	private String password;
	@JsonProperty("recipient")
	private String recipient;
	@JsonProperty("subject")
	private String subject;
	@JsonProperty("body")
	private String body;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The sender
	 */
	@JsonProperty("sender")
	public String getSender() {
		return sender;
	}

	/**
	 * 
	 * @param sender
	 * The sender
	 */
	@JsonProperty("sender")
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * 
	 * @return
	 * The password
	 */
	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password
	 * The password
	 */
	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return
	 * The recipient
	 */
	@JsonProperty("recipient")
	public String getRecipient() {
		return recipient;
	}

	/**
	 * 
	 * @param recipient
	 * The recipient
	 */
	@JsonProperty("recipient")
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * 
	 * @return
	 * The subject
	 */
	@JsonProperty("subject")
	public String getSubject() {
		return subject;
	}

	/**
	 * 
	 * @param subject
	 * The subject
	 */
	@JsonProperty("subject")
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 
	 * @return
	 * The body
	 */
	@JsonProperty("body")
	public String getBody() {
		return body;
	}

	/**
	 * 
	 * @param body
	 * The body
	 */
	@JsonProperty("body")
	public void setBody(String body) {
		this.body = body;
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
		return new HashCodeBuilder().append(sender).append(password).append(recipient).append(subject).append(body).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof EmailMessageRepresentation) == false) {
			return false;
		}
		EmailMessageRepresentation rhs = ((EmailMessageRepresentation) other);
		return new EqualsBuilder().append(sender, rhs.sender).append(password, rhs.password).append(recipient, rhs.recipient).append(subject, rhs.subject).append(body, rhs.body).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}
