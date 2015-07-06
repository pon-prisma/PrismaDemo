package it.prisma.businesslayer.bizws.paas.services.emailaas;

import it.prisma.businesslayer.bizlib.paas.services.emailaas.EmailHelper;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.exceptions.email.DomainNotFoundException;
import it.prisma.domain.dsl.paas.services.emailaas.Credential;
import it.prisma.domain.dsl.paas.services.emailaas.EmailErrorCode;
import it.prisma.domain.dsl.paas.services.emailaas.EmailInfoRepresentation;
import it.prisma.domain.dsl.paas.services.emailaas.EmailMessageRepresentation;
import it.prisma.domain.dsl.paas.services.emailaas.history.RowEmail;
import it.prisma.utils.mailer.exceptions.EmailSyntaxException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJB;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/EMAILaaS")
public class EMAILaaSWS extends BaseWS implements Serializable {

	@EJB
	private EmailHelper helper;
	private static final long serialVersionUID = 1L;
	public static Logger prismaLog = LogManager.getLogger(EMAILaaSWS.class);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/sendEmail")
	public Response sendEmail(EmailMessageRepresentation request)  {

		try {
			// Send email
			helper.sendEmailaas(request);
			return handleResult("Email inviata");

		} catch (EmailSyntaxException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					EmailErrorCode.EMAIL_SYNTAX_EXCEPTION, ejbe);
		}catch (AuthenticationFailedException ejbe){
			return handleError(Status.INTERNAL_SERVER_ERROR,
					EmailErrorCode.AUTHENTICATION_FAILED_EXCEPTION, ejbe);
		} catch (MessagingException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					EmailErrorCode.MESSAGING_EXCEPTION, ejbe);
		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					EmailErrorCode.SENDER_EXCEPTION, ejbe);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createEmailAccount/{userPrismaId}")
	public Response createEmailAccount(@PathParam("userPrismaId") Long userPrismaId, EmailInfoRepresentation request) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		try {
			return handleResult(helper.createEmailAccount(userPrismaId, request));
		}catch (DomainNotFoundException ejbe) {
			return handleError(Status.NOT_FOUND,
					EmailErrorCode.DOMAIN_NOT_FOUND_EXCEPTION, ejbe);
		} catch (EmailSyntaxException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					EmailErrorCode.GENERAL_EMAIL_EXCEPTION, ejbe);
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deleteEmailAccount/{email}/{password}")
	public Response deleteEmailAccount(@PathParam("email") String email, @PathParam("password") String password){
		try{
			return handleResult(helper.deleteEmailAccount(email, password));
		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					EmailErrorCode.GENERAL_EMAIL_EXCEPTION, ejbe);
		} 		
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updatePassword")
	public Response updatePassword(Credential credential){
		try {
			return handleResult(helper.updatePassword(credential));
		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					EmailErrorCode.GENERAL_EMAIL_EXCEPTION, ejbe);
		} 		

	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getEmailAccountList/{userPrismaId}")
	@PrismaWrapper(restParamsEnabled = true)
	public List<RowEmail> getEmailAccountList(@PathParam("userPrismaId") Long userPrismaId){
		try{

			//return handleResult(helper.getEmailAccountList(userPrismaId,0L, Long.MAX_VALUE, 1000L));
			return helper.getEmailAccountList(userPrismaId);
		} catch (Exception ejbe) {
			throw new PrismaWrapperException(ejbe);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getEmailDomainList")
	public Response getEmailDomainList(){
		try{
			return handleResult(helper.getEmailDomainList());
		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					EmailErrorCode.GENERAL_EMAIL_EXCEPTION, ejbe);
		}
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/isEmailAccountExists/{email}")
	public Response isEmailAccountExists(@PathParam("email") String email){
		try{
			return handleResult(helper.isEmailAccountExists(email));
		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					EmailErrorCode.GENERAL_EMAIL_EXCEPTION, ejbe);
		}
	}


}