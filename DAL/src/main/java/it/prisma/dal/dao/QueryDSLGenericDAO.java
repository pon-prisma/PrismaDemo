package it.prisma.dal.dao;

import it.prisma.dal.dao.queryparams.FilteringSpecification;
import it.prisma.dal.dao.queryparams.OrderSpecification;
import it.prisma.dal.dao.queryparams.OrderSpecification.OrderType;
import it.prisma.dal.dao.queryparams.annotations.DAOQueryParams;
import it.prisma.dal.dao.queryparams.exceptions.DAOFilteringParamsException;
import it.prisma.dal.dao.queryparams.exceptions.DAOOrderingParamsException;
import it.prisma.dal.dao.queryparams.exceptions.DAOQueryParamsException;
import it.prisma.dal.dao.queryparams.producers.DefaultDAOQueryParamsProducer;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PathBuilder;

/**
 * Implementation of a {@link GenericDAO}, using QueryDSL libraries. <br/>
 * <br/>
 * <b>Note</b> that this implementation provides automatic query constraints
 * additions (pagination, ordering, filtering), through the use of the injection
 * of {@link it.prisma.dal.dao.queryparams.DAOQueryParams}. <br/>
 * Please, provide a proper <b>specialization</b> of the
 * {@link DefaultDAOQueryParamsProducer} to customize this facility. For
 * example:
 * 
 * <pre>
 * &#064;RequestScoped
 * public class RestWSDAOQueryParamsProducer extends DefaultDAOQueryParamsProducer {
 * 
 *  &#064;Inject
 *  &#064;PrismaRestWSParams
 *  RestWSParamsContainer restWSParams;
 * 
 *  &#064;RequestScoped
 *  &#064;Produces
 *  &#064;DAOQueryParams
 *  &#064;Specializes
 *  &#064;Override
 *  public it.prisma.dal.dao.queryparams.DAOQueryParams createDAOQueryParams() {
 * 
 * 	 DAOPaginationParams paginationParams = new DAOPaginationParams(restWSParams
 * 			.getPaginationParams().getLimit(), restWSParams
 * 			.getPaginationParams().getOffset());
 * 
 * 	 [...]
 * 
 * 	 return new it.prisma.dal.dao.queryparams.DAOQueryParams(orderingParams,
 * 			paginationParams);
 *  }
 * </pre>
 * 
 * @author l.biava
 *
 * @param <EntityClass>
 * @param <IDClass>
 */
