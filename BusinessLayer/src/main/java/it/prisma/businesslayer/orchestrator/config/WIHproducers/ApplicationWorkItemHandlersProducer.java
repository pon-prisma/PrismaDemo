package it.prisma.businesslayer.orchestrator.config.WIHproducers;

import it.prisma.businesslayer.orchestrator.bpm.WIHs.AsyncEJBWorkItemHandler;
import it.prisma.businesslayer.orchestrator.bpm.WIHs.CallbackWorkitemHandler;
import it.prisma.businesslayer.orchestrator.bpm.WIHs.LogResultsWorkitemHandler;
import it.prisma.businesslayer.orchestrator.bpm.WIHs.SyncEJBDispatcherWorkitemHandler;
import it.prisma.businesslayer.orchestrator.bpm.WIHs.SyncEJBWorkItemHandler;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbpm.executor.impl.wih.AsyncWorkItemHandler;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.internal.executor.api.ExecutorService;
import org.kie.internal.runtime.manager.WorkItemHandlerProducer;

/**
 * Binds WIH for the BP 'rest-tast'.
 * 
 * @author l.biava
 *
 */
public class ApplicationWorkItemHandlersProducer implements
		WorkItemHandlerProducer {

	private static final Logger LOG = LogManager
			.getLogger(ApplicationWorkItemHandlersProducer.class);

	@Inject
	private ExecutorService executorService;

	@Inject
	protected EntityManager entityManager;

	@Inject
	private LogResultsWorkitemHandler logResWIH;

	@Inject
	private SyncEJBDispatcherWorkitemHandler syncEJBDispatcherWIH;

	@Override
	public Map<String, WorkItemHandler> getWorkItemHandlers(
			final String identifier, final Map<String, Object> params) {
		final Map<String, WorkItemHandler> workItemHandlers = new HashMap<String, WorkItemHandler>();

		System.setProperty("org.kie.executor.pool.size", "10");

		// executorService.setThreadPoolSize(10);
		if (!executorService.isActive()) {
			LOG.info("Initializing ExecutorService.");
			executorService.init();
		}

		AsyncWorkItemHandler asyncwih = new AsyncWorkItemHandler(
				executorService,
				"org.jbpm.examples.commands.ConsumeRestWSCommand");
		workItemHandlers.put("restWS", asyncwih);

		AsyncEJBWorkItemHandler EJBasyncWIH = new AsyncEJBWorkItemHandler(
				executorService,
				"it.prisma.businesslayer.orchestrator.bpm.commands.EJBDispatcherCommand");

		workItemHandlers.put("asyncEJB", EJBasyncWIH);

		SyncEJBWorkItemHandler syncEJBWorkItemHandler = new SyncEJBWorkItemHandler();
		workItemHandlers.put("syncEJB", syncEJBWorkItemHandler);

		CallbackWorkitemHandler callbackWIH = new CallbackWorkitemHandler();
		workItemHandlers.put("callbackTask", callbackWIH);

		// LogResultsWorkitemHandler logResWIH = new
		// LogResultsWorkitemHandler();
		workItemHandlers.put("logResultsTask", logResWIH);

		workItemHandlers.put("syncEJBDispatcherTask", syncEJBDispatcherWIH);

		return workItemHandlers;
	}

}
