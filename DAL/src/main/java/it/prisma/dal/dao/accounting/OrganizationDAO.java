package it.prisma.dal.dao.accounting;

import it.prisma.dal.dao.DeprecatedQueryDSLGenericDAO;
import it.prisma.dal.entities.organization.Organization;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local(OrganizationDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class OrganizationDAO extends DeprecatedQueryDSLGenericDAO<Organization, Long> {
	
	public OrganizationDAO() {
		super(Organization.class);
	}

}
