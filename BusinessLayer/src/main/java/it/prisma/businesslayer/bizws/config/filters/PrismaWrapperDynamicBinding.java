package it.prisma.businesslayer.bizws.config.filters;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaExceptionMapper;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This dynamic binding provider registers
 * {@link PrismaRestWSResponseWrapperFilter} and {@link PrismaExceptionMapper}
 * only for classes and methods annotated with {@link PrismaWrapper}.
 * */
@Provider
public class PrismaWrapperDynamicBinding implements DynamicFeature {
	// NOTE: In case of compiler error, just set the problem to warning level.
	// This IS NOT an error !

	private static final Logger LOG = LogManager
			.getLogger(PrismaWrapperDynamicBinding.class);

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		PrismaWrapper classPrismaWrapperAnnotation = (PrismaWrapper) resourceInfo
				.getResourceClass().getAnnotation(PrismaWrapper.class);

		PrismaWrapper methodPrismaWrapperAnnotation = (PrismaWrapper) resourceInfo
				.getResourceMethod().getAnnotation(PrismaWrapper.class);

		// Method annotation priority (override)
		if (methodPrismaWrapperAnnotation != null) {
			if (methodPrismaWrapperAnnotation.enabled()) {
				registerComponents(resourceInfo, context,
						methodPrismaWrapperAnnotation);
			}
			return;
		}

		// Class annotation if method annotation not set
		if (classPrismaWrapperAnnotation != null
				&& classPrismaWrapperAnnotation.enabled()) {
			registerComponents(resourceInfo, context,
					classPrismaWrapperAnnotation);
		}
	}

	private void registerComponents(ResourceInfo resourceInfo,
			FeatureContext context, PrismaWrapper prismaWrapperAnnotation) {
		if (prismaWrapperAnnotation.restParamsEnabled()) {
			context.register(PrismaRestWSParamsEnablerRequestFilter.class);
			LOG.debug("Enabled RESTWSParams for "
					+ resourceInfo.getResourceClass() + "."
					+ resourceInfo.getResourceMethod() + "().");
		}

		context.register(PrismaRestWSResponseWrapperFilter.class);

		LOG.debug("Configured for " + resourceInfo.getResourceClass() + "."
				+ resourceInfo.getResourceMethod() + "().");
		// NOT WORKING
		// context.register(PrismaExceptionMapper.class, ExceptionMapper.class);
	}
}