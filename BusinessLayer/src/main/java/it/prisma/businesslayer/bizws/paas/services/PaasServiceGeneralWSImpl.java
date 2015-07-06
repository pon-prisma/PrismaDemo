package it.prisma.businesslayer.bizws.paas.services;

import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;

import javax.inject.Inject;

public class PaasServiceGeneralWSImpl extends BaseWS implements
		PaasServiceGeneralWS {

	@Inject
	protected PaaSServiceDAO paasSvcDAO;

	@Override
	public Boolean isPaaSServiceNameAvailable(Long workgroupId,
			String serviceName) throws Exception{
		
		if (paasSvcDAO.findByTypeAndNameAndWorkgroup(null, serviceName, workgroupId) == null)
				return true;
		return false;
	}

}
