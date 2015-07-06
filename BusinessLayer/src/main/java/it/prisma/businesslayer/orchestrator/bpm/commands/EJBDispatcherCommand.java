package it.prisma.businesslayer.orchestrator.bpm.commands;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.IEJBCommand;

import javax.ejb.Local;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

/**
 * This class implements a jBPM Executor Command which works as a dispatcher to
 * load EJB commands. <br/>
 * To successfully load (and execute) an EJB command it is required a parameter
 * <code>EJBCommandClass</code> containing the FQCN of the EJB command to load. <br/>
 * The EJB Command must also implement {@link IEJBCommand} interface.
 * 
 * @author l.biava
 * 
 */
@Local(EJBDispatcherCommand.class)
public class EJBDispatcherCommand extends CDIBaseCommand {

	private static final Logger LOG = LogManager
			.getLogger(EJBDispatcherCommand.class);

	public EJBDispatcherCommand() throws Exception {
		super();
	}

	@Override
	public ExecutionResults execute(CommandContext ctx) throws Exception {

		WorkItem workItem = (WorkItem) ctx.getData("workItem");
		String EJBCommandClass = (String) workItem
				.getParameter("EJBCommandClass");

		if (EJBCommandClass == null || EJBCommandClass.equals("")) {
			LOG.warn("Using dummy command for empty EJBCommandClass ! Context: "+ctx);
			return new ExecutionResults();
		}

		IEJBCommand ejbCommand = orchestratorContext
				.getCommand(EJBCommandClass);

		return ejbCommand.execute(ctx);
	}
}
