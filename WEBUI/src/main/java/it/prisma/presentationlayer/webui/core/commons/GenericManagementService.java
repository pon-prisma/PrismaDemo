package it.prisma.presentationlayer.webui.core.commons;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;
import it.prisma.domain.dsl.paas.services.event.Events;
import it.prisma.domain.dsl.paas.services.event.Row;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.request.AddAppSrcFileRequest;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.response.AddAppSrcFileResponse;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GenericManagementService<T> {

	static final Logger LOG = LogManager.getLogger(GenericManagementService.class.getName());

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	public boolean startAndStopVM(String workgroup, String serviceType, String service, String id, String action) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

		return prismaBizAPIClient.startAndStopVM(workgroup, serviceType, service, id, action, authToken.getTokenId());
	
	}

	public Events getEvents(String service, String paasServiceType, Long serviceId, RestWSParamsContainer params) {

		try {
			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			
			List<PaaSServiceEventRepresentation> list = prismaBizAPIClient.getPaaSServiceEvents("1", service, paasServiceType,
							serviceId, params, authToken.getTokenId(), meta);
	
			List<Row> rows = new ArrayList<Row>();

			for (PaaSServiceEventRepresentation serviceEvent : list) {
				Row row = new Row();
				row.setDate(String.valueOf(serviceEvent.getCreatedAt().getTime()));
				row.setLevel(serviceEvent.getType());
				row.setDescription(serviceEvent.getDetails());
				rows.add(row);
			}

			Events e = new Events();
			e.setTotal(meta.getMeta().getPagination().getTotalCount().intValue());
			e.setRows(rows);

			return e;

		} catch (Exception e) {
			LOG.error("Exception getting events : " + e);
		}
		return null;
	}

	public AddAppSrcFileResponse uploadFile(AddAppSrcFileRequest request, File file) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.uploadAppSRCFile(request, file, authToken.getTokenId());
	}

	public void deleteService(GenericPaaSServiceRepresentation paaSServiceRepresentation)
			throws IllegalArgumentException, APIErrorException, RestClientException, NoMappingModelFoundException,
			MappingException, ServerErrorResponseException {

			AuthTokenRepresentation authToken = ((PrismaUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal())
					.getUserToken();
			prismaBizAPIClient.undeployPaaSService(paaSServiceRepresentation, authToken.getTokenId());
	}

	@Cacheable("name")
	public boolean checkNameAvailable(Long workgroup, String name) {
		try {
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			LOG.debug(authToken.getTokenId());
			return prismaBizAPIClient.checkNameAvailable(workgroup, name, authToken.getTokenId());
		} catch (Exception e) {
			LOG.error("Exception checking service name: " + e);
		}
		return false;
	}
	
	@Cacheable("dns")
	public boolean checkDNSAvailable(Long workgroup, String dns) {
		try {
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			
			return prismaBizAPIClient.checkDNSAvailable(workgroup, dns, authToken.getTokenId());
		} catch (Exception e) {
			LOG.error("Exception checking dns name: " + e);
		}
		return false;
	}

}
