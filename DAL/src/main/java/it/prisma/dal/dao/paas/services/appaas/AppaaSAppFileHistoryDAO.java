package it.prisma.dal.dao.paas.services.appaas;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.services.appaas.AppaaSAppFileHistory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local(AppaaSAppFileHistoryDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class AppaaSAppFileHistoryDAO extends
		QueryDSLGenericDAO<AppaaSAppFileHistory, Long> {

	public AppaaSAppFileHistoryDAO() {
		super(AppaaSAppFileHistory.class);
	}

}
