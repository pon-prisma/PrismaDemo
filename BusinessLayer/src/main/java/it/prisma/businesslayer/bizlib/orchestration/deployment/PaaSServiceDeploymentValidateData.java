package it.prisma.businesslayer.bizlib.orchestration.deployment;

import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

public interface PaaSServiceDeploymentValidateData {

	/**
	 * Generates a {@link AbstractPaaSService} entity based on deployment
	 * request, also validating data.
	 * 
	 * @param deployRequest
	 * @return the newly generated {@link AbstractPaaSService} NOT PERSISTED !
	 */
	public AbstractPaaSService generateAndValidatePaaSService(
			GenericPaaSServiceDeployRequest<?> deployRequest);
}
