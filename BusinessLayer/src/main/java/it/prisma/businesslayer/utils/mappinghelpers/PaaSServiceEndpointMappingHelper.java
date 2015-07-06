package it.prisma.businesslayer.utils.mappinghelpers;

import it.prisma.dal.entities.paas.services.PaaSServiceEndpoint;
import it.prisma.domain.dsl.paas.services.PaaSServiceEndpointRepresentation;

public class PaaSServiceEndpointMappingHelper
		extends
		MappingHelperBase<PaaSServiceEndpoint, PaaSServiceEndpointRepresentation> {

	@Override
	public PaaSServiceEndpoint getEntity(PaaSServiceEndpointRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public PaaSServiceEndpointRepresentation getDSL(PaaSServiceEndpoint obj) {
		PaaSServiceEndpointRepresentation dsl = new PaaSServiceEndpointRepresentation();

		dsl.setId(obj.getPaasServiceEndpointId());
		dsl.setName(obj.getName());
		dsl.setType(obj.getType());
		dsl.setUri(obj.getUri());

		return dsl;
	}
}
