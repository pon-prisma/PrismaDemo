package it.prisma.businesslayer.utils;

import it.prisma.utils.json.JsonUtility;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

public class CustomLoggerImpl extends CustomLogger {

	public CustomLoggerImpl(Logger logger, Class<?> clazz) {
		super(logger,clazz);
	}

	public Logger getLogger() {
		return logger;
	}

	@Override
	public void fatal(String message) {
		logger.fatal(tag+message);
	}

	@Override
	public void fatal(Object message) {
		String text;
		try {
			text = JsonUtility.serializeJson(message);
			logger.fatal(tag+text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void error(String message) {
		logger.error(tag+message);
	}
	
	@Override
	public void error(String message, Throwable cause) {
		logger.error(tag+message, cause);
	}

	@Override
	public void error(Object message) {
		String text;
		try {
			text = JsonUtility.serializeJson(message);
			logger.error(tag+text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void info(String message) {
		logger.info(tag+message);
	}

	@Override
	public void info(Object message) {
		String text;
		try {
			text = JsonUtility.serializeJson(message);
			logger.info(tag+text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void warn(String message) {
		logger.warn(tag+message);
	}

	@Override
	public void warn(Object message) {
		String text;
		try {
			text = JsonUtility.serializeJson(message);
			logger.warn(tag+text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
