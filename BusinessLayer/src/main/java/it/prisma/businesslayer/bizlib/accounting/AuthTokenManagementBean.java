package it.prisma.businesslayer.bizlib.accounting;

import it.prisma.businesslayer.bizlib.JNDIHelper;
import it.prisma.businesslayer.bizlib.common.exceptions.TokenExpiredException;
import it.prisma.businesslayer.bizlib.common.exceptions.TokenNotFoundException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.dal.dao.accounting.AuthTokenDAO;
import it.prisma.dal.entities.accounting.AuthToken;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.UserProfile;
import it.prisma.domain.dsl.accounting.users.RoleRepresentation;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.utils.authTokenManagement.AuthTokenManagementUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Stateless
@Local(AuthTokenManagement.class)
public class AuthTokenManagementBean implements AuthTokenManagement {

	// Logger
	private static final Logger LOG = LogManager
			.getLogger(AuthTokenManagementBean.class);

	@EJB
	private EnvironmentConfig envConfig;

	/**
	 * In hours.
	 */
	private static final Integer DEFAULT_TOKEN_VALIDITY = 24 * 30;

	public static final String JNDIName = JNDIHelper.EJB_BasePath
			+ "AuthTokenManagementBean!it.prisma.businesslayer.bizlib.accounting.AuthTokenManagement";

	@Inject
	private AuthTokenDAO authTokenDAO;

	@Inject
	private UserManagementBean userBean;

	@Inject
	private MappingHelper<UserProfile, UserRepresentation> userMH;

	@Override
	public AuthToken createToken(Long userAccountId) {
		byte isSession = 0;
		
		UserAccount userAccount = userBean.getUserAccountByUserAccountId(userAccountId);
//		UserProfile user = userBean.getUserProfileById(userAccountId);
		AuthToken token = new AuthToken();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, DEFAULT_TOKEN_VALIDITY);
		token.setExpiresAt(cal.getTime());
		token.setUserAccount(userAccount);
		token.setSession(isSession);
		return authTokenDAO.create(token);
	}

	@Override
	public AuthToken createSessionToken(Long userAccountId) {
		byte isSession = 1;
		UserAccount userAccount = userBean.getUserAccountByUserAccountId(userAccountId);
		
//		UserProfile user = userBean.getUserProfileById(userAccountId);
		AuthToken token = new AuthToken();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, DEFAULT_TOKEN_VALIDITY);
		token.setExpiresAt(cal.getTime());
		token.setUserAccount(userAccount);
		token.setSession(isSession);
		return authTokenDAO.create(token);
	}

	@Override
	public Collection<AuthToken> getTokenByUser(Long userAccountId) {
		return authTokenDAO.findByUser(userAccountId);
	}

	@Override
	public AuthToken getSessionTokenByUser(Long userAccountId)
			throws TokenNotFoundException {
		AuthToken authToken = authTokenDAO
				.findSessionTokenByUser(userAccountId);
		if (authToken == null)
			throw new TokenNotFoundException("Session auth token not found");

		return authToken;
	}

	@Override
	public boolean deleteToken(String tokenId) {
		if (!authTokenDAO.exists(tokenId))
			return false;

		authTokenDAO.delete(tokenId);
		return true;
	}

	@Override
	public PrismaUserDetails getTokenMeta(String tokenId) {

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		if (tokenId.equals(envConfig
				.getSecurityProperty(EnvironmentConfig.DEFAULT_AUTH_TOKEN))) {
			authorities.add(new SimpleGrantedAuthority("ROLE_"
					+ UserManagement.RoleEnum.PRISMA_WEBUI.getRoleString()));

			UserRepresentation user = new UserRepresentation();

			return new PrismaUserDetails("webui", true, true, true, true,
					authorities, user);
		} else if (tokenId
				.equals(envConfig
						.getSecurityProperty(EnvironmentConfig.DEFAULT_MONITORING_TOKEN))) {
			authorities
					.add(new SimpleGrantedAuthority("ROLE_"
							+ UserManagement.RoleEnum.PRISMA_MONITORING
									.getRoleString()));

			UserRepresentation user = new UserRepresentation();

			return new PrismaUserDetails("monitoring", true, true, true, true,
					authorities, user);
		} else {
			UserRepresentation user;

			// Testing purpose token, to impersonate a given user dynamically
			if (tokenId.startsWith("TEST_")) {
				Long userAccountId = Long.parseLong(tokenId
						.replace("TEST_", ""));
				user = userMH
						.getDSL(userBean.getUserAccountByUserAccountId(userAccountId)
								.getUserProfile());
				
				
				LOG.warn("Authentication with TEST TOKEN impersonating UserId "
						+ userAccountId);
			} else {
				AuthToken token = getToken(tokenId);

				if (!isTokenValid(token))
					throw new TokenExpiredException(token.getExpiresAt());

				user = userMH.getDSL(token.getUserAccount().getUserProfile());
			}
			for (RoleRepresentation role : user.getRoles()) {
				String prismaUserRole = role.getName();

				switch (prismaUserRole) {
				case "PRISMA_ADMIN":
					authorities.add(new SimpleGrantedAuthority("ROLE_"
							+ UserManagement.RoleEnum.PRISMA_ADMIN
									.getRoleString()));
					authorities.add(new SimpleGrantedAuthority("ROLE_"
							+ UserManagement.RoleEnum.PRISMA_USER
									.getRoleString()));
					// authorities.add(new
					// SimpleGrantedAuthority("ROLE_GUEST"));

					// TODO : Is the "ADMIN" allowed to see the "GUEST" stuff?
					break;
				case "PRISMA_USER":
					authorities.add(new SimpleGrantedAuthority("ROLE_"
							+ UserManagement.RoleEnum.PRISMA_USER
									.getRoleString()));
					// authorities.add(new
					// SimpleGrantedAuthority("ROLE_GUEST"));

					// TODO : Is the "USER" allowed to see the "GUEST" stuff?
					break;
				default:
					authorities.add(new SimpleGrantedAuthority("ROLE_"
							+ UserManagement.RoleEnum.PRISMA_GUEST
									.getRoleString()));
					// TODO : Do we need to throw an "unknown role exception" in
					// case of unrecognized role?
					break;
				}

			}

			return new PrismaUserDetails(user.getNameIdOnIdentityProvider(),
					true, true, true, true, authorities, user);
		}

	}

	@Override
	public boolean isTokenValid(AuthToken token) {
		return AuthTokenManagementUtil.isTokenValid(token.getExpiresAt());
	}

	@Override
	public AuthToken getToken(String tokenId) throws TokenNotFoundException {
		AuthToken token = authTokenDAO.findById(tokenId);
		if (token == null)
			throw new TokenNotFoundException();

		return token;
	}
}
