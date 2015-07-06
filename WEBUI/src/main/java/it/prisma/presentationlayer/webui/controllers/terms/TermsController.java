package it.prisma.presentationlayer.webui.controllers.terms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TermsController {

	@RequestMapping(value="/terms", method = RequestMethod.GET)
	public String terms() {
		return "pages/terms/terms";
	}
	
}
