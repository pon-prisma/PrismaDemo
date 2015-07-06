package it.prisma.businesslayer.utils.mappinghelpers;

import it.prisma.dal.entities.accounting.AuthToken;
import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;

public class AuthTokenMappingHelper extends
		MappingHelperBase<AuthToken, AuthTokenRepresentation> {

	@Override
	public AuthToken getEntity(AuthTokenRepresentation obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AuthTokenRepresentation getDSL(AuthToken obj) {
		if (obj == null)
			return null;

		AuthTokenRepresentation dsl = new AuthTokenRepresentation();

		dsl.setCreatedAt(obj.getCreatedAt());
		dsl.setExpiresAt(obj.getExpiresAt());
		dsl.setTokenId(obj.getId());
		dsl.setUserAccountId(obj.getUserAccount().getId());
		dsl.setSession(obj.getSession());

		return dsl;
	}

}
