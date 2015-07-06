package it.prisma.businesslayer.bizws.accounting;

import it.prisma.businesslayer.bizlib.accounting.AuthTokenManagement;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.dal.entities.accounting.AuthToken;
import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;

import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;

public class AuthenticationWSImpl extends BaseWS implements AuthenticationWS {

	@Inject
	private AuthTokenManagement authTokenBean;

	@Inject
	private MappingHelper<AuthToken, AuthTokenRepresentation> authTokenMH;

	@Override
	public AuthTokenRepresentation requestToken(long userId) {
		try {
			return authTokenMH.getDSL(authTokenBean.createToken(userId));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
	
	@Override
	public AuthTokenRepresentation requestSessionToken(@PathParam("userId") long userId){
		try {
			return authTokenMH.getDSL(authTokenBean.createSessionToken(userId));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Collection<AuthTokenRepresentation> getTokens(long userId) {
		try {
			return authTokenMH.getDSL(authTokenBean.getTokenByUser(userId));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public void deleteToken(String tokenId) {
		try {
			if (authTokenBean.deleteToken(tokenId))
			{
				getSessionServletResponse().setStatus(
						Status.NO_CONTENT.getStatusCode());
			}
			else
			{
				HttpServletResponse httpServletResponse = getSessionServletResponse();
			
				httpServletResponse.setStatus(
						Status.NOT_FOUND.getStatusCode());
				
				httpServletResponse.getWriter().write("{}");
				httpServletResponse.getWriter().flush();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
	
	@Override
	public AuthTokenRepresentation getSessionToken(@PathParam("userId") long userId){
		try {
			return authTokenMH.getDSL(authTokenBean.getSessionTokenByUser(userId));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

}
