package it.prisma.businesslayer.bizlib.paas.services.appaas;

import it.prisma.businesslayer.bizlib.common.exceptions.DuplicatedResourceException;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSEnvironmentDAO;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class APPaaSManagementBean implements APPaaSManagement {

	@Inject
	protected APPaaSDAO appaaSDAO;

	@Inject
	protected APPaaSEnvironmentDAO appaaSEnvDAO;

	@Inject
	protected PaaSServiceDAO paaSServiceDAO;

	@Inject
	protected PaaSServiceEventDAO paaSServiceEventDAO;

	@Override
	public AppaaS getAPPaaSById(Long appId) {
		return appaaSDAO.findById(appId);
	}

	@Override
	public Collection<AppaaS> getAPPaaSByUserAndWorkgroup(Long workgroupId,
			Long userAccountId, boolean considerDeletedToo) {
		return appaaSDAO.findByUserAndWorkgroup(userAccountId, workgroupId, considerDeletedToo);
	}

	@Override
	public AppaaSEnvironment getAPPaaSEnvironment(Long appId, Long envId) {
		return appaaSEnvDAO.findById(envId);
	}

	@Override
	public PaaSService updateAPPaaSById(Long appId,
			APPaaSRepresentation appRepresentation) {

		PaaSService appEntity = paaSServiceDAO.findById(appId);
		appEntity.setDescription(appRepresentation.getDescription());
		return paaSServiceDAO.update(appEntity);
	}

	@Override
	public Collection<AppaaSEnvironment> getAPPaaSEnvironmentByAppId(Long appId, boolean considerDeletedToo) {
		return appaaSEnvDAO.findByAppId(appId, considerDeletedToo);
	}

	@Override
	public Collection<PaaSServiceEvent> getAPPaaSEnvironmentEvents(Long appId,
			Long envId, Long from, Long to, Long size) {
		return paaSServiceEventDAO.findByPaaSServiceIdWithFilters(envId, from,
				to, size);
	}

	@Override
	public AppaaS checkAPPaaSExists(Long appId) {
		AppaaS app;
		if ((app = appaaSDAO.findById(appId)) == null)
			throw new ResourceNotFoundException(AppaaS.class, "id="+appId);
		return app;
	}

	@Override
	public void checkAPPaaSDuplicated(Long workgroupId, String name) {
		if (appaaSDAO.findByNameAndWorkgroup(name, workgroupId) != null)
			throw new DuplicatedResourceException(AppaaS.class);
	}

	@Override
	public void checkAPPaaSEnvironmentDuplicated(Long workgroupId,
			String envName) {
		AppaaSEnvironment env = appaaSEnvDAO.findByName(envName);
		if (env != null
				&& env.getPaaSService().getWorkgroup().getId() == workgroupId)
			throw new DuplicatedResourceException(AppaaSEnvironment.class);
	}

}
