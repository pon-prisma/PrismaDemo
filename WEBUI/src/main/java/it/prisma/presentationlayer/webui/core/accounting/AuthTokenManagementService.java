package it.prisma.presentationlayer.webui.core.accounting;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.prisma.AccountingErrorCode;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.prisma.idp.PrismaIdPAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenManagementService {


	@Value("${DEFAULT_AUTH_TOKEN}")
	String DEFAULT_AUTH_TOKEN;

	// Logger
	static final Logger LOG = LogManager.getLogger(AuthTokenManagementService.class);

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	@Autowired
	private PrismaIdPAPIClient prismaIdPAPIClient;


	public AuthTokenRepresentation authenticateUserOnBusinessLayer(final String userAccountId) throws Exception {
		try {

			AuthTokenRepresentation authToken = prismaBizAPIClient.getAvailableAuthTokenForUser(userAccountId, DEFAULT_AUTH_TOKEN);

			if (authToken == null)
				authToken = prismaBizAPIClient.generateNewSessionAuthTokenForUser(userAccountId, DEFAULT_AUTH_TOKEN);
			
			LOG.debug("authenticateUserOnBusinessLayer: " + authToken.getTokenId());
			return authToken;

		} catch (APIErrorException e) { 
			if (e.getAPIError().getErrorCode() == OrchestratorErrorCode.ORC_ITEM_NOT_FOUND.getCode()) {
				return prismaBizAPIClient.generateNewSessionAuthTokenForUser(userAccountId, DEFAULT_AUTH_TOKEN);
			}
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode()) {
				LOG.info("Bad request.");
				throw new BadCredentialsException(
						"Provided credentials are invalid.");
			} else {
				LOG.info("Internal server error");
				// TODO: create a proper exception
				throw new Exception("Authentication error on the BL");

			}
		} catch (ServerErrorResponseException e) {
			LOG.info("Connection error");
			// TODO: create a proper exception
			throw new Exception("Connection error");
		} catch (NoMappingModelFoundException | MappingException 
				| RestClientException e) {
			LOG.info("Generic data error");
			throw new IOException();
		}
	}

	public AuthTokenRepresentation createToken(final String userAccountId) throws Exception
	{
		try {
			return	 prismaBizAPIClient.generateNewAuthTokenForUser(userAccountId, DEFAULT_AUTH_TOKEN);						
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode()) {
				LOG.info("Bad request.");
				throw new BadCredentialsException(
						"Provided credentials are invalid.");
			} else {
				LOG.info("Internal server error");
				// TODO: create a proper exception
				throw new Exception("Authentication error on the BL");

			}
		} catch (ServerErrorResponseException e) {
			LOG.info("Connection error");
			// TODO: create a proper exception
			throw new Exception("Connection error");
		} catch (NoMappingModelFoundException | MappingException 
				| RestClientException e) {
			LOG.info("Generic data error");
			throw new IOException();
		}


	}

	public boolean revokeAuthTokenForUser(final String tokenId) throws Exception
	{
		try {

			return prismaBizAPIClient.deleteAuthTokenForUser(tokenId, DEFAULT_AUTH_TOKEN);

		} catch (ServerErrorResponseException e) {
			LOG.info("Connection error");
			// TODO: create a proper exception
			throw new Exception("Connection error");
		} catch (NoMappingModelFoundException | MappingException 
				| RestClientException e) {
			LOG.info("Generic data error");
			throw new IOException();
		}
	}



	public BootstrapTable<AuthTokenRepresentation> getAuthTokenForUser(final Long userAccountId,RestWSParamsContainer params) throws Exception {
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		BootstrapTable<AuthTokenRepresentation> tokens = new BootstrapTable<AuthTokenRepresentation>();
		tokens.setEnvironments(prismaBizAPIClient.getAuthTokenForUser(userAccountId,
				params, meta,DEFAULT_AUTH_TOKEN));
		tokens.setTotal(meta.getMeta().getPagination().getTotalCount());
		return tokens;
	}

}
