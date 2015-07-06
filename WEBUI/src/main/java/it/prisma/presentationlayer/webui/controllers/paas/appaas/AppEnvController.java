package it.prisma.presentationlayer.webui.controllers.paas.appaas;

import it.prisma.domain.dsl.configuration.ConfigurationNames;
import it.prisma.domain.dsl.deployments.VirtualMachineRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.EnvironmentDetails;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.event.Events;
import it.prisma.domain.dsl.paas.services.generic.request.Account;
import it.prisma.domain.dsl.paas.services.generic.request.Environment;
import it.prisma.domain.dsl.paas.services.generic.request.ServiceDetails;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.request.AddAppSrcFileRequest;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.response.AddAppSrcFileResponse;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.PrismaErrorCode;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.commons.GenericManagementService;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.core.exceptions.PrismaError;
import it.prisma.presentationlayer.webui.core.paas.appaas.AppEnvManagementService;
import it.prisma.presentationlayer.webui.core.paas.appaas.AppRepositoryService;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.EnvConfigForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.EnvForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.EnvsourceFromURLForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.vos.IstanceNumber;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.presentationlayer.webui.vos.paas.appaas.EnvDeployResponse;
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
public class AppEnvController {

	static final Logger LOG = LogManager.getLogger(AppEnvController.class
			.getName());

	@Autowired
	private PrismaPlatformConfiguration prismaPlatformConfiguration;

	@Autowired
	private AppEnvManagementService appEnvManagementService;

	@Autowired
	private AppRepositoryService apRepositoryService;

	@Autowired
	private GenericManagementService<APPaaSEnvironmentRepresentation> genericService;

	@RequestMapping(value = "paas/appaas/{appId}/environments/{id}", method = RequestMethod.GET)
	public String getAppEnvFromID(EnvConfigForm envConfigForm,
			@PathVariable Long appId, @PathVariable Long id, Model model,
			@CurrentUser PrismaUserDetails user) {
		try {
			addModelAttributes(appId, id, user.getActiveWorkgroupMembership().getWorkgroupId(), envConfigForm, model, true);
			return "pages/paas/appaas/dashboard-env";
		} catch (Exception e) {
			throw ExceptionHelper.getPrismaException(e, AppEnvController.class);
		}

	}

	@RequestMapping(value = "async/paas/appaas/{appId}/environments/{serviceId}/update", method = RequestMethod.GET)
	public @ResponseBody PrismaJSONResponse getAppEnvRappresentation(
			@PathVariable Long appId, @PathVariable Long serviceId,
			Model model, @CurrentUser PrismaUserDetails user) {
		LOG.debug("APPaaSEnvironmentRepresentation UPDATE");
		try {
			APPaaSEnvironmentRepresentation result = appEnvManagementService
					.getAppaaSEnv(user.getActiveWorkgroupMembership().getWorkgroupId(), appId, serviceId);
			return new PrismaJSONResponse(true, true, result, null);
		} catch (Exception e) {
			throw ExceptionHelper.getPrismaException(e, AppEnvController.class);
		}
	}

