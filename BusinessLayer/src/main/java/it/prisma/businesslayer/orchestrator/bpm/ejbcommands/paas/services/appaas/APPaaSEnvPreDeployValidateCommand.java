//package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services.appaas;
//
//import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
//import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
//import it.prisma.businesslayer.utils.mappinghelpers.APPaaSEnvironmentMappingHelper;
//import it.prisma.dal.dao.paas.services.appaas.APPaaSEnvironmentDAO;
//import it.prisma.dal.dao.paas.services.appaas.ApplicationRepositoryEntryDAO;
//import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
//import it.prisma.dal.entities.paas.services.appaas.ApplicationRepositoryEntry;
//import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
//import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
//import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
//
//import javax.ejb.Local;
//import javax.inject.Inject;
//
//import org.kie.internal.executor.api.CommandContext;
//import org.kie.internal.executor.api.ExecutionResults;
//
///**
// * Bean implementing a command to log PaaSServiceEvent. <br/>
// * <br/>
// * Workitem parameters:
// * 
// * @param LRT
// *            the LRT to log events to.
// * @param PaaSService
// * 
// * @param Type
// * 
// * @param Details
// * 
// * @return .
// * 
// * @throws ORC_WF_DEPLOY_GENERIC_ERROR
// *             in case of generic errors.
// * 
// * @author l.biava
// * 
// */
//@Local(APPaaSEnvPreDeployValidateCommand.class)
//public class APPaaSEnvPreDeployValidateCommand extends BaseCommand {
//
//	@Inject
//	private ApplicationRepositoryEntryDAO appEntryDAO;
//	
//	@Inject
//	private APPaaSEnvironmentDAO appEnvDAO;
//
//	public APPaaSEnvPreDeployValidateCommand() throws Exception {
//		super();
//	}
//
//	@Override
//	public ExecutionResults customExecute(CommandContext ctx) throws Exception {
//
//		// Obtaining parameters
//		
//		AppaaS appaaS = (AppaaS) workItem.getParameter("APPaaS");
//		APPaaSDeployRequest deployRequest = (APPaaSDeployRequest) workItem
//				.getParameter("APPaaSDeployRequest");
//
//		ExecutionResults exResults = new ExecutionResults();
//
//		try {
//			// Check Environment name doesn't already exist
//			if(appEnvDAO.findByName(deployRequest.getEnvironmentParams().getEnvName())!=null)
//				return errorOccurred(OrchestratorErrorCode.ORC_ITEM_ALREADY_EXISTS,
//						"APPaaSEnv duplicated name", exResults);
//			
//			// Build AppEnv entity
//			AppaaSEnvironment appEnv;
//			try {
//				appEnv = APPaaSEnvironmentMappingHelper
//						.generateFromAPPaaSDeployRequest(appaaS, deployRequest);				
//			} catch (Exception e) {
//				return errorOccurred(OrchestratorErrorCode.ORC_BAD_REQUEST, "Invalid APPaaSDeployRequest", exResults);
//			}
//			
//			ApplicationRepositoryEntry appRepoEntry = appEntryDAO
//					.findById(deployRequest.getEnvironmentParams()
//							.getAppFileId());
//
//			// Check AppSrcFile
//			// AppSrcFile exists?
//			if (appRepoEntry == null)
//				return errorOccurred(OrchestratorErrorCode.ORC_ITEM_NOT_FOUND,
//						"AppSrcFile", exResults, new ResourceNotFoundException(
//								ApplicationRepositoryEntry.class));
//
//			// Check private & ownership
//			if(!appRepoEntry.isPublic()&&appRepoEntry.getWorkgroup().getId()!=deployRequest.getWorkgroupId())
//				return errorOccurred(OrchestratorErrorCode.ORC_NOT_AUTHORIZED,
//						"AppSrcFile private", exResults);
//
//			appEnv.setApplicationRepositoryEntry(appRepoEntry);
//
//			resultOccurred(appEnv, exResults);
//		} catch (Exception sysEx) {
//			// System Exception occurred
//			handleSystemException(sysEx,
//					OrchestratorErrorCode.ORC_WF_DEPLOY_GENERIC_ERROR,
//					exResults);
//		}
//
//		return exResults;
//	}
//}
