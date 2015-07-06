package it.prisma.businesslayer.bizlib.misc.events;

import it.prisma.dal.entities.paas.services.PaaSServiceEvent.EventType;

public enum DeployingPrismaEvents implements PrismaEventType {
	//@formatter:off
	DEPLOY_IN_PROGRESS(201, "Service deploy in progress...") /*<Service name>*/,
	DEPLOY_ENDED_SUCCESSFULLY(202, "Service deploy ended successfully. Service is ready to use.") /*<Service name>*/,
	DEPLOY_ENDED_WITH_ERROR(203, EventType.ERROR, "Service deployment failed. Error: %s.") /*<Service name>, <Brief error description>*/,
	DEPLOY_RESOURCE_PROVISIONING(205, "Provisioning service resources..."),
	DEPLOY_RESOURCE_PROVISIONING_SUCCESSFUL(206, "Service resources successfully provisioned."),
	DEPLOY_RESOURCE_PROVISIONING_ERROR(207, EventType.ERROR, "Failed to provision service resources. Error: %s."),
	DEPLOY_CHECKING_PLATFORM_STATUS(208, "Checking the platform status..."),
	DEPLOY_CHECKING_PLATFORM_STATUS_ERROR(209, EventType.ERROR, "The platform is currently unable to perform requested operation."),
	DEPLOY_VM_CREATED(210, "Virtual Machine created."),
	DEPLOY_ITEM_CREATED(211, "%s %s created.") /*<Item name>, <Optional item description>*/,
	DEPLOY_POST_DEPLOY_ADDING_MONITORING(220, "Registering service to monitoring platform..."),
	DEPLOY_POST_DEPLOY_MONITORING_SUCCESSFUL(221, "Service successfully registered to monitoring platform."),
	DEPLOY_POST_DEPLOY_MONITORING_ERROR(222, EventType.ERROR, "Failed to register to monitoring platform. Error: %s."),
	DEPLOY_POST_DEPLOY_DNS_ENTRY_SUCCESSFUL(230, "DNS record created: %s." /*<Domain Name/IP>*/),
	DEPLOY_POST_DEPLOY_DNS_ENTRY_ERROR(231, EventType.ERROR, "Failed to create DNS record."),
	DEPLOY_POST_DEPLOY_WAITING_FOR_SERVICE_READY(240, EventType.INFO, "Configuring service. Waiting for the service to be ready..."),
	DEPLOY_POST_DEPLOY_WAITING_FOR_SERVICE_READY_SUCCESSFUL(241, EventType.INFO, "Service successfully configured."),
	DEPLOY_POST_DEPLOY_WAITING_FOR_SERVICE_READY_ERROR(242, EventType.ERROR, "Failed to configure the service. Error: %s."),
	
	POST_DEPLOY_STOP_IN_PROGRESS(301, "Stopping service..."),
	POST_DEPLOY_STOPPED(302, "Service stopped."),
	POST_DEPLOY_STOP_ERROR(303, EventType.ERROR, "Error stopping the service. Error: %s.") /*<Brief error description>*/,
	POST_DEPLOY_START_IN_PROGRESS(311, "Starting service..."),
	POST_DEPLOY_STARTED(312, "Service started."),
	POST_DEPLOY_START_ERROR(313, EventType.ERROR, "Error starting the service. Error: %s.") /*<Brief error description>*/,
	
	UNDEPLOY_IN_PROGRESS(401, "Service undeploy in progress... (Started by %s)"),
	UNDEPLOY_ENDED_SUCCESSFULLY(402, "Service undeploy ended successfully."),
	UNDEPLOY_ENDED_WITH_ERROR(403, EventType.ERROR, "Service undeployment failed. Error: %s.") /*<Brief error description>*/,
	UNDEPLOY_RESOURCE(410, "Deleting service resources..."),
	UNDEPLOY_RESOURCE_SUCCESSFUL(411, "Service resources successfully deleted."),
	UNDEPLOY_RESOURCE_ERROR(412,EventType.ERROR, "Failed to delete service resources. Error: %s."),
	UNDEPLOY_REMOVING_MONITORING(420, "Unregistering service from monitoring platform..."),
	UNDEPLOY_REMOVING_MONITORING_SUCCESSFUL(421, "Service successfully unregistered from monitoring platform."),
	UNDEPLOY_REMOVING_MONITORING_ERROR(422, EventType.ERROR, "Failed to unregister from monitoring platform. Error: %s."),
	UNDEPLOY_DNS_ENTRY_SUCCESSFUL(430, "DNS record removed." /*<Domain Name/IP>*/),
	UNDEPLOY_DNS_ENTRY_ERROR(431, EventType.ERROR, "Failed to remove DNS record."),;
	//@formatter:on

	private final int code;
	private final String message;
	private final EventType type;

	private DeployingPrismaEvents(int code, EventType type, String message) {
		this.code = code;
		this.message = message;
		this.type = type;
	}

	private DeployingPrismaEvents(int code, String message) {
		this.code = code;
		this.message = message;
		this.type = EventType.INFO;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name();
	}

	public EventType getType() {
		return type;
	}

	@Override
	public String toString() {
		return code + ": " + message;
	}

	public static PrismaEventType lookupFromName(String eventName) {
		for (PrismaEventType e : values()) {
			if (eventName.equals(e.getName())) {
				return e;
			}
		}
		return null;
	}

}
