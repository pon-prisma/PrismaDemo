package it.prisma.businesslayer.bizlib.orchestration;

import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean;
import it.prisma.businesslayer.bizlib.orchestration.deployment.PaaSServiceDeploymentValidateData;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProvider;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProviderRegistry;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.heat.HeatPaaSServiceDeploymentProvider;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.bpm.BusinessProcessManager;
import it.prisma.businesslayer.orchestrator.bpm.BusinessProcessManager.RUNTIME_STRATEGY;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyDeploymentDataBuilder;
import it.prisma.businesslayer.orchestrator.dsl.DeployerDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.DeployerEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessageBuilder;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentDataBuilder;
import it.prisma.businesslayer.orchestrator.dsl.InfrastructureData;
import it.prisma.businesslayer.orchestrator.dsl.InfrastructureDataBuilder;
import it.prisma.businesslayer.orchestrator.dsl.MailEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.MailEndpointBuilder;
import it.prisma.businesslayer.orchestrator.dsl.MonitoringEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.MonitoringEndpointBuilder;
import it.prisma.businesslayer.orchestrator.dsl.PrismaZone;
import it.prisma.businesslayer.orchestrator.dsl.PrismaZoneBuilder;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.deployment.DeployerType;
import it.prisma.businesslayer.utils.PathHelper;
import it.prisma.businesslayer.utils.PathHelper.ResourceType;
import it.prisma.dal.dao.accounting.UserAccountDAO;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSDAO;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.iaas.services.vm.VMaaS;
import it.prisma.dal.entities.iaas.tenant.IaaSTenant;
import it.prisma.dal.entities.orchestrator.lrt.LRT;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entities.paas.services.biaas.BIaaS;
import it.prisma.dal.entities.paas.services.dbaas.DBaaS;
import it.prisma.dal.entities.paas.services.mqaas.MQaaS;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation.DBProductType;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.Error;
import it.prisma.utils.misc.EnumUtils;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Triplet;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@Stateless
public class DeploymentOrchestratorBean implements DeploymentOrchestrator {

	private final static String DEPLOY_WF_ID = "it.prisma.orchestrator.bp.services.paas.provisioning";
	private final static String ARG_IN_DEPLOYMENT_MSG = "DeploymentMessage";
	private final static String ARG_OUT_DEPLOYMENT_MSG = "DeploymentMessage";
	private final static String UNDEPLOY_WF_ID = "it.prisma.orchestrator.bp.services.paas.unprovisioning";
	public final static String ARG_IN_UNDEPLOYMENT_MSG = "UndeploymentMessage";

	public static Logger prismaLog = LogManager
			.getLogger(DeploymentOrchestratorBean.class);

	@Inject
	private EnvironmentConfig envConfig;

	@Inject
	private BusinessProcessManager bpManager;

	@Inject
	UserAccountDAO userDAO;

	@Inject
	WorkgroupDAO workgroupDAO;

	@Inject
	protected PaaSServiceDAO paasSvcDAO;

	@Inject
	private APPaaSDAO appaasDAO;

	@Inject
	private PaaSServiceDeploymentProviderRegistry deploymentProviders;

	@Inject
	private InfrastructureManagerBean infrastructureBean;

