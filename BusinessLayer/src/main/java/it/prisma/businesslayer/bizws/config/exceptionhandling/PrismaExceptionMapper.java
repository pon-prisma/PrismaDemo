package it.prisma.businesslayer.bizws.config.exceptionhandling;

import it.prisma.businesslayer.bizlib.common.exceptions.DuplicatedResourceException;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.common.exceptions.TokenExpiredException;
import it.prisma.businesslayer.bizlib.common.exceptions.TokenNotFoundException;
import it.prisma.businesslayer.bizlib.common.exceptions.UserAccountNotFoundException;
import it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions.IllegalServiceStatusException;
import it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions.PlatformStatusException;
import it.prisma.businesslayer.exceptions.BadRequestException;
import it.prisma.businesslayer.exceptions.ServiceOperationException;
import it.prisma.businesslayer.exceptions.organization.OrganizationNotFoundException;
import it.prisma.businesslayer.exceptions.smsaas.SMSGenericException;
import it.prisma.domain.dsl.exceptions.monitoring.MonitoringException;
import it.prisma.domain.dsl.prisma.ErrorCode;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.OrganizationErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.Error;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper;
import it.prisma.utils.json.JsonUtility;
import it.prisma.utils.misc.StackTrace;

import java.io.IOException;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.openstack4j.api.exceptions.OS4JException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This mapper is triggered by a {@link PrismaWrapperException} thrown inside a
 * JAX-RS WS. It converts the exception data into an {@link Error}, wrapping it
 * into a {@link PrismaResponseWrapper}.
 * 
 * @author l.biava
 *
 */
