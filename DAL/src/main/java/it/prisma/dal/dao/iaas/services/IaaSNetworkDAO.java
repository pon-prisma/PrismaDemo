package it.prisma.dal.dao.iaas.services;

import static it.prisma.dal.entities.iaas.services.QIaaSNetwork.iaaSNetwork;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.iaas.services.IaaSNetwork;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.NotFoundException;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;

@Local(IaaSNetworkDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class IaaSNetworkDAO extends QueryDSLGenericDAO<IaaSNetwork, Long> {

	
	public IaaSNetworkDAO() {
		super(IaaSNetwork.class);
	}
	
	
	/**
	 * At least one of the parameters MUST be specified.
	 * 
	 * @param workgroupId
	 * @return
	 */
	public Collection<IaaSNetwork> findByWorkgroupId(Long workgroupId) {
		if (workgroupId == null)
			throw new IllegalArgumentException("workgroupId");

		JPAQuery query = super.prepareJPAQuery(iaaSNetwork)
				.where(iaaSNetwork.workgroup.id.eq(workgroupId));

		query.orderBy( iaaSNetwork.id.asc());
		
		return getListResults(query, iaaSNetwork);
	}
	
	/**
	 * Find by Name.
	 * 
	 * @param workgroupId
	 * @return
	 */
	public IaaSNetwork findByName(Long workgroupId, String name) throws NotFoundException{
		if (workgroupId == null)
			throw new IllegalArgumentException("workgroupId");

		JPAQuery query = super.prepareJPAQuery(iaaSNetwork)
				.where(iaaSNetwork.name.eq(name));
		return (IaaSNetwork) query.singleResult(iaaSNetwork);
	}

	public Path<?> getFilteringEntityPathFromFieldName(String fieldName) {
		switch(fieldName) {			
			case "name":
				return iaaSNetwork.name;
			
			case "id":
				return iaaSNetwork.id;
		}
		
		return null;
	}
	
	public Path<?> getOrderingEntityPathFromFieldName(String fieldName) {
		switch(fieldName) {
			case "name":
				return iaaSNetwork.name;
			
			case "id":
				return iaaSNetwork.id;
		}
		
		return null;
	}
}
