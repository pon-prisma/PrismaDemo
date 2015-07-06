package it.prisma.businesslayer.orchestrator.bpm.WIHs;

import it.prisma.domain.dsl.prisma.prismaprotocol.Error;

import org.drools.core.process.instance.WorkItemManager;
import org.drools.core.process.instance.impl.WorkItemImpl;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.slf4j.Logger;

public class EJBWorkItemHelper {

	public static CommandContext buildCommandContext(WorkItem workItem,
			Logger logger) {
		String businessKey = buildBusinessKey(workItem);

		if (logger != null)
			logger.debug("Executing work item {} with built business key {}",
					workItem, businessKey);

		CommandContext ctxCMD = new CommandContext();
		ctxCMD.setData("businessKey", businessKey);
		ctxCMD.setData("workItem", workItem);
		ctxCMD.setData("processInstanceId", getProcessInstanceId(workItem));
		ctxCMD.setData("deploymentId",
				((WorkItemImpl) workItem).getDeploymentId());

		return ctxCMD;
	}

	protected static String buildBusinessKey(WorkItem workItem) {
		StringBuffer businessKey = new StringBuffer();
		businessKey.append(getProcessInstanceId(workItem));
		businessKey.append(":");
		businessKey.append(workItem.getId());
		return businessKey.toString();
	}

	protected static long getProcessInstanceId(WorkItem workItem) {
		return ((WorkItemImpl) workItem).getProcessInstanceId();
	}

	public static void signalEvent(KieSession ksession, String eventName,
			Object eventPayload, Long processId) {

		// ((org.drools.core.process.instance.WorkItemManager) manager)
		// .signalEvent("Error-GenericError",
		// (Error) results.getData("Error"),
		// workItem.getProcessInstanceId());

		ksession.signalEvent(eventName, eventPayload, processId);
	}

	/**
	 * If the workitem has failed (i.e. a <code>SignalEvent</code> is present in
	 * the results) an error event will be signaled to the related process and
	 * the workitem will be aborted. Otherwise the workitem will be completed
	 * against the {@link WorkItemManager}.
	 * 
	 * @param results
	 *            the workitem's {@link ExecutionResults}
	 * @param workItem
	 *            the {@link WorkItem}
	 * @param ctx
	 *            the {@link CommandContext}
	 * @param logger
	 *            an optional logger to log events
	 */
	public static void checkWorkItemOutcome(ExecutionResults results,
			WorkItem workItem, CommandContext ctx, Logger logger) {
		RuntimeManager manager = getRuntimeManager(ctx);
		RuntimeEngine engine = manager
				.getRuntimeEngine(ProcessInstanceIdContext.get((Long) ctx
						.getData("processInstanceId")));

		if (results.getData("SignalEvent") != null) {
			// find the right runtime to do the complete

			// TODO Use appropriate event type with 'Error-<ErrorCode as in
			// BPMN file>'
//			SignalEvent signalEvent = (SignalEvent) results
//					.getData("SignalEvent");

			signalEvent(engine.getKieSession(), "Error-GenericError",
					(Error) results.getData("Error"),
					workItem.getProcessInstanceId());

			if (logger != null)
				logger.debug("Command threw error signal {}",
						(Error) results.getData("Error"));

			// Abort the failed WorkItem
			try {
				engine.getKieSession().getWorkItemManager().abortWorkItem(-1);
			} catch (Exception e) {
				if (logger != null)
					logger.debug("Cannot abort workitem {}", workItem);
			}
		} else {
			if (logger != null)
				logger.debug("Command executed successfully with results {}",
						results);

			// Complete the successful WorkItem
			try {
				engine.getKieSession().getWorkItemManager()
						.completeWorkItem(workItem.getId(), results.getData());
			} catch (Exception e) {
				if (logger != null)
					logger.debug("Cannot complete workitem {}", workItem);
			}
		}
	}

	// public void signalError(ExecutionResults results, WorkItem workItem,
	// RuntimeEngine engine) {
	// if (results.getData("SignalEvent") != null) {
	// // TODO Use appropriate event type with 'Error-<ErrorCode as in
	// // BPMN file>'
	// SignalEvent signalEvent = (SignalEvent) results
	// .getData("SignalEvent");
	//
	// signalEvent(engine.getKieSession(), "Error-GenericError",
	// (Error) results.getData("Error"),
	// workItem.getProcessInstanceId());
	//
	// logger.debug("Command threw error signal {}",
	// (Error) results.getData("Error"));
	// }
	// }

	protected static RuntimeManager getRuntimeManager(CommandContext ctx) {
		String deploymentId = (String) ctx.getData("deploymentId");
		RuntimeManager runtimeManager = RuntimeManagerRegistry.get()
				.getManager(deploymentId);

		if (runtimeManager == null) {
			throw new IllegalStateException(
					"There is no runtime manager for deployment "
							+ deploymentId);
		}

		return runtimeManager;
	}
}