@Provider
public class PrismaExceptionMapper implements
		ExceptionMapper<PrismaWrapperException> {

	@Context
	HttpServletRequest request;

	@Override
	public Response toResponse(PrismaWrapperException ex) {
		return toResponse(ex, request);
	}

	/**
	 * Directly write to response the exception mapping.
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void toResponse(PrismaWrapperException ex,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		// TODO: Improve
		Response resp = toResponse(ex, request);
		response.setStatus(resp.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON);
		response.getWriter().write(JsonUtility.serializeJson(resp.getEntity()));
	}

	public Response toResponse(PrismaWrapperException ex,
			HttpServletRequest request) {

		// TODO: Check verbose content, might be too detailed
		boolean verboseEnabled = request.getHeader("X-Verbose") != null;
		// TODO: Remove if not debugging
		verboseEnabled = true;

		Error error;
		Status status;
		Status defaultStatus = Status.INTERNAL_SERVER_ERROR;
		// Exception does not contain a built error to respond -> built it
		if (ex.getError() == null) {

			// Get response status code
			status = getPrismaExceptionStatus(ex, defaultStatus);

			// Extract ErrorCode from Exception
			ErrorCode errorCode = getPrismaErrorFromException(ex);

			// Build the error representation for the response
			error = new Error();
			error.setErrorCode(errorCode.getCode());
			error.setErrorName(errorCode.getName());
			String errorMessage = errorCode.getDescription();
			if (ex.getCause() != null)
				errorMessage += " - " + ex.getCause().getMessage();

			error.setErrorMsg(errorMessage);

			String verbose = verboseEnabled ? (ex.getVerbose() != null ? ex
					.getVerbose() : StackTrace.getStackTraceToString(ex)) : "";
			error.setVerbose(verbose);
		} else { // Retrieve the already built errorO
			error = ex.getError();
			// TODO: Improve
			try {
				ErrorCode ec = OrchestratorErrorCode.ORC_GENERIC_ERROR
						.lookupFromCode(error.getErrorCode());
				status = Status.fromStatusCode(ec.getHttpStatusCode());
			} catch (Exception e) {
				status = defaultStatus;
			}
			if (!verboseEnabled)
				error.setVerbose("");
		}
		//@formatter:off		
		PrismaResponseWrapper<?> prismaResponseWrapper = PrismaResponseWrapper
				.status(status,
						error
						).build();
		//@formatter:on

		return Response.status(status).entity(prismaResponseWrapper.getError())
				.type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Returns an {@link ErrorCode} using Exception data.<br/>
	 * The error is retrieved using the following priority:
	 * <ol>
	 * <li>Error code contained in Exception</li>
	 * <li>Cause Exception type</li>
	 * <li>{@link OrchestratorErrorCode.ORC_GENERIC_ERROR}</li>
	 * </ol>
	 */
	protected ErrorCode getPrismaErrorFromException(PrismaWrapperException pwe) {

		Throwable e = pwe.getCause();
		e = extractCause(e);

		// Error code override
		if (pwe.getErrorCode() != null)
			return pwe.getErrorCode();

		// Exception type override
		if (e instanceof AuthenticationException) {
			if (e instanceof BadCredentialsException)
				if (e.getCause() instanceof TokenNotFoundException)
					return OrchestratorErrorCode.ORC_INVALID_TOKEN;
			if (e.getCause() instanceof TokenExpiredException)
				return OrchestratorErrorCode.ORC_TOKEN_EXPIRED;
			if (e instanceof UserAccountNotFoundException)
				return OrchestratorErrorCode.ORC_USER_ACCOUNT_NOT_FOUND;
			
			
			// InsufficientAuthenticationException
			// TODO:
			// OrchestratorErrorCode.ORC_AUTHENTICATION_REQUIRED;
			// OrchestratorErrorCode.ORC_NOT_AUTHORIZED

			return OrchestratorErrorCode.ORC_AUTHENTICATION_EXCEPTION;
		}
		if (e instanceof IllegalServiceStatusException) {
			return OrchestratorErrorCode.ORC_ILLEGAL_SERVICE_STATE;
		}
		if (e instanceof it.prisma.businesslayer.exceptions.IllegalServiceStateException){
			//Exception thrown when a service's state and service's updated state are inconsistent
			return OrchestratorErrorCode.ORC_SERVICE_ILLEGAL_STATE;
		}
		if (e instanceof ResourceNotFoundException) {
			return OrchestratorErrorCode.ORC_ITEM_NOT_FOUND;
		}
		if (e instanceof DuplicatedResourceException) {
			return OrchestratorErrorCode.ORC_ITEM_ALREADY_EXISTS;
		}
		if (e instanceof PlatformStatusException) {
			return OrchestratorErrorCode.ORC_PLATFORM_STATUS_NOT_READY;
		}
		if (e instanceof OS4JException) {
			return OrchestratorErrorCode.ORC_IAAS_GENERIC_ERROR;
		}
		if (e instanceof BadRequestException
				|| e instanceof it.prisma.businesslayer.bizlib.common.exceptions.BadRequestException) {
			return OrchestratorErrorCode.ORC_BAD_REQUEST;
		}
		if (e instanceof ServiceOperationException) {
			return OrchestratorErrorCode.ORC_BAD_REQUEST;
		}
		if (e instanceof OrganizationNotFoundException) {
			return OrganizationErrorCode.ORG_ORGANIZATION_NOT_FOUND;
		}
		if (e instanceof SMSGenericException) {
			return OrchestratorErrorCode.SMS_GENERIC_EXCEPTION;
		}
		if (e instanceof MonitoringException) {
			return OrchestratorErrorCode.MONITORING_EXCEPTION;
		}
				
		return OrchestratorErrorCode.ORC_GENERIC_ERROR;
	}

	/**
	 * Returns an HttpStatusCode using Exception data.<br/>
	 * The status code is retrieved using the following priority:
	 * <ol>
	 * <li>Status code contained in the Exception</li>
	 * <li>Error code contained in the Exception</li>
	 * <li>Cause Exception type</li>
	 * <li>{@link OrchestratorErrorCode.ORC_GENERIC_ERROR}</li>
	 * </ol>
	 */
	protected Status getPrismaExceptionStatus(PrismaWrapperException pwe,
			Status defaultStatus) {
		if (pwe.getStatus() != null)
			return pwe.getStatus();

		Throwable e = pwe.getCause();
		e = extractCause(e);

		// Error code override
		if (pwe.getErrorCode() != null)
			return Status
					.fromStatusCode(pwe.getErrorCode().getHttpStatusCode());

		// Exception type override
		
		if (e instanceof AuthenticationException) {
			if (e instanceof BadCredentialsException)
				if (e.getCause() instanceof TokenNotFoundException)
					return Status.NOT_FOUND;
			if (e.getCause() instanceof TokenExpiredException)
				return Status.GONE;
			if (e instanceof UserAccountNotFoundException)
				return Status.NOT_FOUND;
		}
		
		if (e instanceof ResourceNotFoundException)
			return Status.NOT_FOUND;
		if (e instanceof BadRequestException
				|| e instanceof it.prisma.businesslayer.bizlib.common.exceptions.BadRequestException)
			return Status.BAD_REQUEST;
		if (e instanceof DuplicatedResourceException)
			return Status.CONFLICT;
		if (e instanceof ResourceNotFoundException)
			return Status.NOT_FOUND;
		if (e instanceof AuthenticationException)
			return Status.UNAUTHORIZED;

		// Default error status
		return defaultStatus;
	}

	private Throwable extractCause(Throwable t) {
		if (t instanceof EJBException)
			return t.getCause();
		
		return t;
	}
}