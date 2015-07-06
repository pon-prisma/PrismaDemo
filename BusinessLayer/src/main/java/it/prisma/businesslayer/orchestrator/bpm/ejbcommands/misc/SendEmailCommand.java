package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.misc;

import it.prisma.businesslayer.bizlib.mail.MailBean;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.entities.paas.services.PaaSService;

import java.util.Properties;

import javax.ejb.Local;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * This class implements a jBPM Executor Command which consumes a REST web
 * service.
 * 
 * @author l.biava
 * 
 */
@Local(SendEmailCommand.class)
public class SendEmailCommand extends BaseCommand {

	@Inject
	protected MailBean mailBean;
	
	public SendEmailCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		PaaSService paasSvc = (PaaSService) workItem
				.getParameter("PaaSService");
		
		//mailBean.sendPaaSDeploySuccesfullEmail(paasSvc);
//		String mailserverUrl = (String) workItem.getParameter("MailserverUrl");
//		String mailAddr = (String) workItem.getParameter("MailAddress");
//		String mailBody = (String) workItem.getParameter("MailBody");

		//Object prevAPIResult = workItem.getParameter("PreviousAPIResult");
		//ServiceInstanceDescriptionResponse servInst = (ServiceInstanceDescriptionResponse) prevAPIResult;

		// TODO Remove and use only previous fields once they works
//		ProvisionDBaaSRequest originalRequest = (ProvisionDBaaSRequest) workItem
//				.getParameter("OriginalRequest");
//		UserAccount user = (UserAccount) workItem.getParameter("UserAccount");
////		UserProfile userProfile = (UserProfile) user.getUserProfile();
//
//		String mailUsername = "Dear " + user.getUserProfile().getFirstName() + " "
//				+  user.getUserProfile().getMiddleName() + " " +  user.getUserProfile().getLastName();
//
// 		String mailAddr = paasSvc.getNotificationEmail();
//		String mailBody = mailUsername + "\n\n your APPaaS environment '" + paasSvc.getName() + "' is ready to be used. Service IP: " + paasSvc.getDomainName();
//
//		sendEmail(mailAddr, "Prisma APPaaS service ready !", mailBody);
		
		ExecutionResults exResults = new ExecutionResults();

		resultOccurred("Success mail sent to:" + paasSvc.getNotificationEmail(), exResults);

		return exResults;
	}

	// static final String FROM = "rossiuser@prismacloud.it"; // Replace with
	// your "From" address. This address must be verified.
	// static final String TO = "rootUserProva@prismacloud.it"; // Replace with
	// a "To" address. If you have not yet requested
	// production access, this address must be verified.
	static final String FROM = "rossiuser@bari.ponsmartcities-prisma.eu"; //
	// Replace with your "From" address. This address must be verified.
	// bari.ponsmartcities-prisma.eu
	// static final String TO = "rootUserProva@bari.ponsmartcities-prisma.eu";

	// Replace with a "To" address. If you have not yet requested
	// production access, this address must be verified.

//	static final String TO = "";
//
//	static final String BODY = "Invio body di test per il mio utente";
//	static final String SUBJECT = "Varie prove invio test";

	// Supply your SMTP credentials below. Note that your SMTP credentials are
	// different from your AWS credentials.
	static final String SMTP_USERNAME = "rossiuser"; // Replace with your
	// SMTP username.
	static final String SMTP_PASSWORD = "pr01rossi"; // Replace with your
	// SMTP password.

	// Amazon SES SMTP host name. This example uses the us-east-1 region.
	static final String HOST = "pr01Mail01.replycloud.prv";
	// static final String HOST ="smpt.bari.ponsmartcities-prisma.eu";

	// mail.bari.ponsmartcities-prisma.eu

	// Port we will connect to on the SES SMTP endpoint. We are choosing port 25
	// because we will use
	// STARTTLS to encrypt the connection.
	static final int PORT = 25;

	public void sendEmail(String to, String subject, String body) throws Exception {
		System.out.println("Hello smtp!");
		// Create a Properties object to contain connection configuration
		// information.
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT);

		// Set properties indicating that we want to use STARTTLS to encrypt the
		// connection.
		// The SMTP session will begin on an unencrypted connection, and then
		// the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", true);
		/*
		 * SSL encryption is started by STARTTLS protocol (described in RFC
		 * 3207)
		 */
		props.put("mail.smtp.starttls.enable", true);
		// props.put("mail.smtp.starttls.required", "true");
		/*
		 * mail.smtp.ssl.trust is needed in script to avoid error
		 * "Could not convert socket to TLS"
		 */
		props.setProperty("mail.smtp.ssl.trust", HOST);
		props.put("mail.debug", "true");

		// Create a Session object to represent a mail session with the
		// specified properties.
		Session session = Session.getDefaultInstance(props);

		// Create a message with the specified information.
		// MimeMessage msg = new MimeMessage(session);
		CustomMimeMessage msg = new CustomMimeMessage(session);
		msg.updateMessageID();
		msg.setFrom(new InternetAddress(FROM));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		msg.setSubject(subject);
		msg.setContent(body, "text/plain");

		// Create a transport.
		Transport transport = session.getTransport();

		// Send the message.
		try {
			System.out
					.println("Attempting to send an email through the SES SMTP interface...");

			// Connect to Amazon SES using the SMTP username and password you
			// specified above.
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
		} finally {
			// Close and terminate the connection.
			transport.close();
		}
	}

	class CustomMimeMessage extends MimeMessage {

		public CustomMimeMessage(Session session) {
			super(session);
		}

		@Override
		protected void updateMessageID() throws MessagingException {
			// setHeader("Message-ID", "objects-message-id");
			removeHeader("Message-Id");
		}

	}
}
