package it.prisma.dal.dao.paas.deployments;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local(PaaSDeploymentDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class PaaSDeploymentDAO extends QueryDSLGenericDAO<PaaSDeployment, Long> {

	public PaaSDeploymentDAO() {
		super(PaaSDeployment.class);
	}
}
