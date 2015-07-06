package it.prisma.presentationlayer.webui.controllers.iaas;

import it.prisma.domain.dsl.iaas.network.NetworkRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.iaas.network.NetworkService;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NetworkController {

	static final Logger LOG = LogManager.getLogger(NetworkController.class
			.getName());

	@Autowired
	private NetworkService networkService;

	@RequestMapping(value = "/iaas/networkaas/", method = RequestMethod.GET)
	public String getKeyList(Model model) {

		return "pages/iaas/networkaas/list-network";
	}
	
	@RequestMapping(value = "/async/iaas/networks", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<NetworkRepresentation> getAllNetworks(@PrismaRestWSParams RestWSParamsContainer params, HttpServletResponse response, @CurrentUser PrismaUserDetails user) {
		try {
			return networkService.getAllNetworks(user.getActiveWorkgroupMembership().getWorkgroupId(), params);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

}