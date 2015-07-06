package it.prisma.dal.dao.iaas.services;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.iaas.services.IaaSSecurityGroup;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local(IaaSSecurityGroupDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class IaaSSecurityGroupDAO extends QueryDSLGenericDAO<IaaSSecurityGroup, Long> {

	
	public IaaSSecurityGroupDAO() {
		super(IaaSSecurityGroup.class);
	}
	
}
