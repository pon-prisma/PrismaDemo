package it.prisma.businesslayer.bizws.misc;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.prisma.DebugInformations;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/misc")
public interface MiscWS {

	/**
	 * Checks whether the given Domain Name is available.
	 * 
	 * @param dn
	 *            the domain name, in query param form.
	 * @return <tt>true</tt> if the the domain name is <b>currently</b>
	 *         available, <tt>false</tt> otherwise.<br>
	 *         <b>Note</b>: the domain name is not reserved after this call.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/dns/available")
	public Response isDomainNameAvailable(
			@QueryParam("dn") final String domainName);

	/**
	 * Return debugging informations (like current server name and build
	 * version).
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/debug")
	@PrismaWrapper
	public DebugInformations debugInformations();
}
