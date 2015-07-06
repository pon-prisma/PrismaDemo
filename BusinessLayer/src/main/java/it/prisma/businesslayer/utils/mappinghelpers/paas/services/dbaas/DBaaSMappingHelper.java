package it.prisma.businesslayer.utils.mappinghelpers.paas.services.dbaas;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.businesslayer.utils.mappinghelpers.PaaSServiceMappingHelper;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.dbaas.DBaaS;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDetails;

import javax.inject.Inject;

public class DBaaSMappingHelper extends
		MappingHelperBase<DBaaS, DBaaSRepresentation> {

	@Inject
	protected MappingHelper<PaaSService, PaaSServiceRepresentation> paasServiceMH;

	public static DBaaS generateFromDeployRequest(
			DBaaSDeployRequest request, UserAccount userAccount,
			Workgroup workgroup) {

		DBaaS db = new DBaaS();

		PaaSService paassvc = PaaSServiceMappingHelper
				.generateFromPaaSServiceDeployRequest(workgroup, userAccount,
						request, PaaSServiceType.DBaaS);

		db.setPaaSService(paassvc);
		//
		DBaaSDetails det = request.getDBaasDetails();

		db.setBackupEnabled(false);
		if (det.getBackup() != null) {
			db.setBackupEnabled(true);
			db.setBackupInterval(det.getBackup().getInterval());
		}

		db.setDiskEncryptionEnabled(det.getDiskEncryptionEnabled());
		db.setDiskSize(det.getDiskSize());
		db.setProductType(det.getProductType());
		db.setProductVersion(det.getProductVersion());

		db.setVerticalScalingEnabled(false);
		if (det.getVerticalScaling() != null) {
			db.setBackupEnabled(true);
			db.setVerticalScalingMaxDiskSize(det.getVerticalScaling()
					.getMaxDiskSize());
			db.setVerticalScalingMaxFlavor(det.getVerticalScaling()
					.getMaxFlavor());
		}

		return db;
	}

	@Override
	public DBaaS getEntity(DBaaSRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public DBaaSRepresentation getDSL(DBaaS obj) {
		DBaaSRepresentation dsl = new DBaaSRepresentation();

		PaaSServiceRepresentation paasSvc = paasServiceMH.getDSL(obj
				.getPaaSService());
		dsl = (DBaaSRepresentation) paasSvc.populateSubclass(dsl);

		dsl.setId(obj.getId());
		dsl.setProductType(obj.getProductType());
		dsl.setProductVersion(obj.getProductVersion());
		dsl.setBackupEnabled(obj.isBackupEnabled());
		dsl.setDiskEncryptionEnabled(obj.isDiskEncryptionEnabled());
		dsl.setDiskSize(obj.getDiskSize());
		dsl.setVerticalScalingEnabled(obj.isVerticalScalingEnabled());
		dsl.setVerticalScalingMaxDiskSize(obj.getVerticalScalingMaxDiskSize());
		dsl.setVerticalScalingMaxFlavor(obj.getVerticalScalingMaxFlavor());

		return dsl;
	}
}
