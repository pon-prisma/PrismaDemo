package it.prisma.businesslayer.bizlib.accounting;

import it.prisma.businesslayer.bizlib.common.exceptions.TokenNotFoundException;
import it.prisma.businesslayer.bizlib.common.exceptions.UserAccountNotFoundException;
import it.prisma.dal.entities.accounting.AuthToken;

import java.util.Collection;

public interface AuthTokenManagement {

	public AuthToken createToken(final Long userAccountId) throws UserAccountNotFoundException;
	
	public AuthToken createSessionToken(final Long userAccountId) throws UserAccountNotFoundException;
	
	public boolean isTokenValid(AuthToken token);
	
	public AuthToken getToken(final String tokenId) throws TokenNotFoundException;
	
	public PrismaUserDetails getTokenMeta(final String tokenId) throws TokenNotFoundException;

	public Collection<AuthToken> getTokenByUser(final Long userAccountId) throws UserAccountNotFoundException;

	public boolean deleteToken(final String tokenId);

	public AuthToken getSessionTokenByUser(Long userAccountId) throws TokenNotFoundException;

}
