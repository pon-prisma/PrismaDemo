package it.prisma.utils.web.ws.rest.apiclients.prisma.idp;

import it.prisma.domain.dsl.prisma.AccountingErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper;
import it.prisma.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.PrismaRestResponseDecoder;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class PrismaIdPAPIClient extends AbstractAPIClient {
	
	private Logger LOG = LogManager.getLogger(PrismaIdPAPIClient.class);

	private static final String APP_PATH = "MIUR_PRISMA-2.1-IdP-WebServices/rest/idpPrisma/";

	public PrismaIdPAPIClient(String baseWSUrl) {
		super(baseWSUrl + APP_PATH);
	}
	
	public PrismaIdPAPIClient(String baseWSUrl,
			RestClientFactory restClientFactory) {
		super(baseWSUrl + APP_PATH, restClientFactory);
	}

	// Sign Up ******************************

	public void signUpOnPrismaIdentityProvider(String username,
			String password, String firstName, String lastName,
			String email, String taxcode, String auth_data)	throws
			RestClientException, NoMappingModelFoundException, 
			MappingException, ServerErrorResponseException {
		
		// Register the user onto the PRISMA IdP 
		
		String URL = baseWSUrl + "registerUser?username=" + username
				+ "&password=" + password + "&firstName=" + firstName
				+ "&lastName=" + lastName + "&email=" + email + "&taxCode="
				+ taxcode;
		
		LOG.debug("signUpOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);
				
		if (result.getStatus().getFamily() != Status.Family.SUCCESSFUL) 
		{
			
			PrismaResponseWrapper<String> responseResult = (PrismaResponseWrapper<String>) result.getResult();
			
			if (responseResult.getError().getErrorMsg().equals("Username already exist"))
			{
				it.prisma.domain.dsl.prisma.prismaprotocol.Error error = responseResult.getError();
				error.setErrorCode(AccountingErrorCode.ACC_USER_SIGNUP_CREDENTIAL_ALREADY_USED_ON_IDP.getCode());

				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						error);
			}
			else
				//TODO capire cosa non ha funzionato
				throw new APIErrorException("API_ERROR", null,
						result.getOriginalRestMessage(),
						((PrismaResponseWrapper) result.getResult()).getError());
		}

	}
		
	
	// Delete user ******************************

	public boolean deleteUserOnPrismaIdentityProvider(String username, 
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Delete the user from the PRISMA IdP 
		
		String URL = baseWSUrl + "deleteUser?username=" + username;
		
		LOG.debug("deleteUserOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);

		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
	
	
	// Unregister user ******************************

	public boolean unregisterUserOnPrismaIdentityProvider(String username,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Unregister the user from the PRISMA IdP 
		
		String URL = baseWSUrl + "unregisterUser?username=" + username;
		
		LOG.debug("unregisterUserOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);

		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
	
	// Check user ******************************

	public boolean checkUserOnPrismaIdentityProvider(String username,
			String password, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Check the user on the PRISMA IdP 
		
		String URL = baseWSUrl + "checkUser?username=" + username + "&password=" + password;
		
		LOG.debug("checkUserOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);

		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
		
	// Edit email user ******************************

	public boolean editEmailOfUserOnPrismaIdentityProvider(String username,
			String email, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Edit the email of the user on the PRISMA IdP 
		
		String URL = baseWSUrl + "editMail?username=" + username + "&mail=" + email;
		
		LOG.debug("editEmailOfUserOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);

		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	// Edit user ******************************

	public boolean editUserOnPrismaIdentityProvider(String username,
			String password, String firstName, String lastName,
			String email, String taxcode, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Edit the user on the PRISMA IdP 
		
		
		String URL = baseWSUrl + "editUser?username=" + username
				+ "&password=" + password + "&firstName=" + firstName
				+ "&lastName=" + lastName + "&email=" + email + "&taxCode="
				+ taxcode;
		
		LOG.debug("editUserOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);

		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
	
	

	// Get Username By Tax Code And Mail ******************************

	public boolean getUsernameByTaxCodeAndMailOnPrismaIdentityProvider(String taxCode,
			String email, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Get Username By Tax Code And Mail on the PRISMA IdP 
		
		
		String URL = baseWSUrl + "getUsernameByTaxCodeAndMail?taxCode=" + taxCode
				+ "&email=" + email;
		
		LOG.debug("getUsernameByTaxCodeAndMailOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);
		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
	
	
	// Get Username By username and Tax Code ******************************

	public boolean getUsernameByUsernameAndTaxCodeOnPrismaIdentityProvider(String username,
			String taxCode, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Get Username By username and Tax Code on the PRISMA IdP 
		
		
		String URL = baseWSUrl + "getUserByUsernameAndTaxCode?username=" + username + 
				"&taxCode=" + taxCode;
		
		LOG.debug("getUsernameByUsernameAndTaxCodeOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);

		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
	
	
	// Recover username ******************************

	public boolean recoverUsernameOnPrismaIdentityProvider(String email,
			String taxCode, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Recover username on the PRISMA IdP 
		
		
		String URL = baseWSUrl + "recoverUsername?email=" + email + 
				"&taxCode=" + taxCode;
		
		LOG.debug("recoverUsernameOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);
		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
	
	// Recover password ******************************

	public boolean recoverPasswordOnPrismaIdentityProvider(String username,
			String taxCode, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Recover password on the PRISMA IdP 
		
		
		String URL = baseWSUrl + "recoverPassword?username=" + username + 
				"&taxCode=" + taxCode;
		
		LOG.debug("recoverPasswordOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);

		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}

	
	// Edit password ******************************

	public boolean editPasswordOnPrismaIdentityProvider(String username,
			String password,String tokenValue, String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {
		
		// Recover password on the PRISMA IdP 
		
		
		String URL = baseWSUrl + "editPassword?username=" + username + 
				"&password=" + password + "&tokenValue=" + tokenValue;
		
		LOG.debug("editPasswordOnPrismaIdentityProvider " + URL);
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		
		
		BaseRestResponseResult result = restClient.getRequest(URL, headers,
				new PrismaRestResponseDecoder<String>(
						String.class), null);
		
		if (result.getStatus() == Status.OK) {
			return true;
		} else {
			//TODO capire cosa non ha funzionato
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
	
}
