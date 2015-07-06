package it.prisma.businesslayer.utils.mappinghelpers;

import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;

import javax.inject.Inject;

public class APPaaSEnvironmentMappingHelper extends
		MappingHelperBase<AppaaSEnvironment, APPaaSEnvironmentRepresentation> {

	@Inject
	protected MappingHelper<PaaSService, PaaSServiceRepresentation> paasServiceMH;

	@Deprecated
	public static AppaaSEnvironment generateFromAPPaaSDeployRequest(
			AppaaS appaas, APPaaSDeployRequest request) {
		AppaaSEnvironment env = new AppaaSEnvironment();

		env.setAppaaS(appaas.getPaaSService());

		PaaSService paassvc = PaaSServiceMappingHelper
				.generateFromPaaSServiceDeployRequest(appaas.getPaaSService()
						.getWorkgroup(), appaas.getPaaSService()
						.getUserAccount(), request,
						PaaSServiceType.APPaaSEnvironment);

		//env.setServerType(request.get().getServerType());
		env.setPaaSService(paassvc);

		return env;
	}
	
	public static AppaaSEnvironment generateFromAPPaaSDeployRequest(
			AppaaS appaas, APPaaSEnvironmentDeployRequest request) {
		AppaaSEnvironment env = new AppaaSEnvironment();

		env.setAppaaS(appaas.getPaaSService());

		PaaSService paassvc = PaaSServiceMappingHelper
				.generateFromPaaSServiceDeployRequest(appaas.getPaaSService()
						.getWorkgroup(), appaas.getPaaSService()
						.getUserAccount(), request,
						PaaSServiceType.APPaaSEnvironment);

		env.setServerType(request.getEnvironmentDetails().getServerType());
		env.setPaaSService(paassvc);

		return env;
	}

	@Override
	public AppaaSEnvironment getEntity(APPaaSEnvironmentRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public APPaaSEnvironmentRepresentation getDSL(AppaaSEnvironment obj) {
		APPaaSEnvironmentRepresentation dsl = new APPaaSEnvironmentRepresentation();

		PaaSServiceRepresentation paasSvc = paasServiceMH.getDSL(obj
				.getPaaSService());
		dsl = (APPaaSEnvironmentRepresentation) paasSvc.populateSubclass(dsl);

		dsl.setId(obj.getId());
		dsl.setAppaasId(obj.getAppaaS().getId());
		dsl.setServerType(obj.getServerType());
		// TODO
		// dsl.setApplicationRepositoryEntry(obj.getApplicationRepositoryEntry());

		return dsl;
	}
}
