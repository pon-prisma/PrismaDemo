package it.prisma.businesslayer.utils.mappinghelpers;

import java.lang.reflect.ParameterizedType;

import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Singleton
public class MappingHelperProvider {
	@Inject
	private Instance<MappingHelper<?, ?>> mappingHelpers;

	// public Object getEntity(Object obj) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// public Collection getEntity(Collection objs) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// public <DSL,ENTITY> DSL getDSL(ENTITY obj) {
	// ParameterizedType superclass = (ParameterizedType)
	// getClass().getMethod("getDSL", parameterTypes);
	//
	// getMappingHelper(obj, Class.forName(DSL.));
	// return null;
	// }
	//
	// public Collection getDSL(Collection objs) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	/**
	 * EXPRIMENTAL
	 */
	public <ENTITY, DSL> MappingHelper<ENTITY, DSL> getMappingHelperA(
			ENTITY entityClass) throws ClassNotFoundException {
		for (MappingHelper<?, ?> mappingHelper : mappingHelpers) {
			Class<?> clz = mappingHelper.getClass();
			while (!(clz.getGenericSuperclass() instanceof ParameterizedType)
					&& clz.getSuperclass() != Object.class)
				clz = clz.getSuperclass();
			if (clz.getGenericSuperclass() instanceof ParameterizedType) {

				if (((ParameterizedType) clz.getGenericSuperclass())
						.getActualTypeArguments()[0].equals(entityClass
						.getClass()))
					try {
						return (MappingHelper<ENTITY, DSL>) mappingHelper;
					} catch (Exception e) {

					}
			}
		}

		throw new ClassNotFoundException(
				"Cannot find a MappingHelper for Entity "
						+ entityClass.getClass() + " and automatic DSL ");
	}

	/**
	 * Retrieve a {@link MappingHelper} suitable for the given
	 * <code>entityClass</code> and <code>dslClass</code>.
	 * 
	 * @param entityClass
	 *            the Class of the Entity.
	 * @param dslClass
	 *            the Class of the DSL.
	 * @return
	 * @throws ClassNotFoundException if not provider has been found
	 */
	public <ENTITY, DSL> MappingHelper<ENTITY, DSL> getMappingHelper(
			ENTITY entityClass, DSL dslClass) throws ClassNotFoundException {
		return (MappingHelper<ENTITY, DSL>) getMappingHelper(
				entityClass.getClass(), dslClass.getClass());
	}

	/**
	 * Retrieve a {@link MappingHelper} suitable for the given
	 * <code>entityClass</code> and <code>dslClass</code>.
	 * 
	 * @param entityClass
	 *            the Class of the Entity.
	 * @param dslClass
	 *            the DSL instance.
	 * @return
	 * @throws ClassNotFoundException if not provider has been found
	 */
	public <ENTITY, DSL> MappingHelper<ENTITY, DSL> getMappingHelper(
			Class<ENTITY> entityClass, DSL dslClass)
			throws ClassNotFoundException {
		return (MappingHelper<ENTITY, DSL>) getMappingHelper(entityClass,
				dslClass.getClass());
	}

	/**
	 * Retrieve a {@link MappingHelper} suitable for the given
	 * <code>entityClass</code> and <code>dslClass</code>.
	 * 
	 * @param entityClass
	 *            the instance of the Entity.
	 * @param dslClass
	 *            the Class of the DSL.
	 * @return
	 * @throws ClassNotFoundException if not provider has been found
	 */
	public <ENTITY, DSL> MappingHelper<ENTITY, DSL> getMappingHelper(
			ENTITY entityClass, Class<DSL> dslClass)
			throws ClassNotFoundException {
		return (MappingHelper<ENTITY, DSL>) getMappingHelper(
				entityClass.getClass(), dslClass);
	}

	/**
	 * Retrieve a {@link MappingHelper} suitable for the given
	 * <code>entityClass</code> and <code>dslClass</code>.
	 * 
	 * @param entityClass
	 *            the instance of the Entity.
	 * @param dslClass
	 *            the instance of the DSL.
	 * @return
	 * @throws ClassNotFoundException if not provider has been found
	 */
	public <ENTITY, DSL> MappingHelper<ENTITY, DSL> getMappingHelper(
			Class<ENTITY> entityClass, Class<DSL> dslClass)
			throws ClassNotFoundException {
		for (MappingHelper<?, ?> mappingHelper : mappingHelpers) {
			Class<?> clz = mappingHelper.getClass();
			while (!(clz.getGenericSuperclass() instanceof ParameterizedType)
					&& clz.getSuperclass() != Object.class)
				clz = clz.getSuperclass();
			if (clz.getGenericSuperclass() instanceof ParameterizedType) {

				if (((ParameterizedType) clz.getGenericSuperclass())
						.getActualTypeArguments()[0].equals(entityClass)
						&& ((ParameterizedType) clz.getGenericSuperclass())
								.getActualTypeArguments()[1].equals(dslClass))
					return (MappingHelper<ENTITY, DSL>) mappingHelper;
			}
		}

		throw new ClassNotFoundException(
				"Cannot find a MappingHelper for Entity "
						+ entityClass.getClass() + " and DSL "
						+ dslClass.getClass());
	}

}
