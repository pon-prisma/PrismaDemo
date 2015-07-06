package it.prisma.presentationlayer.webui.controllers.paas.appaas;

import it.prisma.domain.dsl.configuration.ConfigurationNames;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;
import it.prisma.domain.dsl.paas.services.generic.request.Account;
import it.prisma.domain.dsl.paas.services.generic.request.ServiceDetails;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.PrismaErrorCode;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.core.exceptions.PrismaError;
import it.prisma.presentationlayer.webui.core.paas.appaas.APPaaSManagementService;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.APPaaSForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.AppConfigForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.presentationlayer.webui.vos.platformconfig.PrismaPlatformConfiguration;
import it.prisma.presentationlayer.webui.vos.platformconfig.SelectHelper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class APPaaSController {

	static final Logger LOG = LogManager.getLogger(APPaaSController.class
			.getName());

	@Autowired
	private APPaaSManagementService appaasService;

	@Autowired
	private PrismaPlatformConfiguration prismaPlatformConfiguration;

	@RequestMapping(value = "/paas/appaas", method = RequestMethod.GET)
	public String getApps(Model model) {
		return "pages/paas/appaas/list-appaas";
	}

	@RequestMapping(value = "/async/paas/appaas", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<APPaaSRepresentation> getAllApps(
			@PrismaRestWSParams RestWSParamsContainer params,
			HttpServletResponse response, @CurrentUser PrismaUserDetails user) {
		try {
			return appaasService.getAllApps(user.getActiveWorkgroupMembership().getWorkgroupId(), params);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value = "paas/appaas/{id}", method = RequestMethod.GET)
	public String getAppaaSFromID(AppConfigForm appConfigForm, Model model,
			@PathVariable Long id, @CurrentUser PrismaUserDetails user) {

		try {
			addModelAttributes(id, user.getActiveWorkgroupMembership().getWorkgroupId(), appConfigForm, model, true, "", false);
			return "pages/paas/appaas/dashboard-appaas";
		} catch (Exception e) {
			throw ExceptionHelper.getPrismaException(e, APPaaSController.class);
		}
	}

	/**
	 * Questo controller viene invocato per visualizzare la pagina di creazione
	 * 
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/paas/appaas/create", method = RequestMethod.GET)
	public String create(@CurrentUser PrismaUserDetails user, Model model) {
		return "pages/paas/appaas/create-appaas";
	}

	@RequestMapping(value = "paas/appaas/{appId}", method = RequestMethod.POST)
	public String changeConfiguration(@Valid AppConfigForm appConfigForm,
			BindingResult bindingResult, @PathVariable Long appId, Model model, @CurrentUser PrismaUserDetails user) {

		Long wgId=user.getActiveWorkgroupMembership().getWorkgroupId();
		if (bindingResult.hasErrors()) {
			LOG.debug("Errori nel form");
			for (ObjectError e : bindingResult.getAllErrors()) {
				LOG.debug(e.getObjectName() + " " + e.getDefaultMessage());
			}
		} else {
			try {
				appaasService.updateConfiguration(wgId, appId, appConfigForm);
				addModelAttributes(appId, wgId, appConfigForm, model, false,
						"Salvato", true);
			} catch (Exception e) {
				try {
					addModelAttributes(appId, wgId, appConfigForm, model, false,
							"Errore", false);
				} catch (Exception exception) {
					throw ExceptionHelper.getPrismaException(exception,
							AppEnvController.class);
				}
			}
		}

		return "pages/paas/appaas/dashboard-appaas";
	}

	/**
	 * 
	 * Invocato quando si vuole creare una App
	 * 
	 * @param appForm
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "async/paas/appaas/deploy", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse deployAPPaaS(
			@Valid APPaaSForm appForm, BindingResult bindingResult, Model model, @CurrentUser PrismaUserDetails user) {

		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		List<PrismaError> errors = new ArrayList<PrismaError>();

		if (bindingResult.hasErrors()) {
			response.setSuccess(false);
			for (Object object : bindingResult.getAllErrors()) {
				if (object instanceof FieldError) {
					FieldError fieldError = (FieldError) object;
					errors.add(new PrismaError(PrismaErrorCode.FIELD_ERROR,
							fieldError.getCode(), fieldError.getField()));
				}
			}
			LOG.debug("APP deploy has some error " + errors.toString());
			response.setResult(errors);
			return response;
		} else {
			APPaaSDeployRequest appDeploy = new APPaaSDeployRequest();
			Account account = new Account();
			appDeploy.setAccount(account);
			account.setUserId(user.getUserData().getUserAccountId());
			//TODO change with user workgroup
			account.setWorkgroupId(user.getActiveWorkgroupMembership().getWorkgroupId());
			ServiceDetails appParams = new ServiceDetails();
			appParams.setName(appForm.getServiceName());
			appParams.setDescription(appForm.getDescription());
			appParams.setPublicVisibility(true);
			appDeploy.setServiceDetails(appParams);

			try {
				APPaaSRepresentation appRappresentation = appaasService
						.deploy(appDeploy);
				LOG.debug(appDeploy.toString());
				return new PrismaJSONResponse(true, true, appRappresentation,
						null);
			} catch (Exception e) {
				return ExceptionHelper.getPrismaJSONResponse(e,
						AppEnvController.class);
			}

		}
	}

	private void addModelAttributes(Long appId, Long workgroupId,
			AppConfigForm appConfigForm, Model model,
			boolean isDashboardActive, String configurationStatus,
			boolean isUpdated) throws Exception {

		APPaaSRepresentation appaas = appaasService.getAppaaS(workgroupId,
				appId);

		model.addAttribute("service", appaas);
		model.addAttribute("iaasProfile", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_PROFILE)));
		model.addAttribute("iaasFlavor", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_FLAVOR)));

		appConfigForm.setServiceName(appaas.getName());
		appConfigForm.setDescription(appaas.getDescription());

		model.addAttribute("appConfigForm", appConfigForm);
		model.addAttribute("isDashboard", isDashboardActive);
		model.addAttribute("isConfiguration", !isDashboardActive);
		if (!isDashboardActive) {
			model.addAttribute("configurationStatus", configurationStatus);
			model.addAttribute("isUpdated", isUpdated);
		}
	}

}