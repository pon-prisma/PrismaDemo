package it.prisma.businesslayer.bizws.accounting;

import it.prisma.businesslayer.bizlib.accounting.UserManagement;
import it.prisma.businesslayer.bizlib.common.exceptions.DuplicatedResourceException;
import it.prisma.businesslayer.bizlib.common.exceptions.UserAccountNotFoundException;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.exceptions.accounting.UserAccountFoundException;
import it.prisma.businesslayer.utils.mappinghelpers.accounting.UserProfileMappingHelper;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.UserProfile;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.users.requests.SignUpUserRequest;

import java.net.URLDecoder;
import java.util.Collection;

import javax.inject.Inject;

public class UserManagementWSImpl implements UserManagementWS {

	public static final String URI_ENCODE = "UTF-8";

	// Management bean
	@Inject UserManagement userManagement;

	// Mapping helpers
	@Inject UserProfileMappingHelper userMappingHelper;

	@Override
	public void signUpOnPrismaIdentityProvider(
			SignUpUserRequest signUpUserRequest) {
		
		try {
			userManagement.signUpOnPrismaIdentityProvider(
					signUpUserRequest.getNameIdOnIdentityProvider(),
					signUpUserRequest.getEmail(),
					signUpUserRequest.getFirstName(),
					signUpUserRequest.getMiddleName(),
					signUpUserRequest.getLastName(),
					signUpUserRequest.getPersonalPhone(),
					signUpUserRequest.getWorkPhone(),
					signUpUserRequest.getNationality(),
					signUpUserRequest.getTaxcode(),
					signUpUserRequest.getEmployer(),
					signUpUserRequest.getEmailRef());
		} catch (UserAccountFoundException uafe) {
			throw new PrismaWrapperException(new DuplicatedResourceException(UserAccount.class, "User account already in DB"));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public void signUpFromThirdPartyIdentityProvider(
			SignUpUserRequest signUpUserRequest) {
		
		
		try {
			userManagement.signUpFromThirdPartyIdentityProvider(
					signUpUserRequest.getIdentityProviderEntityId(),
					signUpUserRequest.getNameIdOnIdentityProvider(),
					signUpUserRequest.getEmail(),
					signUpUserRequest.getFirstName(),
					signUpUserRequest.getMiddleName(),
					signUpUserRequest.getLastName(),
					signUpUserRequest.getPersonalPhone(),
					signUpUserRequest.getWorkPhone(),
					signUpUserRequest.getNationality(),
					signUpUserRequest.getTaxcode(),
					signUpUserRequest.getEmployer(),
					signUpUserRequest.getEmailRef());
		} catch (UserAccountFoundException uafe) {
			throw new PrismaWrapperException(new DuplicatedResourceException(UserAccount.class, "User account already in DB"));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public UserRepresentation getUserById(Long userAccountId) {
		
		try {
			
			UserAccount userAccount = userManagement
					.getUserAccountByUserAccountId(userAccountId);
			UserProfile userProfile = userAccount
					.getUserProfile();
			
			UserRepresentation resp = userMappingHelper.getDSL(userProfile);
			return resp;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public UserRepresentation getUserByCredentialsOnIdentityProvider(
			Long identityProviderId, String nameIdOnIdentityProvider) {
		
		try {
			
			UserProfile userProfile = userManagement
					.getUserProfileByCredentialsOnIdentityProvider(
							identityProviderId, nameIdOnIdentityProvider);
			UserRepresentation resp = userMappingHelper.getDSL(userProfile);
			return resp;
		} catch (UserAccountNotFoundException uanfe) {
			throw new PrismaWrapperException(new UserAccountNotFoundException("User not found."));
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	@Override
	public UserRepresentation getUserByCredentialsOnIdentityProvider(
			String identityProviderEntityId, String nameIdOnIdentityProvider) {
		
		try {
			
			System.out.println(URLDecoder.decode(identityProviderEntityId,
					URI_ENCODE));
			UserProfile userProfile = userManagement
					.getUserProfileByCredentialsOnIdentityProvider(URLDecoder
							.decode(identityProviderEntityId, URI_ENCODE),
							nameIdOnIdentityProvider);
			UserRepresentation resp = userMappingHelper.getDSL(userProfile);
			return resp;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public Collection<UserRepresentation> getAllUsers() {
		
		try {
			
			Collection<UserRepresentation> resp = userMappingHelper.getDSL(userManagement.getAllUserProfiles());
			return resp;

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	@Override
	public void suspendUser(Long userAccountId) {
		
		try {
			userManagement.suspendUser(userAccountId);

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	@Override
	public void unsuspendUser(Long userAccountId) {
		
		try {
			userManagement.unsuspendUser(userAccountId);

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public void enableUser(Long userAccountId) {
		
		try {
			userManagement.enableUser(userAccountId);

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public void disableUser(Long userAccountId) {
		
		try {
			userManagement.disableUser(userAccountId);

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public void confirmUserOnPrismaIdentityProvider(String email,
			String token) {
		
		try {
			userManagement.confirmUserOnPrismaIdentityProvider(email, token);

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public void setUserAccountAsPrismaGuest(Long userAccountId) {
		
		try {
			userManagement.setUserAccountAsPrismaGuest(userAccountId);

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public void setUserAccountAsPrismaUser(Long userAccountId) {
		
		try {
			userManagement.setUserAccountAsPrismaUser(userAccountId);

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public void setUserAccountAsPrismaAdmin(Long userAccountId) {
		
		try {
			userManagement.setUserAccountAsPrismaAdmin(userAccountId);

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

	@Override
	public UserRepresentation updateUser(Long userAccountId, UserRepresentation userData) {
		
		try {
			UserRepresentation resp = userMappingHelper.getDSL(userManagement
					.updateUserProfile(userAccountId, userData.getEmail(),
							userData.getFirstName(), userData.getMiddleName(),
							userData.getLastName(),
							userData.getPersonalPhone(),
							userData.getWorkPhone(), userData.getNationality(),
							userData.getTaxcode(), userData.getEmployer(),
							userData.getEmailRef(), userData.getAvatar()));
			
			return resp;

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
		
	}

}
