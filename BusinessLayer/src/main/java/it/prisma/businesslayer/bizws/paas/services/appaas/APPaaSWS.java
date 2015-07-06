package it.prisma.businesslayer.bizws.paas.services.appaas;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.paas.services.PaaSServiceGenericWS;
import it.prisma.domain.dsl.deployments.VirtualMachineRepresentation;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("workgroups/{workgroupId}/paas/appaas")
@PrismaWrapper
public interface APPaaSWS extends PaaSServiceGenericWS<APPaaSRepresentation, APPaaSDeployRequest> {

	public static final String APPaaSEnvAppSrcFile_REST_URL = "/workgroups/{workgroupId}/paas/appaas/{appId}/environments/{envId}/appsrcfile";

	// Create
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{appId}/environments/")
	public APPaaSEnvironmentRepresentation createAppaaSEnv(
			@PathParam("workgroupId") Long workgroupId,
			@PathParam("appId") Long appId,
			APPaaSEnvironmentDeployRequest deployRequest);

	// Read

	/**
	 * Returns a list of {@link AppaaSRepresentation} with the given workgroup
	 * and user. At least one of the filter parameters MUST be specified.
	 * 
	 * @param workgroupId
	 * @return a list of {@link AppaaSRepresentation}.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<APPaaSRepresentation> getAPPaaSByWorkgroupAndUser(
			@PathParam("workgroupId") final Long workgroupId,
			@DefaultValue("false") @QueryParam("consider_deleted") final String considerDeletedToo);

	/**
	 * @param workgroupId
	 * @param appId
	 * @return the {@link AppaaSRepresentation} with the given Id.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{appId}")
	public APPaaSRepresentation getAPPaaSById(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("appId") final Long appId);

	/**
	 * 
	 * 
	 * @param workgroupId
	 * @param appId
	 * @return the {@link AppaaSRepresentation} with the given Id.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{appId}")
	public APPaaSRepresentation updateAPPaaSById(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("appId") final Long appId,
			APPaaSRepresentation appRepresentation);

	/**
	 * @param workgroupId
	 * @param appId
	 * @return the list of {@link APPaaSEnvironmentRepresentation} belonging to
	 *         the APPaaS with the given Id.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{appId}/environments")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<APPaaSEnvironmentRepresentation> getAllAPPaaSEnvironment(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("appId") final Long appId,
			@DefaultValue("false") @QueryParam("consider_deleted") final String considerDeletedToo);

	/**
	 * @param workgroupId
	 * @param appId
	 * @param envId
	 * @return the {@link APPaaSEnvironmentRepresentation} with the given envId.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{appId}/environments/{envId}")
	public APPaaSEnvironmentRepresentation getAPPaaSEnvironmentById(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("appId") final Long appId,
			@PathParam("envId") final Long envId);

	/**
	 * @param workgroupId
	 * @param appId
	 * @param envId
	 * @param from
	 * @param to
	 * @param size
	 * @return the list of {@link PaaSServiceEventRepresentation} related to the
	 *         given envId.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{appId}/environments/{envId}/events")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<PaaSServiceEventRepresentation> getAPPaaSEnvironmentEventsByEnvId(
			@Context HttpServletRequest request,
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("appId") final Long appId, @PathParam("envId") Long envId);

	// AppSrcFile

	/**
	 * Returns only the metadata of the source file for a given APPaaS
	 * Environment.
	 * 
	 * @return the metadata of the file in the headers.
	 */
	@HEAD
	@Path("{appId}/environments/{envId}/appsrcfile")
	@PrismaWrapper(enabled = false)
	public Response headAPPaaSEnvAppSrcFile(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("appId") final Long appId,
			@PathParam("envId") final Long envId);

	/**
	 * Returns the stream of the source file for a given APPaaS Environment.
	 * 
	 * @return the stream of the file, with metadata in the headers.
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("{appId}/environments/{envId}/appsrcfile")
	@PrismaWrapper(enabled = false)
	public Response getAPPaaSEnvAppSrcFile(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("appId") final Long appId,
			@PathParam("envId") final Long envId);

	/**
	 * @return the list of Virtual Machines and related services (instances &
	 *         types) for a the PaaSService.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{appId}/environments/{envId}/vms")
	public Collection<VirtualMachineRepresentation> getAppaaSEnvVirtualMachines(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("appId") final Long appId,
			@PathParam("envId") final Long envId);

	// Update

	// Delete
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{appId}/environments/{envId}")
	public void deleteAppaaSEnv(
			@PathParam("workgroupId") final Long workgroupId,
			@PathParam("appId") final Long appId,
			@PathParam("envId") final Long envId);
}
