package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.paas.services;

import it.prisma.businesslayer.bizlib.misc.events.DeployingPrismaEvents;
import it.prisma.businesslayer.bizlib.misc.events.PrismaEvent;
import it.prisma.businesslayer.bizlib.paas.deployments.PaaSDeploymentManagement;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.dao.paas.deployments.PaaSDeploymentDAO;
import it.prisma.dal.dao.paas.deployments.PaaSDeploymentServiceDAO;
import it.prisma.dal.dao.paas.deployments.PaaSDeploymentServiceInstanceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * Bean implementing a command to log PaaSServiceEvent. <br/>
 * <br/>
 * Workitem parameters:
 * 
 * @param LRT
 *            the LRT to log events to.
 * @param PaaSService
 * 
 * @param Type
 * 
 * @param Details
 * 
 * @return .
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
@Local(APPaaSEnvPostDeployDBUpdateCommand.class)
public class APPaaSEnvPostDeployDBUpdateCommand extends BaseCommand {

	@Inject
	private PaaSServiceDAO paaSSvcDAO;

	@Inject
	private PaaSDeploymentDAO paasDeploymentDAO;

	@Inject
	private PaaSDeploymentServiceDAO paasDeploymentSvcDAO;

	@Inject
	private PaaSDeploymentServiceInstanceDAO paasDeploymentSvcInstDAO;

	@Inject
	private PaaSServiceEventDAO paasServiceEventDAO;

	@Inject
	private PaaSDeploymentManagement paasDeploymentBean;

	public APPaaSEnvPostDeployDBUpdateCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		AppaaSEnvironment appaaSEnv = (AppaaSEnvironment) workItem
				.getParameter("APPaaSEnvironment");
		// ServiceInstanceDescriptionResponse cfySvcInstanceDescr =
		// (ServiceInstanceDescriptionResponse)
		// workItem.getParameter("CfySvcInstDescr");

		ExecutionResults exResults = new ExecutionResults();

		try {
			PaaSService paasSvc = appaaSEnv.getPaaSService();

			// Persist the whole PaaSDeployment
			PaaSDeployment paasD = paasSvc.getPaaSDeployment();

			paasDeploymentDAO.create(paasD);

			for (PaaSDeploymentService service : paasD
					.getPaaSDeploymentServices()) {
				paasDeploymentSvcDAO.create(service);
				for (PaaSDeploymentServiceInstance instance : service
						.getPaaSDeploymentServiceInstances()) {
					paasDeploymentSvcInstDAO.create(instance);
				}
			}

			paaSSvcDAO.update(appaaSEnv.getPaaSService());

			// Log newly created services
			Map<String, List<PaaSDeploymentServiceInstance>> hosts = paasDeploymentBean
					.groupByHost(paasSvc.getPaaSDeployment());

			for (Map.Entry<String, List<PaaSDeploymentServiceInstance>> host : hosts
					.entrySet()) {

				String itemDescription = "Host";
				String servicesDescription = "(with services ";

				// Retrieve ServiceInstances for each host
				for (PaaSDeploymentServiceInstance svcInst : host.getValue()) {
					servicesDescription += svcInst.getPaaSDeploymentService()
							.getType() + ",";
				}
				servicesDescription = servicesDescription.substring(0,
						servicesDescription.length() - 1);
				servicesDescription += ")";

				PrismaEvent prismaEvt = new PrismaEvent(
						DeployingPrismaEvents.DEPLOY_ITEM_CREATED,
						itemDescription, servicesDescription);

				PaaSServiceEvent paaSSvcEvt = new PaaSServiceEvent(paasSvc,
						prismaEvt.getType().toString(), prismaEvt.getDetails());
				paasServiceEventDAO.create(paaSSvcEvt);
			}

			resultOccurred("OK", exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}

}
