package it.prisma.businesslayer.orchestrator.bpm.WIHs;

/* Copyright 2013 JBoss by Red Hat. */

import it.prisma.businesslayer.orchestrator.bpm.commands.EJBDispatcherCommand;

import org.jbpm.executor.impl.wih.AsyncWorkItemHandler;
import org.jbpm.executor.impl.wih.AsyncWorkItemHandlerCmdCallback;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.executor.api.CommandCallback;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.kie.internal.executor.api.ExecutorService;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Asynchronous work item handler that utilizes power of
 * <code>ExecutorService</code>. it expects following parameters to be present
 * on work item for proper execution:
 * <ul>
 * <li>CommandClass - FQCN of the command to be executed - mandatory unless this
 * handler is configured with default command class</li>
 * <li>Retries - number of retires for the command execution - optional</li>
 * </ul>
 * During execution it will set contextual data that will be available inside
 * the command:
 * <ul>
 * <li>businessKey - generated from process instance id and work item id in
 * following format: [processInstanceId]:[workItemId]</li>
 * <li>workItem - actual work item instance that is being executed (including
 * all parameters)</li>
 * <li>processInstanceId - id of the process instance that triggered this work
 * item execution</li>
 * </ul>
 * 
 * In case work item shall be aborted handler will attempt to cancel active
 * requests based on business key (process instance id and work item id)
 */
public class AsyncEJBWorkItemHandler extends AsyncWorkItemHandler implements
		WorkItemHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(AsyncEJBWorkItemHandler.class);

	//private String commandClass;
	private ExecutorService executorService;

	public AsyncEJBWorkItemHandler(ExecutorService executorService) {
		super(executorService);
		this.executorService = executorService;
	}

	public AsyncEJBWorkItemHandler(ExecutorService executorService,
			String commandClass) {
		super(executorService, commandClass);
		this.executorService = executorService;
		//this.commandClass = commandClass;
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

		String cmdClass = EJBDispatcherCommand.class.getCanonicalName();
		try {

			if (executorService == null || !executorService.isActive()) {
				throw new IllegalStateException(
						"Executor is not set or is not active");
			}

			// String businessKey = buildBusinessKey(workItem);
			// logger.debug("Executing work item {} with built business key {}",
			// workItem, businessKey);
			// // cmdClass = (String) workItem.getParameter("EJBCommandClass");
			// // if (cmdClass == null) {
			// // cmdClass = this.commandClass;
			// // }
			// logger.debug("Command class for this execution is {}", cmdClass);
			// CommandContext ctxCMD = new CommandContext();
			// ctxCMD.setData("businessKey", businessKey);
			// ctxCMD.setData("workItem", workItem);
			// ctxCMD.setData("processInstanceId",
			// getProcessInstanceId(workItem));
			// ctxCMD.setData("deploymentId",
			// ((WorkItemImpl) workItem).getDeploymentId());

			CommandContext ctxCMD = EJBWorkItemHelper.buildCommandContext(
					workItem, logger);

			ctxCMD.setData("callbacks",
					AsyncEJBWorkItemHandlerCmdCallback.class.getName());
			if (workItem.getParameter("Retries") != null) {
				ctxCMD.setData("retries", Integer.parseInt(workItem
						.getParameter("Retries").toString()));
			}

			logger.trace("Command context {}", ctxCMD);
			Long requestId = executorService.scheduleRequest(cmdClass, ctxCMD);
			logger.debug("Request scheduled successfully with id {}", requestId);
		} catch (Exception e) {
			logger.error("ERROR: Unable to instantiate requested command ("
					+ cmdClass + ").", e);

			manager.abortWorkItem(-1);
		}

	}

	public static class AsyncEJBWorkItemHandlerCmdCallback extends
			AsyncWorkItemHandlerCmdCallback implements CommandCallback {

		@Override
		public void onCommandDone(CommandContext ctx, ExecutionResults results) {
			WorkItem workItem = (WorkItem) ctx.getData("workItem");
			logger.debug("About to complete work item {}", workItem);

			// find the right runtime to do the complete
			RuntimeManager manager = getRuntimeManager(ctx);
			RuntimeEngine engine = manager
					.getRuntimeEngine(ProcessInstanceIdContext.get((Long) ctx
							.getData("processInstanceId")));

			EJBWorkItemHelper.checkWorkItemOutcome(results, workItem, ctx,
					logger);

			manager.disposeRuntimeEngine(engine);
		}
	}
}
