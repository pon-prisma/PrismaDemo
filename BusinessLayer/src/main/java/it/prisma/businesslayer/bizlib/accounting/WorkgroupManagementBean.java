package it.prisma.businesslayer.bizlib.accounting;

import it.prisma.businesslayer.bizlib.common.exceptions.UserAccountNotFoundException;
import it.prisma.businesslayer.exceptions.BadRequestException;
import it.prisma.businesslayer.exceptions.DataAccessException;
import it.prisma.businesslayer.exceptions.accounting.WorkgroupMembershipNotFoundException;
import it.prisma.businesslayer.exceptions.accounting.WorkgroupNotFoundException;
import it.prisma.dal.dao.accounting.UserAccountDAO;
import it.prisma.dal.dao.accounting.UserAccountHasWorkgroupDAO;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.accounting.WorkgroupPrivilegeDAO;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.UserAccountHasWorkgroup;
import it.prisma.dal.entities.accounting.UserAccountHasWorkgroupId;
import it.prisma.dal.entities.accounting.UserProfile;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.accounting.WorkgroupPrivilege;
import it.prisma.dal.entitywrappers.accounting.WorkgroupMembership;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

public class WorkgroupManagementBean implements WorkgroupManagement {

	@Inject
	private UserManagement userManagement;

	// DAOs
	@Inject private UserAccountDAO userAccountDAO;
	@Inject	private WorkgroupDAO workgroupDAO;
	@Inject	private WorkgroupPrivilegeDAO workgroupPrivilegeDAO;
	@Inject	private UserAccountHasWorkgroupDAO userAccountHasWorkgroupDAO;

	// Create

