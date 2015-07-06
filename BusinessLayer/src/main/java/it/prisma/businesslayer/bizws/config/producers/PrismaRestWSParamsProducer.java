package it.prisma.businesslayer.bizws.config.producers;

import it.prisma.businesslayer.bizws.config.annotations.PrismaRestWSParams;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.FilteringSpecification;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.FilteringSpecification.OperatorType;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.OrderSpecification;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.OrderSpecification.OrderType;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSFilteringParams;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSOrderingParams;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSPaginationParams;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;

/**
 * This class contains a single producer for {@link RestWSParamsContainer}.
 * 
 * @see PrismaRestWSParamsProducer#createParamsContainer(HttpServletRequest)
 * @author l.biava
 *
 */
public class PrismaRestWSParamsProducer {

	public static final String ATTRIBUTE_ENABLER="RestWSParamsEnabled";
	
	/**
	 * This method produces a {@link RestWSParamsContainer}, using data from the
	 * current {@link HttpServletRequest}.
	 * 
	 * @param request
	 *            the current WS request.
	 * @return an instance of {@link RestWSParamsContainer}, populated with the
	 *         WS parameters (pagination, sorting, filtering) found in the
	 *         request.
	 */
	@RequestScoped
	@Produces
	@PrismaRestWSParams
	public RestWSParamsContainer createParamsContainer(
			HttpServletRequest request) {
		// TODO: Add exception handling ?
		
		// Enabled ?
		try {
			if((boolean) request.getAttribute(ATTRIBUTE_ENABLER)!=true)
				throw new Exception();	
		} catch (Exception e) {
			return RestWSParamsContainer.disabled();
			//return RestWSParamsContainer.empty();
		}
		

		RestWSPaginationParams paginationParams = getPaginationParams(request);
		RestWSOrderingParams orderingParams = getOrderingParams(request);
		RestWSFilteringParams filteringParams = getFilteringParams(request);

		return new RestWSParamsContainer(orderingParams, paginationParams,
				filteringParams);
	}

	protected static RestWSPaginationParams getPaginationParams(
			HttpServletRequest request) {
		String hLimit = request
				.getParameter(RestWSPaginationParams.QUERY_PARAM_LIMIT);
		Long limit = (hLimit == null ? 200 : new Long(hLimit));

		String hOffset = request
				.getParameter(RestWSPaginationParams.QUERY_PARAM_OFFSET);
		Long offset = (hOffset == null ? 0 : new Long(hOffset));

		return new RestWSPaginationParams(limit, offset);
	}

	protected static RestWSOrderingParams getOrderingParams(
			HttpServletRequest request) {

		String qOrderBy = request
				.getParameter(RestWSOrderingParams.QUERY_PARAM_ORDERBY);

		if (qOrderBy == null)
			return null;

		String[] orderByCSV = qOrderBy.split(",");

		RestWSOrderingParams ordParams = new RestWSOrderingParams();

		for (String item : orderByCSV) {
			// Format: (nothing|-)field
			if (item.charAt(0) == '-')
				ordParams.getOrderSpecs().add(
						new OrderSpecification(item.substring(1),
								OrderType.DESC));
			else
				ordParams.getOrderSpecs().add(
						new OrderSpecification(item, OrderType.ASC));
		}

		return ordParams;
	}

	protected static RestWSFilteringParams getFilteringParams(
			HttpServletRequest request) {
		String qFilterBy = request
				.getParameter(RestWSFilteringParams.QUERY_PARAM_FILTERBY);

		if (qFilterBy == null)
			return null;

		String[] filterByCSV = qFilterBy.split(",");

		RestWSFilteringParams filtParams = new RestWSFilteringParams();

		for (String item : filterByCSV) {
			// Format: <field><operator><value>
			for (OperatorType type : OperatorType.values()) {
				if (item.contains(type.getOperator())) {
					String[] split = item.split(type.getOperator());
					filtParams.getFilteringSpecs()
							.add(new FilteringSpecification(split[0], type,
									split[1]));
					
					break;
				}
			}
		}

		return filtParams;
	}
}
