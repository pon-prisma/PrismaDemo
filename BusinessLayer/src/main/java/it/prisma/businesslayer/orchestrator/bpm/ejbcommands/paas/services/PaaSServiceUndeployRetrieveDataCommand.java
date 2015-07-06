package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestratorBean;
import it.prisma.businesslayer.bizlib.paas.services.dbaas.exceptions.IllegalServiceStatusException;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.DeployerDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.deployment.DeployerType;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.iaas.services.vm.VMaaSDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSEnvironmentDAO;
import it.prisma.dal.dao.paas.services.biaas.BIaaSDAO;
import it.prisma.dal.dao.paas.services.dbaas.DBaaSDAO;
import it.prisma.dal.dao.paas.services.mqaas.MQaaSDAO;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.iaas.services.vm.VMaaS;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entities.paas.services.biaas.BIaaS;
import it.prisma.dal.entities.paas.services.dbaas.DBaaS;
import it.prisma.dal.entities.paas.services.mqaas.MQaaS;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.utils.misc.StackTrace;

import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

public class PaaSServiceUndeployRetrieveDataCommand extends BaseCommand {

	@Inject
	WorkgroupDAO workgroupDAO;
	
	@Inject
	private APPaaSEnvironmentDAO aPPaaSEnvironmentDAO;
	
	@Inject
	private DBaaSDAO dBaaSDAO;
	
	@Inject
	private MQaaSDAO mQaaSDAO;
	
	@Inject
	private BIaaSDAO bIaaSDAO;

	@Inject
	private VMaaSDAO vMaaSDAO;

	public PaaSServiceUndeployRetrieveDataCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		UndeploymentMessage undepMsg = (UndeploymentMessage) workItem
				.getParameter(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG);
		Preconditions.checkNotNull(undepMsg);

		ExecutionResults exResults = new ExecutionResults();

		AbstractPaaSService paasService;
		GenericPaaSServiceRepresentation paaSServiceRepresentation = undepMsg.getPaaSServiceRepresentation();
		
		{
			Workgroup workgroup = workgroupDAO.findById(paaSServiceRepresentation.getWorkgroupId());
			undepMsg.setWorkgroup(workgroup);
		}

