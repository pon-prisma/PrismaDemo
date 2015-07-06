package it.prisma.businesslayer.bizlib.accounting;

import it.prisma.businesslayer.bizlib.common.exceptions.UserAccountNotFoundException;
import it.prisma.businesslayer.exceptions.BadRequestException;
import it.prisma.businesslayer.exceptions.DataAccessException;
import it.prisma.businesslayer.exceptions.accounting.UserAccountConfirmationTokenExpiredException;
import it.prisma.businesslayer.exceptions.accounting.UserAccountFoundException;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.UserProfile;

import java.util.ArrayList;

import javax.ejb.Local;
import javax.ejb.Stateless;

@Local
@Stateless
public interface UserManagement {

	public enum RoleEnum {
		
		PRISMA_GUEST("PRISMA_GUEST"), PRISMA_USER("PRISMA_USER"), 
		PRISMA_ADMIN("PRISMA_ADMIN"), PRISMA_WEBUI("PRISMA_WEBUI"),
		PRISMA_MONITORING("PRISMA_MONITORING");
		
		private final String role;

	    private RoleEnum(final String role) {
	        this.role = role;
	    }
	    
	    public String getRoleString()
	    {
	    	return this.role;
	    }
	    
		
	}

	// As params, the following methods have to accept DSL objects.

	// Create

	public void signUpOnPrismaIdentityProvider(
			final String nameIdOnIdentityProvider, final String email,
			final String firstName, final String middleName,
			final String lastName, final String personalPhone,
			final String workPhone, final String nationality,
			final String taxcode, final String employer, final String emailRef)
			throws UserAccountFoundException;

	public void signUpFromThirdPartyIdentityProvider(
			final String identityProviderEntityId,
			final String nameIdOnIdentityProvider, final String email,
			final String firstName, final String middleName,
			final String lastName, final String personalPhone,
			final String workPhone, final String nationality,
			final String taxcode, final String employer, final String emailRef)
			throws UserAccountFoundException;

	// Read

	public UserAccount getUserAccountByUserAccountId(Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;
	
	public UserProfile getUserProfileByUserProfileId(final Long userProfileId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public UserProfile getUserProfileByCredentialsOnIdentityProvider(
			final Long identityProviderId,
			final String nameIdOnIdentityProvider)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;
	
	public UserProfile getUserProfileByCredentialsOnIdentityProvider(
			final String identityProviderEntityId,
			final String nameIdOnIdentityProvider)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public ArrayList<UserProfile> getAllUserProfiles()
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;
	
	public boolean isPrismaAdmin(final Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
	DataAccessException;

	// Update

	public void suspendUser(final Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public void unsuspendUser(final Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;;

	public void enableUser(final Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public void disableUser(final Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public void confirmUserOnPrismaIdentityProvider(final String email,
			final String token) throws UserAccountNotFoundException,
			UserAccountConfirmationTokenExpiredException, BadRequestException,
			DataAccessException;

	public UserProfile updateUserProfile(final Long userAccountId,
			final String email, final String firstName,
			final String middleName, final String lastName,
			final String personalPhone, final String workPhone,
			final String nationality, final String taxcode,
			final String employer, final String emailRef, final String avatarURI)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public UserProfile addUserRole(final Long userAccountId, final RoleEnum role)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public UserProfile removeUserRole(final Long userAccountId,
			final RoleEnum role) throws UserAccountNotFoundException,
			BadRequestException, DataAccessException;

	public UserProfile setUserAccountAsPrismaGuest(final Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public UserProfile setUserAccountAsPrismaUser(final Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	public UserProfile setUserAccountAsPrismaAdmin(final Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException;

	// Note that we would't provide direct removal methods.

}
