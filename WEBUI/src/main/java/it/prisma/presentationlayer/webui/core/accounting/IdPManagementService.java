package it.prisma.presentationlayer.webui.core.accounting;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.accounting.organizations.IdentityProviderRepresentation;
import it.prisma.domain.dsl.prisma.AccountingErrorCode;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupOperationException;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class IdPManagementService {

	// Logger
	static final Logger LOG = LogManager
			.getLogger(IdPManagementService.class);

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;


	public IdentityProviderRepresentation getIdpInfoWithIdPId(final Long idpID) throws IOException
	{
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();			

			return prismaBizAPIClient
					.getIdpInfoWithIdPId(idpID, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	
	public IdentityProviderRepresentation getIdpInfoWithEntityID(final String entityID) throws IOException
	{
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();			

			return prismaBizAPIClient
					.getIdpInfoWithEntityID(entityID, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}


}
