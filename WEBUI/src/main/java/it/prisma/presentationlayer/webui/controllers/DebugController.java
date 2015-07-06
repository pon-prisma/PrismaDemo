package it.prisma.presentationlayer.webui.controllers;

import it.prisma.domain.dsl.prisma.DebugInformations;
import it.prisma.presentationlayer.webui.configs.ConfigProperties;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DebugController {

	@Autowired
	ConfigProperties cfgProperties;

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	@RequestMapping(value = "/debug", method = RequestMethod.GET)
	public String underCostruction(Model model) {

		try {
			model.addAttribute("hostname", java.net.InetAddress.getLocalHost()
					.getHostName());
		} catch (IOException e) {
			model.addAttribute("hostname", "Unable to get Hostname");
		}

		model.addAttribute("projectVersion", cfgProperties.getProjectVersion());

		try {
			DebugInformations debugInfo = prismaBizAPIClient
					.getDebugInformations();
			model.addAttribute("debugInfo", debugInfo);
		} catch (Exception e) {
		}

		return "pages/debug";
	}

}
