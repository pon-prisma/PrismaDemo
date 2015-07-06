package it.prisma.businesslayer.bizlib.paas.services.smsaas.playsms;
/**
 * 
 */

/**
 * Information on sending sms
 * @author l.calicchio
 *
 */
public class DataSendSms {
	private String status;
	private String error;
	private String smslog_id;
	private String queue;
	private String to;
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
	 * @return the smslog_id
	 */
	public String getSmslog_id() {
		return smslog_id;
	}
	/**
	 * @param smslog_id the smslog_id to set
	 */
	public void setSmslog_id(String smslog_id) {
		this.smslog_id = smslog_id;
	}
	/**
	 * @return the queue
	 */
	public String getQueue() {
		return queue;
	}
	/**
	 * @param queue the queue to set
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}
	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}
	

}
