package it.prisma.presentationlayer.webui.core.exceptions;

import it.prisma.presentationlayer.webui.core.PrismaErrorCode;

public class CustomGenericException extends RuntimeException {

	private static final long serialVersionUID = 1878576509998350738L;

	private PrismaErrorCode errCode;
	private String errMsg;

	public CustomGenericException(PrismaErrorCode errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public PrismaErrorCode getErrCode() {
		return errCode;
	}

	public void setErrCode(PrismaErrorCode errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
