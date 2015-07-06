package it.prisma.presentationlayer.webui.controllers.paas.appaas;

import it.prisma.domain.dsl.paas.services.appaas.response.ApplicationRepositoryEntryRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.paas.appaas.AppRepositoryService;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppRepositoryController {

	static final Logger LOG = LogManager.getLogger(AppRepositoryController.class
			.getName());
	
	@Autowired
	private AppRepositoryService appRepositoryService;
	
	
	@RequestMapping(value = "async/apprepo/appsrcfiles/private", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<ApplicationRepositoryEntryRepresentation> getPrivateApps(@PrismaRestWSParams RestWSParamsContainer params, HttpServletResponse response, @CurrentUser PrismaUserDetails user) {

		try {
			return appRepositoryService.getPrivateApps(user.getActiveWorkgroupMembership().getWorkgroupId(), params);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}
	
	@RequestMapping(value = "async/apprepo/appsrcfiles/public", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<ApplicationRepositoryEntryRepresentation> getPublicApps(@PrismaRestWSParams RestWSParamsContainer params, HttpServletResponse response) {
		try {
			return appRepositoryService.getPublicApps(params);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}
}
