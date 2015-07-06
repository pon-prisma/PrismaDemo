package it.prisma.presentationlayer.webui.controllers.accounting;

import it.prisma.presentationlayer.webui.core.accounting.UserManagementService;
import it.prisma.presentationlayer.webui.datavalidation.forms.accounting.SignUpForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.accounting.SignUpThirdPartyForm;
import it.prisma.presentationlayer.webui.security.exceptions.SSOCredentialAlreadyUsedException;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/accounting/")
public class SigningController {

	// Logger
	static final Logger LOG = LogManager.getLogger(SigningController.class);

	@Autowired
	private UserManagementService userManagementService;

	@RequestMapping(method = RequestMethod.GET)
	public String disableRoot() {
		return "redirect:/accounting/signup";
	}

	@RequestMapping(value = "/support", method = RequestMethod.GET)
	public String support() {
		return "pages/accounting/support";
	}

	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String idpSelection(HttpServletRequest request, Model model) {
		//Controllo se l'utente è loggato
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			LOG.warn("The current user is already logged.");
			return "redirect:/dashboard";
		} else {
			LOG.info("Redirect to \"/saml/login\".");
			return "redirect:/saml/login";
		}
	}

	@RequestMapping(value = "/signout", method = RequestMethod.GET)
	public String logout(
			@RequestParam(value = "slo", required = false, defaultValue = "global") String slo) {
		if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			LOG.warn("The current user is not logged.");
			return "redirect:/saml/login";
		} else {
			LOG.info("Perform logout.");
			if (slo.compareTo("global") == 0)
				return "forward:/saml/logout";
			else
				return "forward:/saml/logout?local=true";
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String clearSingUp(SignUpForm signUpForm) {
		LOG.debug("Showing the sign-up form.");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			LOG.debug("The user is currently logged in, thus already signed.");
			return "redirect:/dashboard";
		} else
			return "pages/accounting/signup";
	}

	@RequestMapping(value = "/signup-third-party", method = RequestMethod.GET)
	public String signUpThirdParty(SignUpThirdPartyForm signUpThirdPartyForm,
			Model model, HttpServletRequest request) {
		LOG.debug("Showing the signup-third-party form.");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			LOG.debug("The user is currently logged in, thus already signed.");
			return "redirect:/dashboard";
		} else {
			model.addAttribute("identityProviderLabel", request.getSession()
					.getAttribute("identityProviderLabel"));
			LOG.info("Label: "
					+ request.getSession()
							.getAttribute("identityProviderLabel"));
			LOG.info("EntityId: "
					+ request.getSession().getAttribute(
							"identityProviderEntityId"));
			LOG.info("NameId: " + request.getSession().getAttribute("nameId"));
			LOG.info("Email: " + request.getSession().getAttribute("email"));
			return "pages/accounting/signup-third-party";
		}
	}

	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String sendUserInfo(@Valid SignUpForm signUpForm,
			BindingResult bindingResult, Model model) {
		
		LOG.debug("Signing up.");
		boolean thereIsAnError = false;
		boolean credentialsAlreadyInUse = false;		
		
		if (bindingResult.hasErrors()) {
			LOG.debug("SignUpForm has errors");
			thereIsAnError = true;
		} else if (!signUpForm.getPassword().equals(signUpForm.getRepeatedPassword())) {
			LOG.debug("Passwords doesn't match.");
			model.addAttribute("passwordsMatchingError", true);
			thereIsAnError = true;
		} else {
			LOG.debug("User form data: ");
			LOG.info(signUpForm.getUsername());
			LOG.info(signUpForm.getEmployer());
			LOG.info(signUpForm.getEmailRef());
			LOG.info(signUpForm.getWorkTelephone());
			LOG.info(signUpForm.getPersonalTelephone());
			LOG.info(signUpForm.getFirstName());
			LOG.info(signUpForm.getLastName());
			LOG.info(signUpForm.getNationality());
			LOG.info(signUpForm.getFiscalCode());
			try {
				userManagementService.signUpOnPrismaIdentityProvider(
						signUpForm.getUsername(),
						DigestUtils.sha1Hex(signUpForm.getPassword()),
						signUpForm.getEmail(),
						signUpForm.getFirstName(), null,
						signUpForm.getLastName(),
						signUpForm.getPersonalTelephone(),
						signUpForm.getWorkTelephone(),
						signUpForm.getNationality(),
						signUpForm.getFiscalCode(), signUpForm.getEmployer(),
						signUpForm.getEmailRef());
			}
			catch (SSOCredentialAlreadyUsedException e) {
				credentialsAlreadyInUse = true;
			}
			catch (AuthenticationException e) {
				LOG.error("userManagementService.signUpOnPrismaIdentityProvider", e);
				thereIsAnError = true;
			} catch (IOException e){
				LOG.error(e.getMessage(), e);
				return "pages/error/prisma-idp-error";
			}
		}
		// Error check.
		if (credentialsAlreadyInUse) {
			LOG.debug("return pages/accounting/signup-credentials-in-use");
			return "pages/accounting/signup-credentials-in-use";
		}
		else 
			if (thereIsAnError) {
				LOG.debug("return pages/accounting/signup-error");
				return "pages/accounting/signup-error";
			}
			else 
			{
				LOG.debug("return pages/accounting/signup-requested");
				return "pages/accounting/signup-requested";
			}
		
	}

	@RequestMapping(value = "/signup-third-party", method = RequestMethod.POST)
	public String signUpThirdPartyHandler(
			@Valid SignUpThirdPartyForm signUpThirdPartyForm,
			BindingResult bindingResult, Model model, HttpServletRequest request) {
		
		
		
		LOG.debug("signup-third-party");

		boolean credentialsAlreadyInUse = false;
		boolean thereIsAnError = false;

		if (bindingResult.hasErrors()) {
			LOG.debug("SignUpThirdPartyForm has errors");
			thereIsAnError = true;
		} else {
			LOG.info(signUpThirdPartyForm.getEmployer());
			LOG.info(signUpThirdPartyForm.getEmailRef());
			LOG.info(signUpThirdPartyForm.getWorkTelephone());
			LOG.info(signUpThirdPartyForm.getPersonalTelephone());
			LOG.info(signUpThirdPartyForm.getFirstName());
			LOG.info(signUpThirdPartyForm.getLastName());
			LOG.info(signUpThirdPartyForm.getNationality());
			LOG.info(signUpThirdPartyForm.getFiscalCode());
			try {
				userManagementService.signUpFromThirdPartyIdentityProvider(
						request.getSession().getAttribute("identityProviderEntityId")
								.toString(),
						request.getSession().getAttribute("nameId").toString(),
						request.getSession().getAttribute("email").toString(),
						signUpThirdPartyForm.getFirstName(), null,
						signUpThirdPartyForm.getLastName(),
						signUpThirdPartyForm.getPersonalTelephone(),
						signUpThirdPartyForm.getWorkTelephone(),
						signUpThirdPartyForm.getNationality(),
						signUpThirdPartyForm.getFiscalCode(),
						signUpThirdPartyForm.getEmployer(),
						signUpThirdPartyForm.getEmailRef());
			} catch (SSOCredentialAlreadyUsedException e) {
				credentialsAlreadyInUse = true;
			}
			catch (AuthenticationException e) {
				LOG.error("userManagementService.signUpOnPrismaIdentityProvider", e);
				thereIsAnError = true;
			} catch (IOException e){
				LOG.error(e.getMessage(), e);
				return "pages/error/prisma-idp-error";
			}
		}
		
		// Error check.
		if (credentialsAlreadyInUse) {
			LOG.debug("return pages/accounting/signup-credentials-in-use");
			return "pages/accounting/signup-credentials-in-use";
		}
		else 
			if (thereIsAnError) {
				LOG.debug("return pages/accounting/signup-error");
				return "pages/accounting/signup-error";
			}
			else 
			{
				LOG.debug("return pages/accounting/signup-requested");
				return "pages/accounting/signup-requested";
			}
	}

}
