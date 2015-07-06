package it.prisma.businesslayer.utils.mappinghelpers.accounting;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.dal.entities.accounting.IdentityProvider;
import it.prisma.domain.dsl.accounting.organizations.IdentityProviderRepresentation;
import it.prisma.utils.datetime.DateConverter;

import javax.inject.Inject;

public class IdentityProviderMappingHelper extends MappingHelperBase<IdentityProvider, IdentityProviderRepresentation> 
{
	
	@Inject RoleMappingHelper roleMappingHelper;

	@Override
	public IdentityProvider getEntity(IdentityProviderRepresentation obj) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public IdentityProviderRepresentation getDSL(IdentityProvider obj) {
		
		IdentityProviderRepresentation dsl = new IdentityProviderRepresentation();
		dsl.setCreatedAt(DateConverter.convertAsISO8601(obj.getCreatedAt()));
		dsl.setEntityId(obj.getEntityId());
		dsl.setIdentityProviderId(obj.getId());
		dsl.setModifiedAt(DateConverter.convertAsISO8601(obj.getModifiedAt()));
		dsl.setOrganizationId(obj.getOrganization().getId());
		
		return dsl;
	}


}

