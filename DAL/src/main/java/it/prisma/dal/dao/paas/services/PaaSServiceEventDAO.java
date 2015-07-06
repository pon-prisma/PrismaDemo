package it.prisma.dal.dao.paas.services;

import static it.prisma.dal.entities.paas.services.QPaaSServiceEvent.paaSServiceEvent;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;

@Local(PaaSServiceEventDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class PaaSServiceEventDAO extends
		QueryDSLGenericDAO<PaaSServiceEvent, Long> {

	public PaaSServiceEventDAO() {
		super(PaaSServiceEvent.class);
	}
	
	/**
	 * 
	 * @param paaSServiceId
	 * @param from
	 * @param to
	 * @param size
	 *            default limit = 1000
	 * @return
	 */
	public Collection<PaaSServiceEvent> findByPaaSServiceIdWithFilters(
			Long paaSServiceId, Long from, Long to, Long size) {
		
		JPAQuery query = super.prepareJPAQuery(paaSServiceEvent)//.from(paaSServiceEvent)
				.where(paaSServiceEvent.paaSService.id.eq(paaSServiceId));

		query.orderBy( paaSServiceEvent.id.desc());
		
		return getListResults(query, paaSServiceEvent);
	}
	
	public Path<?> getFilteringEntityPathFromFieldName(String fieldName) {
		switch(fieldName) {
			case "createdAt":
				return paaSServiceEvent.createdAt;
			
			case "id":
				return paaSServiceEvent.id;
				
			case "type":
				return paaSServiceEvent.type;
		}
		
		return null;
	}
	
	public Path<?> getOrderingEntityPathFromFieldName(String fieldName) {
		switch(fieldName) {
			case "createdAt":
				return paaSServiceEvent.createdAt;
			
			case "id":
				return paaSServiceEvent.id;
		}
		
		return null;
	}
}
