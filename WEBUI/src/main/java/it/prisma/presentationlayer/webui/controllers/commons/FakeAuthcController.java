package it.prisma.presentationlayer.webui.controllers.commons;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.accounting.users.RoleRepresentation;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;
import it.prisma.presentationlayer.webui.core.accounting.AuthTokenManagementService;
import it.prisma.presentationlayer.webui.core.accounting.UserManagementService;
import it.prisma.presentationlayer.webui.core.accounting.WorkgroupManagementService;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeAuthcController {

	static final Logger LOG = LogManager.getLogger(FakeAuthcController.class
			.getName());

	@Autowired
	private UserManagementService userService;


	@Autowired
	private AuthTokenManagementService authTokenManagementService;
	

	@Autowired
	private WorkgroupManagementService workgroupManagementService;

	@RequestMapping(value = "accounting/fakeauth/{id}", method = RequestMethod.GET)
	public @ResponseBody UserRepresentation fakeauth(@PathVariable Long id) throws Exception {

		UserRepresentation userData = userService.getUserById(id);
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (RoleRepresentation role : userData.getRoles())
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		
		String userAccountId = String.valueOf(userData.getUserAccountId());
		
		AuthTokenRepresentation authToken = authTokenManagementService.authenticateUserOnBusinessLayer(userAccountId);
		
		Collection<WorkgroupMembershipRepresentation> rawMemberships = workgroupManagementService.getAllMembershipsByUserAccountId(userData.getUserAccountId());
		
		WorkgroupMembershipRepresentation activeWorkgroup = rawMemberships.iterator().next();				
		
		PrismaUserDetails user = new PrismaUserDetails("nameId", true, true,
				true, true, authorities, userData, activeWorkgroup, authToken);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				user, null, authorities);
		LOG.debug("Logging in with [{}]", authentication.getPrincipal());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return userData;
	}
}