/*
 * Copyright 2014 PRISMA by MIUR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.prisma.presentationlayer.webui.configs;

import it.prisma.presentationlayer.webui.security.SSOAuthenticationFailureHandler;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetailsServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLDiscovery;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.SAMLWebSSOHoKProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProviderLB;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.parser.ParserPoolHolder;
import org.springframework.security.saml.processor.HTTPArtifactBinding;
import org.springframework.security.saml.processor.HTTPPAOS11Binding;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.HTTPSOAP11Binding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.trust.httpclient.TLSProtocolConfigurer;
import org.springframework.security.saml.trust.httpclient.TLSProtocolSocketFactory;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.ArtifactResolutionProfile;
import org.springframework.security.saml.websso.ArtifactResolutionProfileImpl;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileECPImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//@Value("${debug}")
	//private boolean DEBUG;
	
	// Logger
	static final Logger LOG = LogManager.getLogger(WebSecurityConfig.class);

		
	@Value("${environment}")
	private String environment;
		
	@Autowired
	private SSOAuthenticationFailureHandler ssoAuthenticationFailureHandler;

	@Autowired
	private PrismaUserDetailsServiceImpl prismaUserDetailsServiceImpl;
	
		
	@Autowired
	private SAMLIdentityProviderRegistry samlIdentityProviderRegistry;


	@Value("${environment.${environment}.sp.entity.id}")
	private String SP_ENTITY_ID;
	@Value("${environment.${environment}.samlkey}")
	private String SAML_KEY_NAME;
	@Value("${environment.${environment}.domain}")
	private String DOMAIN;

	
	@Value("${saml.key.pwd}")
	private String SAML_KEY_PWD;
	@Value("${keystore.pwd}")
	private String KEYSTORE_PWD;
	

	
	// Logger for SAML messages and events
	@Bean
	public SAMLDefaultLogger samlDefaultLogger() {
		SAMLDefaultLogger samlDefaultLogger = new SAMLDefaultLogger();
		samlDefaultLogger.setLogMessages(true);
		samlDefaultLogger.setLogErrors(true);
		return samlDefaultLogger;
	}

	// SAML Authentication Provider responsible for validating of received SAML
	// messages
	@Bean
	public SAMLAuthenticationProvider samlAuthenticationProvider() {
		SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
		samlAuthenticationProvider.setForcePrincipalAsString(false);
		samlAuthenticationProvider.setUserDetails(prismaUserDetailsServiceImpl);
		return samlAuthenticationProvider;
	}

	// Provider of default SAML Context
	@Bean
	public SAMLContextProviderLB contextProvider() {
		SAMLContextProviderLB provider = new SAMLContextProviderLB();
		provider.setScheme("https");
		provider.setServerPort(443);
		provider.setServerName(DOMAIN);
		
		provider.setContextPath("/");
		provider.setKeyManager(keyManager());
		return provider;
	}

	// Initialization of OpenSAML library
	@Bean
	public static SAMLBootstrap sAMLBootstrap() {
		return new SAMLBootstrap();
	}

	// SAML 2.0 WebSSO Assertion Consumer
	@Bean
	public WebSSOProfileConsumer webSSOprofileConsumer() {
		WebSSOProfileConsumerImpl webSSO = new WebSSOProfileConsumerImpl();
		webSSO.setMaxAuthenticationAge(604800); // One week
		webSSO.setResponseSkew(240);
		return webSSO;
	}

	// SAML 2.0 Holder-of-Key WebSSO Assertion Consumer
	@Bean
	public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
		return new WebSSOProfileConsumerHoKImpl();
	}

	// SAML 2.0 Web SSO profile
	@Bean
	public WebSSOProfile webSSOprofile() {
		return new WebSSOProfileImpl();
	}

	// SAML 2.0 Holder-of-Key Web SSO profile
	@Bean
	public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
		return new WebSSOProfileConsumerHoKImpl();
	}
	
	// Setup TLS Socket Factory
    @Bean
    public TLSProtocolConfigurer tlsProtocolConfigurer() {
    	return new TLSProtocolConfigurer();
    }
    
    @Bean
    public ProtocolSocketFactory socketFactory() {
        return new TLSProtocolSocketFactory(keyManager(), null, "default");
    }

    @Bean
    public Protocol socketFactoryProtocol() {
        return new Protocol("https", socketFactory(), 443);
    }

    @Bean
    public MethodInvokingFactoryBean socketFactoryInitialization() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetClass(Protocol.class);
        methodInvokingFactoryBean.setTargetMethod("registerProtocol");
        Object[] args = {"https", socketFactoryProtocol()};
        methodInvokingFactoryBean.setArguments(args);
        return methodInvokingFactoryBean;
    }

	// SAML 2.0 ECP profile
	@Bean
	public WebSSOProfileECPImpl ecpprofile() {
		return new WebSSOProfileECPImpl();
	}

	@Bean
	public SingleLogoutProfile logoutprofile() {
		return new SingleLogoutProfileImpl();
	}

	// Central storage of cryptographic keys
	@Bean
	public JKSKeyManager keyManager() {
		DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource storeFile = loader
				.getResource("classpath:/saml/saml-keystore.jks");
		String storePass = KEYSTORE_PWD;
		Map<String, String> passwords = new HashMap<String, String>();
		String defaultKey = SAML_KEY_NAME;
		passwords.put(SAML_KEY_NAME, SAML_KEY_PWD);
		
		return new JKSKeyManager(storeFile, storePass, passwords, defaultKey);
	}

	@Bean
	public WebSSOProfileOptions defaultWebSSOProfileOptions() {
		WebSSOProfileOptions webSSOProfileOptions = new WebSSOProfileOptions();
		webSSOProfileOptions.setIncludeScoping(false);
		return webSSOProfileOptions;
	}

	// Entry point to initialize authentication, default values taken from
	// properties file
	@Bean
	public SAMLEntryPoint samlEntryPoint() {
		SAMLEntryPoint samlEntryPoint = new SAMLEntryPoint();
		samlEntryPoint.setDefaultProfileOptions(defaultWebSSOProfileOptions());
		return samlEntryPoint;
	}

	// IDP Discovery Service
	@Bean
	public SAMLDiscovery samlIDPDiscovery() {
		SAMLDiscovery idpDiscovery = new SAMLDiscovery();
		idpDiscovery.setIdpSelectionPath("/saml/idpSelection");
		return idpDiscovery;
	}

	@Bean
	public ExtendedMetadata extendedMetadata() {
		ExtendedMetadata extendedMetadata = new ExtendedMetadata();
		extendedMetadata.setIdpDiscoveryEnabled(true);
		extendedMetadata.setSignMetadata(true);
		extendedMetadata.setAlias("defaultAlias");
		return extendedMetadata;
	}

	// Filter automatically generates default SP metadata
	@Bean
	public MetadataGenerator metadataGenerator() {
		MetadataGenerator metadataGenerator = new MetadataGenerator();
		metadataGenerator.setEntityId(SP_ENTITY_ID);
		metadataGenerator.setEntityBaseURL("https://" + DOMAIN + ":443");
	
		metadataGenerator.setExtendedMetadata(extendedMetadata());
		return metadataGenerator;
	}

	// The filter is waiting for connections on URL suffixed with filterSuffix
	// and presents SP metadata there
	@Bean
	public MetadataDisplayFilter metadataDisplayFilter() {
		return new MetadataDisplayFilter();
	}

	// Handler deciding where to redirect user after successful login
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler() throws Exception {
		SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler = 
				new SavedRequestAwareAuthenticationSuccessHandler();
		successRedirectHandler.setDefaultTargetUrl("/dashboard");
				
		return successRedirectHandler;
	}

	@Bean
	public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
		SimpleUrlAuthenticationFailureHandler failureHandler = 
				new SimpleUrlAuthenticationFailureHandler();
		failureHandler.setUseForward(true);
		failureHandler.setDefaultFailureUrl("/");
		return failureHandler;
	}

	@Bean
	public SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter()
			throws Exception {
		SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter = 
				new SAMLWebSSOHoKProcessingFilter();
		samlWebSSOHoKProcessingFilter
				.setAuthenticationSuccessHandler(successRedirectHandler());
		samlWebSSOHoKProcessingFilter
				.setAuthenticationManager(authenticationManager());
		return samlWebSSOHoKProcessingFilter;
	}

	// Processing filter for WebSSO profile messages
	@Bean
	public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {
		SAMLProcessingFilter samlWebSSOProcessingFilter = new SAMLProcessingFilter();
		samlWebSSOProcessingFilter
				.setAuthenticationManager(authenticationManager());
		samlWebSSOProcessingFilter
				.setAuthenticationSuccessHandler(successRedirectHandler());
		samlWebSSOProcessingFilter
				.setAuthenticationFailureHandler(ssoAuthenticationFailureHandler);
		return samlWebSSOProcessingFilter;
	}

	@Bean
	public MetadataGeneratorFilter metadataGeneratorFilter() {
		return new MetadataGeneratorFilter(metadataGenerator());
	}

	// Handler for successful logout
	@Bean
	public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
		SimpleUrlLogoutSuccessHandler successLogoutHandler = 
				new SimpleUrlLogoutSuccessHandler();
		successLogoutHandler.setDefaultTargetUrl("/");
		return successLogoutHandler;
	}

	// Logout handler terminating local session
	@Bean
	public SecurityContextLogoutHandler logoutHandler() {
		SecurityContextLogoutHandler logoutHandler = 
				new SecurityContextLogoutHandler();
		logoutHandler.setInvalidateHttpSession(true);
		logoutHandler.setClearAuthentication(true);
		return logoutHandler;
	}

	// Filter processing incoming logout messages
	// First argument determines URL user will be redirected to after successful
	// global logout
	@Bean
	public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
		return new SAMLLogoutProcessingFilter(successLogoutHandler(),
				logoutHandler());
	}

	// Overrides default logout processing filter with the one processing SAML
	// messages
	@Bean
	public SAMLLogoutFilter samlLogoutFilter() {
		return new SAMLLogoutFilter(successLogoutHandler(),
				new LogoutHandler[] { logoutHandler() },
				new LogoutHandler[] { logoutHandler() });
	}
	
	@Bean
	@Qualifier("sielteMeta")
	public ExtendedMetadataDelegate sielteMetadataProvider()
			throws MetadataProviderException, IOException {
		
		 if(environment.equalsIgnoreCase("localhost"))
			return null;
		
		String onServer = "/root/saml/metadata/sielte/FederationMetadata.xml";
		FilesystemMetadataProvider filesystemMetadataProvider = new FilesystemMetadataProvider(
				new File(onServer));
		filesystemMetadataProvider.setParserPool(parserPool());
		ExtendedMetadataDelegate emd = new ExtendedMetadataDelegate(
				filesystemMetadataProvider, new ExtendedMetadata());
		emd.setMetadataTrustCheck(false);
		return emd;
	}

	@Bean
	@Qualifier("replyMeta")
	public ExtendedMetadataDelegate replyMetadataProvider()
			throws MetadataProviderException, IOException {
		
		if(environment.equalsIgnoreCase("localhost"))
			return null;
		
		String onServer = "/root/saml/metadata/reply/FederationMetadata.xml";
		FilesystemMetadataProvider filesystemMetadataProvider = new FilesystemMetadataProvider(
				new File(onServer));
		filesystemMetadataProvider.setParserPool(parserPool());
		ExtendedMetadataDelegate emd = new ExtendedMetadataDelegate(
				filesystemMetadataProvider, new ExtendedMetadata());
		emd.setMetadataTrustCheck(false);
		return emd;
	}
	
	@Bean
	@Qualifier("infnMeta")
	public ExtendedMetadataDelegate infnMetadataProvider()
			throws MetadataProviderException, IOException {
		
		if(environment.equalsIgnoreCase("localhost"))
			return null;
		
		String onServer = "/root/saml/metadata/infn/metadata.xml";
		FilesystemMetadataProvider filesystemMetadataProvider = new FilesystemMetadataProvider(
				new File(onServer));
		filesystemMetadataProvider.setParserPool(parserPool());
		ExtendedMetadataDelegate emd = new ExtendedMetadataDelegate(
				filesystemMetadataProvider, new ExtendedMetadata());
		emd.setMetadataTrustCheck(false);
		return emd;
	}
	
	@Bean
	@Qualifier("prismaMeta")
	public ExtendedMetadataDelegate prismaMetadataProvider()
			throws MetadataProviderException, IOException {
		
		if(environment.equalsIgnoreCase("localhost"))
			return null;
		
		String onServer = "/root/saml/metadata/prisma/metadata.xml";
		FilesystemMetadataProvider filesystemMetadataProvider = new FilesystemMetadataProvider(
				new File(onServer));
		filesystemMetadataProvider.setParserPool(parserPool());
		ExtendedMetadataDelegate emd = new ExtendedMetadataDelegate(
				filesystemMetadataProvider, new ExtendedMetadata());
		emd.setMetadataTrustCheck(false);
		return emd;
	}
	
	@Bean
	@Qualifier("unibaMeta")
	public ExtendedMetadataDelegate unibaMetadataProvider()
			throws MetadataProviderException, IOException {
		
		if(environment.equalsIgnoreCase("localhost"))
			return null;
		
		String onServer = "/root/saml/metadata/uniba/metadata.xml";
		FilesystemMetadataProvider filesystemMetadataProvider = new FilesystemMetadataProvider(
				new File(onServer));
		filesystemMetadataProvider.setParserPool(parserPool());
		ExtendedMetadataDelegate emd = new ExtendedMetadataDelegate(
				filesystemMetadataProvider, new ExtendedMetadata());
		emd.setMetadataTrustCheck(false);
		return emd;
	}

	@Bean
	@Qualifier("fedCohesionMeta")
	public ExtendedMetadataDelegate fedCohesionMetadataProvider()
			throws MetadataProviderException, IOException {
		
		if(environment.equalsIgnoreCase("localhost"))
			return null;
		
		String onServer = "/root/saml/metadata/fedCohesion/metadata.xml";
		FilesystemMetadataProvider filesystemMetadataProvider = new FilesystemMetadataProvider(
				new File(onServer));
		filesystemMetadataProvider.setParserPool(parserPool());
		ExtendedMetadataDelegate emd = new ExtendedMetadataDelegate(
				filesystemMetadataProvider, new ExtendedMetadata());
		emd.setMetadataTrustCheck(false);
		emd.setRequireValidMetadata(true);
		return emd;
	}

	
	// IDP Metadata configuration - paths to metadata of IDPs in circle of trust
	// is here
	// Do no forget to call iniitalize method on providers
	@Bean
	@Qualifier("metadata")
	public CachingMetadataManager metadata() throws MetadataProviderException, IOException {		
		List<MetadataProvider> providers = new ArrayList<MetadataProvider>();
		if(!environment.equalsIgnoreCase("localhost")){
			providers.add(prismaMetadataProvider());
			providers.add(replyMetadataProvider());
			providers.add(infnMetadataProvider());
			providers.add(sielteMetadataProvider());
			providers.add(fedCohesionMetadataProvider());
		}
		return new CachingMetadataManager(providers);
	}

	// Initialization of the velocity engine
	@Bean
	public VelocityEngine velocityEngine() {
		return VelocityFactory.getEngine();
	}

	// XML parser pool needed for OpenSAML parsing
	@Bean(initMethod = "initialize")
	public StaticBasicParserPool parserPool() {
		return new StaticBasicParserPool();
	}

	@Bean(name = "parserPoolHolder")
	public ParserPoolHolder parserPoolHolder() {
		return new ParserPoolHolder();
	}

	// Bindings, encoders and decoders used for creating and parsing messages
	@Bean
	public MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager() {
		return new MultiThreadedHttpConnectionManager();
	}

	/**
	 * 
	 * Proxy Reply, da usare solo per deploy su Rec
	 *  
	 * @return
	 */
	@Bean
	public HttpClient httpClient() {
		HttpClient httpClient = new HttpClient(
				multiThreadedHttpConnectionManager());
		
		if(environment.equalsIgnoreCase("reply") || environment.equalsIgnoreCase("localhost")){
			HostConfiguration hostConfig = new HostConfiguration();
			hostConfig.setProxyHost(new ProxyHost("proxy.reply.eu", 8080));
			httpClient.setHostConfiguration(hostConfig);
		}
		return httpClient;
	}
	
    @Bean
    public HTTPSOAP11Binding soapBinding() {
        return new HTTPSOAP11Binding(parserPool());
    }
	
    private ArtifactResolutionProfile artifactResolutionProfile() {
        final ArtifactResolutionProfileImpl artifactResolutionProfile = new ArtifactResolutionProfileImpl(httpClient());
        artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding()));
        return artifactResolutionProfile;
    }
    
    @Bean
    public HTTPArtifactBinding artifactBinding(ParserPool parserPool, VelocityEngine velocityEngine) {
        return new HTTPArtifactBinding(parserPool, velocityEngine, artifactResolutionProfile());
    }

    @Bean
    public HTTPPostBinding httpPostBinding() {
    	return new HTTPPostBinding(parserPool(), velocityEngine());
    }
    
    @Bean
    public HTTPRedirectDeflateBinding httpRedirectDeflateBinding() {
    	return new HTTPRedirectDeflateBinding(parserPool());
    }
    
    @Bean
    public HTTPSOAP11Binding httpSOAP11Binding() {
    	return new HTTPSOAP11Binding(parserPool());
    }
    
    @Bean
    public HTTPPAOS11Binding httpPAOS11Binding() {
    	return new HTTPPAOS11Binding(parserPool());
    }
    
	@Bean
	public SAMLProcessorImpl processor() {
		Collection<SAMLBinding> bindings = new ArrayList<SAMLBinding>();
		bindings.add(httpRedirectDeflateBinding());
		bindings.add(httpPostBinding());
		bindings.add(artifactBinding(parserPool(), velocityEngine()));
		bindings.add(httpSOAP11Binding());
		bindings.add(httpPAOS11Binding());
		return new SAMLProcessorImpl(bindings);
	}

	@Bean
	public FilterChainProxy samlFilter() throws Exception {
		List<SecurityFilterChain> chains = new ArrayList<SecurityFilterChain>();
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(
				"/saml/login/**"), samlEntryPoint()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(
				"/saml/logout/**"), samlLogoutFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(
				"/saml/metadata/**"), metadataDisplayFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(
				"/saml/SSO/**"), samlWebSSOProcessingFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(
				"/saml/SSOHoK/**"), samlWebSSOHoKProcessingFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(
				"/saml/SingleLogout/**"), samlLogoutProcessingFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(
				"/saml/discovery/**"), samlIDPDiscovery()));
		return new FilterChainProxy(chains);
	}

	/**
	 * Returns the authentication manager currently used by Spring. It
	 * represents a bean definition with the aim allow wiring from other classes
	 * performing the Inversion of Control (IoC).
	 * 
	 * @throws Exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Defines the web based security configuration.
	 * 
	 * @param http
	 *            It allows configuring web based security for specific http
	 *            requests.
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic().authenticationEntryPoint(samlEntryPoint());
		http.addFilterBefore(metadataGeneratorFilter(),
				ChannelProcessingFilter.class).addFilterAfter(samlFilter(),
				BasicAuthenticationFilter.class);
		
		
		if(environment.equalsIgnoreCase("localhost")){
			http.authorizeRequests().antMatchers("/**").permitAll();
		} else {
			http.authorizeRequests()
			.antMatchers("/", "/home").permitAll()
			.antMatchers("/saml/**").permitAll()
			.antMatchers("/static/**").permitAll()
			.antMatchers("/accounting/**").permitAll()
			.antMatchers("/debug").permitAll()
			.anyRequest().authenticated();
		}

		http.logout().logoutSuccessUrl("/");
	}

	/**
	 * Sets a custom authentication provider.
	 * 
	 * @param auth
	 *            SecurityBuilder used to create an AuthenticationManager.
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(samlAuthenticationProvider());
	}

	/**
	 * Allows adding patterns that should that Spring Security should ignore. It
	 * useful to guarantee access to public resources such as CSSs, scripts,
	 * fonts.
	 * 
	 * @param web
	 *            The WebSecurity is created by WebSecurityConfiguration to
	 *            create the FilterChainProxy known as the Spring Security
	 *            Filter Chain (springSecurityFilterChain).
	 * @throws Exception
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**");
	}

}