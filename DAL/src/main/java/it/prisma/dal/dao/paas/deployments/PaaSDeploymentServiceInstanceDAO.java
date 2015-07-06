package it.prisma.dal.dao.paas.deployments;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local(PaaSDeploymentServiceInstanceDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class PaaSDeploymentServiceInstanceDAO extends
		QueryDSLGenericDAO<PaaSDeploymentServiceInstance, Long> {

	public PaaSDeploymentServiceInstanceDAO() {
		super(PaaSDeploymentServiceInstance.class);
	}

}