	// TODO Improve return type !
	// public Map<String, Object> deployAPPaaS(@Valid APPaaSDeployRequest
	// request)
	// throws Exception {
	//
	// checkAPPaaSPreconditions(request);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	//
	// params.put("CfyRecipeInfo", map);
	// params.put("CfyVersion", envConfig
	// .getSvcEndpointProperty(EnvironmentConfig.SVCEP_CFY_VERSION));
	//
	// params.put("MonitoringUrl", envConfig.getMonitoringURL());
	// params.put("MailserverUrl", envConfig.getMailServerURL());
	// params.put("CfyBaseUrl", envConfig.getCloudifyManagerURL());
	//
	// params.put("APPaaSDeployRequest", request);
	// params.put("User", userDAO.findById(request.getUserId()));
	// params.put("Workgroup", workgroupDAO.findById(request.getWorkgroupId()));
	//
	// params.put("ServiceName", "apacheLB");
	//
	// String recipeName = null;
	// // TODO add other server recipes
	// if (request.getEnvironmentParams() != null) {
	// switch (request.getEnvironmentParams().getServerType()) {
	// case "Tomcat":
	// recipeName = "servizio_tomcat.zip";
	// map.put("propertyFilePath", "tomcat/tomcat-service.properties");
	// break;
	// default:
	// recipeName = "servizio_tomcat.zip";
	// map.put("propertyFilePath", "tomcat/tomcat-service.properties");
	// }
	// }
	// String recipePath = PathHelper
	// .getResourcesPath(ResourceType.CLOUDIFY_RECIPES) + recipeName;
	// map.put("recipeName", recipeName);
	// map.put("recipePath", recipePath);
	//
	// params.put("RecipePath", recipePath);
	//
	// // Start process on jbpm runtime
	// Triplet<ProcessInstance, KieSession, LRT> bpResult = bpManager
	// .startProcess(
	// "it.prisma.orchestrator.bp.services.appaas.provisioning",
	// params, RUNTIME_STRATEGY.SINGLETON);
	// ProcessInstance processInstance = bpResult.getValue0();
	// LRT lrt = bpResult.getValue2();
	//
	// WorkflowProcessInstance wfInstance = (WorkflowProcessInstance)
	// processInstance;
	//
	// // Extract result from workflow
	// if (wfInstance.getState() != ProcessInstance.STATE_ABORTED) {
	// // Request was an Env deploy request ?
	// if (wfInstance.getVariable("APPaaSEnvironment") != null) {
	// AppaaSEnvironment appaasEnv = (AppaaSEnvironment) wfInstance
	// .getVariable("APPaaSEnvironment");
	// return handleProcessInstanceResults(lrt, "appaaSEnvironment",
	// appaaSEnvMH.getDSL(appaasEnv), processInstance);
	// } else {
	// AppaaS appaas = (AppaaS) wfInstance.getVariable("APPaaS");
	// return handleProcessInstanceResults(lrt, "appaaS",
	// appaaSMH.getDSL(appaas), processInstance);
	// }
	// }
	//
	// return handleProcessInstanceResults(lrt, processInstance);
	//
	// }

	@Inject
	private PaaSServiceDeploymentValidateData validateDataBean;

	public AppaaS deployAPPaaS(@Valid APPaaSDeployRequest request)
			throws Exception {

		AppaaS appaas = (AppaaS) validateDataBean
				.generateAndValidatePaaSService(request);
		appaas = appaasDAO.create(appaas);

		return appaas;
	}

	public AppaaSEnvironment deployAPPaaSEnvironment(
			@Valid APPaaSEnvironmentDeployRequest request) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();

		DeploymentMessage depMsg = buildDeploymentMessage(request);

		params.put(ARG_IN_DEPLOYMENT_MSG, depMsg);

		// Start process on jbpm runtime
		Triplet<ProcessInstance, KieSession, LRT> bpResult = bpManager
				.startProcess(DEPLOY_WF_ID, params, RUNTIME_STRATEGY.SINGLETON);
		ProcessInstance processInstance = bpResult.getValue0();
		// LRT lrt = bpResult.getValue2();

		WorkflowProcessInstance wfInstance = (WorkflowProcessInstance) processInstance;

		// Extract result from workflow
		if (wfInstance.getState() != ProcessInstance.STATE_ABORTED) {
			depMsg = (DeploymentMessage) wfInstance
					.getVariable(ARG_OUT_DEPLOYMENT_MSG);
			return handleProcessInstanceResults(
					(AppaaSEnvironment) depMsg.getPaaSService(),
					processInstance);
		}

