package it.prisma.dal.dao.queryparams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DAOFilteringParams implements Serializable {

	private static final long serialVersionUID = 7478855260522654487L;

	private List<FilteringSpecification> filteringSpecs = new ArrayList<FilteringSpecification>();

	public List<FilteringSpecification> getFilteringSpecs() {
		return filteringSpecs;
	}

}
