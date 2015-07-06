package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

import it.prisma.domain.dsl.iaas.openstack.Error;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;

/**
 * Exception representing that the API returned an error status. <br/>
 * The original {@link RestMessage} is embedded for inspection.
 * 
 * @author 
 * 
 */
public class OpenstackAPIErrorException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1736252268201316921L;
	
	private RestMessage responseMessage;
	private Error APIError;

	public RestMessage getResponseMessage() {
		return responseMessage;
	}

	public boolean hasResponseMessage() {
		return responseMessage != null;
	}

	public OpenstackAPIErrorException(String msg, Throwable t,
			RestMessage responseMessage, Error APIError) {
		super(msg + ": error code " + APIError, t); 
		this.responseMessage = responseMessage;
		this.APIError = APIError;
	}

	public Error getAPIError() {
		return APIError;
	}
	
	
}