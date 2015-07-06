package it.prisma.presentationlayer.webui.vos.platformconfig;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * This class should be used to create html select.
 *  
 * 
 * @author Reply
 *
 */

public class SelectHelper {
	
	//Map<String, String> selectElem;
	
	public static Map<String, String> generateSelectElementsWithoutKeys(String databaseValue){
		Map<String, String> selectElem = new LinkedHashMap<String, String>();
		String[] splitted = databaseValue.split("-");
		for(String s : splitted){
			String key = s;
			if(s.contains("(") && s.contains(")")){
				key = s.substring(0, s.indexOf("(") -1).trim();
			}
			String value = s.substring(s.indexOf("(")+1, s.lastIndexOf(")"));
			selectElem.put(key.replaceAll(" ", "").toLowerCase().trim(), value);
		}
		return selectElem;
	}
	
	public static Map<String, String> generateSelectElements(String databaseValue){
		Map<String, String> selectElem = new LinkedHashMap<String, String>();
		String[] splitted = databaseValue.split("-");
		for(String s : splitted){
			String key = s;
			if(s.contains("(") && s.contains(")")){
				key = s.substring(0, s.indexOf("(") -1).trim();
			}
			selectElem.put(key.replaceAll(" ", "").toLowerCase().trim(), s);
		}
		return selectElem;
	}
}
