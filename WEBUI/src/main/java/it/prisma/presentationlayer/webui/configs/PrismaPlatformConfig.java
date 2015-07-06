package it.prisma.presentationlayer.webui.configs;

import it.prisma.domain.dsl.configuration.PlatformConfigurations;
import it.prisma.presentationlayer.webui.vos.platformconfig.PrismaPlatformConfiguration;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrismaPlatformConfig {
	
	@Value("${DEFAULT_AUTH_TOKEN}")
	String DEFAULT_AUTH_TOKEN;
	
	@Value("${prisma.dns_suffix}")
	String DNS_SUFFIX;
	
	static final Logger LOG = LogManager.getLogger(PrismaPlatformConfig.class.getName());
	
	@Autowired
	PrismaBizAPIClient prismaClient;

	@Bean
	public PrismaPlatformConfiguration getPlatformConfiguration() {
		PlatformConfigurations p;
		try {
			p = prismaClient.getAllPlatformConfiguration(DEFAULT_AUTH_TOKEN);
			Map<String, String> configurationMap = new HashMap<String, String>();
			for (it.prisma.domain.dsl.configuration.PlatformConfiguration plat : p
					.getPlatformconfiguration()) {
				if (plat.getKey() != null && !plat.getKey().equals("")) {
					configurationMap.put(plat.getKey(), plat.getValue());
					LOG.debug(plat.getKey() + " -> " + plat.getValue());
				}
			}
			return new PrismaPlatformConfiguration(configurationMap);
		} catch (MappingException | NoMappingModelFoundException
				| ServerErrorResponseException | APIErrorException
				| RestClientException | IOException e) {
			LOG.error("Cannot load properties form WS " + e);
			throw new java.lang.Error("Cannot load properties from WS " + e);
		}
	}
	
	@Bean(name = "urlService")
    public UrlService urlService() {
        return new UrlService() {
			
			@Override
			public String getDefaultDNSSuffix() {
				//return ".infn.ponsmartcities-prisma.it";
				return DNS_SUFFIX;
			}
		};
        		
    }
}