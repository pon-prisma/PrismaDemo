/*
 * Copyright 2014 PRISMA by MIUR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.prisma.presentationlayer.webui.controllers.commons;

import it.prisma.presentationlayer.webui.core.accounting.WorkgroupManagementService;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupNotFoundException;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupOperationException;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class PrismaUserDetailsDataInterceptor extends HandlerInterceptorAdapter {

	// Logger
	static final Logger LOG = LogManager
			.getLogger(PrismaUserDetailsDataInterceptor.class);

	@Autowired WorkgroupManagementService workgroupManagementService;

	@Override
	public void postHandle(final HttpServletRequest request,
			final HttpServletResponse response, final Object handler,
			final ModelAndView modelAndView) throws Exception {
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			PrismaUserDetails userDetails = (PrismaUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			try {
				setModelHelper(userDetails, modelAndView);
			} catch (Exception e) {
				response.sendRedirect("/500");
			}
		}
	}

	private void setModelHelper(PrismaUserDetails userDetails,
			ModelAndView modelAndView) throws WorkgroupNotFoundException,
			WorkgroupOperationException, IOException {
		
		modelAndView.getModelMap().addAttribute(
				"currentUser",
				userDetails.getUserData());
		
		modelAndView.getModelMap().addAttribute(
				"fullName",
				userDetails.getUserData().getFirstName() + " "
						+ userDetails.getUserData().getLastName());
		modelAndView.getModelMap().addAttribute("employer",
				userDetails.getUserData().getEmployer());
		if (userDetails.getActiveWorkgroupMembership() != null) {
			LOG.info("<"
					+ userDetails.getUsername()
					+ "> has as active workgroup: <"
					+ userDetails.getActiveWorkgroupMembership()
							.getWorkgroupId() + ">");
			modelAndView.getModelMap().addAttribute("hasAnActiveWorkgroup",
					true);
			modelAndView.getModelMap().addAttribute(
					"activeWorkgroup",
					workgroupManagementService.getWorkgroupById(
							userDetails.getActiveWorkgroupMembership()
									.getWorkgroupId()).getLabel());
		} else {
			LOG.warn("<" + userDetails.getUsername() + "> hasn't a workgroup");
			modelAndView.getModelMap().addAttribute("hasAnActiveWorkgroup",
					false);
		}
		if (userDetails.getUserData().getAvatar() == null
				|| userDetails.getUserData().getAvatar().isEmpty())
			modelAndView.getModelMap().addAttribute("avatar", "unknown.png");
		else
			modelAndView.getModelMap().addAttribute("avatar",
					userDetails.getUserData().getAvatar());
	}

}