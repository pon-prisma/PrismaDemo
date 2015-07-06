package it.prisma.dal.dao.queryparams;


public class OrderSpecification {

	public enum OrderType {
		ASC, DESC
	}

	private String field;
	private OrderType order;

	public OrderSpecification(String field, OrderType order) {
		super();
		this.field = field;
		this.order = order;
	}

	public String getField() {
		return field;
	}

	public OrderType getOrder() {
		return order;
	}

}
