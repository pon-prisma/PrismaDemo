package it.prisma.businesslayer.utils.mappinghelpers.accounting;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.dal.entities.accounting.Role;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.domain.dsl.accounting.users.RoleRepresentation;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.utils.datetime.DateConverter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UserAccountMappingHelper extends MappingHelperBase<UserAccount, UserRepresentation> {
	
	@Inject RoleMappingHelper roleMappingHelper;

	@Override
	public UserAccount getEntity(UserRepresentation obj) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public UserRepresentation getDSL(UserAccount obj) {
		UserRepresentation dsl = new UserRepresentation();
		if (obj != null) {
			dsl.setUserAccountId(obj.getId());
			dsl.setUserProfileId(obj.getUserProfile().getUserAccountId());
			if (obj.getIdentityProvider() != null)
				dsl.setIdentityProviderId(obj.getIdentityProvider().getId());
			dsl.setNameIdOnIdentityProvider(obj.getNameIdOnIdentityProvider());
			dsl.setEmail(obj.getEmail());
				dsl.setSuspended(obj.isSuspended());
			if (obj.getSuspendedAt() != null)
				dsl.setSuspendedAt(DateConverter.convertAsISO8601(obj.getSuspendedAt()));
			dsl.setEnabled(obj.isEnabled());
			if (obj.getEnabledAt() != null)
				dsl.setEnabledAt(DateConverter.convertAsISO8601(obj.getEnabledAt()));
			
			List<RoleRepresentation> roles = new ArrayList<RoleRepresentation>();
			for (Role role : obj.getRoles())
				roles.add(roleMappingHelper.getDSL(role));
			
//			Set<RoleRepresentation> roles = new HashSet<RoleRepresentation>();
//			for (Role role : obj.getRoles())
//				roles.add(roleMappingHelper.getDSL(role));
			
			dsl.setRoles(roles);
		}
		dsl.setFirstName(obj.getUserProfile().getFirstName());
		dsl.setMiddleName(obj.getUserProfile().getMiddleName());
		dsl.setLastName(obj.getUserProfile().getLastName());
		dsl.setEmployer(obj.getUserProfile().getEmployer());
		dsl.setEmailRef(obj.getUserProfile().getEmailRef());
		dsl.setPersonalPhone(obj.getUserProfile().getPersonalPhone());
		dsl.setWorkPhone(obj.getUserProfile().getWorkPhone());
		dsl.setNationality(obj.getUserProfile().getNationality());
		dsl.setTaxcode(obj.getUserProfile().getTaxcode());
		if (obj.getCreatedAt() != null)
			dsl.setCreatedAt(DateConverter
					.convertAsISO8601(obj.getCreatedAt()));
		if (obj.getModifiedAt() != null)
			dsl.setModifiedAt(DateConverter
					.convertAsISO8601(obj.getModifiedAt()));
		return dsl;
	}

}
