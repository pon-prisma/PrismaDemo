package it.prisma.domain.dsl.iaas.network;

public enum State {
	ACTIVE, DOWN, BUILD, ERROR, UNRECOGNIZED;

	public static State forValue(String value) {
		if (value != null) {
			for (State s : State.values()) {
				if (s.name().equalsIgnoreCase(value))
					return s;
			}
		}
		return State.UNRECOGNIZED;
	}
}