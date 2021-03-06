package it.prisma.businesslayer.utils.mappinghelpers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A base implementation of the {@link MappingHelper}.
 * 
 * @author l.biava
 *
 * @param <ENTITY>
 * @param <DSL>
 */
public abstract class MappingHelperBase<ENTITY, DSL> implements
		MappingHelper<ENTITY, DSL> {

	protected Class<ENTITY> entityClass;
	protected Class<DSL> dslClass;

	protected MappingHelperBase() {
	}

	public MappingHelperBase(Class<ENTITY> entityClass, Class<DSL> dslClass) {
		this.entityClass = entityClass;
		this.dslClass = dslClass;
	}

	@Override
	public Class<ENTITY> getEntityClass() {
		return entityClass;
	}

	@Override
	public Class<DSL> getDSLClass() {
		return dslClass;
	}

	/**
	 * @return the actual collection for the
	 *         {@link MappingHelperBase#getEntity(Collection)} method
	 *         implementation.
	 */
	protected Collection<ENTITY> getEntitiesCollection() {
		return new ArrayList<ENTITY>();
	}

	/**
	 * @return the actual collection for the
	 *         {@link MappingHelperBase#getEntity(Collection)} method
	 *         implementation.
	 */
	protected Collection<DSL> getDSLCollection() {
		return new ArrayList<DSL>();
	}

	/**
	 * Default implementation, using the collection generated by
	 * {@link MappingHelperBase#getEntitiesCollection()}.
	 * 
	 * @see MappingHelper#getEntity(Collection).
	 */
	@Override
	public Collection<ENTITY> getEntity(Collection<DSL> objs) {
		Collection<ENTITY> entities = getEntitiesCollection();
		for (DSL obj : objs) {
			entities.add(getEntity(obj));
		}
		return entities;
	}

	/**
	 * Default implementation, using the collection generated by
	 * {@link MappingHelperBase#getDSLCollection()}.
	 * 
	 * @see MappingHelper#getDSL(Collection).
	 */
	@Override
	public Collection<DSL> getDSL(Collection<ENTITY> objs) {
		Collection<DSL> dsls = getDSLCollection();
		for (ENTITY obj : objs) {
			dsls.add(getDSL(obj));
		}
		return dsls;
	}
}
