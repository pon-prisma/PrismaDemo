package it.prisma.dal.dao.paas.services.caaas;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.services.caaas.EjbcaCost;
import it.prisma.dal.entities.paas.services.caaas.QEjbcaCost;
import javax.ejb.Local;
import javax.ejb.Stateless;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(EjbcaCostDAO.class)
//@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class EjbcaCostDAO extends QueryDSLGenericDAO<EjbcaCost, Long> {

	private QEjbcaCost qEjbcaCost;

	public EjbcaCostDAO() {
		super(EjbcaCost.class);
		qEjbcaCost = QEjbcaCost.ejbcaCost;
	}
}
