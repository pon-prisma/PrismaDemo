package it.prisma.businesslayer.bizws.iaas.key;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRepresentation;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRequest;

import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/workgroups/{workgroupId}/iaas/keys")	
@PrismaWrapper
public interface KeyWS {

	/**
	 * List all keys in the workgroup.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<KeypairRepresentation> listAllKeys(@PathParam("workgroupId") final Long workgroupId);
	
	/**
	 * Import a keypair in the workgroup.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public KeypairRepresentation importKey(KeypairRequest key, @PathParam("workgroupId") final Long workgroupId);
	
	/**
	 * generate a keypair in the workgroup.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public KeypairRepresentation generateKey(@PathParam("workgroupId") final Long workgroupId, @PathParam("name") final String name);
	
	/**
	 * generate a keypair in the workgroup.
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public Boolean deleteKey(@PathParam("workgroupId") final Long workgroupId, @PathParam("name") final String name);
	
}
