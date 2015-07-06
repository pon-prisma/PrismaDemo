package it.prisma.businesslayer.bizlib.iaas.services.vm;

import javax.ejb.Stateless;

@Stateless
// @TransactionManagement(value = TransactionManagementType.BEAN)
public class VMAsyncMgmtBean {

//	protected static Logger LOG = LogManager.getLogger(VMAsyncMgmtBean.class);
//
//	@Resource
//	private UserTransaction userTransaction;
//
//	@Inject
//	private PaaSServiceDAO paasServiceDAO;
//	
//	@Inject
//	PaaSServiceEventDAO paaSSvcEvtDAO;
//
//	@Inject
//	private PaaSServiceHelper paasSvcHelper;
//
//	@Inject
//	private EnvironmentConfig envConfig;
//
//	@Inject
//	private MonitoringConfig monitConf;
//
//	@Inject
//	protected MailBean mailBean;
//
//	@Inject
//	protected VMaaSDAO vmDAO;
//
//	@Inject
//	protected PaaSDeploymentDAO paasDeploymentDAO;
//
//	@Inject
//	protected PaaSDeploymentServiceDAO paasDeploymentSvcDAO;
//
//	@Inject
//	protected PaaSDeploymentServiceInstanceDAO paasDeploymentSvcInstDAO;
//
//	@Inject
//	protected MonitoringCreation monitoringBean;
//
//	@Inject
//	protected DNSMgmtBean dnsBean;
//
//	public static final long singleDeployOperationTimeout = 30 * 60 * 1000;
//	private OSClient os;
//
//	// TODO Remove async with Java, just for test
//	@Asynchronous
//	@TransactionTimeout(unit = TimeUnit.HOURS, value = 1)
//	// @TransactionAttribute(TransactionAttributeType.NEVER)
//	public void provisioningVMaaSAsync(VMDeployRequest request, Long vmaasId,
//			String identityURL, String tenant, String username, String pwd) {
//
//		VMaaS vm = vmDAO.findById(vmaasId);
//		// TODO non spostare perché la transazione viene chiusa dal
//		// paasSvcHelper.logPaaSSvcEvent
//		String netID = vm.getIaaSNetworks().iterator().next().getOpenstackId();
//
//		PaaSService paasSvc = vm.getPaaSService();
//
//		paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
//				DeployingPrismaEvents.DEPLOY_IN_PROGRESS));
//
//		paasSvc.setOperation(PaaSService.Operation.DEPLOY.toString());
//		paasServiceDAO.update(paasSvc);
//
//		Stack stackCreate = null;
//		Stack stackPoller = null;
//
//		try {
//			try {
//				paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
//						DeployingPrismaEvents.DEPLOY_RESOURCE_PROVISIONING));
//
//				os = OSFactory.builder().endpoint(identityURL + "/v2.0/")
//						.credentials(username, pwd).tenantName(tenant)
//						.authenticate();
//
//				final String stackName = "stack_"
//						+ vm.getPaaSService().getWorkgroup().getId() + "_"
//						+ vm.getPaaSService().getUserAccount().getId() + "_"
//						+ vm.getPaaSService().getName();
//
//				String template = "";
//				if (request.getVmDetails().getVolume() == 0) {
//					template = "vm.template";
//				} else {
//					template = "vm_volume.template";
//				}
//
//				Map<String, String> parameters = new HashMap<String, String>();
//				parameters.put("key_name", request.getVmDetails().getKey());
//				parameters.put("vm_name", "vm_" + stackName);
//
//				String image = request.getVmDetails().getImageName();
//
//				if (envConfig.getEnvProfile().equals(
//						EnvironmentNames.SIELTE.toString().toLowerCase())) {
//					parameters.put("image_id",
//							envConfig.getTestbed().get("SIELTE").getImages()
//									.get(image));
//				} else {
//					parameters.put(
//							"image_id",
//							envConfig.getTestbed().get("INFN").getImages()
//									.get(image));
//				}
//
//				parameters.put("instance_type", paasSvc.getIaaSFlavor() + "."
//						+ paasSvc.getQoS());
//
//				parameters.put("net_id", netID);
//
//				stackCreate = os
//						.heat()
//						.stacks()
//						.create(Builders
//								.stack()
//								.name(stackName)
//								.templateURL(
//										envConfig
//												.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_URL)
//												+ "templates/iaas/vm/"
//												+ template).timeoutMins(20L)
//								.parameters(parameters).build());
//
//				final String stackUUID = stackCreate.getId();
//
//				// Check Heat stack status
//				AbstractPollingBehaviour<Map<String, Object>, Stack> pollB = new AbstractPollingBehaviour<Map<String, Object>, Stack>() {
//
//					protected Logger LOG = LogManager
//							.getLogger("[OpenStack-poll-behavior]");
//
//					@Override
//					public boolean pollExit(Stack pollResult) {
//						return !pollResult.getStatus().equals(
//								"CREATE_IN_PROGRESS");
//					}
//
//					@Override
//					public Stack doPolling(Map<String, Object> params)
//							throws PollingException {
//						try {
//							return os.heat().stacks()
//									.getDetails(stackName, stackUUID);
//							// return client.showStackDetails(url);
//						} catch (Exception e) {
//							LOG.error(
//									"Error during polling: " + e.getMessage(),
//									e);
//							throw new PollingException(e);
//						}
//					}
//
//					@Override
//					public boolean pollSuccessful(Map<String, Object> params,
//							Stack pollResult) {
//
//						return pollResult.getStatus().equals("CREATE_COMPLETE");
//					}
//
//				};
//
//				pollB.setTimeoutThreshold(singleDeployOperationTimeout);
//				Poller<Map<String, Object>, Stack> heatStackStatusPoller = new Poller<Map<String, Object>, Stack>(
//						pollB, 5000, 10000, 5);
//
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("client", os.heat());
//				params.put("url", stackCreate.getLinks().get(0));
//
//				heatStackStatusPoller.setLogEnabled(true);
//				stackPoller = heatStackStatusPoller.doPoll(params);
//
//			} catch (Exception e) {
//
//				LOG.error("Openstack creation error.", e);
//
//				String errorMsg = "Problem deploying the service to OpenStack";
//				if (e instanceof PollingException) {
//					PollingException pe = (PollingException) e;
//					if (pe.getCause() instanceof TimeoutException) {
//						errorMsg = "Timeout occurred";
//					}
//				}
//
//				paasSvcHelper
//						.logPaaSSvcEvent(
//								paasSvc,
//								new PrismaEvent(
//										DeployingPrismaEvents.DEPLOY_RESOURCE_PROVISIONING_ERROR,
//										errorMsg), e);
//
//				throw new IaaSDeploymentServiceException("Openstack",
//						"Openstack deployment failed.", e);
//			}
//
//			paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
//					DeployingPrismaEvents.DEPLOY_ITEM_CREATED, "Virtual",
//					"Machine"));
//
//			paasSvcHelper
//					.logPaaSSvcEvent(
//							paasSvc,
//							new PrismaEvent(
//									DeployingPrismaEvents.DEPLOY_RESOURCE_PROVISIONING_SUCCESSFUL));
//
//			// Stack creation complete -> Get VMs Info, save on DB,
//			// check monitoring
//			afterStackCreationComplete(vm, stackPoller);
//
//		} catch (Exception e) {
//			LOG.error("Deploy error occurred.", e);
//
//			String errorMsg = null;
//			if (e instanceof IaaSDeploymentServiceException)
//				errorMsg = "Failed to deploy the service to "
//						+ ((IaaSDeploymentServiceException) e).getIaasName()
//						+ " platform";
//			else if (e instanceof DNSServiceException)
//				errorMsg = "Failed to bind the service to the DNS";
//			else if (e instanceof MonitoringServiceException)
//				errorMsg = "Failed to register the service to the monitoring platform";
//			else if (e instanceof ServiceConfigurationException)
//				errorMsg = "Failed to configure the service";
//
//			// TODO Controllare le cause
//			paasSvcHelper.errorOccurred(paasSvc,
//					DeployingPrismaEvents.DEPLOY_ENDED_WITH_ERROR,
//					e.getMessage(), errorMsg, e);
//		}
//
//		return;
//	}
//
//	protected void afterStackCreationComplete(AbstractPaaSService paasService,
//			Stack stackStatusDetailsResp) throws Exception {
//
//		PaaSService paasSvc = paasService.getPaaSService();
//		// Get VMs Info & save on DB
//		// Map<String, Output> outputs = Output.listToMap(stackStatusDetailsResp
//		// .getStack().getOutputs());
//		//
//		// String vmPublicIP = outputs.get("instance_ip").getOutputValue();
//		// String vmPrivateIP = vmPublicIP;
//		// String vmName = "VM NAME";
//		// String vmUUID = outputs.get("instance_id").getOutputValue();
//
//		String vmPublicIP = "";
//		String vmName = "VM NAME";
//		String vmUUID = "";
//		String monitoringServiceCategory = paasSvc.getType();
//		/*
//		 * ToDo: put services Categories into Enum or get em from DB
//		 */
//		for (Map<String, Object> map : stackStatusDetailsResp.getOutputs()) {
//			Iterator it = map.entrySet().iterator();
//			String output_value = null;
//			String output_key = null;
//			while (it.hasNext()) {
//				Map.Entry pairs = (Map.Entry) it.next();
//				if (pairs.getKey().equals("output_value")) {
//					output_value = pairs.getValue().toString();
//				}
//				if (pairs.getKey().equals("output_key")) {
//					output_key = pairs.getValue().toString();
//				}
//				if (output_key != null && output_key.equals("instance_id")) {
//					vmUUID = output_value;
//				}
//				if (output_key != null && output_key.equals("instance_ip")) {
//					vmPublicIP = output_value;
//				}
//
//				it.remove(); // avoids a ConcurrentModificationException
//			}
//			if (map.containsKey("instance_id")) {
//				vmUUID = map.get("instance_id").toString();
//			}
//			if (map.containsKey("instance_ip")) {
//				vmPublicIP = map.get("instance_ip").toString();
//			}
//		}
//		String vmPrivateIP = vmPublicIP;
//
//		// resultOccurred(paasSvc, "CREATE COMPLETE. VM_IP: " + vmPublicIP);
//
//		// TODO Trim fino a tenant
//		// String stackReference = stackStatusDetailsResp.getStack().getLinks()
//		// .get(0).getHref();
//		String stackReference = stackStatusDetailsResp.getLinks().get(0)
//				.getHref();
//		PaaSDeployment paasDeployment = new PaaSDeployment("HEAT");
//		paasDeployment.setName(stackReference);
//
//		paasDeploymentDAO.create(paasDeployment);
//
//		paasSvc.setPaaSDeployment(paasDeployment);
//		paasServiceDAO.update(paasSvc);
//
//		// TODO Generalize: Foreach Service (only mysql in this case)
//		PaaSDeploymentService paasDS = new PaaSDeploymentService(
//				paasDeployment, AtomicServiceType.VM_LINUX.toString(),
//				AtomicServiceType.VM_LINUX.toString());
//		paasDS.setStatus("UNKNOWN");
//
//		paasDeploymentSvcDAO.create(paasDS);
//
//		// TODO Generalize: Foreach VM of the service (only one in this case)
//		PaaSDeploymentServiceInstance paasDSI = new PaaSDeploymentServiceInstance(
//				paasDS, vmPublicIP, vmPrivateIP, "UNKNOWN", vmName, vmUUID);
//
//		paasDeploymentSvcInstDAO.create(paasDSI);
//
//		// Update DNS
//		String serviceDN = paasSvc.getDomainName();
//		String serviceIP = vmPublicIP;
//		try {
//			dnsBean.registerDN(serviceDN, serviceIP);
//
//			paasSvcHelper
//					.logPaaSSvcEvent(
//							paasSvc,
//							new PrismaEvent(
//									DeployingPrismaEvents.DEPLOY_POST_DEPLOY_DNS_ENTRY_SUCCESSFUL,
//									serviceDN + "/" + serviceIP));
//
//		} catch (Exception ex) {
//			paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
//					DeployingPrismaEvents.DEPLOY_POST_DEPLOY_DNS_ENTRY_ERROR),
//					ex);
//			throw new DNSServiceException("Cannot add DNS record (" + serviceDN
//					+ "/" + serviceIP + ")", ex);
//		}
//
//		try {
//			
//			if(Boolean.parseBoolean(envConfig.getAppProperty(EnvironmentConfig.APP_TEST_SKIP_POST_DEPLOY_MONITORING_ADD))){
//				PaaSServiceEvent paaSSvcEvt = new PaaSServiceEvent(
//						paasSvc, EventType.WARNING.name(),
//						"Skipping monitoring");
//				
//				paaSSvcEvtDAO.create(paaSSvcEvt);
//
//			} else {
//			
//				paasSvcHelper
//						.logPaaSSvcEvent(
//								paasSvc,
//								new PrismaEvent(
//										DeployingPrismaEvents.DEPLOY_POST_DEPLOY_ADDING_MONITORING));
//	
//				// TODO: ADD MONITORING
//				List<String> monitorigServices = new ArrayList<String>();
//				monitorigServices.add(envConfig.monitoringConfig()
//						.getAtomicServiceByType(AtomicServiceType.VM_LINUX));
//	
//				String hostGroup = envConfig.monitoringConfig()
//						.getHostGroupByPaaSServiceType(paasSvc.getType());
//				// Add host to monitoring
//				new MonitoringCreation.AddMonitoredVMRetryCommand().init(
//						monitoringBean, "infn", vmName, vmUUID, vmPublicIP,
//						hostGroup, paasSvc.getId().toString(), monitorigServices)
//						.run();
//				final String prismaId = paasSvc.getId().toString();
//				// monitoringBean.addMonitoredVM("infn", vmName, vmUUID, vmPublicIP,
//				// hostGroup, paasSvc.getId().toString(), monitorigServices);
//	
//				// Polling to monitoring - services ended
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("monitoringBean", monitoringBean);
//				params.put("vmUUID", vmUUID);
//				// params.put("services", services);
//	
//				// TODO improve
//				AbstractPollingBehaviour<Map<String, Object>, String> monPollB = new AbstractPollingBehaviour<Map<String, Object>, String>() {
//	
//					@Override
//					public boolean pollExit(String pollResult) {
//						return pollResult.equals("true");
//					}
//	
//					@Override
//					public String doPolling(Map<String, Object> params)
//							throws PollingException {
//						try {
//							MonitoringCreation monitoringBean = (MonitoringCreation) params
//									.get("monitoringBean");
//							String vmUUID = (String) params.get("vmUUID");
//	
//							@SuppressWarnings("unchecked")
//							ArrayList<String> services = (ArrayList<String>) params
//									.get("services");
//	
//							Boolean response = monitoringBean
//									.getServiceRunningStatus(
//											monitConf.getINFNTestbed(),
//											monitConf.getVMaaSServiceCategory(),
//											prismaId);
//							return response.toString();
//						} catch (Exception e) {
//							throw new PollingException(e);
//						}
//					}
//	
//					@Override
//					public boolean pollSuccessful(Map<String, Object> params,
//							String pollResult) {
//						return !pollResult.contains("false");
//					}
//				};
//	
//				monPollB.setTimeoutThreshold(singleDeployOperationTimeout);
//				Poller<Map<String, Object>, String> monPoller = new Poller<Map<String, Object>, String>(
//						monPollB, 30000, 30000, 10);
//	
//				monPoller.setLogEnabled(true);
//				monPoller.doPoll(params);
//	
//				paasSvcHelper
//						.logPaaSSvcEvent(
//								paasSvc,
//								new PrismaEvent(
//										DeployingPrismaEvents.DEPLOY_POST_DEPLOY_MONITORING_SUCCESSFUL));
//			}
//		} catch (Exception ex) {
//			paasSvcHelper.logPaaSSvcEvent(paasSvc, new PrismaEvent(
//					DeployingPrismaEvents.DEPLOY_POST_DEPLOY_MONITORING_ERROR),
//					ex);
//
//			throw new Exception("Monitoring VM error.", ex);
//		}
//
//		// Send Email
//		try {
//			mailBean.sendPaaSDeploySuccesfullEmail(paasService);
//		} catch (Exception ex) {
//			paasSvcHelper.logPaaSSvcEvent(paasSvc, EventType.WARNING,
//					"Unable to send notification e-mail.", ex);
//		}
//
//		paasSvcHelper.resultOccurred(paasSvc,
//				DeployingPrismaEvents.DEPLOY_ENDED_SUCCESSFULLY,
//				PaaSService.Status.RUNNING,
//				"Deploy ended succesfully ! Service is ready to use.");
//
//		// TODO Remove once removed PaasSvcHelper transaction trick
//		try {
//			userTransaction.commit();
//		} catch (Exception e) {
//		}
//		try {
//			userTransaction.begin();
//		} catch (Exception e) {
//		}
//	}

}