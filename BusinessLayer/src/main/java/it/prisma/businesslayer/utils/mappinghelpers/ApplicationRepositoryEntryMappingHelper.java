package it.prisma.businesslayer.utils.mappinghelpers;

import it.prisma.dal.entities.paas.services.appaas.ApplicationRepositoryEntry;
import it.prisma.domain.dsl.paas.services.appaas.response.ApplicationRepositoryEntryRepresentation;

public class ApplicationRepositoryEntryMappingHelper extends
		MappingHelperBase<ApplicationRepositoryEntry, ApplicationRepositoryEntryRepresentation> {

	@Override
	public ApplicationRepositoryEntry getEntity(ApplicationRepositoryEntryRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public ApplicationRepositoryEntryRepresentation getDSL(ApplicationRepositoryEntry obj) {
		ApplicationRepositoryEntryRepresentation dsl = new ApplicationRepositoryEntryRepresentation();

		dsl.setApplicationName(obj.getApplicationName());
		dsl.setCreatedAt(obj.getCreatedAt());
		dsl.setDescription(obj.getDescription());
		dsl.setFileName(obj.getFileName());
		//dsl.setFilePath(obj.getFilePath());
		dsl.setFileSize(obj.getFileSize());
		dsl.setId(obj.getId());
		dsl.setOwnerUserId(obj.getUserAccount().getId());
		dsl.setOwnerWorkgroupId(obj.getWorkgroup().getId());
		dsl.setPublic(obj.isShared());
		dsl.setTag(obj.getTag());

		return dsl;
	}

}
