/**
 * 
 * The service is associated whit the workgroup
 * workgroup is the name of PlaySMS User
 *
 */


package it.prisma.presentationlayer.webui.core.paas;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.SMSNotificationsRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.history.History;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SMSSManagementService {

	static final Logger LOG = LogManager
			.getLogger(SMSSManagementService.class.getName());

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	@Value("${prisma.businesslayer.url}")
	private String businessLayerURL;

	public String isServiceActive(String workgroupId) throws Exception{
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.smsIsUserActive(workgroupId, authToken.getTokenId());
	}


	public String activeService(String workgroupId) throws Exception{
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		String response = prismaBizAPIClient.smsActiveService(workgroupId, authToken.getTokenId());
		return response;

	}

	public String getToken(String workgroupId) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
				
		return prismaBizAPIClient.smsGetToken(workgroupId, authToken.getTokenId());
	}

	public boolean disableService(String workgroupId) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.smsDisableUser(workgroupId, authToken.getTokenId());
	}

	public String regenerateToken(String workgroupId) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.smsChangeToken(workgroupId, authToken.getTokenId());
	}

	public SMSNotificationsRepresentation setDailyNotifications(String  workgroupId, SMSNotificationsRepresentation request){
		try{
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			
			return prismaBizAPIClient.smsSetDailyNotifications(request, workgroupId, authToken.getTokenId());
		} catch (Exception e){
			LOG.error("Error setting sms notify");
		}
		return null;
	}

	public SMSNotificationsRepresentation setMonthlyNotifications(String  workgroupId, SMSNotificationsRepresentation request){
		try{
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
			
			return prismaBizAPIClient.smsSetMonthlyNotifications(request, workgroupId, authToken.getTokenId());
		} catch (Exception e){
			LOG.error("Error setting sms notify");
		}
		return null;
	}

	public BigDecimal getCredit(String  workgroupId) throws Exception{
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.smsGetCredit(workgroupId, authToken.getTokenId());
	}

	public History getSMSHystory(String workgroupId, String search, int limit, int offset) throws Exception {

		boolean filter = false;
		if(search != null){
			filter = true;
		}
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.getSMSHistory(workgroupId, filter, search, limit, offset, authToken.getTokenId());
	}

	public SMSNotificationsRepresentation getNotifications(String workgroupId) throws Exception {
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		
		return prismaBizAPIClient.smsGetNotifications(workgroupId, authToken.getTokenId());
	}

}
