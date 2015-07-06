package it.prisma.presentationlayer.webui.core.organization;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.accounting.organizations.OrganizationRepresentation;
import it.prisma.domain.dsl.prisma.OrganizationErrorCode;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrganizationManagementService {

	
	// Logger
	static final Logger LOG = LogManager.getLogger(OrganizationManagementService.class);

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

		
	public OrganizationRepresentation getOrganizationById(final String organizationId) throws Exception
	{
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();			

			OrganizationRepresentation organization = prismaBizAPIClient.getOrganizationById(organizationId, authToken.getTokenId());
			
			return organization;
			
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == OrganizationErrorCode.ORG_BAD_REQUEST
					.getCode()) {
				LOG.info("Bad request.");
				throw new BadCredentialsException(
						"Provided organization ID is invalid.");
			} else {
				LOG.info("Internal server error");
				// TODO: create a proper exception
				throw new Exception("Organization search error on the BL");
				
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

}
