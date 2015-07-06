//package it.prisma.businesslayer.bizws.config.security;
//
//import java.lang.reflect.Method;
//import java.util.List;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import javax.interceptor.Interceptor;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.Provider;
//
//import org.apache.http.HttpRequest;
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.jboss.resteasy.annotations.interception.ServerInterceptor;
//import org.jboss.resteasy.core.ResourceMethod;
//import org.jboss.resteasy.core.ServerResponse;
//import org.jboss.resteasy.spi.Failure;
//import org.jboss.resteasy.spi.interception.AcceptedByMethod;
//import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
//
//@Interceptor
//@Provider
//@ServerInterceptor
//@SecurityChecked
//public class SecurityCheckInterceptor implements PreProcessInterceptor,
//		AcceptedByMethod {
//	private static final Logger LOGGER = Logger
//			.getLogger(SecurityCheckInterceptor.class);
//
//	@Nullable
//	@Override
//	public ServerResponse preProcess(final HttpRequest request,
//			final ResourceMethod method) throws Failure,
//			WebApplicationException {
//		final List<String> authToken = request.getHttpHeaders()
//				.getRequestHeader("X-AUTH");
//
//		if (authToken == null || !isValidToken(authToken.get(0))) {
//			final ServerResponse serverResponse = new ServerResponse();
//			serverResponse.setStatus(Response.Status.UNAUTHORIZED
//					.getStatusCode());
//			return serverResponse;
//		}
//
//		return null;
//	}
//
//	private static boolean isValidToken(@Nonnull final String authToken) {
//		LOGGER.info("validating token: " + authToken);
//		return true;
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Override
//	public boolean accept(final Class declaring, final Method method) {
//		// return declaring.isAnnotationPresent(SecurityChecked.class); // if
//		// annotation on class
//		return method.isAnnotationPresent(SecurityChecked.class);
//	}
//}