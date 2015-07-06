package it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions;

public class ServiceConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 8570162237634284166L;

	public ServiceConfigurationException() {
		super();
	}

	public ServiceConfigurationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceConfigurationException(String message) {
		super(message);
	}

	public ServiceConfigurationException(Throwable cause) {
		super(cause);
	}

}
