package it.prisma.dal.dao.queryparams.exceptions;

/**
 * Exception related to current {@link DAOQueryParams} values.
 * @author l.biava
 *
 */
public class DAOQueryParamsException extends RuntimeException {

	private static final long serialVersionUID = 3045303401176038195L;

	public DAOQueryParamsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DAOQueryParamsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DAOQueryParamsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DAOQueryParamsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DAOQueryParamsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
