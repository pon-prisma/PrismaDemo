package it.prisma.dal.dao.accounting;

import it.prisma.dal.dao.DeprecatedQueryDSLGenericDAO;
import it.prisma.dal.entities.accounting.QUserAccountConfirmation;
import it.prisma.dal.entities.accounting.UserAccountConfirmation;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;

@Local(UserAccountConfirmationDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class UserAccountConfirmationDAO extends DeprecatedQueryDSLGenericDAO<UserAccountConfirmation, Long> {
	
	private QUserAccountConfirmation qUserAccountConfirmation;

	public UserAccountConfirmationDAO() {
		super(UserAccountConfirmation.class);
	}
	
	@PostConstruct
	private void getProperQ() {
		qUserAccountConfirmation = QUserAccountConfirmation.userAccountConfirmation;
	}
	
	public Collection<UserAccountConfirmation> findByUserAccountId(final Long userAccountId) {
		Collection<UserAccountConfirmation> userAccountConfirmations = super.prepareJPAQuery()
				.from(qUserAccountConfirmation)
				.where(qUserAccountConfirmation.userAccount.id.eq(userAccountId))
				.list(qUserAccountConfirmation);
		if (userAccountConfirmations == null || userAccountConfirmations.isEmpty())
			throw new EntityNotFoundException("Cannot find entities "
					+ UserAccountConfirmation.class.getCanonicalName()
					+ " with userAccountId <" + userAccountId + ">");
		else
			return userAccountConfirmations;
	}
	
	public UserAccountConfirmation findByToken(final String token) {
		UserAccountConfirmation userAccountConfirmation = super.prepareJPAQuery()
				.from(qUserAccountConfirmation)
				.where(qUserAccountConfirmation.token.eq(token)).uniqueResult(qUserAccountConfirmation);
		if (userAccountConfirmation == null)
			throw new EntityNotFoundException("Cannot find entities "
					+ UserAccountConfirmation.class.getCanonicalName()
					+ " with token <" + token + ">");
		else
			return userAccountConfirmation;			
	}
	
	public void invalidateToken(final String token) {
		UserAccountConfirmation userAccountConfirmation = findByToken(token);
		userAccountConfirmation.setValid(false);
		super.update(userAccountConfirmation);
	}
	
	public void invalidateAllTokenForAGivenUserAccount(final Long userAccountId) {
		Collection<UserAccountConfirmation> userAccountConfirmations = findByUserAccountId(userAccountId);
		for (UserAccountConfirmation userAccountConfirmation: userAccountConfirmations)
			invalidateToken(userAccountConfirmation.getToken());
	}

}
