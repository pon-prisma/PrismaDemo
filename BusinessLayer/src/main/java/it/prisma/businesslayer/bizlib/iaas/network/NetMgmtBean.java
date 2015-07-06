package it.prisma.businesslayer.bizlib.iaas.network;

import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizws.iaas.OpenstackHelper;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.iaas.services.IaaSNetworkDAO;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.iaas.services.IaaSNetwork;
import it.prisma.domain.dsl.iaas.network.NetworkRepresentation;
import it.prisma.domain.dsl.iaas.network.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetMgmtBean {

	protected static Logger LOG = LogManager.getLogger(NetMgmtBean.class);

	@Inject
	private OpenstackHelper openstackHelper;

	@Inject
	private IaaSNetworkDAO iaaSNetworkDAO;

	@Inject
	WorkgroupDAO workgroupDAO;

	public List<NetworkRepresentation> getNetworksByWorkgroupId(Long workgroupId) {

		Workgroup workgroup = workgroupDAO.findById(workgroupId);
		if (workgroup == null)
			throw new ResourceNotFoundException(Workgroup.class, "workgroupId="+workgroupId);

		Collection<IaaSNetwork> list = iaaSNetworkDAO.findByWorkgroupId(workgroupId);
		List<NetworkRepresentation> nets = new ArrayList<NetworkRepresentation>();
		for (IaaSNetwork k : list) {
			nets.add(new NetworkRepresentation(k.getId(), k.getName(), k.isShared(), State.valueOf(k.getStatus()), k.getAdminStateUp(), null));
		}
		return nets;
	}

}