package it.prisma.businesslayer.exceptions;

public class ServiceOperationException extends RuntimeException {

	private static final long serialVersionUID = 8570162237634284166L;

	public ServiceOperationException() {
		super();
	}

	public ServiceOperationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceOperationException(String message) {
		super(message);
	}

	public ServiceOperationException(Throwable cause) {
		super(cause);
	}

}
