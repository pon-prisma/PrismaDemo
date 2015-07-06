package it.prisma.businesslayer.orchestrator.bpm.WIHs.misc;


public class SignalEvent {

	public enum SignalEventType {
		ERROR
	}

	private SignalEventType type;
	private String name;
	private Throwable cause;

	public SignalEvent(SignalEventType type, String name) {
		super();
		this.type = type;
		this.name = name;
	}

	public SignalEvent(SignalEventType type, String name, Throwable cause) {
		super();
		this.type = type;
		this.name = name;
		this.cause = cause;
	}

	public SignalEventType getType() {
		return type;
	}

	public void setType(SignalEventType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}
}
