package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify;

import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;

/**
 * Exception representing that the API returned an error status. <br/>
 * The original {@link RestMessage} is embedded for inspection.
 * 
 * @author l.biava
 * 
 */
public class CloudifyAPIErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3965412299072282395L;

	private RestMessage responseMessage;
	private CloudifyError APIError;

	public RestMessage getResponseMessage() {
		return responseMessage;
	}

	public boolean hasResponseMessage() {
		return responseMessage != null;
	}

	public CloudifyAPIErrorException(String msg, Throwable t,
			RestMessage responseMessage, CloudifyError APIError) {
		super(msg, t);
		this.responseMessage = responseMessage;
		this.APIError = APIError;
	}

	public CloudifyError getAPIError() {
		return APIError;
	}
}