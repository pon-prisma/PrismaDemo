package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.misc;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent.LRTEventType;

import javax.ejb.Local;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * Bean implementing a command to log a Result (FinalResult) in LRT Events. <br/>
 * <br/>
 * Workitem parameters:
 * @param LRT the LRT to log events to.
 * @param Result the result to log.
 * 
 * @author l.biava
 * 
 */
@Local(ResultLoggerCommand.class)
public class ResultLoggerCommand extends BaseCommand {

	public ResultLoggerCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		Object result = (Object) workItem.getParameter("Result");

		ExecutionResults exResults = new ExecutionResults();

		resultOccurred(result, exResults);
		// exResults.setData("ResultStatus", "ERROR");
		// exResults.setData("Error", "ErrorMsg");

		lrtEventLogger.logFinalResult(LRTEventType.RESULT, lrt, result);

		return exResults;
	}
}
