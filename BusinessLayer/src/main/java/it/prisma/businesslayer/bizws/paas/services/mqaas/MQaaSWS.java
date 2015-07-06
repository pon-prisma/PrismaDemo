package it.prisma.businesslayer.bizws.paas.services.mqaas;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.paas.services.PaaSServiceGenericWS;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/workgroups/{workgroupId}/paas/mqaas")
@PrismaWrapper
public interface MQaaSWS extends PaaSServiceGenericWS<MQaaSRepresentation, MQaaSDeployRequest> {

	/**
	 * Undeploy the service.
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resId}")
	public void deleteResource(@PathParam("workgroupId") final Long workgroupId,
			@PathParam("resId") final Long resId);
}
