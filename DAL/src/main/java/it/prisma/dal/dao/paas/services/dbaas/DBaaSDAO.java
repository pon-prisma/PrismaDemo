package it.prisma.dal.dao.paas.services.dbaas;

import it.prisma.dal.dao.paas.services.AbstractPaaSServiceDAO;
import it.prisma.dal.entities.paas.services.QPaaSService;
import it.prisma.dal.entities.paas.services.dbaas.DBaaS;
import it.prisma.dal.entities.paas.services.dbaas.QDBaaS;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(DBaaSDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class DBaaSDAO extends AbstractPaaSServiceDAO<DBaaS, Long> {

	private QDBaaS qDBaaS;

	public DBaaSDAO() {
		super(DBaaS.class);
		qDBaaS = QDBaaS.dBaaS;
	}

	/**
	 * At least one of the parameters MUST be specified.
	 * 
	 * @param userAccountId
	 * @param workgroupId
	 * @param considerDeletedToo specify if the deleted DBaaSServices have to be included
	 * @return
	 */
	public Collection<DBaaS> findByUserAndWorkgroup(Long userAccountId,
			Long workgroupId, boolean considerDeletedToo) {

		if (!(userAccountId != null || workgroupId != null))
			throw new IllegalArgumentException(
					"Either userAccountId or workgroupId > 0.");

		JPAQuery query = super.prepareJPAQuery().from(qDBaaS)
				.join(qDBaaS.paaSService, QPaaSService.paaSService);

		if (userAccountId != null)
			query.where(QPaaSService.paaSService.userAccount.id
					.eq(userAccountId));

		if (workgroupId != null)
			query.where(QPaaSService.paaSService.workgroup.id.eq(workgroupId));
		
		if (!considerDeletedToo) {
			query.where(ignoreDeletedPredicate());
		}

		return getListResults(query,qDBaaS);
	}

}
