package it.prisma.utils.mailer;

import it.prisma.utils.mailer.content.ContentFactory;
import it.prisma.utils.mailer.content.accounting.AccountToConfirmContentFactory;
import it.prisma.utils.mailer.core.MailSenderCore;
import it.prisma.utils.mailer.exceptions.EmailSyntaxException;
import it.prisma.utils.mailer.exceptions.MailContentException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;

public class MailerImpl implements Mailer {

	private final MailSenderCore mailSender;

	public MailerImpl() throws IOException {
		mailSender = new MailSenderCore();
	}

	public MailerImpl(Properties properties) {
		mailSender = new MailSenderCore(properties);
	}

	@Override
	public void sendAccountToConfirmMail(final String toEmail,
			final String fullName, final String token)
			throws MessagingException, MailContentException,
			EmailSyntaxException, URISyntaxException {
		Map<String, String> data = new HashMap<String, String>();
		data.put("email", toEmail);
		data.put("fullName", fullName);
		data.put("token", token);
		ContentFactory contentGenerator = new AccountToConfirmContentFactory(
				this.getMailSenderProperties());
		mailSender.send(toEmail, contentGenerator.getFrom(),
				contentGenerator.getSubject(), contentGenerator.getBody(data));
	}

	@Override
	public void sendEmail(final String toEmail, final String fromEmail,
			final String subject, final String body) throws MessagingException,
			EmailSyntaxException {
		mailSender.send(toEmail, fromEmail, subject, body);
	}

	protected Properties getMailSenderProperties() {
		return mailSender.getProperties();
	}

}