package it.prisma.businesslayer.bizlib.accounting;

import it.prisma.businesslayer.bizlib.common.exceptions.UserAccountNotFoundException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.exceptions.BadRequestException;
import it.prisma.businesslayer.exceptions.DataAccessException;
import it.prisma.businesslayer.exceptions.accounting.IdentityProviderNotFoundException;
import it.prisma.businesslayer.exceptions.accounting.UserAccountConfirmationTokenExpiredException;
import it.prisma.businesslayer.exceptions.accounting.UserAccountFoundException;
import it.prisma.dal.dao.accounting.IdentityProviderDAO;
import it.prisma.dal.dao.accounting.RoleDAO;
import it.prisma.dal.dao.accounting.UserAccountConfirmationDAO;
import it.prisma.dal.dao.accounting.UserAccountDAO;
import it.prisma.dal.dao.accounting.UserAccountHasWorkgroupDAO;
import it.prisma.dal.dao.accounting.UserProfileDAO;
import it.prisma.dal.entities.accounting.IdentityProvider;
import it.prisma.dal.entities.accounting.Role;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.UserAccountConfirmation;
import it.prisma.dal.entities.accounting.UserAccountHasWorkgroup;
import it.prisma.dal.entities.accounting.UserAccountHasWorkgroupId;
import it.prisma.dal.entities.accounting.UserProfile;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.time.DateUtils;

public class UserManagementBean implements UserManagement {

	private static final Integer USER_ACCOUNT_CONFIRMATION_TOKEN_VALIDITY = 48; // Hours
	private IdentityProvider prismaIdentityProvider;

	
	//@Inject private MailServiceBean mailService;
	@Inject
	protected EnvironmentConfig envConfig;
	@Inject
	private IdentityProviderDAO identityProviderDAO;
	@Inject
	private UserAccountDAO userAccountDAO;
	@Inject
	private UserProfileDAO userProfileDAO;
	@Inject
	private RoleDAO roleDAO;
	@Inject
	private UserAccountConfirmationDAO userAccountConfirmationDAO;
	
	@Inject
	private UserAccountHasWorkgroupDAO userAccountHasWorkgroupDAO;
	
