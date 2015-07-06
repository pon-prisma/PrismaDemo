package it.prisma.presentationlayer.webui.core.accounting;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.users.requests.SignUpUserRequest;
import it.prisma.domain.dsl.prisma.AccountingErrorCode;
import it.prisma.presentationlayer.webui.security.exceptions.SSOCredentialAlreadyUsedException;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.prisma.idp.PrismaIdPAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

	
	@Value("${DEFAULT_AUTH_TOKEN}")
	String DEFAULT_AUTH_TOKEN;
	
	// Logger
	static final Logger LOG = LogManager.getLogger(UserManagementService.class);

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	@Autowired
	private PrismaIdPAPIClient prismaIdPAPIClient;

	
	public UserRepresentation getUserByCredentialsOnIdentityProvider(
			Long identityProviderId, String nameIdOnIdentityProvider)
					throws AuthenticationCredentialsNotFoundException, IOException {
		try {
			LOG.debug("getUserByCredentialsOnIdentityProvider calling BizApiClient");

			UserRepresentation userRepresentation = prismaBizAPIClient.getUserByCredentialsOnIdentityProvider(
					identityProviderId, nameIdOnIdentityProvider, DEFAULT_AUTH_TOKEN);
			LOG.debug("Biz found: " + userRepresentation.toString());
			return userRepresentation;
			
		} catch (APIErrorException e) {
		
			LOG.debug("Biz error: " + e.getMessage());
						
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND.getCode()) {
				throw new AuthenticationCredentialsNotFoundException(
						"There is no account for the provided credentials.");
			} else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode()) {
				LOG.info("Bad request.", e);
				throw new BadCredentialsException(
						"Provided credentials are invalid.");
			} else {
				LOG.info("Internal server error", e);
				throw new ProviderNotFoundException(
						"Authentication provider not found.");
			}
		} catch (MappingException e) {
			e.printStackTrace();
			
			LOG.info("Internal server error", e);
			throw new ProviderNotFoundException(
					"Authentication provider not found.");
		} catch (NoMappingModelFoundException | ServerErrorResponseException
				| RestClientException | IOException e) {
			e.printStackTrace();
			
			LOG.info("Generic data error.", e);
			throw new IOException();
		}
	}

	public void signUpOnPrismaIdentityProvider(String nameIdOnIdentityProvider,
			String password, String email, String firstName, String middleName, String lastName,
			String personalPhone, String workPhone, String nationality,
			String taxcode, String employer, String emailRef)
					throws AuthenticationException, IOException {

		SignUpUserRequest request = new SignUpUserRequest();
		request.setNameIdOnIdentityProvider(nameIdOnIdentityProvider);
		request.setEmail(email);
		request.setFirstName(firstName);
		request.setMiddleName(middleName);
		request.setLastName(lastName);
		request.setPersonalPhone(personalPhone);
		request.setWorkPhone(workPhone);
		request.setNationality(nationality);
		request.setTaxCode(taxcode);
		request.setEmployer(employer);
		request.setEmailRef(emailRef);

		try {
			// Register the user onto the PRISMA IdP
			// TODO move to Biz
			prismaIdPAPIClient.signUpOnPrismaIdentityProvider(nameIdOnIdentityProvider, password, firstName, lastName, email, taxcode, null);
			
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_SIGNUP_CREDENTIAL_ALREADY_USED_ON_IDP
					.getCode()) {
				throw new SSOCredentialAlreadyUsedException(
						"Provided SSO credentials are already used.");
			}
			else
				throw new BadCredentialsException("Provided credentials are invalid.");				
		} catch (MappingException e) {
			LOG.info("Internal server error", e);
			throw new ProviderNotFoundException(
					"Authentication provider not found.");
		} catch (NoMappingModelFoundException | ServerErrorResponseException
				| RestClientException e) {
			LOG.info("Generic data error", e);
			throw new IOException(e);
		}

		try {
			prismaBizAPIClient.signUpOnPrismaIdentityProvider(request, DEFAULT_AUTH_TOKEN);
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_SIGNUP_CREDENTIAL_ALREADY_USED_ON_IDP
					.getCode()) {
				LOG.info("Provided SSO credentials are already used.", e);
				throw new SSOCredentialAlreadyUsedException(
						"Provided SSO credentials are already used.");
			} else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode()) {
				LOG.info("Bad request.", e);
				throw new BadCredentialsException(
						"Provided credentials are invalid.");
			} else {
				LOG.info("Internal server error", e);
				throw new ProviderNotFoundException(
						"Authentication provider not found.");
			}
		} catch (MappingException e) {
			LOG.info("Internal server error", e);
			throw new ProviderNotFoundException(
					"Authentication provider not found.");
		} catch (NoMappingModelFoundException | ServerErrorResponseException
				| RestClientException | IOException e) {
			LOG.info("Generic data error", e);
			throw new IOException();
		}
	}

	public void signUpFromThirdPartyIdentityProvider(
			String identityProviderEntityId, String nameIdOnIdentityProvider,
			String email, String firstName, String middleName, String lastName,
			String personalPhone, String workPhone, String nationality,
			String taxcode, String employer, String emailRef)
					throws AuthenticationException, IOException {
		
		LOG.debug("Calling signUpFromThirdPartyIdentityProvider");
		
		SignUpUserRequest request = new SignUpUserRequest();
		request.setIdentityProviderEntityId(identityProviderEntityId);
		request.setNameIdOnIdentityProvider(nameIdOnIdentityProvider);
		request.setEmail(email);
		request.setFirstName(firstName);
		request.setMiddleName(middleName);
		request.setLastName(lastName);
		request.setPersonalPhone(personalPhone);
		request.setWorkPhone(workPhone);
		request.setNationality(nationality);
		request.setTaxCode(taxcode);
		request.setEmployer(employer);
		request.setEmailRef(emailRef);
		try {
			LOG.debug("signUpFromThirdPartyIdentityProvider is calling BizAPIClient");
			prismaBizAPIClient.signUpFromThirdPartyIdentityProvider(request, DEFAULT_AUTH_TOKEN);

		} catch (APIErrorException e) {
			LOG.debug("BizAPIClient error: " + e.getStackTrace());
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_SIGNUP_CREDENTIAL_ALREADY_USED_ON_IDP
					.getCode()) {
				LOG.info("Provided SSO credentials are already used.", e);
				throw new SSOCredentialAlreadyUsedException(
						"Provided SSO credentials are already used.");
			} else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode()) {
				LOG.info("Bad request.", e);
				throw new BadCredentialsException(
						"Provided credentials are invalid.");
			} else {
				LOG.info("Internal server error", e);
				throw new ProviderNotFoundException(
						"Authentication provider not found.");
			}
		} catch (ServerErrorResponseException e) {
			LOG.info("Connection error", e);
			throw new ProviderNotFoundException(
					"Authentication provider not found.");
		} catch (NoMappingModelFoundException | MappingException 
				| RestClientException | IOException e) {
			LOG.info("Generic data error", e);
			throw new IOException();
		}
	}

	public UserRepresentation getUserById(final Long userAccountId)
			throws AuthenticationException, IOException {
		try {

			return prismaBizAPIClient.getUserById(userAccountId, DEFAULT_AUTH_TOKEN);
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode()) {
				LOG.info("Credentials not found.", e);
				throw new AuthenticationCredentialsNotFoundException(
						"There is no account for the provided credentials.");
			} else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode()) {
				LOG.info("Bad request.", e);
				throw new BadCredentialsException(
						"Provided credentials are invalid.");
			} else {
				LOG.info("Internal server error", e);
				throw new ProviderNotFoundException(
						"Authentication provider not found.");
			}
		} catch (MappingException e) {
			LOG.info("Internal server error", e);
			throw new ProviderNotFoundException(
					"Authentication provider not found.");
		} catch (NoMappingModelFoundException | ServerErrorResponseException
				| RestClientException | IOException e) {
			LOG.info("Generic data error.", e);
			throw new IOException();
		}
	}

	
	public ArrayList<UserRepresentation> getAllUsers()
			throws AuthenticationException, IOException {
		try {
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();			

			return prismaBizAPIClient.getAllUsers(authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode()) {
				LOG.info("Credentials not found.", e);
				throw new AuthenticationCredentialsNotFoundException(
						"There is no account for the provided credentials.");
			} else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode()) {
				LOG.info("Bad request.", e);
				throw new BadCredentialsException(
						"Provided credentials are invalid.");
			} else {
				LOG.info("Internal server error", e);
				throw new ProviderNotFoundException(
						"Authentication provider not found.");
			}
		} catch (MappingException e) {
			LOG.info("Internal server error", e);
			throw new ProviderNotFoundException(
					"Authentication provider not found.");
		} catch (NoMappingModelFoundException | ServerErrorResponseException
				| RestClientException | IOException e) {
			LOG.info("Generic data error.", e);
			throw new IOException();
		}
	}
	
}
