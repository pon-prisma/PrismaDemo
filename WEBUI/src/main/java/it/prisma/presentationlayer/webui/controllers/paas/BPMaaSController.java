package it.prisma.presentationlayer.webui.controllers.paas;

import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/paas/bpmaas")
public class BPMaaSController {

	static final Logger LOG = LogManager.getLogger(BPMaaSController.class
			.getName());

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String selectionTest(Model model) {

		return "pages/paas/bpmaas/bpm-selection";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(@CurrentUser PrismaUserDetails user, Model model) {

		return "pages/paas/bpmaas/create-bpmaas";

	}

}
