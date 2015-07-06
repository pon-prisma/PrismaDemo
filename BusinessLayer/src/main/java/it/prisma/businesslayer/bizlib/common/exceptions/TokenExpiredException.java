package it.prisma.businesslayer.bizlib.common.exceptions;

import java.util.Date;

public class TokenExpiredException extends PrismaBizExceptionBase {

	private static final long serialVersionUID = 7934998535292296390L;

	private Date expireDate;

	public TokenExpiredException(Date expireDate) {
		super("Token expired at " + expireDate);
	}

	public Date getExpireDate() {
		return expireDate;
	}

}
