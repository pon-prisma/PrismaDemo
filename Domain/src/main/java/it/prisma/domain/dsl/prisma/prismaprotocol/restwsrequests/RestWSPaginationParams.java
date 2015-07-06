package it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests;

import java.io.Serializable;

public class RestWSPaginationParams implements Serializable {

	private static final long serialVersionUID = 8404184773069031907L;
	
	public static final String QUERY_PARAM_LIMIT = "limit";
	public static final String QUERY_PARAM_OFFSET = "offset";
	public static final String HEADER_LINKS = "Link";
	public static final String HEADER_TOTAL_ITEMS = "X-Total-Count";

	private Long limit;
	private Long offset;

	public RestWSPaginationParams(Long limit, Long offset) {
		super();
		this.limit = limit;
		this.offset = offset;
	}

	public Long getLimit() {
		return limit;
	}

	public Long getOffset() {
		return offset;
	}

}
