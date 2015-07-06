package it.prisma.businesslayer.bizws.config;

import it.prisma.businesslayer.bizws.config.security.SecurityConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for main application properties.
 *
 */
@Configuration
// To add AuthPrincipalResolver & other Spring SecurityWebMVCConfigs.
@ComponentScan({ "it.prisma.businesslayer.bizws" })
// Scan this tree for Spring website related classes.
@Import({ SecurityConfiguration.class })
// Include Spring Security Configs
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfiguration {

	public static final String WEB_APP_URL_PREFIX = "";

}