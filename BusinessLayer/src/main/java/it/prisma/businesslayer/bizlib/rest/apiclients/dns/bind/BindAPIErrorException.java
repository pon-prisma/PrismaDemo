package it.prisma.businesslayer.bizlib.rest.apiclients.dns.bind;

import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;

/**
 * Exception representing that the API returned an error status. <br/>
 * The original {@link RestMessage} is embedded for inspection.
 * 
 * @author l.biava
 * 
 */
public class BindAPIErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3965412299072282395L;

	private RestMessage responseMessage;
	private Object APIError;

	public RestMessage getResponseMessage() {
		return responseMessage;
	}

	public boolean hasResponseMessage() {
		return responseMessage != null;
	}

	public BindAPIErrorException(String msg, Throwable t,
			RestMessage responseMessage, Object APIError) {
		super(msg, t);
		this.responseMessage = responseMessage;
		this.APIError = APIError;
	}

	public Object getAPIError() {
		return APIError;
	}
}