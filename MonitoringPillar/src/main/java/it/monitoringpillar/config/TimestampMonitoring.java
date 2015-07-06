package it.monitoringpillar.config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.naming.NamingException;

public class TimestampMonitoring {

	public String decodUnixTime2Date(long timestamp) throws NamingException {

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date timeNew =new Date();
		timeNew =new java.util.Date((long)timestamp*1000);	
		return sdfDate.format(timeNew);
	}

	public long encodeDate2UnitTime(String time) throws NamingException{
//	public static void main(String [] args) { 
		
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String time = "2015-03-21 12:00:00";
		dfm.setTimeZone(TimeZone.getTimeZone("UTC+1:00" + time));

	    long unixtime = 0;
	    
		try {
			unixtime = dfm.parse(time).getTime();
			unixtime=unixtime/1000;
//	        System.out.println(unixtime);
//	        System.out.println(TimeZone.getTimeZone("UTC"));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new NamingException("Impossible to parser the time");
		}
		return unixtime;  
	}
}