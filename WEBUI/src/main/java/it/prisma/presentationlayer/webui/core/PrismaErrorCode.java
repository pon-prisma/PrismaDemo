package it.prisma.presentationlayer.webui.core;


public enum PrismaErrorCode {

	BAD_REQUEST(400),
	NOT_FOUND(404), 
	CONFLICT(409), 
	SERVER_ERROR(500),
	FIELD_ERROR(1000),
	GENERIC(100);

	private final int value;

	private PrismaErrorCode(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static PrismaErrorCode valueOf(int status) {
		
		for (PrismaErrorCode code : values()) {
			if (code.value == status) {
				return code;
			}
		}
		throw new IllegalArgumentException("No matching constant for ["
				+ status + "]");
	}
}
