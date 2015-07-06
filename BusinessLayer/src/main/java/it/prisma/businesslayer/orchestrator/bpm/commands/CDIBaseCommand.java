package it.prisma.businesslayer.orchestrator.bpm.commands;

import it.prisma.businesslayer.orchestrator.bpm.OrchestratorContext;
import it.prisma.businesslayer.utils.BeanManagerHelper;
import it.prisma.businesslayer.utils.CustomLogger;
import it.prisma.businesslayer.utils.CustomLoggerFactory;

import org.kie.internal.executor.api.Command;

/**
 * This class gets the reference to the OrchestratorContext to re-enable CDI in
 * executor context. jBPM Executor's command should subclass this.
 * 
 * @author l.biava
 * 
 */
public abstract class CDIBaseCommand implements Command {

	protected CustomLogger logger;
	protected OrchestratorContext orchestratorContext;

	public CDIBaseCommand() throws Exception {

		logger = CustomLoggerFactory.getLogger(this.getClass());
		try {
			orchestratorContext = (OrchestratorContext) BeanManagerHelper
					.getFromJNDIGlobalThisApp("OrchestratorContext");

			if (orchestratorContext == null)
				throw new Exception();
		} catch (Exception e) {
			logger.fatal("FATAL ERROR: Cannot have access to OrchestratorContext from "
					+ this.getClass());
			throw new Exception(
					"FATAL ERROR: Cannot have access to OrchestratorContext from "
							+ this.getClass(), e);
		}
	}

}
