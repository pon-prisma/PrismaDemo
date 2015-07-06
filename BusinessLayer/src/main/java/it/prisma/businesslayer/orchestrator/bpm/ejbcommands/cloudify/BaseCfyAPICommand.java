package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify;

import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify.CloudifyAPIClient;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

public abstract class BaseCfyAPICommand extends BaseCommand {

	protected String cfyUrl;
	protected String cfyVersion;
	protected CloudifyAPIClient cfyRC;

	public BaseCfyAPICommand() throws Exception {
		super();
	}

	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		cfyUrl = (String) workItem.getParameter("CfyBaseUrl");
		cfyVersion = (String) workItem.getParameter("CfyVersion");

		cfyRC = new CloudifyAPIClient(cfyUrl, cfyVersion);

		return new ExecutionResults();
	}
}
