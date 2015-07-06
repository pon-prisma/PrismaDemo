package it.prisma.presentationlayer.webui.core.monitoring;

import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.HypervisorInfo;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.IaaSHealth;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.domain.dsl.paas.services.smsaas.history.History;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class MonitoringManagementService {

    @Value("${DEFAULT_AUTH_TOKEN}")
    String DEFAULT_AUTH_TOKEN;

    @Value("${init.monitoring}")
    boolean initMonitoring;

    @Value("${fake.monitoring}")
    boolean fakeMonitoring;

    static final Logger LOG = LogManager.getLogger(MonitoringManagementService.class.getName());

    @Autowired
    private PrismaBizAPIClient prismaBizAPIClient;

    @PostConstruct
    public void init() {
	if (initMonitoring) {
	    LOG.debug("Init monitoring bean");
	    try {
		IaaSHealth openstackIaaSHealth = getIaaSStatus();
		LOG.debug(openstackIaaSHealth.toString());

		HypervisorInfo hypervisorInfo = getHypervisorAdminInfo();
		LOG.debug(hypervisorInfo.toString());
	    } catch (Exception e) {
		LOG.error("Init monitoring bean " + e.getMessage());
	    }
	} else {
	    LOG.info("Monitoring init disabled");
	}
    }

    public HypervisorInfo getHypervisorAdminInfo() throws Exception {

	return prismaBizAPIClient.getHypervisorAdminInfo(DEFAULT_AUTH_TOKEN);

    }

    @Cacheable("iaasStatus")
    public IaaSHealth getIaaSStatus() throws Exception {
	if (!fakeMonitoring) {
	    return prismaBizAPIClient.getIaaSStatus(DEFAULT_AUTH_TOKEN);
	} else {
	    IaaSHealth openstackIaaSHealth = new IaaSHealth();
	    openstackIaaSHealth.setAvailableNodes(1);
	    openstackIaaSHealth.setTotalNodes(1);
	    openstackIaaSHealth.setCompute("ready");
	    openstackIaaSHealth.setNetwork("ready");
	    openstackIaaSHealth.setStorage("ready");
	    return openstackIaaSHealth;
	}
    }

    public MonitoringWrappedResponsePaas getHistory4GraphsInfo() throws Exception {

	return prismaBizAPIClient.getPrismaGraph(DEFAULT_AUTH_TOKEN, "");

    }

}