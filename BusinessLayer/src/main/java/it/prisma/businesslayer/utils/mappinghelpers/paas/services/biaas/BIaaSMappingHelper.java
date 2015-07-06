package it.prisma.businesslayer.utils.mappinghelpers.paas.services.biaas;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.businesslayer.utils.mappinghelpers.PaaSServiceMappingHelper;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.biaas.BIaaS;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDetails;

import javax.inject.Inject;

public class BIaaSMappingHelper extends
		MappingHelperBase<BIaaS, BIaaSRepresentation> {

	@Inject
	protected MappingHelper<PaaSService, PaaSServiceRepresentation> paasServiceMH;

	public static BIaaS generateFromDeployRequest(BIaaSDeployRequest request,
			UserAccount userAccount, Workgroup workgroup) {

		BIaaS db = new BIaaS();

		PaaSService paassvc = PaaSServiceMappingHelper
				.generateFromPaaSServiceDeployRequest(workgroup, userAccount,
						request, PaaSServiceType.BIaaS);

		db.setPaaSService(paassvc);
		BIaaSDetails det = request.getBIaasDetails();
		
		db.setConfigurationVariant(det.getConfigurationVariant());
		db.setProductType(det.getProductType());
		db.setProductVersion(det.getProductVersion());

		db.setVerticalScalingEnabled(false);
		return db;
	}

	@Override
	public BIaaS getEntity(BIaaSRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public BIaaSRepresentation getDSL(BIaaS obj) {
		BIaaSRepresentation dsl = new BIaaSRepresentation();

		PaaSServiceRepresentation paasSvc = paasServiceMH.getDSL(obj
				.getPaaSService());
		dsl = (BIaaSRepresentation) paasSvc.populateSubclass(dsl);

		dsl.setId(obj.getId());
		dsl.setConfigurationVariant(obj.getConfigurationVariant());
		dsl.setProductType(obj.getProductType());
		dsl.setProductVersion(obj.getProductVersion());
		dsl.setBackupEnabled(obj.isBackupEnabled());
		dsl.setVerticalScalingEnabled(obj.isVerticalScalingEnabled());
		dsl.setVerticalScalingMaxDiskSize(obj.getVerticalScalingMaxDiskSize());
		dsl.setVerticalScalingMaxFlavor(obj.getVerticalScalingMaxFlavor());

		return dsl;
	}
}
