package it.prisma.businesslayer.bizws.paas.services.appaas;

import it.prisma.businesslayer.bizlib.accounting.PrismaUserDetails;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestrator;
import it.prisma.businesslayer.bizlib.paas.services.appaas.APPaaSManagement;
import it.prisma.businesslayer.bizlib.paas.services.appaas.apprepo.AppRepositoryManagement;
import it.prisma.businesslayer.bizlib.paas.services.appaas.apprepo.StoredObject;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.bizws.orchestrator.OrchestratorMainWS;
import it.prisma.businesslayer.bizws.paas.deployments.PaaSDeploymentWS;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.dal.dao.accounting.UserAccountDAO;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSEnvironmentDAO;
import it.prisma.dal.dao.paas.services.appaas.ApplicationRepositoryEntryDAO;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEvent;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entities.paas.services.appaas.ApplicationRepositoryEntry;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.deployments.VirtualMachineRepresentation;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.security.core.context.SecurityContextHolder;

public class APPaaSWSImpl extends BaseWS implements APPaaSWS {

	@EJB
	protected APPaaSManagement appaaSMgmt;

	@Inject
	protected MappingHelper<AppaaS, APPaaSRepresentation> appaaSMH;

	@Inject
	protected MappingHelper<AppaaSEnvironment, APPaaSEnvironmentRepresentation> appaaSEnvMH;

	@Inject
	protected MappingHelper<PaaSServiceEvent, PaaSServiceEventRepresentation> paaSServiceEventMH;

	@Inject
	protected OrchestratorMainWS orcWS;

	@Inject
	DeploymentOrchestrator deploymentOrchestrator;

	@Inject
	protected PaaSDeploymentWS paasDeploymentWS;

	@Inject
	UserAccountDAO userDAO;

	@Inject
	WorkgroupDAO workgroupDAO;

	@Inject
	ApplicationRepositoryEntryDAO appRepoEntryDAO;

	@Inject
	APPaaSEnvironmentDAO appaaSEnvironmentDAO;

	@Inject
	APPaaSDAO appaaSDAO;

	@EJB
	private AppRepositoryManagement appRepoBean;

	@Override
	public APPaaSRepresentation createResource(Long workgroupId,
			APPaaSDeployRequest deployRequest) {
		return orcWS.provisioningAPPaaS(deployRequest);
	}

