package it.prisma.businesslayer.bizlib.paas.services.smsaas;

import it.prisma.businesslayer.bizlib.common.exceptions.UserAccountNotFoundException;
import it.prisma.businesslayer.exceptions.smsaas.NotificationException;
import it.prisma.businesslayer.exceptions.smsaas.SMSGenericException;
import it.prisma.domain.dsl.exceptions.UserNotFoundException;
import it.prisma.domain.dsl.paas.services.smsaas.SMSNotificationsRepresentation;
import it.prisma.domain.dsl.paas.services.smsaas.history.History;
import it.prisma.domain.dsl.paas.services.smsaas.history.Row;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

/**
 * Class used for database operation
 * 
 * @author l.calicchio
 * 
 */
public class DatabaseHelper {
	private String userDb;
	private String passwordDb;
	private String ipDatabase;

	/**
	 * Constructor of DatabaseHelper
	 * 
	 * @param userDb
	 *            : User of database Mysql
	 * @param passwordDb
	 *            : Password of database Mysql
	 * @param ipDatabase
	 *            : Ip address of database Mysql
	 */
	public DatabaseHelper(String userDb, String passwordDb, String ipDatabase) {

		this.userDb = userDb;
		this.passwordDb = passwordDb;
		this.ipDatabase = ipDatabase;
	}

	/**
	 * @return Connection to database
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	private Connection DatabaseConnection() throws InstantiationException,
	IllegalAccessException, ClassNotFoundException, SQLException {

		// Load JDBC driver
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		// Return connection to the database
		return DriverManager.getConnection("jdbc:mysql://" + ipDatabase
				+ "/playsms" + "?user=" + userDb + "&password=" + passwordDb);

	}

	/**
	 * Add user to database (tenant so can use sms service)
	 * 
	 * @param user
	 * @param password
	 * @param initialCredit
	 * @param dataStr
	 * @param query
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void addDatabase(String user,
			BigDecimal initialCredit, String dataStr, String query)
					throws InstantiationException, IllegalAccessException,
					ClassNotFoundException, SQLException {
		// open connection to database
		Connection db = DatabaseConnection();

		ResultSet rs;
		String password;
		do{
			password=RandomPassword.generate();
			String queryPsw="SELECT * FROM playsms_tblUser WHERE token=MD5('"+password+"')";
			Statement st = db.createStatement();
			rs=st.executeQuery(queryPsw);
		}while(rs.next());
		// Prepare query to database
		PreparedStatement preparedStmt = db.prepareStatement(query);
		preparedStmt.setInt(1, 1);
		preparedStmt.setInt(2, 4);
		preparedStmt.setString(3, user);
		preparedStmt.setString(4, password);// Token is same of password
		preparedStmt.setString(5, password);
		preparedStmt.setInt(6, 1);
		preparedStmt.setString(7, "127.0.0.1, 192.168.*.*");
		preparedStmt.setBigDecimal(8, initialCredit);
		preparedStmt.setString(9, "+0100");
		preparedStmt.setString(10, "en_US");
		preparedStmt.setString(11, dataStr);// Create date and modify date are
		// same
		preparedStmt.setString(12, dataStr);

		// execute the preparedstatement
		preparedStmt.execute();

		// Inserisco anche in playsms_tblNotifyInfo
		// Prepare query to database playsms_tblNotifyInfo
		PreparedStatement preparedStmtNotify = db
				.prepareStatement("INSERT INTO playsms_tblNotifyInfo(username, sms_gg, email_gg, sms_mm, email_mm, threshold_gg, threshold_mm)"
						+ "VALUES (?,?,?,?,?,?,?)");
		preparedStmtNotify.setString(1, user);
		preparedStmtNotify.setString(2, "");
		preparedStmtNotify.setString(3, "");
		preparedStmtNotify.setString(4, "");
		preparedStmtNotify.setString(5, "");
		preparedStmtNotify.setInt(6, 0);
		preparedStmtNotify.setInt(7, 0);

		// execute the preparedstatement
		preparedStmtNotify.execute();

		// close connection to database
		db.close();
	}

	/**
	 * Verify if a user(tenant) is enabled to send sms
	 * 
	 * @param query
	 * @return 1 if the service is active, 0 if it isn't active (when a user
	 *         disable its service) and -1 if the user don't exist
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws IOException
	 */
	public int activeInDatabase(String query) throws ClassNotFoundException,
	InstantiationException, IllegalAccessException, SQLException,
	IOException {
		// open connection to database
		Connection db = DatabaseConnection();

		Statement st = db.createStatement();
		ResultSet result = st.executeQuery(query);
		int active;
		// execute the query, and get a java resultset
		if (result.next()) {
			active = result.getInt("enable_webservices");
			db.close();
			return active;
		} else {
			db.close();
			return -1;
		}
	}

