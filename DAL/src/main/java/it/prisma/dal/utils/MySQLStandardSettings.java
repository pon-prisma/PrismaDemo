package it.prisma.dal.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MySQLStandardSettings {
	
	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date getCurrentDate() {
		return new Date();
	}

	public static String getCurrentDateAsString() {
		Date date = new Date();
		return MySQLStandardSettings.getDateFormat().format(date).toString();
	}
	
}
