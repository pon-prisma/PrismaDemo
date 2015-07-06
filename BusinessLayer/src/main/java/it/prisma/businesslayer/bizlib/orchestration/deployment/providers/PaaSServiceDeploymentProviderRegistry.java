package it.prisma.businesslayer.bizlib.orchestration.deployment.providers;

import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Singleton
public class PaaSServiceDeploymentProviderRegistry {

	Map<String, PaaSServiceDeploymentProvider<?, ?>> providers = new HashMap<String, PaaSServiceDeploymentProvider<?, ?>>();

	@Inject
	@PaaSDeploymentProvider
	Instance<PaaSServiceDeploymentProvider<?, ?>> instance;

	// @PostConstruct
	// private void init() {
	// registerProviders();
	// }
	//
	// private void registerProviders() {
	//
	// Reflections reflections = new Reflections();
	// Set<Class<?>> annotated = reflections
	// .getTypesAnnotatedWith(PaaSDeploymentProvider.class);
	// for(Class<?> clazz : annotated) {
	// providers.add(clazz.getAnnotation(PaaSDeploymentProvider.class).paasType(),clazz);
	// }
	// }

	public PaaSServiceDeploymentProvider<?, ?> getProviderByPaaSType(String type) {
		return getProviderByPaaSType(PaaSServiceType.valueOf(type));
	}

	public PaaSServiceDeploymentProvider<?, ?> getProviderByPaaSType(
			PaaSServiceType type) {
		for (PaaSServiceDeploymentProvider<?, ?> provider : instance) {
			if (provider.getPaaSServiceType().equals(type))
				return provider;
		}
		throw new IllegalArgumentException("No provider found for type " + type);
	}
}
