package it.prisma.dal.dao.monitoring;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.monitoring.QThreashold;
import it.prisma.dal.entities.monitoring.Threashold;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local(ThreasholdDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class ThreasholdDAO extends QueryDSLGenericDAO<Threashold, Long> {

    private QThreashold qThreashold;

    public ThreasholdDAO() {
	super(Threashold.class);
	qThreashold = QThreashold.threashold;
    }

}
