package it.prisma.presentationlayer.webui.core.exceptions;

import it.prisma.presentationlayer.webui.core.PrismaErrorCode;

public class PrismaError {

	private PrismaErrorCode errorCode;
	private String field;
	private String errorName;
	private String message;

	public PrismaError() {
	
	}

	public PrismaError(PrismaErrorCode errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public PrismaError(PrismaErrorCode errorCode, String message, String field) {
		this.errorCode = errorCode;
		this.message = message;
		this.field = field;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public PrismaErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(PrismaErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "PrismaError [errorCode=" + errorCode + ", field=" + field
				+ ", errorName=" + errorName + ", message=" + message + "]";
	}

}