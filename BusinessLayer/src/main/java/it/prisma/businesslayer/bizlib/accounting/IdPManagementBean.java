package it.prisma.businesslayer.bizlib.accounting;

import it.prisma.dal.dao.accounting.IdentityProviderDAO;
import it.prisma.dal.entities.accounting.IdentityProvider;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@Local(IdPManagement.class)
public class IdPManagementBean implements IdPManagement {
	
	@Inject
	private IdentityProviderDAO identityProviderDAO;

	@Override
	public IdentityProvider getIdPInfoFromIdPID(Long idPID) 
	{
		
		IdentityProvider idP = identityProviderDAO.findById(idPID);
		return idP;
		
	}

	@Override
	public IdentityProvider getIdPInfoFromEntityID(String entityID) 
	{
		IdentityProvider idP = identityProviderDAO.findByEntityId(entityID);		
		return idP;
		
	}

}
