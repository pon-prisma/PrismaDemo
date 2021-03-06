package it.prisma.businesslayer.bizws.paas.services.biaas;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.paas.services.PaaSServiceGenericWS;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/workgroups/{workgroupId}/paas/biaas")
@PrismaWrapper
public interface BIaaSWS extends
		PaaSServiceGenericWS<BIaaSRepresentation, BIaaSDeployRequest> {

	// Stop service
	/**
	 * Stops the service.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}/stop")
	public Response stopService(@Context HttpServletRequest request,
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") Long resId);

	/**
	 * Start the service.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}/start")
	public Response startService(@Context HttpServletRequest request,
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") Long resId);

	/**
	 * Restart the service.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}/reboot")
	public Response rebootService(@Context HttpServletRequest request,
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") Long resId);

	/**
	 * Undeploy the service.
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}")
	public void deleteResource(@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") final Long resId);

}
