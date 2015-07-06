package it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions;

public class PlatformStatusException extends RuntimeException {

	private static final long serialVersionUID = 8570162237634284166L;

	public PlatformStatusException() {
		super();
	}

	public PlatformStatusException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PlatformStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlatformStatusException(String message) {
		super(message);
	}

	public PlatformStatusException(Throwable cause) {
		super(cause);
	}

}
