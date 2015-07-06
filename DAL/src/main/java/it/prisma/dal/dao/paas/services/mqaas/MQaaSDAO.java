package it.prisma.dal.dao.paas.services.mqaas;

import it.prisma.dal.dao.paas.services.AbstractPaaSServiceDAO;
import it.prisma.dal.entities.paas.services.QPaaSService;
import it.prisma.dal.entities.paas.services.mqaas.MQaaS;
import it.prisma.dal.entities.paas.services.mqaas.QMQaaS;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class MQaaSDAO extends AbstractPaaSServiceDAO<MQaaS, Long> {

	private QMQaaS qMQaaS;

	public MQaaSDAO() {
		super(MQaaS.class);
		qMQaaS = QMQaaS.mQaaS;
	}

	/**
	 * At least one of the parameters MUST be specified.
	 * 
	 * @param userAccountId
	 * @param workgroupId
	 * @param considerDeletedToo specify if the deleted MQaaSServices have to be included
	 * @return
	 */
	public Collection<MQaaS> findByUserAndWorkgroup(Long userAccountId,
			Long workgroupId, boolean considerDeletedToo) {

		if (!(userAccountId != null || workgroupId != null))
			throw new IllegalArgumentException(
					"Either userAccountId or workgroupId > 0.");

		JPAQuery query = super.prepareJPAQuery().from(qMQaaS)
				.join(qMQaaS.paaSService, QPaaSService.paaSService);

		if (userAccountId != null)
			query.where(QPaaSService.paaSService.userAccount.id
					.eq(userAccountId));

		if (workgroupId != null)
			query.where(QPaaSService.paaSService.workgroup.id.eq(workgroupId));
		
		if (!considerDeletedToo) {
			query.where(ignoreDeletedPredicate());
		}

		return getListResults(query, qMQaaS);
	}

}
