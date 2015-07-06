package it.prisma.businesslayer.bizlib.common.exceptions;


/**
 * This exceptions represents a failed attempt to find a resource with the given
 * parameters.
 * 
 * @author l.biava
 *
 */
public class ResourceNotFoundException extends PrismaBizExceptionBase {

	private static final long serialVersionUID = -8653667265845816185L;

	public ResourceNotFoundException(Class<?> clazz) {
		this(getErrorMessage(clazz));
	}

	public ResourceNotFoundException(Class<?> clazz, String... fields) {
		this(getErrorMessage(clazz, fields));
	}

	private static String getErrorMessage(Class<?> clazz, String... fields) {
		String className = clazz.getSimpleName().replace("Representation", "")
				.replace("Entity", "");
		String msg = className
				+ String.format(" not found with given input: %s",
						org.apache.commons.lang.StringUtils.join(fields, ", "));
		return msg;
	}

	public ResourceNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}