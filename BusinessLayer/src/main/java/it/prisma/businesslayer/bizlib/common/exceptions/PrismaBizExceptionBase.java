package it.prisma.businesslayer.bizlib.common.exceptions;

import javax.ejb.ApplicationException;

/**
 * Base abstract class for all Prisma BizLayer exceptions.
 * @author l.biava
 *
 */
@ApplicationException
public abstract class PrismaBizExceptionBase extends RuntimeException {

	private static final long serialVersionUID = -3272236206458185896L;

	public PrismaBizExceptionBase(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PrismaBizExceptionBase(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PrismaBizExceptionBase(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PrismaBizExceptionBase(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
