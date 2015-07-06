package it.prisma.businesslayer.utils.mappinghelpers.accounting;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.dal.entities.accounting.Role;
import it.prisma.dal.entities.accounting.UserProfile;
import it.prisma.domain.dsl.accounting.users.RoleRepresentation;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.utils.datetime.DateConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class UserProfileMappingHelper extends MappingHelperBase<UserProfile, UserRepresentation> {
	
	@Inject RoleMappingHelper roleMappingHelper;

	@Override
	public UserProfile getEntity(UserRepresentation obj) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public UserRepresentation getDSL(UserProfile obj) {
		UserRepresentation dsl = new UserRepresentation();
		if (obj.getUserAccount() != null) {
			dsl.setUserAccountId(obj.getUserAccount().getId());
			dsl.setUserProfileId(obj.getUserAccountId());
			if (obj.getUserAccount().getIdentityProvider() != null)
				dsl.setIdentityProviderId(obj.getUserAccount()
						.getIdentityProvider().getId());
			dsl.setNameIdOnIdentityProvider(obj.getUserAccount()
					.getNameIdOnIdentityProvider());
			dsl.setEmail(obj.getUserAccount().getEmail());
				dsl.setSuspended(obj.getUserAccount().isSuspended());
			if (obj.getUserAccount().getSuspendedAt() != null)
				dsl.setSuspendedAt(DateConverter.convertAsISO8601(obj
						.getUserAccount().getSuspendedAt()));
			dsl.setEnabled(obj.getUserAccount().isEnabled());
			if (obj.getUserAccount().getEnabledAt() != null)
				dsl.setEnabledAt(DateConverter.convertAsISO8601(obj.getUserAccount()
						.getEnabledAt()));
			
			List<RoleRepresentation> roles = new ArrayList<RoleRepresentation>();
			for (Role role : obj.getUserAccount().getRoles())
				roles.add(roleMappingHelper.getDSL(role));
			
//			Set<RoleRepresentation> roles = new HashSet<RoleRepresentation>();
//			for (Role role : obj.getUserAccount().getRoles())
//				roles.add(roleMappingHelper.getDSL(role));
			
			dsl.setRoles(roles);
		}
		dsl.setFirstName(obj.getFirstName());
		dsl.setMiddleName(obj.getMiddleName());
		dsl.setLastName(obj.getLastName());
		dsl.setEmployer(obj.getEmployer());
		dsl.setEmailRef(obj.getEmailRef());
		dsl.setPersonalPhone(obj.getPersonalPhone());
		dsl.setWorkPhone(obj.getWorkPhone());
		dsl.setNationality(obj.getNationality());
		dsl.setTaxcode(obj.getTaxcode());
		if (obj.getCreatedAt() != null)
			dsl.setCreatedAt(DateConverter
					.convertAsISO8601(obj.getCreatedAt()));
		if (obj.getModifiedAt() != null)
			dsl.setModifiedAt(DateConverter
					.convertAsISO8601(obj.getModifiedAt()));
		return dsl;
	}

}
