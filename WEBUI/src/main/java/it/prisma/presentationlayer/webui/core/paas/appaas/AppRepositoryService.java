package it.prisma.presentationlayer.webui.core.paas.appaas;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.response.ApplicationRepositoryEntryRepresentation;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.request.AddAppSrcFileRequest;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.response.AddAppSrcFileResponse;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AppRepositoryService {

	static final Logger LOG = LogManager.getLogger(AppRepositoryService.class
			.getName());

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	public BootstrapTable<ApplicationRepositoryEntryRepresentation> getPrivateApps(
			Long workgroupId, RestWSParamsContainer params) throws Exception {
		
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		BootstrapTable<ApplicationRepositoryEntryRepresentation> appRepo = new BootstrapTable<ApplicationRepositoryEntryRepresentation>();
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
				
		appRepo.setEnvironments(prismaBizAPIClient.getPrivateAppSrcFiles(workgroupId, params, meta, authToken.getTokenId()));
		appRepo.setTotal(meta.getMeta().getPagination().getTotalCount());
		return appRepo;
	}

	public BootstrapTable<ApplicationRepositoryEntryRepresentation> getPublicApps(RestWSParamsContainer params) throws Exception {
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		BootstrapTable<ApplicationRepositoryEntryRepresentation> appRepo = new BootstrapTable<ApplicationRepositoryEntryRepresentation>();
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		appRepo.setEnvironments(prismaBizAPIClient.getPublicAppSrcFiles(params, meta, authToken.getTokenId()));
		appRepo.setTotal(meta.getMeta().getPagination().getTotalCount());
		return appRepo;
	}
	
	public AddAppSrcFileResponse uploadAppSRCFileFromURL(AddAppSrcFileRequest request) throws Exception{
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.uploadAppSRCFileFromURL(request, authToken.getTokenId());
	}

}
