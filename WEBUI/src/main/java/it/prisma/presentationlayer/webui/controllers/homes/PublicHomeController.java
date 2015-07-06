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

package it.prisma.presentationlayer.webui.controllers.homes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Web controller delegate to solve requests access to the PRISMA portal
 * homepage. It handles the mapping of HTTP/HTTPS requests and makes content available
 * to the <em>view</em> of the portal.
 * 
 * @author <a href="mailto:v.denotaris@reply.it">Vincenzo De Notaris</a>
 * @version 0.1.0
 * @since 0.1.0
 * @see <a
 *      href="http://www.istc.cnr.it/project/prisma-piattaforme-cloud-interoperabili-smart-government">Progetto
 *      PRISMA</a>
 */
@Controller
public class PublicHomeController {
	
	// Logger
	static final Logger LOG = LogManager.getLogger(PublicHomeController.class.getName());
	
	/**
     * Manages the mapping of HTTP/HTTPS requests and makes available the
     * content to the <em>view</em> of the portal.
	 * 
	 * @param name
	 *            Name of the current user. If it has not been done ​​the
     *            login, by default, the user is identified as a guest.
	 *            
	 * @param model
	 *            Holder object of data model. Realize the binding between
     *            Java variables and hypertext content.
	 * @return 
	 *         Indicates the reference to the visual element that forms the
     *         view of the web page desired.
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String home() {
		
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			LOG.trace("The current user is logged. It will be redirected to \"/dashboard\".");
			return "redirect:/dashboard";
		} else {
			LOG.trace("access to public home");			
			return "pages/public-home";
		}
	}

}