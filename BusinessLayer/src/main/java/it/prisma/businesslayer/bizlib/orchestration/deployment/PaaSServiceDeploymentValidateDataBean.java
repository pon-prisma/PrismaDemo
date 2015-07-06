package it.prisma.businesslayer.bizlib.orchestration.deployment;

import it.prisma.businesslayer.bizlib.accounting.UserManagement;
import it.prisma.businesslayer.bizlib.accounting.WorkgroupManagement;
import it.prisma.businesslayer.bizlib.common.exceptions.BadRequestException;
import it.prisma.businesslayer.bizlib.common.exceptions.DuplicatedResourceException;
import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizlib.common.exceptions.UnauthorizedException;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProvider;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProviderRegistry;
import it.prisma.businesslayer.bizlib.paas.services.appaas.APPaaSManagement;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.utils.mappinghelpers.APPaaSEnvironmentMappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.APPaaSMappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.paas.services.dbaas.DBaaSMappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.paas.services.mqaas.MQaaSMappingHelper;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.appaas.ApplicationRepositoryEntryDAO;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entities.paas.services.appaas.ApplicationRepositoryEntry;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PaaSServiceDeploymentValidateDataBean implements
		PaaSServiceDeploymentValidateData {

	@Inject
	private APPaaSManagement appaasBean;

	@Inject
	private ApplicationRepositoryEntryDAO appEntryDAO;

	@Inject
	private UserManagement userBean;

	@Inject
	private WorkgroupManagement workgroupBean;

	@Inject
	private PaaSServiceDAO paasServiceDAO;

	@Inject
	private PaaSServiceDeploymentProviderRegistry deploymentProviders;

	@Inject
	private EnvironmentConfig envConfig;

	@SuppressWarnings("unchecked")
	@Override
	public AbstractPaaSService generateAndValidatePaaSService(
			GenericPaaSServiceDeployRequest<?> deployRequest) {

		if (deployRequest.getServiceDetails().getName().equals(""))
			throw new BadRequestException("Service name cannot be empty");

		// TODO: Improve validation & move elsewhere
		UserAccount user = userBean.getUserAccountByUserAccountId(deployRequest
				.getAccount().getUserId());
		Workgroup wg = workgroupBean.getWorkgroupById(deployRequest
				.getAccount().getWorkgroupId());

		// Check PaaS Service already exists
		if (paasServiceDAO.findByTypeAndNameAndWorkgroup(PaaSServiceType
				.valueOf(deployRequest.getPaaSServiceType().name()),
				deployRequest.getServiceDetails().getName(), wg.getId()) != null)
			throw new DuplicatedResourceException(PaaSService.class);

		// Domain Name validation
		if (!(deployRequest instanceof APPaaSDeployRequest)) {
			String dn = deployRequest.getServiceDetails().getDomainName();
			if (dn == null
					|| !dn.matches("^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$"))
				throw new BadRequestException("Domain Name is not valid.  ");

			String dnSuffix = envConfig
					.getSvcEndpointProperty(EnvironmentConfig.SVCEP_DN_SUFFIX);
			if (!dn.matches("^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.){1}("
					+ dnSuffix + ")$"))
				throw new BadRequestException(String.format(
						"Domain Name should match '<name>.%s'", dnSuffix));

			if (paasServiceDAO.findByDomainName(dn) != null)
				throw new DuplicatedResourceException(String.format(
						"Domain Name '%s' already exists", dn));
		}

		// Service-specific validation
		if (deployRequest instanceof APPaaSDeployRequest) {
			APPaaSDeployRequest appRequest = (APPaaSDeployRequest) deployRequest;

			AppaaS app = APPaaSMappingHelper.generateFromAPPaaSDeployRequest(
					wg, user, appRequest);

			return app;
		} else if (deployRequest instanceof APPaaSEnvironmentDeployRequest) {
			APPaaSEnvironmentDeployRequest appRequest = (APPaaSEnvironmentDeployRequest) deployRequest;

			// Check App exists
			AppaaS app = appaasBean.checkAPPaaSExists(new Long(appRequest
					.getEnvironmentDetails().getAppId()));

			// Check Environment name doesn't already exist
			appaasBean
					.checkAPPaaSEnvironmentDuplicated(appRequest.getAccount()
							.getWorkgroupId(), appRequest.getServiceDetails()
							.getName());

			// Check AppSrcFile
			ApplicationRepositoryEntry appRepoEntry = appEntryDAO
					.findById(appRequest.getEnvironmentDetails().getAppFileId());

			// AppSrcFile exists?
			if (appRepoEntry == null)
				throw new ResourceNotFoundException(
						ApplicationRepositoryEntry.class);

			// Check private & ownership
			if (!appRepoEntry.isShared()
					&& appRepoEntry.getWorkgroup().getId() != appRequest
							.getAccount().getWorkgroupId())
				throw new UnauthorizedException("AppSrcFile private");

			AppaaSEnvironment appEnv = APPaaSEnvironmentMappingHelper
					.generateFromAPPaaSDeployRequest(app, appRequest);

			appEnv.setApplicationRepositoryEntry(appRepoEntry);

			return appEnv;
		} else if (deployRequest instanceof MQaaSDeployRequest) {
			MQaaSDeployRequest mqRequest = (MQaaSDeployRequest) deployRequest;

			return MQaaSMappingHelper.generateFromDeployRequest(mqRequest,
					user, wg);
		} else if (deployRequest instanceof DBaaSDeployRequest) {
			DBaaSDeployRequest dbRequest = (DBaaSDeployRequest) deployRequest;

			return DBaaSMappingHelper.generateFromDeployRequest(dbRequest,
					user, wg);
		} else if (deployRequest instanceof BIaaSDeployRequest) {
			return ((PaaSServiceDeploymentProvider<AbstractPaaSService, GenericPaaSServiceDeployRequest<?>>) deploymentProviders
					.getProviderByPaaSType(PaaSServiceType.BIaaS))
					.generateAndValidatePaaSService(deployRequest, user, wg);
			// BIaaSDeployRequest biRequest = (BIaaSDeployRequest)
			// deployRequest;
			//
			// return BIaaSMappingHelper.generateFromDeployRequest(biRequest,
			// user, wg);
		} else if (deployRequest instanceof VMDeployRequest) {
			return ((PaaSServiceDeploymentProvider<AbstractPaaSService, GenericPaaSServiceDeployRequest<?>>) deploymentProviders
					.getProviderByPaaSType(PaaSServiceType.VMaaS))
					.generateAndValidatePaaSService(deployRequest, user, wg);
		} else
			throw new IllegalArgumentException("PaaSService type not found");
	}

}
