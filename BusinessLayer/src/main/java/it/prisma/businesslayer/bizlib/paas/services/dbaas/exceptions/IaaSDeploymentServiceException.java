package it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions;

public class IaaSDeploymentServiceException extends RuntimeException {

	private static final long serialVersionUID = 8570162237634284166L;

	private String iaasName;
	
	public IaaSDeploymentServiceException(String iaasName) {
		super();
		this.iaasName=iaasName;
	}

	public IaaSDeploymentServiceException(String iaasName, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.iaasName=iaasName;
	}

	public IaaSDeploymentServiceException(String iaasName,String message, Throwable cause) {
		super(message, cause);
		this.iaasName=iaasName;
	}

	public IaaSDeploymentServiceException(String iaasName,String message) {
		super(message);
		this.iaasName=iaasName;
	}

	public IaaSDeploymentServiceException(String iaasName,Throwable cause) {
		super(cause);
		this.iaasName=iaasName;
	}

	public String getIaasName() {
		return iaasName;
	}

}
