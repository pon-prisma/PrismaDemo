package it.prisma.dal.dao.queryparams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DAOOrderingParams implements Serializable {

	private static final long serialVersionUID = -1806449861830325994L;
	
	private List<OrderSpecification> orderSpecs = new ArrayList<OrderSpecification>();

	public List<OrderSpecification> getOrderSpecs() {
		return orderSpecs;
	}

}
