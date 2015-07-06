package it.prisma.businesslayer.bizlib.paas.services.smsaas;

import it.prisma.businesslayer.bizlib.paas.services.emailaas.EmailHelper;
import it.prisma.businesslayer.bizlib.paas.services.smsaas.playsms.PojoCredit;
import it.prisma.businesslayer.bizlib.paas.services.smsaas.playsms.PojoSendSms;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.exceptions.smsaas.NotificationException;
import it.prisma.businesslayer.exceptions.smsaas.SMSGenericException;
import it.prisma.domain.dsl.exceptions.UserNotFoundException;
import it.prisma.domain.dsl.paas.services.emailaas.EmailMessageRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.IsActiveStatus;
import it.prisma.domain.dsl.paas.services.smsaas.SMSMessageRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.SMSNotificationsRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.history.History;
import it.prisma.utils.mailer.exceptions.EmailSyntaxException;
import it.prisma.utils.misc.PropertiesReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for sms operation
 * 
 * @author l.calicchio
 * 
 */
@Stateless
public class SmsHelper {

	// Initial credit of a user
	final static BigDecimal INITIALCREDIT = new BigDecimal(1000000000);

	private Properties properties;

	// Ip address of playsms server
	String IPPLAYSMS;
	// ADMIN playsms used to send notification about thresholds
	String ADMIN;
	String ADMINTOKEN;
	// %0D%0A is used as new line!!!!
	String ADMINMSG;
	String ADMINMSGMONTHLY;
	int HOURSDIFFERENCE;

	// ADMIN parameters used to send notification email
	EmailMessageRepresentation requestEmail;

	@Inject
	private EnvironmentConfig envConfig;
	
	@Inject
	private EmailHelper mailHelper;

	DatabaseHelper database;
	
