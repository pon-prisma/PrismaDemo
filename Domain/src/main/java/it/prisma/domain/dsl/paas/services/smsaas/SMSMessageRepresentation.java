package it.prisma.domain.dsl.paas.services.smsaas;

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
	"password",
	"text",
	"num",
	"scheduling",
	"sendDate",
	"from"
})
public class SMSMessageRepresentation {

	@JsonProperty("password")
	private String password;
	@JsonProperty("text")
	private String text;
	@JsonProperty("num")
	private String num;
	@JsonProperty("scheduling")
	private Boolean scheduling;
	@JsonProperty("sendDate")
	private String sendDate;
	@JsonProperty("from")
	private String from;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
	 * The text
	 */
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	/**
	 * 
	 * @param text
	 * The text
	 */
	@JsonProperty("text")
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 
	 * @return
	 * The num
	 */
	@JsonProperty("num")
	public String getNum() {
		return num;
	}

	/**
	 * 
	 * @param num
	 * The num
	 */
	@JsonProperty("num")
	public void setNum(String num) {
		this.num = num;
	}

	/**
	 * 
	 * @return
	 * The scheduling
	 */
	@JsonProperty("scheduling")
	public Boolean getScheduling() {
		return scheduling;
	}

	/**
	 * 
	 * @param scheduling
	 * The scheduling
	 */
	@JsonProperty("scheduling")
	public void setScheduling(Boolean scheduling) {
		this.scheduling = scheduling;
	}

	/**
	 * 
	 * @return
	 * The sendDate
	 */
	@JsonProperty("sendDate")
	public String getSendDate() {
		return sendDate;
	}

	/**
	 * 
	 * @param sendDate
	 * The sendDate
	 */
	@JsonProperty("sendDate")
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * 
	 * @return
	 * The from
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	/**
	 * 
	 * @param from
	 * The from
	 */
	@JsonProperty("from")
	public void setFrom(String from) {
		this.from = from;
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
		return new HashCodeBuilder().append(password).append(text).append(num).append(scheduling).append(sendDate).append(from).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof SMSMessageRepresentation) == false) {
			return false;
		}
		SMSMessageRepresentation rhs = ((SMSMessageRepresentation) other);
		return new EqualsBuilder().append(password, rhs.password).append(text, rhs.text).append(num, rhs.num).append(scheduling, rhs.scheduling).append(sendDate, rhs.sendDate).append(from, rhs.from).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}