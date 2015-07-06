package it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions;

public class DNSServiceException extends RuntimeException {

	private static final long serialVersionUID = 8570162237634284166L;

	public DNSServiceException() {
		super();
	}

	public DNSServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DNSServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DNSServiceException(String message) {
		super(message);
	}

	public DNSServiceException(Throwable cause) {
		super(cause);
	}

}
