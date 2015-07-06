package it.prisma.businesslayer.bizlib.paas.services.dbaas;

import it.prisma.businesslayer.bizlib.common.exceptions.BadRequestException;
import it.prisma.businesslayer.bizlib.dns.DNSMgmtBean;
import it.prisma.businesslayer.bizlib.mail.MailBean;
import it.prisma.businesslayer.bizlib.paas.services.PaaSServiceHelper;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.config.EnvironmentConfig.EnvironmentNames;
import it.prisma.dal.dao.config.PlatformConfigurationDAO;
import it.prisma.dal.dao.paas.deployments.PaaSDeploymentDAO;
import it.prisma.dal.dao.paas.deployments.PaaSDeploymentServiceDAO;
import it.prisma.dal.dao.paas.deployments.PaaSDeploymentServiceInstanceDAO;
import it.prisma.dal.dao.paas.services.PaaSServiceDAO;
import it.prisma.dal.dao.paas.services.dbaas.DBaaSDAO;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.OSClient;

@Stateless
// @TransactionManagement(value = TransactionManagementType.BEAN)
public class DBaaSAsyncMgmtBean {

	protected static Logger LOG = LogManager
			.getLogger(DBaaSAsyncMgmtBean.class);

	@Resource
	private UserTransaction userTransaction;

	@Inject
	private PaaSServiceDAO paasServiceDAO;

	@Inject
	private PaaSServiceHelper paasSvcHelper;

	@Inject
	private EnvironmentConfig envConfig;

	@Inject
	protected MailBean mailBean;

	@Inject
	protected DBaaSDAO dbaasDAO;

	@Inject
	protected PlatformConfigurationDAO platCfgDAO;

	@Inject
	protected PaaSDeploymentDAO paasDeploymentDAO;

	@Inject
	protected PaaSDeploymentServiceDAO paasDeploymentSvcDAO;

	@Inject
	protected PaaSDeploymentServiceInstanceDAO paasDeploymentSvcInstDAO;

	@Inject
	protected DNSMgmtBean dnsBean;

	public static final long singleDeployOperationTimeout = 45 * 60 * 1000;

	private OSClient os;

