package it.prisma.dal.dao.iaas.services.vm;

import it.prisma.dal.dao.paas.services.AbstractPaaSServiceDAO;
import it.prisma.dal.entities.iaas.services.vm.QVMaaS;
import it.prisma.dal.entities.iaas.services.vm.VMaaS;
import it.prisma.dal.entities.paas.services.QPaaSService;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(VMaaSDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class VMaaSDAO extends AbstractPaaSServiceDAO<VMaaS, Long> {

	private QVMaaS qVMaaS;

	public VMaaSDAO() {
		super(VMaaS.class);
		this.qVMaaS = QVMaaS.vMaaS;
	}

	/**
	 * At least one of the parameters MUST be specified.
	 * 
	 * @param userAccountId
	 * @param workgroupId
	 * @return
	 */
	public Collection<VMaaS> findByUserAndWorkgroup(Long userAccountId,
			Long workgroupId, boolean considerDeletedToo) {

		if (!(userAccountId != null || workgroupId != null))
			throw new IllegalArgumentException(
					"Either userAccountId or workgroupId > 0.");
		
		JPAQuery query = super.prepareJPAQuery().from(qVMaaS)
				.join(qVMaaS.paaSService, QPaaSService.paaSService);
		

		if (userAccountId != null)
			query.where(QPaaSService.paaSService.userAccount.id
					.eq(userAccountId));

		if (workgroupId != null)
			query.where(QPaaSService.paaSService.workgroup.id.eq(workgroupId));

		if (!considerDeletedToo) {
			query.where(ignoreDeletedPredicate());
		}
			
		query.orderBy( qVMaaS.id.asc());
		
		return getListResults(query, qVMaaS);
		
		
		//return query.list(qVMaaS);
	}

}
