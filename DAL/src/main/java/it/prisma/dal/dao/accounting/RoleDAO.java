package it.prisma.dal.dao.accounting;

import it.prisma.dal.dao.DeprecatedQueryDSLGenericDAO;
import it.prisma.dal.entities.accounting.QRole;
import it.prisma.dal.entities.accounting.Role;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;

@Local(RoleDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class RoleDAO extends DeprecatedQueryDSLGenericDAO<Role, Long> {
	
	private QRole qRole;

	public RoleDAO() {
		super(Role.class);
	}

	@PostConstruct
	private void getProperQ() {
		qRole = QRole.role;
	}
	
	public Role findByName(final String name) throws EntityNotFoundException {
		Role role = super.prepareJPAQuery().from(qRole).where(qRole.name.eq(name)).uniqueResult(qRole);
		if (role == null)
			throw new EntityNotFoundException("Cannot find entity "
					+ Role.class.getCanonicalName()
					+ " with name <" + name + ">");
		else
			return role;
	}
	
}
