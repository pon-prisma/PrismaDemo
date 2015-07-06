package it.prisma.businesslayer.bizws.paas.services;

import it.prisma.businesslayer.bizlib.misc.DAOProvider;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.paas.deployments.PaaSDeploymentWS;
import it.prisma.domain.dsl.deployments.VirtualMachineRepresentation;
import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

import java.util.Collection;

import javax.inject.Inject;

@PrismaWrapper
public abstract class PaaSServiceGenericWSImpl<SERVICE_REPRESENTATION_TYPE extends GenericPaaSServiceRepresentation/*, SERVICE_ENTITY_TYPE extends AbstractPaaSService*/, DEPLOY_REQ_TYPE extends GenericPaaSServiceDeployRequest<SERVICE_REPRESENTATION_TYPE>>
		extends BaseWS implements
		PaaSServiceGenericWS<SERVICE_REPRESENTATION_TYPE, DEPLOY_REQ_TYPE> {

//	protected abstract Class<SERVICE_ENTITY_TYPE> getPaaSServiceEntityClass();
			
	@Inject
	protected PaaSDeploymentWS paasDeploymentWS;
	
	@Inject
	protected DAOProvider daoProvider;

	// VMs
	/**
	 * @return the list of Virtual Machines and related services (instances &
	 *         types) for a the PaaSService.
	 */
	@Override
	public Collection<VirtualMachineRepresentation> getPaaSServiceVirtualMachines(
			Long workgroupId, Long resId) {
		return paasDeploymentWS.getVirtualMachinesByPaaSService(resId);
	}
	
//	@Override
//	public Collection<SERVICE_REPRESENTATION_TYPE> getResourcesByWorkgroup(
//			Long workgroupId) {
//		try {
//			try {
//				GenericDAO<SERVICE_ENTITY_TYPE, ?> dao = daoProvider.getPaaSServiceDAO(getPaaSServiceEntityClass());
//				Collection<DBaaS> result = dao.
//						.getDBaaSByUserAndWorkgroup(workgroupId, null);
//
//				return dbaasMH.getDSL(result);
//			} catch (EJBException ejbe) {
//				throw ejbe.getCausedByException();
//			}
//		} catch (Exception e) {
//			throw new PrismaWrapperException(e.getMessage(), e);
//		}
//	}
}
