package it.prisma.businesslayer.bizlib.mail;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.utils.LinkHelper;
import it.prisma.businesslayer.utils.mailer.MailServiceBean;
import it.prisma.dal.dao.accounting.UserAccountDAO;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.utils.mailer.MailerImpl;
import it.prisma.utils.mailer.exceptions.EmailSyntaxException;

import javax.inject.Inject;
import javax.mail.MessagingException;

public class MailBean {

	@Inject
	protected MailServiceBean mailService;

	@Inject
	protected UserAccountDAO userDAO;

	@Inject
	protected EnvironmentConfig envConfig;

	@Inject
	protected LinkHelper linkHelper;

	protected String getBaseEmailAddress() {
		return envConfig.getMailProperty(EnvironmentConfig.MAIL_BASE_ADDRESS);
	}

	enum MailAddressType {
		INFO
	}

	protected String getEmailAddress(MailAddressType type) {
		switch (type) {
		case INFO:
			return envConfig
					.getMailProperty(EnvironmentConfig.MAIL_INFO_ADDRESS)
					+ "@"
					+ getBaseEmailAddress();

		default:
			throw new IllegalArgumentException("Invalid Email address type "
					+ type);
		}
	}

	public void sendPaaSDeploySuccesfullEmail(AbstractPaaSService paasService)
			throws EmailSyntaxException, MessagingException {

		MailerImpl mailSender = mailService.getMailSender();
		String mailUsername;
		PaaSService paasSvc = paasService.getPaaSService();
		// Fix for transaction Workaround
		UserAccount user = userDAO.findById(paasSvc.getUserAccount().getId());

		//Check if middleName is null and this code avoids to print null in email
		if (user.getUserProfile().getMiddleName()==null){
			mailUsername = "Dear " + user.getUserProfile().getFirstName()
					+ " " + user.getUserProfile().getLastName();
		}else {
			mailUsername = "Dear " + user.getUserProfile().getFirstName()
					+ " " + user.getUserProfile().getMiddleName() + " "
					+ user.getUserProfile().getLastName();
		}

		String sender = getEmailAddress(MailAddressType.INFO);
		String recipient = paasSvc.getNotificationEmail();

		String paasServiceType = paasSvc.getType();

		String subject = "Prisma: " + paasSvc.getName() + " ("
				+ paasServiceType + " service) is ready to use !";

		String deployLink = linkHelper.getPaaSServiceLink(paasService);
		String body = mailUsername + "<br/><br/> Your " + paasServiceType
				+ " '<a href=\"" + deployLink + "\">" + paasSvc.getName()
				+ "</a>' is ready to use.<br/>Service domain name: "
				+ paasSvc.getDomainName();

		mailSender.sendEmail(recipient, sender, subject, body);
	}

	public void sendPaaSDeployErrorEmail(AbstractPaaSService paasService,
			String errorDetails) throws EmailSyntaxException,
			MessagingException {

		String mailUsername;

		MailerImpl mailSender = mailService.getMailSender();

		PaaSService paasSvc = paasService.getPaaSService();
		// Fix for transaction Workaround
		UserAccount user = userDAO.findById(paasSvc.getUserAccount().getId());

		//Check if middleName is null and this code avoids to print null in email
		if (user.getUserProfile().getMiddleName()==null){
			mailUsername = "Dear " + user.getUserProfile().getFirstName()
					+ " " + user.getUserProfile().getLastName();
		}else {
			mailUsername = "Dear " + user.getUserProfile().getFirstName()
					+ " " + user.getUserProfile().getMiddleName() + " "
					+ user.getUserProfile().getLastName();
		}


		String sender = getEmailAddress(MailAddressType.INFO);
		String recipient = paasSvc.getNotificationEmail();

		String paasServiceType = paasSvc.getType();

		String subject = "Prisma: " + paasSvc.getName() + " ("
				+ paasServiceType + " service) deploy failed !";

		String deployLink = linkHelper.getPaaSServiceLink(paasService);

		String body = mailUsername
				+ "<br/><br/>Unfortunately, your "
				+ paasServiceType
				+ " '<a href=\""
				+ deployLink
				+ "\">"
				+ paasSvc.getName()
				+ "</a>' deploy has failed.<br/>Error details: "
				+ errorDetails
				+ "<br/><br/>Please, contact the <a href=\"mailto:support@bari.ponsmartcities-prisma.eu\">support team</a> for further instructions.";

		mailSender.sendEmail(recipient, sender, subject, body);
	}

