package it.prisma.businesslayer.utils.mappinghelpers.accounting;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.dal.entities.accounting.Role;
import it.prisma.domain.dsl.accounting.users.RoleRepresentation;

public class RoleMappingHelper extends MappingHelperBase<Role, RoleRepresentation> {

	@Override
	public Role getEntity(RoleRepresentation obj) {
		Role entity = new Role();
		entity.setId(obj.getId());
		entity.setName(obj.getName());
		entity.setDescription(obj.getDescription());
		return entity;
	}

	@Override
	public RoleRepresentation getDSL(Role obj) {
		RoleRepresentation dsl = new RoleRepresentation();
		dsl.setId(obj.getId());
		dsl.setName(obj.getName());
		dsl.setDescription(obj.getDescription());
		return dsl;
	}

}
