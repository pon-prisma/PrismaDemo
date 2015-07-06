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
	"isDaily",
	"isMonth",
	"isDailySms",
	"isMonthSms",
	"isDailyEmail",
	"isMonthEmail",
	"dailySms",
	"monthSms",
	"dailyEmail",
	"monthEmail",
	"thresholdDay",
	"thresholdMonth"
})
public class SMSNotificationsRepresentation {

	@JsonProperty("isDaily")
	private boolean isDaily;
	@JsonProperty("isMonth")
	private boolean isMonth;
	@JsonProperty("isDailySms")
	private boolean isDailySms;
	@JsonProperty("isMonthSms")
	private boolean isMonthSms;
	@JsonProperty("isDailyEmail")
	private boolean isDailyEmail;
	@JsonProperty("isMonthEmail")
	private boolean isMonthEmail;
	@JsonProperty("dailySms")
	private String dailySms;
	@JsonProperty("monthSms")
	private String monthSms;
	@JsonProperty("dailyEmail")
	private String dailyEmail;
	@JsonProperty("monthEmail")
	private String monthEmail;
	@JsonProperty("thresholdDay")
	private int thresholdDay;
	@JsonProperty("thresholdMonth")
	private int thresholdMonth;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The isDaily
	 */
	@JsonProperty("isDaily")
	public boolean getIsDaily() {
		return isDaily;
	}

	/**
	 * 
	 * @param isDaily
	 * The isDaily
	 */
	@JsonProperty("isDaily")
	public void setIsDaily(boolean isDaily) {
		this.isDaily = isDaily;
	}

	/**
	 * 
	 * @return
	 * The isMonth
	 */
	@JsonProperty("isMonth")
	public boolean getIsMonth() {
		return isMonth;
	}

	/**
	 * 
	 * @param isMonth
	 * The isMonth
	 */
	@JsonProperty("isMonth")
	public void setIsMonth(boolean isMonth) {
		this.isMonth = isMonth;
	}

	/**
	 * 
	 * @return
	 * The isDailySms
	 */
	@JsonProperty("isDailySms")
	public boolean getIsDailySms() {
		return isDailySms;
	}

	/**
	 * 
	 * @param isDailySms
	 * The isDailySms
	 */
	@JsonProperty("isDailySms")
	public void setIsDailySms(boolean isDailySms) {
		this.isDailySms = isDailySms;
	}

	/**
	 * 
	 * @return
	 * The isMonthSms
	 */
	@JsonProperty("isMonthSms")
	public boolean getIsMonthSms() {
		return isMonthSms;
	}

	/**
	 * 
	 * @param isMonthSms
	 * The isMonthSms
	 */
	@JsonProperty("isMonthSms")
	public void setIsMonthSms(boolean isMonthSms) {
		this.isMonthSms = isMonthSms;
	}

	/**
	 * 
	 * @return
	 * The isDailyEmail
	 */
	@JsonProperty("isDailyEmail")
	public boolean getIsDailyEmail() {
		return isDailyEmail;
	}

	/**
	 * 
	 * @param isDailyEmail
	 * The isDailyEmail
	 */
	@JsonProperty("isDailyEmail")
	public void setIsDailyEmail(boolean isDailyEmail) {
		this.isDailyEmail = isDailyEmail;
	}

	/**
	 * 
	 * @return
	 * The isMonthEmail
	 */
	@JsonProperty("isMonthEmail")
	public boolean getIsMonthEmail() {
		return isMonthEmail;
	}

	/**
	 * 
	 * @param isMonthEmail
	 * The isMonthEmail
	 */
	@JsonProperty("isMonthEmail")
	public void setIsMonthEmail(boolean isMonthEmail) {
		this.isMonthEmail = isMonthEmail;
	}

	/**
	 * 
	 * @return
	 * The dailySms
	 */
	@JsonProperty("dailySms")
	public String getDailySms() {
		return dailySms;
	}

	/**
	 * 
	 * @param dailySms
	 * The dailySms
	 */
	@JsonProperty("dailySms")
	public void setDailySms(String dailySms) {
		this.dailySms = dailySms;
	}

	/**
	 * 
	 * @return
	 * The monthSms
	 */
	@JsonProperty("monthSms")
	public String getMonthSms() {
		return monthSms;
	}

	/**
	 * 
	 * @param monthSms
	 * The monthSms
	 */
	@JsonProperty("monthSms")
	public void setMonthSms(String monthSms) {
		this.monthSms = monthSms;
	}

	/**
	 * 
	 * @return
	 * The dailyEmail
	 */
	@JsonProperty("dailyEmail")
	public String getDailyEmail() {
		return dailyEmail;
	}

	/**
	 * 
	 * @param dailyEmail
	 * The dailyEmail
	 */
	@JsonProperty("dailyEmail")
	public void setDailyEmail(String dailyEmail) {
		this.dailyEmail = dailyEmail;
	}

	/**
	 * 
	 * @return
	 * The monthEmail
	 */
	@JsonProperty("monthEmail")
	public String getMonthEmail() {
		return monthEmail;
	}

	/**
	 * 
	 * @param monthEmail
	 * The monthEmail
	 */
	@JsonProperty("monthEmail")
	public void setMonthEmail(String monthEmail) {
		this.monthEmail = monthEmail;
	}

	/**
	 * 
	 * @return
	 * The thresholdDay
	 */
	@JsonProperty("thresholdDay")
	public int getThresholdDay() {
		return thresholdDay;
	}

	/**
	 * 
	 * @param thresholdDay
	 * The threshold_day
	 */
	@JsonProperty("thresholdDay")
	public void setThresholdDay(int thresholdDay) {
		this.thresholdDay = thresholdDay;
	}

	/**
	 * 
	 * @return
	 * The thresholdMonth
	 */
	@JsonProperty("thresholdMonth")
	public int getThresholdMonth() {
		return thresholdMonth;
	}

	/**
	 * 
	 * @param thresholdMonth
	 * The threshold_month
	 */
	@JsonProperty("thresholdMonth")
	public void setThresholdMonth(int thresholdMonth) {
		this.thresholdMonth = thresholdMonth;
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
		return new HashCodeBuilder().append(isDaily).append(isMonth).append(isDailySms).append(isMonthSms).append(isDailyEmail).append(isMonthEmail).append(dailySms).append(monthSms).append(dailyEmail).append(monthEmail).append(thresholdDay).append(thresholdMonth).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof SMSNotificationsRepresentation) == false) {
			return false;
		}
		SMSNotificationsRepresentation rhs = ((SMSNotificationsRepresentation) other);
		return new EqualsBuilder().append(isDaily, rhs.isDaily).append(isMonth, rhs.isMonth).append(isDailySms, rhs.isDailySms).append(isMonthSms, rhs.isMonthSms).append(isDailyEmail, rhs.isDailyEmail).append(isMonthEmail, rhs.isMonthEmail).append(dailySms, rhs.dailySms).append(monthSms, rhs.monthSms).append(dailyEmail, rhs.dailyEmail).append(monthEmail, rhs.monthEmail).append(thresholdDay, rhs.thresholdDay).append(thresholdMonth, rhs.thresholdMonth).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}