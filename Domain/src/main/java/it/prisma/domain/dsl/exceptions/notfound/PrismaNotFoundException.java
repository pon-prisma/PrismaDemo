package it.prisma.domain.dsl.exceptions.notfound;

/**
 * @author Reply
 *
 */
public class PrismaNotFoundException extends Exception {

	private static final long serialVersionUID = 2834951083585038374L;

	public PrismaNotFoundException(String message){
		super(message);
	}
	
	public PrismaNotFoundException(String message, Throwable e){
		super(message, e);
	}
}