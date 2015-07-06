package it.prisma.businesslayer.bizws.config.exceptionhandling;

import it.prisma.domain.dsl.prisma.ErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.Error;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response.Status;

/**
 * Exception for returning error in Prisma WS. <br/>
 * Custom parameters can be specified: <tt>status</tt>, <tt>errorCode</tt>,
 * <tt>verbose</tt>.
 * 
 * @author l.biava
 *
 */
@ApplicationException(rollback=false)
public class PrismaWrapperException extends RuntimeException {

	private static final long serialVersionUID = -5129288618082097967L;

	private Status status;
	private ErrorCode errorCode;
	private Error error;
	private String verbose;

	public PrismaWrapperException() {
		super();
	}

	public PrismaWrapperException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PrismaWrapperException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrismaWrapperException(String message) {
		super(message);
	}

	public PrismaWrapperException(Throwable cause) {
//		(cause instanceof PrismaWrapperException) ? super() : super(cause);
//		if(cause instanceof PrismaWrapperException) {
//			super();
//			PrismaWrapperException c = (PrismaWrapperException) cause;
//			this.error=c.getError();
//			this.errorCode=c.getErrorCode();
//			this.status=c.getStatus();
//			this.verbose=c.verbose;			
//		}
//		else
		super(cause);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Prisma Error data in enum.
	 * 
	 * @return
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * Prisma error dsl.
	 * 
	 * @return
	 */
	public Error getError() {
		return error;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getVerbose() {
		return verbose;
	}

	public void setVerbose(String verbose) {
		this.verbose = verbose;
	}

	/**
	 * Pseudo-builder method.
	 * 
	 * @param status
	 * @return
	 */
	public PrismaWrapperException status(Status status) {
		this.status = status;
		return this;
	}

	/**
	 * Pseudo-builder method.
	 * 
	 * @param status
	 * @return
	 */
	public PrismaWrapperException errorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
		return this;
	}
	
	/**
	 * Pseudo-builder method.
	 * 
	 * @param status
	 * @return
	 */
	public PrismaWrapperException error(Error error) {
		this.error = error;
		return this;
	}

	/**
	 * Pseudo-builder method.
	 * 
	 * @param status
	 * @return
	 */
	public PrismaWrapperException verbose(String verbose) {
		this.verbose = verbose;
		return this;
	}

}
