/**
 * 
 */
package it.prisma.domain.dsl.paas.services.caaas;

import it.prisma.domain.dsl.prisma.ErrorCode;

/**
 * @author l.calicchio
 * 
 */
public enum CaErrorCode implements ErrorCode {
	MALFORMED_URL_EXCEPTION(100, "Error with connection, verify URL string ."), GENERATION_KEY_EXCEPTION(
			300, "Error during key generation."), GENERATION_CSR_EXCEPTION(400,
			"Error during CSR generation."), CA_NOT_EXISTS_EXCEPTION(500,
			"Ca does not exists."), USER_NOT_AUTHORIZED_EXCEPTION(501,
			"User isn't authorized to request."), USER_NOT_FOUD_EXCEPTION(502,
			"User cannot be found."), CERTIFICATE_CREATION_EXCEPTION(503,
			"Error during certificate creation."), SEARCH_CERTIFICATE_EXCEPTION(
			600, "Error during certificate search."), CERTIFICATE_EXCEPTION(
			601, "Error during certificate creation."), GENERIC_CERTIFICATE_EXCEPTION(
			700, "Error with CA."), APPROVAL_EXCEPTION(800,
			"Already exists an approval request for this task."), ALREADY_REVOKED_EXCEPTION(
			801,
			"The certificate was already revoked, or you tried to unrevoke a permanently revoked certificate."), WAITING_APPROVAL_EXCEPTION(
			802, "Request has bean added to list of tasks to be approved."), WRITE_CERTIFICATE_EXCEPTION(
			900, "Error while writing the certificate."), WRITE_PRIVATEKEY_EXCEPTION(
			901, "Error while writing the private key.");

	private final int code;
	private final String description;
	private final int httpStatusCode;

	private CaErrorCode(int code, String description) {
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

	public CaErrorCode lookupFromCode(int errorCode) {
		for (CaErrorCode e : values()) {
			if (e.code == errorCode) {
				return e;
			}
		}
		return null;
	}

	public CaErrorCode lookupFromName(String errorName) {
		for (CaErrorCode e : values()) {
			if (errorName.equals(e.name())) {
				return e;
			}
		}
		return null;
	}
}
