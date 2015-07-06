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

package it.prisma.presentationlayer.webui.security.userdetails;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.accounting.users.RoleRepresentation;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;
import it.prisma.presentationlayer.webui.configs.SAMLIdentityProviderRegistry.SAMLIdentityProviderRegistryProcessor;
import it.prisma.presentationlayer.webui.core.accounting.AuthTokenManagementService;
import it.prisma.presentationlayer.webui.core.accounting.UserManagementService;
import it.prisma.presentationlayer.webui.core.accounting.WorkgroupManagementService;
import it.prisma.presentationlayer.webui.security.exceptions.MissingProfileException;
import it.prisma.presentationlayer.webui.security.exceptions.NoRoleException;
import it.prisma.presentationlayer.webui.security.exceptions.UserSuspendedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class PrismaUserDetailsServiceImpl implements SAMLUserDetailsService {

	// Logger
	static final Logger LOG = LogManager
			.getLogger(PrismaUserDetailsServiceImpl.class);

	@Autowired
	private UserManagementService userManagementService;

	@Autowired
	private AuthTokenManagementService authTokenManagementService;
	
	@Autowired
	private WorkgroupManagementService workgroupManagementService;
	
	@Autowired
	private SAMLIdentityProviderRegistryProcessor samlIdentityProviderRegistryProcessor;

	@Override
	public Object loadUserBySAML(SAMLCredential credential)
			throws UsernameNotFoundException {

		// AuthN
		// Fetch idpName and userID from SAML assertions
		String localEntityId = credential.getLocalEntityID();
		String nameId = samlIdentityProviderRegistryProcessor.getNameId(credential);
		String email = samlIdentityProviderRegistryProcessor.getEmail(credential);
		String remoteEntityId = credential.getRemoteEntityID();
		String identityProviderLabel = samlIdentityProviderRegistryProcessor.getIdentityProviderLabel(credential);
		LOG.info("SAML Response\tEntityID: " + localEntityId);
		LOG.info("SAML Response\tNameID: " + nameId);
		LOG.info("SAML Response\tEmail: " + email);
		LOG.info("SAML Response\tRemoteEntityID: " + remoteEntityId);
		LOG.info(nameId + " has just been performing AuthN on: "
				+ remoteEntityId);


		// AuthZ
		try  {
			Long identityProviderId = samlIdentityProviderRegistryProcessor.getIdentityProviderId(credential);
			LOG.info("Looking for " + nameId + " on IdentityProvider <" + identityProviderId.toString() + ">");
			UserRepresentation userData = userManagementService
					.getUserByCredentialsOnIdentityProvider(identityProviderId, nameId);
			
			if (!userData.getEnabled()) {
				LOG.info("The current user is not enabled");
				throw new UserSuspendedException("The current user is not enabled");
			} else if (userData.getSuspended()) {
				LOG.info("The current user is suspended");
				throw new UserSuspendedException("The current user is suspended");
			} else if (userData.getRoles().isEmpty()) {
				LOG.info("No role ");
				throw new NoRoleException("No role found.");
			} else {
				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				for (RoleRepresentation role : userData.getRoles())
					authorities.add(new SimpleGrantedAuthority(role.getName()));

				String userAccountId = String.valueOf(userData.getUserAccountId());
								
				AuthTokenRepresentation authToken = authTokenManagementService.authenticateUserOnBusinessLayer(userAccountId);
				
				Collection<WorkgroupMembershipRepresentation> rawMemberships = workgroupManagementService.getAllMembershipsByUserAccountId(userData.getUserAccountId());
				
				WorkgroupMembershipRepresentation activeWorkgroup = rawMemberships.iterator().next();				
				
				return new PrismaUserDetails(nameId, true, true, true, true,
						authorities, userData, activeWorkgroup, authToken);

			}

			
		} catch (AuthenticationCredentialsNotFoundException e) {
			LOG.info("Missing profile.", e);
			throw new MissingProfileException(identityProviderLabel,remoteEntityId, nameId, email);
		} catch (Exception e) {			
			LOG.info("AuthZ service error.", e);
			throw new AuthenticationServiceException("AuthZ service exception");
		}

		// Note: it this method throws a MissingProfileException,
		// an handler will redirect the authenticated user to the signup page.
	}

}