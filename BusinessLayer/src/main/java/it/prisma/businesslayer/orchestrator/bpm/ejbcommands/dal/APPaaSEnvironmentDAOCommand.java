package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.dal;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSEnvironmentDAO;
import it.prisma.dal.dao.paas.services.appaas.ApplicationRepositoryEntryDAO;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.ejb.Local;
import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * Bean implementing a command to access APPaaSEnvironment Entity. <br/>
 * <br/>
 * Workitem parameters:
 * 
 * @param LRT
 *            the LRT to log events to.
 * @param Operation
 *            the requested operation (only CRUD supported: {create, read,
 *            update, delete}).
 * @param APPaaSEnvironment
 *            the APPaaSEnvironment Entity to create or update
 * @param APPaaSEnvironmentID
 *            the APPaaSEnvironment Id to read or delete
 * @param APPaaSID
 *            the APPaaSID for read operation (to use with
 *            APPaaSEnvironmentName)
 * @param APPaaSEnvironmentName
 *            the APPaaSEnvironment Name for read operation (to use with
 *            APPaaSID)
 * 
 * @return [op=create], the newly created 'APPaaSEnvironment' entity. <br/>
 *         [op=read], the 'APPaaSEnvironment' entity or null if it doesn't
 *         exist. <br/>
 *         [op=update], the updated 'APPaaSEnvironment' entity. <br/>
 *         [op=delete], nothing.
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
@Local(APPaaSEnvironmentDAOCommand.class)
@Deprecated
public class APPaaSEnvironmentDAOCommand extends BaseCommand {

	@Inject
	private APPaaSEnvironmentDAO appaasEnvDAO;

	@Inject
	private PaaSServiceDAO paaSServiceDAO;

	@Inject
	protected ApplicationRepositoryEntryDAO appRepoEntryDAO;

	public APPaaSEnvironmentDAOCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		String operation = (String) workItem.getParameter("Operation");
		APPaaSDeployRequest appDeployRequest = (APPaaSDeployRequest) workItem
				.getParameter("APPaaSDeployRequest");

		ExecutionResults exResults = new ExecutionResults();

		// Check the monitoring using the API Client
		try {
			if ("create".equals(operation)) {
				AppaaSEnvironment appaasEnv = (AppaaSEnvironment) workItem
						.getParameter("APPaaSEnvironment");

//				appaasEnv.setApplicationRepositoryEntry(appRepoEntryDAO
//						.findById(appDeployRequest.getEnvironmentParams()
//								.getAppFileId()));

				createEntity(appaasEnv, exResults);
			} else if ("read".equals(operation)) {
				String APPaaSEnvironmentID = (String) workItem
						.getParameter("APPaaSEnvironmentID");
				String APPaaSEnvironmentName = (String) workItem
						.getParameter("APPaaSEnvironmentName");

				Long APPaaSId = 0L;

				try {
					APPaaSId = new Long(
							(String) workItem.getParameter("APPaaSId"));
				} catch (Exception e) {
				}

				if (APPaaSEnvironmentID != null) {
					getEntity(new Long(APPaaSEnvironmentID), exResults);
				} else {
					getEntity(APPaaSEnvironmentName, APPaaSId, exResults);
				}
			} else if ("update".equals(operation)) {
				AppaaSEnvironment appaasEnv = (AppaaSEnvironment) workItem
						.getParameter("APPaaSEnvironment");

				updateEntity(appaasEnv, exResults);
			} else if ("delete".equals(operation)) {
				Long APPaaSEnvironmentID = new Long(
						(String) workItem.getParameter("APPaaSEnvironmentID"));

				deleteEntity(APPaaSEnvironmentID, exResults);
			}

		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}

	private void getEntity(String APPaaSEnvironmentName, Long APPaaSId,
			ExecutionResults exResults) throws Exception {

		AppaaSEnvironment appaaS = appaasEnvDAO.findByName(
				APPaaSEnvironmentName);

		if (appaaS == null) {
			resultOccurred(null, exResults);
			return;
		}

		exResults.setData("APPaaSEnvironment", appaaS);
		resultOccurred(appaaS, exResults);
	}

	private void getEntity(long APPaaSEnvironmentID, ExecutionResults exResults)
			throws Exception {

		AppaaSEnvironment appaaS = appaasEnvDAO.findById(APPaaSEnvironmentID);

		if (appaaS == null) {
			resultOccurred(null, exResults);
			return;
		}

		exResults.setData("APPaaSEnvironment", appaaS);
		resultOccurred(appaaS, exResults);

	}

	private void createEntity(AppaaSEnvironment appaasEnv,
			ExecutionResults exResults) {

		try {
			// Must create PaaSService entity, which should not already exist !
			// TODO: Check if needed or is done automatically by Hibernate !
			paaSServiceDAO.create(appaasEnv.getPaaSService());

			AppaaSEnvironment appaaS = appaasEnvDAO.create(appaasEnv);

			exResults.setData("APPaaSEnvironment", appaaS);
			resultOccurred(appaaS, exResults);
		} catch (Exception e) {
			throw e;
		}
	}

	private void updateEntity(AppaaSEnvironment appaasEnv,
			ExecutionResults exResults) {
		AppaaSEnvironment appaaS = appaasEnvDAO.update(appaasEnv);

		exResults.setData("APPaaSEnvironment", appaaS);
		resultOccurred(appaaS, exResults);
	}

	private void deleteEntity(long APPaaSEnvironmentID,
			ExecutionResults exResults) {
		appaasEnvDAO.delete(APPaaSEnvironmentID);
		resultOccurred("OK", exResults);
	}

}
