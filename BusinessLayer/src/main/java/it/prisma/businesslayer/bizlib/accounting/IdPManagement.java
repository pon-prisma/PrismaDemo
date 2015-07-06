package it.prisma.businesslayer.bizlib.accounting;

import it.prisma.dal.entities.accounting.IdentityProvider;

public interface IdPManagement {

	public  IdentityProvider getIdPInfoFromIdPID(final Long idPID);
	
	public  IdentityProvider getIdPInfoFromEntityID(final String entityID);

}
