package it.prisma.businesslayer.bizws.paas.services;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.deployments.VirtualMachineRepresentation;
import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/* TODO: Gestire il wrapping delle risposte prisma usando degli interceptor.
 * Così si possono scrivere i WS che restituiscono la risorsa e generlaizzarli 
 * per tutte le risorse.
 * 
 * Utilizzare il seguente Path("/workgroups/{workgroupId}/paas/")
 * 
 */
@Path("/{workgroupId}")
@PrismaWrapper
public interface PaaSServiceGenericWS<SERVICE_REPRESENTATION_TYPE extends GenericPaaSServiceRepresentation, DEPLOY_REQ_TYPE extends GenericPaaSServiceDeployRequest<SERVICE_REPRESENTATION_TYPE>> {

	// TODO Implementare autorizzazione
	// TODO Implementare Filtri (es WGId, UserId, name) e associare
	// autorizzazione
	// TODO Trovare un modo per generalizzare filtri, paginazione e operazioni
	// analoghe a più WS
	// TODO Importare i WS che lanciano WF

	// Create
	/**
	 * Creates a <code>PaaSService</code> with the given deployment data in the
	 * given workgroup.
	 * 
	 * @param workgroupId
	 * @param deployRequest
	 * @return a list of <code>Resource</code>.
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	@PrismaWrapper
	public SERVICE_REPRESENTATION_TYPE createResource(
			@PathParam("workgroupId") final Long workgroupId,
			DEPLOY_REQ_TYPE deployRequest);

	// Read

	/**
	 * Returns a list of <code>Resource</code> with the given workgroup.
	 * 
	 * @param workgroupId
	 * @return a list of <code>Resource</code>.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<SERVICE_REPRESENTATION_TYPE> getResourcesByWorkgroup(
			@PathParam("workgroupId") final Long workgroupId, 
			@DefaultValue("false") @QueryParam("consider_deleted") final String considerDeletedToo);

	/**
	 * @param workgroupId
	 * @param appId
	 * @return the <code>Resource</code> with the given Id.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}")
	@PrismaWrapper
	public SERVICE_REPRESENTATION_TYPE getResourceById(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") final Long appId);

	/**
	 * @param workgroupId
	 * @param resId
	 * @param from
	 * @param to
	 * @param size
	 * @return the list of <code>Resource</code> Events related to the given
	 *         resource Id.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}/events")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<PaaSServiceEventRepresentation> getResourceEvents(
			@Context HttpServletRequest request,
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") Long envId);

	// Update

	// Delete
	
	// VMs
	/**
	 * @return the list of Virtual Machines and related services (instances &
	 *         types) for a the PaaSService.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}/vms")
	public Collection<VirtualMachineRepresentation> getPaaSServiceVirtualMachines(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") final Long resId);
}
