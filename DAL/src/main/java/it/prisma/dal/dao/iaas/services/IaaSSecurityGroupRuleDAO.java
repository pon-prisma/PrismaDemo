package it.prisma.dal.dao.iaas.services;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.iaas.services.IaaSSecurityGroupRule;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local(IaaSSecurityGroupRuleDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class IaaSSecurityGroupRuleDAO extends QueryDSLGenericDAO<IaaSSecurityGroupRule, Long> {

	
	public IaaSSecurityGroupRuleDAO() {
		super(IaaSSecurityGroupRule.class);
	}
	
}
