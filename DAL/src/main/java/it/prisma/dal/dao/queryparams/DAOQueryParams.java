package it.prisma.dal.dao.queryparams;


/**
 * This class is only a wrapper for RestWS common parameters:
 * <ul>
 * <li>
 * Pagination ({@link DAOPaginationParams})</li>
 * <li>
 * Ordering ({@link DAOOrderingParams})</li>
 * <li>
 * Filtering</li>
 * </ul>
 * <br/>
 * 
 * @author l.biava
 *
 */
public class DAOQueryParams {
	private DAOOrderingParams orderingParams;
	private DAOPaginationParams paginationParams;
	private DAOFilteringParams filteringParams;
	private boolean disabled=false;
	
	public DAOQueryParams() {

	}
	
	public DAOQueryParams(boolean disabled) {
		this.disabled=true;
	}

	public DAOQueryParams(DAOOrderingParams orderingParams,
			DAOPaginationParams paginationParams,
			DAOFilteringParams filteringParams) {
		super();
		this.orderingParams = orderingParams;
		this.paginationParams = paginationParams;
		this.filteringParams = filteringParams;
	}
	
	public static DAOQueryParams disabled() {
		return new DAOQueryParams(true);		
	}

	public boolean isDisabled() {
		return disabled;
	}
	
	public DAOOrderingParams getOrderingParams() {
		return orderingParams;
	}

	public DAOPaginationParams getPaginationParams() {
		return paginationParams;
	}

	public DAOFilteringParams getFilteringParams() {
		return filteringParams;
	}

}