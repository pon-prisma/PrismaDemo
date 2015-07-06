package it.prisma.utils.mailer.content.accounting;

import it.prisma.utils.mailer.content.ContentFactory;
import it.prisma.utils.mailer.content.ContentFactorySkeleton;
import it.prisma.utils.mailer.exceptions.MailContentException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

public class AccountToConfirmContentFactory extends ContentFactorySkeleton implements ContentFactory {
	
	private static final String ENCODE = "UTF-8";
	private static final String TEMPLATE_NAME = "AccountConfirmation.vm";
	
	private final String uriScheme;
	private final String uriAuthority;
	private final String confirmationPageURI;
	private final String route;
	private final String from;
	private final String support;
	private final String subject;
	
	public AccountToConfirmContentFactory(Properties properties) throws URISyntaxException {
		uriScheme = properties.getProperty("prisma.webui.uri.scheme");
		uriAuthority = properties.getProperty("prisma.webui.uri.authority");
		route = new StringBuilder()
			.append("/")
			.append(properties.getProperty("prisma.webui.accounting.signup.confirm.route"))
			.toString();
		confirmationPageURI = new URI(uriScheme, uriAuthority, route, null).toASCIIString();
		from = new StringBuilder()
			.append(properties.getProperty("prisma.mailconfig.accounting.address"))
			.append("@").append(uriAuthority)
			.toString();
		subject = properties.getProperty("prisma.mailconfig.accounting.signup.confirm.subject");
		support = new StringBuilder()
			.append(properties.getProperty("prisma.mailconfig.accounting.support.address"))
			.append("@").append(uriAuthority)
			.toString();
	}
	
	@Override
	protected String createSubject() {
		return this.subject;
	}

	@Override
	protected String createFrom() {
		return this.from;
	}

	@Override
	protected Template createTemplate() {
		return super.getTemplateEngine()
				.getTemplate(ContentFactorySkeleton.TEMPLATES_PATH + TEMPLATE_NAME,
						ContentFactorySkeleton.ENCODING);
	}

	@Override
	protected VelocityContext createContext(Map<String, String> data) throws MailContentException {
		VelocityContext context = new VelocityContext();
		try {

			String confirmationRequestURI = new URI(uriScheme, uriAuthority,
					route,
					"&email=" + URLEncoder.encode(data.get("email"), ENCODE) +
					"&code=" + URLEncoder.encode(data.get("token"), ENCODE),
					null).toASCIIString();
			context.put("email", data.get("email"));
			context.put("fullName", data.get("fullName"));
			context.put("token", data.get("token"));
			context.put("confirmationRequestURI", confirmationRequestURI);
			context.put("confirmationPageURI", confirmationPageURI);
			context.put("support", support);
		} catch (Exception e) {
			context = null;
			throw new MailContentException(MailContentException.CONTEXT_ERROR_MSG);
		}
		return context;
	}
	
}