	@Override
	public Workgroup create(String label, String description,
			Long createdByUserAccountId) throws UserAccountNotFoundException,
			BadRequestException, DataAccessException {

		UserProfile user = userManagement.getUserAccountByUserAccountId(createdByUserAccountId)
				.getUserProfile();
		boolean creatorIsPrismaAdmin = userManagement.isPrismaAdmin(user
				.getUserAccount().getId());
		try {
			try {
				Workgroup workgroup = new Workgroup();
				workgroup.setLabel(label);
				workgroup.setDescription(description);
				workgroup.setOrganization(user.getUserAccount()
						.getIdentityProvider().getOrganization());
				workgroup.setUserAccountByCreatedByUserAccountId(user
						.getUserAccount());
				if (creatorIsPrismaAdmin) {
					workgroup.setApproved(true);
					workgroup.setUserAccountByApprovedByUserAccountId(user
							.getUserAccount());
				} else
					workgroup.setApproved(false);
				Workgroup createdWorkgroup = workgroupDAO.create(workgroup);

				// Set membership
				if (creatorIsPrismaAdmin)
					addMembership(createdWorkgroup.getId(),
							createdByUserAccountId, createdByUserAccountId,
							WorkgroupPrivilegeEnum.WG_ADMIN);
				else
					applicateForMembership(createdWorkgroup.getId(),
							createdByUserAccountId);

				return this.getWorkgroupById(createdWorkgroup.getId());
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	// Read

	@Override
	public Workgroup getWorkgroupById(Long workgroupId)
			throws WorkgroupNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				return workgroupDAO.findById(workgroupId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}
	
	@Override
	public Set<UserAccount> getWorkgroupReferentsByWorkgroupId(final Long workgroupId)
			throws WorkgroupNotFoundException, EntityNotFoundException,
			DataAccessException
	{
		
		Set<UserAccount> userAccounts = new HashSet<UserAccount>();
		try {
			try {
				for (UserAccount userAccount : workgroupDAO
						.getReferentsByWorkgroupId(workgroupId)) {
					userAccounts.add(userAccount);
				}
				if (userAccounts.isEmpty())
					throw new EntityNotFoundException();
				else
					return userAccounts;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
		
	}
	
	 @Override
	public Set<Workgroup> getReferencedWorkgroupsByUserAccountId(final Long userAccountId)
			throws WorkgroupNotFoundException, EntityNotFoundException,
			DataAccessException
	{
		Set<Workgroup> referencedWorkgroups = new HashSet<Workgroup>();
		try {
			try {
				for (Workgroup workgroup : userAccountDAO
						.getWorkgroupsForWhichIsReferentByUserAccountId(userAccountId)) {
					referencedWorkgroups.add(workgroup);
				}
				if (referencedWorkgroups.isEmpty())
					throw new EntityNotFoundException();
				else
					return referencedWorkgroups;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}
	

	@Override
	public Set<Workgroup> getAllWorkgroups() throws WorkgroupNotFoundException,
			BadRequestException, DataAccessException {
		Set<Workgroup> workgroups = new HashSet<Workgroup>();
		try {
			try {
				for (Workgroup workgroup : workgroupDAO
						.findAll()) {
					workgroups.add(workgroup);
				}
				if (workgroups.isEmpty())
					throw new EntityNotFoundException();
				else
					return workgroups;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public WorkgroupMembership getMembershipByWorkgroupIdAndUserAccountId(
			Long workgroupId, Long userAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException {
		if (!userAccountDAO.exists(userAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		try {
			try {
				return new WorkgroupMembership(
						userAccountHasWorkgroupDAO
								.findByWorkgroupIdAndUserAccountId(workgroupId,
										userAccountId));
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Set<WorkgroupMembership> getAllMembershipsByWorkgroupId(
			Long workgroupId) throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException {
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		try {
			try {
				Collection<UserAccountHasWorkgroup> rawMemberships = userAccountHasWorkgroupDAO
						.findByWorkgroupId(workgroupId);
				Set<WorkgroupMembership> memberships = new HashSet<WorkgroupMembership>();
				for (UserAccountHasWorkgroup rawMembership : rawMemberships) {
					memberships.add(new WorkgroupMembership(rawMembership));
				}
				if (memberships.isEmpty())
					throw new EntityNotFoundException();
				else
					return memberships;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Set<WorkgroupMembership> getAllMembershipsApplicationsByWorkgroupId(
			Long workgroupId) throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException {
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		try {
			try {
				Collection<UserAccountHasWorkgroup> rawMemberships = userAccountHasWorkgroupDAO
						.findByWorkgroupId(workgroupId);
				Set<WorkgroupMembership> memberships = new HashSet<WorkgroupMembership>();
				for (UserAccountHasWorkgroup rawMembership : rawMemberships) {
					if (!rawMembership.isApproved())
						memberships.add(new WorkgroupMembership(rawMembership));
				}
				if (memberships.isEmpty())
					throw new EntityNotFoundException();
				else
					return memberships;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Set<WorkgroupMembership> getAllApprovedMembershipsByWorkgroupId(
			Long workgroupId) throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException {
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		try {
			try {
				Collection<UserAccountHasWorkgroup> rawMemberships = userAccountHasWorkgroupDAO
						.findByWorkgroupId(workgroupId);
				Set<WorkgroupMembership> memberships = new HashSet<WorkgroupMembership>();
				for (UserAccountHasWorkgroup rawMembership : rawMemberships) {
					if (rawMembership.isApproved())
						memberships.add(new WorkgroupMembership(rawMembership));
				}
				if (memberships.isEmpty())
					throw new EntityNotFoundException();
				else
					return memberships;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Set<WorkgroupMembership> getAllMemberships()
			throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				Collection<UserAccountHasWorkgroup> rawMemberships = userAccountHasWorkgroupDAO
						.findAll();
				Set<WorkgroupMembership> memberships = new HashSet<WorkgroupMembership>();
				for (UserAccountHasWorkgroup rawMembership : rawMemberships) {
					memberships.add(new WorkgroupMembership(rawMembership));
				}
				if (memberships.isEmpty())
					throw new EntityNotFoundException();
				else
					return memberships;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Set<WorkgroupMembership> getAllMembershipsByUserAccountId(
			final Long userAccountId)
			throws WorkgroupMembershipNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		if (!userAccountDAO.exists(userAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		try {
			try {
								
				Collection<UserAccountHasWorkgroup> rawMemberships = userAccountHasWorkgroupDAO
						.findByUserAccountId(userAccountId);
								
				Set<WorkgroupMembership> memberships = new HashSet<WorkgroupMembership>();
				for (UserAccountHasWorkgroup rawMembership : rawMemberships) {
					memberships.add(new WorkgroupMembership(rawMembership));
				}
				if (memberships.isEmpty())
					throw new EntityNotFoundException();
				else
					return memberships;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Set<WorkgroupMembership> getAllMembershipApplicationsByUserAccountId(
			final Long userAccountId)
			throws WorkgroupMembershipNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		if (!userAccountDAO.exists(userAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		try {
			try {
				Collection<UserAccountHasWorkgroup> rawMemberships = userAccountHasWorkgroupDAO
						.findByUserAccountId(userAccountId);
				Set<WorkgroupMembership> memberships = new HashSet<WorkgroupMembership>();
				for (UserAccountHasWorkgroup rawMembership : rawMemberships) {
					if (!rawMembership.isApproved())
						memberships.add(new WorkgroupMembership(rawMembership));
				}
				if (memberships.isEmpty())
					throw new EntityNotFoundException();
				else
					return memberships;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Set<WorkgroupMembership> getAllApprovedMembershipsByUserAccountId(
			Long userAccountId) throws WorkgroupMembershipNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		if (!userAccountDAO.exists(userAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		try {
			try {
				Collection<UserAccountHasWorkgroup> rawMemberships = userAccountHasWorkgroupDAO
						.findByUserAccountId(userAccountId);
				Set<WorkgroupMembership> memberships = new HashSet<WorkgroupMembership>();
				for (UserAccountHasWorkgroup rawMembership : rawMemberships) {
					if (rawMembership.isApproved())
						memberships.add(new WorkgroupMembership(rawMembership));
				}
				if (memberships.isEmpty())
					throw new EntityNotFoundException();
				else
					return memberships;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Set<WorkgroupMembership> getAllMembershipsApplications()
			throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException {
		try {	
			try {
				Collection<UserAccountHasWorkgroup> rawMemberships = userAccountHasWorkgroupDAO
						.findAll();
				Set<WorkgroupMembership> memberships = new HashSet<WorkgroupMembership>();	
				for (UserAccountHasWorkgroup rawMembership : rawMemberships) {
					if (!rawMembership.isApproved())
						memberships.add(new WorkgroupMembership(rawMembership));
				}
				if (memberships.isEmpty())
					throw new EntityNotFoundException();
				else
					return memberships;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Set<WorkgroupMembership> getAllApprovedMemberships()
			throws WorkgroupNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				Collection<UserAccountHasWorkgroup> rawMemberships = userAccountHasWorkgroupDAO
						.findAll();
				Set<WorkgroupMembership> memberships = new HashSet<WorkgroupMembership>();		
				for (UserAccountHasWorkgroup rawMembership : rawMemberships) {
					if (rawMembership.isApproved())
						memberships.add(new WorkgroupMembership(rawMembership));
				}
				if (memberships.isEmpty())
					throw new EntityNotFoundException();
				else
					return memberships;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public boolean isWorkgroupAdmin(Long workgroupId, Long userAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException {
		if (!userAccountDAO.exists(userAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		if (!userAccountHasWorkgroupDAO.exists(workgroupId, userAccountId))
			return false;
		try {
			try {
				UserAccountHasWorkgroup membership = userAccountHasWorkgroupDAO.findByWorkgroupIdAndUserAccountId(
						workgroupId, userAccountId);
				return membership.isApproved()
						&& getWorkgroupPrivilegeEnum(
								membership.getWorkgroupPrivilege()).equals(
								WorkgroupPrivilegeEnum.WG_ADMIN) ? true : false;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			return false;
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public boolean isWorkgroupUser(Long workgroupId, Long userAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException {
		if (!userAccountDAO.exists(userAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		if (!userAccountHasWorkgroupDAO.exists(workgroupId, userAccountId))
			return false;
		try {
			try {
				UserAccountHasWorkgroup membership = userAccountHasWorkgroupDAO.findByWorkgroupIdAndUserAccountId(
						workgroupId, userAccountId);
				return membership.isApproved()
						&& getWorkgroupPrivilegeEnum(
								membership.getWorkgroupPrivilege()).equals(
								WorkgroupPrivilegeEnum.WG_USER) ? true : false;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			return false;
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public boolean isWorkgroupGuest(Long workgroupId, Long userAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException {
		if (!userAccountDAO.exists(userAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		if (!userAccountHasWorkgroupDAO.exists(workgroupId, userAccountId))
			return false;
		try {
			try {
				UserAccountHasWorkgroup membership = userAccountHasWorkgroupDAO.findByWorkgroupIdAndUserAccountId(
						workgroupId, userAccountId);
				return membership.isApproved()
						&& getWorkgroupPrivilegeEnum(
								membership.getWorkgroupPrivilege()).equals(
								WorkgroupPrivilegeEnum.WG_GUEST) ? true : false;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			return false;
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public boolean isWorkgroupApplicant(Long workgroupId, Long userAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException {
		if (!userAccountDAO.exists(userAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		if (!userAccountHasWorkgroupDAO.exists(workgroupId, userAccountId))
			return false;
		try {
			try {
				UserAccountHasWorkgroup membership = userAccountHasWorkgroupDAO.findByWorkgroupIdAndUserAccountId(
						workgroupId, userAccountId);
				if (membership != null)
					if (!membership.isApproved())
						return true;
				return false;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			return false;
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public boolean isWorkgroupApprovedMember(Long workgroupId,
			Long userAccountId) throws WorkgroupNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		if (!userAccountDAO.exists(userAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		if (!userAccountHasWorkgroupDAO.exists(workgroupId, userAccountId))
			return false;
		try {
			try {
				UserAccountHasWorkgroup membership = userAccountHasWorkgroupDAO.findByWorkgroupIdAndUserAccountId(
						workgroupId, userAccountId);
				if (membership != null)
					if (membership.isApproved())
						return true;
				return false;
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			return false;
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	// Update

	@Override
	public void addMembership(Long workgroupId, Long applicantUserAccountId,
			Long approvedByUserAccountId,
			WorkgroupPrivilegeEnum workgroupPrivilege)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException {
		if (userAccountHasWorkgroupDAO.exists(workgroupId, applicantUserAccountId))
			this.changeWorkgroupPrivilege(workgroupId, applicantUserAccountId, approvedByUserAccountId, workgroupPrivilege);
		else {
			Workgroup workgroup = getWorkgroupById(workgroupId);
			UserAccount applicantUserAccount = userManagement.getUserAccountByUserAccountId(
					applicantUserAccountId);
			boolean applicantIsPrismaAdmin = userManagement
					.isPrismaAdmin(applicantUserAccountId);
			boolean approverIsWorkgroupAdmin = false;
			boolean approverIsPrismaAdmin = false;
			UserAccount approvedByUserAccount = null;
			if (!applicantIsPrismaAdmin && approvedByUserAccountId != null) {
				if (workgroupPrivilege == null)
					throw new BadRequestException();
				approvedByUserAccount = userManagement.getUserAccountByUserAccountId(
						approvedByUserAccountId);
				if (approvedByUserAccount != null) {
					approverIsPrismaAdmin = userManagement
							.isPrismaAdmin(approvedByUserAccountId);
					if (isWorkgroupAdmin(workgroupId, approvedByUserAccountId))
						approverIsWorkgroupAdmin = true;
				} else
					throw new BadRequestException();
			}
			try {
				try {
					UserAccountHasWorkgroupId userAccountHasWorkgroupId = new UserAccountHasWorkgroupId(
							applicantUserAccountId, workgroupId);
					UserAccountHasWorkgroup userAccountHasWorkgroup = new UserAccountHasWorkgroup();
					userAccountHasWorkgroup.setId(userAccountHasWorkgroupId);
					userAccountHasWorkgroup.setWorkgroup(workgroup);
					userAccountHasWorkgroup
							.setUserAccountByUserAccountId(applicantUserAccount);
					// Self-approved membership
					if (applicantIsPrismaAdmin) {
						userAccountHasWorkgroup
								.setWorkgroupPrivilege(getWorkgroupPrivilegeFromEnum(WorkgroupPrivilegeEnum.WG_ADMIN));
						userAccountHasWorkgroup.setApproved(true);
						userAccountHasWorkgroup
								.setUserAccountByApprovedByUserAccountId(applicantUserAccount);
					}
					// Approved by workgroup Admin or Prisma Admin
					else if (approverIsPrismaAdmin || approverIsWorkgroupAdmin) {
						userAccountHasWorkgroup
								.setWorkgroupPrivilege(getWorkgroupPrivilegeFromEnum(workgroupPrivilege));
						userAccountHasWorkgroup.setApproved(true);
						userAccountHasWorkgroup
								.setUserAccountByApprovedByUserAccountId(approvedByUserAccount);
					} else { // Do not approve, only a membership request
						// Requested but not usable
						userAccountHasWorkgroup
								.setUserAccountByApprovedByUserAccountId(null);
						userAccountHasWorkgroup.setWorkgroupPrivilege(null);
						userAccountHasWorkgroup.setApproved(false);
					}
					userAccountHasWorkgroupDAO.create(userAccountHasWorkgroup);
				} catch (EJBException ejbe) {
					throw ejbe.getCausedByException();
				}
			} catch (Exception e) {
				throw new DataAccessException();
			}
		}
	}

	@Override
	public void applicateForMembership(Long workgroupId,
			Long applicantUserAccountId) throws WorkgroupNotFoundException,
			UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		addMembership(workgroupId, applicantUserAccountId, null, null);
	}

	@Override
	public void removeMembership(Long workgroupId, Long memberUserAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException {
		if (!userAccountDAO.exists(memberUserAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		try {
			try {
				userAccountHasWorkgroupDAO.delete(userAccountHasWorkgroupDAO
						.findByWorkgroupIdAndUserAccountId(workgroupId,
								memberUserAccountId).getId());
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void unapproveMembership(final Long workgroupId,
			final Long applicantUserAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException {
		if (!userAccountDAO.exists(applicantUserAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		try {
			try {
				UserAccountHasWorkgroup userAccountHasWorkgroup = userAccountHasWorkgroupDAO
						.findByWorkgroupIdAndUserAccountId(workgroupId,
								applicantUserAccountId);
				userAccountHasWorkgroup.setApproved(false);
				userAccountHasWorkgroup.setWorkgroupPrivilege(null);
				userAccountHasWorkgroupDAO.update(userAccountHasWorkgroup);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupMembershipNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void approveWorkgroup(final Long workgroupId,
			final Long approvedByUserAccountId)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			BadRequestException, DataAccessException {
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		if (!userManagement.isPrismaAdmin(approvedByUserAccountId))
			throw new BadRequestException();
		try {
			try {
				Workgroup workgroup = workgroupDAO.findById(workgroupId);
				workgroup.setApproved(true);
				workgroup
						.setUserAccountByApprovedByUserAccountId(userAccountDAO
								.findById(approvedByUserAccountId));
				WorkgroupMembership membership = getMembershipByWorkgroupIdAndUserAccountId(
						workgroupId, workgroup
								.getUserAccountByCreatedByUserAccountId()
								.getId());
				UserAccountHasWorkgroup userAccountHasWorkgroup = userAccountHasWorkgroupDAO
						.findByWorkgroupIdAndUserAccountId(
								membership.getWorkgroupId(),
								membership.getMemberUserAccountId());
				userAccountHasWorkgroup
						.setWorkgroupPrivilege(getWorkgroupPrivilegeFromEnum(WorkgroupPrivilegeEnum.WG_ADMIN));
				userAccountHasWorkgroup.setApproved(true);
				userAccountHasWorkgroup
						.setUserAccountByApprovedByUserAccountId(userAccountDAO
								.findById(approvedByUserAccountId));
				userAccountHasWorkgroupDAO.update(userAccountHasWorkgroup);
				workgroupDAO.update(workgroup);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void unapproveWorkgroup(final Long workgroupId)
			throws WorkgroupNotFoundException, BadRequestException,
			DataAccessException {
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		try {
			try {
				Workgroup workgroup = workgroupDAO.findById(workgroupId);
				workgroup.setApproved(true);
				workgroup.setUserAccountByApprovedByUserAccountId(null);
				workgroupDAO.update(workgroup);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new WorkgroupNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void changeWorkgroupPrivilege(Long workgroupId,
			Long memberUserAccountId, Long approvedByUserAccountId,
			WorkgroupPrivilegeEnum workgroupPrivilege)
			throws WorkgroupNotFoundException, UserAccountNotFoundException,
			WorkgroupMembershipNotFoundException, BadRequestException,
			DataAccessException {
		if (!workgroupDAO.exists(workgroupId))
			throw new WorkgroupNotFoundException();
		if (!userAccountDAO.exists(approvedByUserAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		if (!userAccountDAO.exists(memberUserAccountId))
			throw new UserAccountNotFoundException("User account not found.");
		UserAccountHasWorkgroup membership = null;
		if (userAccountHasWorkgroupDAO
				.exists(workgroupId, memberUserAccountId))
			membership = userAccountHasWorkgroupDAO
					.findByWorkgroupIdAndUserAccountId(workgroupId,
							memberUserAccountId);
		else
			throw new WorkgroupMembershipNotFoundException();
		// Note that privilege downgrade is not allowed for Prisma Admin and
		// Workgroup creator
		if (userManagement.isPrismaAdmin(memberUserAccountId)
				&& !workgroupPrivilege.equals(WorkgroupPrivilegeEnum.WG_ADMIN))
			throw new BadRequestException();
		else if (workgroupDAO.findById(workgroupId)
				.getUserAccountByCreatedByUserAccountId().getId() == userAccountDAO
				.findById(memberUserAccountId).getId()
				&& !workgroupPrivilege.equals(WorkgroupPrivilegeEnum.WG_ADMIN))
			throw new BadRequestException();
		boolean changeable = false;
		if (workgroupPrivilege == null) {
			if (membership.getWorkgroupPrivilege() != null) {
				membership.setUserAccountByApprovedByUserAccountId(null);
				membership.setWorkgroupPrivilege(null);
				membership.setApproved(false);
				changeable = true;
			} // elsewhere, the privilege is already set, thus it does nothing.
		}
		if (workgroupPrivilege != null
				&& (membership.getWorkgroupPrivilege() == null || !getWorkgroupPrivilegeEnum(
						membership.getWorkgroupPrivilege()).equals(
						workgroupPrivilege))) {
			if (userManagement.isPrismaAdmin(approvedByUserAccountId)
					|| getWorkgroupPrivilegeEnum(
							userAccountHasWorkgroupDAO
									.findByWorkgroupIdAndUserAccountId(
											workgroupId,
											approvedByUserAccountId)
									.getWorkgroupPrivilege()).equals(
							WorkgroupPrivilegeEnum.WG_ADMIN)) {
				// Valid approved
				membership.setApproved(true);
				membership
						.setUserAccountByApprovedByUserAccountId(userAccountDAO
								.findById(approvedByUserAccountId));
				membership
						.setWorkgroupPrivilege(getWorkgroupPrivilegeFromEnum(workgroupPrivilege));
				changeable = true;
			} else
				throw new BadRequestException();
		} // elsewhere, the privilege is already set, thus it does nothing.
		if (changeable) {
			try {
				try {
					userAccountHasWorkgroupDAO.update(membership);
				} catch (EJBException ejbe) {
					throw ejbe.getCausedByException();
				}
			} catch (EntityNotFoundException e) {
				throw new EntityNotFoundException();
			} catch (Exception e) {
				throw new DataAccessException();
			}
		}
	}
	
	// Helper

	private WorkgroupPrivilege getWorkgroupPrivilegeFromEnum(
			WorkgroupPrivilegeEnum privilege) {
		try {
			try {
				switch (privilege) {
				case WG_GUEST:
					return workgroupPrivilegeDAO.findByName("WG_GUEST");
				case WG_USER:
					return workgroupPrivilegeDAO.findByName("WG_USER");
				case WG_ADMIN:
					return workgroupPrivilegeDAO.findByName("WG_ADMIN");
				default:
					throw new BadRequestException();
				}
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new BadRequestException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	private WorkgroupPrivilegeEnum getWorkgroupPrivilegeEnum(
			WorkgroupPrivilege privilege) {
		if (privilege == null)
			throw new BadRequestException();
		if (privilege.getName().compareTo("WG_GUEST") == 0)
			return WorkgroupPrivilegeEnum.WG_GUEST;
		else if (privilege.getName().compareTo("WG_USER") == 0)
			return WorkgroupPrivilegeEnum.WG_USER;
		else if (privilege.getName().compareTo("WG_ADMIN") == 0)
			return WorkgroupPrivilegeEnum.WG_ADMIN;
		else
			throw new BadRequestException();
	}
	
	public WorkgroupPrivilegeEnum getWorkgroupPrivilegeEnumByName(
			final String privilegeName) {
		if (privilegeName == null)
			throw new BadRequestException();
		if (privilegeName.compareTo("WG_GUEST") == 0)
			return WorkgroupPrivilegeEnum.WG_GUEST;
		else if (privilegeName.compareTo("WG_USER") == 0)
			return WorkgroupPrivilegeEnum.WG_USER;
		else if (privilegeName.compareTo("WG_ADMIN") == 0)
			return WorkgroupPrivilegeEnum.WG_ADMIN;
		else
			throw new BadRequestException();
	}

}
