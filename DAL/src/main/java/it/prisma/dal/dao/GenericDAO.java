package it.prisma.dal.dao;

import java.util.Collection;

// Defines CRUD Operations for a given DAO
public interface GenericDAO<EntityClass, IDClass> {

	// Create

	public EntityClass create(EntityClass entity);

	// Read

	public EntityClass findById(final IDClass id);

	public Collection<EntityClass> findAll();

	public long count();

	public boolean exists(final IDClass id);

	// Update

	public EntityClass update(final EntityClass entity);

	// Delete

	public void delete(final IDClass id);
	
	// Utilities

	/**
	 * Call this method to flush the EntityManager.
	 */
	public void forceFlush();

}