package it.prisma.presentationlayer.webui.controllers.paas;

import it.prisma.domain.dsl.paas.services.emailaas.Credential;
import it.prisma.domain.dsl.paas.services.emailaas.EmailDomains;
import it.prisma.domain.dsl.paas.services.emailaas.EmailInfoRepresentation;
import it.prisma.domain.dsl.paas.services.emailaas.history.RowEmail;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.PrismaErrorCode;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.core.exceptions.PrismaError;
import it.prisma.presentationlayer.webui.core.paas.EMAILManagementService;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.EmailForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * @author l.calicchio
 * 
 */
@Controller
public class EMAILController {

	static final Logger LOG = LogManager.getLogger(EMAILController.class);

	@Autowired
	private EMAILManagementService emailManagementService;

	private List<EmailDomains> domains;

	@RequestMapping(value = "/paas/emailaas", method = RequestMethod.GET)
	public String getEmailaaS(@CurrentUser PrismaUserDetails user, Model model,
			WebRequest webRequest) {
		try {
			domains = emailManagementService.getEmailDomainList();
			model.addAttribute("domains", domains);
			model.addAttribute("emailForm", new EmailForm());
			return "pages/paas/emailaas/create-email";
		} catch (Exception e) {
			LOG.error("Impossibile visualizzare la pagina iniziale del EMAILaaS");
			throw ExceptionHelper.getPrismaException(e, EMAILController.class);
		}
	}

	@RequestMapping(value = "/paas/emailaas/api", method = RequestMethod.GET)
	public String getAPI(@CurrentUser PrismaUserDetails user) {
		LOG.trace("Richiesta pagina API");
		return "pages/paas/emailaas/api-email";
	}

	@RequestMapping(value = "/async/paas/emailaas", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<RowEmail> getEmailAccount(
			@PrismaRestWSParams RestWSParamsContainer params,
			HttpServletResponse response, @CurrentUser PrismaUserDetails user) {
		try {
			return emailManagementService.getEmailAccountList(user
					.getActiveWorkgroupMembership().getWorkgroupId(), params);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value = "/async/paas/emailaas", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse createEmailAccount(
			@Valid EmailForm emailForm, BindingResult bindingResult,
			@CurrentUser PrismaUserDetails user) {

		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		List<PrismaError> errors = new ArrayList<PrismaError>();

		if (bindingResult.hasErrors()) {
			response.setSuccess(false);
			for (Object object : bindingResult.getAllErrors()) {
				if (object instanceof FieldError) {
					FieldError fieldError = (FieldError) object;
					if (fieldError.getField().equals("accountName")) {
						errors.add(new PrismaError(
								PrismaErrorCode.GENERIC,
								"Inserire un account valido (almeno due caratteri)",
								fieldError.getField()));
					}
					if (fieldError.getField().equals("emailPassword")) {
						errors.add(new PrismaError(
								PrismaErrorCode.GENERIC,
								"Inserire una password valida (almeno 5 caratteri)",
								fieldError.getField()));
					}
				}
			}
		}
		if (!emailForm.getEmailPassword().equals(
				emailForm.getEmailConfirmPassword())
				|| emailForm.getEmailPassword().length() < 5) {
			response.setSuccess(false);
			errors.add(new PrismaError(
					PrismaErrorCode.GENERIC,
					"Errore conferma password (la password deve essere almeno di 5 caratteri)",
					"emailPassword"));
		}
		if (!response.isSuccess()) {
			response.setResult(errors);
			return response;
		}

		try {
			if (emailManagementService.isEmailAccountExists(emailForm
					.getAccountName() + "@" + emailForm.getDomain())) {
				response.setSuccess(false);
				errors.add(new PrismaError(PrismaErrorCode.GENERIC,
						"Account email esistente", "accountName"));
				response.setResult(errors);
				return response;
			}

			EmailInfoRepresentation request = new EmailInfoRepresentation();
			request.setDomain_id(getIdDomain(emailForm.getDomain()));
			request.setPassword(emailForm.getEmailPassword());
			request.setUser(emailForm.getAccountName());

			boolean r = emailManagementService.createEmailAccount(user
					.getActiveWorkgroupMembership().getWorkgroupId(), request);
			response.setSuccess(r);

			response.setResult(request);

			return response;

		} catch (Exception e) {
			LOG.error("Impossible to create email account ");
			return ExceptionHelper.getPrismaJSONResponse(e,
					EMAILController.class);
			// return new PrismaJSONResponse(false,new
			// PrismaError(PrismaErrorCode.SERVER_ERROR,
			// "Impossibile creare un account!"));
		}
	}

	@RequestMapping(value = "/async/paas/emailaas", method = RequestMethod.DELETE, headers = { "Content-type=application/json" })
	public @ResponseBody PrismaJSONResponse disable(
			@RequestBody Credential credential) {
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		try {
			if (credential.getPassword().isEmpty()) {
				response.setSuccess(false);
				response.setResult(new PrismaError(PrismaErrorCode.GENERIC,
						"Password vuota", "password"));
			} else {
				if (!emailManagementService.deleteEmailAccount(
						credential.getEmail(), credential.getPassword())) {
					response.setSuccess(false);
					response.setResult(new PrismaError(PrismaErrorCode.GENERIC,
							"Password errata", "password"));
				}
			}
			return response;
		} catch (Exception e) {
			LOG.error("Impossible to delete email account ");
			return ExceptionHelper.getPrismaJSONResponse(e,
					EMAILController.class);
			// return new PrismaJSONResponse(false,new
			// PrismaError(PrismaErrorCode.SERVER_ERROR,
			// "Impossibile eliminare un account!"));
		}
	}

	@RequestMapping(value = "/async/paas/emailaas", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	public @ResponseBody PrismaJSONResponse updateEmailAccount(
			@RequestBody Credential credential) {
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		List<PrismaError> errors = new ArrayList<PrismaError>();
		if (credential.getPassword().isEmpty()) {
			response.setSuccess(false);
			errors.add(new PrismaError(PrismaErrorCode.GENERIC,
					"Inserire le password attuale", "actualPassword"));
		}
		if (credential.getNewPassword().isEmpty()
				|| credential.getNewPassword().length() < 5) {
			response.setSuccess(false);
			errors.add(new PrismaError(PrismaErrorCode.GENERIC,
					"Inserire la nuova password (almeno 5 caratteri)",
					"newPassword"));
		}
		if (!credential.getConfirmNewPassword().equals(
				credential.getNewPassword())) {
			response.setSuccess(false);
			errors.add(new PrismaError(
					PrismaErrorCode.GENERIC,
					"La password di conferma non coincide con la nuova password",
					"newPassword"));
		}
		if (!response.isSuccess()) {
			response.setResult(errors);
			return response;
		}
		try {
			if (!emailManagementService.updatePassword(credential)) {
				response.setSuccess(false);
				errors.add(new PrismaError(PrismaErrorCode.GENERIC,
						"Password errata", "actualPassword"));
			}
			response.setResult(errors);
			return response;
		} catch (Exception e) {
			LOG.error("Impossible to change password of email account ");
			return ExceptionHelper.getPrismaJSONResponse(e,
					EMAILController.class);
			// return new PrismaJSONResponse(false, new
			// PrismaError(PrismaErrorCode.SERVER_ERROR,
			// "Impossibile cambiare la password all'account"));
		}

	}

	// Search the selected domain_id from List<EmailDomains>
	private long getIdDomain(String domainAccount) {
		for (EmailDomains domain : domains)
			if (domain.getDomain().equals(domainAccount))
				return domain.getId();
		return 0L;
	}
}
