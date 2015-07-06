package it.prisma.presentationlayer.webui.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotEnabledException extends AuthenticationException {

	private static final long serialVersionUID = -4360735333007058175L;

	public UserNotEnabledException(String msg) {
		super(msg);
	}

}
