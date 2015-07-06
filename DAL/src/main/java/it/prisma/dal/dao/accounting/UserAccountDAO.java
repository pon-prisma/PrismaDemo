package it.prisma.dal.dao.accounting;

import static it.prisma.dal.entities.accounting.QUserAccount.userAccount;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.accounting.QUserAccount;
import it.prisma.dal.entities.accounting.Role;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(UserAccountDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class UserAccountDAO extends QueryDSLGenericDAO<UserAccount, Long> {

	private QUserAccount qUserAccount;
	
	public UserAccountDAO() {
		super(UserAccount.class);
	}
	
	@PostConstruct
	private void getProperQ() {
		qUserAccount = QUserAccount.userAccount;
	}

	public UserAccount findByCredentialsOnIdentityProvider(final Long identityProviderId, 
			final String nameIdOnIdentityProvider) throws EntityNotFoundException {
		
		JPAQuery query = super.prepareJPAQuery(userAccount)
				.where(	userAccount.identityProvider.id.eq(identityProviderId),
						qUserAccount.nameIdOnIdentityProvider.eq(nameIdOnIdentityProvider));
		
		return query.uniqueResult(userAccount);
	
	}
	
	public Collection<Workgroup> getWorkgroupsForWhichIsReferentByUserAccountId(final Long userAccountId)
			throws EntityNotFoundException
	{
		JPAQuery query = super.prepareJPAQuery(userAccount)
				.where(	userAccount.id.eq(userAccountId));
		UserAccount user = query.singleResult(userAccount);
		if (user==null)
			throw new EntityNotFoundException();
		return user.getWorkgroupsForWhichIsReferent();
		
	}
	
	
	public boolean existsForGivenCredentialsOnIdentityProvider(final Long identityProviderId, 
			final String nameIdOnIdentityProvider) {
		try {
			UserAccount  account = findByCredentialsOnIdentityProvider(identityProviderId, nameIdOnIdentityProvider);
			return (account != null);
		} catch(EntityNotFoundException e) {
			return false;
		}
	}
	
	public void enable(final Long userAccountId) {
		UserAccount userAccount = super.findById(userAccountId);
		if (!userAccount.isEnabled()) {
			userAccount.setEnabled(true);
			userAccount.setEnabledAt(new Date());
			super.update(userAccount);
		}
	}
	
	public void disable(final Long userAccountId) {
		UserAccount userAccount = super.findById(userAccountId);
		if (userAccount.isEnabled()) {
			userAccount.setEnabled(false);
			userAccount.setEnabledAt(null);
			super.update(userAccount);
		}
	}
	
	public void suspend(final Long userAccountId) {
		UserAccount userAccount = super.findById(userAccountId);
		if (!userAccount.isSuspended()) {
			userAccount.setSuspended(true);
			userAccount.setSuspendedAt(new Date());
			super.update(userAccount);
		}
	}
	
	public void unsuspend(final Long userAccountId) {
		UserAccount userAccount = super.findById(userAccountId);
		if (userAccount.isSuspended()) {
			userAccount.setSuspended(false);
			userAccount.setSuspendedAt(null);
			super.update(userAccount);
		}
	}
	
	public void updateEmail(final Long userAccountId, final String email) {
		UserAccount userAccount = super.findById(userAccountId);
		if (userAccount.getEmail().compareTo(email) != 0) {
			userAccount.setEmail(email);
			super.update(userAccount);
		}
	}
	
	public void addRole(final Long userAccountId, final Role role) {
		UserAccount userAccount = super.findById(userAccountId);
		if (!hasRole(userAccount.getRoles(), role)) {
			Set<Role> roles = userAccount.getRoles();
			roles.add(role);
			userAccount.setRoles(roles);
			super.update(userAccount);
		}
	}

	public void removeRole(final Long userAccountId, final Role role)
			throws EntityNotFoundException {
		UserAccount userAccount = super.findById(userAccountId);
		if (hasRole(userAccount.getRoles(), role)) {
			Set<Role> roles = userAccount.getRoles();
			for (Role r : roles)
				if (r.getId().equals(role.getId()))
					roles.remove(r);
			userAccount.setRoles(roles);
			super.update(userAccount);
		} else
			throw new EntityNotFoundException("Cannot find entity "
					+ UserAccount.class.getCanonicalName() + " with role <"
					+ role.getName() + ">");
	}
	
	public void removeAllRoles(final Long userAccountId) {
		UserAccount userAccount = super.findById(userAccountId);
		if (!userAccount.getRoles().isEmpty()) {
			userAccount.getRoles().clear();
			super.update(userAccount);
		}
	}
	
	// Helper
	protected boolean hasRole(Set<Role> roles, Role role) {
		for (Role r : roles)
			if (r.getId().equals(role.getId()))
				return true;
		return false;
	}

}