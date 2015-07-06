package it.prisma.businesslayer.utils;

import org.apache.logging.log4j.LogManager;

public class CustomLoggerFactory {
	public static <ClassType> CustomLogger getLogger(Class<ClassType> clazz) {
		return new CustomLoggerImpl(LogManager.getLogger(clazz),clazz);
	}
}
