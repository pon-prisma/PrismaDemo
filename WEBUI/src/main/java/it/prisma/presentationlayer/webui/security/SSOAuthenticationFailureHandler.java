package it.prisma.presentationlayer.webui.security;

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

import it.prisma.presentationlayer.webui.security.exceptions.MissingProfileException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class SSOAuthenticationFailureHandler implements
		AuthenticationFailureHandler {

	// Logger
	static final Logger LOG = LogManager
			.getLogger(SSOAuthenticationFailureHandler.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		if (exception.getClass()
				.isAssignableFrom(MissingProfileException.class)) {
			MissingProfileException dataFromIdentityProvider = (MissingProfileException) exception;
			String identityProviderLabel = dataFromIdentityProvider
					.getIdentityProviderEntityLabel();
			String nameId = dataFromIdentityProvider.getNameId();
			String identityProviderEntityId = dataFromIdentityProvider
					.getIdentityProviderEntityId();
			String email = dataFromIdentityProvider.getEmail();
			LOG.info("Requesting profile for: " + nameId + " on "
					+ identityProviderEntityId + " with email " + email);
			request.getSession().setAttribute("identityProviderLabel",
					identityProviderLabel);
			request.getSession().setAttribute("identityProviderEntityId",
					identityProviderEntityId);
			request.getSession().setAttribute("nameId", nameId);
			request.getSession().setAttribute("email", email);
			response.sendRedirect(request.getContextPath()
					+ "/accounting/signup-third-party");
		} else if (exception.getClass().isAssignableFrom(
				CredentialsExpiredException.class)) {
			LOG.error("Credential expired!\n\n");
			response.sendRedirect(request.getContextPath() + "/saml/login");
		} else {
			LOG.error("Generic Auth Error!");
			LOG.error(exception.getMessage());
			LOG.error(exception);
			response.sendRedirect(request.getContextPath() + "/500");
		}
	}

}
