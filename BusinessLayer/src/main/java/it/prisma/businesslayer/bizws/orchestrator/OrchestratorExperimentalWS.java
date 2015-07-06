//package it.prisma.businesslayer.bizws.orchestrator;
//
//import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
//import it.prisma.dal.dao.accounting.UserAccountDAO;
//import it.prisma.dal.dao.accounting.WorkgroupDAO;
//import it.prisma.dal.entities.orchestrator.lrt.LRT;
//import it.prisma.domain.dsl.orchestrator.LRTRepresentation;
//import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
//import it.prisma.domain.dsl.prisma.prismaprotocol.Error;
//import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper;
//
//import java.io.Serializable;
//
//import javax.inject.Inject;
//import javax.ws.rs.Path;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
//
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.jbpm.workflow.instance.WorkflowProcessInstance;
//import org.kie.api.runtime.process.ProcessInstance;
//
//@Path("/orc")
//public class OrchestratorExperimentalWS implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	public static Logger prismaLog = LogManager.getLogger(OrchestratorExperimentalWS.class);
//
//	@Inject
//	private MappingHelper<LRT, LRTRepresentation> lrtMH;
//
//	@Inject
//	UserAccountDAO userDAO;
//
//	@Inject
//	WorkgroupDAO workgroupDAO;
//
////	@POST
////	@Produces(MediaType.APPLICATION_JSON)
////	@Consumes(MediaType.APPLICATION_JSON)
////	@Path("/deployments/appaas")
////	public Response provisioningAPPaaS(APPaaSDeployRequest request) {
////
////		try {
////			Map<String, Object> params = new HashMap<String, Object>();
////
////			params.put("CfyVersion", "2.7.0");
////
////			params.put("MonitoringUrl",
////					"http://" + envConfig.getMonitoringURL());
////			params.put("MailserverUrl",
////					"http://" + envConfig.getMailServerURL());
////			params.put("CfyBaseUrl",
////					"http://" + envConfig.getCloudifyManagerURL());
////
////			params.put("APPaaSDeployRequest", request);
////			params.put("User", userDAO.findById(request.getUserId()));
////			params.put("Workgroup", workgroupDAO.findById(request.getTenantId()));
////
////			params.put("ServiceName", "pentaho");
////			String recipePath = this.getClass().getProtectionDomain()
////					.getCodeSource().getLocation().getPath()
////					+ "/cloudify-recipes/" + "pentaho.zip";
////			params.put("RecipePath", recipePath);
////
////			// Start process on jbpm runtime
////			Triplet<ProcessInstance, KieSession, LRT> bpResult = bpManager
////					.startProcess(
////							"it.prisma.orchestrator.bp.services.appaas.provisioning",
////							params, RUNTIME_STRATEGY.SINGLETON);
////			ProcessInstance processInstance = bpResult.getValue0();
////			LRT lrt = bpResult.getValue2();
////
////			return handleProcessInstanceResults(lrt, processInstance);
////
////		} catch (Exception e) {
////			return handleGenericException(e);
////		}
////
////	}
//
//	protected Response handleGenericException(Exception e) {
//		e.printStackTrace();
//
//		// Unexpected error occurred (probably server error)
//		return Response
//				.status(Status.OK)
//				.entity(PrismaResponseWrapper.error(
//						OrchestratorErrorCode.ORC_GENERIC_ERROR,
//						e.getStackTrace().toString()).build()).build();
//	}
//
//	protected Response handleProcessInstanceResults(LRT lrt,
//			ProcessInstance processInstance) {
//		WorkflowProcessInstance wfInstance = (WorkflowProcessInstance) processInstance;
//		
//		// Error occurred (in sync WF) ?
//		if (wfInstance.getState() == ProcessInstance.STATE_ABORTED) {
//
//			// Get the error and send as response (PrismaAPI Error)
//			try {
//				Error wfError = (Error) wfInstance.getVariable("Error");
//				return Response.status(Status.OK)
//						.entity(PrismaResponseWrapper.error(wfError).build())
//						.build();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			// If unable to get the WF error
//			return Response
//					.status(Status.OK)
//					.entity(PrismaResponseWrapper.error(
//							OrchestratorErrorCode.ORC_GENERIC_ERROR,
//							wfInstance.getOutcome()).build()).build();
//
//		} else {
//			// Process running async -> return LRT data
//			it.prisma.domain.dsl.orchestrator.LRTRepresentation dslLrt = lrtMH.getDSL(lrt);
//			
//			return Response.status(Status.OK)
//					.entity(PrismaResponseWrapper.result(dslLrt).build()).build();
//		}
//	}
//
////	@GET
////	@Produces(MediaType.APPLICATION_JSON)
////	@Path("/testPrismaRestClient/user/find/{username}")
////	public Response testPrismaRestClient(@PathParam("username") String username) {
////
////		try {
////			PrismaBizAPIClient prismaClient = new PrismaBizAPIClient(
////					"http://localhost:8080");
////
////			UserInfo user = prismaClient.AccountingFindUserById(username, "");
////
////
////			// return Response.ok(user).build();
////			return Response.status(Status.OK)
////					.entity(PrismaResponseWrapper.result(user).build()).build();
////
////		} catch (APIErrorException apie) {
////			return Response
////					.status(Status.OK)
////					.entity(PrismaResponseWrapper.error(apie.getAPIError())
////							.build()).build();
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			RESTErrors errors = new RESTErrors().addError(new RESTError(500,
////					"Internal error:" + e));
////
////			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
////					.entity(errors).build();
////		}
////
////	}
//
//}