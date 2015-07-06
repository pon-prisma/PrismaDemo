package it.prisma.businesslayer.bizws.paas.deployments;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.deployments.VirtualMachineRepresentation;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/deployments")
@PrismaWrapper
public interface PaaSDeploymentWS {

	/** 
	 * @param id
	 *            the Id of the PaaSService.
	 * @return the list of Virtual Machines and related services (instances &
	 *         types) for a given PaaSService.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{paasSvcId}/vms")
	public Collection<VirtualMachineRepresentation> getVirtualMachinesByPaaSService(@PathParam("paasSvcId") Long id);

}
