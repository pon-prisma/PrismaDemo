package it.prisma.businesslayer.exceptions;

public class IllegalServiceStateException extends RuntimeException {

	private static final long serialVersionUID = 8570162237634284166L;

	public IllegalServiceStateException() {
		super();
	}

	public IllegalServiceStateException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalServiceStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalServiceStateException(String message) {
		super(message);
	}

	public IllegalServiceStateException(Throwable cause) {
		super(cause);
	}

}
