package it.prisma.dal.dao.monitoring;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.monitoring.AtomicService;
import it.prisma.dal.entities.monitoring.QAtomicService;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(AtomicServiceDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class AtomicServiceDAO extends QueryDSLGenericDAO<AtomicService, Long> {

    private QAtomicService qAtomicService;

    public AtomicServiceDAO() {
	super(AtomicService.class);
	qAtomicService = QAtomicService.atomicService;
    }

    /**
     * 
     * @param name the name the AtomicService
     * @return an AtomicService instance or null if not found
     * @throws IllegalArgumentException if name is null
     */
    public AtomicService findByName(String name) throws IllegalArgumentException {
	if (name == null)
	    throw new IllegalArgumentException("name cannot be null");

	JPAQuery query = super.prepareJPAQuery().from(qAtomicService).where(qAtomicService.name.equalsIgnoreCase(name));

	return query.singleResult(qAtomicService);
    }
    
    
    /**
     * 
     * @param name the name the AtomicService
     * @return an AtomicService instance or null if not found
     * @throws IllegalArgumentException if name is null
     */
    public AtomicService findByPaaSDeploymentServiceType(String type) throws IllegalArgumentException {
	if (type == null)
	    throw new IllegalArgumentException("Type cannot be null");

	JPAQuery query = super.prepareJPAQuery().from(qAtomicService).where(qAtomicService.type.equalsIgnoreCase(type));

	return query.singleResult(qAtomicService);
    }

   

}
