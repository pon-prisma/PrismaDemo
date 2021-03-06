package it.prisma.dal.dao.paas.services.biaas;

import it.prisma.dal.dao.paas.services.AbstractPaaSServiceDAO;
import it.prisma.dal.entities.paas.services.QPaaSService;
import it.prisma.dal.entities.paas.services.biaas.BIaaS;
import it.prisma.dal.entities.paas.services.biaas.QBIaaS;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(BIaaSDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class BIaaSDAO extends AbstractPaaSServiceDAO<BIaaS, Long> {

	private QBIaaS qBIaaS;

	public BIaaSDAO() {
		super(BIaaS.class);
		qBIaaS = QBIaaS.bIaaS;
	}

	/**
	 * At least one of the parameters MUST be specified.
	 * 
	 * @param userAccountId
	 * @param workgroupId
	 * @param considerDeletedToo specify if the deleted BIaaSServices have to be included
	 * @return
	 */
	public Collection<BIaaS> findByUserAndWorkgroup(Long userAccountId,
			Long workgroupId, boolean considerDeletedToo) {

		if (!(userAccountId != null || workgroupId != null))
			throw new IllegalArgumentException(
					"Either userAccountId or workgroupId > 0.");

		JPAQuery query = super.prepareJPAQuery().from(qBIaaS)
				.join(qBIaaS.paaSService, QPaaSService.paaSService);

		if (userAccountId != null)
			query.where(QPaaSService.paaSService.userAccount.id
					.eq(userAccountId));

		if (workgroupId != null)
			query.where(QPaaSService.paaSService.workgroup.id.eq(workgroupId));
		
		if (!considerDeletedToo) {
			query.where(ignoreDeletedPredicate());
		}

		return getListResults(query, qBIaaS);
	}

}
