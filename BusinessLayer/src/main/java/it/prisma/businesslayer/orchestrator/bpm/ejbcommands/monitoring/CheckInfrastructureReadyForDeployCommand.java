package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.monitoring;

import it.prisma.businesslayer.bizlib.monitoring.MonitoringBean;
import it.prisma.businesslayer.bizws.iaas.OpenstackHelper;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.businesslayer.orchestrator.dsl.QuotaLimits;
import it.prisma.dal.entities.iaas.tenant.IaaSTenant;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.CheckIaaSResponse;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Limits;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to check whether the infrastructure is ready to
 * perform deploy operations.<br/>
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} deploymentMessage
 * @param throwError
 *            pass <code>true</code> to also launch an error for the WF if the
 *            infrastructure status is
 *            {@link CheckIaaSResponse.StatusType#NOT_READY}.
 * @return <code>status</code> - The status of the infrastructure (
 *         {@link CheckIaaSResponse.StatusType}).
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class CheckInfrastructureReadyForDeployCommand extends BaseCommand {

    private static final Logger LOG = LogManager.getLogger(CheckInfrastructureReadyForDeployCommand.class);

    @Inject
    private EnvironmentConfig envConfig;

    @Inject
    private MonitoringBean monitoring;

    public CheckInfrastructureReadyForDeployCommand() throws Exception {
	super();
    }

    @Override
    public ExecutionResults customExecute(CommandContext ctx) throws Exception {

	// Obtaining parameters
	DeploymentMessage depMsg = (DeploymentMessage) workItem.getParameter("deploymentMessage");
	String te = (String) workItem.getParameter("throwError");
	boolean throwError = (te != null && te.equals("true") ? true : false);
	Preconditions.checkNotNull(depMsg);

	ExecutionResults exResults = new ExecutionResults();

	try {
	    // TODO: Move in another command/ WF task ?
	    try {
		if (isIaaSQuotaExceeded(depMsg)) {
		    errorOccurred(OrchestratorErrorCode.ORC_PLATFORM_QUOTA_EXCEEDED,
			    "Platform is not ready for the requested operation. Quota exceeded.", exResults);
		    return exResults;
		}
	    } catch (Exception e) {
		LOG.error("Unable to check Quota " + e.getMessage());
	    }

	    // TESTING ! Skip monitoring check
	    if ("true".equals(envConfig.getAppProperty(EnvironmentConfig.APP_TEST_SKIP_MONITORING))) {
		LOG.warn("Skipping Pre-deploy monitoring check");
		exResults.setData("status", "READY");
		resultOccurred("READY", exResults);
		return exResults;
	    } else {

		boolean status = monitoring.isDeployAvailable();

		if (status) {
		    LOG.debug("Infrastructure status is OK");
		    // Good result
		    exResults.setData("status", CheckIaaSResponse.StatusType.READY);
		    resultOccurred(CheckIaaSResponse.StatusType.READY, exResults);
		} else {
		    // Bad result
		    exResults.setData("status", CheckIaaSResponse.StatusType.NOT_READY);
		    resultOccurred(CheckIaaSResponse.StatusType.NOT_READY, exResults);

		    // If requested by throwError
		    if (throwError)
			errorOccurred(OrchestratorErrorCode.ORC_PLATFORM_STATUS_NOT_READY,
				"Platform is not ready for the requested operation.", exResults);

		}
	    }
	} catch (Exception sysEx) {
	    // System Exception occurred
	    handleSystemException(sysEx, OrchestratorErrorCode.ORC_WF_DEPLOY_MONITORING_ERROR, exResults);
	}

	return exResults;
    }

	protected boolean isIaaSQuotaExceeded(DeploymentMessage depMsg) {

		// TODO: If OpenStack check quota
		try {

			IaaSTenant iaasTenant = depMsg.getInfrastructureData()
					.getIaasTenant();
			OSClient osRC = getAPIClient(iaasTenant);

			Limits limits = osRC.compute().quotaSets().limits();
			QuotaLimits ql = depMsg.getQuotaLimits();

			boolean exceeded = false;
			exceeded |= checkQuotaExceeded("CPUs", limits.getAbsolute()
					.getMaxTotalCores(), limits.getAbsolute()
					.getTotalCoresUsed(), ql.getCpus());
			exceeded |= checkQuotaExceeded("RAM", limits.getAbsolute()
					.getMaxTotalRAMSize(), limits.getAbsolute()
					.getTotalRAMUsed(), ql.getRam());
			exceeded |= checkQuotaExceeded("Instances", limits.getAbsolute()
					.getMaxTotalInstances(), limits.getAbsolute()
					.getTotalInstancesUsed(), ql.getInstances());
			exceeded |= checkQuotaExceeded("IPs", limits.getAbsolute()
					.getMaxTotalFloatingIps(), limits.getAbsolute()
					.getTotalFloatingIpsUsed(), ql.getIps());
			exceeded |= checkQuotaExceeded("Volumes", limits.getAbsolute()
					.getMaxTotalVolumes(), limits.getAbsolute()
					.getTotalVolumesUsed(), ql.getVolumes());
			exceeded |= checkQuotaExceeded("Volumes space", limits
					.getAbsolute().getMaxTotalVolumeGigabytes(), limits
					.getAbsolute().getTotalVolumeGigabytesUsed(),
					ql.getVolumeSize());

			return exceeded;
		} catch (Exception e) {
			throw new RuntimeException(String.format(
					"Unable to check infrastructure quota for Workgroup %d",
					depMsg.getWorkgroup().getId()), e);
		}
	}

	protected boolean checkQuotaExceeded(String name, int total, int used,
			int min) {
		LOG.debug(String
				.format("OpenStack quota check: current status of %s - total=%d, used=%d, available=%d, min allowed=%d",
						name, total, used, total - used, min));

		// If limit not available, skip
		if (total < 0) {
			LOG.debug(String.format(
					"OpenStack quota check: limit unavailable for %s (%d)",
					name, total));
			return false;
		}

		if (total - used < min) {
			LOG.warn(String
					.format("OpenStack quota check: exceeded %s - total=%d, used=%d, available=%d, min allowed=%d",
							name, total, used, total - used, min));
			return true;
		}
		return false;
	}

	protected OSClient getAPIClient(IaaSTenant iaasTenant) {
		// TODO: Identity v2 vs v3
		return OpenstackHelper.getOSClient(iaasTenant.getIaasEnvironment()
				.getIdentityURL(), ""
				+ iaasTenant.getIaasEnvironment().getIdentityVersion(),
				iaasTenant.getName(), iaasTenant.getUsername(), iaasTenant
						.getPassword());
	}
}
