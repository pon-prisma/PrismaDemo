package it.prisma.dal.dao.queryparams.exceptions;

import it.prisma.dal.dao.queryparams.DAOOrderingParams;

/**
 * Exception related to current {@link DAOOrderingParams} values.
 * @author l.biava
 *
 */
public class DAOPaginationParamsException extends DAOQueryParamsException {

	private static final long serialVersionUID = 3045303401176038195L;

	public DAOPaginationParamsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DAOPaginationParamsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DAOPaginationParamsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DAOPaginationParamsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DAOPaginationParamsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
