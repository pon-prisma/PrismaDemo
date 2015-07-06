package it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions;

public class PrismaDeploymentException extends RuntimeException {

	private static final long serialVersionUID = 8570162237634284166L;

	public PrismaDeploymentException() {
		super();
	}

	public PrismaDeploymentException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PrismaDeploymentException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrismaDeploymentException(String message) {
		super(message);
	}

	public PrismaDeploymentException(Throwable cause) {
		super(cause);
	}

}
