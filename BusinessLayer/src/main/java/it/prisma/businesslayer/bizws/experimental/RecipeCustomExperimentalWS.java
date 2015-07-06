package it.prisma.businesslayer.bizws.experimental;

import it.prisma.businesslayer.bizlib.iaas.cloudify.CustomRecipe;
import it.prisma.businesslayer.utils.mailer.MailServiceBean;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponse;
import it.prisma.utils.mailer.exceptions.EmailSyntaxException;
import it.prisma.utils.misc.StackTrace;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/exp")
public class RecipeCustomExperimentalWS implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger prismaLog = LogManager
			.getLogger(RecipeCustomExperimentalWS.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customrecipe")
	public Response customrecipe() {

		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("db.name", "myDBName");
			param.put("db.password", "myDBPassword");

			String originalRecipePath = this.getClass().getProtectionDomain()
					.getCodeSource().getLocation().getPath()
					+ "/cloudify-recipes/" + "custom-recipe.zip";

			CustomRecipe cr = new CustomRecipe(originalRecipePath, null, param);

			return handleResult(cr.customize());

		} catch (Exception e) {
			return handleGenericException(e);
		}

	}

	@Inject
	private MailServiceBean mailService;

	@GET
	@Path("/testMail")
	public void testMail() throws EmailSyntaxException, MessagingException {
		String sender = "info@bari.ponsmartcities-prisma.eu";
		String recipient = "lore.b.reply@gmail.com";
		String subject = "Prisma: APPaaS service ready !";
		String body = "Prova !";

		mailService.getMailSender().sendEmail(recipient, sender, subject, body);
	}

	protected Response handleGenericException(Exception e) {
		e.printStackTrace();

		// Unexpected error occurred (probably server error)
		return PrismaResponse
				.status(Status.INTERNAL_SERVER_ERROR,
						OrchestratorErrorCode.ORC_GENERIC_ERROR,
						StackTrace.getStackTraceToString(e)).build().build();
	}

	protected <ResponseType> Response handleResult(ResponseType result) {
		return PrismaResponse.status(Status.OK, result).build().build();
	}

}