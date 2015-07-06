// CHECKSTYLE:OFF
/**
 * Source code generated by Fluent Builders Generator
 * Do not modify this file
 * See generator home page at: http://code.google.com/p/fluent-builders-generator-eclipse-plugin/
 */

package it.prisma.dal.entities.paas.services.builders;

import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEndpoint;
import java.net.URI;

public class PaaSServiceEndpointBuilder extends
		PaaSServiceEndpointBuilderBase<PaaSServiceEndpointBuilder> {
	public static PaaSServiceEndpointBuilder paaSServiceEndpoint() {
		return new PaaSServiceEndpointBuilder();
	}

	public PaaSServiceEndpointBuilder() {
		super(new PaaSServiceEndpoint());
	}

	public PaaSServiceEndpoint build() {
		return getInstance();
	}
}

class PaaSServiceEndpointBuilderBase<GeneratorT extends PaaSServiceEndpointBuilderBase<GeneratorT>> {
	private PaaSServiceEndpoint instance;

	protected PaaSServiceEndpointBuilderBase(PaaSServiceEndpoint aInstance) {
		instance = aInstance;
	}

	protected PaaSServiceEndpoint getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withPaasServiceEndpointId(Long aValue) {
		instance.setPaasServiceEndpointId(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withPaasService(PaaSService aValue) {
		instance.setPaasService(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withType(String aValue) {
		instance.setType(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withName(String aValue) {
		instance.setName(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withUriToPersist(String aValue) {
		instance.setUriToPersist(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withUri(URI aValue) {
		instance.setUri(aValue);

		return (GeneratorT) this;
	}
}
