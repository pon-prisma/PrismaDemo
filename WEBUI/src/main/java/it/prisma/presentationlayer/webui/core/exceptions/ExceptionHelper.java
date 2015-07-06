package it.prisma.presentationlayer.webui.core.exceptions;

import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper;
import it.prisma.presentationlayer.webui.core.PrismaErrorCode;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExceptionHelper {

	static final Logger LOG = LogManager.getLogger(ExceptionHelper.class);

	/**
	 * If class == null no logger
	 * @param exception
	 * @param c: the name of the class. If null not logger enabled
	 * @return
	 */
	public static <T> CustomGenericException getPrismaException(Exception exception, Class<T> c) {
		if (exception instanceof APIErrorException) {
			APIErrorException apiEx = (APIErrorException) exception;
			if (c != null) {
				LOG.error("Class: " + c.getName() + apiEx.getAPIError().getVerbose());
			}
			return getPrismaException(apiEx);
		}
		LOG.error(exception);
		return getPrismaException(exception);
	}
	
	
	public static CustomGenericException getPrismaException(Exception exception) {
		//TODO improve cast
		if (exception instanceof APIErrorException) {

			APIErrorException apiEx = (APIErrorException) exception;

			switch (apiEx.getResponseMessage().getHttpStatusCode()) {
			case 404:
				return new CustomGenericException(PrismaErrorCode.NOT_FOUND, "");
			case 500:
				return new CustomGenericException(PrismaErrorCode.SERVER_ERROR,
						"Errore del server remoto");
			default:
				return new CustomGenericException(PrismaErrorCode.GENERIC,
						"Errore server");
			}
		} else if(exception instanceof ServerErrorResponseException){
			ServerErrorResponseException serverEx = (ServerErrorResponseException) exception;
			switch (serverEx.getResponseMessage().getHttpStatusCode()) {
			case 404:
				return new CustomGenericException(PrismaErrorCode.NOT_FOUND, "");
			case 500:
				return new CustomGenericException(PrismaErrorCode.SERVER_ERROR,
						"Errore del server remoto");
			default:
				return new CustomGenericException(PrismaErrorCode.GENERIC,
						"Errore server");
			}
		}
		return new CustomGenericException(PrismaErrorCode.GENERIC,
				"Errore non trovato");

	}

	/**
	 * 
	 * If class == null no logger
	 * 
	 * 
	 * @param <T>
	 * @param exception
	 * @param c: the name of the class. If null not logger enabled
	 * @return
	 */
	public static <T> PrismaJSONResponse getPrismaJSONResponse(Exception exception, Class<T> c) {

		if (exception instanceof APIErrorException) {
			APIErrorException apiEx = (APIErrorException) exception;
			if (c != null) {
				LOG.error("Class: " + c.getName() + apiEx.getAPIError().getVerbose());
			}
			return getPrismaJSONResponseNoLogger(apiEx);
		}
		LOG.error(exception);
		return new PrismaJSONResponse(false, false, null, null);
	}

	private static PrismaJSONResponse getPrismaJSONResponseNoLogger(APIErrorException exception) {

		if (exception instanceof APIErrorException) {
			APIErrorException apiEx = (APIErrorException) exception;
			String s = (String) apiEx.getResponseMessage().getBody();
			ObjectMapper mapper = new ObjectMapper();
			PrismaResponseWrapper<Object> response;
			try {
				response = mapper.readValue(s, PrismaResponseWrapper.class);
				
				
					return new PrismaJSONResponse(true, false, null, 
							new PrismaError(PrismaErrorCode.valueOf((int) (response.getMeta().getStatus())), response.getError().getErrorMsg()));
				
				
				
			} catch (Exception e) {
				return new PrismaJSONResponse(false, false, null, null);
			}
		}

		return new PrismaJSONResponse(false, false, null, null);
	}

	@Deprecated
	public static PrismaJSONResponse getPrismaJSONResponse(Exception exception) {

		if (exception instanceof APIErrorException) {
			APIErrorException apiEx = (APIErrorException) exception;
			String s = (String) apiEx.getResponseMessage().getBody();
			ObjectMapper mapper = new ObjectMapper();
			PrismaResponseWrapper<Object> response;
			try {
				response = mapper.readValue(s, PrismaResponseWrapper.class);
				switch (response.getError().getErrorCode()) {
				case 400:
					return new PrismaJSONResponse(true, false, null,
							new PrismaError(PrismaErrorCode.valueOf(response
									.getError().getErrorCode()), response
									.getError().getErrorMsg()));
				case 409:
					return new PrismaJSONResponse(true, false, null,
							new PrismaError(PrismaErrorCode.valueOf(response
									.getError().getErrorCode()), response
									.getError().getErrorMsg()));
				}
			} catch (Exception e) {
				return new PrismaJSONResponse(false, false, null, null);
			}
		}

		return new PrismaJSONResponse(false, false, null, null);
	}

}
