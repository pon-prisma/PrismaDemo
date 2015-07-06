package it.prisma.dal.dao.queryparams;

import java.io.Serializable;

public class DAOPaginationParams implements Serializable {

	private static final long serialVersionUID = 8404184773069031907L;

	private long limit;
	private long offset;
	private Long resultTotalCount;

	public DAOPaginationParams(long limit, long offset) {
		if (limit < 1)
			throw new IllegalArgumentException(
					"Pagination limit must be greater than 0.");
		if (offset < 0)
			throw new IllegalArgumentException(
					"Pagination offset must be greater than or equal to 0.");

		this.limit = limit;
		this.offset = offset;
	}

	public long getLimit() {
		return limit;
	}

	public long getOffset() {
		return offset;
	}

	public Long getResultTotalCount() {
		return resultTotalCount;
	}

	public void setResultTotalCount(long resultTotalCount) {
		this.resultTotalCount = resultTotalCount;
	}
}
