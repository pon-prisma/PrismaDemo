package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import java.net.SocketTimeoutException;

import it.prisma.businesslayer.bizlib.orchestration.DeploymentOrchestratorBean;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIErrorException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyDeploymentData;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyEndpoint;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.UndeploymentMessage;
import it.prisma.domain.dsl.cloudify.response.ApplicationDescriptionResponse;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.utils.misc.polling.ExternallyControlledPoller;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

public class CloudifyWaitPaaSServiceUndeployCompleteCommand extends
		BaseCloudifyCommand {

	@Inject
	private EnvironmentConfig envConfig;

	public CloudifyWaitPaaSServiceUndeployCompleteCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		UndeploymentMessage undepMsg = (UndeploymentMessage) workItem
				.getParameter(DeploymentOrchestratorBean.ARG_IN_UNDEPLOYMENT_MSG);

		Preconditions.checkNotNull(undepMsg);
		
		ExecutionResults exResults = new ExecutionResults();

		logger.info("Checking undeployment status");

		try {
		
			CloudifyAPIClient cfyRC = super.getAPIClient((CloudifyEndpoint) undepMsg
							.getInfrastructureData().getDeployerEndpoint());
	
			if (envConfig.getAppProperty(EnvironmentConfig.APP_TEST_FAKE_CFY)
					.equals("true")) {
				exResults.setData("ResultDeploymentCompleted", true);
				resultOccurred(true, exResults);
			} else {
				try {
					ApplicationDescriptionResponse response = cfyRC.ApplicationDescription(
							undepMsg.getPaaSService().getPaaSService().getPaaSDeployment().getName());
//					if (response.getApplicationState().equals("IN_PROGRESS")) {
						exResults.setData("ResultDeploymentCompleted", false);
						resultOccurred(false, exResults);
//					}
				} catch (CloudifyAPIErrorException apiEx) {
					if (apiEx.getAPIError().getMessageId()
							.equals("missing_resource")) {
						exResults.setData("ResultDeploymentCompleted", true);
						resultOccurred(true, exResults);
					} else {
						// API Exception occurred
						errorOccurred(
								OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_UNDEPLOYING_ERROR,
								"API_ERROR:" + apiEx.getAPIError(), exResults);
					}
				} catch (RestClientException rce) {
					if (rce.getCause() != null && rce.getCause().getCause() != null) {
						if (rce.getCause().getCause() instanceof SocketTimeoutException) {
							logger.error("SocketTimeoutException trying to contact Cloudify at [" + cfyRC.getBaseWSUrl() +
									"] while checking for undeploy status of Paas [" +  undepMsg.getPaaSServiceRepresentation().getId() + "].");
							exResults.setData("ResultDeploymentCompleted", false);
							resultOccurred(false, exResults);
						}
					}
				}
			}
		} catch (Exception sysEx) {
			// System Exception occurred
			handleSystemException(
					sysEx,
					OrchestratorErrorCode.ORC_WF_DEPLOY_PAAS_UNDEPLOYING_ERROR,
					exResults);
		}
		return exResults;
	}

}


