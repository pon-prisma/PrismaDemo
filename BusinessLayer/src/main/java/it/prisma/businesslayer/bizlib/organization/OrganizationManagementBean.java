package it.prisma.businesslayer.bizlib.organization;

import it.prisma.businesslayer.exceptions.DataAccessException;
import it.prisma.businesslayer.exceptions.organization.OrganizationNotFoundException;
import it.prisma.dal.dao.accounting.OrganizationDAO;
import it.prisma.dal.entities.organization.Organization;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

public class OrganizationManagementBean implements OrganizationManagement {

	@Inject
	private OrganizationDAO organizationDAO;


	@PostConstruct
	private void check() throws IOException {
		assert (organizationDAO != null);
	}



	@Override
	public Organization getOrganizationById(Long organizationId) 
			throws OrganizationNotFoundException, DataAccessException
	{
		
		try {
			try {
				return organizationDAO.findById(organizationId);
				
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new OrganizationNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
		
	}

}
