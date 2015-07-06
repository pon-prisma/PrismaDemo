package it.prisma.businesslayer.bizws.accounting;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.accounting.organizations.IdentityProviderRepresentation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/idp")
@PrismaWrapper
public interface IdPWS {
	
	@GET
	@Path("/info/entityID/{entityId}")
	@Produces(MediaType.APPLICATION_JSON)
	public IdentityProviderRepresentation getIdPInfoFromEntityID(
			@PathParam("entityId") String entityId);
	
	@GET
	@Path("/info/idpID/{identityProviderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public IdentityProviderRepresentation getIdPInfoFromIdentityProviderID(
			@PathParam("identityProviderId") Long identityProviderId);

}

