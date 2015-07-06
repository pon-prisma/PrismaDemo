package it.prisma.businesslayer.utils;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;

import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class LinkHelper {

	@Inject
	private EnvironmentConfig envConfig;

	public String getBaseBizLayerURL() {
		return envConfig
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_BIZLAYER_BASE_REST_URL);
	}
	
	public String getBaseWebUIURL() {
		return envConfig
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_WEBUI_BASE_REST_URL);
	}	

	public String getPaaSServiceLink(AbstractPaaSService paasService) {
		String baseURL = getBaseWebUIURL();
		String deployURL = baseURL;
		PaaSService paasSvc = paasService.getPaaSService();
		PaaSServiceType type = PaaSServiceType.valueOf(paasSvc.getType());
		if (type == PaaSServiceType.APPaaSEnvironment) {
			deployURL += "/paas/" + paasSvc.getType().toLowerCase() + "/"
					+ ((AppaaSEnvironment) paasService).getAppaaS().getId()
					+ "/environments/" + paasSvc.getId();
		} else if (type == PaaSServiceType.VMaaS) {
			deployURL += "/iaas/" + paasSvc.getType().toLowerCase() + "/"
					+ paasSvc.getId();
		} else {
			deployURL += "/paas/" + paasSvc.getType().toLowerCase() + "/"
					+ paasSvc.getId();
		}
		return deployURL;
	}
}