		try {
			if (paaSServiceRepresentation instanceof APPaaSEnvironmentRepresentation) {
				APPaaSEnvironmentRepresentation aPPaaSEnvironmentRepresentation = (APPaaSEnvironmentRepresentation) paaSServiceRepresentation;
				AppaaSEnvironment appaaSEnvironment = (AppaaSEnvironment) aPPaaSEnvironmentDAO.findById(aPPaaSEnvironmentRepresentation.getId());
				if (appaaSEnvironment != null) {
					paasService = appaaSEnvironment;
					Workgroup workgroup = appaaSEnvironment.getPaaSService().getWorkgroup();
					PaaSService appaaS = appaaSEnvironment.getAppaaS();
					
					if (workgroup == null || !workgroup.getId().equals(undepMsg.getWorkgroup().getId())
							|| appaaS == null || !appaaS.getId().equals(aPPaaSEnvironmentRepresentation.getAppaasId())) {
						throw new ResourceNotFoundException(AppaaSEnvironment.class);
					}
				} else {
					throw new ResourceNotFoundException(AppaaSEnvironment.class);
				}
			} else if (paaSServiceRepresentation instanceof MQaaSRepresentation) {
				MQaaSRepresentation mQaaSRepresentation = (MQaaSRepresentation) paaSServiceRepresentation;
				MQaaS mQaaS = (MQaaS) mQaaSDAO.findById(mQaaSRepresentation.getId());
				if (mQaaS != null) {
					paasService = mQaaS;
					Workgroup workgroup = mQaaS.getPaaSService().getWorkgroup();
					if (workgroup == null || !workgroup.getId().equals(undepMsg.getWorkgroup().getId())) {
						throw new ResourceNotFoundException(MQaaS.class);
					}
				} else {
					throw new ResourceNotFoundException(MQaaS.class);
				}
			} else if (paaSServiceRepresentation instanceof DBaaSRepresentation) {
				DBaaSRepresentation dBaaSRepresentation = (DBaaSRepresentation) paaSServiceRepresentation;
				DBaaS dBaaS = (DBaaS) dBaaSDAO.findById(dBaaSRepresentation.getId());
				if (dBaaS != null) {
					paasService = dBaaS;
					Workgroup workgroup = dBaaS.getPaaSService().getWorkgroup();
					if (workgroup == null || !workgroup.getId().equals(undepMsg.getWorkgroup().getId())) {
						throw new ResourceNotFoundException(DBaaS.class);
					}
				} else {
					throw new ResourceNotFoundException(DBaaS.class);
				}
			} else if (paaSServiceRepresentation instanceof BIaaSRepresentation) {
				BIaaSRepresentation bIaaSRepresentation = (BIaaSRepresentation) paaSServiceRepresentation;
				BIaaS bIaaS = (BIaaS) bIaaSDAO.findById(bIaaSRepresentation.getId());
				if (bIaaS != null) {
					paasService = bIaaS;
					Workgroup workgroup = bIaaS.getPaaSService().getWorkgroup();
					if (workgroup == null || !workgroup.getId().equals(undepMsg.getWorkgroup().getId())) {
						throw new ResourceNotFoundException(BIaaS.class);
					}
				} else {
					throw new ResourceNotFoundException(BIaaS.class);
				}
			} else if (paaSServiceRepresentation instanceof VMRepresentation) {
				VMRepresentation vMRepresentation = (VMRepresentation) paaSServiceRepresentation;
				VMaaS vMaaS = (VMaaS) vMaaSDAO.findById(vMRepresentation.getId());
				if (vMaaS != null) {
					paasService = vMaaS;
					Workgroup workgroup = vMaaS.getPaaSService().getWorkgroup();
					if (workgroup == null || !workgroup.getId().equals(undepMsg.getWorkgroup().getId())) {
						throw new ResourceNotFoundException(VMaaS.class);
					}
				} else {
					throw new ResourceNotFoundException(VMaaS.class);
				}
			} else {
				throw new Exception("PaaSService type not supported: " + paaSServiceRepresentation.getClass().getCanonicalName() + ".");
			}
			
			PaaSDeployment paaSDeployment = paasService.getPaaSService().getPaaSDeployment();
			if (paaSDeployment != null) {
				DeployerType deployerType = DeployerType.valueOf(paasService.getPaaSService().getPaaSDeployment().getPaaSType());
				undepMsg.setDeployerType(deployerType);
				
				//TODO gestire eventualmente meglio se si vuole assegnare tempi di timeout diversi a seconda del tipo di PaaS
				DeployerDeploymentData deployerDeploymentData;
				if (deployerType == DeployerType.HEAT) {
					deployerDeploymentData = new HeatDeploymentData();
				} else {
					deployerDeploymentData = new CloudifyDeploymentData();
				}
				
				deployerDeploymentData.setDeploymentCheckTimeout(30 * 1000);
				deployerDeploymentData.setDeploymentFailTimeout(30 * 60 * 1000);
				undepMsg.setDeploymentData(deployerDeploymentData);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			switch (PaaSService.Status.valueOf(paasService.getPaaSService().getStatus())) {
			case DELETED:
				throw new IllegalServiceStatusException("Service already undeployed.");
			case DEPLOY_IN_PROGRESS:
				throw new IllegalServiceStatusException("Cannot undeploy a service still in deployment status.");
			case DEPLOY_PENDING:
				//TODO probabilmente basterebbe rimuovere il deploy in coda (se questo è quello che DEPLOY_PENDING rappresenta)
				throw new IllegalServiceStatusException("Cannot undeploy a service with a pending deploy.");
			case ERROR:
				Boolean undeployServiceWithError = (Boolean) workItem.getParameter("UndeployServiceWithError");
				if (undeployServiceWithError == null || !undeployServiceWithError) {
					throw new IllegalServiceStatusException("Cannot undeploy a service in a error status.");
				}
				break;
			case RESTART_IN_PROGRESS:
				throw new IllegalServiceStatusException("Cannot undeploy a restarting service.");
			case START_IN_PROGRESS:
				throw new IllegalServiceStatusException("Cannot undeploy a starting service.");
			case STOP_IN_PROGRESS:
				throw new IllegalServiceStatusException("Cannot undeploy a stopping service.");
			case UNDEPLOY_IN_PROGRESS:
				throw new IllegalServiceStatusException("Cannot undeploy a service already in undeployment status.");
			default:
				throw new Exception("Unsupported PaaSService status: ID " + paasService.getPaaSService().getId() + ", status " + paasService.getPaaSService().getStatus() + ".");
			case RUNNING:
			case STOPPED:
				break;
			}				
			
			undepMsg.setPaaSService(paasService);
			
			//TODO necessario per eseguire il fetch quando ancora il database è connesso
			if (paaSDeployment != null) {
				Hibernate.initialize(paaSDeployment.getPaaSDeploymentServices());
				for (PaaSDeploymentService pds : paaSDeployment.getPaaSDeploymentServices()) {
					Hibernate.initialize(pds.getPaaSDeploymentServiceInstances());
				}
			}
			//TODO: Set this status elsewhere or remove
			paasService.getPaaSService().setStatus(PaaSService.Status.UNDEPLOY_IN_PROGRESS.name());
			
			exResults.setData(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG, undepMsg);
			resultOccurred(undepMsg, exResults);
		} catch (ResourceNotFoundException nfEx) {
			errorOccurred(OrchestratorErrorCode.ORC_ITEM_NOT_FOUND,
					StackTrace.getStackTraceToString(nfEx), exResults);
		} catch (IllegalServiceStatusException issEx) {
			errorOccurred(OrchestratorErrorCode.ORC_ILLEGAL_SERVICE_STATE,
					StackTrace.getStackTraceToString(issEx), exResults);
		} catch	(Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_UNDEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}
}
