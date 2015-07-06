package it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer;

import it.prisma.domain.dsl.iaas.openstack.identity.response.Endpoint;

import java.util.List;

/**
 * This class contains Prisma BizLib Rest API requests implementation.
 * 
 * @author l.biava
 * 
 */
public interface PrismaBizMasterAPIClient {

	public AppRepoAPIClient getAppRepoClient();
}