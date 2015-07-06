package it.prisma.dal.dao.accounting;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.accounting.QUserProfile;
import it.prisma.dal.entities.accounting.UserProfile;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;

@Local(UserProfileDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class UserProfileDAO extends QueryDSLGenericDAO<UserProfile, Long> {

	private QUserProfile qUserProfile;
	
	public UserProfileDAO() {
		super(UserProfile.class);
	}
	
	@PostConstruct
	private void getProperQ() {
		qUserProfile = QUserProfile.userProfile;
	}
	
	public UserProfile findByUserAccountId(Long userAccountId) {
		UserProfile userProfile = super.prepareJPAQuery()
				.from(qUserProfile)
				.where(qUserProfile.userAccount.id.eq(userAccountId)).uniqueResult(qUserProfile);
		if (userProfile == null)
			throw new EntityNotFoundException("Cannot find entity "
					+ super.getEntityClass().getCanonicalName()
					+ " with UserAccount_userAccountId <" + userAccountId + ">");
		else
			return userProfile;
	}
	
	public boolean userAccountHasProfile(Long userAccountId) {
		boolean hasProfile;
		try {
			findByUserAccountId(userAccountId);
			hasProfile = true;
		} catch (EntityNotFoundException e) {
			hasProfile = false;
		}
		return hasProfile;
	}
	
}
