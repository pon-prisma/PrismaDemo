package it.prisma.businesslayer.exceptions.email;

public class DomainNotFoundException extends Exception {

	/**
	 * @author l.calicchio
	 */
	private static final long serialVersionUID = 1L;

	public DomainNotFoundException(){
		super("Exception: domain not found");
	}
}
