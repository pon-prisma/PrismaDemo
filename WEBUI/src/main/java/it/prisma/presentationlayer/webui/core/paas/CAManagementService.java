package it.prisma.presentationlayer.webui.core.paas;
/**
 * 
 * The service is associated whit the workgroup
 * workgroup is the name of CA User
 *
 */

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.paas.services.caaas.CertificateInfoRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CAManagementService {
		
		static final Logger LOG = LogManager
				.getLogger(CAManagementService.class.getName());
		
		@Autowired
		private PrismaBizAPIClient prismaBizAPIClient;
		
		@Value("${prisma.businesslayer.url}")
		private String businessLayerURL;
		
		public String createCertificate(Long workgroupId, String subjectDN) throws Exception{
			
				AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			
				return prismaBizAPIClient.caCreateCertificate(workgroupId, subjectDN, authToken.getTokenId());
		}
		
		public String revokeCertificate(Long workgroupId, String serialNumber, int reason) throws Exception {
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			
			return prismaBizAPIClient.caRevokeCertificate(workgroupId, serialNumber, reason, authToken.getTokenId());
		}
		
		public String viewCertificate(Long workgroupId, String serialNumber) throws Exception {
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			
			return prismaBizAPIClient.caViewCertificate(workgroupId, serialNumber, authToken.getTokenId());
		}
		
		public BootstrapTable<CertificateInfoRepresentation> allCertificate(long workgroupId,
				RestWSParamsContainer params) throws Exception{
			
			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			BootstrapTable<CertificateInfoRepresentation> cas = new BootstrapTable<CertificateInfoRepresentation>();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			
			cas.setEnvironments(prismaBizAPIClient.caAllCertificate(workgroupId, params, meta, authToken.getTokenId()));
			cas.setTotal(meta.getMeta().getPagination().getTotalCount());
			return cas;
		}
		
		public String downloadCertificate(Long workgroupId, String serialNumber) throws Exception{
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			
			return prismaBizAPIClient.caSaveCertificate(workgroupId, serialNumber, authToken.getTokenId());
	}
}
