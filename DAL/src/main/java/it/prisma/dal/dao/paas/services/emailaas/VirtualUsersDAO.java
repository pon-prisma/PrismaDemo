package it.prisma.dal.dao.paas.services.emailaas;

import static it.prisma.dal.entities.paas.services.emailaas.QVirtualUsers.virtualUsers;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.services.emailaas.VirtualUsers;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.NotFoundException;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(VirtualUsersDAO.class)
@Stateless
public class VirtualUsersDAO extends QueryDSLGenericDAO<VirtualUsers, Long> {

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

	public VirtualUsersDAO() {
		super(VirtualUsers.class);
	}

		
	/**
	 * 
	 * @param user_prisma_id
	 * @param from
	 * @param to
	 * @param size
	 *            default limit = 1000
	 * @return
	 */
	public Collection<VirtualUsers> findByVirtualUsersPrismaIdWithFilters(
			Long user_prisma_id) {

		JPAQuery query = super.prepareJPAQuery(virtualUsers)// .from(paaSServiceEvent)
				.where(virtualUsers.user_prisma_id.eq(user_prisma_id));

		query.orderBy( virtualUsers.email.asc());

		return getListResults(query, virtualUsers);
	}


	/**
	 * Delete an Account
	 * 
	 * @param email
	 * @param password
	 * @throws NotFoundException - If the entity identified by email and password doesn't exist in the DB
	 * @throws NullPointerException - If the entity identified by id doesn't exist in the DB.
	 * @throws IllegalArgumentException - If the id is null.
	 * @throws IllegalStateException - If there were an error opening the transaction or the instance is not an Entity
	 */
	public boolean deleteEmailAccount(String email, String MD5Password) throws NullPointerException, IllegalArgumentException, IllegalStateException {
	
		VirtualUsers user = findByEmailAndPassword(email, MD5Password);
		if(user == null)
			return false;		
		delete(user.getId());
		return true;
	
	}
	
	public VirtualUsers findByEmailAndPassword(String email, String MD5Password){
				
		if (email == null)
			throw new IllegalArgumentException("email");
		if (MD5Password == null)
			throw new IllegalArgumentException("password");

		JPAQuery query = super.prepareJPAQuery(virtualUsers)// .from(paaSServiceEvent)
				.where(virtualUsers.email.eq(email), virtualUsers.password.eq(MD5Password));
		return (VirtualUsers) query.singleResult(virtualUsers);	
	}

	public boolean isEmailAccountExists(String email) {
		if (email == null)
			throw new IllegalArgumentException("email");
		
		JPAQuery query = super.prepareJPAQuery(virtualUsers)
				.where(virtualUsers.email.eq(email));
		return query.singleResult(virtualUsers)==null?false:true;
	}
	

}
