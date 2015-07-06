package it.prisma.businesslayer.bizlib.paas.services.appaas;

import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;

import java.util.Collection;

public interface APPaaSManagement {

	// Read

	public AppaaS getAPPaaSById(final Long appId);

	public PaaSService updateAPPaaSById(final Long appId,
			APPaaSRepresentation appRepresentation);

	/**
	 * At least one of the given parameter MUST be specified.
	 * 
	 * @param workgroupId
	 * @param userAccountId
	 * @return
	 */
	public Collection<AppaaS> getAPPaaSByUserAndWorkgroup(
			final Long workgroupId, final Long userAccountId, boolean considerDeletedToo);

	public AppaaSEnvironment getAPPaaSEnvironment(final Long appId, Long envId);

	/**
	 * 
	 * @param appId
	 * @return a list of {@link AppaaSEnvironment} related to the given appId.
	 */
	public Collection<AppaaSEnvironment> getAPPaaSEnvironmentByAppId(
			final Long appId, boolean considerDeletedToo);

	/**
	 * 
	 * @param appId
	 *            the Id of the APPaaS (useful ?).
	 * @param envId
	 *            the Id of the APPaaSEnvironment.
	 * @param from
	 *            if > 0, filters events starting with the given timestamp.
	 * @param to
	 *            if > 0, filters events ending with the given timestamp.
	 * @param size
	 *            if > 0, limits the result size to the given value.
	 * @return a set of {@link PaaSServiceEvent} related to the given
	 *         APPaaSEnvironment and satisfying given conditions.
	 */
	public Collection<PaaSServiceEvent> getAPPaaSEnvironmentEvents(
			final Long appId, Long envId, final Long from, final Long to,
			final Long size);

	/**
	 * Checks if the the given App Id exists. If it does, the related entity is
	 * returned.
	 * 
	 * @param appId
	 * @return
	 * @throws ResourceNotFoundException
	 *             if the given App Id does not exists.
	 */
	public AppaaS checkAPPaaSExists(Long appId)
			throws ResourceNotFoundException;

	public void checkAPPaaSDuplicated(Long workgroupId, String name);

	public void checkAPPaaSEnvironmentDuplicated(Long workgroupId,
			String envName);
}
