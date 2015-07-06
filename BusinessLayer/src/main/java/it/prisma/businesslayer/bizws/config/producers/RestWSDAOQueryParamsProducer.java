package it.prisma.businesslayer.bizws.config.producers;

import it.prisma.businesslayer.bizws.config.annotations.PrismaRestWSParams;
import it.prisma.dal.dao.queryparams.DAOFilteringParams;
import it.prisma.dal.dao.queryparams.DAOOrderingParams;
import it.prisma.dal.dao.queryparams.DAOPaginationParams;
import it.prisma.dal.dao.queryparams.FilteringSpecification.OperatorType;
import it.prisma.dal.dao.queryparams.OrderSpecification.OrderType;
import it.prisma.dal.dao.queryparams.annotations.DAOQueryParams;
import it.prisma.dal.dao.queryparams.producers.DefaultDAOQueryParamsProducer;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.FilteringSpecification;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.OrderSpecification;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

/**
 * This class specializes a {@link DefaultDAOQueryParamsProducer}, producing
 * {@link it.prisma.dal.dao.queryparams.DAOQueryParams} using
 * {@link RestWSParamsContainer}.
 * 
 * @author l.biava
 *
 */
@RequestScoped
public class RestWSDAOQueryParamsProducer extends DefaultDAOQueryParamsProducer {

	@Inject
	@RequestScoped
	@PrismaRestWSParams
	RestWSParamsContainer restWSParams;

	/**
	 * Produces {@link it.prisma.dal.dao.queryparams.DAOQueryParams} using
	 * {@link RestWSParamsContainer}.
	 */
	@RequestScoped
	@Produces
	@DAOQueryParams
	@Specializes
	@Override
	public it.prisma.dal.dao.queryparams.DAOQueryParams createDAOQueryParams() {

		if(restWSParams.isDisabled())
			return it.prisma.dal.dao.queryparams.DAOQueryParams.disabled();
		
		DAOPaginationParams paginationParams = new DAOPaginationParams(
				restWSParams.getPaginationParams().getLimit(), restWSParams
						.getPaginationParams().getOffset());

		DAOOrderingParams orderingParams = new DAOOrderingParams();
		if (restWSParams.getOrderingParams() != null)
			for (OrderSpecification item : restWSParams.getOrderingParams()
					.getOrderSpecs())
				orderingParams.getOrderSpecs().add(
						new it.prisma.dal.dao.queryparams.OrderSpecification(
								item.getField(), OrderType.valueOf(item
										.getOrder().toString())));

		DAOFilteringParams filteringParams = new DAOFilteringParams();
		if (restWSParams.getFilteringParams() != null)
			for (FilteringSpecification item : restWSParams
					.getFilteringParams().getFilteringSpecs())
				filteringParams
						.getFilteringSpecs()
						.add(new it.prisma.dal.dao.queryparams.FilteringSpecification(
								item.getField(), OperatorType.valueOf(item
										.getOperator().toString()), item
										.getValue()));

		return new it.prisma.dal.dao.queryparams.DAOQueryParams(orderingParams,
				paginationParams, filteringParams);
	}
}
