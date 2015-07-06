package it.prisma.businesslayer.orchestrator.bpm.WIHs;

import it.prisma.businesslayer.orchestrator.bpm.callbacks.IGenericCallback;

import java.util.HashMap;

import org.jbpm.process.workitem.AbstractLogOrThrowWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom WIH which simply calls the callback function passed as a parameter to
 * the BP. Requires a runtime parameter called 'Callback', containing an
 * instance of an IGenericCallback interface.
 * 
 * @author l.biava
 * 
 */
public class CallbackWorkitemHandler extends AbstractLogOrThrowWorkItemHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(CallbackWorkitemHandler.class);

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// Do nothing, this work item cannot be aborted
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		IGenericCallback callback = (IGenericCallback) workItem
				.getParameter("Callback");

		if (callback == null) {
			logger.warn("CallbackWorkItem, no callback received.");
		} else {
			try {
				callback.onCallback(workItem, WorkItem.class);
			} catch (Exception e) {
				logger.warn("CallbackWorkItem, exception:" + e);
			}
		}
		manager.completeWorkItem(workItem.getId(),
				new HashMap<String, Object>());
	}
}