package it.prisma.presentationlayer.webui.core.paas.appaas;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.AppConfigForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class APPaaSManagementService {
	
	static final Logger LOG = LogManager
			.getLogger(APPaaSManagementService.class.getName());
	
	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;	
	
//	public List<APPaaSRepresentation> getAllApps(Long workgroupID){
//		
//		try {
//			
//			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
//			
//			return prismaBizAPIClient.getAllAPPaaS(workgroupID, authToken.getTokenId());
//		} catch (MappingException | NoMappingModelFoundException
//				| ServerErrorResponseException | APIErrorException
//				| RestClientException | IOException e) {
//			e.printStackTrace();
//			LOG.error("error getting app list of workgroup " + workgroupID.toString() + " " + e.getStackTrace());
//		}
//		return null;
//	}
	
	public BootstrapTable<APPaaSRepresentation> getAllApps(Long workgroupId, RestWSParamsContainer params) throws Exception {
		
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		BootstrapTable<APPaaSRepresentation> list = new BootstrapTable<APPaaSRepresentation>();
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		list.setEnvironments(prismaBizAPIClient.getAllAPPaaS(workgroupId, params, meta, authToken.getTokenId()));
		list.setTotal(meta.getMeta().getPagination().getTotalCount());
		return list;
	}
	
	public APPaaSRepresentation getAppaaS(Long workgroupID,  Long id) throws Exception{
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.getAPPaaS(workgroupID, id, authToken.getTokenId());
	}
	
	
	public List<APPaaSEnvironmentRepresentation> getAppaaSEnv(Long workgroupID, Long id){
		//TODO
//		try {
//			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
//			
//			return prismaBizAPIClient.getAllAPPaaSEnv(workgroupID, id, authToken.getTokenId());
//		} catch (MappingException | NoMappingModelFoundException
//				| ServerErrorResponseException | APIErrorException
//				| RestClientException | IOException e) {
//			e.printStackTrace();
//			LOG.error("error getting app list of workgroup " + workgroupID.toString() + " " + e.getStackTrace());
//		}
		return null;
	}

	public APPaaSRepresentation deploy(APPaaSDeployRequest request) throws Exception {
	
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

		return prismaBizAPIClient.deployPaaSService(request, APPaaSRepresentation.class, authToken.getTokenId());
	}

	public APPaaSRepresentation updateConfiguration(Long workgroupID, Long appId, AppConfigForm appConfigForm) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		APPaaSRepresentation appRepresentation = prismaBizAPIClient.getAPPaaS(workgroupID, appId, authToken.getTokenId());
		appRepresentation.setDescription(appConfigForm.getDescription());
		return prismaBizAPIClient.updateAPPaaS(workgroupID, appId, appRepresentation, authToken.getTokenId());
	}
		
}
