package it.prisma.presentationlayer.webui.stereotypes;

import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation qualifies the injection of an instance of
 * {@link RestWSParamsContainer}. <br/>
 * Usage example:
 * 
 * <pre>
 * &#064;Inject
 * &#064;PrismaRestWSParams
 * RestWSParamsContainer restWSParams;
 * </pre>
 * 
 * <b>Note</b> that you should provide a valid producer (for example
 * {@link PrismaRestWSParamsProducer}).
 * 
 * @author l.biava
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrismaRestWSParams {
}