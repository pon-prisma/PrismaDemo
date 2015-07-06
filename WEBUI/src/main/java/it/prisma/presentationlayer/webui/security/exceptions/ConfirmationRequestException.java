package it.prisma.presentationlayer.webui.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class ConfirmationRequestException extends AuthenticationException {

	private static final long serialVersionUID = -5588966104267574044L;

	public ConfirmationRequestException(String msg) {
		super(msg);
	}

}
