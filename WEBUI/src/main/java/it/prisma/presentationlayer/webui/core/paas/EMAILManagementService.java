package it.prisma.presentationlayer.webui.core.paas;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.paas.services.emailaas.Credential;
import it.prisma.domain.dsl.paas.services.emailaas.EmailDomains;
import it.prisma.domain.dsl.paas.services.emailaas.EmailInfoRepresentation;
import it.prisma.domain.dsl.paas.services.emailaas.history.RowEmail;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EMAILManagementService {

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;


	
	public boolean isEmailAccountExists(String email) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.emailIsEmailAccountExists(email, authToken.getTokenId());
	}

	public BootstrapTable<RowEmail> getEmailAccountList(Long userPrismaId, RestWSParamsContainer params) throws Exception {
		
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		BootstrapTable<RowEmail> table = new BootstrapTable<RowEmail>();
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
				
		table.setEnvironments(prismaBizAPIClient.emailGetEmailAccountList(userPrismaId, params, meta, authToken.getTokenId()));
		table.setTotal(meta.getMeta().getPagination().getTotalCount());
		return table;	
	}

	public List<EmailDomains> getEmailDomainList() throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.emailGetEmailDomainList(authToken.getTokenId());
	}

	public boolean updatePassword(Credential credential)
			throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.emailUpdatePassword(credential, authToken.getTokenId());
	}

	public boolean deleteEmailAccount(String email, String password) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.emailDeleteEmailAccount(email, password, authToken.getTokenId());
	}

	public boolean createEmailAccount(Long userPrismaId, EmailInfoRepresentation request) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.emailCreateEmailAccount(userPrismaId, request, authToken.getTokenId());
	}
}
