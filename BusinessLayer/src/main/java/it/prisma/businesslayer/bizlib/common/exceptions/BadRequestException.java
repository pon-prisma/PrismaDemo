package it.prisma.businesslayer.bizlib.common.exceptions;

public class BadRequestException extends PrismaBizExceptionBase {

	private static final long serialVersionUID = -8653667265845816185L;

	public BadRequestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(Throwable cause) {
		super(cause);
	}

}
