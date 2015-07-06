package it.prisma.presentationlayer.webui.controllers.monitoring;

import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.IaaSHealth;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.presentationlayer.webui.core.monitoring.MonitoringManagementService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("async/monitoring")
public class MonitoringController {

    static final Logger LOG = LogManager.getLogger(MonitoringController.class.getName());

    @Autowired
    private MonitoringManagementService monitoringManagementService;

    @RequestMapping(value = "/iaasStatus", method = RequestMethod.GET)
    public @ResponseBody
    IaaSHealth iaasStatusTest() throws Exception {

	try {
	    return monitoringManagementService.getIaaSStatus();
	} catch (Exception e) {
	    IaaSHealth health = new IaaSHealth();
	    health.setAvailableNodes(0);
	    health.setTotalNodes(0);
	    health.setNetwork("KO");
	    health.setStorage("KO");
	    health.setCompute("not ready");
	    return health;
	}
    }

    /**********
     * VMaaS
     *********/
    @RequestMapping(value = "/metrics-vm", method = RequestMethod.GET)
    public String getMetricsVMPage(Model model) {

	return "pages/iaas/vmaas/metrics-vm";
    }

    /**************
     * VMaaS True
     **************/
    @RequestMapping(value = "/metrics-vm-true", method = RequestMethod.GET)
    public MonitoringWrappedResponsePaas getMetricsVMaasPageTest(Model model) {

	try {
	    return monitoringManagementService.getHistory4GraphsInfo();
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}

    }

    /**********
     * AppaaS
     *********/
    @RequestMapping(value = "/metrics-appaas", method = RequestMethod.GET)
    public String getMetricsAppaaSPage(Model model) {
	return "pages/paas/appaas/metrics-appaas";
    }

    /**********
     * DBaaS
     *********/
    @RequestMapping(value = "/metrics-dbaas", method = RequestMethod.GET)
    public String getMetricsDBaaSPage(Model model) {
	return "pages/paas/dbaas/metrics-dbaas";
    }

    /**********
     * MQaaS
     *********/
    @RequestMapping(value = "/metrics-mqaas", method = RequestMethod.GET)
    public String getMetricsMQaaSPage(Model model) {
	return "pages/paas/mqaas/metrics-mqaas";
    }

}
