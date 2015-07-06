package it.prisma.presentationlayer.webui.core;

import it.prisma.presentationlayer.webui.core.exceptions.PrismaError;

public class PrismaJSONResponse {

	private boolean status;
	private boolean success;
	private Object result;
	private PrismaError error;

	public PrismaJSONResponse() {
	}

	public PrismaJSONResponse(boolean status) {
		this.status = status;
	}
	
	public PrismaJSONResponse(boolean status, PrismaError error) {
		this.status = status;
		this.error=error;
	}
	
	public PrismaJSONResponse(boolean status, boolean success) {
		this.success = success;
		this.status = status;
	}
	
	public PrismaJSONResponse(boolean status, boolean success, Object result,
			PrismaError error) {
		this.status = status;
		this.success = success;
		this.result = result;
		this.error = error;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public PrismaError getError() {
		return error;
	}

	public void setError(PrismaError error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "PrismaJSONResponse [status=" + status + ", success=" + success
				+ ", result=" + result + ", error=" + error + "]";
	}

}