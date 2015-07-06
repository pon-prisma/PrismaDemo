package it.prisma.businesslayer.bizlib.paas.services.emailaas;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.exceptions.email.DomainNotFoundException;
import it.prisma.businesslayer.utils.PathHelper.ResourceType;
import it.prisma.dal.dao.paas.services.emailaas.VirtualDomainsDAO;
import it.prisma.dal.dao.paas.services.emailaas.VirtualUsersDAO;
import it.prisma.dal.entities.paas.services.emailaas.VirtualDomains;
import it.prisma.dal.entities.paas.services.emailaas.VirtualUsers;
import it.prisma.domain.dsl.paas.services.emailaas.Credential;
import it.prisma.domain.dsl.paas.services.emailaas.EmailDomains;
import it.prisma.domain.dsl.paas.services.emailaas.EmailInfoRepresentation;
import it.prisma.domain.dsl.paas.services.emailaas.EmailMessageRepresentation;
import it.prisma.domain.dsl.paas.services.emailaas.MD5;
import it.prisma.domain.dsl.paas.services.emailaas.history.RowEmail;
import it.prisma.utils.misc.PropertiesReader;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class EmailHelper {

	private static final Logger LOG = LogManager.getLogger(EmailHelper.class);

	private Properties properties;
	@Inject
	protected EnvironmentConfig envConfig;
	@Inject
	VirtualUsersDAO virtualUsersDAO;
	@Inject
	VirtualDomainsDAO virtualDomainsDAO;

	@PostConstruct
	private void init() throws IOException {

		properties = PropertiesReader
				.read(ResourceType.CONFIG_ENV_VARIABLE_PROPERTIES.getSubPath()
						+ "/" + "mailaas.mailer.properties");
		if (LOG.isDebugEnabled())
			for (Entry<Object, Object> item : properties.entrySet()) {
				LOG.debug("[MailaaS Mailer Properties] - K: " + item.getKey()
						+ ", V: " + item.getValue());
			}
	}

	public void sendEmailaas(EmailMessageRepresentation request)
			throws AuthenticationFailedException, MessagingException,
			NoSuchAlgorithmException {

		// Create a Session object to represent a mail session with the
		// specified properties.
		Session session = Session.getDefaultInstance(properties);

		// Create a message with the specified information.
		CustomMimeMessage msg = new CustomMimeMessage(session);
		msg.updateMessageID();
		msg.setFrom(new InternetAddress(request.getSender()));
		msg.setRecipient(Message.RecipientType.TO,
				new InternetAddress(request.getRecipient()));
		msg.setSubject(request.getSubject());
		msg.setContent(request.getBody(), "text/html");

		// Create a transport.
		Transport transport = session.getTransport();
		// Send the message.
		transport.connect(properties.getProperty("mail.smtp.ssl.trust"),
				request.getSender().split("@")[0],
				MD5.getMD5(request.getPassword()));
		// Send the email.
		transport.sendMessage(msg, msg.getAllRecipients());
		// Close and terminate the connection.
		transport.close();

	}

	public String createEmailAccount(Long userPrismaId,
			EmailInfoRepresentation request) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			DomainNotFoundException {

		VirtualDomains domain = virtualDomainsDAO.findById(request
				.getDomain_id());
		if (domain == null)
			throw new DomainNotFoundException();

		VirtualUsers user = new VirtualUsers();
		user.setDomain_id(domain.getId());
		user.setUser_prisma_id(userPrismaId);
		user.setUser(request.getUser());
		user.setPassword(MD5.getMD5(request.getPassword()));
		user.setEmail(request.getUser() + "@" + domain.getName());

		virtualUsersDAO.create(user);
		return user.getEmail();
		// //String
		// queryDomain="SELECT name FROM virtual_domains WHERE id='"+request.getDomain_id()+"'";
		// String
		// query="INSERT INTO virtual_users(domain_id, user_prisma_id, user, password, email)"
		// + "VALUES (?,?,?,MD5(?),?)";
		// add new email prisma to database postfix
		// return helper.addUser(queryDomain,query,userPrismaId,request);

	}

	public boolean deleteEmailAccount(String email, String password)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		return virtualUsersDAO.deleteEmailAccount(email, MD5.getMD5(password));

		// String
		// query="DELETE FROM virtual_users WHERE email=? AND password=MD5(?)";
		// return helper.deleteEmailAccount(query,email,password);
	}

	public boolean updatePassword(Credential credential)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		VirtualUsers user = virtualUsersDAO.findByEmailAndPassword(
				credential.getEmail(), MD5.getMD5(credential.getPassword()));
		// there is no user with this password
		if (user == null)
			return false;
		user.setPassword(MD5.getMD5(credential.getNewPassword()));
		virtualUsersDAO.update(user);
		// user = virtualUsersDAO.findByEmailAndPassword(credential.getEmail(),
		// MD5.getMD5(credential.getNewPassword()));
		// //there is no user with new password, so the password has not been
		// changed
		// if (user==null)
		// return false;
		return true;

		// String
		// query="UPDATE virtual_users SET password=MD5(?) WHERE email=? AND password=MD5(?)";
		// return helper.updateEmailPassword(query,credential);
	}

	public List<RowEmail> getEmailAccountList(Long userPrismaId)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		Collection<VirtualUsers> collection = virtualUsersDAO
				.findByVirtualUsersPrismaIdWithFilters(userPrismaId);
		return refactoringEmailSearchAll(collection);
		// String
		// query="Select * FROM virtual_users WHERE user_prisma_id='"+userPrismaId+"' ORDER BY user";
		// return helper.getEmailAccountList(query);
	}

	public boolean isEmailAccountExists(String email)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		return virtualUsersDAO.isEmailAccountExists(email);
		// String
		// query="Select user FROM virtual_users WHERE email='"+email+"'";
		// return helper.isEmailAccountExists(query);
	}

	public List<EmailDomains> getEmailDomainList()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		return refactoringDomainSearchAll(virtualDomainsDAO.findAll());
		// String query="SELECT * FROM virtual_domains";
		// return helper.getEmailDomainList(query);
	}

	private List<RowEmail> refactoringEmailSearchAll(
			Collection<VirtualUsers> collection) {
		List<RowEmail> rows = new ArrayList<RowEmail>();
		for (VirtualUsers account : collection) {
			RowEmail row = new RowEmail();
			row.setId(account.getId());
			row.setDomainId(account.getDomain_id());
			row.setEmail(account.getEmail());
			row.setPassword(account.getPassword());
			row.setUser(account.getUser());
			row.setUserPrismaId(account.getUser_prisma_id());
			rows.add(row);
		}
		return rows;
	}

	private List<EmailDomains> refactoringDomainSearchAll(
			Collection<VirtualDomains> collection) {
		List<EmailDomains> domains = new ArrayList<EmailDomains>();
		for (VirtualDomains account : collection) {
			EmailDomains domain = new EmailDomains();
			domain.setId(account.getId());
			domain.setDomain(account.getName());
			domains.add(domain);
		}
		return domains;
	}

}

class CustomMimeMessage extends MimeMessage {

	public CustomMimeMessage(Session session) {
		super(session);
	}

	@Override
	protected void updateMessageID() throws MessagingException {
		removeHeader("Message-Id");
	}

}
