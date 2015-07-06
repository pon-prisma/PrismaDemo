package it.prisma.businesslayer.bizlib.paas.services.biaas;

import it.prisma.businesslayer.bizlib.common.exceptions.BadRequestException;
import it.prisma.businesslayer.bizlib.orchestration.deployment.DeploymentUtils;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSDeploymentProvider;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.heat.BaseHeatDeploymentProvider;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.heat.HeatPaaSServiceDeploymentProvider;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentDataBuilder;
import it.prisma.businesslayer.utils.mappinghelpers.paas.services.biaas.BIaaSMappingHelper;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService.PaaSDeploymentServiceType;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.PaaSServiceEndpoint;
import it.prisma.dal.entities.paas.services.biaas.BIaaS;
import it.prisma.dal.entities.paas.services.biaas.pentaho.PentahoConsoleOnlyParameters;
import it.prisma.dal.entities.paas.services.biaas.pentaho.PentahoServerAndConsoleParameters;
import it.prisma.dal.entities.paas.services.biaas.pentaho.PentahoServerOnlyParameters;
import it.prisma.dal.entities.paas.services.builders.PaaSServiceEndpointBuilder;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation.BIPentahoConfigurationVariant;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation.BIProductType;
import it.prisma.domain.dsl.paas.services.biaas.pentaho.request.PentahoConsoleOnlyDeployDetails;
import it.prisma.domain.dsl.paas.services.biaas.pentaho.request.PentahoServerAndConsoleDeployDetails;
import it.prisma.domain.dsl.paas.services.biaas.pentaho.request.PentahoServerOnlyDeployDetails;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.generic.request.BaseCustomPaaSDetails;
import it.prisma.utils.misc.EnumUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.heat.Stack;