	// // TODO Remove async with Java, just for test
	// @Asynchronous
	// @TransactionTimeout(unit = TimeUnit.HOURS, value = 1)
	// // @TransactionAttribute(TransactionAttributeType.NEVER)
	// public void provisioningDBaaSAsync(DBaaSDeployRequest request,
	// Long dbaasId, String identityURL, String tenant, String username,
	// String pwd) {
	//
	// DBaaS dbaas = dbaasDAO.findById(dbaasId);
	// PaaSService paasSvc = dbaas.getPaaSService();
	//
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_IN_PROGRESS));
	//
	// // Set ongoing requested operation
	// paasSvc.setOperation(PaaSService.Operation.DEPLOY.toString());
	// paasServiceDAO.update(paasSvc);
	//
	// Stack stackCreate = null;
	// Stack stackPoller = null;
	// try {
	// try {
	//
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_RESOURCE_PROVISIONING));
	//
	// os = OSFactory.builder().endpoint(identityURL + "/v2.0/")
	// .credentials(username, pwd).tenantName(tenant)
	// .authenticate();
	//
	// final String stackName = "dbaas_"
	// + dbaas.getPaaSService().getWorkgroup().getId() + "_"
	// + dbaas.getPaaSService().getUserAccount().getId() + "_"
	// + dbaas.getId();
	//
	//
	// // Find the template
	// String templateURL = findTemplateURL(request.getDBaasDetails()
	// .getProductType());
	//
	// stackCreate = os
	// .heat()
	// .stacks()
	// .create(Builders.stack().name(stackName)
	// .templateURL(templateURL)
	// .parameters(fillTemplateParameters(request, paasSvc)).build());
	//
	// final String stackUUID = stackCreate.getId();
	//
	// // Check Heat stack status
	// AbstractPollingBehaviour<Map<String, Object>, Stack> pollB = new
	// AbstractPollingBehaviour<Map<String, Object>, Stack>() {
	//
	// protected Logger LOG = LogManager
	// .getLogger("[OpenStack-poll-behavior]");
	//
	// @Override
	// public boolean pollExit(Stack pollResult) {
	// return !pollResult.getStatus().equals(
	// "CREATE_IN_PROGRESS");
	// }
	//
	// @Override
	// public Stack doPolling(Map<String, Object> params)
	// throws PollingException {
	// try {
	//
	// return os.heat().stacks()
	// .getDetails(stackName, stackUUID);
	// // return client.showStackDetails(url);
	// } catch (Exception e) {
	// LOG.error(
	// "Error during polling: " + e.getMessage(),
	// e);
	// throw new PollingException(e);
	// }
	// }
	//
	// @Override
	// public boolean pollSuccessful(Map<String, Object> params,
	// Stack pollResult) {
	//
	// return pollResult.getStatus().equals("CREATE_COMPLETE");
	// }
	//
	// };
	//
	// pollB.setTimeoutThreshold(singleDeployOperationTimeout);
	// Poller<Map<String, Object>, Stack> heatStackStatusPoller = new
	// Poller<Map<String, Object>, Stack>(
	// pollB, 30000, 30000, 5);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("client", os.heat());
	// params.put("url", stackCreate.getLinks().get(0));
	//
	// heatStackStatusPoller.setLogEnabled(true);
	// stackPoller = heatStackStatusPoller.doPoll(params);
	//
	// } catch (Exception e) {
	//
	// LOG.error("Openstack creation error.", e);
	//
	// String errorMsg = "Problem deploying the service to OpenStack";
	// if (e instanceof PollingException) {
	// PollingException pe = (PollingException) e;
	// if (pe.getCause() instanceof TimeoutException) {
	// errorMsg = "Timeout occurred";
	// }
	// }
	//
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_RESOURCE_PROVISIONING_ERROR,
	// errorMsg), e);
	//
	// throw new IaaSDeploymentServiceException("Openstack",
	// "Openstack deployment failed.", e);
	// }
	//
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_ITEM_CREATED, "Virtual",
	// "Machine"));
	//
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_ITEM_CREATED, "Storage",
	// "Service"));
	//
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_RESOURCE_PROVISIONING_SUCCESSFUL));
	//
	// // Stack creation complete -> Get VMs Info, save on DB,
	// // check monitoring
	// afterStackCreationComplete(paasSvc, stackPoller);
	//
	// } catch (Exception e) {
	// LOG.error("Deploy error occurred.", e);
	//
	// String errorMsg = null;
	// if (e instanceof IaaSDeploymentServiceException)
	// errorMsg = "Failed to deploy the service to "
	// + ((IaaSDeploymentServiceException) e).getIaasName()
	// + " platform";
	// else if (e instanceof DNSServiceException)
	// errorMsg = "Failed to bind the service to the DNS";
	// else if (e instanceof MonitoringServiceException)
	// errorMsg = "Failed to register the service to the monitoring platform";
	// else if (e instanceof ServiceConfigurationException)
	// errorMsg = "Failed to configure the service";
	//
	// // TODO Controllare le cause
	// paasSvcHelper.errorOccurred(paasSvc,
	// DeployingPrismaEvents.DEPLOY_ENDED_WITH_ERROR,
	// e.getMessage(), errorMsg, e);
	// }
	//
	// return;
	// }

	// protected void afterStackCreationComplete(PaaSService paasSvc,
	// Stack stackStatusDetailsResp) throws Exception {
	//
	// // Get VMs Info & save on DB
	// /*
	// * Map<String, Output> outputs = Output.listToMap(stackStatusDetailsResp
	// * .getStack().getOutputs());
	// *
	// * String vmPublicIP = outputs.get("instance_ip").getOutputValue();
	// * String vmPrivateIP = vmPublicIP; String vmName = "VM NAME"; String
	// * vmUUID = outputs.get("instance_id").getOutputValue();
	// */
	//
	// String vmPublicIP = "";
	// String vmName = "VM NAME";
	// String vmUUID = "";
	//
	// String serviceCategory = paasSvc.getType();
	//
	// /*
	// * ToDo: Put service categories into a enum o retireve'em from DB
	// */
	// for (Map<String, Object> map : stackStatusDetailsResp.getOutputs()) {
	// Iterator it = map.entrySet().iterator();
	// String output_value = null;
	// String output_key = null;
	// while (it.hasNext()) {
	// Map.Entry pairs = (Map.Entry) it.next();
	// if (pairs.getKey().equals("output_value")) {
	// output_value = pairs.getValue().toString();
	// }
	// if (pairs.getKey().equals("output_key")) {
	// output_key = pairs.getValue().toString();
	// }
	// if (output_key != null && output_key.equals("instance_id")) {
	// vmUUID = output_value;
	// }
	// if (output_key != null && output_key.equals("instance_ip")) {
	// vmPublicIP = output_value;
	// }
	//
	// it.remove(); // avoids a ConcurrentModificationException
	// }
	// if (map.containsKey("instance_id")) {
	// vmUUID = map.get("instance_id").toString();
	// }
	// if (map.containsKey("instance_ip")) {
	// vmPublicIP = map.get("instance_ip").toString();
	// }
	// }
	// String vmPrivateIP = vmPublicIP;
	//
	// // resultOccurred(paasSvc, "CREATE COMPLETE. VM_IP: " + vmPublicIP);
	//
	// // TODO Trim fino a tenant
	// String stackReference = stackStatusDetailsResp.getLinks().get(0)
	// .getHref();
	// PaaSDeployment paasDeployment = new PaaSDeployment("HEAT");
	// paasDeployment.setName(stackReference);
	//
	// paasDeploymentDAO.create(paasDeployment);
	//
	// paasSvc.setPaaSDeployment(paasDeployment);
	// paasServiceDAO.update(paasSvc);
	//
	// // TODO Generalize: Foreach Service (only mysql in this case)
	// PaaSDeploymentService paasDS = new PaaSDeploymentService(
	// paasDeployment, AtomicServiceType.DB_MySQL.toString(),
	// AtomicServiceType.DB_MySQL.toString());
	// paasDS.setStatus("UNKNOWN");
	//
	// paasDeploymentSvcDAO.create(paasDS);
	//
	// // TODO Generalize: Foreach VM of the service (only one in this case)
	// PaaSDeploymentServiceInstance paasDSI = new
	// PaaSDeploymentServiceInstance(
	// paasDS, vmPublicIP, vmPrivateIP, "UNKNOWN", vmName, vmUUID);
	//
	// paasDeploymentSvcInstDAO.create(paasDSI);
	//
	// // Update DNS
	// String serviceDN = paasSvc.getDomainName();
	// String serviceIP = vmPublicIP;
	// try {
	// dnsBean.registerDN(serviceDN, serviceIP);
	//
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_POST_DEPLOY_DNS_ENTRY_SUCCESSFUL,
	// serviceDN + "/" + serviceIP));
	//
	// } catch (Exception ex) {
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_POST_DEPLOY_DNS_ENTRY_ERROR),
	// ex);
	// throw new DNSServiceException("Cannot add DNS record (" + serviceDN
	// + "/" + serviceIP + ")", ex);
	// }
	//
	// ArrayList<String> services = new ArrayList<String>();
	// try {
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_POST_DEPLOY_ADDING_MONITORING));
	//
	// String hostGroup = envConfig.monitoringConfig()
	// .getHostGroupByPaaSServiceType(paasSvc.getType());
	//
	// // Add host to monitoring
	// services.add(envConfig.monitoringConfig().getAtomicServiceByType(
	// AtomicServiceType.WS_APACHE_HTTPD));
	//
	// String dbType = dbaasDAO.findById(paasSvc.getId()).getProductType();
	// if(dbType.equalsIgnoreCase("mysql")) {
	// services.add(envConfig.monitoringConfig().getAtomicServiceByType(
	// AtomicServiceType.DB_MySQL));
	// } else if(dbType.equalsIgnoreCase("postgresql")){
	// services.add(envConfig.monitoringConfig().getAtomicServiceByType(
	// AtomicServiceType.DB_POSTGRE));
	// } else {
	// throw new
	// BadRequestException("Monitoring add error: type of database unrecognized");
	// }
	//
	//
	// monitoringBean
	// .addMonitoredVM(
	// envConfig
	// .getSvcEndpointProperty(EnvironmentConfig.MONITORING_PILLAR_ZONE),
	// vmName, vmUUID, vmPublicIP, hostGroup, paasSvc
	// .getId().toString(), services);
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_POST_DEPLOY_MONITORING_SUCCESSFUL));
	// } catch (Exception ex) {
	// // TODO: Define error messages !!
	// String errorMsg = "generic error";
	//
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_POST_DEPLOY_MONITORING_ERROR,
	// errorMsg), ex);
	//
	// throw new MonitoringServiceException(
	// "Unable to add the service to monitoring.", ex);
	// }
	//
	// try {
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_POST_DEPLOY_WAITING_FOR_SERVICE_READY));
	//
	// // Polling to monitoring - services ended
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("vmUUID", vmUUID);
	// params.put("services", services);
	// params.put("PaaSService", paasSvc);
	// params.put("MonitoringBean", monitoringBean);
	// params.put("EnvironmentConfig", envConfig);
	//
	// AbstractPollingBehaviour<Map<String, Object>, Boolean> monPollB = new
	// AbstractPollingBehaviour<Map<String, Object>, Boolean>() {
	//
	// @Override
	// public boolean pollExit(Boolean pollResult) {
	// // return !pollResult.contains("false");
	// return pollResult;//
	// pollResult.equals("{ \"mysql-services-running\" : true , \"apache-services-running\" : true }");
	// }
	//
	// @Override
	// public Boolean doPolling(Map<String, Object> params)
	// throws PollingException {
	// try {
	// PaaSService paasSvc = (PaaSService) params
	// .get("PaaSService");
	// MonitoringCreation monitoringBean = (MonitoringCreation) params
	// .get("MonitoringBean");
	// EnvironmentConfig envConfig = (EnvironmentConfig) params
	// .get("EnvironmentConfig");
	//
	// // MonitoringCreation monitoringBean =
	// // (MonitoringCreation) params
	// // .get("monitoringBean");
	// // String vmUUID = (String) params.get("vmUUID");
	// // @SuppressWarnings("unchecked")
	// // ArrayList<String> services = (ArrayList<String>)
	// // params
	// // .get("services");
	// // return (String) monitoringBean.getRunningServices(
	// // "infn", vmUUID, services);
	//
	// String paaStype = envConfig.monitoringConfig()
	// .getHostGroupByPaaSServiceType(
	// PaaSServiceType.DBaaS);
	//
	// return monitoringBean
	// .isPaaSServiceUp(
	// envConfig
	// .getSvcEndpointProperty(EnvironmentConfig.MONITORING_PILLAR_ZONE),
	// paaStype, paasSvc.getId().toString());
	//
	// } catch (Exception e) {
	// throw new PollingException(e);
	// }
	// }
	//
	// @Override
	// public boolean pollSuccessful(Map<String, Object> params,
	// Boolean pollResult) {
	// return pollResult;// !pollResult.contains("false");
	// }
	// };
	//
	// monPollB.setTimeoutThreshold(singleDeployOperationTimeout);
	// Poller<Map<String, Object>, Boolean> monPoller = new Poller<Map<String,
	// Object>, Boolean>(
	// monPollB, 2000, 2000, 5);
	//
	// monPoller.setLogEnabled(true);
	// monPoller.doPoll(params);
	//
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_POST_DEPLOY_WAITING_FOR_SERVICE_READY_SUCCESSFUL));
	// } catch (Exception e) {
	// // TODO: Define error messages !!
	// String errorMsg = "generic error";
	// if (e instanceof PollingException) {
	// PollingException pe = (PollingException) e;
	// if (pe.getCause() instanceof TimeoutException) {
	// errorMsg = "Timeout occurred";
	// }
	// }
	//
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.DEPLOY_POST_DEPLOY_WAITING_FOR_SERVICE_READY_ERROR,
	// errorMsg), e);
	// throw new ServiceConfigurationException(
	// "Service configuration failed.", e);
	// }
	//
	// // Send Email
	// try {
	// mailBean.sendPaaSDeploySuccesfullEmail(paasSvc);
	// } catch (Exception ex) {
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.WARNING,
	// "Unable to send notification e-mail.", ex);
	// }
	//
	// paasSvcHelper.resultOccurred(paasSvc,
	// DeployingPrismaEvents.DEPLOY_ENDED_SUCCESSFULLY,
	// PaaSService.Status.RUNNING, null);
	//
	// // Reset the ongoing operation
	// paasSvc.setOperation(null);
	// paasServiceDAO.update(paasSvc);
	//
	// // TODO Remove once removed PaasSvcHelper transaction trick
	// try {
	// userTransaction.commit();
	// } catch (Exception e) {
	// }
	// try {
	// userTransaction.begin();
	// } catch (Exception e) {
	// }
	// }

	// @Asynchronous
	// @TransactionTimeout(unit = TimeUnit.HOURS, value = 1)
	// public void unprovisioningDBaaSAsync(Long dbaasId, String identityURL,
	// String tenant, String username, String pwd) {
	//
	// DBaaS dbaas = dbaasDAO.findById(dbaasId);
	// PaaSService paasSvc = dbaas.getPaaSService();
	// PaaSDeployment paasDeployment = paasSvc.getPaaSDeployment();
	// paasDeployment.getName();
	//
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.UNDEPLOY_IN_PROGRESS));
	//
	// // Set ongoing requested operation
	// paasSvc.setOperation(PaaSService.Operation.UNDEPLOY.toString());
	// paasServiceDAO.update(paasSvc);
	//
	// try {
	// try {
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.UNDEPLOY_RESOURCE));
	//
	// os = OSFactory.builder().endpoint(identityURL)
	// .credentials(username, pwd).tenantName(tenant)
	// .authenticate();
	//
	// String refTrim = paasDeployment.getName();
	// String separator = "/stacks/";
	// refTrim = refTrim.substring(refTrim.indexOf("/stacks/")
	// + separator.length());
	// String[] stack_params = refTrim.split("/");
	// os.heat().stacks().delete(stack_params[0], stack_params[1]);
	//
	// } catch (Exception e) {
	// boolean skip = false;
	// if (e instanceof OpenstackAPIErrorException) {
	// if (((OpenstackAPIErrorException) e).getAPIError()
	// .getMessage().contains("could not be found"))
	// skip = true;
	// }
	//
	// if (!skip) {
	// LOG.error("Openstack creation error.", e);
	//
	// String errorMsg = "Problem deploying the service to OpenStack";
	// if (e instanceof PollingException) {
	// PollingException pe = (PollingException) e;
	// if (pe.getCause() instanceof TimeoutException) {
	// errorMsg = "Timeout occurred";
	// }
	// }
	//
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.UNDEPLOY_RESOURCE_ERROR,
	// errorMsg), e);
	//
	// throw new IaaSDeploymentServiceException("Openstack",
	// "Openstack deployment failed.", e);
	// }
	// }
	//
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.UNDEPLOY_RESOURCE_SUCCESSFUL));
	//
	// // Remove service from monitoring
	// // ArrayList<String> services = new ArrayList<String>();
	// try {
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.UNDEPLOY_REMOVING_MONITORING));
	//
	// // Check monitoring
	// // services.add("mysql");
	// // services.add("apache");
	// // TODO: Remove from monitorin platform
	// // monitoringBean.addMonitoredVM(vmName,
	// // vmUUID, vmPublicIP, services);
	//
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.UNDEPLOY_REMOVING_MONITORING_SUCCESSFUL));
	// } catch (Exception ex) {
	// // TODO: Define error messages !!
	// String errorMsg = "generic error";
	//
	// paasSvcHelper
	// .logPaaSSvcEvent(
	// paasSvc,
	// new PrismaEvent(
	// DeployingPrismaEvents.UNDEPLOY_REMOVING_MONITORING_ERROR,
	// errorMsg), ex);
	//
	// throw new MonitoringServiceException(
	// "Unable to add the service to monitoring.", ex);
	// }
	//
	// // Update DNS
	// String serviceDN = paasSvc.getDomainName();
	// try {
	// String serviceIP = InetAddress.getByName(serviceDN)
	// .getHostAddress();
	//
	// // TODO: Capire qual è lo stato dell'entry e se serve il vero IP
	// // per cancellarla... Non ha molto senso...
	// dnsBean.unregisterDN(serviceDN, serviceIP);
	//
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.UNDEPLOY_DNS_ENTRY_SUCCESSFUL,
	// serviceDN + "/" + serviceIP));
	//
	// } catch (Exception ex) {
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
	// DeployingPrismaEvents.UNDEPLOY_DNS_ENTRY_ERROR), ex);
	// throw new DNSServiceException("Cannot remove DNS record ("
	// + serviceDN + "/)", ex);
	// }
	//
	// // Send Email
	// try {
	// mailBean.sendPaaSUndeploySuccesfullEmail(dbaas);
	// } catch (Exception ex) {
	// paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.WARNING,
	// "Unable to send notification e-mail.", ex);
	// }
	//
	// paasSvcHelper.resultOccurred(paasSvc,
	// DeployingPrismaEvents.UNDEPLOY_ENDED_SUCCESSFULLY,
	// PaaSService.Status.DELETED, null);
	//
	// // Reset the ongoing operation
	// paasSvc.setOperation(null);
	// paasServiceDAO.update(paasSvc);
	//
	// // TODO Remove once removed PaasSvcHelper transaction trick
	// try {
	// userTransaction.commit();
	// } catch (Exception e) {
	// }
	// try {
	// userTransaction.begin();
	// } catch (Exception e) {
	// }
	//
	// } catch (Exception e) {
	// LOG.error("Deploy error occurred.", e);
	//
	// String errorMsg = null;
	// if (e instanceof IaaSDeploymentServiceException)
	// errorMsg = "Failed to undeploy the service from "
	// + ((IaaSDeploymentServiceException) e).getIaasName()
	// + " platform";
	// else if (e instanceof DNSServiceException)
	// errorMsg = "Failed to unbind the service from the DNS";
	// else if (e instanceof MonitoringServiceException)
	// errorMsg = "Failed to unregister the service to the monitoring platform";
	//
	// // TODO Controllare le cause
	// paasSvcHelper.errorOccurred(paasSvc,
	// DeployingPrismaEvents.UNDEPLOY_ENDED_WITH_ERROR,
	// e.getMessage(), errorMsg, e);
	// }
	// }

	private String findTemplateURL(String productType)
			throws BadRequestException {

		String url = envConfig
				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_URL)
				+ envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_BASE_PAAS);

		// TODO improve asap
		if (productType.toLowerCase().contains("mysql")) {
			return url
					+ envConfig
							.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_DBAAS_MYSQL);
		} else if (productType.toLowerCase().contains("postgre")) {
			return url
					+ envConfig
							.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_DBAAS_POSTGRE);
		} else {
			throw new BadRequestException("Cannot find template for: "
					+ productType);
		}
	}

	private Map<String, String> fillTemplateParameters(
			DBaaSDeployRequest request, PaaSService paasSvc) {
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("vm_name", "vm_" + "_"
				+ request.getAccount().getWorkgroupId() + "_"
				+ request.getAccount().getUserId() + "_" + paasSvc.getId());
		parameters.put("instance_type",
				paasSvc.getIaaSFlavor() + "." + paasSvc.getQoS());

		// TODO test on sielte
		if (envConfig.getEnvProfile().equals(
				EnvironmentNames.SIELTE.toString().toLowerCase())) {
			parameters.put("key_name", envConfig.getOpenStackPresets()
					.getPaaSKey());
			parameters.put("net_id", envConfig.getOpenStackPresets()
					.getNetworks().get("net_id"));

		} else {

			parameters.put("key_name", envConfig.getOpenStackPresets()
					.getPaaSKey());

			parameters.put("net_id", envConfig.getOpenStackPresets()
					.getNetworks().get("net_id"));
		}

		parameters.put("db_name", paasSvc.getName());
		parameters.put("db_root_password", request.getDBaasDetails()
				.getRootPassword());

		if (request.getDBaasDetails().getExtraUser() != null) {
			parameters.put("db_username", request.getDBaasDetails()
					.getExtraUser().getUsername());
			parameters.put("db_password", request.getDBaasDetails()
					.getExtraUser().getPassword());
		}

		parameters.put("volume_size", request.getDBaasDetails().getDiskSize()
				.toString());

		return parameters;
	}
}