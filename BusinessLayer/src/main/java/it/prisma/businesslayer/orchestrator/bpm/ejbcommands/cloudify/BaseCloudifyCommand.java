package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.businesslayer.orchestrator.dsl.CloudifyEndpoint;

public abstract class BaseCloudifyCommand extends BaseCommand {

	public BaseCloudifyCommand() throws Exception {
		super();
	}

	protected CloudifyAPIClient getAPIClient(CloudifyEndpoint cfyEP) {
		return new CloudifyAPIClient(cfyEP.getUrl(), cfyEP.getVersion());
	}
}
