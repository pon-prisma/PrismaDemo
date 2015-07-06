package it.prisma.dal.dao.paas.services.appaas;

import it.prisma.dal.dao.GenericDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

/**
 * APPaaSDAO class which implements the DAO for the {@link APPaaS} class. <br/>
 * <b>Note that this is a virtual Entity which is remapped to
 * {@link PaaSService} entity.</b>
 */

@Local(APPaaSDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class APPaaSDAO implements GenericDAO<AppaaS, Long> {

	@Inject
	private PaaSServiceDAO paaSServiceDAO;

	public APPaaSDAO() {

	}

	public AppaaS findByNameAndWorkgroup(String appName, Long workgroupId) {
		PaaSService paasSvc = paaSServiceDAO.findByTypeAndNameAndWorkgroup(
				PaaSService.PaaSServiceType.APPaaS, appName, workgroupId);
		if (paasSvc == null)
			return null;

		return new AppaaS(paasSvc);
	}

	/**
	 * At least one of the parameters MUST be specified.
	 * 
	 * @param userAccountId
	 * @param workgroupId
	 * @param considerDeletedToo specify if the deleted AppaaS have to be included
	 * @return
	 */
	public Collection<AppaaS> findByUserAndWorkgroup(Long userAccountId,
			Long workgroupId, boolean considerDeletedToo) {
		Set<AppaaS> result = new HashSet<AppaaS>();
		Collection<PaaSService> services = paaSServiceDAO
				.findByTypeAndUserAndWorkgroup(
						PaaSService.PaaSServiceType.APPaaS, userAccountId,
						workgroupId, considerDeletedToo);

		for (PaaSService service : services)
			result.add(new AppaaS(service));

		return result;
	}

	@Override
	public AppaaS create(AppaaS entity) {
		paaSServiceDAO.create(entity.getPaaSService());
		return entity;
	}

	@Override
	public AppaaS findById(Long id) {
		if (id == null)
			throw new NullPointerException("AppId cannot be null.");

		try {
			return new AppaaS(paaSServiceDAO.findById(id));
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * @param considerDeletedToo specify if the deleted AppaaS have to be included
	 * @return
	 */
	public Collection<AppaaS> findAll(boolean considerDeletedToo) {
		Collection<PaaSService> results = paaSServiceDAO.findAll(considerDeletedToo);
		Collection<AppaaS> appaasResult = new HashSet<AppaaS>();
		for (PaaSService item : results)
			appaasResult.add(new AppaaS(item));

		return appaasResult;
	}
	
	/**
	 * <b> Deleted AppaaS are ignored </b>
	 */
	/* (non-Javadoc)
	 * @see it.prisma.dal.dao.GenericDAO#findAll()
	 */
	@Override
	public Collection<AppaaS> findAll() {
		return findAll(false);
	}


	/**
	 * @param considerDeletedToo specify if the deleted AppaaS have to be included
	 * @return
	 */
	public long count(boolean considerDeletedToo) {
		return paaSServiceDAO.countByType(PaaSServiceType.APPaaS, considerDeletedToo);
	}
	
	/**
	 * <b> Deleted AppaaS are ignored </b>
	 */
	/* (non-Javadoc)
	 * @see it.prisma.dal.dao.GenericDAO#count()
	 */
	@Override
	public long count() {
		return count(false);
	}

	@Override
	public boolean exists(Long id) {
		// TODO Improve
		return count() > 0;
	}

	@Override
	public AppaaS update(AppaaS entity) {
		entity.setPaaSService(paaSServiceDAO.update(entity.getPaaSService()));
		return entity;
	}

	@Override
	public void delete(Long id) {
		paaSServiceDAO.delete(id);
	}

	@Override
	public void forceFlush() {
		paaSServiceDAO.forceFlush();
	}
}
