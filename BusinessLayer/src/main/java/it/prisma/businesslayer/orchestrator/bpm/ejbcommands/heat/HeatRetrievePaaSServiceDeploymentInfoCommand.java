package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.heat;

import it.prisma.businesslayer.bizlib.iaas.heat.HeatManagement;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.HeatDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.OpenStackEndpoint;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEndpoint;
import it.prisma.domain.dsl.paas.deployments.PaaSDeploymentServiceRepresentation.PaaSDeploymentServiceRepresentationType;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.openstack4j.api.OSClient;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to retrieve deployment informations for a
 * PaaSService deployed to Heat. It will also populate the
 * {@link PaaSDeployment} structure related to the PaaSService accordingly.<br/>
 * The command is expecting a {@link DeploymentMessage} with
 * {@link HeatDeploymentData} containing the data of the ongoing deployment (ie.
 * the heat stack data). <br/>
 * <br/>
 * 
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} deploymentMessage
 * @return <ul>
 *         <li>{@link PaasDeployment} <code>paasDeployment</code> the service
 *         deployment data</li>
 *         <li>{@link DeploymentMessage} <code>deploymentMessage</code> with the
 *         newly populated {@link PaaSDeployment} and
 *         {@link AbstractPaaSService} fields (<b>NOT YET PERSISTED ON DB !</b>)
 *         </li>
 *         </ul>
 * 
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class HeatRetrievePaaSServiceDeploymentInfoCommand extends
		BaseHeatCommand {

	@Inject
	private EnvironmentConfig envConfig;

	@Inject
	private HeatManagement heatBean;

	public HeatRetrievePaaSServiceDeploymentInfoCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		DeploymentMessage depMsg = (DeploymentMessage) workItem
				.getParameter("deploymentMessage");
		Preconditions.checkNotNull(depMsg);

		ExecutionResults exResults = new ExecutionResults();

		try {
			OSClient osRC = super.getAPIClient((OpenStackEndpoint) depMsg
					.getInfrastructureData().getDeployerEndpoint());

			HeatDeploymentData heatDepData = (HeatDeploymentData) depMsg
					.getDeploymentData();

			PaaSDeployment paasDeployment;
			Collection<PaaSServiceEndpoint> paasEndpoints = new ArrayList<PaaSServiceEndpoint>();
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_HEAT)
					.equals("true")) {

				PaaSDeployment paasD = new PaaSDeployment("HEAT");
				paasD.setName(heatDepData.getStack().getName());
				String status = PaaSService.Status.RUNNING.toString();
				paasD.setStatus(status);

				Set<PaaSDeploymentService> services = new HashSet<PaaSDeploymentService>();
				paasD.setPaaSDeploymentServices(services);

				// Tomcat Service + 1 Instance
				PaaSDeploymentService service = new PaaSDeploymentService(
						paasD,
						PaaSDeploymentServiceRepresentationType.AS_TOMCAT.toString(),
						PaaSDeploymentServiceRepresentationType.AS_TOMCAT.toString());
				Set<PaaSDeploymentServiceInstance> instances = new HashSet<PaaSDeploymentServiceInstance>();
				service.setPaaSDeploymentServiceInstances(instances);

				services.add(service);

				PaaSDeploymentServiceInstance instance = new PaaSDeploymentServiceInstance(
						service, "10.10.10.10", "10.10.10.10",
						PaaSService.Status.RUNNING.toString(), "1",
						"dummy-UUID-" + UUID.randomUUID());

				instances.add(instance);

				// Apache LB Service + 1 Instance
				service = new PaaSDeploymentService(
						paasD,
						PaaSDeploymentServiceRepresentationType.LB_APACHE.toString(),
						PaaSDeploymentServiceRepresentationType.LB_APACHE.toString());
				instances = new HashSet<PaaSDeploymentServiceInstance>();
				service.setPaaSDeploymentServiceInstances(instances);

				services.add(service);

				instance = new PaaSDeploymentServiceInstance(service,
						"10.10.10.20", "10.10.10.20",
						PaaSService.Status.RUNNING.toString(), "1",
						"dummy-UUID-" + UUID.randomUUID());

				instances.add(instance);

				paasDeployment = paasD;

			} else {
				heatDepData.setStack(osRC
						.heat()
						.stacks()
						.getDetails(heatDepData.getStack().getName(),
								heatDepData.getStack().getId()));
				paasDeployment = heatBean.getStackServicesInformations(osRC,
						heatDepData.getStack(), heatDepData,
						depMsg.getPaaSService());

				depMsg.getPaaSService().getPaaSService()
						.setPaaSDeployment(paasDeployment);

				paasEndpoints = heatBean.getPaaSServiceEnpoints(depMsg
						.getPaaSService());
			}

			// Update Deployment Message
			depMsg.setPaasDeployment(paasDeployment);
			depMsg.getPaaSService().getPaaSService()
					.setPaaSDeployment(paasDeployment);

			depMsg.getPaaSService().getPaaSService().getPaasServiceEndpoints()
					.clear();
			depMsg.getPaaSService().getPaaSService().getPaasServiceEndpoints()
					.addAll(paasEndpoints);

			exResults.setData("deploymentMessage", depMsg);
			exResults.setData("paasDeployment", paasDeployment);
			resultOccurred(paasDeployment, exResults);

		} catch (CloudifyAPIErrorException apiEx) {
			// API Exception occurred
			errorOccurred(
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					"API_ERROR:" + apiEx.getAPIError(), exResults);
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR,
					exResults);
		}

		return exResults;
	}

}
