/**
 * 
 */
package it.prisma.domain.dsl.paas.services.smsaas;

import it.prisma.domain.dsl.prisma.ErrorCode;

/**
 * SMS error code
 * 
 * @author l.calicchio
 * 
 */
public enum SmsErrorCode implements ErrorCode {
	TOKEN_EXCEPTION(901, "Error: unable to retrieve the token"), MALFORMED_URL_EXCEPTION(
			100,
			"Error with connection, verify user, token, number, text, schedule date and mittent."), SEND_SMS_EXCEPTION(
			101, "Error while sending the sms."), ACTIVATION_INFORMATION_EXCEPTION(
			300, "Error: unable to verify the activation status of the user"), DISABLE_USER_EXCEPTION(
			400, "Error: impossible to disable the user"), CREDIT_EXCEPTION(
			500, "Error: unable to retrieve the credit"), SMS_ARCHIVE_EXCEPTION(
			600, "Error: unable to retrieve SMS messages sent "), SET_NOTIFY_EXCEPTION(
			700, "Error: unable to set notify parameters "), GET_NOTIFY_EXCEPTION(
			701, "Error: unable to retrieve the notify parameters"), CHANGE_PASSWORD_EXCEPTION(
			800, "Error: unable to change the password"), ACTIVE_USER_EXCEPTION(
			900, "Error: impossible to activate the user"), USER_NOT_FOUND_EXCEPTION(
			301, "Error: user not found!");

	private final int code;
	private final String description;
	private final int httpStatusCode;

	private SmsErrorCode(int code, String description) {
		this.code = code;
		this.description = description;
		this.httpStatusCode = 500;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name();
	}

	@Override
	public String toString() {
		return code + ": " + description;
	}

	public SmsErrorCode lookupFromCode(int errorCode) {
		for (SmsErrorCode e : values()) {
			if (e.code == errorCode) {
				return e;
			}
		}
		return null;
	}

	public SmsErrorCode lookupFromName(String errorName) {
		for (SmsErrorCode e : values()) {
			if (errorName.equals(e.name())) {
				return e;
			}
		}
		return null;
	}

}
