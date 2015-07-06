package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.heat;

import it.prisma.businesslayer.bizws.iaas.OpenstackHelper;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.OpenStackEndpoint;

import org.openstack4j.api.OSClient;

public abstract class BaseHeatCommand extends BaseCommand {

	public BaseHeatCommand() throws Exception {
		super();
	}

	protected OSClient getAPIClient(OpenStackEndpoint osEP) {
		// TODO: Identity v2 vs v3
		return OpenstackHelper.getOSClient(osEP.getUrl(), osEP
				.getIdentityVersion(), osEP.getTenantName(), osEP
				.getCredentials().getUsername(), osEP.getCredentials()
				.getPassword());
	}
}
