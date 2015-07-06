package it.prisma.businesslayer.bizlib.accounting;

import it.prisma.businesslayer.bizlib.common.exceptions.UserAccountNotFoundException;
import it.prisma.businesslayer.exceptions.BadRequestException;
import it.prisma.businesslayer.exceptions.DataAccessException;
import it.prisma.businesslayer.exceptions.accounting.WorkgroupMembershipNotFoundException;
import it.prisma.businesslayer.exceptions.accounting.WorkgroupNotFoundException;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entitywrappers.accounting.WorkgroupMembership;

import java.util.Set;

import javax.persistence.EntityNotFoundException;

public interface WorkgroupManagement {

	public enum WorkgroupPrivilegeEnum {
		WG_GUEST, WG_USER, WG_ADMIN
	}

	// Create

	public Workgroup create(final String label, final String description,
			final Long createdByUserAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	// Read

	public Workgroup getWorkgroupById(final Long workgroupId)
			throws WorkgroupNotFoundException, BadRequestException,
			DataAccessException;

	public Set<UserAccount> getWorkgroupReferentsByWorkgroupId(final Long workgroupId)
			throws WorkgroupNotFoundException, EntityNotFoundException,
			DataAccessException;
	
	public Set<Workgroup> getReferencedWorkgroupsByUserAccountId(final Long userAccountId)
			throws WorkgroupNotFoundException, EntityNotFoundException,
			DataAccessException;
	
	public Set<Workgroup> getAllWorkgroups() throws WorkgroupNotFoundException,
			BadRequestException, DataAccessException;

	public WorkgroupMembership getMembershipByWorkgroupIdAndUserAccountId(
			final Long workgroupId, final Long userAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException;

	public Set<WorkgroupMembership> getAllMembershipsByWorkgroupId(
			final Long workgroupId) throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException;

	public Set<WorkgroupMembership> getAllMembershipsApplicationsByWorkgroupId(
			final Long workgroupId) throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException;

	public Set<WorkgroupMembership> getAllApprovedMembershipsByWorkgroupId(
			final Long workgroupId) throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException;

	public Set<WorkgroupMembership> getAllMemberships()
			throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException;

	public Set<WorkgroupMembership> getAllMembershipsByUserAccountId(
			final Long userAccountId)
			throws WorkgroupMembershipNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public Set<WorkgroupMembership> getAllMembershipApplicationsByUserAccountId(
			final Long userAccountId)
			throws WorkgroupMembershipNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public Set<WorkgroupMembership> getAllApprovedMembershipsByUserAccountId(
			final Long userAccountId)
			throws WorkgroupMembershipNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public Set<WorkgroupMembership> getAllMembershipsApplications()
			throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException;

	public Set<WorkgroupMembership> getAllApprovedMemberships()
			throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException;

	public boolean isWorkgroupAdmin(final Long workgroupId,
			final Long userAccountId) throws WorkgroupNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public boolean isWorkgroupUser(final Long workgroupId,
			final Long userAccountId) throws WorkgroupNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public boolean isWorkgroupGuest(final Long workgroupId,
			final Long userAccountId) throws WorkgroupNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public boolean isWorkgroupApplicant(final Long workgroupId,
			final Long userAccountId) throws WorkgroupNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public boolean isWorkgroupApprovedMember(final Long workgroupId,
			final Long userAccountId) throws WorkgroupNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	// Update

	public void applicateForMembership(final Long workgroupId,
			final Long applicantUserAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException;

	public void addMembership(final Long workgroupId,
			final Long applicantUserAccountId,
			final Long approvedByUserAccountId,
			final WorkgroupPrivilegeEnum workgroupPrivilege)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException;

	public void removeMembership(final Long workgroupId,
			final Long memberUserAccountId) throws WorkgroupNotFoundException,
			UserAccountNotFoundException, WorkgroupMembershipNotFoundException,
			BadRequestException, DataAccessException;

	public void changeWorkgroupPrivilege(final Long workgroupId,
			final Long memberUserAccountId, final Long approvedByUserAccountId,
			final WorkgroupPrivilegeEnum workgroupPrivilege)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException;

	public void unapproveMembership(final Long workgroupId,
			final Long applicantUserAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException;

	public void approveWorkgroup(final Long workgroupId,
			final Long approvedByUserAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException;

	public void unapproveWorkgroup(final Long workgroupId)
			throws WorkgroupNotFoundException, BadRequestException,
			DataAccessException;

	public WorkgroupPrivilegeEnum getWorkgroupPrivilegeEnumByName(
			final String privilegeName) throws BadRequestException;
	
}