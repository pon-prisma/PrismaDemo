package it.prisma.businesslayer.utils.mappinghelpers.organization;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.businesslayer.utils.mappinghelpers.accounting.RoleMappingHelper;
import it.prisma.dal.entities.organization.Organization;
import it.prisma.domain.dsl.accounting.organizations.OrganizationRepresentation;
import it.prisma.utils.datetime.DateConverter;

import javax.inject.Inject;

public class OrganizationMappingHelper extends MappingHelperBase<Organization, OrganizationRepresentation> {
	
	@Inject RoleMappingHelper roleMappingHelper;

	@Override
	public Organization getEntity(OrganizationRepresentation obj) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public OrganizationRepresentation getDSL(Organization obj) {
		
		OrganizationRepresentation dsl = new OrganizationRepresentation();
		
		
		dsl.setOrganizationId(obj.getId());
		dsl.setName(obj.getName());
		dsl.setDescription(obj.getDescription());
		dsl.setLogoUri(dsl.getLogoUri());
		dsl.setWebsiteUri(dsl.getWebsiteUri());
		dsl.setWebsiteLabel(obj.getWebsiteLabel());

		if (obj.getCreatedAt() != null)
			dsl.setCreatedAt(DateConverter
					.convertAsISO8601(obj.getCreatedAt()));
		
		if (obj.getModifiedAt() != null)
			dsl.setModifiedAt(DateConverter
					.convertAsISO8601(obj.getModifiedAt()));
		
		
		return dsl;
	}

}
