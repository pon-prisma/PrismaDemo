package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

/**
 * Exception representing an Openstack authentication problem..
 * 
 * @author
 * 
 */
public class OpenstackAuthenticationException extends Exception {

	public OpenstackAuthenticationException() {
		super();

	}

	public OpenstackAuthenticationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OpenstackAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OpenstackAuthenticationException(String message) {
		super(message);
	}

	public OpenstackAuthenticationException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1736252268201316921L;

}