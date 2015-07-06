package it.prisma.presentationlayer.webui.controllers.paas;

import it.prisma.domain.dsl.configuration.ConfigurationNames;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.controllers.PrismaController;
import it.prisma.presentationlayer.webui.core.IPrismaService;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.commons.GenericManagementService;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.MQForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.utility.MapperHelper;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.presentationlayer.webui.vos.platformconfig.PrismaPlatformConfiguration;
import it.prisma.presentationlayer.webui.vos.platformconfig.SelectHelper;

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
public class MQController extends PrismaController<MQaaSRepresentation, MQForm> {

	static final Logger LOG = LogManager
			.getLogger(MQController.class.getName());


	@Autowired
	private PrismaPlatformConfiguration prismaPlatformConfiguration;

	@Autowired
	@Qualifier("mqaas")
	private IPrismaService<MQaaSRepresentation, MQaaSDeployRequest> service;
	
	@Autowired
	private GenericManagementService<MQaaSRepresentation> genericService;

	@Override
	@RequestMapping(value = "/paas/mqaas", method = RequestMethod.GET)
	public String listServicePage(@CurrentUser PrismaUserDetails user, Model model) {
		return "pages/paas/mqaas/list-mqaas";
	}

	@Override
	@RequestMapping(value = "/async/paas/mqaas", method = RequestMethod.GET)
	public BootstrapTable<MQaaSRepresentation> getAllServicesTableRepresentation(
			RestWSParamsContainer params, @CurrentUser PrismaUserDetails user,
			HttpServletResponse response) {
		try {
			return service.getAllServiceTableRepresentation(user.getActiveWorkgroupMembership().getWorkgroupId(), params, MQaaSRepresentation.class);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

	@Override
	@RequestMapping(value = "/paas/mqaas/create", method = RequestMethod.GET)
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
		model.addAttribute("loadBalancing", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.PAAS_LOAD_BALANCING)));
		model.addAttribute("rabbitVersions", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.PAAS_RABBIT_VERSIONS)));

		return "pages/paas/mqaas/create-mqaas";
	}

	@Override
	@RequestMapping(value = "async/paas/mqaas/update/{workgroup}/{id}", method = RequestMethod.GET)
	public PrismaJSONResponse getServiceRepresentation(
			@PathVariable Long workgroup, @PathVariable Long id,
			@CurrentUser PrismaUserDetails user) {

		try {
			MQaaSRepresentation result = service.getServiceRepresentation(workgroup, null, id, MQaaSRepresentation.class);
			return new PrismaJSONResponse(true, true, result, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, MQController.class);
		}
	}

	@Override
	@RequestMapping(value = "paas/mqaas/{id}", method = RequestMethod.GET)
	public String getServicePageById(@PathVariable Long id,
			@CurrentUser PrismaUserDetails user, Model model) {

		try {
			LOG.debug("MQaaS instance page");
			model.addAttribute("iaasProfile", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.IAAS_PROFILE)));
			model.addAttribute("iaasFlavor", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.IAAS_FLAVOR)));
			model.addAttribute("zone", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.AVAILABILITY_ZONE)));
			model.addAttribute("loadBalancing", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.PAAS_LOAD_BALANCING)));
			model.addAttribute("rabbitVersions", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.PAAS_RABBIT_VERSIONS)));

			MQaaSRepresentation mqaas = service.getServiceRepresentation(
				user.getActiveWorkgroupMembership().getWorkgroupId(), null, id, MQaaSRepresentation.class);
			// Set model only configuration variable. Used in js to fill the tab
			// "Configurazione"
			model.addAttribute("notificationEmail",
					mqaas.getNotificationEmail());
			model.addAttribute("flavorSelected", mqaas.getIaaSFlavor());
			model.addAttribute("profileSelected", mqaas.getQoS());
			model.addAttribute("verticalScaling", "false");
			// if (dbaas.get()) {
			// model.addAttribute("verticalScaling", "true");
			// model.addAttribute("maxFlavor",
			// dbaas.getVerticalScalingMaxFlavor());
			// model.addAttribute("maxDisk",
			// dbaas.getVerticalScalingMaxDiskSize());
			// }
			model.addAttribute("mqaas", mqaas);
			model.addAttribute("mqaasId", mqaas.getId());
			model.addAttribute("workgroupId", mqaas.getWorkgroupId());
			model.addAttribute("name", mqaas.getName());
			// TODO fix get name
			String domainName[] = mqaas.getDomainName().split("\\.");
			model.addAttribute("domainName", domainName[0]);
			return "pages/paas/mqaas/dashboard-mqaas";
		} catch (Exception e) {
			throw ExceptionHelper.getPrismaException(e, MQController.class);
		}

	}

	@Override
	@RequestMapping(value = "async/paas/mqaas/deploy", method = RequestMethod.POST)
	public @ResponseBody
	PrismaJSONResponse deployService(@Valid MQForm form,
			BindingResult bindingResult, @CurrentUser PrismaUserDetails user) {

		LOG.trace("Queue deploy starts");
		if (bindingResult.hasErrors()) {
			return getFormErrors(bindingResult);
		}
		try {
			MQaaSRepresentation mqaasRap = service.deployService(MQaaSRepresentation.class, MapperHelper.getDeployRequest(user, form));
			return new PrismaJSONResponse(true, true, mqaasRap, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, MQController.class);
		}
	}
	
	@Override
	@RequestMapping(value = "async/workgroups/{workgroupId}/paas/mqaas/{id}", method = RequestMethod.DELETE)
	public @ResponseBody PrismaJSONResponse undeployService(@PathVariable Long workgroupId, @PathVariable Long id, @CurrentUser PrismaUserDetails user) {
		LOG.trace("Undeploy MQ request start");	
		MQaaSRepresentation mQaaSRepresentation = new MQaaSRepresentation();
		mQaaSRepresentation.setId(id);
		mQaaSRepresentation.setWorkgroupId(workgroupId);
		try {
			genericService.deleteService(mQaaSRepresentation);
			return new PrismaJSONResponse(true, true, null, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, MQController.class);
		}
	}
}
