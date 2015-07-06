package it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;

public interface PrismaAPIClient {

	    
//		/**
//		 * Gets the supported services.  A set of ServiceTypes will be returned identifying the OpenStack services installed and supported
//		 *
//		 * @return the supported services
//		 */
//		Set<ServiceType> getSupportedServices();
//		
//		/**
//		 * Determines if the Compute (Nova) service is supported
//		 *
//		 * @return true, if supports compute
//		 */
//		boolean supportsCompute();
		
		/**
		 * Gets the token that was assigned during authorization
		 *
		 * @return the authentication token
		 */
		AuthTokenRepresentation getToken();
		
		/**
		 * Gets the current endpoint of the Identity service
		 *
		 * @return the endpoint
		 */
		String getEndpoint();
		
		/**
		 * Returns the Identity Service API
		 *
		 * @return the identity service
		 */
		AppRepoAPIClient appRepo();
		
	}

