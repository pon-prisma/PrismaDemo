package it.prisma.presentationlayer.webui.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserSuspendedException extends AuthenticationException {

	private static final long serialVersionUID = 5626652852106017776L;

	public UserSuspendedException(String msg) {
		super(msg);
	}

}
