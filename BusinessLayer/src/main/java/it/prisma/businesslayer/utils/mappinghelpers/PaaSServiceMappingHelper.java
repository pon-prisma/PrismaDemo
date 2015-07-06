package it.prisma.businesslayer.utils.mappinghelpers;

import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.ProductSpecificParameters;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment.Status;
import it.prisma.domain.dsl.paas.services.PaaSServiceEndpointRepresentation;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.generic.request.Environment;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.paas.services.generic.request.ServiceDetails;
import it.prisma.utils.json.JsonUtility;

import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaaSServiceMappingHelper extends
		MappingHelperBase<PaaSService, PaaSServiceRepresentation> {

	private static final Logger LOG = LogManager
			.getLogger(PaaSServiceMappingHelper.class);

	@Inject
	private PaaSServiceEndpointMappingHelper pseMH;

	// public static PaaSService generateFromPaaSServiceDeployRequest(
	// Workgroup workgroup, UserAccount userAccount,
	// APPaaSDeployRequest request, PaaSService.PaaSServiceType type) {
	//
	// PaaSService paassvc = new PaaSService();
	// paassvc.setType(type.toString());
	//
	// // TODO Check
	// paassvc.setWorkgroup(workgroup);
	// paassvc.setUserAccount(userAccount);
	//
	// if (type == PaaSServiceType.APPaaSEnvironment) {
	//
	// if (request.getEnvironmentParams().getSecureConnection() != null) {
	// paassvc.setCertificatePath(request.getEnvironmentParams()
	// .getSecureConnection().getCertificatePath());
	// paassvc.setSecureConnectionEnabled(request
	// .getEnvironmentParams().getSecureConnection() != null);
	// }
	//
	// paassvc.setName(request.getEnvironmentParams().getEnvName());
	// paassvc.setDescription(request.getEnvironmentParams()
	// .getEnvDescription());
	//
	// paassvc.setIaaSFlavor(request.getEnvironmentParams()
	// .getIaaSFlavor());
	// paassvc.setQoS(request.getEnvironmentParams()
	// .getQosProfile());
	// paassvc.setLoadBalancingInstances(request.getEnvironmentParams()
	// .getLoadBalancingInstances());
	// paassvc.setNotificationEmail(request.getEnvironmentParams()
	// .getNotificationEmail());
	// paassvc.setDomainName(request.getEnvironmentParams()
	// .getEnvPublicEndpoint());
	// paassvc.setSecurityGroup(request.getEnvironmentParams()
	// .getSecurityGroup());
	// paassvc.setStatus(Status.DEPLOY_IN_PROGRESS.toString());
	//
	// paassvc.setAvailabilityZone(request.getEnvironmentParams()
	// .getAvailabilityZone());
	// } else {
	// paassvc.setDescription(request.getApplicationParams()
	// .getAppDescription());
	// paassvc.setName(request.getApplicationParams().getAppName());
	// }
	//
	// return paassvc;
	// }

	public static PaaSService generateFromPaaSServiceDeployRequest(
			Workgroup workgroup, UserAccount userAccount,
			GenericPaaSServiceDeployRequest request,
			PaaSService.PaaSServiceType type) {

		PaaSService paassvc = new PaaSService();
		paassvc.setType(type.toString());

		// Account
		paassvc.setWorkgroup(workgroup);
		paassvc.setUserAccount(userAccount);

		// Service Details
		ServiceDetails det = request.getServiceDetails();
		paassvc.setName(det.getName());
		paassvc.setDescription(det.getDescription());
		paassvc.setDomainName(det.getDomainName());
		paassvc.setNotificationEmail(det.getNotificationEmail());
		paassvc.setLoadBalancingInstances(det.getLoadBalancingInstances());

		// > Secure Connection
		paassvc.setSecureConnectionEnabled(false);
		if (request.getServiceDetails().getSecureConnection() != null) {
			paassvc.setCertificatePath(request.getServiceDetails()
					.getSecureConnection().getCertificatePath());
			paassvc.setSecureConnectionEnabled(true);
		}

		// > Environment
		Environment env = request.getServiceDetails().getEnvironment();
		if (env != null) {
			paassvc.setIaaSFlavor(env.getIaaSFlavor());
			paassvc.setQoS(env.getQos());
			paassvc.setAvailabilityZone(env.getAvailabilityZone());
		}

		if (type == PaaSServiceType.APPaaS) {
			paassvc.setStatus(Status.RUNNING.toString());
		} else {
			paassvc.setStatus(Status.DEPLOY_PENDING.toString());
		}

		return paassvc;
	}

	@Override
	public PaaSService getEntity(PaaSServiceRepresentation obj) {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public PaaSServiceRepresentation getDSL(PaaSService obj) {
		PaaSServiceRepresentation dsl = new PaaSServiceRepresentation();

		dsl.setId(obj.getId());
		dsl.setCertificatePath(obj.getCertificatePath());
		dsl.setCreatedAt(obj.getCreatedAt());
		dsl.setDescription(obj.getDescription());
		dsl.setDomainName(obj.getDomainName());
		dsl.setErrorDescription(obj.getErrorDescription());
		dsl.setIaaSFlavor(obj.getIaaSFlavor());
		dsl.setId(obj.getId());
		dsl.setLoadBalancingInstances(obj.getLoadBalancingInstances());
		dsl.setName(obj.getName());
		dsl.setNotificationEmail(obj.getNotificationEmail());
		dsl.setOperation(obj.getOperation());
		// TODO
		// dsl.setPaaSServiceEvents(obj.getPaaSServiceEvents());
		dsl.setSecureConnectionEnabled(obj.getSecureConnectionEnabled());
		dsl.setSecurityGroup(obj.getSecurityGroup());
		dsl.setStatus(obj.getStatus());
		dsl.setUserId(obj.getUserAccount().getId());
		dsl.setWorkgroupId(obj.getWorkgroup().getId());
		dsl.setAvailabilityZone(obj.getAvailabilityZone());
		dsl.setPublicVisibility(obj.isPublicVisibility());
		dsl.setQoS(obj.getQoS());

		dsl.setPaasServiceEndpoints((List<PaaSServiceEndpointRepresentation>) pseMH
				.getDSL(obj.getPaasServiceEndpoints()));

		try {
			ProductSpecificParameters params = obj
					.getProductSpecificParameters();
			if (params != null)
				dsl.setProductSpecificParameters((it.prisma.domain.dsl.paas.services.ProductSpecificParameters) JsonUtility.deserializeJson(
						JsonUtility.serializeJson(params),
						it.prisma.domain.dsl.paas.services.ProductSpecificParameters.class));
		} catch (Exception e) {
			LOG.warn("Error mapping PaaSService [" + obj + "]", e);
		}

		return dsl;
	}
}
