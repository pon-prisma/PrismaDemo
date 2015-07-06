package it.prisma.presentationlayer.webui.controllers.paas;

import it.prisma.domain.dsl.configuration.ConfigurationNames;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.controllers.PrismaController;
import it.prisma.presentationlayer.webui.core.IPrismaService;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.commons.GenericManagementService;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.core.iaas.keypairs.KeypairService;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas.BIClientForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas.BIForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas.BIServerAndClientForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas.BIServerForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.utility.MapperHelper;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.presentationlayer.webui.vos.platformconfig.PrismaPlatformConfiguration;
import it.prisma.presentationlayer.webui.vos.platformconfig.SelectHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BIaaSController extends
		PrismaController<BIaaSRepresentation, BIForm> {

	static final Logger LOG = LogManager.getLogger(BIaaSController.class
			.getName());

	@Autowired
	private HttpServletRequest request;

	@Autowired
	@Qualifier("biaas")
	private IPrismaService<BIaaSRepresentation, BIaaSDeployRequest> service;

	@Autowired
	private GenericManagementService<BIaaSRepresentation> genericService;

	@Autowired
	private PrismaPlatformConfiguration prismaPlatformConfiguration;

	@Autowired
	private KeypairService keypairService;

	@Override
	@RequestMapping(value = "/paas/biaas/", method = RequestMethod.GET)
	public String listServicePage(@CurrentUser PrismaUserDetails user,
			Model model) {
		return "pages/paas/biaas/list-biaas";
	}

	/**
	 * With this controller to get the page for selection of Business
	 * Intelligence instances
	 * 
	 * @param user
	 *            -logged in the system
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/paas/biaas/selection", method = RequestMethod.GET)
	public String selection(@CurrentUser PrismaUserDetails user, Model model) {
		return "pages/paas/biaas/selection";
	}

	@Override
	@RequestMapping(value = "/async/paas/biaas", method = RequestMethod.GET)
	public BootstrapTable<BIaaSRepresentation> getAllServicesTableRepresentation(
			RestWSParamsContainer params, @CurrentUser PrismaUserDetails user,
			HttpServletResponse response) {
		try {
			return service.getAllServiceTableRepresentation(user.getActiveWorkgroupMembership().getWorkgroupId(), params,
					BIaaSRepresentation.class);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

	@Override
	@RequestMapping(value = "/paas/biaas/create/pentaho", method = RequestMethod.GET)
	public String createServicePage(@CurrentUser PrismaUserDetails user,
			Model model) {
		model.addAttribute("iaasProfile", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_PROFILE)));
		model.addAttribute("iaasFlavor", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_FLAVOR)));
		model.addAttribute("zone", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.AVAILABILITY_ZONE)));

		// TODO migliorare con Enum
		String type = request.getParameter("type");
		switch (type) {
		case "server":
			return "pages/paas/biaas/create-pentaho-server";
		case "client":
			try {
				model.addAttribute("keypairs", keypairService
						.getAllkeypairs(user.getActiveWorkgroupMembership()
								.getWorkgroupId()));
				return "pages/paas/biaas/create-pentaho-client";
			} catch (Exception e) {
				throw ExceptionHelper.getPrismaException(e,
						BIaaSController.class);
			}

		case "server-and-client":
			try {
				model.addAttribute("keypairs", keypairService
						.getAllkeypairs(user.getActiveWorkgroupMembership()
								.getWorkgroupId()));
				return "pages/paas/biaas/create-pentaho-server-and-client";
			} catch (Exception e) {
				throw ExceptionHelper.getPrismaException(e,
						BIaaSController.class);
			}
		default:
			return "redirect:/prisma/error";
		}
	}

	@Override
	@RequestMapping(value = "async/paas/biaas/update/{workgroup}/{id}", method = RequestMethod.GET)
	public PrismaJSONResponse getServiceRepresentation(@PathVariable Long workgroup, @PathVariable Long id,  @CurrentUser PrismaUserDetails user) {
		try {
			BIaaSRepresentation result = service.getServiceRepresentation(workgroup, null, id, BIaaSRepresentation.class);
			return new PrismaJSONResponse(true, true, result, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, DBaaSController.class);
		}
	}

	@Override
	@RequestMapping(value = "paas/biaas/{id}", method = RequestMethod.GET)
	public String getServicePageById(@PathVariable Long id, @CurrentUser PrismaUserDetails user, Model model) {
		try {
		
			BIaaSRepresentation bi = service.getServiceRepresentation(user.getActiveWorkgroupMembership().getWorkgroupId(), null, id, BIaaSRepresentation.class);
			
			model.addAttribute("service", bi);
			
			String publicKey = (String) bi.getProductSpecificParameters().getAdditionalProperties().get("userPublicKey");
			if(publicKey!=null)
				model.addAttribute("serviceUserPublicKey", publicKey);
			
			return "pages/paas/biaas/dashboard-biaas";
		} catch (Exception e) {
			throw ExceptionHelper.getPrismaException(e, DBaaSController.class);
		}
	}

	@RequestMapping(value = "async/paas/biaas/deploy-server", method = RequestMethod.POST)
	public @ResponseBody
	PrismaJSONResponse deployPentahoServer(@Valid BIServerForm form,
			BindingResult bindingResult, @CurrentUser PrismaUserDetails user) {
		if (bindingResult.hasErrors()) {
			return getFormErrors(bindingResult);
		} else {
			return deployService(form, bindingResult, user);
		}
	}

	@RequestMapping(value = "async/paas/biaas/deploy-client", method = RequestMethod.POST)
	public @ResponseBody
	PrismaJSONResponse deployPentahoClient(@Valid BIClientForm form,
			BindingResult bindingResult, @CurrentUser PrismaUserDetails user) {
		if (bindingResult.hasErrors()) {
			return getFormErrors(bindingResult);
		} else {
			return deployService(form, bindingResult, user);
		}
	}

	@RequestMapping(value = "async/paas/biaas/deploy-server-and-client", method = RequestMethod.POST)
	public @ResponseBody
	PrismaJSONResponse deployPentahoServerAndClient(
			@Valid BIServerAndClientForm form, BindingResult bindingResult,
			@CurrentUser PrismaUserDetails user) {
		if (bindingResult.hasErrors()) {
			return getFormErrors(bindingResult);
		} else {
			return deployService(form, bindingResult, user);
		}
	}

	@Override
	public @ResponseBody
	PrismaJSONResponse deployService(BIForm form, BindingResult bindingResult,
			PrismaUserDetails user) {
		try {
			BIaaSRepresentation BIService = service.deployService(
					BIaaSRepresentation.class,
					MapperHelper.getDeployRequest(user, form));
			return new PrismaJSONResponse(true, true, BIService, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e,
					BIaaSController.class);
		}
	}

	@Override
	@RequestMapping(value = "async/workgroups/{workgroupId}/paas/biaas/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	PrismaJSONResponse undeployService(@PathVariable Long workgroupId,
			@PathVariable Long id, @CurrentUser PrismaUserDetails user) {
		LOG.trace("Undeploy BI request start");
		BIaaSRepresentation bIaaSRepresentation = new BIaaSRepresentation();
		bIaaSRepresentation.setId(id);
		bIaaSRepresentation.setWorkgroupId(workgroupId);
		try {
			genericService.deleteService(bIaaSRepresentation);
			return new PrismaJSONResponse(true, true, null, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e,
					BIaaSController.class);
		}
	}
}
