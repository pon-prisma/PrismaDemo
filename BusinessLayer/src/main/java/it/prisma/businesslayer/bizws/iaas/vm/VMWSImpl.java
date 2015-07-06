package it.prisma.businesslayer.bizws.iaas.vm;

import it.prisma.businesslayer.bizlib.accounting.PrismaUserDetails;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.iaas.services.vm.VMMgmtBean;
import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestrator;
import it.prisma.businesslayer.bizlib.paas.services.PaaSServiceHelper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.bizws.orchestrator.OrchestratorMainWS;
import it.prisma.businesslayer.bizws.paas.services.PaaSServiceGenericWSImpl;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.iaas.services.IaaSNetworkDAO;
import it.prisma.dal.dao.iaas.services.vm.VMaaSDAO;
import it.prisma.dal.dao.paas.deployments.PaaSDeploymentDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.iaas.services.IaaSNetwork;
import it.prisma.dal.entities.iaas.services.vm.VMaaS;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.VMaaSMonitoringStatus;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

public class VMWSImpl extends
		PaaSServiceGenericWSImpl<VMRepresentation, VMDeployRequest> implements
		VMWS {

	private static Logger LOG = LogManager.getLogger(VMWSImpl.class);

	@Inject
	protected VMMgmtBean vmMgmt;

	@Inject
	protected MappingHelper<VMaaS, VMRepresentation> vmMH;

	@Inject
	protected MappingHelper<PaaSServiceEvent, PaaSServiceEventRepresentation> paaSServiceEventMH;

	@Inject
	protected EnvironmentConfig envConfig;

	@Inject
	protected PaaSServiceHelper paasSvcHelper;

	@Inject
	protected PaaSServiceDAO paasSvcDAO;

	@Inject
	protected PaaSDeploymentDAO paasDeploymentDAO;

	@Override
	public Collection<VMRepresentation> getResourcesByWorkgroup(
			Long workgroupId, String considerDeletedToo) {
		try {
			try {
				Collection<VMaaS> result = vmMgmt.getVMByUserAndWorkgroup(
						workgroupId, null,
						Boolean.parseBoolean(considerDeletedToo));

				return vmMH.getDSL(result);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e.getMessage(), e);
		}
	}

	@Override
	public VMRepresentation getResourceById(Long workgroupId, Long vmId) {
		try {
			try {
				VMaaS result = vmMgmt.getVMaaSById(vmId);

				if (result == null)
					throw new ResourceNotFoundException(VMRepresentation.class, "workgroupId="+workgroupId, "id="+vmId);

				return vmMH.getDSL(result);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (EntityNotFoundException enfe) {
			throw new ResourceNotFoundException(VMRepresentation.class, "workgroupId="+workgroupId, "id="+vmId);
		} catch (Exception e) {
			throw new PrismaWrapperException(e.getMessage(), e);
		}
	}

	@Override
	public Collection<PaaSServiceEventRepresentation> getResourceEvents(
			HttpServletRequest request, Long workgroupId, Long envId) {
		try {

			Collection<PaaSServiceEvent> events = vmMgmt.getVMEvents(envId, 0L,
					Long.MAX_VALUE, 1000L);

			return paaSServiceEventMH.getDSL(events);
		} catch (Exception e) {
			// return handleGenericException(e);
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public VMRepresentation stopService(Long workgroupId, Long resId) {
		try {
			VMaaS result = vmMgmt.stopVirtualMachine(workgroupId, resId);
			return vmMH.getDSL(result);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public VMRepresentation startService(Long workgroupId, Long resId) {
		try {
			VMaaS result = vmMgmt.startVirtualMachine(workgroupId, resId);
			return vmMH.getDSL(result);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public VMRepresentation restartService(Long workgroupId, Long resId) {
		try {
			VMaaS result = vmMgmt.restartVirtualMachine(workgroupId, resId);
			return vmMH.getDSL(result);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	/* TEST */

	@Inject
	private VMaaSDAO vmDAO;

	@Inject
	private IaaSNetworkDAO netDAO;

	@Inject
	private WorkgroupDAO wgDAO;

	@Path("/test/nets/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public VMRepresentation testNets(@PathParam("id") Long id) {

		netDAO.findById(1L);

		try {
			VMaaS vm = vmMgmt.getVMaaSById(id);
			if (vm == null) {
				throw new javax.ws.rs.NotFoundException();
			}
			IaaSNetwork net1 = new IaaSNetwork();
			net1.setName("Net1");
			Workgroup w = wgDAO.findById(1L);
			net1.setWorkgroup(w);
			netDAO.create(net1);

			IaaSNetwork net2 = new IaaSNetwork();
			net2.setName("Net2");
			net2.setWorkgroup(w);
			netDAO.create(net2);

			vm.getIaaSNetworks().add(net1);
			vm.getIaaSNetworks().add(net2);

			vmDAO.update(vm);

			return vmMH.getDSL(vm);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public boolean updateStatus(VMaaSMonitoringStatus vMaaSMonitoringStatus) {
		try {
			return vmMgmt.updateVirtualMachineStatus(vMaaSMonitoringStatus);
		} catch (ResourceNotFoundException enfe) {
			throw new PrismaWrapperException(enfe);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Inject
	protected OrchestratorMainWS orcWS;

	@Override
	public VMRepresentation createResource(Long workgroupId,
			VMDeployRequest deployRequest) {
		return orcWS.provisioningVMaaS(deployRequest);
	}

	@Inject
	DeploymentOrchestrator deploymentOrchestrator;

	@Override
	public void deleteResource(final Long workgroupId, final Long resourceId) {
		try {
			Long userAccountId = ((PrismaUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal())
					.getUserData().getUserAccountId();
			deploymentOrchestrator.undeployVMaaS(userAccountId, workgroupId,
					resourceId);
			getSessionServletResponse().setStatus(
					Status.ACCEPTED.getStatusCode());
		} catch (PrismaWrapperException pwe) {
			throw pwe;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
}
