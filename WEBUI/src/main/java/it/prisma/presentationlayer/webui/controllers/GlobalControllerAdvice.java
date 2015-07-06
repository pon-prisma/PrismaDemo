package it.prisma.presentationlayer.webui.controllers;

import it.prisma.domain.dsl.accounting.workgroups.WorkgroupRepresentation;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.IaaSHealth;
import it.prisma.presentationlayer.webui.core.accounting.WorkgroupManagementService;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupNotFoundException;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupOperationException;
import it.prisma.presentationlayer.webui.core.exceptions.CustomGenericException;
import it.prisma.presentationlayer.webui.core.monitoring.MonitoringManagementService;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

	static final Logger LOG = LogManager.getLogger(GlobalControllerAdvice.class
			.getName());

	@Autowired
	private MonitoringManagementService monitoringService;

	@Autowired
	private WorkgroupManagementService workgroupManagementService;

	@ModelAttribute("iaasHealt")
	public IaaSHealth getIaaSHealt(HttpServletResponse response) {

		try {
			return monitoringService.getIaaSStatus();
		} catch (Exception e) {
			IaaSHealth healt = new IaaSHealth();
			healt.setAvailableNodes(0);
			healt.setTotalNodes(0);
			healt.setNetwork("ko");
			healt.setStorage("ko");
			healt.setCompute("not ready");
			return healt;
		}
	}

	@ModelAttribute("activeUserWorkgroup")
	public WorkgroupRepresentation getActiveUserWorkgroup(
			HttpServletResponse response) throws WorkgroupNotFoundException,
			WorkgroupOperationException, IOException {
		try {

			WorkgroupRepresentation activeWorkgroup = new WorkgroupRepresentation();

			if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {

				PrismaUserDetails user = (PrismaUserDetails) SecurityContextHolder
						.getContext().getAuthentication().getPrincipal();

				return workgroupManagementService.getWorkgroupById(user
						.getActiveWorkgroupMembership().getWorkgroupId());
			}

			return activeWorkgroup;
		} catch (Exception e) {
			LOG.error("Cannot retrieve active user workgroups.", e);
		}
		return null;

	}

	@ExceptionHandler({ CustomGenericException.class })
	public RedirectView customGenericException(CustomGenericException e) {

		LOG.error("CustomGenericException: ", e);
		switch (e.getErrCode()) {
		case NOT_FOUND:
			return new RedirectView("/404");

		case SERVER_ERROR:
			return new RedirectView("/500");

		default:
			return new RedirectView("/prisma/error?code=" + e.getErrCode()
					+ "&message=" + e.getErrMsg());
		}
	}

	@ExceptionHandler({ Exception.class })
	public RedirectView exception(Exception e) {

		LOG.error("Exception: ", e);
		return new RedirectView("/500");
	}
}