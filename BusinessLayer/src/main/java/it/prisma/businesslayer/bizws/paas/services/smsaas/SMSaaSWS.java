package it.prisma.businesslayer.bizws.paas.services.smsaas;

import it.prisma.businesslayer.bizlib.paas.services.smsaas.SmsHelper;
import it.prisma.businesslayer.bizlib.paas.services.smsaas.playsms.PojoSendSms;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.exceptions.smsaas.NotificationException;
import it.prisma.businesslayer.exceptions.smsaas.SMSGenericException;
import it.prisma.domain.dsl.exceptions.UserNotFoundException;
import it.prisma.domain.dsl.paas.services.smsaas.IsActiveStatus;
import it.prisma.domain.dsl.paas.services.smsaas.SMSMessageRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.SMSNotificationsRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.SmsErrorCode;
import it.prisma.domain.dsl.paas.services.smsaas.history.History;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/SMSaas")
public class SMSaaSWS extends BaseWS implements Serializable {
	@Inject
	private SmsHelper Helper;

	private static final long serialVersionUID = 1L;

	public static Logger prismaLog = LogManager.getLogger(SMSaaSWS.class);
	public SMSaaSWS() throws IOException {
		//Helper = new SmsHelper();
	}

	/**
	 * Send sms
	 * @param username: username of playsms user.
	 * @param password: password of username.
	 * @param text: text of message.
	 * @param num: number to send sms.
	 * @param scheduling: true if you want to postpone the send of message else false.
	 * @param sendData: set the data wherein to send the message, otherwise set 0.
	 * @param from: you can set the name of application which send the message. (it is included in the head of text message) 
	 * @return
	 * @throws Exception
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PrismaWrapper
	@Path("/sendSms/{username}")
	public PojoSendSms sendSms(@PathParam("username") String username, SMSMessageRepresentation request) {
		try{
			PojoSendSms pojoSendSms = Helper.sendSms(username, request);
			if(pojoSendSms.getError_string() == null){
				return pojoSendSms;
			} else {
				throw new PrismaWrapperException(new SMSGenericException(pojoSendSms.getError_string()));
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	/**
	 * Check if an user is active or less.
	 * @param username: username of playsms user.
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/isUserActive/{username}")
	public Response isUserActive(@PathParam("username") String username) {

		try {
			IsActiveStatus status = Helper.isActive(username);
			//			switch (status) {
			//			case NOT_FOUND_USER:
			//				return handleNotFoundException("User");
			//			default:
			return handleResult(status.getStatus());
			//			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException | IOException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.ACTIVATION_INFORMATION_EXCEPTION, ejbe);
		}

	}

	/**
	 * Disable an user. So he doesn't send sms or use web ui.
	 * @param username: username of playsms user.
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/disableUser/{username}")
	public Response disableUser(@PathParam("username") String username) {

		try {		
			return handleResult(Helper.disableUser(username));
		}catch (UserNotFoundException ejbe){
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.USER_NOT_FOUND_EXCEPTION, ejbe);
		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.DISABLE_USER_EXCEPTION, ejbe);
		}

	}

	/**
	 * Return the user's token.
	 * @param username: username of playsms user.
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/requestToken/{username}")
	public Response requestToken(@PathParam("username") String username) {
		try {
			String token = Helper.getToken(username);

			return handleResult(token);
		}catch (UserNotFoundException ejbe){
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.USER_NOT_FOUND_EXCEPTION, ejbe);
		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.TOKEN_EXCEPTION, ejbe);
		}
	}

	/**
	 * Active user's sms service.
	 * @param username: username of playsms user.
	 * @return
	 * @throws Exception
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/activeService/{username}")
	public Response activeService(@PathParam("username") String username)
			throws Exception {

		try {
			Helper.addUser(username);

			return handleResult("Utente attivo");

		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.ACTIVE_USER_EXCEPTION, ejbe);
		}
	}

	/**
	 * get user's credit.
	 * @param username: username of playsms user.
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getCredit/{username}")
	public Response getCredit(@PathParam("username") String username) {

		try {
			BigDecimal credit = Helper.getCredit(username);
			return handleResult(credit);

		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.CREDIT_EXCEPTION, ejbe);
		}
	}

	/**
	 * Sms sent from a user
	 * @param username: username of playsms user.
	 * @param filter
	 *            : 0 no filter, 1 filter from destination, 2 filter from data
	 *            (YYYY-MM-DD hh:mm:ss but can be enough one of this parameter because the query is %data%), 3 filter from text,
	 *            4 filter from sender.
	 * @param filterData
	 *            : parameter of a filter
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/smsHistory/{username}/{filter}/{filterData}")
	public Response getSMSHistory(@PathParam("username") String username,
			@PathParam("filter") boolean filter,
			@PathParam("filterData") String filterData, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {

		try {
			History history = Helper.getSMSHistory(username, filter, filterData, offset, limit);
			return handleResult(history);
		} catch (SMSGenericException ejbe) {
			throw new PrismaWrapperException(new SMSGenericException());
		} catch (Exception e ){
			throw new PrismaWrapperException(e);
		}
	}

	/**
	 * Set daily notifications
	 * @param username
	 * @param request
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/setDailyNotifications/{username}")
	public Response setDailyNotifications(@PathParam("username") String username, SMSNotificationsRepresentation request) {

		try {
			Helper.setDailyNotifications(username, request);
			return handleResult("Notifiche settate");
		}catch (UserNotFoundException ejbe){
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.USER_NOT_FOUND_EXCEPTION, ejbe);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.SET_NOTIFY_EXCEPTION, ejbe);
		}
	}

	/**
	 * Set monthly notifications
	 * @param username
	 * @param request
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/setMonthlyNotifications/{username}")
	public Response setMonthlyNotifications(@PathParam("username") String username, SMSNotificationsRepresentation request) {

		try {
			Helper.setMonthlyNotifications(username, request);
			return handleResult("Notifiche settate");
		}catch (UserNotFoundException ejbe){
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.USER_NOT_FOUND_EXCEPTION, ejbe);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.SET_NOTIFY_EXCEPTION, ejbe);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getNotify/{username}")
	public Response getNotify(@PathParam("username") String username) {

		try {		
			return handleResult(Helper.getNotify(username));
		} catch (NotificationException|InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.GET_NOTIFY_EXCEPTION, ejbe);
		}
	}

	/**
	 * Change user's password.
	 * @param username: username of playsms user.
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/changeToken/{username}")
	public Response changeToken(@PathParam("username") String username) {

		try {
			return handleResult(Helper.changePassword(username));
		}catch (UserNotFoundException ejbe){
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.USER_NOT_FOUND_EXCEPTION, ejbe);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					SmsErrorCode.CHANGE_PASSWORD_EXCEPTION, ejbe);
		}
	}
}