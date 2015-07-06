package it.prisma.businesslayer.bizws;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Exception> {
	
	private static String RESPONSE_MEDIA_TYPE = MediaType.TEXT_PLAIN;

	@Override
	public Response toResponse(Exception e) {
		if (e.getClass().isAssignableFrom(BadRequestException.class))
			return getResponseHelper(Status.BAD_REQUEST, e.getMessage());
		else if (e.getClass().isAssignableFrom(ForbiddenException.class))
			return getResponseHelper(Status.FORBIDDEN, e.getMessage());
		else if (e.getClass().isAssignableFrom(NotAcceptableException.class))
			return getResponseHelper(Status.NOT_ACCEPTABLE, e.getMessage());
		else if (e.getClass().isAssignableFrom(NotAuthorizedException.class))
			return getResponseHelper(Status.UNAUTHORIZED, e.getMessage());
		else if (e.getClass().isAssignableFrom(NotSupportedException.class))
			return getResponseHelper(Status.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
		else if (e.getClass().isAssignableFrom(NotFoundException.class))
			return getResponseHelper(Status.NOT_FOUND, e.getMessage());
		else
			return Response.serverError().entity(e.getMessage().toString())
					.type(MediaType.TEXT_PLAIN).build();
	}

	private Response getResponseHelper(Status status, final String msg) {
		return Response.status(status).entity(msg)
				.type(RESPONSE_MEDIA_TYPE).build();
	}
}