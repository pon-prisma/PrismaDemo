package it.prisma.utils;

import it.prisma.domain.dsl.exceptions.monitoring.NotFoundMonitoringException;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.IaaSHostGroups;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringDeleteRequest;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringRequest;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.HostAffected;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.TriggerShot;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.CreatedHostInServer;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.Group;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType;
import it.prisma.utils.web.ws.rest.apiclients.prisma.PrismaMonitoringAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

//TODO: Gestire la request secondo refactoring
public class Test {

    private static PrismaMonitoringAPIClient client;
    private static PrismaBizAPIClient bizClient;

    public static void main(String[] args) {

	// http://90.147.102.24
	client = new PrismaMonitoringAPIClient("http://90.147.102.24:8080/monitoring/adapters/");
	
	try {
	    WrappedIaasHealthByTrigger wrappedIaasHealthByTrigger = client.getShotTriggerByHostGroup("zabbix",
		    IaaSHostGroups.INFRASTRUCTURE_HEALTH.getGroupName());
	    System.out.println("WrappedIaasHealthByTrigger:");
	    for (HostAffected hostAffected : wrappedIaasHealthByTrigger.getHostAffecteds()) {
		System.out.println(hostAffected.getHostName());
		for (TriggerShot triggerShot : hostAffected.getTriggerShots()) {
		    System.out.println("  " + triggerShot.getDescription());
		    System.out.println("  " + triggerShot.getTime());
		}
	    }
	} catch (Exception e) {
	    System.out.println(e);
	}

	

	try {
	    MonitoringWrappedResponsePaas monitoringWrappedResponsePaas = client.getMonitoringServiceByTag("zabbix", "458");
	    for (Group cat : monitoringWrappedResponsePaas.getGroups()) {
		System.out.println(cat.getGroupName());

	    }
	} catch (RestClientException | NoMappingModelFoundException | MappingException | ServerErrorResponseException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}


    }

}
