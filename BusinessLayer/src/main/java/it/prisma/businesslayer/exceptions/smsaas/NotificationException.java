package it.prisma.businesslayer.exceptions.smsaas;

public class NotificationException extends Exception {

	/**
	 * @author l.calicchio
	 */
	private static final long serialVersionUID = 1L;

	public NotificationException(){
		super("Exception using notifications");
	}
}
