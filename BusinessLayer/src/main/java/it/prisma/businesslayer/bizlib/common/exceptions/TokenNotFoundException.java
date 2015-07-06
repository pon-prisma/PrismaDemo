package it.prisma.businesslayer.bizlib.common.exceptions;

import it.prisma.dal.entities.accounting.AuthToken;

public class TokenNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 1371794767938328920L;

	public TokenNotFoundException() {
		super(AuthToken.class);
	}

	public TokenNotFoundException(String message) {
		super(message);
	}

}
