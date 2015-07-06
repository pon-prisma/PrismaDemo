package it.prisma.businesslayer.bizws.organization;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.accounting.organizations.OrganizationRepresentation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/organizations")
@PrismaWrapper
public interface OrganizationWS {

	
	@GET
	@Path("/{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrganizationRepresentation getOrganizationById(
			@PathParam("organizationId") String organizationId);

}