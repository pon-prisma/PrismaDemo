package it.prisma.presentationlayer.webui.datavalidation.forms.paas.smsaas;
import java.io.Serializable;

public class MonthlyNotificationForm implements Serializable {
private static final long serialVersionUID = 1L;

	
	private String monthlyNot;

	private String monthlySms;

	private String monthlyEmail;

	private String numberMonthly;

	private String emailAddressMonthly;

	private Integer smsThresholdMonthly;

	public String getMonthlyNot() {
		return monthlyNot;
	}

	public void setMonthlyNot(String monthlyNot) {
		this.monthlyNot = monthlyNot;
	}

	public String getMonthlySms() {
		return monthlySms;
	}

	public void setMonthlySms(String monthlySms) {
		this.monthlySms = monthlySms;
	}

	public String getMonthlyEmail() {
		return monthlyEmail;
	}

	public void setMonthlyEmail(String monthlyEmail) {
		this.monthlyEmail = monthlyEmail;
	}

	public String getNumberMonthly() {
		return numberMonthly;
	}

	public void setNumberMonthly(String numberMonthly) {
		this.numberMonthly = numberMonthly;
	}

	public String getEmailAddressMonthly() {
		return emailAddressMonthly;
	}

	public void setEmailAddressMonthly(String emailAddressMonthly) {
		this.emailAddressMonthly = emailAddressMonthly;
	}

	public Integer getSmsThresholdMonthly() {
		return smsThresholdMonthly;
	}

	public void setSmsThresholdMonthly(Integer smsThresholdMonthly) {
		this.smsThresholdMonthly = smsThresholdMonthly;
	}

	@Override
	public String toString() {
		return "NotificationForm [monthlyNot=" + monthlyNot + ", monthlySms="
				+ monthlySms + ", monthlyEmail=" + monthlyEmail + ", numberMonthly="
				+ numberMonthly + ", emailAddressMonthly=" + emailAddressMonthly
				+ ", smsThresholdMonthly=" + smsThresholdMonthly + "]";
	}

	

}


