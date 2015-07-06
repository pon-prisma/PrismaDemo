package it.prisma.presentationlayer.webui.core.iaas.network;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.iaas.network.NetworkRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSPaginationParams;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class NetworkService {

	static final Logger LOG = LogManager.getLogger(NetworkService.class
			.getName());

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	/**
	 * 
	 * No pagination: TEST BEFORE USE
	 * 
	 * @param workgroupId
	 * @param auth_data
	 * @return
	 */
	public List<NetworkRepresentation> getAllNetworks(Long workgroupId) throws JsonParseException, JsonMappingException, APIErrorException, MappingException, NoMappingModelFoundException, ServerErrorResponseException, RestClientException, IOException {
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		RestWSPaginationParams paginationParams = new RestWSPaginationParams(Long.MAX_VALUE, 0L);
		RestWSParamsContainer params = new RestWSParamsContainer(null, paginationParams, null);
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.getAllNetworks(workgroupId, params, meta, authToken.getTokenId());
	}

	public BootstrapTable<NetworkRepresentation> getAllNetworks(long workgroupId,
			RestWSParamsContainer params) throws Exception{
		
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		BootstrapTable<NetworkRepresentation> nets = new BootstrapTable<NetworkRepresentation>();
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		nets.setEnvironments(prismaBizAPIClient.getAllNetworks(workgroupId, params, meta, authToken.getTokenId()));
		nets.setTotal(meta.getMeta().getPagination().getTotalCount());
		return nets;
	}	
}
