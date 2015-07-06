package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.misc;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent.LRTEventType;
import it.prisma.domain.dsl.prisma.prismaprotocol.Error;

import javax.ejb.Local;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * Bean implementing a command to log an Error (FinalResult) in LRT Events. <br/>
 * <br/>
 * Workitem parameters:
 * @param LRT the LRT to log events to.
 * @param Error the error to log.
 * 
 * @author l.biava
 * 
 */
@Local(ErrorLoggerCommand.class)
public class ErrorLoggerCommand extends BaseCommand {

	public ErrorLoggerCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		// Obtaining parameters
		Error prismaError = (Error) workItem.getParameter("Error");

		ExecutionResults exResults = new ExecutionResults();

		resultOccurred("OK", exResults);
		exResults.setData("ResultStatus", "ERROR");
		//exResults.setData("Error", "ErrorMsg");

		try{
			logger.error(prismaError);
		
			lrtEventLogger.logFinalResult(LRTEventType.ERROR, lrt, prismaError);
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		return exResults;
	}
}
