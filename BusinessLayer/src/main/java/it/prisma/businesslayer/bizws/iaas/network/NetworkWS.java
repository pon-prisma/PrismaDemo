package it.prisma.businesslayer.bizws.iaas.network;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.iaas.network.NetworkRepresentation;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/workgroups/{workgroupId}/iaas/networks")
public interface NetworkWS {

	// TODO Implementare autorizzazione
	// TODO Implementare Filtri (es WGId, UserId, name) e associare
	// autorizzazione
	// TODO Trovare un modo per generalizzare filtri, paginazione e operazioni
	// analoghe a più WS
	// TODO Importare i WS che lanciano WF

	// Create

	// Read

	// Update

	// Delete
	
	// Stop service
	
	/**
	 * List all keys in the workgroup.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<NetworkRepresentation> listAllNetworks(@PathParam("workgroupId") final Long workgroupId);
	
}
