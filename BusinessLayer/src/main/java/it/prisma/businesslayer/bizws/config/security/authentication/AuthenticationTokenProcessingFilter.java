package it.prisma.businesslayer.bizws.config.security.authentication;

import it.prisma.businesslayer.bizlib.accounting.AuthTokenManagement;
import it.prisma.businesslayer.bizlib.accounting.AuthTokenManagementBean;
import it.prisma.businesslayer.bizlib.accounting.PrismaUserDetails;
import it.prisma.businesslayer.bizlib.common.exceptions.TokenExpiredException;
import it.prisma.businesslayer.bizlib.common.exceptions.TokenNotFoundException;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.util.Assert;

public class AuthenticationTokenProcessingFilter extends
		BasicAuthenticationFilter {

	public static final String HEADER_PARAM_TOKEN = "X-Auth-Token";

	private static final Logger LOG = LogManager
			.getLogger(AuthenticationTokenProcessingFilter.class);

	@EJB(mappedName = AuthTokenManagementBean.JNDIName)
	private AuthTokenManagement authTokenbean;

	/**
	 * @deprecated Use constructor injection
	 */
	public AuthenticationTokenProcessingFilter() {
		super();
	}

	public AuthenticationTokenProcessingFilter(
			AuthenticationManager authenticationManager,
			AuthenticationEntryPoint authenticationEntryPoint) {
		super(authenticationManager, authenticationEntryPoint);
	}

	public AuthenticationTokenProcessingFilter(
			AuthenticationEntryPoint authenticationEntryPoint) {
		super(null, authenticationEntryPoint);
	}

	public AuthenticationTokenProcessingFilter(
			AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	public void afterPropertiesSet() {
		// Assert.notNull(this.authenticationManager,
		// "An AuthenticationManager is required");

		if (!isIgnoreFailure()) {
			Assert.notNull(super.getAuthenticationEntryPoint(),
					"An AuthenticationEntryPoint is required");
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		String tokenId = request.getHeader(HEADER_PARAM_TOKEN);

		if (tokenId != null) {
			LOG.debug("Token: " + tokenId);
			try {
				try {
					PrismaUserDetails principal = authTokenbean
							.getTokenMeta(tokenId);

					Authentication authentication = new UsernamePasswordAuthenticationToken(
							principal, null, principal.getAuthorities());

					SecurityContextHolder.getContext().setAuthentication(
							authentication);
				} catch (TokenNotFoundException tnfe) {
					LOG.debug("Invalid token");
					throw new BadCredentialsException("Invalid token", tnfe);
				} catch (TokenExpiredException tnfe) {
					LOG.debug("Token expired");
					throw new NonceExpiredException("Token expired", tnfe);
				}
			} catch (AuthenticationException failed) {
				SecurityContextHolder.clearContext();

				LOG.debug("Authentication request for failed: " + failed);

				// onUnsuccessfulAuthentication(request, response, failed);

				if (super.isIgnoreFailure()) {
					chain.doFilter(request, response);
				} else {
					super.getAuthenticationEntryPoint().commence(request,
							response, failed);
				}

				return;
			} catch (Exception e) {
				LOG.error("Token validation error: " + e.getMessage(), e);
				((HttpServletResponse) response).sendError(
						Status.INTERNAL_SERVER_ERROR.getStatusCode(),
						"Token validation error.");
				throw new PrismaWrapperException(e);

			}
		} else {
			LOG.debug("No token found");
		}
		// continue thru the filter chain
		chain.doFilter(request, response);
	}
}