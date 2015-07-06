package it.prisma.businesslayer.utils.mailer;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.utils.PathHelper.ResourceType;
import it.prisma.utils.mailer.MailerImpl;
import it.prisma.utils.misc.PropertiesReader;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
@Local
public class MailServiceBean {

	private static final Logger LOG = LogManager
			.getLogger(MailServiceBean.class);

	private MailerImpl mailer;

	@Inject
	protected EnvironmentConfig envConfig;

	@PostConstruct
	protected void init() throws IOException {
		// mailer = new MailerImpl(
		// PropertiesReader.read(ResourceType.CONFIG_ENV_VARIABLE_PROPERTIES_PROFILES.getSubPath()
		// + envConfig.getEnvProfile()
		// + "/"
		// + "orc.mailer.properties"));
		Properties props = PropertiesReader
				.read(ResourceType.CONFIG_ENV_VARIABLE_PROPERTIES.getSubPath()
						+ "/" + "orc.mailer.properties");
		if (LOG.isDebugEnabled())
			for (Entry<Object, Object> item : props.entrySet()) {
				LOG.debug("[Mailer Properties] - K: " + item.getKey() + ", V: "
						+ item.getValue());
			}
		
		mailer = new MailerImpl(props);
	}

	public MailerImpl getMailSender() {
		return mailer;
	}

}
