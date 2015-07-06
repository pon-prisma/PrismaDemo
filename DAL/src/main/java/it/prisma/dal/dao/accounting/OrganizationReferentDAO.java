package it.prisma.dal.dao.accounting;

import it.prisma.dal.dao.DeprecatedQueryDSLGenericDAO;
import it.prisma.dal.entities.organization.OrganizationReferent;
import it.prisma.dal.entities.organization.QOrganizationReferent;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;

@Local(OrganizationReferentDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class OrganizationReferentDAO extends DeprecatedQueryDSLGenericDAO<OrganizationReferent, Long> {
	
	private QOrganizationReferent qOrganizationReferent;

	public OrganizationReferentDAO() {
		super(OrganizationReferent.class);
	}
	
	@PostConstruct
	private void getProperQ() {
		qOrganizationReferent = QOrganizationReferent.organizationReferent;
	}
	
	public Collection<OrganizationReferent> findByLastName(final String lastName)
			throws EntityNotFoundException {
		List<OrganizationReferent> organizationReferents = super.prepareJPAQuery()
			.from(qOrganizationReferent)
			.where(qOrganizationReferent.lastName.eq(lastName))
			.list(qOrganizationReferent);
		if (organizationReferents == null || organizationReferents.isEmpty())
			throw new EntityNotFoundException("Cannot find entities "
					+ OrganizationReferent.class.getCanonicalName()
					+ " with lastName <" + lastName + ">");
		else
			return organizationReferents;
	}
	
}
