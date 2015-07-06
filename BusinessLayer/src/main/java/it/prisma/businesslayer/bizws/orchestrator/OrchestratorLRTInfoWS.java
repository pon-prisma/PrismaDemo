package it.prisma.businesslayer.bizws.orchestrator;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.dal.dao.accounting.UserAccountDAO;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.orchestrator.lrt.LRTDAO;
import it.prisma.dal.dao.orchestrator.lrt.LRTEventDAO;
import it.prisma.dal.entities.orchestrator.lrt.LRT;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent;
import it.prisma.domain.dsl.orchestrator.LRTRepresentation;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponse;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/orc")
public class OrchestratorLRTInfoWS implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger prismaLog = LogManager
			.getLogger(OrchestratorLRTInfoWS.class);

	@Inject
	private MappingHelper<LRT, LRTRepresentation> lrtMH;

	@Inject
	UserAccountDAO userDAO;

	@Inject
	WorkgroupDAO workgroupDAO;

	@Inject
	private LRTDAO lrtDAO;

	@Inject
	private LRTEventDAO lrtEventDAO;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/info/lrtinfo/{lrtId}")
	public Response getProcessInfo(@PathParam("lrtId") Long lrtId)
			throws Exception {

		try {
			try {
				LRT lrt = lrtDAO.findById(lrtId);
				if(lrt==null)
					throw new Exception("LRT not found.");
				
				it.prisma.domain.dsl.orchestrator.LRTRepresentation dslLrt = lrtMH
						.getDSL(lrt);

				return PrismaResponse.status(Status.OK, dslLrt).build().build();

				// return Response.status(Status.OK)
				// .entity(PrismaResponseWrapper.result(dslLrt).build())
				// .build();

			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception npe) {

			return PrismaResponse
					.status(Status.NOT_FOUND,
							OrchestratorErrorCode.ORC_LRT_NOT_FOUND, "")
					.build().build();

			// return Response
			// .status(Status.OK)
			// .entity(PrismaResponseWrapper.error(
			// OrchestratorErrorCode.ORC_LRT_NOT_FOUND, "").build())
			// .build();
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/lrtevents/{lrtId}")
	public Response getProcessEvents(@PathParam("lrtId") Long lrtId) {

		return getProcessEvents(lrtId, null);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/lrtevents/{lrtId}/{createdAt}")
	public Response getProcessEvents(@PathParam("lrtId") Long lrtId,
			@PathParam("createdAt") Long createdAt) {

		List<LRTEvent> lrtevents = lrtEventDAO.getByLRT(lrtId, createdAt);

		/*
		 * if(lrt==null){ RESTErrors errors = new RESTErrors().addError(new
		 * RESTError(20,"No such LRT."));
		 * 
		 * // Alternative version to /*ResponseBuilder builder =
		 * Response.status(Response.Status.BAD_REQUEST);
		 * builder.entity("Missing idLanguage parameter on request"); Response
		 * response = builder.build(); throw new
		 * WebApplicationException(response);
		 */

		/*
		 * return
		 * Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
		 * }
		 */

		return Response.ok(lrtevents).build();

		/*
		 * List<LRTEvent> events=lrt.getEvents(); for(LRTEvent event : events)
		 * response+="\n"+event;
		 * 
		 * return response;
		 */
	}

}