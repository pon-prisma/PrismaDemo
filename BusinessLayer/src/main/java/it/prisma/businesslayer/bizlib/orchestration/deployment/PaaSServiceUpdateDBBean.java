package it.prisma.businesslayer.bizlib.orchestration.deployment;

import it.prisma.dal.dao.GenericDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;

import java.lang.reflect.ParameterizedType;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Stateless
public class PaaSServiceUpdateDBBean implements PaaSServiceUpdateDB {

	@Inject
	private PaaSServiceDAO superPaasServiceDAO;

	@Inject
	private Instance<GenericDAO<?, ?>> daoInstances;

	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public AbstractPaaSService createPaaSService(AbstractPaaSService paasService) {
		// TODO: Improve
		superPaasServiceDAO.create(paasService.getPaaSService());
		GenericDAO paasServiceDAO = getPaaSServiceDAO(paasService);
		AbstractPaaSService paas = (AbstractPaaSService) paasServiceDAO
				.create(paasService);
		// superPaasServiceDAO.forceFlush();
		// paasServiceDAO.forceFlush();
		return paas;
	}

	@Override
	public AbstractPaaSService updatePaaSService(AbstractPaaSService paasService) {
		// TODO: Update PaaSDeployment ??
		// if(paasService.getPaaSService().getPaaSDeployment().getId()==null)
		// paasDeploymentDAO.create(paasService.getPaaSService().getPaaSDeployment());
		// else
		// paasDeploymentDAO.update(paasService.getPaaSService().getPaaSDeployment());

		superPaasServiceDAO.update(paasService.getPaaSService());
		GenericDAO paasServiceDAO = getPaaSServiceDAO(paasService);

		AbstractPaaSService paas = (AbstractPaaSService) paasServiceDAO
				.update(paasService);
		//superPaasServiceDAO.forceFlush();
		//paasServiceDAO.forceFlush();

		// WARNING: Maybe should return the merged instance, but it would be
		// detached in the caller because of the nested transaction end
		return paasService;
	}

	protected <ENTITY, ID> GenericDAO<ENTITY, ID> getPaaSServiceDAO(
			ENTITY entity) {
		for (GenericDAO dao : daoInstances) {
			Class clz = dao.getClass();
			while (!(clz.getGenericSuperclass() instanceof ParameterizedType)
					&& clz.getSuperclass() != Object.class)
				clz = clz.getSuperclass();
			if (clz.getGenericSuperclass() instanceof ParameterizedType) {

				if (((ParameterizedType) clz.getGenericSuperclass())
						.getActualTypeArguments()[0].equals(entity.getClass()))
					return dao;
			}
			// Class clz = dao.getClass();
			// while (clz.getSuperclass() != AbstractGenericDAO.class
			// && clz != Object.class)
			// clz = clz.getSuperclass();
			// if (clz.getSuperclass() == AbstractGenericDAO.class) {
			//
			// if (((ParameterizedType) clz.getGenericSuperclass())
			// .getActualTypeArguments()[0].equals(entity.getClass()))
			// return dao;
			// }
		}
		throw new NullPointerException("Cannot find DAO for PaaSService "
				+ entity.getClass());
	}
}
