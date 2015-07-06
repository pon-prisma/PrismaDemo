package it.prisma.utils.mailer.content;

import it.prisma.utils.mailer.exceptions.MailContentException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * Abstract class. It defines the skeletons of the algorithms that generate
 * mail content (sender address, subject, body), according to the <em>Template
 * Method</em> design pattern.
 * 
 * @author v.denotaris
 *
 */
abstract public class ContentFactorySkeleton {
	
	protected static final String TEMPLATES_PATH = "it/prisma/utils/mailer/templates/";
	protected static final String ENCODING = "UTF-8";
	private VelocityEngine velocityEngine;
	
	public ContentFactorySkeleton() {
		this.setTemplateEngine(new VelocityEngine());
	}
	
	protected VelocityEngine getTemplateEngine() {
		return velocityEngine;
	}

	protected void setTemplateEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
		this.velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		this.velocityEngine.setProperty(
				"classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		this.velocityEngine.init();
	}

	public String getSubject() {
		return createSubject();
	}
	
	public String getFrom() {
		return createFrom();
	}

	// Standardize the skeletons of the algorithms in a "template" method
	public String getBody(Map<String, String> data) {
		
		// Set up mail template and context
		Template template = createTemplate();
		VelocityContext context = createContext(data);

		// Merge template and context into string
		try {
			return mergeTemplateAndContextIntoString(template, context);
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
			throw new MailContentException(MailContentException.CONTENT_ERROR_MSG);
		}
	}
	
	// Hooks requiring peculiar implementation
	abstract protected String createSubject();
	abstract protected String createFrom();
	abstract protected Template createTemplate();
	abstract protected VelocityContext createContext(Map<String, String> data);
	
	// Common implementations of individual steps are defined in base class
	protected String mergeTemplateAndContextIntoString(Template template, VelocityContext context)
			throws IOException, NullPointerException {
		String result = null;
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        result = writer.toString();
		writer.close();
		return result;
	}
	
}
