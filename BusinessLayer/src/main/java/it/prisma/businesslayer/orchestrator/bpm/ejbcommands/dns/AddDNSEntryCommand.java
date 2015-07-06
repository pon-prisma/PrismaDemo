package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.dns;

import it.prisma.businesslayer.bizlib.dns.DNSMgmtBean;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import javax.ejb.Local;
import javax.inject.Inject;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * This class implements a jBPM Executor Command which adds a DNS record.
 * 
 * @author l.biava
 * 
 */
@Local(AddDNSEntryCommand.class)
public class AddDNSEntryCommand extends BaseCommand {

	@Inject
	private DNSMgmtBean dnsBean;

	public AddDNSEntryCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		String serviceDN = (String) workItem.getParameter("DomainName");
		// String serviceIP = (String) workItem
		// .getParameter("IP");

		ExecutionResults exResults = new ExecutionResults();

		try {
			// TODO Use a provider/something which knows the service type/name
			// and
			// instance to bind to the DN
			PaaSService paasSvc = (PaaSService) workItem
					.getParameter("PaaSService");
			// TODO Improve
			PaaSDeploymentService lbSvc = getLBService(paasSvc
					.getPaaSDeployment());
			if (lbSvc == null)
				throw new Exception(
						"No load balancer found to register to DNS.");

			PaaSDeploymentServiceInstance lbSvcInst = lbSvc
					.getPaaSDeploymentServiceInstances().iterator().next();
			if (lbSvc.getPaaSDeploymentServiceInstances().size() < 1)
				throw new Exception(
						"No load balancer found to register to DNS.");

			String serviceIP = lbSvcInst.getPublicIP();

			// TODO: Restore
			// if (serviceIP == null || serviceIP.equals(""))
			// throw new Exception("No public IP to register to DNS."
			// + lbSvcInst.toString());
			if (serviceIP == null || serviceIP.equals(""))
				serviceIP = lbSvcInst.getPrivateIP();

			new DNSMgmtBean.RegisterDNRetryCommand().init(dnsBean, serviceDN,
					serviceIP).run();

			resultOccurred("OK", exResults);
		} catch (Exception e) {
			handleSystemException(e, OrchestratorErrorCode.ORC_GENERIC_ERROR,
					exResults);
		}

		return exResults;
	}

	public static PaaSDeploymentService getLBService(
			PaaSDeployment paasDeployment) {
		for (PaaSDeploymentService item : paasDeployment
				.getPaaSDeploymentServices()) {
			// TODO: Fix naming
			if (item.getType().contains("LB")
					|| item.getType().contains("apache"))
				return item;
		}
		return null;
	}
}
