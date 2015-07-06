package it.prisma.dal.dao.monitoring;

import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.monitoring.AtomicService;
import it.prisma.dal.entities.monitoring.Metric;
import it.prisma.dal.entities.monitoring.QAtomicService;
import it.prisma.dal.entities.monitoring.QMetric;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(MetricDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class MetricDAO extends QueryDSLGenericDAO<Metric, Long> {

    private QMetric qMetric;

    @Inject
    private AtomicServiceDAO atomicServiceDAO;

    public MetricDAO() {
	super(Metric.class);
	qMetric = QMetric.metric;
    }

    /**
     * 
     * @param name
     * @param PURPOSE
     *            see possible value into {@link Metric} Entity
     * @return
     */
    public Collection<Metric> findByAtomicServiceAndPurposeLevel(Long atomicServiceID, int PURPOSE) {

	QAtomicService qAtomicService = QAtomicService.atomicService;
	QMetric qMetric = QMetric.metric;

	JPAQuery query = super.prepareJPAQuery().from(qAtomicService)
		.innerJoin(qAtomicService.metrics, qMetric)
		.where(qAtomicService.id.eq(atomicServiceID))
		.where(qMetric.purpose.eq(PURPOSE));
	   
	return query.list(qMetric);

    }

//    private boolean hasPurpose(Metric metric, int purpose) {
//	return (metric.getPurpose() & purpose) >= purpose ? true : false;
//    }

}
