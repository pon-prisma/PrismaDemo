package it.prisma.dal.dao.iaas.tenant;

import static it.prisma.dal.entities.iaas.tenant.QIaaSEnvironment.iaaSEnvironment;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.iaas.tenant.IaaSEnvironment;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;

@Local(IaasEnvironmentDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class IaasEnvironmentDAO extends
		QueryDSLGenericDAO<IaaSEnvironment, Long> {

	public IaasEnvironmentDAO() {
		super(IaaSEnvironment.class);
	}

	/**
	 * Find by Name.
	 * 
	 * @param name
	 * @return
	 */
	public IaaSEnvironment findByName(String name) {
		if (name == null)
			throw new IllegalArgumentException("name");

		JPAQuery query = super.prepareJPAQuery(iaaSEnvironment).where(
				iaaSEnvironment.name.eq(name));

		return query.singleResult(iaaSEnvironment);
	}

	public Path<?> getFilteringEntityPathFromFieldName(String fieldName) {
		switch (fieldName) {
		case "id":
			return iaaSEnvironment.id;
		case "name":
			return iaaSEnvironment.name;
		case "domain":
			return iaaSEnvironment.domain;
		}

		return null;
	}

	public Path<?> getOrderingEntityPathFromFieldName(String fieldName) {
		switch (fieldName) {
		case "id":
			return iaaSEnvironment.id;
		case "name":
			return iaaSEnvironment.name;
		case "domain":
			return iaaSEnvironment.domain;
		}

		return null;
	}
}
