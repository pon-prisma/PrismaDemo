package it.prisma.businesslayer.utils.mappinghelpers.accounting;

import it.prisma.businesslayer.bizlib.accounting.WorkgroupManagement;
import it.prisma.businesslayer.exceptions.accounting.WorkgroupMembershipNotFoundException;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupRepresentation;

import javax.inject.Inject;

public class WorkgroupMappingHelper extends
		MappingHelperBase<Workgroup, WorkgroupRepresentation> {

	@Inject
	private WorkgroupManagement workgroupManagement;
	
	@Inject 
	private UserAccountMappingHelper userAccountMappingHelper;
	

	@Override
	public Workgroup getEntity(WorkgroupRepresentation obj) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public WorkgroupRepresentation getDSL(Workgroup obj) {
		WorkgroupRepresentation dsl = new WorkgroupRepresentation();
		long applicantsNumber = 0;
		long approvedMembersNumber = 0;

		if (obj== null)
			return null;
		try {
			applicantsNumber = workgroupManagement
					.getAllMembershipsApplicationsByWorkgroupId(obj.getId())
					.size();
		} catch (WorkgroupMembershipNotFoundException e) {
			applicantsNumber = 0;
		}

		try {
			approvedMembersNumber = workgroupManagement
					.getAllApprovedMembershipsByWorkgroupId(obj.getId()).size();
		} catch (WorkgroupMembershipNotFoundException e) {
			approvedMembersNumber = 0;
		}

		dsl.setApplicantsNumber(applicantsNumber);
		dsl.setApprovedMembersNumber(approvedMembersNumber);
		dsl.setTotalMembersNumber(applicantsNumber + approvedMembersNumber);
		dsl.setLabel(obj.getLabel());
		dsl.setDescription(obj.getDescription());
		dsl.setWorkgroupId(obj.getId());
		dsl.setApproved(obj.isApproved());
		if (obj.getUserAccountByApprovedByUserAccountId() != null)
			dsl.setApprovedByUserAccountId(obj
					.getUserAccountByApprovedByUserAccountId().getId());
		if (obj.getUserAccountByCreatedByUserAccountId() != null)
			dsl.setCreatedByUserAccountId(obj
					.getUserAccountByCreatedByUserAccountId().getId());
		
		if (obj.getReferentUsers() != null)
		{
			dsl.setReferentUsers(userAccountMappingHelper.getDSL(obj.getReferentUsers()));
		}
		
		return dsl;
	}

}