	/**
	 * Get token of a user
	 * 
	 * @param query
	 * @return token of a user
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws UserNotFoundException 
	 * @throws IOException
	 */
	public String getTokenDatabase(String query) throws ClassNotFoundException,
	InstantiationException, IllegalAccessException, SQLException, UserNotFoundException {

		// open connection to database
		Connection db = DatabaseConnection();
		// create the java statement
		Statement st = db.createStatement();
		ResultSet result = st.executeQuery(query);
		// unnecessary to check whether it exists because there must necessarily
		// since you can only see if writing to the service
		/*
		 * if (result.next()){ String active=result.getString("token");
		 * db.close(); return active; } else{ db.close(); return
		 * "Errore ne recupero del token"; }
		 */
		if (result.next()){
			String active = result.getString("token");
			db.close();
			return active;}
		throw new UserAccountNotFoundException("User account not found.");
	}

	/**
	 * Modifier a field in a database
	 * 
	 * @param query
	 * @param user
	 *            : user to change field
	 * @param webservice
	 *            : 1 if enable 0 if not
	 * @param dataStr
	 *            : corrent date
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws UserNotFoundException 
	 * @throws IOException
	 */
	public void modifierDatabase(String query, String user, int webservice,
			String dataStr) throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, UserNotFoundException {
		// open connection to database
		Connection db = DatabaseConnection();

		PreparedStatement preparedStmt = db.prepareStatement(query);
		preparedStmt.setInt(1, webservice);
		preparedStmt.setString(2, dataStr);
		preparedStmt.setString(3, user);

		// execute the java preparedstatement
		int modified=preparedStmt.executeUpdate();
		db.close();
		if (modified==0)
			throw new UserAccountNotFoundException("User account not found.");
	}

	public void disableNotify(String query, String user, int disableDay,
			int disableMonth) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException, NotificationException {
		// open connection to database
		Connection db = DatabaseConnection();

		PreparedStatement preparedStmt = db.prepareStatement(query);
		preparedStmt.setInt(1, disableDay);
		preparedStmt.setInt(2, disableMonth);
		preparedStmt.setString(3, user);

		// execute the java preparedstatement
		int modified=preparedStmt.executeUpdate();
		db.close();
		if (modified==0)
			throw new NotificationException();
	}

	public void setNotify(String query, String user, int threshold,
			String whereNotify) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException, NotificationException {
		// open connection to database
		Connection db = DatabaseConnection();

		PreparedStatement preparedStmt = db.prepareStatement(query);
		preparedStmt.setString(1, whereNotify);
		preparedStmt.setInt(2, threshold);
		preparedStmt.setString(3, user);

		// execute the java preparedstatement
		int modified=preparedStmt.executeUpdate();
		db.close();
		if (modified==0)
			throw new NotificationException();
	}

	public SMSNotificationsRepresentation getNotify(String query, String user)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, NotificationException {
		// open connection to database
		Connection db = DatabaseConnection();
		// create the java statement
		Statement st = db.createStatement();
		ResultSet result = st.executeQuery(query);
		// unnecessary to check whether it exists because there must necessarily
		// since you can only see if writing to the service
		/*
		 * if (result.next()){ String active=result.getString("token");
		 * db.close(); return active; } else{ db.close(); return
		 * "Errore ne recupero del token"; }
		 */
		if (result.next())
		{
			SMSNotificationsRepresentation notification = new SMSNotificationsRepresentation();
			notification.setThresholdMonth(result.getInt("threshold_mm"));
			notification.setThresholdDay(result.getInt("threshold_gg"));
			notification.setDailyEmail(result.getString("email_gg"));
			notification.setDailySms(result.getString("sms_gg"));
			notification.setMonthEmail(result.getString("email_mm"));
			notification.setMonthSms(result.getString("sms_mm"));
			db.close();
			return notification;
		}
		else throw new NotificationException();
	}

	public void changePassword(String query, String password, String user)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, UserNotFoundException {
		// open connection to database
		Connection db = DatabaseConnection();

		PreparedStatement preparedStmt = db.prepareStatement(query);
		preparedStmt.setString(1, password);
		preparedStmt.setString(2, password);
		preparedStmt.setString(3, user);

		// execute the java preparedstatement
		int modified=preparedStmt.executeUpdate();
		db.close();
		if (modified==0)
			throw new UserAccountNotFoundException("User account not found.");
	}

