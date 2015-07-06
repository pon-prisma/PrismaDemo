package it.prisma.dal.dao.paas.deployments;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local(PaaSDeploymentServiceDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class PaaSDeploymentServiceDAO extends
QueryDSLGenericDAO<PaaSDeploymentService, Long> {

	public PaaSDeploymentServiceDAO() {
		super(PaaSDeploymentService.class);
	}

}
