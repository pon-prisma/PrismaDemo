package it.prisma.businesslayer.orchestrator.bpm.WIHs;

import it.prisma.businesslayer.orchestrator.bpm.WIHs.misc.SignalEvent;
import it.prisma.businesslayer.orchestrator.bpm.commands.EJBDispatcherCommand;

import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.executor.impl.wih.AsyncWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom WIH to synchronously call the EJB command dispatcher (
 * {@link EJBDispatcherCommand}). This handler simulates the behaviour of
 * {@link AsyncWorkItemHandler} but executes the command syncronously.
 * 
 * @author l.biava
 * 
 */
public class SyncEJBDispatcherWorkitemHandler implements WorkItemHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(SyncEJBDispatcherWorkitemHandler.class);

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// Do nothing, this work item cannot be aborted
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

		ExecutionResults exResults = new ExecutionResults();
		try {
			EJBDispatcherCommand ejbDispCmd = new EJBDispatcherCommand();

			// TODO: Dummy implementation
			CommandContext ctxCMD = new CommandContext();

			ctxCMD.setData("workItem", workItem);
			ctxCMD.setData("deploymentId",
					((WorkItemImpl) workItem).getDeploymentId());
			ctxCMD.setData("processInstanceId",
					((WorkItemImpl) workItem).getProcessInstanceId());

			exResults = ejbDispCmd.execute(ctxCMD);

			if (exResults.getData("SignalEvent") != null) {
				SignalEvent signalEvent = (SignalEvent) exResults
						.getData("SignalEvent");
				((org.drools.core.process.instance.WorkItemManager) manager)
						.signalEvent(
								signalEvent.getType().toString(),
								(org.drools.core.process.instance.WorkItem) workItem,
								workItem.getProcessInstanceId());
				// workItem.getParameters().put(signalEvent, cause);
				//
				// } else {
				// if( cause instanceof RuntimeException ) {
				// throw (RuntimeException) cause;
				// } else {
				// throw new WorkItemHandlerRuntimeException(cause,
				// "Signalling process instance " +
				// workItem.getProcessInstanceId() + " with '" + this.eventType
				// + "' resulted this exception.");
				// }
				// }
			}
		} catch (Exception e) {
			logger.error("ERROR: Unable to instantiate requested command.", e);
			e.printStackTrace();
			manager.abortWorkItem(-1);
		}

		manager.completeWorkItem(workItem.getId(), exResults.getData());
	}
}