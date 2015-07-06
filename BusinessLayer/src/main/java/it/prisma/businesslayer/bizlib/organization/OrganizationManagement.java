package it.prisma.businesslayer.bizlib.organization;

import it.prisma.businesslayer.exceptions.DataAccessException;
import it.prisma.businesslayer.exceptions.organization.OrganizationNotFoundException;
import it.prisma.dal.entities.organization.Organization;

import javax.ejb.Local;
import javax.ejb.Stateless;

@Local
@Stateless
public interface OrganizationManagement {

	public Organization getOrganizationById(final Long organizationId)
		throws OrganizationNotFoundException, DataAccessException;
	
}
