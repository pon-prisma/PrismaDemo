package it.prisma.dal.dao.config;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.config.PlatformConfiguration;

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

@Local(PlatformConfigurationDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class PlatformConfigurationDAO extends
QueryDSLGenericDAO<PlatformConfiguration, String> {

	public PlatformConfigurationDAO() {
		super(PlatformConfiguration.class);
	}

	// TODO User QueryDSL
	public List<PlatformConfiguration> findByListOfKeys(List<String> keys) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PlatformConfiguration> query = cb
				.createQuery(PlatformConfiguration.class);
		Root<PlatformConfiguration> sm = query
				.from(PlatformConfiguration.class);
		Expression<String> tm = sm.get("key");
		query.where(tm.in(keys));
		query.select(sm);
		TypedQuery<PlatformConfiguration> q = entityManager.createQuery(query);
		List<PlatformConfiguration> results = q.getResultList();
		return results;
	}
	
	public List<PlatformConfiguration> findAll() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PlatformConfiguration> query = cb
				.createQuery(PlatformConfiguration.class);
		Root<PlatformConfiguration> sm = query
				.from(PlatformConfiguration.class);
		query.select(sm);
		TypedQuery<PlatformConfiguration> q = entityManager.createQuery(query);
		List<PlatformConfiguration> results = q.getResultList();
		return results;
	}

}
