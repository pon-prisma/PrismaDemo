package it.prisma.dal.dao.orchestrator.lrt;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

@Local(LRTEventDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class LRTEventDAO extends QueryDSLGenericDAO<LRTEvent, Long>{

	public LRTEventDAO() {
		super(LRTEvent.class);		
	}

	public List<LRTEvent> getByLRT(long lrtId) {
		return getByLRT(lrtId, null); 
	}
	
	public List<LRTEvent> getByLRT(long lrtId, Long createdAt) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<LRTEvent> query = cb.createQuery(LRTEvent.class);
		Root<LRTEvent> sm = query.from(LRTEvent.class);
		
		//Metamodel m = entityManager.getMetamodel();
		//EntityType<LRTEvent> LRTEvent_ = m.entity(LRTEvent.class);

		query.where(cb.equal(sm.get("LRT"), lrtId));
		if(createdAt!=null){
			Expression<Long> tm=sm.get("createdAt");
			query.where(cb.ge(tm, createdAt));
		}
		query.select(sm);

		TypedQuery<LRTEvent> q = entityManager.createQuery(query);
		List<LRTEvent> results = q.getResultList();
		return results;

	}

}
