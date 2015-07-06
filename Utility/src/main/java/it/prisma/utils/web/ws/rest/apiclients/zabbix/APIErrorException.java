package it.prisma.utils.web.ws.rest.apiclients.zabbix;

import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.JSONRPCError;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;

/**
 * Exception representing that the API returned an error status. <br/>
 * The original {@link RestMessage} is embedded for inspection.
 * 
 * @author l.biava
 * 
 */
public class APIErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3965412299072282395L;

	private RestMessage responseMessage;
	private JSONRPCError APIError;

	public RestMessage getResponseMessage() {
		return responseMessage;
	}

	public boolean hasResponseMessage() {
		return responseMessage != null;
	}

	public APIErrorException(String msg, Throwable t,
			RestMessage responseMessage, JSONRPCError APIError) {
		super(msg, t);
		this.responseMessage = responseMessage;
		this.APIError = APIError;
	}

	public JSONRPCError getAPIError() {
		return APIError;
	}
}
