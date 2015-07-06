package it.prisma.businesslayer.exceptions.smsaas;

public class SMSGenericException extends RuntimeException {

		private static final long serialVersionUID = 8570162237634284166L;

		public SMSGenericException() {
			super();
		}

		public SMSGenericException(String message, Throwable cause,
				boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		public SMSGenericException(String message, Throwable cause) {
			super(message, cause);
		}

		public SMSGenericException(String message) {
			super(message);
		}

		public SMSGenericException(Throwable cause) {
			super(cause);
		}

	}
