package it.prisma.businesslayer.orchestrator.bpm.ejbcommands.misc;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.BaseCommand;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent.LRTEventType;
import it.prisma.domain.dsl.cloudify.response.ServiceInstanceDescriptionResponse;

import javax.ejb.Local;

import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * This class implements a jBPM Executor Command which consumes a REST web
 * service.
 * 
 * @author l.biava
 * 
 */
@Local(UpdateDBCommand.class)
public class UpdateDBCommand extends BaseCommand {

	public UpdateDBCommand() throws Exception {
		super();
	}

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults customExecute(CommandContext ctx) throws Exception {

		Object prevAPIResult = workItem.getParameter("PreviousAPIResult");
		ServiceInstanceDescriptionResponse servInst = (ServiceInstanceDescriptionResponse) prevAPIResult;

		String publicIP = servInst.getPublicIp();

		ExecutionResults exResults = new ExecutionResults();

		resultOccurred("PublicIP:" + publicIP, exResults);

		lrtEventLogger.logFinalResult(LRTEventType.RESULT, lrt, publicIP);

		return exResults;
	}
}
