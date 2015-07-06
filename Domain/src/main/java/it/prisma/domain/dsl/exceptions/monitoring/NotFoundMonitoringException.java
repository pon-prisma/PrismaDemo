package it.prisma.domain.dsl.exceptions.monitoring;

public class NotFoundMonitoringException extends MonitoringException {
	
	private static final long serialVersionUID = 4377449055260705550L;

	public NotFoundMonitoringException(String message, Throwable e){
		super(message, e);
	}
}