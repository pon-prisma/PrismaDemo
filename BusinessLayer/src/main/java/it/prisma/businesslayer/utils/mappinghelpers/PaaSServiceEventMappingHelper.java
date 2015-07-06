package it.prisma.businesslayer.utils.mappinghelpers;

import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;

public class PaaSServiceEventMappingHelper extends
		MappingHelperBase<PaaSServiceEvent, PaaSServiceEventRepresentation> {

	@Override
	public PaaSServiceEvent getEntity(PaaSServiceEventRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public PaaSServiceEventRepresentation getDSL(PaaSServiceEvent obj) {
		PaaSServiceEventRepresentation dsl = new PaaSServiceEventRepresentation();

		dsl.setCreatedAt(obj.getCreatedAt());
		dsl.setDetails(obj.getDetails());
		dsl.setEventId(obj.getId());
		dsl.setPaaSServiceId(obj.getPaaSService().getId());
		dsl.setType(obj.getType());
		dsl.setVerbose(obj.getVerbose());

		return dsl;
	}
}