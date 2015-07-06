package it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions;

public class IllegalServiceStatusException extends RuntimeException {

	private static final long serialVersionUID = 8570162237634284166L;

	public IllegalServiceStatusException() {
		super();
	}

	public IllegalServiceStatusException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalServiceStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalServiceStatusException(String message) {
		super(message);
	}

	public IllegalServiceStatusException(Throwable cause) {
		super(cause);
	}

}
