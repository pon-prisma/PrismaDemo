package it.prisma.businesslayer.bizws.accounting;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.users.requests.SignUpUserRequest;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/accounting/")
@PrismaWrapper
public interface UserManagementWS {

	@PUT
	@Path("/users/signup/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void signUpOnPrismaIdentityProvider(SignUpUserRequest signUpUserRequest);

	@PUT
	@Path("/users/signup-third-party/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void signUpFromThirdPartyIdentityProvider(SignUpUserRequest signUpUserRequest);

	@GET
	@Path("/users/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public UserRepresentation getUserById(@PathParam("id") Long userAccountId);

	@GET
	@Path("/organizations/idps/{idp-id}/users/{name-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public UserRepresentation getUserByCredentialsOnIdentityProvider(
			@PathParam("idp-id") Long identityProviderId,
			@PathParam("name-id") String nameIdOnIdentityProvider);

	@GET
	@Path("/organizations/idps/by-entity-id/{idp-entity-id}/users/{name-id}")
	public UserRepresentation getUserByCredentialsOnIdentityProvider(
			@PathParam("idp-entity-id") String identityProviderEntityId,
			@PathParam("name-id") String nameIdOnIdentityProvider);

	
	@GET
	@Path("/users/")
	@Produces(MediaType.APPLICATION_JSON)
	@PrismaWrapper(restParamsEnabled = true)
	// TODO Add query params to support pagination and sorting
	public Collection<UserRepresentation> getAllUsers();

	@DELETE
	@Path("/users/{id}/suspend")
	@Produces(MediaType.APPLICATION_JSON)
	public void suspendUser(@PathParam("id") Long userAccountId);

	@PUT
	@Path("/users/{id}/unsuspend")
	@Produces(MediaType.APPLICATION_JSON)
	public void unsuspendUser(@PathParam("id") Long userAccountId);

	@PUT
	@Path("/users/{id}/enable")
	@Produces(MediaType.APPLICATION_JSON)
	public void enableUser(@PathParam("id") Long userAccountId);

	@DELETE
	@Path("/users/{id}/disable")
	@Produces(MediaType.APPLICATION_JSON)
	public void disableUser(@PathParam("id") Long userAccountId);

	@PUT
	@Path("/users/{email}/confirm/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public void confirmUserOnPrismaIdentityProvider(
			@PathParam("email") String email, @PathParam("token") String token);

	@PUT
	@Path("/users/{id}/roles/prisma-guest")
	@Produces(MediaType.APPLICATION_JSON)
	public void setUserAccountAsPrismaGuest(
			@PathParam("id") Long userAccountId);

	@PUT
	@Path("/users/{id}/roles/prisma-user")
	@Produces(MediaType.APPLICATION_JSON)
	public void setUserAccountAsPrismaUser(
			@PathParam("id") Long userAccountId);

	@PUT
	@Path("/users/{id}/roles/prisma-admin")
	@Produces(MediaType.APPLICATION_JSON)
	public void setUserAccountAsPrismaAdmin(
			@PathParam("id") Long userAccountId);

	@POST
	@Path("/users/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public UserRepresentation updateUser(@PathParam("id") Long userAccountId, UserRepresentation userData);

	
}
