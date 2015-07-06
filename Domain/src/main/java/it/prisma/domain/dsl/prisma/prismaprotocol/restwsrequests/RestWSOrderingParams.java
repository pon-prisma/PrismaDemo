package it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests;

import java.util.ArrayList;
import java.util.List;

public class RestWSOrderingParams {

	public static final String QUERY_PARAM_ORDERBY = "order";

	private List<OrderSpecification> orderSpecs = new ArrayList<OrderSpecification>();

	public List<OrderSpecification> getOrderSpecs() {
		return orderSpecs;
	}

}
