package it.prisma.presentationlayer.webui.core.paas.appaas;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.deployments.VirtualMachineRepresentation;
import it.prisma.domain.dsl.paas.services.PaaSServiceEventRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.event.Events;
import it.prisma.domain.dsl.paas.services.event.Row;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AppEnvManagementService {

	static final Logger LOG = LogManager
			.getLogger(AppEnvManagementService.class.getName());

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	public BootstrapTable<APPaaSEnvironmentRepresentation> getAllAppaaSEnv(
			Long workgroupId, Long id, RestWSParamsContainer params)
			throws Exception {

		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		BootstrapTable<APPaaSEnvironmentRepresentation> envs = new BootstrapTable<APPaaSEnvironmentRepresentation>();

		AuthTokenRepresentation authToken = ((PrismaUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal())
				.getUserToken();

		envs.setEnvironments(prismaBizAPIClient.getAllAPPaaSEnv(workgroupId,
				id, params, meta, authToken.getTokenId()));
		envs.setTotal(meta.getMeta().getPagination().getTotalCount());
		return envs;
	}

	public APPaaSEnvironmentRepresentation getAppaaSEnv(Long workgroupID,
			Long appId, Long id) throws Exception {

		// try {
		AuthTokenRepresentation authToken = ((PrismaUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal())
				.getUserToken();

		return prismaBizAPIClient.getAPPaaSEnv(workgroupID, appId, id,
				authToken.getTokenId());
		// } catch (MappingException | NoMappingModelFoundException
		// | ServerErrorResponseException | APIErrorException
		// | RestClientException | IOException e) {
		// e.printStackTrace();
		// LOG.error("error getting env " + e.getStackTrace());
		// }
		// return null;
	}

	public APPaaSEnvironmentRepresentation deployEnv(
			APPaaSEnvironmentDeployRequest request) throws Exception {

		AuthTokenRepresentation authToken = ((PrismaUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal())
				.getUserToken();

		return prismaBizAPIClient.deployPaaSService(request,
				APPaaSEnvironmentRepresentation.class, authToken.getTokenId());
	}

	public void undeployEnv(Long workgroupID, Long appId, Long id)
			throws Exception {

		AuthTokenRepresentation authToken = ((PrismaUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal())
				.getUserToken();
		APPaaSEnvironmentRepresentation aPPaaSEnvironmentRepresentation = new APPaaSEnvironmentRepresentation();
		aPPaaSEnvironmentRepresentation.setId(id);
		aPPaaSEnvironmentRepresentation.setAppaasId(appId);
		aPPaaSEnvironmentRepresentation.setWorkgroupId(workgroupID);
		prismaBizAPIClient.undeployPaaSService(aPPaaSEnvironmentRepresentation,
				authToken.getTokenId());
	}

	public Events getEvents(Long wgId, Long appId, Long envId,
			RestWSParamsContainer params) {

		try {
			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();

			AuthTokenRepresentation authToken = ((PrismaUserDetails) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal())
					.getUserToken();

			List<PaaSServiceEventRepresentation> list = prismaBizAPIClient
					.getAPPaaSEnvEvents(wgId, appId, envId,
							authToken.getTokenId(), params, meta);

			List<Row> rows = new ArrayList<Row>();

			for (PaaSServiceEventRepresentation serviceEvent : list) {
				Row row = new Row();
				row.setDate(String.valueOf(serviceEvent.getCreatedAt()
						.getTime()));
				row.setLevel(serviceEvent.getType());
				row.setDescription(serviceEvent.getDetails());
				rows.add(row);
			}

			Events e = new Events();
			e.setTotal(meta.getMeta().getPagination().getTotalCount()
					.intValue());
			e.setRows(rows);

			return e;

		} catch (Exception e) {

			LOG.error("Exception getting events : " + e);
		}
		return null;
	}

	public List<VirtualMachineRepresentation> getVirtualMachines(
			Long workgroupId, Long appId, Long envId) throws Exception {

		AuthTokenRepresentation authToken = ((PrismaUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal())
				.getUserToken();

		return prismaBizAPIClient.getVirtualMachines(workgroupId, appId, envId,
				authToken.getTokenId());

	}
}
