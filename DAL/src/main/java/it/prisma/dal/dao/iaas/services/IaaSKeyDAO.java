package it.prisma.dal.dao.iaas.services;

import static it.prisma.dal.entities.iaas.services.QIaaSKey.iaaSKey;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.iaas.services.IaaSKey;
import it.prisma.dal.entities.iaas.services.QIaaSKey;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.NotFoundException;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;

@Stateless
@Local(IaaSKeyDAO.class)
public class IaaSKeyDAO extends QueryDSLGenericDAO<IaaSKey, Long> {

	public IaaSKeyDAO() {
		super(IaaSKey.class);
	}

	/**
	 * At least one of the parameters MUST be specified.
	 * 
	 * @param workgroupId
	 * @return
	 */
	public Collection<IaaSKey> findByWorkgroupId(Long workgroupId) {
		if (workgroupId == null)
			throw new IllegalArgumentException("workgroupId");

		JPAQuery query = super.prepareJPAQuery(iaaSKey)
				.where(iaaSKey.workgroup.id.eq(workgroupId));

		query.orderBy( iaaSKey.id.asc());
		
		return getListResults(query, iaaSKey);
	}
	
	/**
	 * Find by Name.
	 * 
	 * @param workgroupId
	 * @return
	 */
	public IaaSKey findByName(Long workgroupId, String name) throws NotFoundException{
		if (workgroupId == null)
			throw new IllegalArgumentException("workgroupId");

		JPAQuery query = super.prepareJPAQuery(iaaSKey)
				.where(iaaSKey.name.eq(name));
		return (IaaSKey) query.singleResult(iaaSKey);
	}

	public Path<?> getFilteringEntityPathFromFieldName(String fieldName) {
		switch(fieldName) {			
			case "name":
				return iaaSKey.name;
			
			case "id":
				return iaaSKey.id;
		}
		
		return null;
	}
	
	public Path<?> getOrderingEntityPathFromFieldName(String fieldName) {
		switch(fieldName) {
			case "name":
				return iaaSKey.name;
			
			case "id":
				return iaaSKey.id;
		}
		
		return null;
	}
}