	public void sendPaaSUndeploySuccesfullEmail(AbstractPaaSService paasService)
			throws EmailSyntaxException, MessagingException {

		String mailUsername; 

		MailerImpl mailSender = mailService.getMailSender();

		PaaSService paasSvc = paasService.getPaaSService();
		// Fix for transaction Workaround
		UserAccount user = userDAO.findById(paasSvc.getUserAccount().getId());

		//Check if middleName is null and this code avoids to print null in email
		if (user.getUserProfile().getMiddleName()==null){
			mailUsername = "Dear " + user.getUserProfile().getFirstName()
					+ " " + user.getUserProfile().getLastName();
		}else {
			mailUsername = "Dear " + user.getUserProfile().getFirstName()
					+ " " + user.getUserProfile().getMiddleName() + " "
					+ user.getUserProfile().getLastName();
		}

		String sender = getEmailAddress(MailAddressType.INFO);
		String recipient = paasSvc.getNotificationEmail();

		String paasServiceType = paasSvc.getType();

		String subject = "Prisma: " + paasSvc.getName() + " ("
				+ paasServiceType + " service) has been undeployed !";

		String body = mailUsername + "<br/><br/> Your " + paasServiceType
				+ " '" + paasSvc.getName()
				+ "' has been successfully deleted from the platform.";

		mailSender.sendEmail(recipient, sender, subject, body);
	}

	public void sendPaaSUndeployErrorEmail(AbstractPaaSService paasService,
			String errorDetails) throws EmailSyntaxException,
			MessagingException {

		String mailUsername;
		
		MailerImpl mailSender = mailService.getMailSender();

		PaaSService paasSvc = paasService.getPaaSService();
		// Fix for transaction Workaround
		UserAccount user = userDAO.findById(paasSvc.getUserAccount().getId());

		//Check if middleName is null and this code avoids to print null in email
		if (user.getUserProfile().getMiddleName()==null){
			mailUsername = "Dear " + user.getUserProfile().getFirstName()
					+ " " + user.getUserProfile().getLastName();
		}else {
			mailUsername = "Dear " + user.getUserProfile().getFirstName()
					+ " " + user.getUserProfile().getMiddleName() + " "
					+ user.getUserProfile().getLastName();
		}

		String sender = getEmailAddress(MailAddressType.INFO);
		String recipient = paasSvc.getNotificationEmail();

		String paasServiceType = paasSvc.getType();

		String subject = "Prisma: " + paasSvc.getName() + " ("
				+ paasServiceType + " service) undeploy failed !";

		String body = mailUsername
				+ "<br/><br/>Unfortunately, your "
				+ paasServiceType
				+ " '"
				+ paasSvc.getName()
				+ "' undeployment failed.<br/>Error details: "
				+ errorDetails
				+ "<br/><br/>Please, contact the <a href=\"mailto:support@bari.ponsmartcities-prisma.eu\">support team</a> for further instructions.";

		mailSender.sendEmail(recipient, sender, subject, body);
	}

	public void sendTestEmail(String email) throws EmailSyntaxException,
	MessagingException {

		MailerImpl mailSender = mailService.getMailSender();

		String sender = getEmailAddress(MailAddressType.INFO);
		String recipient = (email != null ? email : sender);

		String subject = "Prisma BizLayer Mail Test";

		String body = "Prisma automatic mail test.";

		mailSender.sendEmail(recipient, sender, subject, body);
	}
}
