package it.prisma.dal.dao.orchestrator.lrt;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.orchestrator.lrt.LRT;
import it.prisma.dal.entities.orchestrator.lrt.LRT.LRTStatus;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Local(LRTDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class LRTDAO extends QueryDSLGenericDAO<LRT, Long>{
	

	public LRTDAO() {
		super(LRT.class);
	}

	public LRT getByInstanceId(long id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<LRT> query = cb.createQuery(LRT.class);
		Root<LRT> sm = query.from(LRT.class);

		query.where(cb.equal(sm.get("instanceId"), id));
		query.select(sm);

		TypedQuery<LRT> q = entityManager.createQuery(query);
		List<LRT> results = q.getResultList();
		if (results != null && results.size() > 0)
			return results.get(0);
		else
			return null;

	}

	public List<LRT> getPending() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<LRT> query = cb.createQuery(LRT.class);
		Root<LRT> sm = query.from(LRT.class);

		query.where(cb.equal(sm.get("status"), LRTStatus.RUNNING));
		query.select(sm);

		TypedQuery<LRT> q = entityManager.createQuery(query);
		return q.getResultList();
	}
	
}
