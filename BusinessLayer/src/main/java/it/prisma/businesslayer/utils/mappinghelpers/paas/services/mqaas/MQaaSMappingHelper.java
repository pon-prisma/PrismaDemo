package it.prisma.businesslayer.utils.mappinghelpers.paas.services.mqaas;

import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperBase;
import it.prisma.businesslayer.utils.mappinghelpers.PaaSServiceMappingHelper;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.mqaas.MQaaS;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;
import it.prisma.domain.dsl.paas.services.mqaas.QueueDetails;

import javax.inject.Inject;

public class MQaaSMappingHelper extends
		MappingHelperBase<MQaaS, MQaaSRepresentation> {

	@Inject
	protected MappingHelper<PaaSService, PaaSServiceRepresentation> paasServiceMH;

	public static MQaaS generateFromDeployRequest(MQaaSDeployRequest request,
			UserAccount userAccount, Workgroup workgroup) {

		MQaaS mq = new MQaaS();

		PaaSService paassvc = PaaSServiceMappingHelper
				.generateFromPaaSServiceDeployRequest(workgroup, userAccount,
						request, PaaSServiceType.MQaaS);

		mq.setPaaSService(paassvc);
		// TODO: Improve & remove hardcoded product info
		QueueDetails det = request.getMQaasDetails();
		mq.setProductType("RabbitMQ");
		mq.setProductVersion("3.1");

		return mq;
	}

	@Override
	public MQaaS getEntity(MQaaSRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public MQaaSRepresentation getDSL(MQaaS obj) {
		MQaaSRepresentation dsl = new MQaaSRepresentation();

		PaaSServiceRepresentation paasSvc = paasServiceMH.getDSL(obj
				.getPaaSService());
		dsl = (MQaaSRepresentation) paasSvc.populateSubclass(dsl);

		// TODO: Improve
		dsl.setProductType(obj.getProductType());
		dsl.setProductVersion(obj.getProductVersion());
		dsl.setId(obj.getId());

		return dsl;
	}
}
