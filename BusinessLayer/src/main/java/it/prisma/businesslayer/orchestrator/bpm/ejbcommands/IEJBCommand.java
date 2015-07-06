package it.prisma.businesslayer.orchestrator.bpm.ejbcommands;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * All commands that are executed by jBPM executor MUST implement this interface
 * (for EJB loading purpose).
 * 
 * @author l.biava
 * 
 */
public interface IEJBCommand {
	/**
	 * Executed this command's logic.
	 * 
	 * @param ctx
	 *            - contextual data given by the executor service
	 * @return returns any results in case of successful execution
	 * @throws Exception
	 *             in case execution failed and shall be retried if possible
	 */
	public ExecutionResults execute(CommandContext ctx) throws Exception;
}
