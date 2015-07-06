package it.prisma.businesslayer.bizlib.orchestration;

import it.prisma.businesslayer.bizlib.misc.events.DeployingPrismaEvents;
import it.prisma.businesslayer.bizlib.misc.events.PrismaEvent;
import it.prisma.businesslayer.bizlib.paas.deployments.PaaSDeploymentManagement;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService.PaaSDeploymentServiceType;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PaaSServiceManagerBean implements PaaSServiceManager {

	@Inject
	private PaaSServiceEventDAO paaSSvcEvtDAO;

	@Inject
	private PaaSDeploymentManagement paasDeploymentMgmt;

	@Override
	public PaaSServiceEvent logPaaSServiceEvent(
			AbstractPaaSService paasService, String type, String details,
			String verbose) {

		return logPaaSServiceEvent(paasService.getPaaSService(),
				new PaaSServiceEvent(paasService.getPaaSService(), type,
						details, verbose));
	}

	@Override
	public PaaSServiceEvent logPaaSServiceEvent(PaaSService paasService,
			PaaSServiceEvent event) {
		event.setPaaSService(paasService);
		return paaSSvcEvtDAO.create(event);
	}

	@Override
	public void logPaaSServiceComponentsEvents(PaaSService paasService) {
		Map<String, List<PaaSDeploymentServiceInstance>> hosts = paasDeploymentMgmt
				.groupByHost(paasService.getPaaSDeployment());

		for (Map.Entry<String, List<PaaSDeploymentServiceInstance>> host : hosts
				.entrySet()) {

			String itemDescription = "Host ";
			String servicesDescription = "(services: [";

			// Retrieve ServiceInstances for each host
			for (PaaSDeploymentServiceInstance svcInst : host.getValue()) {
				servicesDescription += lookupTypeNL(svcInst
						.getPaaSDeploymentService().getType()) + ", ";
			}
			servicesDescription = servicesDescription.substring(0,
					servicesDescription.length() - 2);
			servicesDescription += "], IP:[";

			PaaSDeploymentServiceInstance hostData = host.getValue().iterator()
					.next();

			// Host IPs
			if (hostData.getPrivateIP() != null)
				servicesDescription += "Private: " + hostData.getPrivateIP()
						+ ", ";
			if (hostData.getPublicIP() != null)
				servicesDescription += "Public: " + hostData.getPublicIP()
						+ ", ";
			servicesDescription = servicesDescription.substring(0,
					servicesDescription.length() - 2);

			servicesDescription += "], ID: ";

			// Host ID
			servicesDescription += hostData.getHostId();
			servicesDescription += ")";

			PrismaEvent prismaEvt = new PrismaEvent(
					DeployingPrismaEvents.DEPLOY_ITEM_CREATED, itemDescription,
					servicesDescription);

			PaaSServiceEvent paaSSvcEvt = new PaaSServiceEvent(paasService,
					prismaEvt.getType().toString(), prismaEvt.getDetails());
			logPaaSServiceEvent(paasService, paaSSvcEvt);
		}
	}

	protected String lookupTypeNL(String paasSvcType/* , String version */) {
		try {

			PaaSDeploymentServiceType type = PaaSDeploymentServiceType
					.valueOf(paasSvcType);
			if (type == null)
				return paasSvcType;

			switch (type) {
			case AS_GLASSFISH:
				return "AS Glassfish";
			case AS_JBOSS:
				return "AS JBoss";
			case AS_TOMCAT:
				return "AS Tomcat";
			case DB_MySQL:
				return "DB MySQL";
			case DB_POSTGRE:
				return "DB Postgre";
			case LB_APACHE:
				return "LB Apache";
			case MQ_RABBITMQ:
				return "MQ RabbitMQ";
			case VM_LINUX:
				return "VM Linux";
			case WS_APACHE_HTTPD:
				return "WS Apache Httpd";
			case EX_PENTAHO:
				return "BI Pentaho Server";
			case RD_X2GO:
				return "RD X2GO Remote Desktop";
			}

			return paasSvcType;
		} catch (Exception e) {
			return "Unknown";
		}
	}
}
