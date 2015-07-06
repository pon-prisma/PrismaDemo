package it.prisma.businesslayer.utils.mappinghelpers;

import it.prisma.dal.entities.orchestrator.lrt.LRT;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent;
import it.prisma.domain.dsl.orchestrator.LRTEventRepresentation;
import it.prisma.domain.dsl.orchestrator.LRTRepresentation;

import java.util.HashSet;

import javax.inject.Inject;

public class LRTMappingHelper extends MappingHelperBase<LRT, LRTRepresentation> {

	@Inject
	protected MappingHelper<LRTEvent, LRTEventRepresentation> lrtEvtMH;

	@Override
	public LRT getEntity(LRTRepresentation obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public LRTRepresentation getDSL(LRT obj) {
		if (obj == null)
			return null;

		LRTRepresentation dsl = new LRTRepresentation();

		dsl.setInstanceId(obj.getInstanceId());
		dsl.setLrtid(obj.getId());
		dsl.setName(obj.getName());
		if (obj.getUserAccount() != null)
			dsl.setuserAccountId(obj.getUserAccount().getId());
		dsl.setStatus(obj.getStatus().toString());
		dsl.setEvents(new HashSet<LRTEventRepresentation>(lrtEvtMH.getDSL(obj.getLrtevents())));
		dsl.setResult(lrtEvtMH.getDSL(obj.getResult()));

		if (obj.getCompletedAt() != null)
			dsl.setCompletedAt(obj.getCompletedAt().getTime());
		if (obj.getCreatedAt() != null)
			dsl.setCreatedAt(obj.getCreatedAt().getTime());

		return dsl;
	}
}
