package it.prisma.dal.dao.iaas.tenant;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.iaas.tenant.CloudifyInstance;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local(CloudifyEndpointDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class CloudifyEndpointDAO extends
		QueryDSLGenericDAO<CloudifyInstance, Long> {

	public CloudifyEndpointDAO() {
		super(CloudifyInstance.class);
	}

}
