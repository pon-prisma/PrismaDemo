package it.prisma.dal.dao.queryparams.exceptions;

import it.prisma.dal.dao.queryparams.DAOOrderingParams;

/**
 * Exception related to current {@link DAOOrderingParams} values.
 * @author l.biava
 *
 */
public class DAOOrderingParamsException extends DAOQueryParamsException {

	private static final long serialVersionUID = 3045303401176038195L;

	public DAOOrderingParamsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DAOOrderingParamsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DAOOrderingParamsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DAOOrderingParamsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DAOOrderingParamsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