@Stateless
@PaaSDeploymentProvider
public class BIaaSDeploymentProvider extends
		BaseHeatDeploymentProvider<BIaaS, BIaaSDeployRequest> implements
		HeatPaaSServiceDeploymentProvider<BIaaS, BIaaSDeployRequest> {

	private static final Logger LOG = LogManager
			.getLogger(BIaaSDeploymentProvider.class);

	@Inject
	private EnvironmentConfig envConfig;

	@Override
	public PaaSServiceType getPaaSServiceType() {
		return PaaSServiceType.BIaaS;
	}

	@Override
	public BIaaS generateAndValidatePaaSService(
			BIaaSDeployRequest deployRequest, UserAccount user,
			Workgroup workgroup) {
		
		BIaaSDeployRequest biRequest = (BIaaSDeployRequest) deployRequest;

		BIaaS biaas = BIaaSMappingHelper.generateFromDeployRequest(biRequest,
				user, workgroup);

		switch (EnumUtils.fromString(BIProductType.class,
				biaas.getProductType())) {
		case Pentaho:
			switch (EnumUtils.fromString(BIPentahoConfigurationVariant.class,
					biaas.getConfigurationVariant())) {
			case Console_only:
				try {
					PentahoConsoleOnlyParameters pcoParams = new PentahoConsoleOnlyParameters();
					PentahoConsoleOnlyDeployDetails pcodd = (PentahoConsoleOnlyDeployDetails) deployRequest
							.getBIaasDetails().getProductSpecificDetails();

					if (!validateIP(pcodd.getBiServerIP()))
						throw new BadRequestException("Invalid server IP "
								+ pcodd.getBiServerIP());

					if (!validatePassword(pcodd.getDesignerPassword()))
						throw new BadRequestException(
								"Invalid designer password "
										+ pcodd.getDesignerPassword());

					if (!validateUsername(pcodd.getDesignerUsername()))
						throw new BadRequestException(
								"Invalid designer username "
										+ pcodd.getDesignerUsername());
					//TODO: UserPublicKey
					if (!validateSSHPublicKey(pcodd.getUserPublicKey()))
						throw new BadRequestException(
								"Invalid SSH-RSA user public key "
										+ pcodd.getUserPublicKey());

					pcoParams.setBiServerIP(pcodd.getBiServerIP());
					//pcoParams.setDesignerPassword(pcodd.getDesignerPassword());
					pcoParams.setDesignerUsername(pcodd.getDesignerUsername());
					pcoParams.setUserPublicKey(pcodd.getUserPublicKey());

					biaas.getPaaSService().setProductSpecificParameters(
							pcoParams);
				} catch (Exception e) {
					throw new BadRequestException(
							"Invalid parameters for Pentaho Console_Only: "
									+ e.getMessage(), e);
				}

				break;
			case Server_only:
				try {
					PentahoServerOnlyParameters pcoParams = new PentahoServerOnlyParameters();
					PentahoServerOnlyDeployDetails psodd = (PentahoServerOnlyDeployDetails) deployRequest
							.getBIaasDetails().getProductSpecificDetails();

					if (!validatePassword(psodd.getAdminPassword()))
						throw new BadRequestException("Invalid admin password "
								+ psodd.getAdminPassword());

					//pcoParams.setAdminPassword(psodd.getAdminPassword());

					biaas.getPaaSService().setProductSpecificParameters(
							pcoParams);
				} catch (Exception e) {
					throw new BadRequestException(
							"Invalid parameters for Pentaho Server_Only: "
									+ e.getMessage(), e);
				}

				break;
			case Server_w_Console:
				try {
					PentahoServerAndConsoleParameters pscParams = new PentahoServerAndConsoleParameters();
					PentahoServerAndConsoleDeployDetails pscdd = (PentahoServerAndConsoleDeployDetails) deployRequest
							.getBIaasDetails().getProductSpecificDetails();

					if (!validatePassword(pscdd.getAdminPassword()))
						throw new BadRequestException("Invalid admin password "
								+ pscdd.getAdminPassword());

					if (!validatePassword(pscdd.getDesignerPassword()))
						throw new BadRequestException(
								"Invalid designer password "
										+ pscdd.getDesignerPassword());

					if (!validateUsername(pscdd.getDesignerUsername()))
						throw new BadRequestException(
								"Invalid designer username "
										+ pscdd.getDesignerUsername());

					//TODO: UserPublicKey
					if (!validateSSHPublicKey(pscdd.getUserPublicKey()))
						throw new BadRequestException(
								"Invalid SSH-RSA user public key "
										+ pscdd.getUserPublicKey());
					
					//pscParams.setAdminPassword(pscdd.getAdminPassword());
					//pscParams.setDesignerPassword(pscdd.getDesignerPassword());
					pscParams.setDesignerUsername(pscdd.getDesignerUsername());
					pscParams.setUserPublicKey(pscdd.getUserPublicKey());

					biaas.getPaaSService().setProductSpecificParameters(
							pscParams);
				} catch (Exception e) {
					throw new BadRequestException(
							"Invalid parameters for Pentaho Console_Only: "
									+ e.getMessage(), e);
				}

				break;

			default:
				throw new IllegalArgumentException(
						"Configuration variant not supported "
								+ biaas.getConfigurationVariant());
			}
			break;

		default:
			throw new UnsupportedOperationException("Product type "
					+ biaas.getProductType() + " is not supported for service "
					+ biaas);
		}

		return biaas;
	}

	@Override
	public PaaSDeployment getStackServicesInformations(OSClient client,
			Stack stack, HeatDeploymentData heatDeploymentData,
			BIaaS paasService) throws Exception {

		// TODO: Improve multiple VM data retrieval
		List<Map<String, Object>> op = stack.getOutputs();

		PaaSDeployment paasDeployment = createHeatPaaSDeployment(stack);

		BIaaS biaas = (BIaaS) paasService;

		String vmPublicIP = (String) DeploymentUtils
				.lookupValueFromTemplateOutputParams(op, "instance_ip");
		String vmPrivateIP = vmPublicIP;
		String vmID = (String) DeploymentUtils
				.lookupValueFromTemplateOutputParams(op, "instance_id");
		String vmName = vmID;

		switch (EnumUtils.fromString(BIProductType.class,
				biaas.getProductType())) {
		case Pentaho:
			switch (EnumUtils.fromString(BIPentahoConfigurationVariant.class,
					biaas.getConfigurationVariant())) {
			case Console_only:
				paasDeployment = addPentahoClientOnlyServices(paasDeployment,
						vmID, vmName, vmPublicIP, vmPrivateIP);

				break;
			case Server_only:
				paasDeployment = addPentahoServerOnlyServices(paasDeployment,
						vmID, vmName, vmPublicIP, vmPrivateIP);

				break;
			case Server_w_Console:
				paasDeployment = addPentahoServerOnlyServices(paasDeployment,
						vmID, vmName, vmPublicIP, vmPrivateIP);
				paasDeployment = addPentahoClientOnlyServices(paasDeployment,
						vmID, vmName, vmPublicIP, vmPrivateIP);

				break;

			default:
				throw new IllegalArgumentException(
						"Configuration variant not supported "
								+ biaas.getConfigurationVariant());
			}

			return paasDeployment;

		default:
		}

		throw new UnsupportedOperationException(
				"Cannot extract Heat params for service " + paasService);
	}

	private PaaSDeployment addPentahoClientOnlyServices(
			PaaSDeployment paasDeployment, String vmID, String vmName,
			String vmPublicIP, String vmPrivateIP) {
		PaaSDeploymentService paasDS;
		PaaSDeploymentServiceInstance paasDSI;

		// X2GO
		// TODO: Extract port from template
		Integer x2goPort = 22;

		paasDS = new PaaSDeploymentService(paasDeployment,
				PaaSDeploymentServiceType.RD_X2GO.toString(),
				PaaSDeploymentServiceType.RD_X2GO.toString());
		paasDS.setStatus(PaaSDeploymentServiceInstance.StatusType.STARTING.toString());
		paasDeployment.getPaaSDeploymentServices().add(paasDS);

		paasDSI = new PaaSDeploymentServiceInstance(paasDS, vmPublicIP,
				vmPrivateIP,
				PaaSDeploymentServiceInstance.StatusType.STARTING.toString(),
				vmName, vmID, x2goPort);
		paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);

		return paasDeployment;
	}

	private PaaSDeployment addPentahoServerOnlyServices(
			PaaSDeployment paasDeployment, String vmID, String vmName,
			String vmPublicIP, String vmPrivateIP) {
		PaaSDeploymentService paasDS;
		PaaSDeploymentServiceInstance paasDSI;

		// PENTAHO SERVER
		// TODO: Extract port from template
		Integer pentahoSrvPort = 8080;

		paasDS = new PaaSDeploymentService(paasDeployment,
				PaaSDeploymentServiceType.EX_PENTAHO.toString(),
				PaaSDeploymentServiceType.EX_PENTAHO.toString());
		paasDS.setStatus(PaaSDeploymentServiceInstance.StatusType.STARTING.toString());
		paasDeployment.getPaaSDeploymentServices().add(paasDS);

		paasDSI = new PaaSDeploymentServiceInstance(paasDS, vmPublicIP,
				vmPrivateIP,
				PaaSDeploymentServiceInstance.StatusType.STARTING.toString(),
				vmName, vmID, pentahoSrvPort);
		paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);

		// TOMCAT AS
		// TODO: Extract port from template
		Integer tomcatPort = pentahoSrvPort;

		paasDS = new PaaSDeploymentService(paasDeployment,
				PaaSDeploymentServiceType.AS_TOMCAT.toString(),
				PaaSDeploymentServiceType.AS_TOMCAT.toString());
		paasDS.setStatus(PaaSDeploymentServiceInstance.StatusType.STARTING.toString());
		paasDeployment.getPaaSDeploymentServices().add(paasDS);

		paasDSI = new PaaSDeploymentServiceInstance(paasDS, vmPublicIP,
				vmPrivateIP,
				PaaSDeploymentServiceInstance.StatusType.STARTING.toString(),
				vmName, vmID, tomcatPort);
		paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);

		// PostegreSQL
		// TODO: Extract port from template
		/**
		 * POSTGRESQL (LOACALHOST) is included into pentaho service
		 * 
		 */
