package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.misc;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;

import javax.ejb.Local;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * Bean implementing a dummy command, does nothing, always return result OK (200).
 * 
 * @author l.biava
 * 
 */
@Local(DummyCommand.class)
public class DummyCommand extends BaseCommand {

	public DummyCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		ExecutionResults exResults = new ExecutionResults();

		resultOccurred("DUMMY OK", exResults);

		return exResults;
	}
}
