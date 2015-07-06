package it.prisma.businesslayer.bizlib.misc.events;

import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrismaEvent {

	private String details;
	private String formatMessage;
	private String typeName;
	private EventType type;
	private int code;

	/**
	 * Constructs a {@link PrismaEvent} using a predefined
	 * {@link PrismaEventType}.
	 * 
	 * @param type
	 * @param args
	 *            the arguments to substitute in the message format.
	 */
	public PrismaEvent(PrismaEventType type, Object... args) {
		this(type.getMessage(), type.getName(), type.getCode(), type.getType(),
				args);
	}

	public PrismaEvent(String formatMessage, String typeName, int code,
			EventType type, Object... args) {
		super();

		boolean done = false;
		int i = 0;
		int MAX_ARGS_RETRY = 5;

		if (args != null) {
			List<Object> argsList = new ArrayList<Object>(Arrays.asList(args));
			while (i < MAX_ARGS_RETRY && !done) {
				try {
					this.details = String.format(formatMessage,
							argsList.toArray());
					done = true;
				} catch (Exception ex) {
					i++;
					argsList.add("");
				}
			}		
		if (i >= MAX_ARGS_RETRY)
			this.details = formatMessage;
		}else{
			this.details = formatMessage;
		}
		

		this.formatMessage = formatMessage;
		this.typeName = typeName;
		this.code = code;
		this.type = type;
	}

	public PrismaEvent(String details, String typeName, int code, EventType type) {
		super();
		this.details = details;
		this.formatMessage = details;
		this.typeName = typeName;
		this.code = code;
		this.type = type;
	}

	public String getDetails() {
		return details;
	}

	public String getFormatMessage() {
		return formatMessage;
	}

	public String getTypeName() {
		return typeName;
	}

	public int getCode() {
		return code;
	}

	public EventType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "PrismaEvents [details=" + details + ", formatMessage="
				+ formatMessage + ", typeName=" + typeName + ", type=" + type
				+ ", code=" + code + "]";
	}

}
