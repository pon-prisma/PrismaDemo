package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.dns;

import it.prisma.businesslayer.bizlib.dns.DNSMgmtBean;
import it.prisma.businesslayer.bizlib.dns.DNSUtils;
import it.prisma.businesslayer.bizlib.orchestration.deployment.providers.PaaSServiceDeploymentProviderRegistry;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

import com.google.common.base.Preconditions;

public class UnregisterPaaSServiceFromDNSCommand extends BaseCommand {

	private static final Logger LOG = LogManager
			.getLogger(UnregisterPaaSServiceFromDNSCommand.class);

	@Inject
	private DNSMgmtBean dnsBean;
	
	@Inject
	private PaaSServiceDeploymentProviderRegistry deploymentProviders;

	public UnregisterPaaSServiceFromDNSCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		AbstractPaaSService paasSvc = (AbstractPaaSService) workItem
				.getParameter("PaaSService");
		Preconditions.checkNotNull(paasSvc);

		ExecutionResults exResults = new ExecutionResults();

		try {

			String serviceDN = paasSvc.getPaaSService().getDomainName();
			
			// TODO Improve
			Set<String> addresses = new HashSet<String>();
			try {
				for (InetAddress address : InetAddress.getAllByName(serviceDN)) {
					addresses.add(address.getHostAddress());
				}
			} catch (UnknownHostException e) {
				LOG.warn("No IP address for the domain name [" + serviceDN
						+ "] of the PaaS [" + paasSvc.getPaaSService().getId()
						+ "] could be found with a lookup");
			}
			addresses.addAll(getIPAddresses(paasSvc));

			if (addresses.size() == 0) {
				LOG.warn("No services found to unregister from the DNS for PaaS [" + paasSvc.getPaaSService().getId()
						+ "]");
				resultOccurred(true, exResults);
			}

			for (String ip : addresses) {
				LOG.debug("Unregistering IP [" + ip + "] from DN [" + serviceDN
						+ "] for PaaS [" + paasSvc.getPaaSService().getId()
						+ "]");
				new DNSMgmtBean.UnregisterDNRetryCommand().init(dnsBean,
						serviceDN, ip).run();
				LOG.debug("Unregistered IP [" + ip + "] from DN [" + serviceDN
						+ "] for PaaS [" + paasSvc.getPaaSService().getId()
						+ "]");
			}

			resultOccurred(true, exResults);
		} catch (Exception e) {
			handleSystemException(e, OrchestratorErrorCode.ORC_WF_UNDEPLOY_DNS_ERROR,
					exResults);
		}

		return exResults;
	}

	public List<String> getIPAddresses(AbstractPaaSService paasService) {
		PaaSServiceType type = PaaSServiceType.valueOf(paasService
				.getPaaSService().getType());
		List<String> addresses = null;
		switch (type) {
		case APPaaSEnvironment:
			addresses = DNSUtils.getLoadBalancerIPAddresses(paasService);
			break;
		case VMaaS:
		case BIaaS:
			addresses = deploymentProviders.getProviderByPaaSType(type)
					.getLBIPAddresses(paasService);
			break;
		case DBaaS:
			addresses = DNSUtils.getSingleHostIPAddresses(paasService);
			break;
		case MQaaS:
			addresses = DNSUtils.getLoadBalancerIPAddresses(paasService);
			break;
		case APPaaS:
		default:
			throw new RuntimeException("PaaSService type not supported: " + type + ".");
		}

		return (addresses != null ? addresses : new ArrayList<String>());
	}
}
