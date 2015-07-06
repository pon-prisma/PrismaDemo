package it.prisma.businesslayer.bizws.config.security;

import it.prisma.businesslayer.bizlib.accounting.UserManagement;
import it.prisma.businesslayer.bizws.config.AppConfiguration;
import it.prisma.businesslayer.bizws.config.security.authentication.AuthenticationTokenProcessingFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Configuration class for all the security realms of Spring Security. <br/>
 * Note that there are two realms: one unauthenticated for RESTful WS (/rest/*)
 * and one for the website (/web/*).
 * 
 * @author bln
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
// , mode = AdviceMode.ASPECTJ, proxyTargetClass = true, prePostEnabled=true)
public class SecurityConfiguration {

	/**
	 * Configures the {@link AuthenticationManager} for using the custom
	 * {@link DBUserDetailsService} and related {@link PasswordEncoder}.
	 * 
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		// auth.userDetailsService(userDetailsService()).passwordEncoder(
		// passwordEncoder());
		auth.inMemoryAuthentication().withUser("test").password("test")
				.roles("USER");
	}

	// RESTful WS branch configuration
	// @Configuration
	// @Order(1)
	// public static class RestufulWSWebSecurityConfigurationAdapter extends
	// WebSecurityConfigurerAdapter {
	// protected void configure(HttpSecurity http) throws Exception {
	// http.antMatcher("/rest/**").authorizeRequests().anyRequest()
	// .permitAll();
	// }
	// }

	@Configuration
	@Order(1)
	public static class WebsiteWebSecurityConfigurerAdapter extends
			WebSecurityConfigurerAdapter {

		@Override
		public void configure(WebSecurity web) throws Exception {
			String PREFIX = AppConfiguration.WEB_APP_URL_PREFIX;

			web.ignoring()
			// Spring Security should completely ignore URLs starting with
			// /resources/
					.antMatchers(PREFIX + "/static/**");
			// .antMatchers("/**");
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

			String PREFIX = "/rest";

			String[] URLsToBeProtectedAsWebUiAndUser = {
														PREFIX + "/accounting/workgroups/**/memberships",
														PREFIX + "/accounting/workgroups/memberships/**",
														PREFIX + "/idp/info/**",
														PREFIX + "/orchestrator/monitoring/**",
														PREFIX + "/accounting/users/**",
														}; 
			
			String[] URLsToBeProtectedAsWebUi = {PREFIX + "/auth/tokens/**", 
												 PREFIX + "/config/**", 
												 PREFIX + "/accounting/organizations/idps/**"}; 
			
			String[] URLsToBeProtectedAsMonitoring = {PREFIX + "/orc/iaas/vmaas"}; 
			
			
			//TODO: manage the admin's URLs
			String[] URLsToBeProtectedAsAdmin = {					
												 }; 
			
			String[] URLsToBeProtectedAsUser = {					
												 PREFIX + "/workgroups/**", 
												 PREFIX + "/misc/**",
												 PREFIX + "/orc/**", 
												 PREFIX + "/accounting/**",  
												 PREFIX + "/apprepo/**", 
												 PREFIX + "/CAaas/**", 
										 		 PREFIX + "/SMSaas/**", 
										 		 PREFIX + "/EMAILaaS/**",
												 PREFIX + "/orchestrator/**",
												}; 

			String[] URLsNotProtected = { 
											PREFIX + "/workgroups/*/paas/appaas/*/environments/*/appsrcfile", 
											PREFIX + "/misc/debug", 
										};
			

			// @formatter:off
			http
			.csrf().disable()
			.exceptionHandling().authenticationEntryPoint(authenticationExceptionEntryPoint())
			.and()
			//.x509().
			//.httpBasic().authenticationEntryPoint(authenticationEntryPoint)
			.authorizeRequests().antMatchers(URLsNotProtected).permitAll().and()
			// Currently not allowed to generate token through basic authentication
			.antMatcher(PREFIX + "/**")
					.authorizeRequests()
					// Routes privileges
//					.antMatchers(PREFIX + "/auth/**")
//					.permitAll()
					//TODO Restore to restrict access
					.antMatchers(URLsToBeProtectedAsWebUiAndUser).hasAnyRole(UserManagement.RoleEnum.PRISMA_WEBUI.getRoleString(),
																			 UserManagement.RoleEnum.PRISMA_USER.getRoleString())
					.antMatchers(URLsToBeProtectedAsWebUi).hasRole(UserManagement.RoleEnum.PRISMA_WEBUI.getRoleString())
					.antMatchers(URLsToBeProtectedAsMonitoring).hasRole(UserManagement.RoleEnum.PRISMA_MONITORING.getRoleString())
					.antMatchers(URLsToBeProtectedAsAdmin).hasRole(UserManagement.RoleEnum.PRISMA_ADMIN.getRoleString())
					.antMatchers(URLsToBeProtectedAsUser).hasRole(UserManagement.RoleEnum.PRISMA_USER.getRoleString())
			.and()
            .addFilterBefore(authenticationTokenProcessingFilter(), BasicAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			//org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter
			//					.and()
					// Errors pages overrides
//					.exceptionHandling()
//					.accessDeniedHandler(lmfAccessDeniedHandler())
//					.accessDeniedPage(PREFIX + "/errors/access_denied")
//					.and()
					// Logout
//					.logout()
//					.logoutUrl(PREFIX + "/logout")
//					.logoutRequestMatcher(
//							new AntPathRequestMatcher(PREFIX + "/logout", "GET"))
//					.logoutSuccessUrl(PREFIX + "/")
//					.and()
//					// Login
//					.formLogin()
//					.usernameParameter("username")
//					.passwordParameter("password")
//					.loginPage(PREFIX + "/login?error=required")
//					.failureUrl(PREFIX + "/login?error=invalid")
//					.loginProcessingUrl(PREFIX + "/login")
//					.defaultSuccessUrl(ROUTE_LOGIN_SUCCESFUL)
//					.permitAll()
//					.and()
			// @formatter:on
		}

		/**
		 * Produces a LMFAccessDeniedHandler.
		 * 
		 * @return
		 */
		// @Bean
		// public LMFAccessDeniedHandler lmfAccessDeniedHandler() {
		// return new LMFAccessDeniedHandler();
		// }

		@Bean
		public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter() {
			return new AuthenticationTokenProcessingFilter(
					authenticationExceptionEntryPoint());
		}

		@Bean
		public AuthenticationExceptionEntryPoint authenticationExceptionEntryPoint() {
			return new AuthenticationExceptionEntryPoint();
		}
	}

	/**
	 * Produces a password encoder for users password encryption.
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	// @Bean
	// public SocialUserDetailsService socialUserDetailsService() {
	// return new LMFSocialUserDetailsService();
	// }

	/**
	 * Produces a DB implementation of a {@link UserDetailsService}.
	 * 
	 * @return
	 */
	// @Bean
	// public UserDetailsService userDetailsService() {
	// return new DBUserDetailsService();
	// }

}