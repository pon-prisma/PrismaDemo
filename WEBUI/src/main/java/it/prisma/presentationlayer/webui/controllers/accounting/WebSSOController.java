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

package it.prisma.presentationlayer.webui.controllers.accounting;

import it.prisma.presentationlayer.webui.configs.SAMLIdentityProviderRegistry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/saml")
public class WebSSOController {

	// Logger
	private static final Logger LOG = LoggerFactory
			.getLogger(WebSSOController.class);
	
	@Autowired
	SAMLIdentityProviderRegistry samlIdentityProviderRegistry;
	
	@RequestMapping(method = RequestMethod.GET)
	public String disableRoot(){
		return "redirect:/saml/login";
	}
	
	@RequestMapping(value = "/idpSelection", method = RequestMethod.GET)
	public String idpSelection(HttpServletRequest request, Model model) {
		if (!(SecurityContextHolder.getContext().getAuthentication()
				instanceof AnonymousAuthenticationToken)) {
			LOG.warn("The current user is already logged.");
			return "redirect:/landing";
		} else {
			if (isForwarded(request)) {
				model.addAttribute("sielteIdP", samlIdentityProviderRegistry.getSielteIdP());
				model.addAttribute("replyIdP", samlIdentityProviderRegistry.getReplyIdP());	
				model.addAttribute("prismaIdP", samlIdentityProviderRegistry.getPrismaIdP());
				model.addAttribute("infnIdP", samlIdentityProviderRegistry.getInfnIdP());
				model.addAttribute("unibaIdP", samlIdentityProviderRegistry.getUniBaIdP());
				return "pages/accounting/signin";
			} else {
				LOG.warn("Direct access to '/idpSelection' route are not allowed");
				return "redirect:/";
			}
		}
	}

	/*
	 * Checks if an HTTP request is forwarded from servlet.
	 */
	private boolean isForwarded(HttpServletRequest request){
		if (request.getAttribute("javax.servlet.forward.request_uri") == null)
			return false;
		else
			return true;
	}

}
