package it.prisma.presentationlayer.webui.controllers;

import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UnderConstructionController {

	@RequestMapping(value="/under-costruction", method = RequestMethod.GET)
	public String underCostruction(@CurrentUser PrismaUserDetails user) {
		return "pages/under-costruction";
	}
	
}
