package it.prisma.businesslayer.utils.mappinghelpers.accounting;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.dal.entities.accounting.WorkgroupPrivilege;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupPrivilegeRepresentation;

public class WorkgroupPrivilegeMappingHelper extends MappingHelperBase<WorkgroupPrivilege, WorkgroupPrivilegeRepresentation> {

	@Override
	public WorkgroupPrivilege getEntity(WorkgroupPrivilegeRepresentation obj) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public WorkgroupPrivilegeRepresentation getDSL(final WorkgroupPrivilege obj) {
		return obj == null ? null : new WorkgroupPrivilegeRepresentation() {
			{
				setWorkgroupPrivilegeId(obj.getId());
				setWorkgroupPrivilegeDescription(obj.getDescription());
				setWorkgroupPrivilegeName(obj.getName());
			}
		};
	}

}