//		Integer postgreSQLPort = 5432;
//
//		paasDS = new PaaSDeploymentService(paasDeployment,
//				PaaSDeploymentServiceType.DB_POSTGRE.toString(),
//				PaaSDeploymentServiceType.DB_POSTGRE.toString());
//		paasDS.setStatus(PaaSDeploymentServiceInstance.StatusType.STARTING.toString());
//		paasDeployment.getPaaSDeploymentServices().add(paasDS);
//
//		paasDSI = new PaaSDeploymentServiceInstance(paasDS, vmPublicIP,
//				vmPrivateIP,
//				PaaSDeploymentServiceInstance.StatusType.STARTING.toString(),
//				vmName, vmID, postgreSQLPort);
//		paasDS.getPaaSDeploymentServiceInstances().add(paasDSI);

		return paasDeployment;
	}

	@Override
	public Map<String, String> getDeploymentTemplateParameters(
			HeatDeploymentData heatDeploymentData,
			BIaaSDeployRequest deployRequest, BIaaS paasService)
			throws Exception {
		Map<String, String> params = super.getDeploymentTemplateParameters(
				heatDeploymentData, deployRequest, paasService);

		switch (EnumUtils.fromString(BIProductType.class,
				paasService.getProductType())) {
		case Pentaho:
			switch (EnumUtils.fromString(BIPentahoConfigurationVariant.class,
					deployRequest.getBIaasDetails().getConfigurationVariant())) {
			case Console_only:
				PentahoConsoleOnlyDeployDetails pcodd = (PentahoConsoleOnlyDeployDetails) deployRequest
						.getBIaasDetails().getProductSpecificDetails();
				params.put("ip_server_bi", pcodd.getBiServerIP());
				params.put("design_user", pcodd.getDesignerUsername());
				params.put("design_password", pcodd.getDesignerPassword());
				params.put("user_public_key", pcodd.getUserPublicKey());
				break;
			case Server_only:
				PentahoServerOnlyDeployDetails psodd = (PentahoServerOnlyDeployDetails) deployRequest
						.getBIaasDetails().getProductSpecificDetails();
				paasService.getPaaSService().getProductSpecificParameters();
				params.put("admin_password", psodd.getAdminPassword());
				break;
			case Server_w_Console:
				PentahoServerAndConsoleDeployDetails psacdd = (PentahoServerAndConsoleDeployDetails) deployRequest
						.getBIaasDetails().getProductSpecificDetails();
				params.put("admin_password", psacdd.getAdminPassword());
				params.put("design_user", psacdd.getDesignerUsername());
				params.put("design_password", psacdd.getDesignerPassword());
				params.put("user_public_key", psacdd.getUserPublicKey());
				break;

			default:
				throw new IllegalArgumentException(
						"Configuration variant not supported "
								+ (deployRequest).getBIaasDetails()
										.getConfigurationVariant());
			}
			return params;

		default:
		}

		throw new UnsupportedOperationException(
				"Cannot extract Heat template params for service" + paasService);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PaaSServiceEndpoint> getPaaSServiceEndpoints(BIaaS paasService) {

		List<PaaSServiceEndpoint> pses = new ArrayList<PaaSServiceEndpoint>();

		switch (EnumUtils.fromString(BIProductType.class,
				paasService.getProductType())) {
		case Pentaho:
			switch (EnumUtils.fromString(BIPentahoConfigurationVariant.class,
					paasService.getConfigurationVariant())) {
			case Console_only:
				pses.addAll(getX2GOEndpoint(paasService));
				break;
			case Server_only:
				pses.addAll(getPentahoServerEndpoint(paasService));
				break;
			case Server_w_Console:
				pses.addAll(getX2GOEndpoint(paasService));
				pses.addAll(getPentahoServerEndpoint(paasService));
				break;

			default:
				throw new IllegalArgumentException(
						"Configuration variant not supported "
								+ paasService.getConfigurationVariant());
			}
			return pses;

		default:
		}

		throw new UnsupportedOperationException(
				"Cannot extract endpoints for service" + paasService);

	}

	protected List<PaaSServiceEndpoint> getX2GOEndpoint(
			AbstractPaaSService paasService) {

		List<PaaSServiceEndpoint> pses = new ArrayList<PaaSServiceEndpoint>();

		for (PaaSDeploymentService pds : selectPaaSDeploymentServiceByType(
				paasService, PaaSDeploymentServiceType.RD_X2GO)) {
			for (PaaSDeploymentServiceInstance pdsi : pds
					.getPaaSDeploymentServiceInstances()) {
				URI uri = null;
				try {
					//TODO: Improve URI lookup from DN depending from host registration
					uri = new URI(getScheme(pds.getType()), (String) null,
							paasService.getPaaSService().getDomainName()/*pdsi.getPublicIP()*/, getURIPort(pdsi.getPort()),
							null, null, null);
				} catch (URISyntaxException e) {
					LOG.error(e);
				}

				pses.add(PaaSServiceEndpointBuilder.paaSServiceEndpoint()
						.withName(pds.getName())
						.withPaasService(paasService.getPaaSService())
						.withType(pds.getType()).withUri(uri).build());
			}
		}

		return pses;
	}

	protected List<PaaSServiceEndpoint> getPentahoServerEndpoint(
			AbstractPaaSService paasService) {

		List<PaaSServiceEndpoint> pses = new ArrayList<PaaSServiceEndpoint>();

		for (PaaSDeploymentService pds : selectPaaSDeploymentServiceByType(
				paasService, PaaSDeploymentServiceType.EX_PENTAHO)) {
			for (PaaSDeploymentServiceInstance pdsi : pds
					.getPaaSDeploymentServiceInstances()) {
				URI uri = null;
				try {
					//TODO: Improve URI lookup from DN depending from host registration
					uri = new URI(getScheme(pds.getType()), (String) null,
							paasService.getPaaSService().getDomainName()/*pdsi.getPublicIP()*/, getURIPort(pdsi.getPort()),
							null, null, null);
				} catch (URISyntaxException e) {
					LOG.error(e);
				}

				pses.add(PaaSServiceEndpointBuilder.paaSServiceEndpoint()
						.withName(pds.getName())
						.withPaasService(paasService.getPaaSService())
						.withType(pds.getType()).withUri(uri).build());
			}
		}

		return pses;
	}

	@Override
	public HeatDeploymentData getTemplateData(BIaaSDeployRequest deployRequest) {
		String template;
		switch (EnumUtils.fromString(BIProductType.class, deployRequest
				.getBIaasDetails().getProductType())) {

		case Pentaho:
			switch (EnumUtils.fromString(BIPentahoConfigurationVariant.class,
					deployRequest.getBIaasDetails().getConfigurationVariant())) {
			case Console_only:
				template = envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_BIAAS_PENTAHO_CONSOLE_ONLY);
				break;
			case Server_only:
				template = envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_BIAAS_PENTAHO_SERVER_ONLY);
				break;
			case Server_w_Console:
				template = envConfig
						.getSvcEndpointProperty(EnvironmentConfig.SVCEP_TEMPLATE_REPO_PAAS_BIAAS_PENTAHO_SERVER_W_CONSOLE);
				break;
			default:
				throw new IllegalArgumentException(
						"Configuration variant not supported "
								+ (deployRequest).getBIaasDetails()
										.getConfigurationVariant());
			}
			break;

		default:
			throw new IllegalArgumentException("Product type not supported "
					+ deployRequest.getBIaasDetails().getProductType());
		}

		return HeatDeploymentDataBuilder
				.heatDeploymentData()
				.withTemplateURL(
						getDeploymentUtils().getHeatPaaSTemplatesBasePath()
								+ "/" + template).withTemplateName(template)
				.build();
	}

	@Override
	public BIaaSDeployRequest castToProductSpecificRequest(
			BIaaSDeployRequest deployRequest) throws Exception {

		BaseCustomPaaSDetails details = deployRequest.getBIaasDetails()
				.getProductSpecificDetails();

		BaseCustomPaaSDetails specificDetails = details;

		switch (EnumUtils.fromString(BIProductType.class, deployRequest
				.getBIaasDetails().getProductType())) {

		case Pentaho:
			switch (EnumUtils.fromString(BIPentahoConfigurationVariant.class,
					deployRequest.getBIaasDetails().getConfigurationVariant())) {
			case Console_only:
				specificDetails = castToSpecificProductDetails(details,
						PentahoConsoleOnlyDeployDetails.class);
				break;
			case Server_only:
				specificDetails = castToSpecificProductDetails(details,
						PentahoServerOnlyDeployDetails.class);
				break;
			case Server_w_Console:
				specificDetails = castToSpecificProductDetails(details,
						PentahoServerAndConsoleDeployDetails.class);
				break;
			default:
				throw new IllegalArgumentException(
						"Configuration variant not supported "
								+ (deployRequest).getBIaasDetails()
										.getConfigurationVariant());
			}
			break;

		default:
			throw new IllegalArgumentException("Product type not supported "
					+ deployRequest.getBIaasDetails().getProductType());
		}
		deployRequest.getBIaasDetails().setProductSpecificDetails(
				specificDetails);

		return deployRequest;
	}

	/* VALIDATION */
	public static final String IPV4_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	protected boolean validateIP(String ip) {
		return ip != null
				&& ip.matches(IPV4_PATTERN);
	}

	protected boolean validateUsername(String un) {
		return un != null && un.length() > 3;
	}

	protected boolean validatePassword(String pwd) {
		return pwd != null && pwd.length() > 3;
	}
			
	protected boolean validateSSHPublicKey(String publicKeyString) {
		return publicKeyString != null && publicKeyString.matches("^ssh-rsa[\\s]+[0-9A-Za-z\\/+=\\s]*$");
	}
}