	/**
	 * 
	 * @param query
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Double executeQueryDouble(String query)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Connection db = DatabaseConnection();
		Statement st = db.createStatement();
		ResultSet result = st.executeQuery(query);
		result.next();
		return result.getDouble(1);
	}

	/**
	 * 
	 * @param query
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int executeQueryInt(String query) throws InstantiationException,
	IllegalAccessException, ClassNotFoundException, SQLException {
		Connection db = DatabaseConnection();
		Statement st = db.createStatement();
		ResultSet result = st.executeQuery(query);
		result.next();
		return result.getInt(1);
	}

	public int[] executeQueryThreshold(String query)throws InstantiationException,
	IllegalAccessException, ClassNotFoundException, SQLException {
		Connection db = DatabaseConnection();
		Statement st = db.createStatement();
		ResultSet result = st.executeQuery(query);
		result.next();
		int[] threshold=new int[2];
		threshold[0]=result.getInt(1);
		threshold[1]=result.getInt(2);
		return threshold ;
	}
	/**
	 * 
	 * @param query
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String executeQueryString(String query)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Connection db = DatabaseConnection();
		Statement st = db.createStatement();
		ResultSet result = st.executeQuery(query);
		result.next();
		return result.getString(1);
	}

	public void setNotifications(String query, String user,
			String sms, String email, int threshold)
					throws InstantiationException, IllegalAccessException,
					ClassNotFoundException, SQLException, UserNotFoundException {

		Connection db = DatabaseConnection();

		PreparedStatement preparedStmt = db.prepareStatement(query);
		preparedStmt.setString(1, sms);
		preparedStmt.setString(2, email);
		preparedStmt.setInt(3, threshold);
		preparedStmt.setString(4, user);

		// execute the java preparedstatement
		int modified=preparedStmt.executeUpdate();
		db.close();
		if (modified==0)
			throw new UserAccountNotFoundException("User account not found.");
	}

	public History getSmsHistory(String queryCount, String query, String user, String filterData,
			int offset, int limit, int HOURSDIFFERENCE) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SQLException {

		Connection db = DatabaseConnection();
		//GET UID of user
		Statement st = db.createStatement();
		ResultSet result = st.executeQuery("SELECT uid FROM playsms_tblUser WHERE username='"+user+"'");
		result.next();
		int uid= result.getInt(1);

		PreparedStatement preparedStmtCount = db.prepareStatement(queryCount);

		preparedStmtCount.setInt(1, uid);
		preparedStmtCount.setString(2, "%" + filterData + "%");
		preparedStmtCount.setString(3, "%" + filterData + "%");
		preparedStmtCount.setString(4, "%" + filterData + "%");
		ResultSet count = preparedStmtCount.executeQuery();

		Long counter = 0L;
		if(count.next()){
			counter = (long) count.getInt(1);
		}

		PreparedStatement preparedStmt = db.prepareStatement(query);

		preparedStmt.setInt(1, uid);
		preparedStmt.setString(2, "%" + filterData + "%");
		preparedStmt.setString(3, "%" + filterData + "%");
		preparedStmt.setString(4, "%" + filterData + "%");
		preparedStmt.setInt(5, limit);
		preparedStmt.setInt(6, offset);

		// execute the java preparedstatement
		ResultSet set = preparedStmt.executeQuery();
		History h = refactoringSmsSearchAll(set, counter, HOURSDIFFERENCE);
		db.close();
		return h;

	}

	private History refactoringSmsSearchAll(ResultSet set, Long counter, int HOURSDIFFERENCE) throws SQLException {
		Date date=null;
		String newDate=null;
		History history = new History();
		ArrayList<Row> rows = new ArrayList<Row>();
		while (set.next()) {
			Row row = new Row();
			//Viene restituito l'orario sbagliato con 1h indietro
			try{
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(set.getString("p_datetime"));
			newDate  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.addHours(date,HOURSDIFFERENCE));
			}catch(ParseException e){
				throw new SMSGenericException("Date format invalid; please use yyyy-MM-dd HH:mm:ss format", e);
			}
			row.setDate(newDate);
			row.setRecipient(set.getString("p_dst"));
			String[] split = set.getString("p_msg").split(":\n");
			//Se non è utilizzata la sintassi corretta (sender: msg), lo split restituisce un valore e quindi genera eccezione, così setto
			//manualmente il sender.
			if (split.length > 1){
				row.setText(split[0]);
				row.setSender(split[1]);
			}else{
				row.setText(split[0]);
				row.setSender("No Sender");
			}
			rows.add(row);

		}

		history.setRows(rows);
		history.setTotal(counter);

		return history;

	}
}
