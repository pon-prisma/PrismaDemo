package it.prisma.businesslayer.orchestrator.bpm.WIHs;

/* Copyright 2013 JBoss by Red Hat. */

import it.prisma.businesslayer.orchestrator.bpm.commands.EJBDispatcherCommand;

import org.drools.core.process.instance.impl.WorkItemImpl;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
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
public class SyncEJBWorkItemHandler implements WorkItemHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(SyncEJBWorkItemHandler.class);

	private String commandClass;

	public SyncEJBWorkItemHandler() {

	}

	public SyncEJBWorkItemHandler(String commandClass) {

		this.commandClass = commandClass;
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		String cmdClass = "UNKNOWN";
		try {
			ExecutionResults exResults = new ExecutionResults();
			EJBDispatcherCommand ejbDispatcherCommand = new EJBDispatcherCommand();

			cmdClass = (String) workItem.getParameter("EJBCommandClass");
			if (cmdClass == null) {
				cmdClass = this.commandClass;
			}
			logger.debug("Command class for this execution is {}", cmdClass);

			CommandContext ctxCMD = EJBWorkItemHelper.buildCommandContext(
					workItem, logger);

			logger.trace("Command context {}", ctxCMD);

			exResults = ejbDispatcherCommand.execute(ctxCMD);

			EJBWorkItemHelper.checkWorkItemOutcome(exResults, workItem, ctxCMD,
					logger);

		} catch (Exception e) {
			logger.error("ERROR: Unable to instantiate requested command ("
					+ cmdClass + ").", e);

			manager.abortWorkItem(-1);
		}

	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// String businessKey = buildBusinessKey(workItem);
		// logger.info(
		// "Looking up for not cancelled and not done requests for business key {}",
		// businessKey);
		// List<RequestInfo> requests = executorService
		// .getRequestsByBusinessKey(businessKey);
		// if (requests != null) {
		// for (RequestInfo request : requests) {
		// if (request.getStatus() != STATUS.CANCELLED
		// && request.getStatus() != STATUS.DONE
		// && request.getStatus() != STATUS.ERROR) {
		// logger.info(
		// "About to cancel request with id {} and business key {} request state {}",
		// request.getId(), businessKey, request.getStatus());
		// executorService.cancelRequest(request.getId());
		// }
		// }
		// }
	}

	protected String buildBusinessKey(WorkItem workItem) {
		StringBuffer businessKey = new StringBuffer();
		businessKey.append(getProcessInstanceId(workItem));
		businessKey.append(":");
		businessKey.append(workItem.getId());
		return businessKey.toString();
	}

	protected long getProcessInstanceId(WorkItem workItem) {
		return ((WorkItemImpl) workItem).getProcessInstanceId();
	}

}
