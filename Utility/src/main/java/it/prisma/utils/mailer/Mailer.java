package it.prisma.utils.mailer;

import it.prisma.utils.mailer.exceptions.EmailSyntaxException;
import it.prisma.utils.mailer.exceptions.MailContentException;

import java.net.URISyntaxException;

import javax.mail.MessagingException;

public interface Mailer {

	public void sendAccountToConfirmMail(final String toEmail,
			final String fullName, final String token)
			throws MessagingException, MailContentException, EmailSyntaxException, URISyntaxException;

	public void sendEmail(final String toEmail, final String fromEmail,
			final String subject, final String body) throws MessagingException,
			EmailSyntaxException;

}
