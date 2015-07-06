package it.prisma.dal.dao.paas.services.emailaas;


import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.services.emailaas.VirtualDomains;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Local(VirtualDomainsDAO.class)
@Stateless
public class VirtualDomainsDAO extends QueryDSLGenericDAO<VirtualDomains, Long> {

	@PersistenceContext(unitName = "EmailaaS-PU")
	protected EntityManager customEntityManager;

	//Assign the customEntityManager to entityManager (for use mailserverdb instead of prisma_paas)
	@PostConstruct
	private void OverrideEntityManager(){
		entityManager=this.customEntityManager;
	}
	
	@Override
	protected EntityManager getEntityManager() {
		assert (customEntityManager != null);
		return this.customEntityManager;
	}

	public VirtualDomainsDAO() {
		super(VirtualDomains.class);
	}

		
	

}