public abstract class QueryDSLGenericDAO<EntityClass, IDClass> extends
		AbstractGenericDAO<EntityClass, IDClass> implements
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
	public QueryDSLGenericDAO(Class<EntityClass> entityClass) {
		super(entityClass);
	}

	@PostConstruct
	private void initQueryDSL() {
		qObject = new PathBuilder<EntityClass>(super.getEntityClass(), "q"
				+ super.getEntityClassSimpleName());
	}

	public Collection<EntityClass> findAll() {
		assert (qObject != null);
		return prepareJPAQuery().from(qObject).list(qObject);
	}

	public long count() {
		assert (qObject != null);
		return prepareJPAQuery().from(qObject).count();
	}

	/**
	 * @see QueryDSLGenericDAO#prepareJPAQuery(boolean)
	 * @return a {@link JPAQuery} with automatically added constraints from
	 *         {@link it.prisma.dal.dao.queryparams.DAOQueryParams}.
	 */
	protected JPAQuery prepareJPAQuery() {
		return prepareJPAQuery(null, true);
	}

	/**
	 * @see QueryDSLGenericDAO#prepareJPAQuery(boolean)
	 * @param fromEntity
	 *            the entity table to execute the query from (<b>DO NOT request
	 *            another 'from' from the same table to the query</b>).
	 * @return a {@link JPAQuery} with automatically added constraints from
	 *         {@link it.prisma.dal.dao.queryparams.DAOQueryParams}.
	 */
	protected JPAQuery prepareJPAQuery(EntityPath<?> fromEntity) {
		return prepareJPAQuery(fromEntity, true);
	}

	/**
	 * This method sets up a {@link JPAQuery}, automatically adding constraints
	 * from {@link it.prisma.dal.dao.queryparams.DAOQueryParams} if enabled.
	 * 
	 * @param fromEntity
	 *            the entity table to execute the query from (<b>DO NOT request
	 *            another 'from' from the same table to the query</b>).
	 * @param enableParams
	 *            <tt>true</tt> if constraints from
	 *            {@link it.prisma.dal.dao.queryparams.DAOQueryParams} must be
	 *            added, <tt>false</tt> otherwise.
	 * @return the query for further operations.
	 */
	protected JPAQuery prepareJPAQuery(EntityPath<?> fromEntity,
			boolean enableParams) throws DAOQueryParamsException {
		JPAQuery query = new JPAQuery(getEntityManager());

		if (fromEntity != null)
			query.from(fromEntity);

		it.prisma.dal.dao.queryparams.DAOQueryParams params;
		// Use automatically query params - if enabled
		try {
			if (enableParams && (params = getSessionQueryParams()) != null
					&& !params.isDisabled()) {
				// Pagination parameters
				query.limit(params.getPaginationParams().getLimit());
				query.offset(params.getPaginationParams().getOffset());

				// Ordering parameters
				if (params.getOrderingParams() != null) {
					for (OrderSpecification orderSpec : params
							.getOrderingParams().getOrderSpecs()) {
						Path<?> field = getOrderingEntityPathFromFieldName(orderSpec
								.getField());

						if (field == null)
							throw new DAOOrderingParamsException("Order by "
									+ orderSpec.getField()
									+ " not supported for this entity.");

						if (orderSpec.getOrder() == OrderType.ASC)
							query.orderBy(((ComparableExpressionBase<?>) field)
									.asc());
						else
							query.orderBy(((ComparableExpressionBase<?>) field)
									.desc());
					}
				}

				// Ordering parameters
				if (params.getFilteringParams() != null) {
					for (FilteringSpecification filterSpec : params
							.getFilteringParams().getFilteringSpecs()) {
						Path<?> fieldPath = getFilteringEntityPathFromFieldName(filterSpec
								.getField());

						if (fieldPath == null)
							throw new DAOFilteringParamsException(
									"Filtering by " + filterSpec.getField()
											+ " not supported for this entity.");

						ComparableExpressionBase<?> field = (ComparableExpressionBase<?>) fieldPath;

						Class<?> valueType = fieldPath.getType();
						Object value = null;
						if (valueType.equals(Long.class)) {
							value = new Long(filterSpec.getValue());
						}
						if (valueType.equals(Integer.class)) {
							value = new Integer(filterSpec.getValue());
						}
						if (valueType.equals(String.class)) {
							value = filterSpec.getValue();
						}
						if (value == null)
							throw new DAOFilteringParamsException(
									"Value type not supported, requested "
											+ valueType + ".");

						switch (filterSpec.getOperator()) {
						case EQUALS:
							Expression<?> constant = Expressions
									.constant(value);
							query.where(Expressions.predicate(Ops.EQ, field,
									constant));
							// query.where(field.eq((Expression<?>)
							// Expressions.constant(filterSpec.getValue())));
							break;
						default:
							throw new DAOFilteringParamsException(
									"Filtering by " + filterSpec.getField()
											+ " not supported for this entity.");
						}
					}
				}
			}
		} catch (Exception e) {
			if (e.getMessage().contains("Undeclared path"))
				throw new DAOFilteringParamsException(
						"Filtering of this entity is not supported.");

			throw new DAOQueryParamsException(
					"Problem using given DAOQueryParams: " + e.getMessage(), e);
		}
		return query;
	}

	@Inject
	@DAOQueryParams
	protected it.prisma.dal.dao.queryparams.DAOQueryParams queryParams;

	protected it.prisma.dal.dao.queryparams.DAOQueryParams getSessionQueryParams() {
		return queryParams;
	}

	protected PathBuilder<EntityClass> getQ() {
		assert (qObject != null);
		return qObject;
	}

	/**
	 * Executes the query, returning a list of results. It also logs the total
	 * items count for pagination parameters.
	 * 
	 * @param query
	 * @param id
	 *            the id of the entity
	 * @return
	 */
	@SuppressWarnings("unused")
	protected Collection<EntityClass> getListResults(JPAQuery query,
			EntityPathBase<EntityClass> qpath) {
		// TODO: Convert in an interceptor or something like it

		Collection<EntityClass> result = query.list(qpath);

		// If pagination is enabled, get total rows count
		if (getSessionQueryParams() != null
				&& !getSessionQueryParams().isDisabled()) {
			query.clone();
			getSessionQueryParams().getPaginationParams().setResultTotalCount(
					query.count());			
		}

		// SearchResults<?> a = query.listResults(qpath);
		// getSessionQueryParams().getPaginationParams().setResultTotalCount(
		// a.getTotal());

		return result;
	}

	/**
	 * Returns the proper {@link Path} for the specified entity field name.<br/>
	 * Should be overridden from entity-specific DAOs and could support a subset
	 * of original fields set. If the given field is not supported, should
	 * return <tt>null</tt>.
	 * 
	 * @param fieldName
	 *            the name of the field of the entity.
	 * @return the path for the given field of the entity, or <tt>null</tt> if
	 *         the given field is not supported.
	 */
	public Path<?> getEntityPathFromFieldName(String fieldName) {
		// return getQ().get(fieldName);
		return null;
	}

	/**
	 * Returns the proper {@link Path} for the specified entity field name, that
	 * will be used for filtering purpose.<br/>
	 * Should be overridden from entity-specific DAOs and could support a subset
	 * of original fields set. If the given field is not supported (in general
	 * or for filtering purpose), should return <tt>null</tt>.
	 * 
	 * @param fieldName
	 *            the name of the field of the entity.
	 * @return the path for the given field of the entity, or <tt>null</tt> if
	 *         the given field is not supported.
	 */
	public Path<?> getFilteringEntityPathFromFieldName(String fieldName) {
		// return getQ().get(fieldName);
		return null;
	}

	/**
	 * Returns the proper {@link Path} for the specified entity field name, that
	 * will be used for ordering purpose.<br/>
	 * Should be overridden from entity-specific DAOs and could support a subset
	 * of original fields set. If the given field is not supported (in general
	 * or for ordering purpose), should return <tt>null</tt>.
	 * 
	 * @param fieldName
	 *            the name of the field of the entity.
	 * @return the path for the given field of the entity, or <tt>null</tt> if
	 *         the given field is not supported.
	 */
	public Path<?> getOrderingEntityPathFromFieldName(String fieldName) {
		// return getQ().get(fieldName);
		return null;
	}
}
