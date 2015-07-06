package it.prisma.businesslayer.bizws.config.annotations;

import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaExceptionMapper;
import it.prisma.businesslayer.bizws.config.filters.PrismaRestWSResponseWrapperFilter;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation provides the wrapping of a RESTful WS response (provided by
 * {@link PrismaRestWSResponseWrapperFilter}) and the automatic conversion of exceptions
 * in {@link PrismaResponseWrapper} errors (provided by
 * {@link PrismaExceptionMapper}).
 * 
 * @author l.biava
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface PrismaWrapper {

	public boolean enabled() default true;
	public boolean restParamsEnabled() default false;
}
