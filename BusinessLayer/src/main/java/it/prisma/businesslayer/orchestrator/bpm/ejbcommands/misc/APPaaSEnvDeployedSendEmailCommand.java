package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.misc;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.utils.mailer.MailServiceBean;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.utils.mailer.MailerImpl;

import javax.ejb.Local;
import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * This class implements a jBPM Executor Command which consumes a REST web
 * service.
 * 
 * @author l.biava
 * 
 */
@Local(APPaaSEnvDeployedSendEmailCommand.class)
public class APPaaSEnvDeployedSendEmailCommand extends BaseCommand {

	@Inject
	private MailServiceBean mailService;

	public APPaaSEnvDeployedSendEmailCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		MailerImpl mailSender = mailService.getMailSender();

		PaaSService paasSvc = (PaaSService) workItem
				.getParameter("PaaSService");

		UserAccount user = (UserAccount) workItem.getParameter("UserAccount");

		String mailUsername = "Dear " + user.getUserProfile().getFirstName()
				+ " " + user.getUserProfile().getMiddleName() + " "
				+ user.getUserProfile().getLastName();

		String sender = "info@bari.ponsmartcities-prisma.eu";
		String recipient = paasSvc.getNotificationEmail();
		String subject = "Prisma: APPaaS service ready !";
		String body = mailUsername + "\n\nYour APPaaS environment '"
				+ paasSvc.getName() + "' is ready to be used. Service IP: "
				+ paasSvc.getDomainName();

		ExecutionResults exResults = new ExecutionResults();

		try {

			mailSender.sendEmail(recipient, sender, subject, body);

			resultOccurred("OK", exResults);
		} catch (Exception e) {
			handleSystemException(e, OrchestratorErrorCode.ORC_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}

}
