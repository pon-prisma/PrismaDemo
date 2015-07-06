package it.prisma.businesslayer.utils.mappinghelpers;

import java.util.Collection;

/**
 * This interface define a MappingHelper which is used to convert Entity to DSL
 * (Persistence Entities into JSON Pojos and viceversa).
 * 
 * @author l.biava
 *
 * @param <ENTITY>
 *            The type of the entity.
 * @param <DSL>
 *            The type of the DSL.
 */
public interface MappingHelper<ENTITY, DSL> {

	/**
	 * @return the class of the Entity.
	 */
	public Class<ENTITY> getEntityClass();

	/**
	 * @return the class of the DSL.
	 */
	public Class<DSL> getDSLClass();

	/**
	 * Converts a DSL into the related Entity.
	 * 
	 * @param obj
	 *            the DSL.
	 * @return the related Entity.
	 */
	public ENTITY getEntity(DSL obj);

	/**
	 * Converts a set of DSL into a set of the related Entities.
	 * 
	 * @param objs
	 *            the set of DSL.
	 * @return the set of related Entities.
	 */
	public Collection<ENTITY> getEntity(Collection<DSL> objs);

	/**
	 * Converts an Entity into the related DSL.
	 * 
	 * @param obj
	 *            the Entity.
	 * @return the related DSL.
	 */
	public DSL getDSL(ENTITY obj);

	/**
	 * Converts a set of Entities into a set of related DSLs.
	 * 
	 * @param objs
	 *            the set of Entities.
	 * @return the set of related DSL.
	 */
	public Collection<DSL> getDSL(Collection<ENTITY> objs);
}
