package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.dal;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSDAO;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bean implementing a command to access APPaaS Entity. <br/>
 * <br/>
 * Workitem parameters:
 * 
 * @param LRT
 *            the LRT to log events to.
 * @param Operation
 *            the requested operation (only CRUD supported: {create, read,
 *            update, delete}).
 * @param APPaaS
 *            the APPaaS Entity to create or update
 * @param APPaaSID
 *            the APPaaS Id to read or delete
 * @param WorkgroupID
 *            the APPaaS WorkgroupId for read operation (to use with name)
 * @param APPaaSName
 *            the APPaaS Name for read operation (to use with WorkgroupId)
 * 
 * @return [op=create], the newly created 'APPaaSEnvironment' entity. <br/>
 *         [op=read], the 'APPaaS' entity or null if it doesn't exist. <br/>
 *         [op=update], the updated 'APPaaS' entity. <br/>
 *         [op=delete], nothing.
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
@Local(APPaaSDAOCommand.class)
public class APPaaSDAOCommand extends BaseCommand {

	private static final Logger LOG = LoggerFactory
			.getLogger(APPaaSDAOCommand.class);

	// @Inject
	// private AppaaSDetailsDAO appaaSDetailsDAO;

	@Inject
	private PaaSServiceDAO paaSServiceDAO;

	@Inject
	private APPaaSDAO appaasDAO;

	public APPaaSDAOCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): LRT, APPaaSID or APPaaSName and WorkgroupID.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		String operation = (String) workItem.getParameter("Operation");

		ExecutionResults exResults = new ExecutionResults();

		// Check the monitoring using the API Client
		try {
			if ("create".equals(operation)) {
				AppaaS appaas = (AppaaS) workItem.getParameter("APPaaS");

				createEntity(appaas, exResults);
			} else if ("read".equals(operation)) {
				String APPaaSID = (String) workItem.getParameter("APPaaSID");
				String APPaaSName = (String) workItem
						.getParameter("APPaaSName");
				Long tenantID = 0L;

				try {
					tenantID = new Long(
							(String) workItem.getParameter("WorkgroupID"));
				} catch (Exception e) {
				}

				if (APPaaSID != null) {
					getEntity(new Long(APPaaSID), exResults);
				} else {
					getEntity(APPaaSName, tenantID, exResults);
				}
			} else if ("update".equals(operation)) {
				AppaaS appaas = (AppaaS) workItem.getParameter("AppaaS");

				updateEntity(appaas, exResults);
			} else if ("delete".equals(operation)) {
				Long APPaaSID = (Long) workItem.getParameter("APPaaSID");

				deleteEntity(APPaaSID, exResults);
			}

		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}

	@Resource
	UserTransaction ut;

	private void getEntity(String APPaaSName, long tenantID,
			ExecutionResults exResults) throws Exception {

		AppaaS appaaS = appaasDAO.findByNameAndWorkgroup(APPaaSName, tenantID);

		if (appaaS == null) {
			resultOccurred(null, exResults);
			return;
		}

		exResults.setData("APPaaS", appaaS);
		exResults.setData("APPaaSDetails", appaaS);
		resultOccurred(appaaS, exResults);

	}

	// private void getEntity(String APPaaSName, long tenantID,
	// ExecutionResults exResults) throws Exception {
	// try {
	// AppaaSDetails appaaS = appaasDAO.findByNameAndTenant(APPaaSName,
	// tenantID);
	//
	// exResults.setData("APPaaS", appaaS);
	// resultOccurred(appaaS, exResults);
	// } catch (EJBException ejbe) {
	// try {
	// throw ejbe.getCausedByException();
	// } catch (EntityNotFoundException npe) {
	// resultOccurred(null, exResults);
	// }
	// }
	// }

	private void getEntity(long APPaaSID, ExecutionResults exResults)
			throws Exception {

		AppaaS appaaS = appaasDAO.findById(APPaaSID);

		if (appaaS == null) {
			resultOccurred(null, exResults);
			return;
		}

		exResults.setData("APPaaS", appaaS);
		resultOccurred(appaaS, exResults);
	}

	private void createEntity(AppaaS appaas, ExecutionResults exResults) {
		try {
			// Must create PaaSService entity, which should not already exist !
			// TODO: Check if needed or is done automatically by Hibernate !
			paaSServiceDAO.create(appaas.getPaaSService());

			AppaaS appaaS = appaasDAO.create(appaas);

			exResults.setData("APPaaS", appaaS);
			resultOccurred(appaaS, exResults);
		} catch (Exception e) {
			// LOG.error("Cannot insert PaaSService for APPaaS.", e);
			throw e;
		}
	}

	private void updateEntity(AppaaS appaas, ExecutionResults exResults) {
		AppaaS appaaS = appaasDAO.update(appaas);

		exResults.setData("APPaaS", appaaS);
		resultOccurred(appaaS, exResults);
	}

	private void deleteEntity(long APPaaSID, ExecutionResults exResults) {
		appaasDAO.delete(APPaaSID);
		resultOccurred("OK", exResults);
	}

}
