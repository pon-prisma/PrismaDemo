package it.prisma.presentationlayer.webui.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class SSOCredentialAlreadyUsedException extends AuthenticationException {

	private static final long serialVersionUID = -6600425491713205731L;

	public SSOCredentialAlreadyUsedException(String msg) {
		super(msg);
	}

}