	@Override
	public APPaaSRepresentation getAPPaaSById(Long workgroupId, Long appId) {
		// TODO Authentication !
		try {
			AppaaS result = appaaSMgmt.getAPPaaSById(appId);
			if (result == null)
				throw new ResourceNotFoundException(APPaaSRepresentation.class,
						"workgroupId=" + workgroupId, "id=" + appId);

			return appaaSMH.getDSL(result);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public APPaaSRepresentation updateAPPaaSById(Long workgroupId, Long appId,
			APPaaSRepresentation appRepresentation) {
		PaaSService service = appaaSMgmt.updateAPPaaSById(appId,
				appRepresentation);
		APPaaSRepresentation app = new APPaaSRepresentation();
		app.setId(service.getId());
		app.setCreatedAt(app.getCreatedAt());
		app.setDescription(service.getDescription());
		app.setName(service.getName());
		return app;
	}

	@Override
	public Collection<APPaaSRepresentation> getAPPaaSByWorkgroupAndUser(
			Long workgroupId, String considerDeletedToo) {
		try {
			try {
				Collection<AppaaS> result = appaaSMgmt
						.getAPPaaSByUserAndWorkgroup(workgroupId, null,
								Boolean.parseBoolean(considerDeletedToo));

				return appaaSMH.getDSL(result);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Collection<APPaaSEnvironmentRepresentation> getAllAPPaaSEnvironment(
			Long workgroupId, Long appId, String considerDeletedToo) {
		try {
			try {
				Collection<AppaaSEnvironment> result = appaaSMgmt
						.getAPPaaSEnvironmentByAppId(appId,
								Boolean.parseBoolean(considerDeletedToo));

				return appaaSEnvMH.getDSL(result);
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public APPaaSEnvironmentRepresentation getAPPaaSEnvironmentById(
			Long workgroupId, Long appId, Long envId) {
		try {
			AppaaSEnvironment result = appaaSMgmt.getAPPaaSEnvironment(appId,
					envId);

			if (result == null)
				throw new ResourceNotFoundException(
						APPaaSEnvironmentRepresentation.class,
						"appId=" + appId, "envId=" + envId);

			return appaaSEnvMH.getDSL(result);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Collection<PaaSServiceEventRepresentation> getAPPaaSEnvironmentEventsByEnvId(
			HttpServletRequest request, Long workgroupId, Long appId, Long envId) {
		try {

			Collection<PaaSServiceEvent> events = appaaSMgmt
					.getAPPaaSEnvironmentEvents(appId, envId, 0L, 9999999L, 10L);
			return paaSServiceEventMH.getDSL(events);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Response headAPPaaSEnvAppSrcFile(final Long workgroupId,
			final Long appId, final Long envId) {
		try {
			return getAPPaaSEnvAppSrcFileInternal(workgroupId, appId, envId,
					true);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Response getAPPaaSEnvAppSrcFile(final Long workgroupId,
			final Long appId, final Long envId) {

		try {
			return getAPPaaSEnvAppSrcFileInternal(workgroupId, appId, envId,
					false);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	protected Response getAPPaaSEnvAppSrcFileInternal(final Long workgroupId,
			final Long appId, final Long envId, final boolean metaOnly)
			throws Exception {
		Workgroup workgroup = workgroupDAO.findById(workgroupId);
		AppaaSEnvironment appaasEnv = appaaSEnvironmentDAO.findById(envId);
		if (appaasEnv == null)
			throw new ResourceNotFoundException(AppaaSEnvironment.class,
					"appId", "envId");

		AppaaS appaas = appaaSDAO.findById(appId);

		ApplicationRepositoryEntry appRepoEntry = appaasEnv
				.getApplicationRepositoryEntry();

		StoredObject object;
		if (metaOnly)
			object = appRepoBean.getAPPaaSEnvAppSrcFileMeta(workgroup, appaas,
					appaasEnv);
		else
			object = appRepoBean.getAPPaaSEnvAppSrcFile(workgroup, appaas,
					appaasEnv);

		// Extract Content-Type
		ResponseBuilder rb;
		if (metaOnly)
			rb = Response.ok();
		else
			rb = Response.ok(object.getInputStream());

		if (object.getMetadata().get(HttpHeaders.CONTENT_TYPE) != null)
			rb.type((String) object.getMetadata().get(HttpHeaders.CONTENT_TYPE));

		String length = (String) object.getMetadata().get(
				HttpHeaders.CONTENT_LENGTH);
		if (length != null)
			rb.header(HttpHeaders.CONTENT_LENGTH, length);

		rb.header("Content-Disposition",
				"attachment; filename=" + appRepoEntry.getFileName());

		return rb.build();
	}

	@Override
	public Collection<VirtualMachineRepresentation> getAppaaSEnvVirtualMachines(
			Long workgroupId, Long appId, Long envId) {
		return paasDeploymentWS.getVirtualMachinesByPaaSService(envId);
	}

	@Override
	public Collection<APPaaSRepresentation> getResourcesByWorkgroup(
			Long workgroupId, String considerDeletedToo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public APPaaSRepresentation getResourceById(Long workgroupId, Long appId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PaaSServiceEventRepresentation> getResourceEvents(
			HttpServletRequest request, Long workgroupId, Long envId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public APPaaSEnvironmentRepresentation createAppaaSEnv(Long workgroupId,
			Long appId, APPaaSEnvironmentDeployRequest deployRequest) {
		return orcWS.provisioningAPPaaSEnvironment(deployRequest);
	}

	@Override
	public Collection<VirtualMachineRepresentation> getPaaSServiceVirtualMachines(
			Long workgroupId, Long resId) {
		try {
			getSessionServletResponse().sendError(404,
					"Operation not supported.");
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

		return null;
	}

	@Override
	public void deleteAppaaSEnv(final Long workgroupId, final Long appId,
			Long envId) {
		try {
			Long userAccountId = ((PrismaUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal())
					.getUserData().getUserAccountId();
			deploymentOrchestrator.undeployAPPaaSEnvironment(userAccountId,
					workgroupId, appId, envId);
			getSessionServletResponse().setStatus(
					Status.ACCEPTED.getStatusCode());
		} catch (PrismaWrapperException pwe) {
			throw pwe;
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
}
