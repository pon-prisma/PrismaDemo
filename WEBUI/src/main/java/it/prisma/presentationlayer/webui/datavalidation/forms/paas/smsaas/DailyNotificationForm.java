//TODO improve the validation

package it.prisma.presentationlayer.webui.datavalidation.forms.paas.smsaas;

import java.io.Serializable;

public class DailyNotificationForm implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String dailyNot;

	private String dailySms;

	private String dailyEmail;

	private String numberDaily;

	private String emailAddressDaily;

	private Integer smsThresholdDaily;

	public String getDailyNot() {
		return dailyNot;
	}

	public void setDailyNot(String dailyNot) {
		this.dailyNot = dailyNot;
	}

	public String getDailySms() {
		return dailySms;
	}

	public void setDailySms(String dailySms) {
		this.dailySms = dailySms;
	}

	public String getDailyEmail() {
		return dailyEmail;
	}

	public void setDailyEmail(String dailyEmail) {
		this.dailyEmail = dailyEmail;
	}

	public String getNumberDaily() {
		return numberDaily;
	}

	public void setNumberDaily(String numberDaily) {
		this.numberDaily = numberDaily;
	}

	public String getEmailAddressDaily() {
		return emailAddressDaily;
	}

	public void setEmailAddressDaily(String emailAddressDaily) {
		this.emailAddressDaily = emailAddressDaily;
	}

	public Integer getSmsThresholdDaily() {
		return smsThresholdDaily;
	}

	public void setSmsThresholdDaily(Integer smsThresholdDaily) {
		this.smsThresholdDaily = smsThresholdDaily;
	}

	@Override
	public String toString() {
		return "NotificationForm [dailyNot=" + dailyNot + ", dailySms="
				+ dailySms + ", dailyEmail=" + dailyEmail + ", numberDaily="
				+ numberDaily + ", emailAddressDaily=" + emailAddressDaily
				+ ", smsThresholdDaily=" + smsThresholdDaily + "]";
	}

	

}
