package it.prisma.businesslayer.bizlib.common.exceptions;

public class UnauthorizedException extends PrismaBizExceptionBase {

	private static final long serialVersionUID = -8653667265845816185L;

	public UnauthorizedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedException(String message) {
		super(message);
	}

	public UnauthorizedException(Throwable cause) {
		super(cause);
	}

}
