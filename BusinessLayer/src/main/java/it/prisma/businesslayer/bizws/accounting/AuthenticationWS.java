package it.prisma.businesslayer.bizws.accounting;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;

import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@PrismaWrapper
public interface AuthenticationWS {

	@PUT
	@Path("/tokens/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public AuthTokenRepresentation requestToken(@PathParam("userId") long userId);


	@PUT
	@Path("/tokens/session/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public AuthTokenRepresentation requestSessionToken(@PathParam("userId") long userId);
	
	@GET
	@Path("/tokens/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<AuthTokenRepresentation> getTokens(
			@PathParam("userId") long userId);

	@GET
	@Path("/tokens/session/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public AuthTokenRepresentation getSessionToken(
			@PathParam("userId") long userId);
	
	@DELETE
	@Path("/tokens/{tokenId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteToken(@PathParam("tokenId") String tokenId);

}
