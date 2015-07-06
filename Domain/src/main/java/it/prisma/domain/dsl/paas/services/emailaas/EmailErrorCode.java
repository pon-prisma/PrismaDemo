/**
 * 
 */
package it.prisma.domain.dsl.paas.services.emailaas;

import it.prisma.domain.dsl.prisma.ErrorCode;

/**
 * @author l.calicchio
 * 
 */
public enum EmailErrorCode implements ErrorCode {

	/**
	 * @author l.calicchio
	 * 
	 */
	SENDER_EXCEPTION(100, "Unable to configure the mail sender."), EMAIL_EXCEPTION(
			300, "Error while sending the email."), MESSAGING_EXCEPTION(400,
			"Unable to send message."), EMAIL_SYNTAX_EXCEPTION(500,
			"Attention! Control the syntax of email address"), AUTHENTICATION_FAILED_EXCEPTION(
			600, "Authentication failed"), USER_NOT_FOUND_EXCEPTION(700,
			"User not found"), DOMAIN_NOT_FOUND_EXCEPTION(701,
			"Domain not found"), USER_OR_PASSWORD_EXCEPTION(900,
			"User not found or incorrect password"), PASSWORD_EXCEPTION(901,
			"The password is incorrect"), PASSWORD_NOT_CHANGED_EXCEPTION(901,
			"The password has not been changed"), GENERAL_EMAIL_EXCEPTION(
			800, "Email exception");

	private final int code;
	private final String description;
	private final int httpStatusCode;

	private EmailErrorCode(int code, String description) {
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

	public EmailErrorCode lookupFromCode(int errorCode) {
		for (EmailErrorCode e : values()) {
			if (e.code == errorCode) {
				return e;
			}
		}
		return null;
	}

	public EmailErrorCode lookupFromName(String errorName) {
		for (EmailErrorCode e : values()) {
			if (errorName.equals(e.name())) {
				return e;
			}
		}
		return null;
	}

}
