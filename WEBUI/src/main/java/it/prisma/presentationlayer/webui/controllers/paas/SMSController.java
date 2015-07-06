package it.prisma.presentationlayer.webui.controllers.paas;

import it.prisma.domain.dsl.paas.services.smsaas.IsActiveStatus;
import it.prisma.domain.dsl.paas.services.smsaas.SMSNotificationsRepresentation;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.core.paas.SMSSManagementService;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.smsaas.DailyNotificationForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.smsaas.MonthlyNotificationForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SMSController {

	static final Logger LOG = LogManager.getLogger(SMSController.class
			.getName());

	private String  wordkgroup_prisma_id="1";

	@Autowired
	private SMSSManagementService smsManagementService;

	@RequestMapping(value = "/paas/smsaas", method = RequestMethod.GET)
	public String createSMS(@CurrentUser PrismaUserDetails user, 
			Model model) {

		String serviceStatus;
		try {
			serviceStatus = smsManagementService.isServiceActive(wordkgroup_prisma_id);
			if(serviceStatus.equals(IsActiveStatus.SERVICE_ON.getStatus())){
				try {
					model.addAttribute("isServiceActive", "true");
					String token = smsManagementService.getToken(wordkgroup_prisma_id);
					model.addAttribute("token", token);
					//List<SMS> smsList = smsManagementService.getSMSHystory("1", null);
					//model.addAttribute("smsList", smsList);
					BigDecimal credit;
					credit = smsManagementService.getCredit(wordkgroup_prisma_id).setScale(2, RoundingMode.FLOOR);
					model.addAttribute("credit", String.valueOf(credit));
					SMSNotificationsRepresentation notification = smsManagementService.getNotifications(wordkgroup_prisma_id);
					model.addAttribute("notification", notification);
				} catch (Exception e) {
					LOG.error("Error retriving sms information of user");
					throw ExceptionHelper.getPrismaException(e,SMSController.class);
				}
			} else if(serviceStatus.equals(IsActiveStatus.SERVICE_OFF.getStatus())){
				LOG.error("utente disabilitato nel database");
				model.addAttribute("isServiceActive", "false");
			}else if(serviceStatus.equals(IsActiveStatus.NOT_FOUND_USER.getStatus())){
				LOG.error("utente non trovato nel database");
				model.addAttribute("isServiceActive", "false");
				//return "redirect:/500";
			}
		} catch (Exception e1) {
			LOG.error("Impossible to recovery status of user");
			return "redirect:/500";
		}
		LOG.trace(" requests the page create sms");
		return "pages/paas/smsaas/create-sms";
	}

	@RequestMapping(value = "async/paas/smsaas/daily", method = RequestMethod.POST)
	public @ResponseBody SMSNotificationsRepresentation setDailyNotifications(@Valid DailyNotificationForm form,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			String errors = "";
			for (Object object : bindingResult.getAllErrors()) {
				if (object instanceof FieldError) {
					FieldError fieldError = (FieldError) object;
					errors = errors + " " + fieldError.getField() + " "
							+ fieldError.getCode() + "\n";
					LOG.error(errors.toString());

				}
			}
			LOG.debug("Notification " + errors);

			return null;
		} else {

			SMSNotificationsRepresentation request = new SMSNotificationsRepresentation();
			if(form.getDailyNot() == null){
				request.setIsDaily(false);	
				return smsManagementService.setDailyNotifications(wordkgroup_prisma_id, request);
			} else {
				request.setIsDaily(true);
				if(form.getDailySms() == null){
					request.setIsDailySms(false);
				} else{
					request.setIsDailySms(true);
					request.setDailySms(form.getNumberDaily());
					request.setThresholdDay(form.getSmsThresholdDaily());
				}
				if(form.getDailyEmail() == null){
					request.setIsDailyEmail(false);
				} else {
					request.setIsDailyEmail(true);
					request.setDailyEmail(form.getEmailAddressDaily());
					//Attenzione si chiama SmsThresholdDaily ma è unico per entrambi
					request.setThresholdDay(form.getSmsThresholdDaily());
				}

				return smsManagementService.setDailyNotifications(wordkgroup_prisma_id, request);
			}			
		}
	}


	@RequestMapping(value = "async/paas/smsaas/monthly", method = RequestMethod.POST)
	public @ResponseBody SMSNotificationsRepresentation setMonthlyNotifications(@Valid MonthlyNotificationForm form,
			BindingResult bindingResult) {


		if (bindingResult.hasErrors()) {
			String errors = "";
			for (Object object : bindingResult.getAllErrors()) {
				if (object instanceof FieldError) {
					FieldError fieldError = (FieldError) object;
					errors = errors + " " + fieldError.getField() + " "
							+ fieldError.getCode() + "\n";
					LOG.error(errors.toString());
				}
			}
			LOG.debug("Notification " + errors);

			return null;
		} else {

			SMSNotificationsRepresentation request = new SMSNotificationsRepresentation();
			if(form.getMonthlyNot() == null){
				request.setIsMonth(false);	
				return smsManagementService.setMonthlyNotifications(wordkgroup_prisma_id, request);
			} else {
				request.setIsMonth(true);
				if(form.getMonthlySms() == null){
					request.setIsMonthSms(false);
				} else{
					request.setIsMonthSms(true);
					request.setMonthSms(form.getNumberMonthly());
					request.setThresholdMonth(form.getSmsThresholdMonthly());
				}
				if(form.getMonthlyEmail() == null){
					request.setIsMonthEmail(false);
				} else {
					request.setIsMonthEmail(true);
					request.setMonthEmail(form.getEmailAddressMonthly());
					//Attenzione si chiama SmsThresholdDaily ma è unico per entrambi sms ed email 
					request.setThresholdMonth(form.getSmsThresholdMonthly());
				}

				return smsManagementService.setMonthlyNotifications(wordkgroup_prisma_id, request);
			}			
		}
	}

	@RequestMapping(value = "async/paas/smsaas/disable", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse disable() {
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		try{
			response.setSuccess(smsManagementService.disableService(wordkgroup_prisma_id));
			return response;
		}catch(Exception e){
			LOG.error("Error disabling sms service");
			return ExceptionHelper.getPrismaJSONResponse(e, SMSController.class);
		}
	}

	@RequestMapping(value = "async/paas/smsaas/regenerateToken", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse regenerateToken() {
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		try{
			response.setResult( smsManagementService.regenerateToken(wordkgroup_prisma_id));
			return response;
		}catch(Exception e){
			LOG.error("Error regenerating sms token");
			return ExceptionHelper.getPrismaJSONResponse(e, SMSController.class);
		}

	}

	@RequestMapping(value = "/async/paas/smsaas/history", method = RequestMethod.GET)
	public @ResponseBody Object getHistory(WebRequest webRequest) {
		try{
			String search = webRequest.getParameter("search");
			if(search == null || search.length() == 0){
				search = null;
			}
			String offsetString = webRequest.getParameter("offset");
			String limitString = webRequest.getParameter("limit");

			if(offsetString == null || offsetString.equals("")){
				offsetString = "0";
			}

			if(limitString == null || limitString.equals("")){
				limitString = "10";
			}

			return smsManagementService.getSMSHystory(wordkgroup_prisma_id, search, Integer.parseInt(limitString), Integer.parseInt(offsetString));
		}catch(Exception e){
			LOG.error("Error retrieving sms history for workgroup " + wordkgroup_prisma_id);
			return ExceptionHelper.getPrismaJSONResponse(e, SMSController.class);
		}


	}


	@RequestMapping(value = "async/paas/smsaas/deploy", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse deploySMS() {
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		try {
			if (!smsManagementService.activeService(wordkgroup_prisma_id).equals("Utente attivo")){
				response.setSuccess(false);
			}
			return response;
		} catch (Exception e) {
			LOG.error("Error activating sms service for workgroup " + wordkgroup_prisma_id);
			return ExceptionHelper.getPrismaJSONResponse(e, SMSController.class);
		}	
	}


	@RequestMapping(value = "/paas/smsaas/api", method = RequestMethod.GET)
	public String getAPI() {

		LOG.trace("Richiesta pagina API");

		return "pages/paas/smsaas/api-sms";
	}

}
