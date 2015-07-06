package it.prisma.dal.dao.queryparams.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

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
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER,
		ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface DAOQueryParams {
}