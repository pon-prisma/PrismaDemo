package it.prisma.businesslayer.bizlib.orchestration.deployment;

import it.prisma.dal.entities.paas.services.AbstractPaaSService;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

public interface PaaSServiceUpdateDB {
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public AbstractPaaSService createPaaSService(AbstractPaaSService paasService);

	//WARNING: Might be useful, but creates entities detached problems at the end of the inner Tx
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public AbstractPaaSService updatePaaSService(AbstractPaaSService paasService);
}
