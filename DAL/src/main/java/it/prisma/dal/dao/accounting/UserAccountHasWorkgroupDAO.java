package it.prisma.dal.dao.accounting;

import static it.prisma.dal.entities.accounting.QUserAccountHasWorkgroup.userAccountHasWorkgroup;
import static it.prisma.dal.entities.accounting.QAuthToken.authToken;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.accounting.AuthToken;
import it.prisma.dal.entities.accounting.UserAccountHasWorkgroup;
import it.prisma.dal.entities.accounting.UserAccountHasWorkgroupId;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityNotFoundException;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(UserAccountHasWorkgroupDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class UserAccountHasWorkgroupDAO extends 
	QueryDSLGenericDAO<UserAccountHasWorkgroup, UserAccountHasWorkgroupId> {

	public UserAccountHasWorkgroupDAO() {
		super(UserAccountHasWorkgroup.class);
	}

	@PostConstruct
	private void getProperQ() {
	}

	// We need to override the default implementation in order to fix an
	// Hibernate bug.
	@Override
	public UserAccountHasWorkgroup findById(final UserAccountHasWorkgroupId id)
			throws EntityNotFoundException {
		return findByWorkgroupIdAndUserAccountId(id.getWorkgroupId(),
				id.getUserAccountId());
	}

	// We need to override the default implementation in order to fix an
	// Hibernate bug.
	@Override
	public boolean exists(final UserAccountHasWorkgroupId id) {
		try {
			if (findByWorkgroupIdAndUserAccountId(id.getWorkgroupId(),
					id.getUserAccountId()) == null)
				return false;
			else
				return true;
		} catch (EntityNotFoundException e) {
			return false;
		}

	}
	
	public boolean exists(final Long workgroupId, final Long memberUserAccountId) {
		try {
			if (findByWorkgroupIdAndUserAccountId(workgroupId,
					memberUserAccountId) == null)
				return false;
			else
				return true;
		} catch (EntityNotFoundException e) {
			return false;
		}
	}

	// We need to override the default implementation in order to fix an
	// Hibernate bug.
	@Override
	public long count() {
		try {
			return super.findAll().size();
		} catch (EntityNotFoundException e) {
			return 0;
		}
	}

	public void approveMembership(
			final UserAccountHasWorkgroupId userAccountHasWorkgroupId) {
		UserAccountHasWorkgroup membership = super
				.findById(userAccountHasWorkgroupId);
		membership.setApproved(true);
		super.update(membership);
	}

	public void unapproveMembership(
			final UserAccountHasWorkgroupId userAccountHasWorkgroupId) {
		UserAccountHasWorkgroup membership = super
				.findById(userAccountHasWorkgroupId);
		membership.setApproved(false);
		super.update(membership);
	}

	public Collection<UserAccountHasWorkgroup> findByWorkgroupId(
			final Long workgroupId) {
		
		JPAQuery query = super.prepareJPAQuery(userAccountHasWorkgroup)//
				.where(userAccountHasWorkgroup.workgroup.id.eq(workgroupId));
		return getListResults(query, userAccountHasWorkgroup);
		
//		Collection<UserAccountHasWorkgroup> result = super.prepareJPAQuery()
//				.from(qUserAccountHasWorkgroup)
//				.where(qUserAccountHasWorkgroup.workgroup.id.eq(workgroupId))
//				.list(qUserAccountHasWorkgroup);
//		if (result == null || result.isEmpty())
//			throw new EntityNotFoundException();
//		else
//			return result;
	}

	public Collection<UserAccountHasWorkgroup> findByUserAccountId(
			final Long userAccountId) {
		
		JPAQuery query = super.prepareJPAQuery(userAccountHasWorkgroup)
				.where(userAccountHasWorkgroup.userAccountByUserAccountId.id
						.eq(userAccountId));
		
		return getListResults(query, userAccountHasWorkgroup);
		
	}

	public UserAccountHasWorkgroup findByWorkgroupIdAndUserAccountId(
			final Long workgroupId, final Long userAccountId) {
		
		JPAQuery query = super.prepareJPAQuery(userAccountHasWorkgroup)
				.where(	userAccountHasWorkgroup.workgroup.id.eq(workgroupId),
						userAccountHasWorkgroup.userAccountByUserAccountId.id.eq(userAccountId));
		return (UserAccountHasWorkgroup) query.singleResult(userAccountHasWorkgroup);
				
//		UserAccountHasWorkgroup result = super
//				.prepareJPAQuery()
//				.from(qUserAccountHasWorkgroup)
//				.where(qUserAccountHasWorkgroup.workgroup.id
//						.eq(workgroupId)
//						.and(qUserAccountHasWorkgroup.userAccountByUserAccountId.id
//								.eq(userAccountId)))
//				.uniqueResult(qUserAccountHasWorkgroup);
//		if (result == null)
//			throw new EntityNotFoundException();
//		else
//			return result;
	}

}
