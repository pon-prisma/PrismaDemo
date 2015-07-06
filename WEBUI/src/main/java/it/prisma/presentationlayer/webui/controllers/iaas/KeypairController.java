package it.prisma.presentationlayer.webui.controllers.iaas;

import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRepresentation;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRequest;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.core.iaas.keypairs.KeypairService;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KeypairController {

	static final Logger LOG = LogManager.getLogger(KeypairController.class
			.getName());

	@Autowired
	private KeypairService keypairService;

	@RequestMapping(value = "/iaas/keyaas/", method = RequestMethod.GET)
	public String getKeyPage(Model model) {	
		return "pages/iaas/keypairaas/list-keypair";
	}
	
	@RequestMapping(value = "/async/iaas/keyaas", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<KeypairRepresentation> getAllKeypair(@PrismaRestWSParams RestWSParamsContainer params, HttpServletResponse response, @CurrentUser PrismaUserDetails user) {
		try {
			return keypairService.getAllkeypairs(user.getActiveWorkgroupMembership().getWorkgroupId(), params);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value = "/async/iaas/keyaas/{name}", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse createKey(Model model, @PathVariable String name,
			HttpServletRequest request, @CurrentUser PrismaUserDetails user) {

		try {
			KeypairRepresentation result = keypairService.createKeypair(name, user.getActiveWorkgroupMembership().getWorkgroupId());
			request.getSession().setAttribute("name", name);
			request.getSession().setAttribute("privateKey", result.getPrivateKey());
			request.getSession().setMaxInactiveInterval(30000);
			result.setPrivateKey("");
			return new PrismaJSONResponse(true, true, result, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, KeypairController.class);
		}
	}

	@RequestMapping(value = "/async/iaas/keyaas/download", method = RequestMethod.GET)
	public void getFile(HttpServletRequest request, HttpServletResponse response) {		
		
		try {
			String name = (String) request.getSession().getAttribute("name");
			request.getSession().removeAttribute("name");
			String file = name + ".pem";
			String privateKey = (String) request.getSession().getAttribute(
					"privateKey");
			request.getSession().removeAttribute("public");
			if (name != null && privateKey != null) {
				InputStream is = new ByteArrayInputStream(privateKey.getBytes());
				
				response.reset();
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.setHeader("Content-Disposition","attachment; filename=\""+ file + "\"");
				
				org.apache.commons.io.IOUtils.copy(is,
						response.getOutputStream());
				response.flushBuffer();
			}		
		} catch (Exception ex) {
			LOG.error("Error writing file to output stream. Filename was '{}'",
					ex);
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			try {
				response.flushBuffer();
			} catch (IOException e) {
				LOG.error(e);
			}
		}
	}

	@RequestMapping(value = "/async/iaas/keyaas", method = RequestMethod.POST)
	public @ResponseBody
	PrismaJSONResponse importKey(Model model, @RequestBody final KeypairRequest keypair, @CurrentUser PrismaUserDetails user) {

		try {
			LOG.debug(keypair.toString());
			KeypairRepresentation result = keypairService.importKeypair(keypair, user.getActiveWorkgroupMembership().getWorkgroupId());

			return new PrismaJSONResponse(true, true, result, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, KeypairController.class);
		}
	}

	@RequestMapping(value = "async/iaas/keyaas/{name}", method = RequestMethod.DELETE)
	public @ResponseBody
	boolean deleteKey(Model model, @PathVariable String name, @CurrentUser PrismaUserDetails user) {

		try {
			return keypairService.deleteKeypair(name, user.getActiveWorkgroupMembership().getWorkgroupId());
		} catch (Exception e) {
			LOG.error("Exception deleting keypair " + name + ": "
					+ e.getMessage());
		}
		return false;
	}

}