		return handleProcessInstanceResults(null, processInstance);
	}

	public MQaaS deployMQaaS(@Valid MQaaSDeployRequest request)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();

		DeploymentMessage depMsg = buildDeploymentMessage(request);

		params.put(ARG_IN_DEPLOYMENT_MSG, depMsg);

		// Start process on jbpm runtime
		Triplet<ProcessInstance, KieSession, LRT> bpResult = bpManager
				.startProcess(DEPLOY_WF_ID, params, RUNTIME_STRATEGY.SINGLETON);
		ProcessInstance processInstance = bpResult.getValue0();
		// LRT lrt = bpResult.getValue2();

		WorkflowProcessInstance wfInstance = (WorkflowProcessInstance) processInstance;

		// Extract result from workflow
		if (wfInstance.getState() != ProcessInstance.STATE_ABORTED) {
			depMsg = (DeploymentMessage) wfInstance
					.getVariable(ARG_OUT_DEPLOYMENT_MSG);
			return handleProcessInstanceResults(
					(MQaaS) depMsg.getPaaSService(), processInstance);
		}

		return handleProcessInstanceResults(null, processInstance);
	}

	public DBaaS deployDBaaS(@Valid DBaaSDeployRequest request)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();

		DeploymentMessage depMsg = buildDeploymentMessage(request);

		params.put(ARG_IN_DEPLOYMENT_MSG, depMsg);

		// Start process on jbpm runtime
		Triplet<ProcessInstance, KieSession, LRT> bpResult = bpManager
				.startProcess(DEPLOY_WF_ID, params, RUNTIME_STRATEGY.SINGLETON);
		ProcessInstance processInstance = bpResult.getValue0();
		// LRT lrt = bpResult.getValue2();

		WorkflowProcessInstance wfInstance = (WorkflowProcessInstance) processInstance;

		// Extract result from workflow
		if (wfInstance.getState() != ProcessInstance.STATE_ABORTED) {
			depMsg = (DeploymentMessage) wfInstance
					.getVariable(ARG_OUT_DEPLOYMENT_MSG);
			return handleProcessInstanceResults(
					(DBaaS) depMsg.getPaaSService(), processInstance);
		}

		return handleProcessInstanceResults(null, processInstance);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BIaaS deployBIaaS(@Valid BIaaSDeployRequest request)
			throws Exception {

		request = (BIaaSDeployRequest) ((PaaSServiceDeploymentProvider) deploymentProviders
				.getProviderByPaaSType(PaaSServiceType.BIaaS))
				.castToProductSpecificRequest(request);

		Map<String, Object> params = new HashMap<String, Object>();

		DeploymentMessage depMsg = buildDeploymentMessage(request);

		params.put(ARG_IN_DEPLOYMENT_MSG, depMsg);

		// Start process on jbpm runtime
		Triplet<ProcessInstance, KieSession, LRT> bpResult = bpManager
				.startProcess(DEPLOY_WF_ID, params, RUNTIME_STRATEGY.SINGLETON);
		ProcessInstance processInstance = bpResult.getValue0();

		WorkflowProcessInstance wfInstance = (WorkflowProcessInstance) processInstance;

		// Extract result from workflow
		if (wfInstance.getState() != ProcessInstance.STATE_ABORTED) {
			depMsg = (DeploymentMessage) wfInstance
					.getVariable(ARG_OUT_DEPLOYMENT_MSG);
			return handleProcessInstanceResults(
					(BIaaS) depMsg.getPaaSService(), processInstance);
		}

		return handleProcessInstanceResults(null, processInstance);
	}

	@Override
	public VMaaS deployVMaaS(VMDeployRequest request) throws Exception {
		return deployGenericPaaSService(request);
	}

	@SuppressWarnings("unchecked")
	public <PAAS_TYPE extends AbstractPaaSService, DEPLOY_REQUEST_TYPE extends GenericPaaSServiceDeployRequest<?>> PAAS_TYPE deployGenericPaaSService(
			@Valid DEPLOY_REQUEST_TYPE request) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();

		DeploymentMessage depMsg = buildDeploymentMessage(request);

		params.put(ARG_IN_DEPLOYMENT_MSG, depMsg);

		// Start process on jbpm runtime
		Triplet<ProcessInstance, KieSession, LRT> bpResult = bpManager
				.startProcess(DEPLOY_WF_ID, params, RUNTIME_STRATEGY.SINGLETON);
		ProcessInstance processInstance = bpResult.getValue0();

		WorkflowProcessInstance wfInstance = (WorkflowProcessInstance) processInstance;

		// Extract result from workflow
		if (wfInstance.getState() != ProcessInstance.STATE_ABORTED) {
			depMsg = (DeploymentMessage) wfInstance
					.getVariable(ARG_OUT_DEPLOYMENT_MSG);
			return handleProcessInstanceResults(
					(PAAS_TYPE) depMsg.getPaaSService(), processInstance);
		}

		return handleProcessInstanceResults(null, processInstance);
	}

	protected <T> T handleProcessInstanceResults(T result,
			ProcessInstance processInstance) {
		WorkflowProcessInstance wfInstance = (WorkflowProcessInstance) processInstance;

		// Error occurred (in sync WF) ?
		if (wfInstance.getState() == ProcessInstance.STATE_ABORTED) {

			// Get the error and send as response (PrismaAPI Error)
			try {
				Error wfError = (Error) wfInstance.getVariable("Error");

				// TODO: IMPORTANT ! Use other exceptions
				throw new PrismaWrapperException().error(wfError);
			} catch (PrismaWrapperException pwe) {
				throw pwe;
			} catch (Exception e) {
				e.printStackTrace();
			}

			// If unable to get the WF error
			throw new PrismaWrapperException(wfInstance.getOutcome());

		} else {
			return result;
		}
	}

	// protected void checkAPPaaSPreconditions(@Valid APPaaSDeployRequest
	// request) {
	// try {
	// // APP Env ? -> APP exists ?
	// if (request.getEnvironmentParams() != null) {
	// appaasMgmtBean.checkAPPaaSExists(Long.parseLong(request
	// .getApplicationParams().getAppId()));
	//
	// // APP Env duplicated ?
	// appaasMgmtBean.checkAPPaaSEnvironmentDuplicated(request
	// .getWorkgroupId(), request.getEnvironmentParams()
	// .getEnvName());
	//
	// // DNS name validation
	// request.getEnvironmentParams().setEnvPublicEndpoint(
	// request.getEnvironmentParams().getEnvPublicEndpoint()
	// .toLowerCase());
	//
	// // DNS duplicated ?
	// if (paasSvcDAO.findByDomainName(request.getEnvironmentParams()
	// .getEnvPublicEndpoint()) != null)
	// throw new DuplicatedResourceException("Domain name "
	// + request.getEnvironmentParams()
	// .getEnvPublicEndpoint());
	// } else {
	// appaasMgmtBean.checkAPPaaSDuplicated(request.getWorkgroupId(),
	// request.getApplicationParams().getAppName());
	// }
	// } catch (Exception e) {
	// throw new BadRequestException(e);
	// }
	// }

	protected DeploymentMessage buildDeploymentMessage(
			GenericPaaSServiceDeployRequest<?> deployRequest) {
		// TODO: Lookup data
		UserAccount account = userDAO.findById(deployRequest.getAccount()
				.getUserId());
		Workgroup workgroup = workgroupDAO.findById(deployRequest.getAccount()
				.getWorkgroupId());

		InfrastructureData infrastructureData = buildInfrastructureData(
				deployRequest.getPaaSServiceType(), workgroup);
		DeployerDeploymentData deploymentData = buildDeploymentData(deployRequest);

		return DeploymentMessageBuilder.deploymentMessage()
				.withUserAccount(account).withWorkgroup(workgroup)
				.withDeployRequest(deployRequest)
				.withInfrastructureData(infrastructureData)
				.withDeployerType(infrastructureData.getType())
				.withDeploymentData(deploymentData).build();
	}

	protected IaaSTenant getIaaSTenant(PrismaZone zone, Long workgroupId) {
		return infrastructureBean.getIaaSTenantForWorkgroup(workgroupId, true);
	}

	protected DeployerEndpoint getDeployerEndpoint(
			DeployerType infrastructureType, PrismaZone zone, Long workgroupId) {
		// TODO: Migliorare con struttura dati apposita per EndPoint a seconda
		// del tipo !

		switch (infrastructureType) {
		case HEAT:

			// TODO: MA POI HEAT E' SOLO DI OPENSTACK !!
			infrastructureType = DeployerType.HEAT;

			return infrastructureBean.getOpenStackEndpointForWorkgroup(
					workgroupId, true);

		case CLOUDIFY:
			infrastructureType = DeployerType.CLOUDIFY;

			return infrastructureBean.getCloudifyEndpointForWorkgroup(
					workgroupId, true);
		}
		throw new IllegalArgumentException("Infrastructure "
				+ infrastructureType + " not valid.");
	}

	@Override
	public InfrastructureData buildInfrastructureData(
			it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType paaSServiceType,
			Workgroup workgroup) {

		// TODO: Improve data lookup

		DeployerEndpoint deployerEP;
		DeployerType infrastructureType;
		// Deployer = Heat
		if (paaSServiceType == it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType.DBaaS
				|| paaSServiceType == it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType.BIaaS
				|| paaSServiceType == it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType.VMaaS) {

			infrastructureType = DeployerType.HEAT;
		} else // Deployer = Cloudify
		{
			infrastructureType = DeployerType.CLOUDIFY;
		}

		// TODO: Generate InfrastructureData

		PrismaZone zone = PrismaZoneBuilder.prismaZone()
				.withName("Dummy-Default-Zone").build();

		// Deployer data (Cloudify, Heat, etc)
		deployerEP = getDeployerEndpoint(infrastructureType, zone,
				workgroup.getId());

		// Infrastructure data - i.e. IaaSTenant
		IaaSTenant iaasTenant = getIaaSTenant(zone, workgroup.getId());

		MailEndpoint mailEP = MailEndpointBuilder.mailEndpoint().build();// .withEmailDomain(aValue)
		MonitoringEndpoint monitoringEP = MonitoringEndpointBuilder
				.monitoringEndpoint()
				.withUrl(
						envConfig
								.getSvcEndpointProperty(EnvironmentConfig.SVCEP_MONITORING_PILLAR_URL))
				.withAdapter(
						envConfig
								.getSvcEndpointProperty(EnvironmentConfig.SVCEP_MONITORING_ADAPTER_TYPE))
				.build();

		return InfrastructureDataBuilder.infrastructureData()
				.withIaasTenant(iaasTenant).withDeployerEndpoint(deployerEP)
				.withMailEndpoint(mailEP).withMonitoringEndpoint(monitoringEP)
				.withType(infrastructureType).withZone(zone).build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected DeployerDeploymentData buildDeploymentData(
			GenericPaaSServiceDeployRequest<?> deployRequest) {

		// Deployer = Heat
		if (deployRequest instanceof DBaaSDeployRequest
				|| deployRequest instanceof BIaaSDeployRequest
				|| deployRequest instanceof VMDeployRequest) {
			String templateBasePath = envConfig
					.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_URL)
					+ envConfig
							.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_BASE_PAAS);
			String template = "";
			// String templateURL = "";

			if (deployRequest instanceof DBaaSDeployRequest) {
				switch (EnumUtils.fromString(DBProductType.class,
						((DBaaSDeployRequest) deployRequest).getDBaasDetails()
								.getProductType())) {

				case MySQL:
					template = envConfig
							.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_DBAAS_MYSQL);
					break;

				case PostgreSQL:
					template = envConfig
							.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_DBAAS_POSTGRE);
					break;

				default:
					throw new IllegalArgumentException(
							"Product type not supported "
									+ ((DBaaSDeployRequest) deployRequest)
											.getDBaasDetails().getProductType());
				}

			} else if (deployRequest instanceof BIaaSDeployRequest) {
				// TODO: Improve when all services are using
				return (((HeatPaaSServiceDeploymentProvider) deploymentProviders
						.getProviderByPaaSType(PaaSServiceType.BIaaS))
						.getTemplateData(deployRequest));

				// BIaaSDeployRequest biRequest = (BIaaSDeployRequest)
				// deployRequest;
				// switch (EnumUtils.fromString(BIProductType.class, biRequest
				// .getBIaasDetails().getProductType())) {
				//
				// case Pentaho:
				// switch (EnumUtils.fromString(
				// BIPentahoConfigurationVariant.class, biRequest
				// .getBIaasDetails()
				// .getConfigurationVariant())) {
				// case Console_only:
				// template = envConfig
				// .getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_BIAAS_PENTAHO_CONSOLE_ONLY);
				// break;
				// case Server_only:
				// template = envConfig
				// .getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_BIAAS_PENTAHO_SERVER_ONLY);
				// break;
				// case Server_w_Console:
				// template = envConfig
				// .getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_BIAAS_PENTAHO_SERVER_W_CONSOLE);
				// break;
				// default:
				// throw new IllegalArgumentException(
				// "Configuration variant not supported "
				// + (biRequest).getBIaasDetails()
				// .getConfigurationVariant());
				// }
				// break;
				//
				// default:
				// throw new IllegalArgumentException(
				// "Product type not supported "
				// + biRequest.getBIaasDetails()
				// .getProductType());
				// }
			} else if (deployRequest instanceof VMDeployRequest) {
				// TODO: Improve when all services are using
				return (((HeatPaaSServiceDeploymentProvider) deploymentProviders
						.getProviderByPaaSType(PaaSServiceType.VMaaS))
						.getTemplateData(deployRequest));
			}
			// TODO: build HeatDeploymentData
			return HeatDeploymentDataBuilder.heatDeploymentData()
					.withTemplateURL(templateBasePath + "/" + template)
					.withTemplateName(template).build();
		} else // Deployer = Cloudify
		{
			String recipeName = "";
			String recipePropertiesFile = "";

			if (deployRequest instanceof MQaaSDeployRequest) {
				// TODO add other server recipes

				recipeName = "rabbit_service_LB.zip";
				recipePropertiesFile = "rabbitmq/rabbitmq-service.properties";

			} else if (deployRequest instanceof APPaaSEnvironmentDeployRequest) {

				// TODO add other server recipes
				switch (((APPaaSEnvironmentDeployRequest) deployRequest)
						.getEnvironmentDetails().getServerType()) {
				case "Tomcat":
					recipeName = "servizio_tomcat.zip";
					recipePropertiesFile = "tomcat/tomcat-service.properties";
					break;
				default:
					recipeName = "servizio_tomcat.zip";
					recipePropertiesFile = "tomcat/tomcat-service.properties";
				}

			}
			// TODO: Add rest

			String recipePath = PathHelper
					.getResourcesPath(ResourceType.CLOUDIFY_RECIPES)
					+ recipeName;

			return CloudifyDeploymentDataBuilder.cloudifyDeploymentData()
					.withRecipeURL(recipePath)
					.withRecipePropertiesFile(recipePropertiesFile).build();
		}

		// throw new IllegalArgumentException(
		// "Unable to build DeployerDeploymentData for service "
		// + deployRequest);
	}

	private void undeployGenericPaaSService(
			GenericPaaSServiceRepresentation paaSServiceRepresentation,
			boolean sendEmail) throws Exception {
		UndeploymentMessage undepMsg = new UndeploymentMessage();
		undepMsg.setPaaSServiceRepresentation(paaSServiceRepresentation);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ARG_IN_UNDEPLOYMENT_MSG, undepMsg);

		// TODO metterlo nel WF ////////////////////////////////////
		params.put("UndeployServiceWithError", true);
		// ////////////////////////////////////////

		params.put("SendEmail", sendEmail);

		// Start process on jbpm runtime
		Triplet<ProcessInstance, KieSession, LRT> bpResult = bpManager
				.startProcess(UNDEPLOY_WF_ID, params,
						RUNTIME_STRATEGY.SINGLETON);
		ProcessInstance processInstance = bpResult.getValue0();

		// Extract result from workflow
		handleProcessInstanceResults(null, processInstance);
		return;
	}

	@Override
	public void undeployAPPaaSEnvironment(Long userAccountId, Long workgroupId,
			Long appId, Long envId) throws Exception {

		APPaaSEnvironmentRepresentation aPPaaSEnvironmentRepresentation = new APPaaSEnvironmentRepresentation();
		aPPaaSEnvironmentRepresentation.setUserId(userAccountId);
		aPPaaSEnvironmentRepresentation.setWorkgroupId(workgroupId);
		aPPaaSEnvironmentRepresentation.setAppaasId(appId);
		aPPaaSEnvironmentRepresentation.setId(envId);

		undeployGenericPaaSService(aPPaaSEnvironmentRepresentation, true);

	}

	@Override
	public void undeployVMaaS(Long userAccountId, Long workgroupId,
			Long resourceId) throws Exception {
		VMRepresentation vMRepresentation = new VMRepresentation();
		vMRepresentation.setUserId(userAccountId);
		vMRepresentation.setWorkgroupId(workgroupId);
		vMRepresentation.setId(resourceId);

		undeployGenericPaaSService(vMRepresentation, true);

	}

	@Override
	public void undeployDBaaS(Long userAccountId, Long workgroupId,
			Long resourceId) throws Exception {
		DBaaSRepresentation dBaaSRepresentation = new DBaaSRepresentation();
		dBaaSRepresentation.setUserId(userAccountId);
		dBaaSRepresentation.setWorkgroupId(workgroupId);
		dBaaSRepresentation.setId(resourceId);

		undeployGenericPaaSService(dBaaSRepresentation, true);
	}

	@Override
	public void undeployMQaaS(Long userAccountId, Long workgroupId,
			Long resourceId) throws Exception {
		MQaaSRepresentation mQaaSRepresentation = new MQaaSRepresentation();
		mQaaSRepresentation.setUserId(userAccountId);
		mQaaSRepresentation.setWorkgroupId(workgroupId);
		mQaaSRepresentation.setId(resourceId);

		undeployGenericPaaSService(mQaaSRepresentation, true);
	}

	@Override
	public void undeployBIaaS(Long userAccountId, Long workgroupId,
			Long resourceId) throws Exception {
		BIaaSRepresentation bIaaSRepresentation = new BIaaSRepresentation();
		bIaaSRepresentation.setUserId(userAccountId);
		bIaaSRepresentation.setWorkgroupId(workgroupId);
		bIaaSRepresentation.setId(resourceId);

		undeployGenericPaaSService(bIaaSRepresentation, true);
	}
}