	@RequestMapping(value = "async/paas/appaas/{id}/environments", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<APPaaSEnvironmentRepresentation> getPrivateApps(
			@PathVariable Long id,
			@PrismaRestWSParams RestWSParamsContainer params,
			HttpServletResponse response, @CurrentUser PrismaUserDetails user) {
		try {
			return appEnvManagementService.getAllAppaaSEnv(user.getActiveWorkgroupMembership().getWorkgroupId(), id, params);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value = "async/paas/appaas/{appId}/environments/{id}/events", method = RequestMethod.GET)
	public @ResponseBody Events getEventsJson(@PathVariable Long appId,
			@PathVariable Long id,
			@PrismaRestWSParams RestWSParamsContainer params,
			@CurrentUser PrismaUserDetails user) {

		LOG.debug("Update Events for service: APPaaSEnv with ID: " + id);

		// TODO creare il metodo con paramentro di ricerca
		return appEnvManagementService.getEvents(user.getActiveWorkgroupMembership().getWorkgroupId(), appId, id, params);

	}

	@RequestMapping(value = "paas/appaas/{appId}/environments/create", method = RequestMethod.GET)
	public String create(@PathVariable String appId, Model model,
			@CurrentUser PrismaUserDetails user) {

		LOG.trace("create env for app " + appId);

		model.addAttribute("appId", appId);

		model.addAttribute("zone", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.AVAILABILITY_ZONE)));

		model.addAttribute("iaasFlavor", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_FLAVOR)));

		model.addAttribute("iaasProfile", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_PROFILE)));

		model.addAttribute("serverTypes", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.PAAS_APPAAS_SERVER_TYPE)));

		model.addAttribute("webServers", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.PAAS_APPAAS_WEB_SERVER)));
		model.addAttribute(
				"applicationServers",
				SelectHelper.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.PAAS_APPAAS_APPLICATION_SERVER)));

		model.addAttribute("loadBalancing", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.PAAS_LOAD_BALANCING)));

		model.addAttribute("uploadFileSize", prismaPlatformConfiguration
				.getValue(ConfigurationNames.UPLOAD_FILE_SIZE));

		return "pages/paas/appaas/create-env";
	}

	@RequestMapping(value = "async/paas/appaas/sourcefromurl", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse uploadSourceFromUrl(
			@CurrentUser PrismaUserDetails user,
			EnvsourceFromURLForm envSourceFromURLForm,
			HttpServletResponse response) {

		LOG.debug(envSourceFromURLForm.toString());
		AddAppSrcFileRequest request = new AddAppSrcFileRequest();

		request.setUserId(user.getUserData().getUserAccountId());
		request.setWorkgroupId(user.getActiveWorkgroupMembership().getWorkgroupId());

		request.setAppDescription(envSourceFromURLForm.getSourceLabelPRISMA());
		request.setAppName(envSourceFromURLForm.getSourceLabelPRISMA());
		request.setFileName(envSourceFromURLForm.getSourceLabelPRISMA());
		request.setTag(envSourceFromURLForm.getSourceLabelPRISMA());

		if (envSourceFromURLForm.getAppVisibilityPRISMA().equalsIgnoreCase(
				"public"))
			request.setPublic(true);
		else
			request.setPublic(false);

		request.setURL(envSourceFromURLForm.getUrlStorage());
		PrismaJSONResponse prismaResponse = new PrismaJSONResponse(true);
		response.setStatus(200);
		try {
			AddAppSrcFileResponse fileResponse = apRepositoryService
					.uploadAppSRCFileFromURL(request);
			prismaResponse.setSuccess(true);
			prismaResponse.setResult(fileResponse);
			return prismaResponse;
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e,
					AppEnvController.class);
		}
	}

	@RequestMapping(value = "async/paas/appaas/{appId}/environments/deploy", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse deployEnv(@Valid EnvForm form,
			@PathVariable String appId, BindingResult bindingResult,
			Model model, @CurrentUser PrismaUserDetails user) {

		APPaaSEnvironmentRepresentation envRap = null;
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		List<PrismaError> errors = new ArrayList<PrismaError>();

		if (bindingResult.hasErrors()) {
			response.setSuccess(false);
			for (Object object : bindingResult.getAllErrors()) {
				if (object instanceof FieldError) {
					FieldError fieldError = (FieldError) object;
					errors.add(new PrismaError(PrismaErrorCode.GENERIC,
							fieldError.getCode(), fieldError.getField()));
				}
			}
			response.setResult(errors);
			return response;
		} else {

			Account account = new Account();
			account.setUserId(user.getUserData().getUserAccountId());
			// TODO change with user workgroup
			account.setWorkgroupId(user.getActiveWorkgroupMembership().getWorkgroupId());
			Environment env = new Environment();
			env.setIaaSFlavor(form.getFlavorSelect());
			env.setQos(form.getQosSelect());
			env.setAvailabilityZone(form.getZoneSelect());

			ServiceDetails svcDet = new ServiceDetails();
			svcDet.setEnvironment(env);
			svcDet.setName(form.getServiceName());
			svcDet.setDescription(form.getDescription());
			svcDet.setDomainName(form.getUrl() + form.getDomainName());
			svcDet.setNotificationEmail(form.getNotificationEmail());
			svcDet.setPublicVisibility(true);

			// TODO
			svcDet.setSecureConnection(null);

			EnvironmentDetails envDetails = new EnvironmentDetails();
			envDetails.setAppId(appId);
			envDetails.setServerType(form.getServerName());

			APPaaSEnvironmentDeployRequest appEnvReq = new APPaaSEnvironmentDeployRequest();
			appEnvReq.setAccount(account);
			appEnvReq.setServiceDetails(svcDet);
			appEnvReq.setEnvironmentDetails(envDetails);

			switch (form.getSource()) {
			case "localApp":
				envDetails.setAppFileId(form.getFileuploadId());
				break;
			case "prismaApp":
				envDetails.setAppFileId(form.getFileURLId());
				break;
			case "privateApp":
				envDetails.setAppFileId(form.getInputPrivateId());
				break;
			case "publicApp":
				envDetails.setAppFileId(form.getInputPublicId());
				break;
			}

			try {
				IstanceNumber scalingNumber = IstanceNumber.lookupFromName(form
						.getScalingSelect());

				svcDet.setLoadBalancingInstances(scalingNumber.getNumber());
			} catch (IllegalArgumentException e) {
				LOG.error("Unable to find number of istance in enum, used default value");
				svcDet.setLoadBalancingInstances(1);
			}

			try {
				envRap = appEnvManagementService.deployEnv(appEnvReq);

				EnvDeployResponse envResp = new EnvDeployResponse();
				envResp.setAppId(envRap.getAppaasId());
				envResp.setEnvId(envRap.getId());
				envResp.setName("appaas");
				response.setResult(envResp);
				return response;
			} catch (Exception e) {
				return ExceptionHelper.getPrismaJSONResponse(e,
						AppEnvController.class);
			}
		}
	}

	@RequestMapping(value = "paas/appaas/{appId}/environments/{envId}", method = RequestMethod.POST)
	public String changeConfiguration(@Valid EnvConfigForm envConfigForm,
			BindingResult bindingResult, @PathVariable Long appId,
			@PathVariable Long envId, Model model, @CurrentUser PrismaUserDetails user) {

		if (bindingResult.hasErrors()) {
			LOG.debug("Errori nel form");
			for (ObjectError e : bindingResult.getAllErrors()) {
				LOG.debug(e.getObjectName() + " " + e.getDefaultMessage());
			}
		}

		try {
			addModelAttributes(appId, envId, user.getActiveWorkgroupMembership().getWorkgroupId(), envConfigForm, model, false);
		} catch (Exception e) {
			throw ExceptionHelper.getPrismaException(e, APPaaSController.class);
		}

		return "pages/paas/appaas/dashboard-env";
	}

	@RequestMapping(value = "paas/appaas/{appId}/environments/{envId}/vms/{vmId}", method = RequestMethod.GET)
	public String vmDetails(@PathVariable Long appId, @PathVariable Long envId,
			@PathVariable Long vmId) {

		// TODO get data from backend
		return "pages/paas/appaas/dashboard-vm";
	}

	/**
	 * 
	 * @param appId
	 * @param envId
	 * @param workgroupId
	 * @param envConfigForm
	 * @param model
	 * @param isDashboardActive
	 *            serve nel model per capire quale tab visualizzare
	 */
	private void addModelAttributes(Long appId, Long envId, Long workgroupId,
			EnvConfigForm envConfigForm, Model model, boolean isDashboardActive)
			throws Exception {

		APPaaSEnvironmentRepresentation env = appEnvManagementService
				.getAppaaSEnv(workgroupId, appId, envId);

		model.addAttribute("service", env);
		model.addAttribute("appId", env.getAppaasId());
		model.addAttribute("serviceId", env.getId());

		model.addAttribute("iaasFlavor", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_FLAVOR)));
		model.addAttribute("iaasProfile", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.IAAS_PROFILE)));

		model.addAttribute("loadBalancing", SelectHelper
				.generateSelectElements(prismaPlatformConfiguration
						.getValue(ConfigurationNames.PAAS_LOAD_BALANCING)));

		model.addAttribute("instanceScaling",
				IstanceNumber.lookupFromCode(env.getLoadBalancingInstances())
						.getText());

		envConfigForm.setServiceName(env.getName());
		envConfigForm.setDomainName(env.getDomainName());
		envConfigForm.setNotificationEmail(env.getNotificationEmail());

		model.addAttribute("envConfigForm", envConfigForm);
		model.addAttribute("isDashboard", isDashboardActive);
		model.addAttribute("isConfiguration", !isDashboardActive);

		envConfigForm.setNotificationEmail(env.getNotificationEmail());
		List<VirtualMachineRepresentation> vms = null;
		try {
			vms = appEnvManagementService.getVirtualMachines(workgroupId,
					appId, envId);
		} catch (Exception e) {
			LOG.error("Unable to retrieve the list of VMS that compose the environment");
		}
		model.addAttribute("vms", vms);
		model.addAttribute("workgroupId", env.getWorkgroupId());
		model.addAttribute("name", env.getName());
	}

	@RequestMapping(value = "async/workgroups/{workgroupId}/paas/appaas/{appId}/environments/{id}", method = RequestMethod.DELETE)
	public @ResponseBody PrismaJSONResponse undeployEnv(
			@PathVariable Long workgroupId, @PathVariable Long appId,
			@PathVariable Long id, @CurrentUser PrismaUserDetails user) {
		LOG.trace("Undeploy APPenvironment request start");
		APPaaSEnvironmentRepresentation aPPaaSEnvironmentRepresentation = new APPaaSEnvironmentRepresentation();
		aPPaaSEnvironmentRepresentation.setId(id);
		aPPaaSEnvironmentRepresentation.setAppaasId(appId);
		aPPaaSEnvironmentRepresentation.setWorkgroupId(workgroupId);
		try {
			genericService.deleteService(aPPaaSEnvironmentRepresentation);
			return new PrismaJSONResponse(true, true, null, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e,
					AppEnvController.class);
		}
	}

}