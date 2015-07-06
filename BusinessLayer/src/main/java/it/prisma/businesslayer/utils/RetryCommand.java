package it.prisma.businesslayer.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Retries the given command.
 * 
 * @author l.biava
 *
 * @param <T>
 *            the return type of the command.
 */
public abstract class RetryCommand<T> {

	/**
	 * Exception during command retry.
	 * 
	 * @author l.biava
	 *
	 */
	public static class RetryCommandException extends RuntimeException {

		private static final long serialVersionUID = 9057924124456152810L;

		private RetryCommand<?> command;
		private Throwable lastCause;

		/**
		 * @param message
		 */
		public RetryCommandException(String message, RetryCommand<?> command,
				Throwable lastCause) {
			super(message);
			this.command = command;
			this.lastCause = lastCause;
		}

		public RetryCommand<?> getCommand() {
			return command;
		}

		public Throwable getLastCause() {
			return lastCause;
		}

		@Override
		public String toString() {
			return "RetryCommandException [command=" + command + ", lastCause="
					+ lastCause + "]";
		}
	}

	private static final Logger LOG = LogManager.getLogger(RetryCommand.class);

	public static final int DEFAULT_MAX_RETRIES = 3;
	/**
	 * In ms.
	 */
	public static final int DEFAULT_RETRY_DELAY = 5000;

	private int maxRetries = DEFAULT_MAX_RETRIES;
	private int retryDelay = DEFAULT_RETRY_DELAY;

	/**
	 * Default
	 */
	public RetryCommand() {
	}

	/**
	 * @param maxRetries
	 * @param retryDelay
	 */
	public RetryCommand(int maxRetries, int retryDelay) {
		this.maxRetries = maxRetries;
		this.retryDelay = retryDelay;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public int getRetryDelay() {
		return retryDelay;
	}

	public void setRetryDelay(int retryDelay) {
		this.retryDelay = retryDelay;
	}

	/**
	 * This is the command logic to be implemented.
	 * 
	 * @return
	 */
	public abstract T command(Object... args) throws Exception;

	/**
	 * Runs the command and retries for maxRetries times in case of Exception
	 * thrown.
	 * 
	 * @return
	 * @throws RuntimeException
	 */
	public final T run(Object... args) throws RetryCommandException {
		int retryCounter = 0;
		String cmdName = this.getClass().getCanonicalName();
		Throwable lastCause = null;
		do {
			try {
				return command(args);
			} catch (Exception e) {
				lastCause = e;
				retryCounter++;

				LOG.warn("Failed running command [" + cmdName
						+ "]. Will be retried " + (maxRetries - retryCounter)
						+ " more times, waiting " + retryDelay + " ms.", e);

				// Wait for the retry delay
				if (retryCounter < maxRetries)
					try {
						if (retryDelay > 0)
							Thread.sleep(retryDelay);
					} catch (Exception e2) {
					}
			}
			// Retry until reach max retries			
		} while (retryCounter < maxRetries);
		
		throw new RetryCommandException("Command [" + cmdName
				+ "] failed for all " + maxRetries + " retries", this,
				lastCause);
	}
}