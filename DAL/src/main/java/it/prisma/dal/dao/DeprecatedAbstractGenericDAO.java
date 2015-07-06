package it.prisma.dal.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.validation.ConstraintViolationException;

/**
 * Deprecated class, throwing {@link EntityNotFoundException} in case of entry
 * not found. DO NOT USE IT ANY MORE ! Causes EJB Rollback...
 * 
 * @author l.biava
 *
 * @param <EntityClass>
 * @param <IDClass>
 */
// Note I: Declaring a class abstract only means that you don't allow it to be
// instantiated on its own.
@Deprecated
public abstract class DeprecatedAbstractGenericDAO<EntityClass, IDClass> implements GenericDAO<EntityClass, IDClass> {

	protected Class<EntityClass> entityClass;
	
	@PersistenceContext(unitName = "PrismaDAL")
	protected EntityManager entityManager;

	
	// Note II: You don't need to instantiate the EntityManager by hand, your container does that
	// for you because of the @PersistenceContext annotation. Also, you don't need to begin the
	// transaction manually, it's also provided by your containter.
	
	public DeprecatedAbstractGenericDAO(final Class<EntityClass> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * Method which creates the provided entity.
	 * 
	 * @param entity
	 *            The <i>entity</i> to be inserted.
	 * @return Returns the inserted <i>entity</i>. Use the returned instance for
	 *         further operations.
	 * @throws IllegalStateException
	 *             If there were an error opening the transaction or the
	 *             instance is not an Entity
	 * @throws PersistenceException
	 *             If an error occurred during the transaction
	 * @throws ConstraintViolationException
	 *             If the <i>entity</i> already exists in the DB. This means a
	 *             integrity constraint violation occurred.
	 * @throws SystemException 
	 * @throws NotSupportedException 
	 */
	@Override
	public EntityClass create(final EntityClass entity) {
		assert(entityManager != null);
		entityManager.persist(entity);
		return entity;
	}

	/**
	 * Method which deletes the <i>entity</i> identified by <i>id</i>.
	 * 
	 * @param id
	 *            The <i>id</i> of the <i>entity</i> to be deleted.
	 * @throws NullPointerException
	 *             If the <i>entity</i> identified by <i>id</i> doesn't exist in
	 *             the DB.
	 * @throws IllegalArgumentException
	 *             If the <i>id</i> is null.
	 * @throws IllegalStateException
	 *             If there were an error opening the transaction or the
	 *             instance is not an Entity
	 * @throws PersistenceException
	 *             If an error occurred during the transaction
	 */
	@Override
	public void delete(final IDClass id) throws NullPointerException,
			IllegalArgumentException, IllegalStateException,
			PersistenceException {
		assert(entityManager != null);
		entityManager.remove(findById(id));
	}

	/**
	 * Method which finds a <i>entity</i> by the provided <i>id</i>.
	 * 
	 * @param id
	 *            The <i>id</i> of the <i>entity</i> to be found.
	 * @return Returns the searched <i>entity</i>.
	 * @throws NullPointerException
	 *             If the <i>entity</i> identified by <i>id</i> doesn't exist in
	 *             the DB.
	 * @throws IllegalArgumentException
	 *             If the <i>id</i> is null.
	 */
	@Override
	public EntityClass findById(final IDClass id) throws EntityNotFoundException {
		assert(entityManager != null);	
		EntityClass entity = entityManager.find(entityClass, id);
		if (entity == null)
			throw new EntityNotFoundException("Cannot find entity "
					+ entityClass.getCanonicalName() + " with Id <" + id + ">");
		return entity;
	}
	
	@Override
	public boolean exists(final IDClass id) {
		assert(entityManager != null);
		boolean exists = false;
		try {
			this.findById(id);
			exists = true;
		} catch(EntityNotFoundException e) {
			exists = false;
		}
		return exists;
	}

	/**
	 * Method which updates the provided <i>entity</i>.
	 * 
	 * @param entity
	 *            The updated <i>entity</i>.
	 * @return Returns the updated <i>entity</i>. Use the returned instance for
	 *         further operations.
	 * @throws IllegalStateException
	 *             If there were an error opening the transaction or the
	 *             instance is not an Entity
	 * @throws PersistenceException
	 *             If an error occurred during the transaction
	 */
	@Override
	public EntityClass update(final EntityClass entity) throws IllegalStateException,
			PersistenceException {
		assert(entityManager != null);	
		return entityManager.merge(entity);
	}

	protected EntityManager getEntityManager() {
		assert(entityManager != null);
		return entityManager;
	}
	
	protected final Class<EntityClass> getEntityClass() {
		assert(entityManager != null);
		return entityClass;
	}
	
	protected final String getEntityClassName() {
		assert(entityClass != null);
		return entityClass.getName().toString();
	}
	
	protected final String getEntityClassSimpleName() {
		assert(entityClass != null);
		return entityClass.getSimpleName().toString();
	}
	
	@Override
	public void forceFlush() {
		entityManager.flush();
	}
	
}