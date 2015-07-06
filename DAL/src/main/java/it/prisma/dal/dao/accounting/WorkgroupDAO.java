package it.prisma.dal.dao.accounting;

import static it.prisma.dal.entities.accounting.QWorkgroup.workgroup;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(WorkgroupDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class WorkgroupDAO extends QueryDSLGenericDAO<Workgroup, Long> {

	public WorkgroupDAO() {
		super(Workgroup.class);
	}

	public void approve(final Long workgroupId, final UserAccount approvedBy) {
		Workgroup workgroup = super.findById(workgroupId);
		workgroup.setApproved(true);
		workgroup.setUserAccountByApprovedByUserAccountId(approvedBy);
		super.update(workgroup);
	}

	public void unapprove(final Long workgroupId) {
		Workgroup workgroup = super.findById(workgroupId);
		workgroup.setApproved(false);
		workgroup.setUserAccountByApprovedByUserAccountId(null);
		super.update(workgroup);
	}
		
	public Collection<UserAccount> getReferentsByWorkgroupId(final Long workgroupid)
	{
		JPAQuery query = super.prepareJPAQuery(workgroup)
			.where(workgroup.id.eq(workgroupid));
	
		Workgroup wg = query.singleResult(workgroup);
		return wg.getReferentUsers();
		
	}
	
}