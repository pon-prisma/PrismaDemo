package it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests;

public class FilteringSpecification {

	public enum OperatorType {
		//@formatter:off
		EQUALS("="),
		NOT_EQUAL("!="),
		GREATER(">="),
		GREATER_EQUAL(">="),
		LESS("<="),
		LESS_EQUAL("<="),
		CONTAINS("%");
		//@formatter:on

		private String operator;

		private OperatorType(String operator) {
			this.operator = operator;
		}
		
		public String getOperator() {
			return operator;
		}
	}

	private String field;
	private OperatorType operator;
	private String value;
	public FilteringSpecification(String field, OperatorType operator,
			String value) {
		super();
		this.field = field;
		this.operator = operator;
		this.value = value;
	}
	public String getField() {
		return field;
	}
	public OperatorType getOperator() {
		return operator;
	}
	public String getValue() {
		return value;
	}

}
