package it.prisma.businesslayer.orchestrator.bpm.ejbcommands;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.businesslayer.orchestrator.bpm.WIHs.misc.SignalEvent;
import it.prisma.businesslayer.orchestrator.bpm.WIHs.misc.SignalEvent.SignalEventType;
import it.prisma.businesslayer.orchestrator.logging.LRTEventLogger;
import it.prisma.businesslayer.utils.CustomLogger;
import it.prisma.businesslayer.utils.CustomLoggerFactory;
import it.prisma.dal.entities.orchestrator.lrt.LRT;
import it.prisma.domain.dsl.prisma.ErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.Error;
import it.prisma.utils.misc.StackTrace;

import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.kie.api.runtime.process.WorkItem;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for command implementation.<br/>
 * This sets up logger and some variables from the Executor command context. It
 * also manages logging at the start and end of command and provides helper
 * methods for error and result handling.
 * 
 * @author l.biava
 * 
 */
// @Local(IEJBCommand.class)
public abstract class BaseCommand implements IEJBCommand {

	private static final Logger LOG = LoggerFactory
			.getLogger(BaseCommand.class);

	protected WorkItem workItem;
	protected LRT lrt;
	protected long procInstanceId;

	protected CustomLogger logger;

	@Inject
	protected LRTEventLogger lrtEventLogger;

	@Inject
	protected EnvironmentConfig envConfig;

	// @Inject
	// protected LRTDAO lrtDAO;

	public BaseCommand() throws Exception {
		logger = CustomLoggerFactory.getLogger(this.getClass());
	}

	/**
	 * Must implement custom execution logic.
	 * 
	 * @param ctx
	 *            - contextual data given by the executor service
	 * @return returns any results in case of successful execution
	 * @throws Exception
	 *             in case execution failed and shall be retried if possible
	 */
	protected abstract ExecutionResults customExecute(CommandContext ctx)
			throws Exception;

	@Resource
	private UserTransaction userTx;

	/**
	 * <b>This method SHOULD NOT be overridden !</b> <br/>
	 * Use the {@link BaseCommand#customExecute(CommandContext)} method for the
	 * command logic.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ExecutionResults execute(CommandContext ctx) throws Exception {

		// TODO: Check why on Earth there could be no transaction !!!!
		// Hints: Inner WF...
		boolean userTxRequired = false;
		if (userTx.getStatus() == Status.STATUS_NO_TRANSACTION) {
			userTxRequired = true;
			userTx.begin();
		}

		workItem = (WorkItem) ctx.getData("workItem");
		lrt = (LRT) workItem.getParameter("LRT");
		procInstanceId = (Long) ctx.getData("processInstanceId");

		logCommandStarted(workItem.getParameters());

		ExecutionResults exRes = customExecute(ctx);

		logCommandEnded(exRes);

		if (userTxRequired)
			if (userTx.getStatus() == Status.STATUS_MARKED_ROLLBACK)
				userTx.rollback();
			else if (userTx.getStatus() != Status.STATUS_ROLLING_BACK
					&& userTx.getStatus() != Status.STATUS_ROLLEDBACK)
				userTx.commit();

		return exRes;
	}

	/**
	 * Logs command started info.
	 */
	protected void logCommandStarted(Map<String, Object> params) {
		// Logging into Prisma Log
		String WIId = "TBD";
		String tag = "(Task: " + this.getClass().getName() + ", PID: "
				+ procInstanceId + ", WIId: " + WIId + ", params: " + params
				+ ") - ";
		logger.setTag(tag);
		//System.out.println(tag + "STARTED");
		logger.info("STARTED");
	}

	/**
	 * Logs command ended info.
	 */
	protected void logCommandEnded(ExecutionResults exResults) {
		// Logging into Prisma Log
		try {
			logger.info("ENDED, ResultStatus("
					+ exResults.getData("ResultStatus") + "), Result("
					+ exResults.getData("Result") + ")");
//			LOG.info(logger.getTag() + "ENDED, ResultStatus("
//					+ exResults.getData("ResultStatus") + "), Result("
//					+ exResults.getData("Result") + ")");
		} catch (Exception ex) {
			LOG.warn("Cannot log EJBCommand result.", ex);
		}
	}

	/**
	 * Handles system exception (for instance during Rest WS consume:
	 * ServerErrorException, RestClientException, MappingException, ...) logging
	 * the exception in Prisma log and preparing the Error output variable.
	 * 
	 * @param ex
	 *            the Exception.
	 * @param defaultErrorCode
	 *            the {@link ErrorCode} to be returned.
	 * @param exResults
	 *            {@link ExecutionResults} of the command for creating the Error
	 *            output variable.
	 */
	protected void handleSystemException(Exception ex,
			ErrorCode defaultErrorCode, ExecutionResults exResults) {
		//ex.printStackTrace();

		errorOccurred(defaultErrorCode, StackTrace.getStackTraceToString(ex),
				exResults);

		/*
		 * lrtEventLogger.log(LRTEventType.ERROR, lrt, "CONNECTION_ERROR: " +
		 * ex.getMessage());
		 */
	}

	/**
	 * Helper method to log the error in Prisma logging system and preparing the
	 * Error output variable.
	 * 
	 * @param wfError
	 *            the {@link ErrorCode} to be returned.
	 * @param verbose
	 *            the verbose data for the error.
	 * @param exResults
	 *            {@link ExecutionResults} of the command for creating the Error
	 *            output variable.
	 */
	protected ExecutionResults errorOccurred(ErrorCode wfError, String verbose,
			ExecutionResults exResults) {
		return errorOccurred(wfError, verbose, exResults, null);
	}

	/**
	 * Helper method to log the error in Prisma logging system and preparing the
	 * Error output variable.
	 * 
	 * @param wfError
	 *            the {@link ErrorCode} to be returned.
	 * @param verbose
	 *            the verbose data for the error.
	 * @param exResults
	 *            {@link ExecutionResults} of the command for creating the Error
	 *            output variable.
	 * @param t
	 *            a {@link Throwable} to print the stack trace in the error log.
	 */
	protected ExecutionResults errorOccurred(ErrorCode wfError, String verbose,
			ExecutionResults exResults, Throwable t) {
		it.prisma.domain.dsl.prisma.prismaprotocol.Error error = new Error(
				wfError);
		error.setVerbose(verbose);

		exResults.setData("ResultStatus", "ERROR");
		exResults.setData("Error", error);
		exResults.setData("SignalEvent", new SignalEvent(SignalEventType.ERROR,
				"My_Error"));

		// TODO: Migliorare logging su log prisma
		if (t != null)
			logger.error("ERROR: " + error, t);
		else
			logger.error("ERROR: " + error);

		// TODO: Decidere se deve essere loggato qui o altro l'evento
		/*
		 * try { lrtEventLogger.logFinalResult(LRTEventType.ERROR, lrt, error);
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		return exResults;
	}

	/**
	 * Helper method to set the result output variable of the command.
	 * 
	 * @param result
	 *            the result data.
	 * @param exResults
	 *            {@link ExecutionResults} of the command for creating the
	 *            result output variable.
	 */
	protected <ResultType> void resultOccurred(ResultType result,
			ExecutionResults exResults) {
		exResults.setData("ResultStatus", "OK");
		exResults.setData("Result", result);
	}
}
