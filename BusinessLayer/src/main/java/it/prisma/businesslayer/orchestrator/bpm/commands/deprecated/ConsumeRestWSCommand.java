package it.prisma.businesslayer.orchestrator.bpm.commands.deprecated;

import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jbpm.process.workitem.rest.RESTWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.executor.api.Command;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * This class implements a jBPM Executor Command which consumes a REST web
 * service.
 * 
 * @author l.biava
 * 
 */
@Deprecated
public class ConsumeRestWSCommand implements Command {

	// Dummy variables for the workaround to use the default RestWIH to consume
	// REST WS.
	private long managerResultId;
	private Map<String, Object> managerResultResults;

	public static Logger prismaLog = LogManager
			.getLogger(ConsumeRestWSCommand.class);

	@Override
	/**
	 * Required process params (in workItem): Url, Method.
	 */
	public ExecutionResults execute(CommandContext ctx) throws Exception {

		RESTWorkItemHandler restWIH = new RESTWorkItemHandler();

		// Retrieving data from context (also process variables)
		WorkItem workItem = (WorkItem) ctx.getData("workItem");
		String url = (String) workItem.getParameter("Url");
		// Map<String, Object> headers = new HashMap<String, Object>();
		// headers.put("Content-type","application/json");
		// headers.put("Accept", "application/json");
		// headers = workItem.getParameters();

		//String method = (String) workItem.getParameter("Method");
		long procInstanceId = (Long) ctx.getData("processInstanceId");
		Boolean redoTask = (Boolean) workItem.getParameter("RedoTask");

		// Logging into Prisma Log
		if (redoTask != null && redoTask)
			prismaLog.info("RESUMED WIH - ConsumeRestWS. PID(" + procInstanceId
					+ "), Url(" + url + ")");
		else
			prismaLog.info("STARTED WIH - ConsumeRestWS. PID(" + procInstanceId
					+ "), Url(" + url + ")");
		System.out.println("Doing REST WS Consume on URL:" + url);

		restWIH.executeWorkItem(workItem, new DummyWorkItemManager());

		String resultString = (String) managerResultResults.get("Result");
		System.out.println("Done REST WS Consume, result: " + resultString);

		long delay = 5000;
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Simulating " + delay + "ms delay");

		ExecutionResults exResults = new ExecutionResults();
		exResults.setData("Result", resultString);

		prismaLog.info("ENDED WIH - ConsumeRestWS. PID(" + procInstanceId
				+ "), Url(" + url + "), Result(" + resultString + ")");

		return exResults;
	}

	public long getManagerResultId() {
		return managerResultId;
	}

	public void setManagerResultId(long managerResultId) {
		this.managerResultId = managerResultId;
	}

	// Dummy WIM for the workaround
	class DummyWorkItemManager implements WorkItemManager {

		@Override
		public void completeWorkItem(long id, Map<String, Object> results) {
			setManagerResultId(id);
			managerResultResults = results;
		}

		@Override
		public void abortWorkItem(long id) {
			// TODO Auto-generated method stub

		}

		@Override
		public void registerWorkItemHandler(String workItemName,
				WorkItemHandler handler) {
			// TODO Auto-generated method stub

		}
	}

}
