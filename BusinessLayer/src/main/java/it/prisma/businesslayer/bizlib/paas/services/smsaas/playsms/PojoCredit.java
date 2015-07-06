package it.prisma.businesslayer.bizlib.paas.services.smsaas.playsms;

import java.math.BigDecimal;

/**
 * Pojo for credit request to PlaySms
 * @author l.calicchio
 *
 */
public class PojoCredit {
	private String status;
	private String error;
	private BigDecimal credit;
	private String error_string;
	private int timestamp;
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	/**
	 * @return the token
	 */
	public BigDecimal getCredit() {
		return credit;
	}
	/**
	 * @param token the token to set
	 */
	public void setcredit(BigDecimal credit) {
		this.credit = credit;
	}
	/**
	 * @return the error_string
	 */
	public String getError_string() {
		return error_string;
	}
	/**
	 * @param error_string the error_string to set
	 */
	public void setError_string(String error_string) {
		this.error_string = error_string;
	}
	/**
	 * @return the timestamp
	 */
	public int getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

}
