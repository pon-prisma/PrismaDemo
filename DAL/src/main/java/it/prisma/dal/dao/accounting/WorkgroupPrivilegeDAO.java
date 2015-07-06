package it.prisma.dal.dao.accounting;

import it.prisma.dal.dao.DeprecatedQueryDSLGenericDAO;
import it.prisma.dal.entities.accounting.QWorkgroupPrivilege;
import it.prisma.dal.entities.accounting.WorkgroupPrivilege;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;

@Local(WorkgroupPrivilegeDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class WorkgroupPrivilegeDAO extends DeprecatedQueryDSLGenericDAO<WorkgroupPrivilege, Long> {

	private QWorkgroupPrivilege qWorkgroupPrivilege;
	
	public WorkgroupPrivilegeDAO() {
		super(WorkgroupPrivilege.class);
	}
	
	@PostConstruct
	private void getProperQ() {
		qWorkgroupPrivilege = QWorkgroupPrivilege.workgroupPrivilege;
	}
	
	public WorkgroupPrivilege findByName(final String name) throws EntityNotFoundException {
		WorkgroupPrivilege workgroupPrivilege = super.prepareJPAQuery()
				.from(qWorkgroupPrivilege)
				.where(qWorkgroupPrivilege.name.eq(name))
				.uniqueResult(qWorkgroupPrivilege);
		if (workgroupPrivilege == null)
			throw new EntityNotFoundException("Cannot find entity "
					+ WorkgroupPrivilege.class.getCanonicalName()
					+ " with name <" + name + ">");
		else
			return workgroupPrivilege;
	}
	
}
