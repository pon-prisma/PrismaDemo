package it.prisma.businesslayer.orchestrator.logging;

import it.prisma.dal.dao.orchestrator.lrt.LRTDAO;
import it.prisma.dal.dao.orchestrator.lrt.LRTEventDAO;
import it.prisma.dal.entities.orchestrator.lrt.LRT;
import it.prisma.dal.entities.orchestrator.lrt.LRTEvent;
import it.prisma.utils.json.JsonUtility;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Startup
@javax.ejb.Singleton
@ApplicationScoped
public class LRTEventLogger {
	
	@Inject
	private LRTEventDAO lrtEventDAO;

	@Inject
	private LRTDAO lrtDAO;

	/*
	 * public void logResult(LRT lrt, String wiResult) throws Exception { try {
	 * 
	 * LRTEvent lrtEvent = new LRTEvent(lrt, 1, LRTEventType.RESULT, wiResult);
	 * lrtEventDAO.create(lrtEvent);
	 * 
	 * } catch (Exception e) {
	 * System.out.println("ERROR: Cannot update LRTEvents - " + e); throw e; } }
	 */

	/*
	 * public <ResultType> void logResult(LRT lrt, ResultType result) throws
	 * Exception { try { String wiResult=JsonUtility.serializeJson(result);
	 * LRTEvent lrtEvent = new LRTEvent(lrt, 1, LRTEventType.RESULT, wiResult);
	 * lrtEventDAO.create(lrtEvent);
	 * 
	 * } catch (Exception e) {
	 * System.out.println("ERROR: Cannot update LRTEvents - " + e); throw e; } }
	 */

	public <ResultType> LRTEvent log(LRTEvent.LRTEventType eventType, LRT lrt,
			ResultType result) throws Exception {
		try {
			String wiResult = JsonUtility.serializeJson(result);
			LRTEvent lrtEvent = new LRTEvent(lrt, eventType, wiResult);
			lrtEventDAO.create(lrtEvent);

			// LRTInfoEndpoint.onLRTEvent(lrt.getLRTID(), lrtEvent);

			return lrtEvent;
		} catch (Exception e) {
			System.out.println("ERROR: Cannot update LRTEvents - " + e);
			throw e;
		}
	}

	public <ResultType> void logFinalResult(LRTEvent.LRTEventType eventType,
			LRT lrt, ResultType result) throws Exception {
		try {
			LRTEvent lrtEvent = log(eventType, lrt, result);

			lrt.setResult(lrtEvent);
			lrtDAO.update(lrt);

		} catch (Exception e) {
			System.out.println("ERROR: Cannot update LRTEvents - " + e);
			throw e;
		}
	}

}
