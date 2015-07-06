package it.prisma.businesslayer.bizlib.orchestration.deployment.providers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * This annotation is used to register a {@link PaaSServiceDeploymentProvider}
 * to the singleton {@link PaaSServiceDeploymentProviderRegistry}.
 * 
 * @author l.biava
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD,
		ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface PaaSDeploymentProvider {
	/**
	 * @see PaaSService#PaaSServiceType
	 * @return
	 */
	//String paasType();
}
