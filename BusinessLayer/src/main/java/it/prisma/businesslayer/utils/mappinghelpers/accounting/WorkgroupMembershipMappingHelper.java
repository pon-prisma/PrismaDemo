package it.prisma.businesslayer.utils.mappinghelpers.accounting;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.dal.entitywrappers.accounting.WorkgroupMembership;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;

import javax.inject.Inject;

public class WorkgroupMembershipMappingHelper extends MappingHelperBase<WorkgroupMembership, WorkgroupMembershipRepresentation>{
	
	@Inject WorkgroupPrivilegeMappingHelper workgroupPrivilegeMappingHelper;

	@Override
	public WorkgroupMembership getEntity(WorkgroupMembershipRepresentation obj) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public WorkgroupMembershipRepresentation getDSL(WorkgroupMembership obj) {
		WorkgroupMembershipRepresentation dsl = new WorkgroupMembershipRepresentation();
		dsl.setWorkgroupId(obj.getWorkgroupId());
		dsl.setMemberUserAccountId(obj.getMemberUserAccountId());
		dsl.setApprovedByUserAccountId(obj.getApprovedByUserAccountId());
		dsl.setApproved(obj.isApproved());
		dsl.setWorkgroupPrivilege(workgroupPrivilegeMappingHelper.getDSL(obj.getWorkgroupPrivilege()));
		return dsl;
	}

}
