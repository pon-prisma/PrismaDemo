package it.prisma.presentationlayer.webui.controllers.homes;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DashboardHomeController {

	static final Logger LOG = LogManager.getLogger(DashboardHomeController.class
			.getName());
		
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String general(Model model) throws IOException {		
		return "pages/dashboard-home";
	}
	
}