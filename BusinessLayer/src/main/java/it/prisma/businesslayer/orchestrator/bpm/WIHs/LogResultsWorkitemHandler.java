package it.prisma.businesslayer.orchestrator.bpm.WIHs;

import it.prisma.dal.dao.orchestrator.lrt.LRTEventDAO;
import it.prisma.dal.entities.orchestrator.lrt.LRT;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent.LRTEventType;

import java.util.HashMap;

import javax.inject.Inject;

import org.jbpm.process.workitem.AbstractLogOrThrowWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

/**
 * Custom WIH which simply calls the callback function passed as a parameter to
 * the BP. Requires a runtime parameter called 'Callback', containing an
 * instance of an IGenericCallback interface.
 * 
 * @author l.biava
 * 
 */
public class LogResultsWorkitemHandler extends
		AbstractLogOrThrowWorkItemHandler {

	@Inject
	private LRTEventDAO lrtEventDAO;

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// Do nothing, this work item cannot be aborted
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		try {

			LRT lrt = (LRT) workItem.getParameter("LRTInfo");
			String wiResult = (String) workItem.getParameter("Result");
			//RestWSResult wsr= new RestWSResult(new Date().getTime(), 200L, wiResult);
			LRTEvent lrtEvent = new LRTEvent(lrt, LRTEventType.RESULT,
					wiResult);
			lrtEventDAO.create(lrtEvent);

		} catch (Exception e) {
			System.out.println("ERROR: Cannot update LRTEvents - " + e);
		}

		manager.completeWorkItem(workItem.getId(),
				new HashMap<String, Object>());
	}
}