	@PostConstruct
	private void check() throws IOException {
		assert (identityProviderDAO != null);
		assert (userAccountDAO != null);
		assert (userProfileDAO != null);
		assert (roleDAO != null);
		assert (userAccountConfirmationDAO != null);
		try {
			try {
				prismaIdentityProvider = identityProviderDAO
						.findByEntityId(envConfig.
								getSvcEndpointProperty(EnvironmentConfig.SVCEP_PRISMA_IDP_URL));
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new IdentityProviderNotFoundException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void signUpOnPrismaIdentityProvider(String nameIdOnIdentityProvider,
			String email, String firstName, String middleName, String lastName,
			String personalPhone, String workPhone, String nationality,
			String taxcode, String employer, String emailRef)
			throws UserAccountFoundException
	{
		try {
			try {
				
				if (userAccountDAO.existsForGivenCredentialsOnIdentityProvider(
						prismaIdentityProvider.getId(),
						nameIdOnIdentityProvider)) {
					throw new UserAccountFoundException();
				}
				
				UserRepresentation userRepresentation = new UserRepresentation();
				
				userRepresentation.setNameIdOnIdentityProvider(nameIdOnIdentityProvider);
				userRepresentation.setEmail(email);
				userRepresentation.setFirstName(firstName);
				userRepresentation.setMiddleName(middleName);
				userRepresentation.setLastName(lastName);
				userRepresentation.setPersonalPhone(personalPhone);
				userRepresentation.setWorkPhone(workPhone);
				userRepresentation.setNationality(nationality);
				userRepresentation.setTaxcode(taxcode);
				userRepresentation.setEmployer(employer);
				userRepresentation.setEmailRef(emailRef);
				
				insertUserIntoDB(prismaIdentityProvider, userRepresentation);
				
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (UserAccountFoundException e) {
			throw e;
		} catch (EntityNotFoundException e) {
			throw new BadRequestException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	
	@Override
	public void signUpFromThirdPartyIdentityProvider(
			String identityProviderEntityId, String nameIdOnIdentityProvider,
			String email, String firstName, String middleName, String lastName,
			String personalPhone, String workPhone, String nationality,
			String taxcode, String employer, String emailRef) throws UserAccountFoundException {
	
			if (identityProviderEntityId.equals(prismaIdentityProvider.getEntityId()))
				signUpOnPrismaIdentityProvider(identityProviderEntityId,
							email, firstName, middleName, lastName,
							personalPhone, workPhone, nationality, taxcode,
							employer, emailRef);
			else {
				IdentityProvider identityProvider = identityProviderDAO
					.findByEntityId(identityProviderEntityId);
				
				
				if (userAccountDAO.existsForGivenCredentialsOnIdentityProvider(
						identityProvider.getId(),
						nameIdOnIdentityProvider)) {
					throw new UserAccountFoundException();
				}
				
				
				UserRepresentation userRepresentation = new UserRepresentation();
				
				userRepresentation.setNameIdOnIdentityProvider(nameIdOnIdentityProvider);
				userRepresentation.setEmail(email);
				userRepresentation.setFirstName(firstName);
				userRepresentation.setMiddleName(middleName);
				userRepresentation.setLastName(lastName);
				userRepresentation.setPersonalPhone(personalPhone);
				userRepresentation.setWorkPhone(workPhone);
				userRepresentation.setNationality(nationality);
				userRepresentation.setTaxcode(taxcode);
				userRepresentation.setEmployer(employer);
				userRepresentation.setEmailRef(emailRef);
				
				insertUserIntoDB(identityProvider, userRepresentation);
			
			}
	}

	@Override
	public UserAccount getUserAccountByUserAccountId(Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				return userAccountDAO.findById(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}
	
	@Override
	public UserProfile getUserProfileByUserProfileId(Long userProfileId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				return userProfileDAO.findByUserAccountId(userProfileId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public UserProfile getUserProfileByCredentialsOnIdentityProvider(
			Long identityProviderId, String nameIdOnIdentityProvider)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		
		UserAccount userAccount = userAccountDAO.findByCredentialsOnIdentityProvider(identityProviderId, nameIdOnIdentityProvider);
			
		if(userAccount == null)
			throw new UserAccountNotFoundException("User not found.");
				
		return userAccount.getUserProfile();
				
	}

	public UserProfile getUserProfileByCredentialsOnIdentityProvider(
			final String identityProviderEntityId,
			final String nameIdOnIdentityProvider)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				UserAccount userAccount = userAccountDAO
						.findByCredentialsOnIdentityProvider(
								identityProviderDAO.findByEntityId(
										identityProviderEntityId).getId(),
								nameIdOnIdentityProvider);
				return userAccount.getUserProfile();
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public ArrayList<UserProfile> getAllUserProfiles()
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				return (ArrayList<UserProfile>) userProfileDAO.findAll();
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public boolean isPrismaAdmin(Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {

		for (Role role : this.getUserAccountByUserAccountId(userAccountId).getRoles())
			if (role.getName().compareTo(getRoleFromEnum(RoleEnum.PRISMA_ADMIN).getName()) == 0)
				return true;
		return false;
	}
	
	@Override
	public void suspendUser(Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				userAccountDAO.suspend(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void unsuspendUser(Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				userAccountDAO.unsuspend(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void enableUser(Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				userAccountDAO.enable(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void disableUser(Long userAccountId) {
		try {
			try {
				userAccountDAO.disable(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public void confirmUserOnPrismaIdentityProvider(String email, String token)
			throws UserAccountNotFoundException,
			UserAccountConfirmationTokenExpiredException, BadRequestException,
			DataAccessException {
		try {
			try {
				UserAccountConfirmation userAccountConfirmation = userAccountConfirmationDAO
						.findByToken(token);
				UserAccount userAccount = userAccountConfirmation
						.getUserAccount();
				if ((userAccount.getIdentityProvider().getId() == prismaIdentityProvider
						.getId())
						&& (userAccount.getEmail().compareTo(email) == 0)
						&& (!userAccount.isEnabled())) {
					if (!userAccountConfirmation.isValid())
						throw new UserAccountConfirmationTokenExpiredException();
					else if (isExpired(userAccountConfirmation.getCreatedAt(),
							userAccountConfirmation.getExpiresOn())) {
						if (userAccountConfirmation.isValid())
							userAccountConfirmationDAO.invalidateToken(token);
						throw new UserAccountConfirmationTokenExpiredException();
					} else {
						userAccountConfirmationDAO
								.invalidateAllTokenForAGivenUserAccount(userAccount
										.getId());
						this.setUserAccountAsPrismaUser(userAccount.getId());
						userAccountDAO.enable(userAccount.getId());
					}
				} else
					throw new BadRequestException();
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (UserAccountConfirmationTokenExpiredException e) {
			throw e;
		} catch (EntityNotFoundException e) {
			throw new BadRequestException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public UserProfile updateUserProfile(final Long userAccountId,
			final String email, final String firstName,
			final String middleName, final String lastName,
			final String personalPhone, final String workPhone,
			final String nationality, final String taxcode,
			final String employer, final String emailRef, final String avatarURI)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				UserAccount userAccount = userAccountDAO.findById(userAccountId);
				if (email != null && email.length() > 0)
					userAccount.setEmail(email);
				UserProfile userProfile = userAccount.getUserProfile();
				
//				UserProfile userProfile = userProfileDAO.findById(userAccountId);
				if (firstName != null && firstName.length() > 0)
					userProfile.setFirstName(firstName);
				if (middleName != null && middleName.length() > 0)
					userProfile.setMiddleName(middleName);
				if (lastName != null && lastName.length() > 0)
					userProfile.setLastName(lastName);
				if (personalPhone != null && personalPhone.length() > 0)
					userProfile.setPersonalPhone(personalPhone);
				if (workPhone != null && workPhone.length() > 0)
					userProfile.setWorkPhone(workPhone);
				if (nationality != null && nationality.length() > 0)
					userProfile.setNationality(nationality);
				if (taxcode != null && taxcode.length() > 0)
					userProfile.setTaxcode(taxcode);
				if (employer != null && employer.length() > 0)
					userProfile.setEmployer(employer);
				if (emailRef != null && emailRef.length() > 0)
					userProfile.setEmailRef(emailRef);
				if (avatarURI != null && avatarURI.length() > 0)
					userProfile.setAvatarUri(avatarURI);
				userAccountDAO.update(userAccount);
				return userProfileDAO.update(userProfile);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public UserProfile addUserRole(Long userAccountId, RoleEnum role)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				userAccountDAO.addRole(userAccountId, getRoleFromEnum(role));
				return userAccountDAO.findById(userAccountId).getUserProfile();
				
//				return userProfileDAO.findByUserAccountId(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public UserProfile removeUserRole(Long userAccountId, RoleEnum role)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				userAccountDAO.removeRole(userAccountId, getRoleFromEnum(role));
				UserAccount userAccount = userAccountDAO
						.findById(userAccountId);
				if (userAccount.getRoles().isEmpty()) {
					userAccountDAO.addRole(userAccountId,
							getRoleFromEnum(RoleEnum.PRISMA_GUEST));
					userAccountDAO.suspend(userAccountId);
				}
				return userAccount.getUserProfile();
//				return userProfileDAO.findByUserAccountId(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public UserProfile setUserAccountAsPrismaGuest(Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				userAccountDAO.removeAllRoles(userAccountId);
				userAccountDAO.addRole(userAccountId,
						getRoleFromEnum(RoleEnum.PRISMA_GUEST));
				return userAccountDAO.findById(userAccountId).getUserProfile();
//				return userProfileDAO.findByUserAccountId(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public UserProfile setUserAccountAsPrismaUser(Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				userAccountDAO.removeAllRoles(userAccountId);
				userAccountDAO.addRole(userAccountId,
						getRoleFromEnum(RoleEnum.PRISMA_USER));
				return userAccountDAO.findById(userAccountId).getUserProfile();
//				return userProfileDAO.findByUserAccountId(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	@Override
	public UserProfile setUserAccountAsPrismaAdmin(Long userAccountId)
			throws UserAccountNotFoundException, BadRequestException,
			DataAccessException {
		try {
			try {
				userAccountDAO.removeAllRoles(userAccountId);
				userAccountDAO.addRole(userAccountId,
						getRoleFromEnum(RoleEnum.PRISMA_ADMIN));
				return userAccountDAO.findById(userAccountId).getUserProfile();
//				return userProfileDAO.findByUserAccountId(userAccountId);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new UserAccountNotFoundException("User not found.");
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}
	
	
	private void insertUserIntoDB(IdentityProvider identityProvider, UserRepresentation userRepresentation)
	{
		//TODO: gestire l'abilitazione dell'account
		UserAccount userAccount = new UserAccount(identityProvider, 
				userRepresentation.getNameIdOnIdentityProvider(), userRepresentation.getEmail(), false, true);
		Set<Role> roles = new HashSet<Role>();
		// User role is set as PRISMA_GUEST while confirming the account
		roles.add(roleDAO.findByName(UserManagement.RoleEnum.PRISMA_USER.getRoleString()));
		userAccount.setRoles(roles);
		UserProfile userProfile = new UserProfile(userAccount,
				userRepresentation.getFirstName(), userRepresentation.getMiddleName(), userRepresentation.getLastName(),
				userRepresentation.getPersonalPhone(), userRepresentation.getEmployer(), userRepresentation.getEmailRef(),
				userRepresentation.getWorkPhone(), userRepresentation.getNationality(), userRepresentation.getTaxcode());
		userAccountDAO.create(userAccount);
		UserProfile profile = userProfileDAO.create(userProfile);
		userAccount.setUserProfile(userProfile);
		userAccountDAO.update(userAccount);
		Date currentDate = new Date();
		UserAccountConfirmation userAccountConfirmation = new UserAccountConfirmation(
				userAccount, this.generateToken(), true, currentDate,
				DateUtils.addHours(currentDate,
						USER_ACCOUNT_CONFIRMATION_TOKEN_VALIDITY));
		userAccountConfirmationDAO.create(userAccountConfirmation);
		StringBuilder fullNameBuilder = new StringBuilder();
		fullNameBuilder.append(userRepresentation.getFirstName()).append(" ");
		if (userRepresentation.getMiddleName() != null)
			fullNameBuilder.append(userRepresentation.getMiddleName()).append(" ");
		fullNameBuilder.append(userRepresentation.getLastName());
		
		// TODO quando si abilitano le email mettere a false enable nella entity UserAccount
		/*
		mailService.getMailSender().sendAccountToConfirmMail(email,
				fullNameBuilder.toString(),
				userAccountConfirmation.getToken());
		*/
			
		// TODO: Da spostare la creazione dei WG alla fase di autorizzazione dell'account con il WG appropriato
		UserAccountHasWorkgroup userAccountHasWorkgroup = (UserAccountHasWorkgroup) (new ArrayList(userAccountHasWorkgroupDAO.findAll())).get(0);
		UserAccountHasWorkgroupId userAccountHasWorkgroupId = new UserAccountHasWorkgroupId(userAccount.getId(), 1L);
		userAccountHasWorkgroupDAO.create(new UserAccountHasWorkgroup(userAccountHasWorkgroupId, 
				userAccountHasWorkgroup.getUserAccountByUserAccountId(), userAccountHasWorkgroup.getWorkgroupPrivilege(), userAccountHasWorkgroup.getUserAccountByApprovedByUserAccountId(), userAccountHasWorkgroup.getWorkgroup(), true));
				
	}
	
	

	// Helpers

	private boolean isExpired(Date start, Date end) {
		return (((end.getTime() - start.getTime()) / (60 * 60 * 1000) % 24) > USER_ACCOUNT_CONFIRMATION_TOKEN_VALIDITY) ? true
				: false;
	}

	private String generateToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	private Role getRoleFromEnum(RoleEnum role) {
		try {
			try {
				switch (role) {
				case PRISMA_GUEST:
					return roleDAO.findByName("PRISMA_GUEST");
				case PRISMA_USER:
					return roleDAO.findByName("PRISMA_USER");
				case PRISMA_ADMIN:
					return roleDAO.findByName("PRISMA_ADMIN");
				default:
					throw new BadRequestException();
				}
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException e) {
			throw new BadRequestException();
		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

}
