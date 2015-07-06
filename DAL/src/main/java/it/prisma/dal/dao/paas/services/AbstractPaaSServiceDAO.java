package it.prisma.dal.dao.paas.services;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.QPaaSService;

import java.util.Collection;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;


public abstract class AbstractPaaSServiceDAO<EntityClass, IDClass> extends QueryDSLGenericDAO<EntityClass, IDClass> {

	protected Class<EntityClass> entityClass;
	
	private StringPath deletedColumnPath;


	public AbstractPaaSServiceDAO(final Class<EntityClass> EntityClass) {
		super(EntityClass);
//		if (EntityClass.isInstance(AbstractPaaSService.class) || EntityClass.isInstance(PaaSService.class)) {
			deletedColumnPath = QPaaSService.paaSService.status;
//		} else {
//			throw new IllegalArgumentException(EntityClass.getCanonicalName() + " not supported by " + AbstractPaaSServiceDAO.class.getCanonicalName());
//		}
		this.entityClass = EntityClass;
	}

	public Collection<EntityClass> findAll(boolean considerDeletedToo) {
		PathBuilder<EntityClass> qObject = getQ();
		JPAQuery query = prepareJPAQuery().from(qObject);
		if (!considerDeletedToo) {
			query.where(ignoreDeletedPredicate());
		}
		return query.list(qObject);
	}

	/**
	 * <b> Deleted Entities are ignored </b>
	 */
	/* (non-Javadoc)
	 * @see it.prisma.dal.dao.QueryDSLGenericDAO#findAll()
	 */
	@Override
	public Collection<EntityClass> findAll() {
		return findAll(false);
	}
	
	public long count(boolean considerDeletedToo) {
		JPAQuery query = prepareJPAQuery().from(getQ());
		if (!considerDeletedToo) {
			query.where(ignoreDeletedPredicate());
		}
		return query.count();
	}
	
	/**
	 * <b> Deleted Entities are ignored </b>
	 */
	/* (non-Javadoc)
	 * @see it.prisma.dal.dao.QueryDSLGenericDAO#count()
	 */
	@Override
	public long count() {
		return count(false);
	}
	
	protected Predicate ignoreDeletedPredicate() {
		return deletedColumnPath.ne(PaaSService.Status.DELETED.toString());
	}
}