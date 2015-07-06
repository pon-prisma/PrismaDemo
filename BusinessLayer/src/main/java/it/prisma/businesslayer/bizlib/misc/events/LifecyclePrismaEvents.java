package it.prisma.businesslayer.bizlib.misc.events;

import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;

public enum LifecyclePrismaEvents {
	
	START_IN_PROGRESS("Start in progress..."),
	STARTED("Service is started"),
	STOP_IN_PROGRESS("Stop in progress..."),
	STOPPED("Service is stopped"),
	RESTART_IN_PROGRESS("Restart in progress...");


	private final String message;
	private final EventType type;

	private LifecyclePrismaEvents( EventType type, String message) {
		this.message = message;
		this.type = type;
	}

	private LifecyclePrismaEvents(String message) {
		this.message = message;
		this.type = EventType.INFO;
	}

	public String getMessage() {
		return message;
	}

	public String getName() {
		return name();
	}

	public EventType getType() {
		return type;
	}

	@Override
	public String toString() {
		return message;
	}

	public static LifecyclePrismaEvents lookupFromName(String eventName) {
		for (LifecyclePrismaEvents e : values()) {
			if (eventName.equals(e.getName())) {
				return e;
			}
		}
		return null;
	}

}
