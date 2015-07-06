//package it.prisma.dal.dao.paas.services.appaas;
//
//import it.prisma.dal.dao.QueryDSLGenericDAO;
//import it.prisma.dal.entities.paas.services.appaas.AppaaSDetails;
//
//import java.util.List;
//
//import javax.ejb.Local;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//
//@Local(AppaaSDetailsDAO.class)
//@TransactionAttribute(TransactionAttributeType.REQUIRED)
//public class AppaaSDetailsDAO extends QueryDSLGenericDAO<AppaaSDetails, Long> {
//
//	public AppaaSDetailsDAO() {
//		super(AppaaSDetails.class);
//	}
//
//	public AppaaSDetails findByNameAndTenant(String appName, long tenantId) {
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<AppaaSDetails> query = cb.createQuery(AppaaSDetails.class);
//		Root<AppaaSDetails> sm = query.from(AppaaSDetails.class);
//
//		query.where(cb.equal(sm.get("workgroup"), tenantId));
//		query.where(cb.equal(sm.get("name"), appName));
//		query.select(sm);
//
//		TypedQuery<AppaaSDetails> q = entityManager.createQuery(query);
//		List<AppaaSDetails> results = q.getResultList();
//		if (results != null && results.size() > 0)
//			return results.get(0);
//		else
//			throw new EntityNotFoundException("No AppaaSDetails entry found for name '"+appName+"' and tenant '"+tenantId+"'");
//	}
//}
