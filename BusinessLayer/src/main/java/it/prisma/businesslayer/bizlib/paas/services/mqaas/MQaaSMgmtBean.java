package it.prisma.businesslayer.bizlib.paas.services.mqaas;

import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceEventDAO;
import it.prisma.dal.dao.paas.services.mqaas.MQaaSDAO;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.mqaas.MQaaS;

import java.util.Collection;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MQaaSMgmtBean {

	protected static Logger LOG = LogManager.getLogger(MQaaSMgmtBean.class);

	private static final String TAG = "[MQaaSBean] ";

	@Inject
	private PaaSServiceDAO paaSServiceDAO;

	@Inject
	private MQaaSDAO mqaaSDAO;

	@Inject
	protected PaaSServiceEventDAO paaSServiceEventDAO;

	public MQaaS getById(Long Id) {
		return mqaaSDAO.findById(Id);
	}

	public Collection<MQaaS> getByUserAndWorkgroup(Long workgroupId,
			Long userAccountId, boolean considerDeletedToo) {
		return mqaaSDAO.findByUserAndWorkgroup(userAccountId, workgroupId, considerDeletedToo);
	}

	public Collection<PaaSServiceEvent> getEvents(Long id, Long from, Long to,
			Long size) {
		return paaSServiceEventDAO.findByPaaSServiceIdWithFilters(id, from, to,
				size);
	}
}