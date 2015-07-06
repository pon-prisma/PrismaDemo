package it.prisma.businesslayer.bizlib.orchestration;

import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

public interface PaaSServiceManager {

	/**
	 * Immediately logs the an event, using a nested transaction.
	 * 
	 * @param paasService
	 * @param event
	 * @return the newly created event. <b>Warning:</b> it might be detached,
	 *         because of the nested transaction.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public PaaSServiceEvent logPaaSServiceEvent(PaaSService paasService,
			PaaSServiceEvent event);

	/**
	 * Immediately logs the an event, using a nested transaction.
	 * 
	 * @param paasService
	 * @param type
	 * @param details
	 * @param verbose
	 * @return the newly created event. <b>Warning:</b> it might be detached,
	 *         because of the nested transaction.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public PaaSServiceEvent logPaaSServiceEvent(
			AbstractPaaSService paasService, String type, String details,
			String verbose);

	/**
	 * Logs a list of event with informations about the atomic services (i.e.
	 * {@link PaaSDeploymentServiceInstance}) composing the given
	 * {@link PaaSService}.
	 * <br/>
	 * The log is made inside a nested transaction !
	 * 
	 * @param paasService
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void logPaaSServiceComponentsEvents(PaaSService paasService);
}
