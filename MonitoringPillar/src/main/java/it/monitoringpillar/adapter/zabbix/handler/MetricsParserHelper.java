package it.monitoringpillar.adapter.zabbix.handler;

public class MetricsParserHelper {

	//To be used only in Paas platform where float values are supposed to be used
	public static Float getMetricParsedValue(String metricValueType, String metricValue){ 

		if (metricValueType.equals("0")) {
			metricValueType = "FLOAT";
			return Float.parseFloat(metricValue);
		} 

		else if (metricValueType.equals("3")) {
			metricValueType = "NUMERIC UNSIGNED";
			try{
				return Float.parseFloat(metricValue);
			}
			catch(NumberFormatException nfe){
				throw new NumberFormatException();
			}
		}
		else if (metricValueType.equals("4")) {
			metricValueType = "TEXT";
			try{
				return Float.parseFloat(metricValue);
			} catch(NumberFormatException nfe){
				throw new NumberFormatException();
			}
		}
		else throw new IllegalArgumentException("Not existing passed argument type");

	}	

	//IN Case of Server infrastructure one has to manage all the situations (types of values..)
	// that's why it return an object
	public static Object getMetricIaaSParsedValue(String metricValueType, String metricValue){ 

		if (metricValueType.equals("0")) {
			metricValueType = "FLOAT";
			return Float.parseFloat(metricValue);
		} 
		else if (metricValueType.equals("1")) {
			metricValueType = "CHARACTER";
			return metricValue; 
		} 
		else if (metricValueType.equals("2")) {
			metricValueType = "LOG";
			return metricValue;
		}
		else if (metricValueType.equals("3")) {
			metricValueType = "NUMERIC UNSIGNED";
			try{
				return Float.parseFloat(metricValue);
			}
			catch(NumberFormatException nfe){
				throw new NumberFormatException();
			}
		} 
		else if (metricValueType.equals("4")) {
			metricValueType = "TEXT";
			return metricValue;
		}
		else throw new IllegalArgumentException("Not existing passed argument type");

	}
}
