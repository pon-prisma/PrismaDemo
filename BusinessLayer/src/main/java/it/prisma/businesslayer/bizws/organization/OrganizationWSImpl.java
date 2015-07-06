package it.prisma.businesslayer.bizws.organization;

import it.prisma.businesslayer.bizlib.organization.OrganizationManagement;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.utils.mappinghelpers.organization.OrganizationMappingHelper;
import it.prisma.domain.dsl.accounting.organizations.OrganizationRepresentation;

import javax.inject.Inject;

public class OrganizationWSImpl implements OrganizationWS {

	public static final String URI_ENCODE = "UTF-8";

	// Management bean
	@Inject OrganizationManagement organizationManagement;

	// Mapping helpers
	@Inject OrganizationMappingHelper organizationMappingHelper;

	@Override
	public OrganizationRepresentation getOrganizationById(String organizationId) {
		
		try 
		{
			OrganizationRepresentation resp = organizationMappingHelper.getDSL(organizationManagement
				.getOrganizationById(Long.valueOf(organizationId)));

			return resp;
			
		}
		catch (Exception e)
		{
			 throw new PrismaWrapperException(e);
		}
		

	}

}
