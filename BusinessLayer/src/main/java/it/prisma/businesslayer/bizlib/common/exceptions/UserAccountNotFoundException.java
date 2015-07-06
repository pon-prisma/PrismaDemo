package it.prisma.businesslayer.bizlib.common.exceptions;

import org.springframework.security.core.AuthenticationException;


public class UserAccountNotFoundException extends AuthenticationException {


	public UserAccountNotFoundException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 627091950135258977L;

}
