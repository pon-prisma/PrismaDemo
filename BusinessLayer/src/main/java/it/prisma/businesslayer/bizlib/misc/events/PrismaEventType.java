package it.prisma.businesslayer.bizlib.misc.events;

import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;

import java.util.Formatter;

/**
 * This interface is used to specify Prisma Event Types and related code e
 * message formatter, using an {@link Enum}.
 * 
 * @author l.biava
 * 
 */
public interface PrismaEventType {

	/**
	 * 
	 * @return the formatter message of the event. <br/>
	 *         See {@link Formatter} for syntax.
	 * @see Formatter
	 */
	public String getMessage();

	/**
	 * 
	 * @return the code of the event.
	 */
	public int getCode();

	/**
	 * 
	 * @return the name of the event.
	 */
	public String getName();

	/**
	 * 
	 * @return the severity of the event. See {@link PaaSServiceEvent.EventType}
	 *         .
	 */
	public EventType getType();
	
	/**
	 * Search an event having the given name.
	 * @param eventName
	 * @return the event with given name, <tt>null</tt> if not exists.
	 */
	//public PrismaEventType lookupFromName(Class<PrismaEventType> enumClass, String eventName);
}