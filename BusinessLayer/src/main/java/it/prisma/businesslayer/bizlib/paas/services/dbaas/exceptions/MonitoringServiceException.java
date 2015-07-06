package it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions;

public class MonitoringServiceException extends RuntimeException {

	private static final long serialVersionUID = 8570162237634284166L;

	public MonitoringServiceException() {
		super();
	}

	public MonitoringServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MonitoringServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MonitoringServiceException(String message) {
		super(message);
	}

	public MonitoringServiceException(Throwable cause) {
		super(cause);
	}

}
