package it.prisma.presentationlayer.webui.configs;

import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.prisma.idp.PrismaIdPAPIClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RESTfulWSConfig {

	@Value("${prisma.businesslayer.url}")
	private String businessLayerURL;
	
	@Value("${prisma.idp.url}")
	private String prismaIdPURL;
	
	@Bean
	public PrismaBizAPIClient prismaBizAPIClient() {
		return new PrismaBizAPIClient(businessLayerURL);
	}
	
	@Bean
	public PrismaIdPAPIClient prismaIdPAPIClient() {
		return new PrismaIdPAPIClient(prismaIdPURL);
	}
	
}
