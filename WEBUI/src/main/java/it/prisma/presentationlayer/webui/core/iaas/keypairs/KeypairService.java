package it.prisma.presentationlayer.webui.core.iaas.keypairs;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRepresentation;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRequest;
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
public class KeypairService {

	static final Logger LOG = LogManager.getLogger(KeypairService.class
			.getName());

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	/**
	 * 
	 * Usato nella creazione delle vm
	 * 
	 */
	public List<KeypairRepresentation> getAllkeypairs(Long workgroupId) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.getAllkeypairs(workgroupId, authToken.getTokenId());
	}

	public BootstrapTable<KeypairRepresentation> getAllkeypairs(
			Long workgroupId, RestWSParamsContainer params)
			throws Exception {
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		BootstrapTable<KeypairRepresentation> keys = new BootstrapTable<KeypairRepresentation>();
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		keys.setEnvironments(prismaBizAPIClient.getAllkeypairs(workgroupId,
				params, meta, authToken.getTokenId()));
		keys.setTotal(meta.getMeta().getPagination().getTotalCount());
		return keys;
	}

	public boolean deleteKeypair(String name, Long workgroupId)
			throws JsonParseException, JsonMappingException, APIErrorException,
			MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, RestClientException, IOException {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.deleteKeypair(name, workgroupId, authToken.getTokenId());
	}

	public KeypairRepresentation importKeypair(KeypairRequest keypair,
			Long workgroupId) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.importKeypair(keypair, workgroupId, authToken.getTokenId());
	}

	public KeypairRepresentation createKeypair(String name, Long workgroupId) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.createKeypair(name, workgroupId, authToken.getTokenId());
	}
}
