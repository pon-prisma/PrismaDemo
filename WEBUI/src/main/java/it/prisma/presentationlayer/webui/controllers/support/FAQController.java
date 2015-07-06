package it.prisma.presentationlayer.webui.controllers.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FAQController {

	// Logger
	static final Logger LOG = LogManager.getLogger(FAQController.class.getName());

	@RequestMapping(value="/support/faq", method = RequestMethod.GET)
	public String selection() {

		LOG.trace("User requests the page FAQ");
		return "pages/support/faq";
	}
}
