package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.dns;

import it.prisma.businesslayer.bizlib.dns.DNSMgmtBean;
import it.prisma.businesslayer.bizlib.dns.DNSUtils;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProviderRegistry;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.DeploymentMessage;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

/**
 * Bean implementing a command to register a PaaSService to the DNS.<br/>
 * <br/>
 * Input parameters:
 * 
 * @param {@link DeploymentMessage} <code>deploymentMessage</code>
 * @return {@link DeploymentMessage} <code>deploymentMessage</code> containing
 *         updated data related to what has been registered to the DNS (<b>NOT
 *         SUPPORTED YET</b>)
 * 
 * @throws ORC_WF_DEPLOY_GENERIC_ERROR
 *             in case of generic errors.
 * 
 * @author l.biava
 * 
 */
public class RegisterPaaSServiceToDNSCommand extends BaseCommand {

	private static final Logger LOG = LogManager
			.getLogger(RegisterPaaSServiceToDNSCommand.class);

	@Inject
	private DNSMgmtBean dnsBean;

	@Inject
	private PaaSServiceDeploymentProviderRegistry deploymentProviders;

	public RegisterPaaSServiceToDNSCommand() throws Exception {
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
			String serviceDN = depMsg.getDeployRequest().getServiceDetails()
					.getDomainName();

			// TODO Use a provider/something which knows the service type/name
			// and
			// instance to bind to the DN
			AbstractPaaSService paasSvc = depMsg.getPaaSService();
			// TODO Improve
			List<String> addresses = getIPAddresses(paasSvc);

			if (addresses == null || addresses.size() == 0)
				throw new Exception("No services found to register to DNS.");

			for (String ip : addresses) {
				LOG.debug("Registering IP [" + ip + "] to DN [" + serviceDN
						+ "] for PaaS [" + paasSvc.getPaaSService().getId()
						+ "]");
				new DNSMgmtBean.RegisterDNRetryCommand().init(dnsBean,
						serviceDN, ip).run();
				LOG.debug("Registered IP [" + ip + "] to DN [" + serviceDN
						+ "] for PaaS [" + paasSvc.getPaaSService().getId()
						+ "]");
			}

			exResults.setData("deploymentMessage", depMsg);
			resultOccurred(depMsg, exResults);
		} catch (Exception e) {
			handleSystemException(e,
					OrchestratorErrorCode.ORC_WF_DEPLOY_DNS_ERROR, exResults);
		}

		return exResults;
	}

	public List<String> getIPAddresses(AbstractPaaSService paasService) {
		PaaSServiceType type = PaaSServiceType.valueOf(paasService
				.getPaaSService().getType());
		switch (type) {
		case APPaaS:
			break;
		case APPaaSEnvironment:
			return DNSUtils.getLoadBalancerIPAddresses(paasService);
		case VMaaS:
		case BIaaS:
			return deploymentProviders.getProviderByPaaSType(type)
					.getLBIPAddresses(paasService);
		case DBaaS:
			return DNSUtils.getSingleHostIPAddresses(paasService);
		case MQaaS:
			return DNSUtils.getLoadBalancerIPAddresses(paasService);
		default:
			break;

		}

		return null;
	}

}
