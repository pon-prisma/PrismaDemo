package it.prisma.presentationlayer.webui.controllers.paas;

import it.prisma.domain.dsl.configuration.ConfigurationNames;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.controllers.PrismaController;
import it.prisma.presentationlayer.webui.core.IPrismaService;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.commons.GenericManagementService;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.DBForm;
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
public class DBaaSController extends PrismaController<DBaaSRepresentation, DBForm> {

	static final Logger LOG = LogManager.getLogger(DBaaSController.class.getName());
	
	@Autowired
	private PrismaPlatformConfiguration prismaPlatformConfiguration;

	@Autowired
	@Qualifier("dbaas")
	private IPrismaService<DBaaSRepresentation, DBaaSDeployRequest> service;
	
	@Autowired
	private GenericManagementService<DBaaSRepresentation> genericService;

	@Override
	@RequestMapping(value = "/paas/dbaas/", method = RequestMethod.GET)
	public String listServicePage(@CurrentUser PrismaUserDetails user, Model model) {		
		return "pages/paas/dbaas/list-dbaas";
	}

	@Override
	@RequestMapping(value = "/async/paas/dbaas", method = RequestMethod.GET)
	public BootstrapTable<DBaaSRepresentation> getAllServicesTableRepresentation(RestWSParamsContainer params, @CurrentUser PrismaUserDetails user, HttpServletResponse response) {
		try {
			return service.getAllServiceTableRepresentation(user.getActiveWorkgroupMembership().getWorkgroupId(), params, DBaaSRepresentation.class);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

	@Override
	@RequestMapping(value = "/paas/dbaas/create", method = RequestMethod.GET)
	public String createServicePage(@CurrentUser PrismaUserDetails user, Model model) {
		model.addAttribute("iaasProfile", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_PROFILE)));
		model.addAttribute("iaasFlavor", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_FLAVOR)));
		model.addAttribute("zone", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.AVAILABILITY_ZONE)));
		model.addAttribute("dbms", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.PAAS_DBAAS_DBMS)));

		model.addAttribute("backup", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.PAAS_DBAAS_BACKUP)));

		return "pages/paas/dbaas/create-dbaas";
	}

	@Override
	@RequestMapping(value = "async/paas/dbaas/update/{workgroup}/{id}", method = RequestMethod.GET)
	public PrismaJSONResponse getServiceRepresentation(@PathVariable Long workgroup, @PathVariable Long id,  @CurrentUser PrismaUserDetails user) {

		try {
			DBaaSRepresentation result = service.getServiceRepresentation(workgroup, null, id, DBaaSRepresentation.class);
			return new PrismaJSONResponse(true, true, result, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, DBaaSController.class);
		}
	}

	@Override
	@RequestMapping(value = "paas/dbaas/{id}", method = RequestMethod.GET)
	public String getServicePageById(@PathVariable Long id,  @CurrentUser PrismaUserDetails user, Model model) {
		
		try {
			LOG.debug("DBaaS instance page");
			model.addAttribute("iaasProfile", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.IAAS_PROFILE)));

			model.addAttribute("iaasFlavor", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.IAAS_FLAVOR)));

			model.addAttribute("DBaaSBackup", SelectHelper
					.generateSelectElements(prismaPlatformConfiguration
							.getValue(ConfigurationNames.PAAS_DBAAS_BACKUP)));

			DBaaSRepresentation dbaas = service.getServiceRepresentation(user.getActiveWorkgroupMembership().getWorkgroupId(), null, id, DBaaSRepresentation.class);
			// Set model only configuration variable. Used in js to fill the tab
			// "Configurazione"
			model.addAttribute("notificationEmail", dbaas.getNotificationEmail());
			model.addAttribute("flavorSelected", dbaas.getIaaSFlavor());
			model.addAttribute("profileSelected", dbaas.getQoS());
			model.addAttribute("verticalScaling", "false");
			if (dbaas.getVerticalScalingEnabled()) {
				model.addAttribute("verticalScaling", "true");
				model.addAttribute("maxFlavor", dbaas.getVerticalScalingMaxFlavor());
				model.addAttribute("maxDisk", dbaas.getVerticalScalingMaxDiskSize());
			}
			model.addAttribute("dbaas", dbaas);
			model.addAttribute("dbaasId", dbaas.getId());
			model.addAttribute("workgroupId", dbaas.getWorkgroupId());
			model.addAttribute("name", dbaas.getName());
			// TODO fix get name
			String domainName[] = dbaas.getDomainName().split("\\.");
			model.addAttribute("domainName", domainName[0]);
			return "pages/paas/dbaas/dashboard-dbaas";
		} catch (Exception e) {
			throw ExceptionHelper.getPrismaException(e, DBaaSController.class);
		}
		
	}

	@Override
	@RequestMapping(value = "async/paas/dbaas/deploy", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse deployService(@Valid DBForm form, BindingResult bindingResult, @CurrentUser PrismaUserDetails user) {

		if (bindingResult.hasErrors()) {
			return getFormErrors(bindingResult);
		} else {
			
			try {
				DBaaSRepresentation dbaasRap = service.deployService(DBaaSRepresentation.class, MapperHelper.getDeployRequest(user, form));
				return new PrismaJSONResponse(true, true, dbaasRap, null);
			} catch (Exception e) {
				return ExceptionHelper.getPrismaJSONResponse(e, DBaaSController.class);
			}
		}
	}
	
	@Override
	@RequestMapping(value = "async/workgroups/{workgroupId}/paas/dbaas/{id}", method = RequestMethod.DELETE)
	public @ResponseBody PrismaJSONResponse undeployService(@PathVariable Long workgroupId, @PathVariable Long id, @CurrentUser PrismaUserDetails user) {
		LOG.trace("Undeploy DB request start");	
		DBaaSRepresentation dBaaSRepresentation = new DBaaSRepresentation();
		dBaaSRepresentation.setId(id);
		dBaaSRepresentation.setWorkgroupId(workgroupId);
		try {
			genericService.deleteService(dBaaSRepresentation);
			return new PrismaJSONResponse(true, true, null, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, DBaaSController.class);
		}
	}

}