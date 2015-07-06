package it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests;

import java.util.ArrayList;
import java.util.List;

public class RestWSFilteringParams {

	public static final String QUERY_PARAM_FILTERBY = "search";

	private List<FilteringSpecification> filteringSpecs = new ArrayList<FilteringSpecification>();

	public List<FilteringSpecification> getFilteringSpecs() {
		return filteringSpecs;
	}

}
