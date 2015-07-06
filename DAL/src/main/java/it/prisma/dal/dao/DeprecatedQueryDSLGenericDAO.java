package it.prisma.dal.dao;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.PathBuilder;

/**
 * Deprecated class, throwing {@link EntityNotFoundException} in case of entry
 * not found. DO NOT USE IT ANY MORE ! Causes EJB Rollback...
 * 
 * @author l.biava
 *
 * @param <EntityClass>
 * @param <IDClass>
 */
@Deprecated
public abstract class DeprecatedQueryDSLGenericDAO<EntityClass, IDClass>
		extends DeprecatedAbstractGenericDAO<EntityClass, IDClass> implements
		GenericDAO<EntityClass, IDClass> {

	// Note: You don't need to instantiate the EntityManager by hand, your
	// container does that
	// for you because of the @PersistenceContext annotation. Also, you don't
	// need to begin the
	// transaction manually, it's also provided by your containter.

	private PathBuilder<EntityClass> qObject;

	/**
	 * Constructor which assign the passed EntityClass to the GenericDAO.
	 * 
	 * @param entityClass
	 *            The assigned Class<EntityClass>.
	 */
	public DeprecatedQueryDSLGenericDAO(Class<EntityClass> entityClass) {
		super(entityClass);
	}

	@PostConstruct
	private void initQueryDSL() {
		qObject = new PathBuilder<EntityClass>(super.getEntityClass(), "q"
				+ super.getEntityClassSimpleName());
	}

	public Collection<EntityClass> findAll() {
		assert (qObject != null);
		List<EntityClass> result = prepareJPAQuery().from(qObject)
				.list(qObject);
		if (result.isEmpty())
			throw new EntityNotFoundException("Cannot find entities "
					+ entityClass.getCanonicalName());
		else
			return result;
	}

	public long count() {
		assert (qObject != null);
		return prepareJPAQuery().from(qObject).count();
	}

	protected JPAQuery prepareJPAQuery() {
		return new JPAQuery(super.getEntityManager());
	}

	protected PathBuilder<EntityClass> getQ() {
		assert (qObject != null);
		return qObject;
	}

}
