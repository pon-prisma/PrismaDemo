package it.prisma.presentationlayer.webui.configs;

import it.prisma.domain.dsl.configuration.PlatformConfiguration;
import it.prisma.domain.dsl.configuration.PlatformConfigurations;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import javax.servlet.MultipartConfigElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiPartConfig {

	static final Logger LOG = LogManager.getLogger(MultiPartConfig.class.getName());

	@Autowired
	PrismaBizAPIClient prismaBizAPIClient;

	@Value("${DEFAULT_AUTH_TOKEN}")
	String DEFAULT_AUTH_TOKEN;
	

	@Bean
	MultipartConfigElement multipartConfigElement() {
		String uploadSize = null;
		String keys[] = { "uploadFileSize" };
		try {
			
			PlatformConfigurations p = prismaBizAPIClient.getPlatformConfiguration(
					keys, DEFAULT_AUTH_TOKEN);
			for (PlatformConfiguration plat : p.getPlatformconfiguration()) {
				if (plat.getKey().equals("uploadFileSize")) {
					uploadSize = plat.getValue();
				}
			}
		} catch (Exception e) {
			LOG.error("Exception: " + e.getMessage());
		}

		MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
		if (uploadSize != null) {
			LOG.info("Max upload file-size allowed: " + uploadSize);
			multipartConfigFactory.setMaxFileSize(uploadSize);
			multipartConfigFactory.setMaxRequestSize(uploadSize);
		} else {
			LOG.info("Max upload file-size (default): 200MB");
			multipartConfigFactory.setMaxFileSize("200MB");
			multipartConfigFactory.setMaxRequestSize("200MB");
		}
		return multipartConfigFactory.createMultipartConfig();
	}
}