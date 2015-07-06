package it.prisma.presentationlayer.webui.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class NoRoleException extends AuthenticationException {

	private static final long serialVersionUID = -2554245810636356875L;

	public NoRoleException(String msg) {
		super(msg);
	}

}
