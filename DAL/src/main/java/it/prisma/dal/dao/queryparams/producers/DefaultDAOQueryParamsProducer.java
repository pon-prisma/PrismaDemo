package it.prisma.dal.dao.queryparams.producers;

import it.prisma.dal.dao.queryparams.DAOFilteringParams;
import it.prisma.dal.dao.queryparams.DAOOrderingParams;
import it.prisma.dal.dao.queryparams.DAOPaginationParams;
import it.prisma.dal.dao.queryparams.annotations.DAOQueryParams;

import javax.enterprise.inject.Produces;

/**
 * Default producer for {@link it.prisma.dal.dao.queryparams.DAOQueryParams}.
 * 
 * @author l.biava
 *
 */
public class DefaultDAOQueryParamsProducer {

	/**
	 * Produces a default instance of
	 * {@link it.prisma.dal.dao.queryparams.DAOQueryParams}, with offset = 0 and
	 * limit = 20.
	 * 
	 * @return
	 */
	@Produces
	@DAOQueryParams
	public it.prisma.dal.dao.queryparams.DAOQueryParams createDAOQueryParams() {

		DAOPaginationParams paginationParams = new DAOPaginationParams(20L, 0L);

		DAOOrderingParams orderingParams = new DAOOrderingParams();
		
		DAOFilteringParams filteringParams = new DAOFilteringParams();

		return new it.prisma.dal.dao.queryparams.DAOQueryParams(orderingParams,
				paginationParams, filteringParams);
	}
}
