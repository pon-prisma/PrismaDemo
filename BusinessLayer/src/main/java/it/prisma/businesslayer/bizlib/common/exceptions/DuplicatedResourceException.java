package it.prisma.businesslayer.bizlib.common.exceptions;

public class DuplicatedResourceException extends PrismaBizExceptionBase {

	private static final long serialVersionUID = -8653667265845816185L;

	public DuplicatedResourceException(Class<?> clazz, String... fields) {
		this(getErrorMessage(clazz, fields));
	}

	private static String getErrorMessage(Class<?> clazz, String... fields) {
		String className = clazz.getSimpleName().replace("Representation", "")
				.replace("Entity", "");
		String msg = className + " already existing";
		if (fields.length > 0) {
			msg += ". Try with a different";
			if (fields.length > 1) {
				msg += " fields combination: ";
				for (String item : fields)
					msg += item + ", ";

			} else {
				msg += ": " + fields[0];
			}
			msg += ".";
		}
		return msg;
	}

	public DuplicatedResourceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DuplicatedResourceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DuplicatedResourceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DuplicatedResourceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}