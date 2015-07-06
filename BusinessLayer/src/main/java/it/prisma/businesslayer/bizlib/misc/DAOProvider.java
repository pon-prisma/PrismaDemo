package it.prisma.businesslayer.bizlib.misc;

import it.prisma.dal.dao.GenericDAO;

import java.lang.reflect.ParameterizedType;

import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Singleton
public class DAOProvider {

	@Inject
	private Instance<GenericDAO<?, ?>> daoInstances;

	public <ENTITY, ID> GenericDAO<ENTITY, ID> getPaaSServiceDAO(ENTITY entity) {
		return (GenericDAO<ENTITY, ID>) getPaaSServiceDAO(entity.getClass());
	}

	public <ENTITY, ID> GenericDAO<ENTITY, ID> getPaaSServiceDAO(
			Class<ENTITY> entityClass) {

		for (GenericDAO dao : daoInstances) {
			Class clz = dao.getClass();
			while (!(clz.getGenericSuperclass() instanceof ParameterizedType)
					&& clz.getSuperclass() != Object.class)
				clz = clz.getSuperclass();
			if (clz.getGenericSuperclass() instanceof ParameterizedType) {

				if (((ParameterizedType) clz.getGenericSuperclass())
						.getActualTypeArguments()[0].equals(entityClass))
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
				+ entityClass);
	}
}
