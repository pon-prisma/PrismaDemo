package it.prisma.businesslayer.utils.mappinghelpers;

import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;

import javax.inject.Inject;

public class APPaaSMappingHelper extends
		MappingHelperBase<AppaaS, APPaaSRepresentation> {

	@Inject
	protected MappingHelper<PaaSService, PaaSServiceRepresentation> paasServiceMH;

	public static AppaaS generateFromAPPaaSDeployRequest(Workgroup workgroup, UserAccount user,
			APPaaSDeployRequest request) {
		AppaaS appaas = new AppaaS();

		PaaSService paassvc = PaaSServiceMappingHelper
				.generateFromPaaSServiceDeployRequest(workgroup, user, request,
						PaaSServiceType.APPaaS);
		
		appaas.setPaaSService(paassvc);
		appaas.setDescription(request.getServiceDetails().getDescription());
		appaas.setName(request.getServiceDetails().getName());

		return appaas;
	}
	
	@Override
	public AppaaS getEntity(APPaaSRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public APPaaSRepresentation getDSL(AppaaS obj) {
		APPaaSRepresentation dsl = new APPaaSRepresentation();

		PaaSServiceRepresentation paasSvc = paasServiceMH.getDSL(obj
				.getPaaSService());
		dsl = (APPaaSRepresentation) paasSvc.populateSubclass(dsl);
		
//		dsl.setId(obj.getId());
//		dsl.setCreatedAt(obj.getCreatedAt());
//		dsl.setDescription(obj.getDescription());
//		dsl.setName(obj.getName());
//		dsl.setStatus(obj.getStatus());
//		dsl.setUserId(obj.getUserAccount().getId());
//		dsl.setWorkgroupId(obj.getWorkgroup().getId());
		
		return dsl;
	}
}
