package it.prisma.dal.dao.accounting;

import it.prisma.dal.dao.DeprecatedQueryDSLGenericDAO;
import it.prisma.dal.entities.accounting.IdentityProvider;
import it.prisma.dal.entities.accounting.QIdentityProvider;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;

@Local(IdentityProviderDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class IdentityProviderDAO extends DeprecatedQueryDSLGenericDAO<IdentityProvider, Long> {
	
	private QIdentityProvider qIdentityProvider;

	public IdentityProviderDAO() {
		super(IdentityProvider.class);
	}

	@PostConstruct
	private void getProperQ() {
		qIdentityProvider = QIdentityProvider.identityProvider;
	}
	
	public IdentityProvider findByEntityId(final String entityId) throws EntityNotFoundException {
		IdentityProvider identityprovider = super.prepareJPAQuery()
				.from(qIdentityProvider)
				.where(qIdentityProvider.entityId.eq(entityId))
				.uniqueResult(qIdentityProvider);
		if (identityprovider == null)
			throw new EntityNotFoundException("Cannot find entity "
					+ IdentityProvider.class.getCanonicalName()
					+ " with EntityId <" + entityId + ">");
		else
			return identityprovider;
	}
	
}
