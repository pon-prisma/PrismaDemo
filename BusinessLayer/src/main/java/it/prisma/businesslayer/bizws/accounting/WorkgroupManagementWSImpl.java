package it.prisma.businesslayer.bizws.accounting;

import it.prisma.businesslayer.bizlib.accounting.WorkgroupManagement;
import it.prisma.businesslayer.bizlib.accounting.WorkgroupManagement.WorkgroupPrivilegeEnum;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.exceptions.accounting.WorkgroupNotFoundException;
import it.prisma.businesslayer.utils.mappinghelpers.accounting.UserAccountMappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.accounting.WorkgroupMappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.accounting.WorkgroupMembershipMappingHelper;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entitywrappers.accounting.WorkgroupMembership;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupApprovationRequest;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupCreationRequest;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupMembershipRequest;

import java.util.Collection;

import javax.inject.Inject;

public class WorkgroupManagementWSImpl implements WorkgroupManagementWS {

	@Inject private WorkgroupManagement workgroupManagement;
	@Inject private WorkgroupMappingHelper workgroupMappingHelper;
	@Inject private UserAccountMappingHelper userAccountMappingHelper;
	@Inject private WorkgroupMembershipMappingHelper workgroupMembershipMappingHelper;
	
	
	@Inject
	private InfrastructureManagerBean infrastructureBean;

	
	@Override
	public WorkgroupRepresentation create(WorkgroupCreationRequest workgroupCreationrequest) {
		
		try {
			return workgroupMappingHelper.getDSL(workgroupManagement.create(
						workgroupCreationrequest.getLabel(),
						workgroupCreationrequest.getDescription(),
						workgroupCreationrequest
								.getCreatedByUserAccountId()));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public WorkgroupRepresentation getWorkgroupById(Long workgroupId) {
		
		try {
			return workgroupMappingHelper.getDSL(workgroupManagement
					.getWorkgroupById(workgroupId));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Collection<WorkgroupRepresentation> getAllWorkgroups() {
		
		try {
			
			Collection<WorkgroupRepresentation> workgroups = workgroupMappingHelper
					.getDSL(workgroupManagement.getAllWorkgroups());
			return workgroups;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	@Override
	public WorkgroupMembershipRepresentation getMembershipByWorkgroupIdAndUserAccountId(
			Long workgroupId, Long userAccountId) {
		
		try {
			return workgroupMembershipMappingHelper.getDSL(workgroupManagement
					.getMembershipByWorkgroupIdAndUserAccountId(
							workgroupId, userAccountId));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	@Override
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipsByWorkgroupId(Long workgroupId) {
		
		try {
			
			Collection<WorkgroupMembership> rawMemberships = workgroupManagement.getAllMembershipsByWorkgroupId(workgroupId);
			Collection<WorkgroupMembershipRepresentation> memberships = workgroupMembershipMappingHelper
					.getDSL(rawMemberships);

			return memberships;
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	@Override
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipsApplicationsByWorkgroupId(Long workgroupId) {
		
		try {
			
			Collection<WorkgroupMembershipRepresentation> memberships = workgroupMembershipMappingHelper
					.getDSL(workgroupManagement
							.getAllMembershipsApplicationsByWorkgroupId(workgroupId));
			return memberships;
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Collection<WorkgroupMembershipRepresentation> getAllApprovedMembershipsByWorkgroupId(Long workgroupId) {
		
		try {
			
			Collection<WorkgroupMembershipRepresentation> memberships = workgroupMembershipMappingHelper
					.getDSL(workgroupManagement
							.getAllApprovedMembershipsByWorkgroupId(workgroupId));
			return memberships;
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}
	

	@Override
	public Collection<UserRepresentation> getWorkgroupReferentsByWorkgroupId(Long workgroupId)
	{
		try {
			Collection<UserRepresentation> referents = userAccountMappingHelper
					.getDSL(workgroupManagement
							.getWorkgroupReferentsByWorkgroupId(workgroupId));

			return referents;
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}
	

	@Override
	public Collection<WorkgroupMembershipRepresentation> getAllMemberships() {
		
		try {
			
			Collection<WorkgroupMembershipRepresentation> memberships = workgroupMembershipMappingHelper
					.getDSL(workgroupManagement.getAllMemberships());
			return memberships;
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
	

	@Override
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipsByUserAccountId(Long userAccountId) {
		
		try {
			
			Collection<WorkgroupMembershipRepresentation> memberships = workgroupMembershipMappingHelper
					.getDSL(workgroupManagement
							.getAllMembershipsByUserAccountId(userAccountId));
			return memberships;
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	@Override
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipApplicationsByUserAccountId(
			Long userAccountId) {
		
		try {
			
			Collection<WorkgroupMembershipRepresentation> memberships = workgroupMembershipMappingHelper
					.getDSL(workgroupManagement
							.getAllMembershipApplicationsByUserAccountId(userAccountId));
			return memberships;
						
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	@Override
	public Collection<WorkgroupMembershipRepresentation> getAllApprovedMembershipsByUserAccountId(Long userAccountId) {
		
		try {
			
			Collection<WorkgroupMembershipRepresentation> memberships = workgroupMembershipMappingHelper
					.getDSL(workgroupManagement
							.getAllApprovedMembershipsByUserAccountId(userAccountId));
			return memberships;
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}
	
	@Override
	public Collection<WorkgroupRepresentation> getReferencedWorkgroupsByUserAccountId(Long userAccountId)
	{
		try {
			Collection<WorkgroupRepresentation> workgroups = workgroupMappingHelper.getDSL(workgroupManagement.getReferencedWorkgroupsByUserAccountId(userAccountId));
			return workgroups;	
			
		} catch (WorkgroupNotFoundException wnfe) {
			throw new PrismaWrapperException(new ResourceNotFoundException(Workgroup.class, "Referenced workgroups not found for user " + userAccountId));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}		
	}
	

	@Override
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipsApplications() {
		
		try {
			
			Collection<WorkgroupMembershipRepresentation> memberships = workgroupMembershipMappingHelper
					.getDSL(workgroupManagement.getAllMembershipsApplications());
			return memberships;
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	@Override
	public Collection<WorkgroupMembershipRepresentation> getAllApprovedMemberships() {
		
		
		try {
			
			Collection<WorkgroupMembershipRepresentation> memberships = workgroupMembershipMappingHelper
					.getDSL(workgroupManagement.getAllApprovedMemberships());
			return memberships;
						
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public void applicateForMembership(Long workgroupId, Long userAccountId) {
		
		try {
			workgroupManagement.applicateForMembership(workgroupId,
					userAccountId);
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public void addWorkgroupAdminMembership(
			WorkgroupMembershipRequest workgroupMembershipRequest) {
		
		try {
			workgroupManagement.addMembership(
					workgroupMembershipRequest.getWorkgroupId(),
					workgroupMembershipRequest.getApplicantUserAccountId(),
					workgroupMembershipRequest.getApprovedByUserAccountId(),
					WorkgroupPrivilegeEnum.WG_ADMIN);
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public void addWorkgroupUserMembership(
			WorkgroupMembershipRequest workgroupMembershipRequest) {
		
		
		try {
			workgroupManagement.addMembership(
					workgroupMembershipRequest.getWorkgroupId(),
					workgroupMembershipRequest.getApplicantUserAccountId(),
					workgroupMembershipRequest.getApprovedByUserAccountId(),
					WorkgroupPrivilegeEnum.WG_USER);
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public void addWorkgroupGuestMembership(
			WorkgroupMembershipRequest workgroupMembershipRequest) {
		
		try {
			workgroupManagement.addMembership(
					workgroupMembershipRequest.getWorkgroupId(),
					workgroupMembershipRequest.getApplicantUserAccountId(),
					workgroupMembershipRequest.getApprovedByUserAccountId(),
					WorkgroupPrivilegeEnum.WG_GUEST);
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public void removeMembership(Long workgroupId, Long userAccountId) {
		
		try {
			workgroupManagement.removeMembership(workgroupId, userAccountId);
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public void unapproveMembership(Long workgroupId, Long userAccountId) {
		
		try {
			workgroupManagement.unapproveMembership(workgroupId, userAccountId);
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public void approveWorkgroup(
			WorkgroupApprovationRequest workgroupApprovationRequest) {
		
		try {
			workgroupManagement.approveWorkgroup(
					workgroupApprovationRequest.getWorkgroupId(),
					workgroupApprovationRequest.getApprovedByUserAccountId());
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public void unapproveWorkgroup(Long workgroupId) {
		
		try {
			workgroupManagement.unapproveWorkgroup(workgroupId);
			
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

}
