package it.prisma.businesslayer.bizws.config.security;

import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaExceptionMapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class AuthenticationExceptionEntryPoint implements
		AuthenticationEntryPoint {
	// // The class from Step 1
	// private MessageProcessor processor;
	//
	// public CustomEntryPoint() {
	// // It is up to you to decide when to instantiate
	// processor = new MessageProcessor();
	// }

	PrismaExceptionMapper prismaExceptionMapper;

	public AuthenticationExceptionEntryPoint() {
		prismaExceptionMapper = new PrismaExceptionMapper();
	}

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		try {
			prismaExceptionMapper.toResponse(new PrismaWrapperException(
					authException), request, response);
		} catch (Exception e) {
			throw new ServletException();
		}
	}
}
