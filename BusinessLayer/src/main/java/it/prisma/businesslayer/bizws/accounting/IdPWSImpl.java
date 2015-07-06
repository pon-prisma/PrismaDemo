package it.prisma.businesslayer.bizws.accounting;

import it.prisma.businesslayer.bizlib.accounting.IdPManagement;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.dal.entities.accounting.IdentityProvider;
import it.prisma.domain.dsl.accounting.organizations.IdentityProviderRepresentation;

import javax.inject.Inject;
import javax.ws.rs.PathParam;

public class IdPWSImpl implements IdPWS {

	@Inject
	private IdPManagement idPBean;

	@Inject
	private MappingHelper<IdentityProvider, IdentityProviderRepresentation> idpInfoMH;
		
	
	@Override
	public IdentityProviderRepresentation getIdPInfoFromEntityID(
			@PathParam("entityId") String entityId)
	{
		try {
			return idpInfoMH.getDSL(idPBean.getIdPInfoFromEntityID(entityId));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
	
	@Override
	public IdentityProviderRepresentation getIdPInfoFromIdentityProviderID(
			@PathParam("identityProviderId") Long identityProviderId)
	{
		try {
			return idpInfoMH.getDSL(idPBean.getIdPInfoFromIdPID(identityProviderId));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	
	
}
