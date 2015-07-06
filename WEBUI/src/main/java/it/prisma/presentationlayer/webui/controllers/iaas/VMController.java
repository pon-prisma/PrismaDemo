package it.prisma.presentationlayer.webui.controllers.iaas;

import it.prisma.domain.dsl.configuration.ConfigurationNames;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.controllers.PrismaController;
import it.prisma.presentationlayer.webui.controllers.paas.DBaaSController;
import it.prisma.presentationlayer.webui.core.IPrismaService;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.commons.GenericManagementService;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.core.iaas.keypairs.KeypairService;
import it.prisma.presentationlayer.webui.core.iaas.network.NetworkService;
import it.prisma.presentationlayer.webui.datavalidation.forms.iaas.VMForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.utility.MapperHelper;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.presentationlayer.webui.vos.platformconfig.PrismaPlatformConfiguration;
import it.prisma.presentationlayer.webui.vos.platformconfig.SelectHelper;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
public class VMController extends PrismaController<VMRepresentation, VMForm> {

	//private Logger LOG = LogManager.getLogger();
	
	@Autowired
	@Qualifier("vmaas")
	private IPrismaService<VMRepresentation, VMDeployRequest> service;
	
	@Autowired
	private GenericManagementService<VMRepresentation> genericService;

	@Autowired
	private KeypairService keypairService;

	@Autowired
	private NetworkService networkService;

	@Autowired
	private PrismaPlatformConfiguration prismaPlatformConfiguration;
	
	@Override
	@RequestMapping(value = "/iaas/vmaas", method = RequestMethod.GET)
	public String listServicePage(@CurrentUser PrismaUserDetails user, Model model) {
		
		model.addAttribute("images", SelectHelper.generateSelectElements(prismaPlatformConfiguration.getValue(ConfigurationNames.IAAS_IMAGES)));

		return "pages/iaas/vmaas/list-vm";
	}

	@Override
	@RequestMapping(value = "/async/iaas/vmaas", method = RequestMethod.GET)
	public BootstrapTable<VMRepresentation> getAllServicesTableRepresentation(RestWSParamsContainer params, @CurrentUser PrismaUserDetails user, HttpServletResponse response) {
		try {
			return service.getAllServiceTableRepresentation(user.getActiveWorkgroupMembership().getWorkgroupId(), params, VMRepresentation.class);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

	@Override
	@RequestMapping(value = "/iaas/vmaas/create", method = RequestMethod.GET)
	public String createServicePage(@CurrentUser PrismaUserDetails user, Model model) {
		try {
			model.addAttribute("iaasProfile", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue("iaas_profile")));

			model.addAttribute("iaasFlavor", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.IAAS_FLAVOR)));
			model.addAttribute("zone", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.AVAILABILITY_ZONE)));
			
			model.addAttribute("imageName", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue("iaas_images")));


			model.addAttribute("networks", networkService.getAllNetworks(user.getActiveWorkgroupMembership().getWorkgroupId()));
			model.addAttribute("keypairs", keypairService.getAllkeypairs(user.getActiveWorkgroupMembership().getWorkgroupId()));
			return "pages/iaas/vmaas/create-vm";

		} catch (Exception e) {
			throw ExceptionHelper.getPrismaException(e, VMController.class);
		}
	}

	@Override
	@RequestMapping(value = "async/iaas/vmaas/update/{workgroup}/{id}", method = RequestMethod.GET)
	public PrismaJSONResponse getServiceRepresentation(@PathVariable Long workgroup, @PathVariable Long id, @CurrentUser PrismaUserDetails user) {
		try {
			VMRepresentation result = service.getServiceRepresentation(workgroup, null, id, VMRepresentation.class);
			return new PrismaJSONResponse(true, true, result, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, DBaaSController.class);
		}
	}

	@Override
	@RequestMapping(value = "/iaas/vmaas/{id}", method = RequestMethod.GET)
	public String getServicePageById(@PathVariable Long id, @CurrentUser PrismaUserDetails user, Model model) {
		try{
			model.addAttribute("iaasProfile", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.IAAS_PROFILE)));

			model.addAttribute("iaasFlavor", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.IAAS_FLAVOR)));


			VMRepresentation serviceRepresentation = service.getServiceRepresentation(user.getActiveWorkgroupMembership().getWorkgroupId(), null, id, VMRepresentation.class);

			// Set model only configuration variable. Used in js to fill the tab
			// "Configurazione"
			model.addAttribute("notificationEmail", serviceRepresentation.getNotificationEmail());
			model.addAttribute("flavorSelected", serviceRepresentation.getIaaSFlavor());
			model.addAttribute("profileSelected", serviceRepresentation.getQoS());
			model.addAttribute("verticalScaling", "false");
			
			// Cerco il nome dal Bean, se  non lo trovo metto il valore della chiave
			String image = (SelectHelper.generateSelectElements(prismaPlatformConfiguration.getValue(ConfigurationNames.IAAS_IMAGES))).get(serviceRepresentation.getImageName());
			if(image != null)
				model.addAttribute("os", image);
			else
				model.addAttribute("os", serviceRepresentation.getImageName());

			model.addAttribute("vm", serviceRepresentation);
			model.addAttribute("id", serviceRepresentation.getId());
			model.addAttribute("workgroupId", serviceRepresentation.getWorkgroupId());
			model.addAttribute("name", serviceRepresentation.getName());

			return "pages/iaas/vmaas/dashboard-vm";
		} catch (Exception e) {
			throw ExceptionHelper.getPrismaException(e, VMController.class);
		}
	}

	@Override
	@RequestMapping(value = "async/iaas/vmaas/deploy", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse deployService(@Valid VMForm form, BindingResult bindingResult, @CurrentUser PrismaUserDetails user) {
		LOG.trace("Deploy VM request start");

		if (bindingResult.hasErrors()) {
			return getFormErrors(bindingResult);
		} else {

			VMDeployRequest vmRep = MapperHelper.getDeployRequest(user, form);
			
			try {
				VMRepresentation vm =  service.deployService(VMRepresentation.class, vmRep);
				return new PrismaJSONResponse(true, true, vm, null);
			} catch (Exception e) {
				return ExceptionHelper.getPrismaJSONResponse(e, VMController.class);
			}
		}
	}
	

	@Override
	@RequestMapping(value = "async/workgroups/{workgroupId}/iaas/vmaas/{id}", method = RequestMethod.DELETE)
	public @ResponseBody PrismaJSONResponse undeployService(@PathVariable Long workgroupId, @PathVariable Long id, @CurrentUser PrismaUserDetails user) {
		LOG.trace("Undeploy VM request start");
		VMRepresentation vMRepresentation = new VMRepresentation();
		vMRepresentation.setId(id);
		vMRepresentation.setWorkgroupId(workgroupId);
		try {
			genericService.deleteService(vMRepresentation);
			return new PrismaJSONResponse(true, true, null, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, VMController.class);
		}
	}

	
}
