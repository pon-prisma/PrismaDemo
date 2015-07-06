package it.prisma.businesslayer.bizws.paas.services;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/workgroups/{workgroupId}")
@PrismaWrapper
public interface PaasServiceGeneralWS {

	/**
	 * Checks whether the given service name is available in the given
	 * workgroup.
	 * 
	 * @param workgroupId
	 * @param name
	 *            the service name, in query param form.
	 * @return <tt>true</tt> if the the name is <b>currently</b> available,
	 *         <tt>false</tt> otherwise.<br>
	 *         <b>Note</b>: the name is not reserved after this call.
	 * @throws Exception 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/paas/name/available")
	public Boolean isPaaSServiceNameAvailable(
			@PathParam("workgroupId") final Long workgroupId,
			@QueryParam("name") final String serviceName) throws Exception;

}