	@PostConstruct
	private void init() throws IOException {
		
		this.properties = PropertiesReader.read("smsServer.properties");
		requestEmail = new EmailMessageRepresentation();
		requestEmail.setPassword(properties.getProperty("ADMINEMAILPASSWORD"));
		requestEmail.setSubject(properties.getProperty("SUBJECT"));
		requestEmail.setSender(properties.getProperty("ADMINEMAIL"));
		
		this.IPPLAYSMS = envConfig
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_PLAYSMS_IP);
		this.HOURSDIFFERENCE = Integer.parseInt(properties.getProperty("HOURSDIFFERENCE"));
		this.ADMIN = properties.getProperty("ADMIN");
		this.ADMINTOKEN = envConfig.getSvcEndpointProperty(EnvironmentConfig.SVCEP_PLAYSMS_ADMIN_TOKEN);
		this.ADMINMSG = properties.getProperty("ADMINMSG");
		this.ADMINMSGMONTHLY = properties.getProperty("ADMINMSGMONTHLY");
		// Object for database operation
		database = new DatabaseHelper(
				envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_PLAYSMS_DB_USER),
				envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_PLAYSMS_DB_PASSWORD),
				envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_PLAYSMS_DB_URL));
	}


	/**
	 * Return user's token,it's necessary for playsms operation
	 * 
	 * @param user
	 *            : name of a user
	 * @return token of a user
	 * @throws IOException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws UserNotFoundException 
	 */
	public String getToken(String user) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SQLException, UserNotFoundException {

		String query = "select token from playsms_tblUser where username ='"
				+ user + "'";
		return database.getTokenDatabase(query);

	}

	/*
	 * public static boolean exist(String user) throws ClassNotFoundException,
	 * InstantiationException, IllegalAccessException, SQLException, IOException
	 * { Connection db=Database.DatabaseConnection(USERDB, PASSWORDDB);
	 * 
	 * String query =
	 * "select * from playsms_tblUser where username ='"+user+"'"; // create the
	 * java statement Statement st = db.createStatement();
	 * 
	 * // execute the query, and get a java resultset boolean
	 * result=st.executeQuery(query).next(); db.close(); return result;
	 * 
	 * }
	 */

	/**
	 * Verify if a user(tenant) is enabled to send sms
	 * 
	 * @param user
	 * @return
	 * @return if the service is active, if it isn't active (when a user disable
	 *         its service) and if the user doesn't exist
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws IOException
	 */
	public IsActiveStatus isActive(String user) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SQLException,
			IOException {

		String query = "select enable_webservices from playsms_tblUser where username ='"
				+ user + "'";
		// Query to database
		int i = database.activeInDatabase(query);
		if (i == 1)
			return IsActiveStatus.SERVICE_ON;
		else if (i == 0)
			return IsActiveStatus.SERVICE_OFF;
		else
			return IsActiveStatus.NOT_FOUND_USER;
	}

	/**
	 * Add user to playsms database
	 * 
	 * @param user
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws IOException
	 * @throws UserNotFoundException 
	 */
	public void addUser(String user) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SQLException,
			IOException, UserNotFoundException {

		// If the user does not exist, it must be created
		if (isActive(user).equals(IsActiveStatus.NOT_FOUND_USER)) {
			// Ottengo la data e l'ora in modo da sapere la data e orario di
			// creazione

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // creo
			// l'oggetto
			String dataStr = sdf.format(new Date());

			// Preparo la query da inviare al database
			String query = "INSERT INTO playsms_tblUser(parent_uid, status, username, password, token, enable_webservices, webservices_ip, credit, datetime_timezone, language_module, register_datetime, lastupdate_datetime)"
					+ "VALUES (?,?,?,MD5(?),MD5(?),?,?,?,?,?,?,?)";
			// Effettuo la query
			// Inserisco lo uid e lo user anche in playsms_tblNotifyInfo
			database.addDatabase(user, INITIALCREDIT, dataStr, query);
		}
		// case in which the user exists and wants to reactivate the service.The
		// case in which the user already has the service is not considered
		// since the webui does not allow the activation to a user already
		// active
		else {
			// enable sms service of user
			modifierUser(user, 1);
		}
	}

	/**
	 * Modify a user's fields (enable and disable service)
	 * 
	 * @param user
	 * @param webservice
	 *            : 1 activates the service, 0 disables
	 * @throws IOException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws UserNotFoundException 
	 */
	public void modifierUser(String user, int webservice)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, UserNotFoundException {
		// Obtain current data and time
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // creo
		// l'oggetto
		String dataStr = sdf.format(new Date());
		String query = "update playsms_tblUser set enable_webservices = ?, lastupdate_datetime = ? where username = ?";
		database.modifierDatabase(query, user, webservice, dataStr);
	}

	/**
	 * Disable a user's sms service
	 * 
	 * @param user
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws UserNotFoundException 
	 * @throws IOException
	 */
	public boolean disableUser(String user) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SQLException, UserNotFoundException, IOException {
		modifierUser(user, 0);
		if (isActive(user)==IsActiveStatus.SERVICE_OFF)
			return true;
		else return false;

	}

	/**
	 * get user's credit
	 * 
	 * @param user
	 * @return credit
	 * @throws IOException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public BigDecimal getCredit(String user) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		PojoCredit pojo;

		String token = getToken(user);

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		pojo = mapper.readValue(new URL("http://" + IPPLAYSMS
				+ "/playsms/index.php?app=ws&u=" + user + "&h=" + token
				+ "&op=cr"), PojoCredit.class);
		// playsms return the residue credit, which i do difference from
		// INITIALCREDIT
		return INITIALCREDIT.subtract(pojo.getCredit());
	}

	/**
	 * Sms sent from a user
	 * 
	 * @param user
	 * @param filter
	 *            : 0 no filter, 1 filter from destination, 2 filter from date
	 *            (YYYY-MM-DD hh:mm:ss but can be enough one of this parameter
	 *            because the query is %data%), 3 filter from text, 4 filter
	 *            from sender.
	 * @param filterData
	 *            : parameter of a filter
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public History getSMSHistory(String user, boolean filter,
			String filterData, int offset, int limit)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SQLException, JsonParseException,
			JsonMappingException, MalformedURLException, IOException {
		if (!filter) {
			filterData = "";
		}
		String queryCount = "SELECT COUNT(*) FROM playsms.playsms_tblSMSOutgoing WHERE uid = ? AND flag_deleted = 0 AND ( p_msg LIKE ? OR p_dst LIKE ? OR p_datetime LIKE ? )";
		String query = "SELECT p_datetime, p_dst, p_msg FROM playsms.playsms_tblSMSOutgoing WHERE uid = ? AND flag_deleted = 0 AND ( p_msg LIKE ? OR p_dst LIKE ? OR p_datetime LIKE ?) ORDER BY p_datetime DESC limit ? offset ? ;";
		return database.getSmsHistory(queryCount, query, user, filterData,
				offset, limit , HOURSDIFFERENCE);

	}

	/**
	 * Send sms
	 * 
	 * @param user
	 * @param token
	 * @param text
	 * @param num
	 * @param scheduling
	 *            : boolen for choose a date of dispatch(if is true)
	 * @param sendDate
	 *            : date of dispatch(if scheduling is true)
	 * @param from
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws MessagingException
	 * @throws EmailSyntaxException
	 * @throws NoSuchAlgorithmException
	 * @throws NotificationException 
	 */
	public PojoSendSms sendSms(String user, SMSMessageRepresentation request)
			throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, EmailSyntaxException,
			MessagingException, NoSuchAlgorithmException, NotificationException {

		ObjectMapper mapper = new ObjectMapper();
		PojoSendSms pojo = new PojoSendSms();
		URL url;
		SimpleDateFormat sdf;
		String dataStr;

		// consente di non ottenere errori in caso di sottoutilizzo dei campi di
		// pojo
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		// permette di avere una lista di un solo elemento
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
				true);

		// send not scheduled sms
		if (request.getScheduling() == false) {
			URL url1 = new URL("http://" + IPPLAYSMS
					+ "/playsms/index.php?app=ws&u=" + user + "&h="
					+ request.getPassword() + "&op=pv&to=" + request.getNum()
					+ "&msg=" + request.getFrom() + ":%0D%0A"
					+ URLEncoder.encode(request.getText(), "UTF-8"));
			pojo = mapper.readValue(url1, PojoSendSms.class);
			// send scheduled(to sendDate date) sms
		} else {
			try {
				Date sendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request.getSendDate());
				if( (new Date()).after(sendDate) ){
					throw new SMSGenericException("Invalid scheduled time: date is older");
				}
			} catch (ParseException e) {
				throw new SMSGenericException("Date format invalid; please use yyyy-MM-dd HH:mm:ss format", e);
			}
			url = new URL("http://" + IPPLAYSMS
					+ "/playsms/index.php?app=ws&u=" + user + "&h="
					+ request.getPassword() + "&op=pv&to=" + request.getNum()
					+ "&msg=" + request.getFrom() + ":%0D%0A" 
					+ URLEncoder.encode(request.getText(), "UTF-8")
					+ "&schedule=" + request.getSendDate().replaceAll(" ", "+"));
			pojo = mapper.readValue(url, PojoSendSms.class);
		}
		// if there were not errors in sending the message check notification
		// threshold
		if (pojo.getError_string() == null) {
			// Obtain uid of user for the use of playsms_tblSMSOutgoing
			String queryUid = "SELECT uid FROM playsms_tblUser WHERE username='"
					+ user + "'";
			// Obtain db notifications values
			SMSNotificationsRepresentation info = getNotify(user);
			// Verify if day threshold is active
			if (info.getIsDaily()) {
				int uid = database.executeQueryInt(queryUid);
				// Obtain current data and time
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				dataStr = sdf.format(new Date());
				// Query to get the number of daily sms for one user
				String queryCountSms = "SELECT COUNT(*) FROM playsms_tblSMSOutgoing WHERE p_datetime LIKE '"
						+ dataStr + "%' AND uid='" + uid + "'";
				// check if the number of daily sms is greater then setted
				// threshold
				if (database.executeQueryInt(queryCountSms) >= info
						.getThresholdDay()) {
					// check if the user wants sms daily notifications
					if (info.getIsDailySms()) {
						// send not scheduled sms
						if (request.getScheduling() == false) {
							
							url = new URL("http://" + IPPLAYSMS
									+ "/playsms/index.php?app=ws&u=" + ADMIN
									+ "&h=" + ADMINTOKEN + "&op=pv&to="
									+ info.getDailySms() + "&msg=" 
									+ URLEncoder.encode(ADMINMSG, "UTF-8") + "%0D%0A");
							mapper.readValue(url, PojoSendSms.class);
							// send scheduled sms
						} else {
							url = new URL("http://"
									+ IPPLAYSMS
									+ "/playsms/index.php?app=ws&u="
									+ ADMIN
									+ "&h="
									+ ADMINTOKEN
									+ "&op=pv&to="
									+ info.getDailySms()
									+ "&msg="
									+ URLEncoder.encode(ADMINMSG, "UTF-8")
									+ "&schedule="
									+ request.getSendDate()
											.replaceAll(" ", "+") + "%0D%0A");
							mapper.readValue(url, PojoSendSms.class);
						}
					}
					// Invio email all'indirizzo presente in
					// playsms_tblNotifyInfo
					// per l'account se sceglie l'email
					// check if user wants daily email notifications
					if (info.getIsDailyEmail()) {						
						requestEmail.setBody(properties
								.getProperty("ADMINMSGEMAIL"));
						requestEmail.setRecipient(info.getDailyEmail());
						mailHelper.sendEmailaas(requestEmail);
					}
				}
			}

			// Check if month notifications are active
			if (info.getIsMonth()) {
				int uid = database.executeQueryInt(queryUid);
				// Obtain current date
				sdf = new SimpleDateFormat("yyyy-MM");
				dataStr = sdf.format(new Date());
				// Preparo la query da inviare al database per recuperare il
				// numero
				// di sms mensili
				String queryCountSms = "SELECT COUNT(*) FROM playsms_tblSMSOutgoing WHERE p_datetime LIKE '"
						+ dataStr + "%' AND uid='" + uid + "'";
				if (database.executeQueryInt(queryCountSms) >= info
						.getThresholdMonth()) {
					if (info.getIsMonthSms()) {
						if (request.getScheduling() == false) {
							url = new URL("http://" + IPPLAYSMS
									+ "/playsms/index.php?app=ws&u=" + ADMIN
									+ "&h=" + ADMINTOKEN + "&op=pv&to="
									+ info.getMonthSms() + "&msg="
									+ URLEncoder.encode(ADMINMSGMONTHLY, "UTF-8")
									+ "%0D%0A");
							mapper.readValue(url, PojoSendSms.class);
						} else {
							url = new URL("http://"
									+ IPPLAYSMS
									+ "/playsms/index.php?app=ws&u="
									+ ADMIN
									+ "&h="
									+ ADMINTOKEN
									+ "&op=pv&to="
									+ info.getMonthSms()
									+ "&msg="
									+ URLEncoder.encode(ADMINMSGMONTHLY, "UTF-8")
									+ "&schedule="
									+ request.getSendDate()
											.replaceAll(" ", "+") + "%0D%0A");
							mapper.readValue(url, PojoSendSms.class);
						}
					}
					// Invio email all'indirizzo presente in
					// playsms_tblNotifyInfo
					// per
					// l'account se sceglie l'email
					if (info.getIsMonthEmail()) {						
						requestEmail.setRecipient(info.getMonthEmail());
						requestEmail.setBody(properties
								.getProperty("ADMINMSGMONTHLYEMAIL"));
						mailHelper.sendEmailaas(requestEmail);
					}
				}
			}
		}
		return pojo;
	}

	/**
	 * Set user's notifications info
	 * 
	 * @param user
	 * @param request
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws UserNotFoundException 
	 */
	public void setDailyNotifications(String user,
			SMSNotificationsRepresentation request)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, UserNotFoundException {

		String query = "update playsms_tblNotifyInfo set sms_gg = ?, email_gg=?, threshold_gg=? where username = ?";

		if (request.getIsDaily()) {
			if (request.getIsDailyEmail() && request.getIsDailySms())
				database.setNotifications(query, user, request.getDailySms(),
						request.getDailyEmail(), request.getThresholdDay());
			else if (request.getIsDailySms())
				database.setNotifications(query, user, request.getDailySms(),
						"", request.getThresholdDay());
			else if (request.getIsDailyEmail())
				database.setNotifications(query, user, "",
						request.getDailyEmail(), request.getThresholdDay());
			else
				database.setNotifications(query, user, "", "", 0);
		} else {
			database.setNotifications(query, user, "", "", 0);
		}

	}

	/**
	 * Set user's notifications info
	 * 
	 * @param user
	 * @param request
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws UserNotFoundException 
	 */
	public void setMonthlyNotifications(String user,
			SMSNotificationsRepresentation request)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, UserNotFoundException {

		String query = "update playsms_tblNotifyInfo set sms_mm = ?, email_mm=?, threshold_mm=? where username = ?";

		if (request.getIsMonth()) {
			if (request.getIsMonthEmail() && request.getIsMonthSms())
				database.setNotifications(query, user, request.getMonthSms(),
						request.getMonthEmail(), request.getThresholdMonth());
			else if (request.getIsMonthSms())
				database.setNotifications(query, user, request.getMonthSms(),
						"", request.getThresholdMonth());
			else if (request.getIsMonthEmail())
				database.setNotifications(query, user, "",
						request.getMonthEmail(), request.getThresholdMonth());
			else
				database.setNotifications(query, user, "", "", 0);
		} else {
			database.setNotifications(query, user, "", "", 0);
		}

	}

	/**
	 * Get notifications info
	 * 
	 * @param user
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws NotificationException 
	 */
	public SMSNotificationsRepresentation getNotify(String user)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, NotificationException {
		String query = "SELECT * FROM playsms_tblNotifyInfo WHERE username='"
				+ user + "'";
		SMSNotificationsRepresentation notification = database.getNotify(query,
				user);
		notification.setIsMonthEmail(!notification.getMonthEmail().isEmpty());
		notification.setIsDailyEmail(!notification.getDailyEmail().isEmpty());
		notification.setIsMonthSms(!notification.getMonthSms().isEmpty());
		notification.setIsDailySms(!notification.getDailySms().isEmpty());
		if (notification.getIsDailyEmail() || notification.getIsDailySms())
			notification.setIsDaily(true);
		else
			notification.setIsDaily(false);
		if (notification.getIsMonthEmail() || notification.getIsMonthSms())
			notification.setIsMonth(true);
		else
			notification.setIsMonth(false);
		return notification;
	}

	/**
	 * Change the user's password
	 * 
	 * @param user
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws UserNotFoundException 
	 */
	public String changePassword(String user) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException, UserNotFoundException {

		String password = RandomPassword.generate();
		String query = "update playsms_tblUser set password = MD5(?), token = MD5(?) where username = ?  ";
		database.changePassword(query, password, user);

		return this.getToken(user);
	}
}