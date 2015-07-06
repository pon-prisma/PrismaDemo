package it.prisma.businesslayer.bizws.iaas.vm;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.paas.services.PaaSServiceGenericWS;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.VMaaSMonitoringStatus;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/workgroups/{workgroupId}/iaas/vmaas")
@PrismaWrapper
public interface VMWS extends PaaSServiceGenericWS<VMRepresentation, VMDeployRequest> {

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
	 * Stops the service.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}/stop")
	public VMRepresentation stopService(@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") Long resId);
	
	/**
	 * Start the service.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}/start")
	public VMRepresentation startService(@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") Long resId);
	
	/**
	 * Restart the service.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}/restart")
	public VMRepresentation restartService(@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") Long resId);
	
	
	/**
	 * Update database status from monitoring.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/monitoring/status")
	public boolean updateStatus(VMaaSMonitoringStatus vMaaSMonitoringStatus);

	/**
	 * Delete the service.
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}")
	public void deleteResource(@PathParam("workgroupId") final Long workgroupId, @PathParam("resId") final Long resourceId);
}
