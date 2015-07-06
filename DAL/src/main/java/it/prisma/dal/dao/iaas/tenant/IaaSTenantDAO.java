package it.prisma.dal.dao.iaas.tenant;

import static it.prisma.dal.entities.iaas.tenant.QIaaSTenant.iaaSTenant;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.iaas.tenant.IaaSTenant;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;

@Local(IaaSTenantDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class IaaSTenantDAO extends QueryDSLGenericDAO<IaaSTenant, Long> {

	public IaaSTenantDAO() {
		super(IaaSTenant.class);
	}

	/**
	 * 
	 * @param workgroupId
	 * @return
	 */
	public IaaSTenant findByWorkgroupId(Long workgroupId) {
		if (workgroupId == null)
			throw new IllegalArgumentException("workgroupId");

		JPAQuery query = super.prepareJPAQuery(iaaSTenant).where(
				iaaSTenant.workgroup.id.eq(workgroupId));

		return query.singleResult(iaaSTenant);
	}

	/**
	 * Find by Name.
	 * 
	 * @param name
	 * @return
	 */
	public IaaSTenant findByName(String name) {
		if (name == null)
			throw new IllegalArgumentException("name");

		JPAQuery query = super.prepareJPAQuery(iaaSTenant).where(
				iaaSTenant.name.eq(name));
		
		return query.singleResult(iaaSTenant);
	}

	public Path<?> getFilteringEntityPathFromFieldName(String fieldName) {
		//TODO: Ma serve davvero ?
		switch (fieldName) {
		case "id":
			return iaaSTenant.id;
		case "name":
			return iaaSTenant.name;
		case "username":
			return iaaSTenant.username;
		}

		return null;
	}

	public Path<?> getOrderingEntityPathFromFieldName(String fieldName) {
		//TODO: Ma serve davvero ?
		switch (fieldName) {
		case "iaasTenantId":
			return iaaSTenant.id;
		case "name":
			return iaaSTenant.name;
		case "username":
			return iaaSTenant.username;
		}

		return null;
	}

}
