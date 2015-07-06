package it.prisma.businesslayer.utils.mappinghelpers;

import it.prisma.dal.entities.orchestrator.lrt.LRTEvent;
import it.prisma.domain.dsl.orchestrator.LRTEventRepresentation;

public class LRTEventMappingHelper extends
		MappingHelperBase<LRTEvent, LRTEventRepresentation> {

	// private static final LRTEventMappingHelper instance = new
	// LRTEventMappingHelper();
	//
	// public static synchronized LRTEventMappingHelper me() {
	// return getInstance();
	// }
	// public static synchronized LRTEventMappingHelper getInstance() {
	// return instance;
	// }

	@Override
	public LRTEvent getEntity(LRTEventRepresentation obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public LRTEventRepresentation getDSL(LRTEvent obj) {
		if (obj == null)
			return null;

		LRTEventRepresentation dsl = new LRTEventRepresentation();

		dsl.setCreatedAt(obj.getTimestamp().getTime());
		dsl.setData(obj.getData());
		dsl.setEventId(obj.getId());
		dsl.setEventType(obj.getEventType().toString());
		dsl.setLRTId(obj.getLrt().getId());

		return dsl;
	}

}
